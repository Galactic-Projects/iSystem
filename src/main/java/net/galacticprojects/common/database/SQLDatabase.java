package net.galacticprojects.common.database;

import com.zaxxer.hikari.pool.HikariPool;
import me.lauriichan.laylib.logger.ISimpleLogger;
import me.lauriichan.minecraft.wildcard.migration.IMigrationSource;
import net.galacticprojects.common.database.migration.impl.MigrationManager;
import net.galacticprojects.common.database.model.*;
import net.galacticprojects.common.util.TimeHelper;
import net.galacticprojects.common.util.cache.Cache;
import net.galacticprojects.common.util.cache.ThreadSafeCache;
import net.galacticprojects.common.database.migration.impl.SQLMigrationType;
import net.galacticprojects.common.util.Ref;

import java.sql.*;
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
    public static final String INSERT_PLAYER_BAN = String.format("INSERT INTO `%s` (Player, Owner, Id, Reason, Time, CreationTime) VALUES (?,?,?,?,?,?)", SQLTable.BAN_TABLE);

    public static final String SELECT_PLAYER = String.format("SELECT * FROM `%s` WHERE Player = ?", SQLTable.PLAYER_TABLE);
    public static final String SELECT_IP_PLAYER = String.format("SELECT * FROM `%s` WHERE Ip = ?", SQLTable.PLAYER_TABLE);
    public static final String DELETE_PLAYER = String.format("DELETE FROM `%s` WHERE Player = ?", SQLTable.PLAYER_TABLE);
    public static final String INSERT_PLAYER = String
            .format("INSERT INTO `%s` (Player, Ip, Coins, Level, Language, OnlineTime) VALUES (?, ?, ?, ?, ?, ?)", SQLTable.PLAYER_TABLE);
    public static final String UPDATE_PLAYER = String.format("UPDATE `%s` SET Ip = ?, Coins = ?, Level = ?, Language = ?, OnlineTime = ? WHERE Player = ?", SQLTable.PLAYER_TABLE);

    public static final String SELECT_FRIENDPLAYER = String.format("SELECT FRIENDUUID FROM `%s` WHERE UUID = ?", SQLTable.FRIENDS_TABLE);
    public static final String DELETE_FRIEND = String.format("DELETE FROM `%s` WHERE UUID = ? AND FRIENDUUID = ?", SQLTable.FRIENDS_TABLE);
    public static final String INSERT_FRIENDPLAYER = String.format("INSERT INTO `%s` (UUID, FRIENDUUID, DATE) VALUES (?,?,?)", SQLTable.FRIENDS_TABLE);

    public static final String SELECT_FRIENDREQUESTS = String.format("SELECT * FROM `%s` WHERE UUID = ?", SQLTable.FRIENDSREQUEST_TABLE);
    public static final String SELECT_FRIENDREQUESTS_FROM_REQUESTID = String.format("SELECT * FROM `%s` WHERE REQUESTID = ?", SQLTable.FRIENDSREQUEST_TABLE);

    //public static final String SELECT_FRIENDREQUESTS_COUNT_REQUESTID = String.format("SELECT COUNT(UUID) AS COUNT `%s` WHERE REQUESTID = ?", SQLTable.FRIENDSREQUEST_TABLE);
    //public static final String SELECT_FRIENDREQUESTS_COUNT = String.format("SELECT COUNT(REQUESTID) AS COUNT `%s` WHERE UUID = ?", SQLTable.FRIENDSREQUEST_TABLE);
    public static final String DELETE_FRIENDREQUEST = String.format("DELETE FROM `%s` WHERE UUID = ? AND REQUESTID = ?", SQLTable.FRIENDSREQUEST_TABLE);
    public static final String DELETE_FRIENDREQUEST_ALL = String.format("DELETE FROM `%s` WHERE REQUESTID = ?", SQLTable.FRIENDSREQUEST_TABLE);
    public static final String INSERT_FRIENDREQUEST = String.format("INSERT INTO `%s` (UUID, REQUESTID, DATE) VALUES (?,?,?)", SQLTable.FRIENDSREQUEST_TABLE);

    public static final String SELECT_FRIENDSETTINGS = String.format("SELECT * FROM `%s` WHERE UUID = ?", SQLTable.FRIENDS_SETTINGS);
    public static final String UPDATE__FRIENDSETTINGS = String.format("UPDATE `%s` SET REQUESTS = ?, JUMP = ?, MESSAGES = ? WHERE UUID = ?", SQLTable.FRIENDS_SETTINGS);
    public static final String INSERT_FRIENDSETTINGS = String.format("INSERT INTO `%s` (UUID, MESSAGES, JUMP, REQUESTS) VALUES (?,?,?,?)", SQLTable.FRIENDS_SETTINGS);


    public static final String SELECT_PLAYERHISTORY = String.format("SELECT * FROM `%s` WHERE UUID = ?", SQLTable.PLAYER_HISTORY);

    public static final String INSERT_PLAYER_HISTORY = String.format("INSERT INTO `%s` (Player, Owner, Type, Id, Reason, Time, CreationTime) VALUES (?,?,?,?,?,?,?)", SQLTable.BAN_TABLE);

    public static final String INSERT_CHATLOG = String.format("INSERT INTO `%s` (UUID, NAME, IP, SERVER, TIMESTAMP, MESSAGE) VALUES (?,?,?,?,?,?)", SQLTable.PLAYER_CHATLOG);
    public static final String SELECT_CHATLOG = String.format("SELECT * FROM `%s` WHERE UUID = ?", SQLTable.PLAYER_CHATLOG);

    private final HikariPool pool;

    private final Timer timer;

    private final Ref<List<Cache<?, ?>>> caches = Ref.of(new ArrayList<>());
    private final ExecutorService service = Executors.newCachedThreadPool();

    private final Cache<UUID, Ban> banCache = newCache(UUID.class, 300);
    private final Cache<UUID, Player> playerCache = newCache(UUID.class, 300);
    private final Cache<String, Player> playerIpCache = newCache(String.class, 300);

    private final Cache<UUID, FriendSettings> friendSettingsCache = newCache(UUID.class, 300);
    private final Cache<UUID, FriendRequest> friendRequestCache = newCache(UUID.class, 300);
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

    public CompletableFuture<FriendRequest> createFriendRequest(UUID uniqueId, UUID requestUniqueId, String date){
        return CompletableFuture.supplyAsync(() -> {
           try  (Connection connection = pool.getConnection()) {
               PreparedStatement preparedStatement = connection.prepareStatement(INSERT_FRIENDREQUEST);
               preparedStatement.setString(1, uniqueId.toString());
               preparedStatement.setString(2, requestUniqueId.toString());
               preparedStatement.setString(3, date);
               preparedStatement.executeUpdate();
               FriendRequest friendRequest = new FriendRequest(uniqueId, requestUniqueId, date);
               friendRequestCache.put(uniqueId, friendRequest);
               return friendRequest;
           } catch (SQLException e){
               logger.warning("A few SQL things went wrong while creating friendrequest ", e);
               return null;
           }
        }, service);
    }

    public CompletableFuture<Boolean> deleteFriendRequest(UUID uniqueId, UUID requestUniqueId) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = pool.getConnection()) {
                PreparedStatement preparedStatement = connection.prepareStatement(DELETE_FRIENDREQUEST);
                preparedStatement.setString(1, uniqueId.toString());
                preparedStatement.setString(2, requestUniqueId.toString());
                preparedStatement.executeUpdate();
                return true;
            } catch (SQLException e) {
                logger.warning("A few SQL things went wrong while creating friendrequest ", e);
                return false;
            }
        }, service);
    }

    public CompletableFuture<Boolean> deleteFriendRequest(UUID requestUniqueId) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = pool.getConnection()) {
                PreparedStatement preparedStatement = connection.prepareStatement(DELETE_FRIENDREQUEST_ALL);
                preparedStatement.setString(1, requestUniqueId.toString());
                preparedStatement.executeUpdate();
                return true;
            } catch (SQLException e) {
                logger.warning("A few SQL things went wrong while creating friendrequest ", e);
                return false;
            }
        }, service);
    }

    /*public CompletableFuture<Integer> getFriendRequestsAmount(UUID uniqueId) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = pool.getConnection()) {
                PreparedStatement preparedStatement = connection.prepareStatement(SELECT_FRIENDREQUESTS_COUNT);
                preparedStatement.setString(1, uniqueId.toString());
                ResultSet set = preparedStatement.executeQuery();
                if(set.next()) {
                    int amount = set.getInt("COUNT");
                    return amount;
                }
                return null;
            } catch (SQLException e) {
                logger.warning("A few SQL things went wrong while creating friendrequest ", e);
                return null;
            }
        }, service);
    }

    public CompletableFuture<Integer> getFriendRequestsAmountByRequest(UUID uniqueId) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = pool.getConnection()) {
                PreparedStatement preparedStatement = connection.prepareStatement(SELECT_FRIENDREQUESTS_COUNT_REQUESTID);
                preparedStatement.setString(1, uniqueId.toString());
                ResultSet set = preparedStatement.executeQuery();
                if(set.next()) {
                    int amount = set.getInt("COUNT");
                    return amount;
                }
                return null;
            } catch (SQLException e) {
                logger.warning("A few SQL things went wrong while creating friendrequest ", e);
                return null;
            }
        }, service);
    } */

    public CompletableFuture<Chatlog> getChatlog(UUID uniqueId){
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = pool.getConnection()) {
                PreparedStatement preparedStatement = connection.prepareStatement(SELECT_CHATLOG);
                preparedStatement.setString(1, uniqueId.toString());
                ResultSet set = preparedStatement.executeQuery();
                if(set.next()) {
                    String name = set.getString("NAME");
                    String ip = set.getString("IP");
                    String server = set.getString("SERVER");
                    String timestamp = set.getString("TIMESTAMP");
                    String message = set.getString("MESSAGE");
                    Chatlog chatlog = new Chatlog(uniqueId, name, ip, server, timestamp, message);
                    return chatlog;
                }
                return null;
            } catch (SQLException e) {
                logger.warning("A few SQL things went wrong while creating friendrequest ", e);
                return null;
            }
        });
    }

    public CompletableFuture<Void> createChatlog(Chatlog chatlog) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = pool.getConnection()) {
                PreparedStatement preparedStatement = connection.prepareStatement(INSERT_CHATLOG);
                preparedStatement.setString(1,  chatlog.getUUID().toString());
                preparedStatement.setString(2,  chatlog.getName());
                preparedStatement.setString(3,  chatlog.getIp());
                preparedStatement.setString(4,  chatlog.getServer());
                preparedStatement.setString(5,  chatlog.getTimestamp());
                preparedStatement.setString(6,  chatlog.getMessage());
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                logger.warning("A few SQL things went wrong while creating friendrequest ", e);
            }
            return null;
        });
    }

    public CompletableFuture<ArrayList<FriendRequest>> getFriendRequestByRequesterId(UUID uniqueId) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = pool.getConnection()) {
                PreparedStatement preparedStatement = connection.prepareStatement(SELECT_FRIENDREQUESTS_FROM_REQUESTID);
                preparedStatement.setString(1, uniqueId.toString());
                ResultSet set = preparedStatement.executeQuery();
                if(set.next()) {
                    ArrayList<FriendRequest> friendRequests = new ArrayList<>();
                    UUID uuid = UUID.fromString(set.getString("UUID"));
                    String date = set.getString("DATE");
                    FriendRequest friendRequest = new FriendRequest(uuid, uniqueId, date);
                    friendRequestCache.put(uniqueId, friendRequest);
                    friendRequests.add(friendRequest);
                    return friendRequests;
                }
                return null;
            } catch (SQLException e) {
                logger.warning("A few SQL things went wrong while creating friendrequest ", e);
                return null;
            }
        }, service);
    }

    public CompletableFuture<ArrayList<FriendRequest>> getFriendRequest(UUID uniqueId) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = pool.getConnection()) {
                PreparedStatement preparedStatement = connection.prepareStatement(SELECT_FRIENDREQUESTS);
                preparedStatement.setString(1, uniqueId.toString());
                ResultSet set = preparedStatement.executeQuery();
                if(set.next()) {
                    ArrayList<FriendRequest> friendRequests = new ArrayList<>();
                    UUID uuid = UUID.fromString(set.getString("REQUESTID"));
                    String date = set.getString("DATE");
                    FriendRequest friendRequest = new FriendRequest(uniqueId, uuid, date);
                    friendRequestCache.put(uniqueId, friendRequest);
                    friendRequests.add(friendRequest);
                    return friendRequests;
                }
                return null;
            } catch (SQLException e) {
                logger.warning("A few SQL things went wrong while creating friendrequest ", e);
                return null;
            }
        }, service);
    }

    public CompletableFuture<FriendSettings> createFriendSettings(UUID uniqueId) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = pool.getConnection()) {
                PreparedStatement preparedStatement = connection.prepareStatement(INSERT_FRIENDSETTINGS);
                preparedStatement.setString(1, uniqueId.toString());
                preparedStatement.setBoolean(2, true);
                preparedStatement.setBoolean(3, true);
                preparedStatement.setBoolean(4, true);
                preparedStatement.executeUpdate();
                FriendSettings friendSettings = new FriendSettings(uniqueId, true, true, true);
                friendSettingsCache.put(uniqueId, friendSettings);
                return friendSettings;
            } catch (SQLException e) {
                logger.warning("A few SQL things went wrong while creating friendsettings ", e);
                return null;
            }
        }, service);
    }

    public CompletableFuture<FriendSettings> getFriendSettings(UUID uniqueId){
        return CompletableFuture.supplyAsync(() -> {
            if(friendSettingsCache.has(uniqueId)) {
                return friendSettingsCache.get(uniqueId);
            }
            try (Connection connection = pool.getConnection()) {
                PreparedStatement statement = connection.prepareStatement(SELECT_FRIENDSETTINGS);
                statement.setString(1, uniqueId.toString());
                ResultSet set = statement.executeQuery();
                if(set.next()) {
                    boolean jump = set.getBoolean("JUMP");
                    boolean messages = set.getBoolean("MESSAGES");
                    boolean requests = set.getBoolean("REQUESTS");
                    FriendSettings friendSettings = new FriendSettings(uniqueId, jump, messages, requests);
                    friendSettingsCache.put(uniqueId, friendSettings);
                    return friendSettings;
                }
                return null;
            } catch (SQLException e) {
                logger.warning("A few SQL things went wrong while ask for friendsettings at request", e);
                return null;
            }
        }, service);
    }

    public CompletableFuture<FriendSettings> updateFriendSettings(UUID uniqueId, boolean jump, boolean messages, boolean requests) {
        return CompletableFuture.supplyAsync(() -> {
            try  (Connection connection = pool.getConnection()) {
                PreparedStatement preparedStatement = connection.prepareStatement(UPDATE__FRIENDSETTINGS);
                preparedStatement.setBoolean(1, jump);
                preparedStatement.setBoolean(2, messages);
                preparedStatement.setBoolean(3, requests);
                preparedStatement.setString(4, uniqueId.toString());
                preparedStatement.executeUpdate();
                if(friendSettingsCache.has(uniqueId)) {
                    friendSettingsCache.remove(uniqueId);
                }
                FriendSettings friendSettings = new FriendSettings(uniqueId, jump, messages, requests);
                friendSettingsCache.put(uniqueId, friendSettings);
                return friendSettings;
            } catch (SQLException e) {
                logger.warning("A few SQL things went wrong while updating Player", e);
                return null;
            }
        }, service);
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
                statement.executeUpdate();
                Player player = new Player(uniqueId, ip, onlineTime, coins, language, level);
                playerCache.put(uniqueId, player);
                return player;
            } catch (SQLException e) {
                logger.warning("A few SQL things went wrong while creating Player", e);
                return null;
            }

        }, service);
    }

    public boolean checkPlayer(UUID uniqueId) {
            try(Connection connection = pool.getConnection()) {
                PreparedStatement statement = connection.prepareStatement(SELECT_PLAYER);
                statement.setString(1, uniqueId.toString());
                ResultSet set = statement.executeQuery();
                if(set.next()) {
                    return true;
                }
                return false;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
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
                return null;
            }
        }, service);
    }

    public CompletableFuture<Player> getPlayer(String ip) {
        return CompletableFuture.supplyAsync(() -> {
            if(playerIpCache.has(ip)) {
                return playerIpCache.get(ip);
            }
            try(Connection connection = pool.getConnection()) {
                PreparedStatement statement = connection.prepareStatement(SELECT_IP_PLAYER);
                statement.setString(1, ip);
                ResultSet set = statement.executeQuery();
                if(set.next()) {
                    UUID uniqueId = UUID.fromString(set.getString("Player"));
                    int coins = set.getInt("Coins");
                    int level = set.getInt("Level");
                    String language = set.getString("Language");
                    long onlineTime = set.getLong("OnlineTime");
                    Player player = new Player(uniqueId, ip, onlineTime, coins, language, level);
                    playerIpCache.put(ip, player);
                    return player;
                }
                return null;
            }catch(SQLException e) {
                logger.warning("A few SQL things went wrong while get Player", e);
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
                return false;
            }
        }, service);
    }

    public CompletableFuture<Player> updatePlayer(UUID uniqueId, String ip, int coins, int level, String language, long onlineTime) {
        return CompletableFuture.supplyAsync(() -> {
            try(Connection connection = pool.getConnection()) {
                PreparedStatement statement = connection.prepareStatement(UPDATE_PLAYER);
                statement.setString(1, ip);
                statement.setInt(2, coins);
                statement.setInt(3, level);
                statement.setString(4, language);
                statement.setLong(5, onlineTime);
                statement.setString(6, uniqueId.toString());
                statement.executeUpdate();
                if(playerCache.has(uniqueId)) {
                    playerCache.remove(uniqueId);
                }
                Player player = new Player(uniqueId, ip, onlineTime, coins, language, level);
                playerCache.put(uniqueId, player);
                return player;
            }catch (SQLException e) {
                logger.warning("A few SQL things went wrong while delete Player", e);
                return null;
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
                statement.executeUpdate();
                Ban ban = new Ban(uniqueId, owner, id, reason, time, creationTime);
                banCache.put(uniqueId, ban);
                return ban;
            } catch(SQLException e) {
                logger.warning("A few SQL things went wrong while ban Player", e);
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
                return null;
            }
        }, service);
    }

}
