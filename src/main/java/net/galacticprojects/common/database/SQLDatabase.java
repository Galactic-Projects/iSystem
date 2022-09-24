package net.galacticprojects.common.database;

import com.zaxxer.hikari.pool.HikariPool;
import io.netty.util.concurrent.BlockingOperationException;
import me.lauriichan.laylib.logger.ISimpleLogger;
import me.lauriichan.minecraft.wildcard.migration.IMigrationSource;
import net.galacticprojects.common.database.migration.impl.MigrationManager;
import net.galacticprojects.common.util.TimeHelper;
import net.galacticprojects.common.util.cache.Cache;
import net.galacticprojects.common.util.cache.ThreadSafeCache;
import net.galacticprojects.common.database.migration.impl.SQLMigrationType;
import net.galacticprojects.common.database.model.Ban;
import net.galacticprojects.common.database.model.Player;
import net.galacticprojects.common.util.Ref;
import org.h2.util.DateTimeUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
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
            .format("INSERT INTO `%s` (Player, Owner, Id, Reason, Time, CreationTime) VALUES (?,?,?,?,?,?)", SQLTable.BAN_TABLE);

    public static final String SELECT_PLAYER = String.format("SELECT * FROM `%s` WHERE Player = ?", SQLTable.PLAYER_TABLE);
    public static final String DELETE_PLAYER = String.format("DELETE FROM `%s` WHERE Player = ?", SQLTable.PLAYER_TABLE);
    public static final String INSERT_PLAYER = String
            .format("INSERT INTO `%s` (Player, Ip, Coins, Level, Language, OnlineTime) VALUES (?, ?, ?, ?, ?, ?)", SQLTable.PLAYER_TABLE);

    private final HikariPool pool;

    private final Timer timer;

    private final Ref<List<Cache<?, ?>>> caches = Ref.of(new ArrayList<>());
    private final ExecutorService service = Executors.newCachedThreadPool();

    private final Cache<UUID, Ban> banCache = newCache(UUID.class, 300);
    private final Cache<UUID, Player> playerCache = newCache(UUID.class, 300);
    private final ISimpleLogger logger;

    public SQLDatabase(final ISimpleLogger logger, final IPoolProvider provider) {
        caches.get();
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

    public CompletableFuture<Player> createPlayer(UUID uniqueId, String ip, int coins, int level, String language, long onlineTime) {
        return CompletableFuture.supplyAsync(() -> {
            try(Connection connection = pool.getConnection()) {
                PreparedStatement statement = connection.prepareStatement(INSERT_PLAYER);
                statement.setString(1, uniqueId.toString());
                statement.setString(2, ip);
                statement.setInt(3, coins);
                statement.setInt(4, level);
                statement.setString(5, language);
                statement.setLong(6, onlineTime);
                statement.execute();
                Player player = new Player(uniqueId, ip, onlineTime, coins, language, level);
                playerCache.put(uniqueId, player);
                return player;
            } catch (SQLException e) {
                logger.warning("A few SQL things went wrong while creating Player", e);
                e.printStackTrace();
                return null;
            }

        }, service);
    }

    public CompletableFuture<Player> getPlayer(UUID uniqueId) {
        return CompletableFuture.supplyAsync(() -> {
            if(playerCache.has(uniqueId)) {
                return playerCache.get(uniqueId);
            }
            try(Connection connection = pool.getConnection()) {
                PreparedStatement statement = connection.prepareStatement(SELECT_PLAYER);
                statement.setString(1, uniqueId.toString());
                ResultSet set = statement.executeQuery();
                if(set.next()) {
                    String ip = set.getString("Ip");
                    int coins = set.getInt("Coins");
                    int level = set.getInt("Level");
                    String language = set.getString("Language");
                    long onlineTime = set.getLong("OnlineTime");
                    Player player = new Player(uniqueId, ip, onlineTime, coins, language, level);
                    playerCache.put(uniqueId, player);
                    return player;
                }
                return null;
            }catch(SQLException e) {
                logger.warning("A few SQL things went wrong while get Player", e);
                e.printStackTrace();
                return null;
            }
        }, service);
    }

    public CompletableFuture<Boolean> deletePlayer(UUID uniqueId) {
        return CompletableFuture.supplyAsync(() -> {
            try(Connection connection = pool.getConnection()) {
                PreparedStatement statement = connection.prepareStatement(DELETE_PLAYER);
                statement.setString(1, uniqueId.toString());
                statement.executeUpdate();
                return true;
            }catch (SQLException e) {
                logger.warning("A few SQL things went wrong while delete Player", e);
                e.printStackTrace();
                return false;
            }
        }, service);
    }

    public CompletableFuture<Ban> banPlayer(final UUID uniqueId, final UUID owner, final int id, final String reason, final OffsetDateTime time, final OffsetDateTime creationTime) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = pool.getConnection()) {
                PreparedStatement statement = connection.prepareStatement(INSERT_PLAYER_BAN);
                statement.setString(1, uniqueId.toString());
                statement.setString(2, owner.toString());
                statement.setInt(3, id);
                statement.setString(4, reason);
                statement.setString(5, TimeHelper.toString(time));
                statement.setString(6, TimeHelper.toString(creationTime));
                statement.execute();
                Ban ban = new Ban(uniqueId, owner, id, reason, time, creationTime);
                banCache.put(uniqueId, ban);
                return ban;
            } catch(SQLException e) {
                logger.warning("A few SQL things went wrong while ban Player", e);
                e.printStackTrace();
                return null;
            }
        }, service);
    }

    public CompletableFuture<Boolean> deleteBan(final UUID uniqueId) {
        return CompletableFuture.supplyAsync(() -> {
            try(Connection connection = pool.getConnection()) {
                PreparedStatement statement = connection.prepareStatement(DELETE_PLAYER_BAN);
                statement.setString(1, uniqueId.toString());
                statement.executeUpdate();
                if(banCache.has(uniqueId)) {
                    banCache.remove(uniqueId);
                }
                return true;
            } catch (SQLException e) {
                logger.warning("A few SQL things went wrong while delete ban", e);
                e.printStackTrace();
                return false;
            }
        }, service);
    }

    public CompletableFuture<Ban> getBan(final UUID uniqueId) {
        return CompletableFuture.supplyAsync(() -> {
            if(banCache.has(uniqueId)) {
                return banCache.get(uniqueId);
            }
            try (Connection connection = pool.getConnection()) {
                PreparedStatement statement = connection.prepareStatement(SELECT_PLAYER_BAN);
                statement.setString(1, uniqueId.toString());
                ResultSet set = statement.executeQuery();
                if(set.next()) {
                    UUID owner = UUID.fromString(set.getString("Owner"));
                    int id = set.getInt("Id");
                    String reason = set.getString("Reason");
                    OffsetDateTime time = TimeHelper.fromString(set.getString("Time"));
                    OffsetDateTime creationTime = TimeHelper.fromString(set.getString("CreationTime"));
                    Ban ban = new Ban(uniqueId, owner, id, reason, time, creationTime);
                    banCache.put(uniqueId, ban);
                    return ban;
                }
                return null;
            } catch (SQLException e) {
                logger.warning("A few SQL things went wrong while get ban", e);
                e.printStackTrace();
                return null;
            }
        }, service);
    }

}
