package net.galacticprojects.isystem.database;

import com.zaxxer.hikari.pool.HikariPool;
import net.galacticprojects.isystem.bungeecord.cache.Cache;
import net.galacticprojects.isystem.bungeecord.cache.ThreadSafeCache;
import net.galacticprojects.isystem.bungeecord.command.ban.BanType;
import net.galacticprojects.isystem.utils.JavaInstance;
import net.galacticprojects.isystem.utils.MojangProfileService;
import net.galacticprojects.isystem.utils.TimeHelper;
import net.galacticprojects.isystem.database.model.Ban;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MySQL {

    @SuppressWarnings("rawtypes")
    private static final class SQLTimer extends TimerTask {
        private final Cache[] caches;

        public SQLTimer(final Cache... caches) {
            this.caches = caches;
        }

        @Override
        public void run() {
            for (final Cache cache : caches) {
                cache.tick();
            }
        }
    }

    private final HikariPool pool;

    private final Timer timer;

    private final Cache<UUID, Ban> banCache = new ThreadSafeCache<>(UUID.class, 300);

    private final ExecutorService service = Executors.newCachedThreadPool();

    public MySQL(final IPoolProvider provider) {
        this.pool = Objects.requireNonNull(provider, "Provider can't be null").createPool();
        this.timer = new Timer();
        JavaInstance.put(this);
        timer.scheduleAtFixedRate(new SQLTimer(banCache), 0, 1000);
        try (Connection connection = pool.getConnection()) {
            System.out.println("[SQLDatabase] Successfully connected!");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("[SQLDatabase] A SQLException occurred! Please ignore this Error!!");
        }
    }

    public void shutdown() {
        System.out.println("[SQLDatabase] Trying to disconnect database...");
        try {
            pool.shutdown();
            System.out.println("[SQLDatabase] Successfully disconnected!");
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println("[SQLDatabase] An interrupted Exception ocurred \n " + e.getMessage());
        }
    }

    public void reload() {
        banCache.clear();
        shutdown();
        try (Connection connection = pool.getConnection()) {
            System.out.println("[SQLDatabase] Successfully reconnected");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("[SQLDatabase] A SQLException occurred! Please ignore this Error!!");
        }
    }

    public void createTables() {
        try (Connection connection = pool.getConnection()) {

            PreparedStatement st = connection.prepareStatement("CREATE TABLE IF NOT EXISTS PlayerBans(ID INT NOT NULL AUTO_INCREMENT PRIMARY KEY, PLAYER VARCHAR(36), STAFF VARCHAR(36), IP VARCHAR(16), REASON VARCHAR(100), TYPE VARCHAR(12), ENDTIME VARCHAR(32), CREATIONTIME VARCHAR(32), DURATION VARCHAR(20))");
            PreparedStatement statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS PlayerData()");
            PreparedStatement state = connection.prepareStatement("CREATE TABLE IF NOT EXISTS Clans()");
            PreparedStatement states = connection.prepareStatement("CREATE TABLE IF NOT EXISTS PlayerFriends()");

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("[SQLDatabase] A SQLException occurred! Please ignore this Error!!");
        }
    }

    public CompletableFuture<Ban> createBan(final UUID player, final String staff, String ip, final String reason, BanType type, final OffsetDateTime time, final OffsetDateTime creationTime, int duration) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = pool.getConnection()) {
                final PreparedStatement statement = connection.prepareStatement("INSERT INTO PlayerBans (PLAYER, STAFF, IP, REASON, TYPE, ENDTIME, CREATIONTIME, DURATION) VALUES (?, ?, ?, ?, ?, ?, ?, ?);");
                statement.setString(1, player.toString());
                statement.setString(2, staff);
                statement.setString(3, ip);
                statement.setString(4, reason);
                statement.setString(5, type.name());
                statement.setString(6, TimeHelper.toString(time));
                statement.setString(7, TimeHelper.toString(creationTime));
                statement.setInt(8, duration);
                statement.executeUpdate();
                Ban ban = new Ban(player, staff, ip, reason, type, time, creationTime, duration);
                banCache.put(player, ban);
                return ban;
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("[SQLDatabase] A SQLException occurred! Please ignore this Error!!");
            }
            return null;
        }, service);
    }

    public CompletableFuture<Ban> updateBan(final UUID player, final String staff, String ip, final String reason, BanType type, final OffsetDateTime endtime, final OffsetDateTime creationTime, int duration) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = pool.getConnection()) {
                PreparedStatement state = connection.prepareStatement("DELETE * FROM PlayerBans WHERE UUID = ?");
                state.setString(1, player.toString());
                state.executeUpdate();
                final PreparedStatement statement = connection.prepareStatement("INSERT INTO PlayerBans (PLAYER, STAFF, IP, REASON, TYPE, ENDTIME, CREATIONTIME, DURATION) VALUES (?, ?, ?, ?, ?, ?, ?, ?);");
                statement.setString(1, player.toString());
                statement.setString(2, staff);
                statement.setString(3, ip);
                statement.setString(4, reason);
                statement.setString(5, type.name());
                statement.setString(6, TimeHelper.toString(endtime));
                statement.setString(7, TimeHelper.toString(creationTime));
                statement.setInt(8, duration);
                statement.executeUpdate();
                Ban ban = new Ban(player, staff, ip, reason, type, endtime, creationTime, duration);
                banCache.put(player, ban);
                return ban;
            } catch(SQLException e) {
                e.printStackTrace();
                System.out.println("[SQLDatabase] A SQLException occurred! Please ignore this Error!!");
                return null;
            }
        }, service);
    }

    public CompletableFuture<Ban> getBanned(final UUID player) {
        return CompletableFuture.supplyAsync(() -> {
            if (banCache.has(player)) {
                return banCache.get(player);
            }
            try (Connection connection = pool.getConnection()) {
                final PreparedStatement statement = connection.prepareStatement("SELECT * FROM PlayerBans WHERE UUID = ?");
                statement.setString(1, player.toString());
                ResultSet set = statement.executeQuery();
                if (set.next()) {
                    String staff = set.getString("STAFF");
                    String ip = set.getString("IP");
                    String reason = set.getString("REASON");
                    BanType type = BanType.fromString(set.getString("TYPE"));
                    OffsetDateTime endTime = TimeHelper.fromString(set.getString("ENDTIME"));
                    OffsetDateTime creationTime = TimeHelper.fromString(set.getString("CREATIONTIME"));
                    int duration = set.getInt("DURATION");
                    Ban ban = new Ban(player, staff, ip, reason, type, endTime, creationTime, duration);
                    banCache.put(player, ban);
                    return ban;
                }
                return null;
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("[SQLDatabase] A SQLException occurred! Please ignore this Error!!");
                return null;
            }
        }, service);
    }

    public CompletableFuture<Void> deleteBan(final UUID player) {
        return CompletableFuture.runAsync(() -> {
            try (Connection connection = pool.getConnection()) {
                PreparedStatement statement = connection.prepareStatement("DELETE * FROM PlayerBans WHERE UUID = ?");
                statement.setString(1, player.toString());
                statement.executeUpdate();
            }catch(SQLException e) {
                e.printStackTrace();
                System.out.println("[SQLDatabase] A SQLException occurred! Please ignore this Error!!");
            }
        }, service);
    }

}
