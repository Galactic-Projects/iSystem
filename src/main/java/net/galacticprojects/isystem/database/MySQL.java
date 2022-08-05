package net.galacticprojects.isystem.database;

import com.zaxxer.hikari.pool.HikariPool;
import net.galacticprojects.isystem.bungeecord.cache.Cache;
import net.galacticprojects.isystem.bungeecord.cache.ThreadSafeCache;
import net.galacticprojects.isystem.bungeecord.command.ban.BanType;
import net.galacticprojects.isystem.database.model.Player;
import net.galacticprojects.isystem.utils.JavaInstance;
import net.galacticprojects.isystem.utils.Languages;
import net.galacticprojects.isystem.utils.MojangProfileService;
import net.galacticprojects.isystem.utils.TimeHelper;
import net.galacticprojects.isystem.database.model.Ban;

import javax.xml.transform.Result;
import java.io.IOException;
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
    private final Cache<UUID, Player> playerCache = new ThreadSafeCache<>(UUID.class, 300);

    private final ExecutorService service = Executors.newCachedThreadPool();

    public MySQL(final IPoolProvider provider) {
        this.pool = Objects.requireNonNull(provider, "Provider can't be null").createPool();
        this.timer = new Timer();
        JavaInstance.put(this);
        timer.scheduleAtFixedRate(new SQLTimer(banCache), 0, 1000);
        try (Connection connection = pool.getConnection()) {
            System.out.println("[SQLDatabase] Successfully connected!");
            createTables();
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

            PreparedStatement playersBans = connection.prepareStatement("CREATE TABLE IF NOT EXISTS PlayerBans(ID INT NOT NULL AUTO_INCREMENT PRIMARY KEY, UUID VARCHAR(36), STAFF VARCHAR(36), IP VARCHAR(16), REASON VARCHAR(100), TYPE VARCHAR(12), ENDTIME VARCHAR(256), CREATIONTIME VARCHAR(32), DURATION VARCHAR(20));");
            PreparedStatement playerData = connection.prepareStatement("CREATE TABLE IF NOT EXISTS PlayerData(ID INT NOT NULL AUTO_INCREMENT PRIMARY KEY, UUID VARCHAR(36), NAME VARCHAR(16), IP VARCHAR(16), COUNTRY VARCHAR(64), ONLINETIME LONGTEXT NOT NULL DEFAULT 0, LANGUAGE VARCHAR(86), FIRSTJOIN VARCHAR(256), SERVERONLINE VARCHAR(64) NOT NULL DEFAULT '', LATESTJOIN VARCHAR(256), REPORT BOOLEAN NOT NULL DEFAULT false, TEAMCHAT BOOLEAN NOT NULL DEFAULT false);");
            PreparedStatement clan = connection.prepareStatement("CREATE TABLE IF NOT EXISTS Clans();");
            PreparedStatement clanData = connection.prepareStatement("CREATE TABLE IF NOT EXISTS ClansData();");

            playersBans.executeUpdate();
            playerData.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("[SQLDatabase] A SQLException occurred! Please ignore this Error!!");
        }
    }

    public CompletableFuture<Player> createPlayer(UUID uuid, String name, String ip, String country, long onlineTime, Languages languages, OffsetDateTime firstJoin, String serverOnline, OffsetDateTime latestJoin, boolean report, boolean teamchat) {
        return CompletableFuture.supplyAsync(() -> {
            if (playerCache.has(uuid)) {
                return playerCache.get(uuid);
            }
            try (Connection connection = pool.getConnection()){
                final PreparedStatement statement = connection.prepareStatement("INSERT INTO PlayerData(UUID, NAME, IP, COUNTRY, ONLINETIME, LANGUAGE, FIRSTJOIN, SERVERONLINE, LATESTJOIN, REPORT, TEAMCHAT) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
                statement.setString(1, String.valueOf(uuid));
                statement.setString(2, name);
                statement.setString(3, ip);
                statement.setString(4, country);
                statement.setLong(5, onlineTime);
                statement.setString(6, String.valueOf(languages));
                statement.setString(7, TimeHelper.toString(firstJoin));
                statement.setString(8, serverOnline);
                statement.setString(9, TimeHelper.toString(latestJoin));
                statement.setBoolean(10, report);
                statement.setBoolean(11, teamchat);
                statement.executeUpdate();
                Player player = new Player(uuid, name, ip, country, onlineTime, languages, firstJoin, serverOnline, latestJoin, report, teamchat);
                playerCache.put(uuid, player);
                return player;
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("[SQLDatabase] A SQLException occurred! Please ignore this Error!!");
                return null;
            }
        }, service);
    }

    public CompletableFuture<Player> getPlayer(final UUID uniqueId) {
        return CompletableFuture.supplyAsync(() -> {
            if (playerCache.has(uniqueId)) {
                return playerCache.get(uniqueId);
            }
            try (Connection connection = pool.getConnection()) {
                final PreparedStatement statement = connection.prepareStatement("SELECT * FROM PlayerData WHERE UUID = ?");
                statement.setString(1, uniqueId.toString());
                ResultSet set = statement.executeQuery();
                if (set.next()) {
                    UUID uuid = UUID.fromString(set.getString("UUID"));
                    String name = set.getString("NAME");
                    String ip = set.getString("IP");
                    String country = set.getString("COUNTRY");
                    long onlineTime = set.getLong("ONLINETIME");
                    Languages languages = Languages.valueOf(set.getString("LANGUAGE"));
                    OffsetDateTime firstJoin = TimeHelper.fromString(set.getString("FIRSTJOIN"));
                    String serverOnline = set.getString("SERVERONLINE");
                    OffsetDateTime latestJoin = TimeHelper.fromString(set.getString("LATESTJOIN"));
                    boolean report = set.getBoolean("REPORT");
                    boolean teamchat = set.getBoolean("TEAMCHAT");
                    Player player = new Player(uuid, name, ip, country, onlineTime, languages, firstJoin, serverOnline, latestJoin, report, teamchat);
                    playerCache.put(uuid, player);
                    return player;
                }
                return null;
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("[SQLDatabase] A SQLException occurred! Please ignore this Error!!");
                return null;
            }
        }, service);
    }

    public CompletableFuture<Player> getPlayerFromIp(final String ip) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = pool.getConnection()) {
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM PlayerData WHERE IP = ?");
                statement.setString(1, ip);
                ResultSet set = statement.executeQuery();
                if(set.next()) {
                    UUID uuid = UUID.fromString(set.getString("UUID"));
                    String name = set.getString("NAME");
                    String country = set.getString("COUNTRY");
                    long onlineTime = set.getLong("ONLINETIME");
                    Languages languages = Languages.valueOf(set.getString("LANGUAGE"));
                    OffsetDateTime firstJoin = TimeHelper.fromString(set.getString("FIRSTJOIN"));
                    String serverOnline = set.getString("SERVERONLINE");
                    OffsetDateTime latestJoin = TimeHelper.fromString(set.getString("LATESTJOIN"));
                    boolean report = set.getBoolean("REPORT");
                    boolean teamchat = set.getBoolean("TEAMCHAT");
                    Player player = new Player(uuid, name, ip, country, onlineTime, languages, firstJoin, serverOnline, latestJoin, report, teamchat);
                    playerCache.put(uuid, player);
                    return player;
                }
                return null;
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("[SQLDatabase] A SQLException occurred! Please ignore this Error!!");
                return null;
            }
        }, service);
    }

    public CompletableFuture<Player> updatePlayer(UUID uuid, String name, String ip, String country, Languages languages, OffsetDateTime firstJoin, String serverOnline, OffsetDateTime latestJoin, boolean report, boolean teamchat) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = pool.getConnection()) {
                final PreparedStatement statement = connection.prepareStatement("UPDATE PlayerData SET NAME = ?, IP = ?, COUNTRY = ?, LANGUAGE = ?, FIRSTJOIN = ?, SERVERONLINE = ?, LATESTJOIN = ?, REPORT = ?, TEAMCHAT = ? WHERE UUID = ?");
                statement.setString(1, name);
                statement.setString(2, ip);
                statement.setString(3, country);
                statement.setString(4, String.valueOf(languages));
                statement.setString(5, TimeHelper.toString(firstJoin));
                statement.setString(6, serverOnline);
                statement.setString(7, TimeHelper.toString(latestJoin));
                statement.setBoolean(8, report);
                statement.setBoolean(9, teamchat);
                statement.setString(10, String.valueOf(uuid));
                statement.executeUpdate();
                Player player = new Player(uuid, name, ip, country, playerCache.get(uuid).getOnlineTime(), languages, firstJoin, serverOnline, latestJoin, report, teamchat);
                playerCache.put(uuid, player);
                return player;
            } catch(SQLException e) {
                e.printStackTrace();
                System.out.println("[SQLDatabase] A SQLException occurred! Please ignore this Error!!");
                return null;
            }
        }, service);
    }

    public CompletableFuture<Player> updateServer(UUID uuid, String serverOnline) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = pool.getConnection()) {
                final PreparedStatement statement = connection.prepareStatement("UPDATE PlayerData SET SERVERONLINE = ? WHERE UUID = ?");
                statement.setString(1, serverOnline);
                statement.setString(2, String.valueOf(uuid));
                statement.executeUpdate();
                Player player = new Player(uuid, playerCache.get(uuid).getName(), playerCache.get(uuid).getIP(), playerCache.get(uuid).getIP(), playerCache.get(uuid).getOnlineTime(), playerCache.get(uuid).getLanguages(), playerCache.get(uuid).getFirstJoin(), serverOnline, playerCache.get(uuid).getLatestJoin(), playerCache.get(uuid).isReport(), playerCache.get(uuid).isTeamchat());
                playerCache.put(uuid, player);
                return player;
            } catch(SQLException e) {
                e.printStackTrace();
                System.out.println("[SQLDatabase] A SQLException occurred! Please ignore this Error!!");
                return null;
            }
        }, service);
    }

    public CompletableFuture<Player> updateOnlineTime(UUID uuid, long onlineTime) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = pool.getConnection()) {
                final PreparedStatement statement = connection.prepareStatement("UPDATE PlayerData SET ONLINETIME = ? WHERE UUID = ?");
                long newTime = playerCache.get(uuid).getOnlineTime() + onlineTime;
                statement.setLong(1, newTime);
                statement.setString(2, String.valueOf(uuid));
                statement.executeUpdate();
                Player player = new Player(uuid, playerCache.get(uuid).getName(), playerCache.get(uuid).getIP(), playerCache.get(uuid).getIP(), newTime, playerCache.get(uuid).getLanguages(), playerCache.get(uuid).getFirstJoin(), playerCache.get(uuid).getServerOnline(), playerCache.get(uuid).getLatestJoin(), playerCache.get(uuid).isReport(), playerCache.get(uuid).isTeamchat());
                playerCache.put(uuid, player);
                return player;
            } catch(SQLException e) {
                e.printStackTrace();
                System.out.println("[SQLDatabase] A SQLException occurred! Please ignore this Error!!");
                return null;
            }
        }, service);
    }

    public CompletableFuture<Ban> createBan(final UUID player, final String staff, String ip, final String reason, BanType type, final OffsetDateTime time, final OffsetDateTime creationTime, int duration) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = pool.getConnection()) {
                final PreparedStatement statement = connection.prepareStatement("INSERT INTO PlayerBans (UUID, STAFF, IP, REASON, TYPE, ENDTIME, CREATIONTIME, DURATION) VALUES (?, ?, ?, ?, ?, ?, ?, ?);");
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
                final PreparedStatement statement = connection.prepareStatement("INSERT INTO PlayerBans (UUID, STAFF, IP, REASON, TYPE, ENDTIME, CREATIONTIME, DURATION) VALUES (?, ?, ?, ?, ?, ?, ?, ?);");
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
