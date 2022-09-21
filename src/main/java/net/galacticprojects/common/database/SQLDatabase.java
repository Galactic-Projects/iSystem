package net.galacticprojects.common.database;

import com.zaxxer.hikari.pool.HikariPool;
import me.lauriichan.laylib.logger.ISimpleLogger;
import me.lauriichan.minecraft.wildcard.migration.IMigrationSource;
import net.galacticprojects.common.database.migration.impl.MigrationManager;
import net.galacticprojects.common.util.cache.Cache;
import net.galacticprojects.common.util.cache.ThreadSafeCache;
import net.galacticprojects.common.database.migration.impl.SQLMigrationType;
import net.galacticprojects.common.database.model.Ban;
import net.galacticprojects.common.database.model.Player;
import net.galacticprojects.common.util.Ref;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class SQLDatabase implements IMigrationSource {

    @SuppressWarnings("rawtypes")
    private static final class SQLTimer extends TimerTask {
        private final List<Cache<?, ?>> caches;

        public SQLTimer(final List<Cache<?, ?>> caches) {
            this.caches = caches;
        }

        @Override
        public void run() {
            for (final Cache cache : caches) {
                cache.tick();
            }
        }
    }

    private <A, B> ThreadSafeCache<A, B> newCache(Class<A> clazz, int cacheTime){
        if(caches.isLocked()) {
            return null;
        }
        ThreadSafeCache<A, B> cache = new ThreadSafeCache<>(clazz, cacheTime);
        caches.get().add(cache);
        return cache;
    }

    public static final String SELECT_PLAYER_BAN = String.format("SELECT * FROM `%s` WHERE Player = ?", SQLTable.BAN_TABLE);
    public static final String DELETE_PLAYER_BAN = String.format("DELETE FROM `%s` WHERE Player = ?", SQLTable.BAN_TABLE);
    public static final String INSERT_PLAYER_BAN = String
            .format("INSERT INTO `%s` (Player, Owner, Reason, Time, CreationTime) VALUES (?,?,?,?,?)", SQLTable.BAN_TABLE);

    private final HikariPool pool;

    private final Timer timer;

    private final Cache<UUID, Ban> banCache = newCache(UUID.class, 300);
    private final Cache<UUID, Player> playerCache = newCache(UUID.class, 300);

    private final ExecutorService service = Executors.newCachedThreadPool();

    private final Ref<List<Cache<?, ?>>> caches = Ref.of(new ArrayList<>());

    private final ISimpleLogger logger;

    public SQLDatabase(final ISimpleLogger logger, final IPoolProvider provider) {
        this.logger = logger;
        this.pool = Objects.requireNonNull(provider, "Provider can't be null").createPool();
        this.timer = new Timer();
        timer.scheduleAtFixedRate(new SQLTimer(caches.set(Collections.unmodifiableList(caches.get())).lock().get()), 0, 1000);
        try (Connection connection = pool.getConnection(15000)) {
            final PreparedStatement statement = connection.prepareStatement("/* ping */ SELECT 1");
            statement.setQueryTimeout(15);
            statement.executeQuery();
        } catch (final SQLException exp) {
            shutdown();
            throw new IllegalStateException("MySQL connection test failed", exp);
        }
        try {
            MigrationManager.migrate(this, SQLMigrationType.class);
        } catch (final Throwable e) {
            shutdown();
            throw new IllegalStateException("Failed to run migration of database", e);
        }
    }

    public Connection getConnection() throws SQLException {
        return pool.getConnection();
    }

    public void shutdown() {
        timer.cancel();
        try {
            pool.shutdown();
        } catch (final Throwable e) {
            // Ignore
        }
    }

}
