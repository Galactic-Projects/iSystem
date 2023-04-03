package net.galacticprojects.common.database;

import com.zaxxer.hikari.pool.HikariPool;
import me.lauriichan.laylib.logger.ISimpleLogger;
import me.lauriichan.minecraft.wildcard.migration.IMigrationSource;
import net.galacticprojects.bungeecord.ProxyPlugin;
import net.galacticprojects.bungeecord.util.Type;
import net.galacticprojects.common.database.migration.impl.MigrationManager;
import net.galacticprojects.common.database.model.*;
import net.galacticprojects.common.secure.GalacticSecure;
import net.galacticprojects.common.util.TimeHelper;
import net.galacticprojects.common.util.cache.Cache;
import net.galacticprojects.common.util.cache.ThreadSafeCache;
import net.galacticprojects.common.database.migration.impl.SQLMigrationType;
import net.galacticprojects.common.util.Ref;

import java.sql.*;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.Timer;
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

    private <A, B> ThreadSafeCache<A, B> newCache(Class<A> clazz, int cacheTime) {
        if (caches.isLocked()) {
            return null;
        }
        ThreadSafeCache<A, B> cache = new ThreadSafeCache<>(clazz, cacheTime);
        caches.get().add(cache);
        return cache;
    }

    public static final String SELECT_PLAYER_BAN = String.format("SELECT * FROM `%s` WHERE UUID = ?", SQLTable.BAN_TABLE);
    public static final String DELETE_PLAYER_BAN = String.format("DELETE FROM `%s` WHERE UUID = ?", SQLTable.BAN_TABLE);
    public static final String INSERT_PLAYER_BAN = String.format("INSERT INTO `%s` (UUID, STAFF, BANID, REASON, TIME, CREATIONTIME) VALUES (?,?,?,?,?,?)", SQLTable.BAN_TABLE);

    public static final String SELECT_PLAYER = String.format("SELECT * FROM `%s` WHERE UUID = ?", SQLTable.PLAYER_TABLE);
    public static final String SELECT_IP_PLAYER = String.format("SELECT * FROM `%s` WHERE IP = ?", SQLTable.PLAYER_TABLE);
    public static final String DELETE_PLAYER = String.format("DELETE FROM `%s` WHERE UUID = ?", SQLTable.PLAYER_TABLE);
    public static final String INSERT_PLAYER = String.format("INSERT INTO `%s` (UUID, IP, COINS, LEVEL, LANGUAGE, ONLINETIME, VERIFIED) VALUES (?, ?, ?, ?, ?, ?, ?)", SQLTable.PLAYER_TABLE);
    public static final String UPDATE_PLAYER = String.format("UPDATE `%s` SET IP = ?, COINS = ?, LEVEL = ?, LANGUAGE = ?, ONLINETIME = ?, VERIFIED = ? WHERE UUID = ?", SQLTable.PLAYER_TABLE);

    public static final String SELECT_FRIENDPLAYER = String.format("SELECT * FROM `%s` WHERE UUID = ?", SQLTable.FRIENDS_TABLE);
    public static final String SELECT_FRIENDPLAYER_REQUEST = String.format("SELECT * FROM `%s` WHERE UUID = ? OR FRIENDUUID = ?", SQLTable.FRIENDS_TABLE);
    public static final String DELETE_FRIEND = String.format("DELETE FROM `%s` WHERE UUID = ? AND FRIENDUUID = ?", SQLTable.FRIENDS_TABLE);
    public static final String INSERT_FRIENDPLAYER = String.format("INSERT INTO `%s` (UUID, FRIENDUUID, DATE) VALUES (?,?,?)", SQLTable.FRIENDS_TABLE);

    public static final String SELECT_FRIENDREQUEST = String.format("SELECT * FROM `%s` WHERE UUID = ? OR REQUEST = ?", SQLTable.FRIENDSREQUEST_TABLE);
    public static final String SELECT_FRIENDREQUEST_SINGLE = String.format("SELECT * FROM `%s` WHERE REQUEST = ?", SQLTable.FRIENDSREQUEST_TABLE);
    public static final String SELECT_FRIENDREQUEST_REQUESTOR = String.format("SELECT * FROM `%s` WHERE UUID = ?", SQLTable.FRIENDSREQUEST_TABLE);
    public static final String DELETE_FRIENDREQUEST = String.format("DELETE FROM `%s` WHERE UUID = ? AND REQUEST = ?", SQLTable.FRIENDSREQUEST_TABLE);
    public static final String INSERT_FRIENDREQUEST = String.format("INSERT INTO `%s` (UUID, REQUEST, DATE) VALUES (?,?,?)", SQLTable.FRIENDSREQUEST_TABLE);

    public static final String SELECT_FRIENDSETTINGS = String.format("SELECT * FROM `%s` WHERE UUID = ?", SQLTable.FRIENDS_SETTINGS);
    public static final String UPDATE__FRIENDSETTINGS = String.format("UPDATE `%s` SET REQUESTS = ?, JUMP = ?, MESSAGES = ? WHERE UUID = ?", SQLTable.FRIENDS_SETTINGS);
    public static final String INSERT_FRIENDSETTINGS = String.format("INSERT INTO `%s` (UUID, MESSAGES, JUMP, REQUESTS) VALUES (?,?,?,?)", SQLTable.FRIENDS_SETTINGS);

    public static final String INSERT_PLAYER_HISTORY = String.format("INSERT INTO `%s` (UUID, INFORMATION) VALUES (?,?)", SQLTable.PLAYER_HISTORY);
    public static final String SELECT_PLAYER_HISTORY = String.format("SELECT * FROM `%s` WHERE UUID = ?", SQLTable.PLAYER_HISTORY);
    public static final String DELETE_PLAYER_HISTORY = String.format("DELETE FROM`%s` WHERE UUID = ?", SQLTable.PLAYER_HISTORY);

    public static final String INSERT_REPORT = String.format("INSERT INTO `%s` (UUID, CREATOR, REASON, STATUS, TIMESTAMP) VALUES (?,?,?,?,?)", SQLTable.REPORT_TABLE);
    public static final String UPDATE_STATUS_REPORT = String.format("UPDATE `%s` SET STATUS = ? WHERE UUID = ?", SQLTable.REPORT_TABLE);
    public static final String SELECT_REPORT = String.format("SELECT * FROM `%s` WHERE UUID = ?", SQLTable.REPORT_TABLE);
    public static final String DELETE_REPORT = String.format("DELETE FROM `%s` WHERE UUID = ? AND CREATOR = ?", SQLTable.REPORT_TABLE);

    public static final String INSERT_CHATLOG = String.format("INSERT INTO `%s` (UUID, NAME, IP, SERVER, TIMESTAMP, MESSAGE) VALUES (?,?,?,?,?,?)", SQLTable.PLAYER_CHATLOG);
    public static final String SELECT_CHATLOG = String.format("SELECT * FROM `%s` WHERE UUID = ?", SQLTable.PLAYER_CHATLOG);

    public static final String SELECT_LOBBY = String.format("SELECT * FROM `%s` WHERE UUID = ?", SQLTable.LOBBY_TABLE);
    public static final String INSERT_LOBBY = String.format("INSERT INTO `%s` (UUID, LOTTERY, CHEST, DATA) VALUES (?, ?, ?, ?)", SQLTable.LOBBY_TABLE);
    public static final String UPDATE_LOBBY = String.format("UPDATE `%s` SET LOTTERY = ?, CHEST = ?, DATA = ? WHERE UUID = ?", SQLTable.LOBBY_TABLE);

    public static final String INSERT_LINK = String.format("INSERT INTO `%s` (UUID, DISCORDTAG, DISCORDTIME, DISCORDBOOL, TSIDENTIFIER, TSIP, TSTIME, TSBOOL) VALUES (?,?,?,?,?,?,?,?)", SQLTable.LINK_TABLE);
    public static final String SELECT_LINK = String.format("SELECT * FROM `%s` WHERE UUID = ?", SQLTable.LINK_TABLE);
    public static final String UPDATE_LINK = String.format("UPDATE `%s` SET DISCORDTAG = ?, DISCORDTIME = ?, DISCORDBOOL = ?, TSIDENTIFIER = ?, TSIP = ?, TSIP = ?, TSBOOL = ? WHERE UUID = ?", SQLTable.LINK_TABLE);

    private final HikariPool pool;

    private final Timer timer;

    private final Ref<List<Cache<?, ?>>> caches = Ref.of(new ArrayList<>());
    private final ExecutorService service = Executors.newCachedThreadPool();

    private final Cache<UUID, Ban> banCache = newCache(UUID.class, 300);
    private final Cache<UUID, Player> playerCache = newCache(UUID.class, 300);
    private final Cache<String, Player> playerIpCache = newCache(String.class, 300);
    private final Cache<UUID, Report> reportCache = newCache(UUID.class, 300);
    private final Cache<UUID, FriendSettings> friendSettingsCache = newCache(UUID.class, 300);
    private final Cache<UUID, FriendRequest> friendRequestCache = newCache(UUID.class, 300);
    private final Cache<UUID, LinkPlayer> linkPlayerCache = newCache(UUID.class, 300);
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


    public CompletableFuture<LinkPlayer> createLinkPlayer(UUID uniqueId) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = pool.getConnection()) {
                PreparedStatement preparedStatement = connection.prepareStatement(INSERT_LINK);
                preparedStatement.setString(1, uniqueId.toString());
                preparedStatement.setString(2, null);
                preparedStatement.setString(3, null);
                preparedStatement.setBoolean(4, false);
                preparedStatement.setString(5, null);
                preparedStatement.setString(6, null);
                preparedStatement.setString(7, null);
                preparedStatement.setBoolean(8, false);
                preparedStatement.executeUpdate();
                LinkPlayer linkPlayer = new LinkPlayer(uniqueId, null, null, false, null, null, null, false);
                linkPlayerCache.put(uniqueId, linkPlayer);
                return linkPlayer;
            } catch (SQLException e) {
                logger.warning("A few SQL things went wrong while creating link ", e);
                return null;
            }
        }, service);
    }

    public CompletableFuture<LinkPlayer> getLinkedPlayer(UUID uniqueId) {
        return CompletableFuture.supplyAsync(() -> {
            if(linkPlayerCache.has(uniqueId)) {
                return linkPlayerCache.get(uniqueId);
            }
            try (Connection connection = pool.getConnection()) {
                PreparedStatement preparedStatement = connection.prepareStatement(SELECT_LINK);
                preparedStatement.setString(1, uniqueId.toString());
                ResultSet resultSet = preparedStatement.executeQuery();
                if(resultSet != null) {
                    String discordTag = resultSet.getString("DISCORDTAG");
                    String discordTime = resultSet.getString("DISCORDTIME");
                    boolean discordLinked = resultSet.getBoolean("DISCORDBOOL");
                    String teamspeakIdentifier = resultSet.getString("TSIDENTIFIER");
                    String teamspeakIP = resultSet.getString("TSIP");
                    String teamspeakTime = resultSet.getString("TSTIME");
                    boolean teamspeakLinked = resultSet.getBoolean("TSBOOL");

                    LinkPlayer linkPlayer = new LinkPlayer(uniqueId, discordTag, TimeHelper.fromString(discordTime), discordLinked, teamspeakIdentifier, teamspeakIP, TimeHelper.fromString(teamspeakTime), teamspeakLinked);
                    linkPlayerCache.put(uniqueId, linkPlayer);
                    return linkPlayer;
                }
                return null;
            } catch (SQLException e) {
                logger.warning("A few SQL things went wrong while catching link player");
                return null;
            }
        }, service);
    }

    public CompletableFuture<LinkPlayer> updateLinkPlayer(LinkPlayer linkPlayer) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = pool.getConnection()) {
                PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_LINK);
                preparedStatement.setString(1, linkPlayer.getDiscordTag());
                preparedStatement.setString(2, TimeHelper.toString(linkPlayer.getDiscordTime()));
                preparedStatement.setBoolean(3, linkPlayer.isDiscordLinked());
                preparedStatement.setString(4, linkPlayer.getTeamspeakIdentifier());
                preparedStatement.setString(5, linkPlayer.getTeamspeakIp());
                preparedStatement.setString(6, TimeHelper.toString(linkPlayer.getTeamspeakTime()));
                preparedStatement.setBoolean(7, linkPlayer.isTeamspeakLinked());
                preparedStatement.setString(8, linkPlayer.getUniqueId().toString());
                preparedStatement.executeUpdate();
                if(linkPlayerCache.has(linkPlayer.getUniqueId())){
                    linkPlayerCache.remove(linkPlayer.getUniqueId());
                }
                linkPlayerCache.put(linkPlayer.getUniqueId(), linkPlayer);
                return linkPlayer;
            } catch (SQLException e) {
                logger.warning("A few SQL things went wrong while updating link player");
                return null;
            }
        }, service);
    }

    public CompletableFuture<Report> createReport(UUID uniqueId, UUID creator, String reason, boolean status, String timestamp) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = pool.getConnection()) {
                PreparedStatement preparedStatement = connection.prepareStatement(INSERT_REPORT);
                preparedStatement.setString(1, uniqueId.toString());
                preparedStatement.setString(2, encrypt(creator.toString()));
                preparedStatement.setString(3, encrypt(reason));
                preparedStatement.setBoolean(4, status);
                preparedStatement.setString(5, encrypt(timestamp));
                preparedStatement.executeUpdate();
                Report report = new Report(getReport(uniqueId).join().getID(), uniqueId, creator, reason, status, timestamp);
                reportCache.put(uniqueId, report);
                return report;
            } catch (SQLException e) {
                logger.warning("A few SQL things went wrong while creating report ", e);
                return null;
            }
        }, service);
    }

    public CompletableFuture<Report> getReport(UUID uniqueId) {
        return CompletableFuture.supplyAsync(() -> {
            if(reportCache.has(uniqueId)) {
                return reportCache.get(uniqueId);
            }
            try (Connection connection = pool.getConnection()) {
                PreparedStatement preparedStatement = connection.prepareStatement(SELECT_REPORT);
                preparedStatement.setString(1, uniqueId.toString());
                ResultSet set = preparedStatement.executeQuery();
                if(set.next()) {
                    int id = set.getInt("ID");
                    UUID creator = UUID.fromString(decrypt(set.getString("CREATOR")));
                    String reason = decrypt(set.getString("REASON"));
                    boolean status = set.getBoolean("STATUS");
                    String timestamp = decrypt(set.getString("TIMESTAMP"));
                    Report report =  new Report(id, uniqueId, creator, reason, status, timestamp);
                    reportCache.remove(uniqueId);
                    reportCache.put(uniqueId, report);
                    return report;
                }
                return null;
            } catch (SQLException e) {
                logger.warning("A few SQL things went wrong while get report ", e);
                return null;
            }
        },service);
    }

    public CompletableFuture<Report> updateReport(Report report) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = pool.getConnection()) {
                PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_STATUS_REPORT);
                preparedStatement.setBoolean(1, report.isStatus());
                preparedStatement.setString(2, report.getUUID().toString());
                preparedStatement.executeUpdate();
                Report meta = new Report(report.getID(), report.getUUID(), report.getCreator(), report.getReason(), report.isStatus(), report.getTimestamp());
                if(reportCache.has(report.getUUID())) {
                    reportCache.remove(report.getUUID());
                }
                reportCache.put(report.getUUID(), meta);
                return report;
            } catch (SQLException e) {
                logger.warning("A few SQL things went wrong while get report ", e);
                return null;
            }
        },service);
    }

    public CompletableFuture<Void> deleteReport(UUID uniqueId, UUID creator) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = pool.getConnection()) {
                PreparedStatement preparedStatement = connection.prepareStatement(DELETE_REPORT);
                preparedStatement.setString(1, uniqueId.toString());
                preparedStatement.setString(2, encrypt(creator.toString()));
                preparedStatement.executeUpdate();
                if(reportCache.has(uniqueId)) {
                    reportCache.remove(uniqueId);
                }
                return null;
            } catch (SQLException e) {
                logger.warning("A few SQL things went wrong while delete report ", e);
                return null;
            }
        }, service);
    }

    public CompletableFuture<ArrayList<History>> createHistory(final UUID uniqueId, final UUID owner, final Type type, final String reason, final OffsetDateTime time, final OffsetDateTime creationTime) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = pool.getConnection()) {
                ArrayList<History> histories;
                histories = getHistory(uniqueId).join();
                if(histories == null) {
                    histories = new ArrayList<>();
                }
                histories.add(new History(uniqueId, owner, type, getBan(uniqueId).join().getID(), reason, time, creationTime));
                Array history = connection.createArrayOf(Array.class.getTypeName(), histories.toArray());
                PreparedStatement statement = connection.prepareStatement(INSERT_PLAYER_HISTORY);
                statement.setString(1, uniqueId.toString());
                statement.setArray(2, history);
                statement.executeUpdate();
                return histories;
            } catch (SQLException e) {
                logger.warning("A few SQL things went wrong while creating history ", e);
                return null;
            }
        }, service);
    }

    public CompletableFuture<Void> deleteHistory(final UUID uniqueId) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = pool.getConnection()) {
                PreparedStatement preparedStatement = connection.prepareStatement(DELETE_PLAYER_HISTORY);
                preparedStatement.setString(1, uniqueId.toString());
                preparedStatement.executeUpdate();
                return null;
            } catch (SQLException e) {
                logger.warning("A few SQL things went wrong while trying to delete players history ", e);
                return null;
            }
        }, service);
    }

    public CompletableFuture<ArrayList<History>> getHistory(final UUID uniqueId) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = pool.getConnection()) {
                PreparedStatement preparedStatement = connection.prepareStatement(SELECT_PLAYER_HISTORY);
                preparedStatement.setString(1, uniqueId.toString());
                ResultSet set = preparedStatement.executeQuery();
                if(set.next()) {
                    Array historyUniqueId = set.getArray("INFORMATION");
                    History[] histories = (History[]) historyUniqueId.getArray();
                    ArrayList<History> history = (ArrayList<History>) Arrays.asList(histories);
                    logger.info(new Throwable("Amount getHistory " + history.size()));
                    return history;
                }
                preparedStatement.close();
                set.close();
                return null;
            } catch (SQLException e) {
                logger.warning("Can't catch history from player", e);
                return null;
            }
        }, service);
    }

    public CompletableFuture<Chatlog> getChatlog(UUID uniqueId) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = pool.getConnection()) {
                PreparedStatement preparedStatement = connection.prepareStatement(SELECT_CHATLOG);
                preparedStatement.setString(1, uniqueId.toString());
                ResultSet set = preparedStatement.executeQuery();
                if (set.next()) {
                    String name = decrypt(set.getString("NAME"));
                    String ip = decrypt(set.getString("IP"));
                    String server = decrypt(set.getString("SERVER"));
                    String timestamp = decrypt(set.getString("TIMESTAMP"));
                    String message = decrypt(set.getString("MESSAGE"));
                    return new Chatlog(uniqueId, name, ip, server, timestamp, message);
                }
                return null;
            } catch (SQLException e) {
                logger.warning("A few SQL things went wrong while get chatlog ", e);
                return null;
            }
        });
    }

    public CompletableFuture<Void> createChatlog(Chatlog chatlog) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = pool.getConnection()) {
                PreparedStatement preparedStatement = connection.prepareStatement(INSERT_CHATLOG);
                preparedStatement.setString(1, chatlog.getUUID().toString());
                preparedStatement.setString(2, encrypt(chatlog.getName()));
                preparedStatement.setString(3, encrypt(chatlog.getIp()));
                preparedStatement.setString(4, encrypt(chatlog.getServer()));
                preparedStatement.setString(5, encrypt(chatlog.getTimestamp()));
                preparedStatement.setString(6, encrypt(chatlog.getMessage()));
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                logger.warning("A few SQL things went wrong while creating chatlog ", e);
            }
            return null;
        });
    }

    public CompletableFuture<UUID> createFriend(UUID uniqueId, UUID friendUniqueId, String date) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = pool.getConnection()) {
                PreparedStatement preparedStatement = connection.prepareStatement(INSERT_FRIENDPLAYER);
                preparedStatement.setString(1, uniqueId.toString());
                preparedStatement.setString(2, friendUniqueId.toString());
                preparedStatement.setString(3, date);
                preparedStatement.executeUpdate();
                return uniqueId;
            } catch (SQLException e) {
                logger.warning("A few SQL things went wrong while creating friend ", e);
                return null;
            }
        }, service);
    }

    public CompletableFuture<Boolean> hasFriends(UUID uniqueId) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = pool.getConnection()) {
                PreparedStatement preparedStatement = connection.prepareStatement(SELECT_FRIENDPLAYER);
                preparedStatement.setString(1, uniqueId.toString());
                ResultSet set = preparedStatement.executeQuery();
                return set.next() && set.getString("FRIENDUUID") != null;
            } catch (SQLException e) {
                logger.warning("A few SQL things went wrong while creating friendrequest ", e);
                return false;
            }
        }, service);
    }

    public CompletableFuture<ArrayList<Friends>> getFriends(UUID uniqueId) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = pool.getConnection()) {
                PreparedStatement preparedStatement = connection.prepareStatement(SELECT_FRIENDPLAYER);
                preparedStatement.setString(1, uniqueId.toString());
                ResultSet set = preparedStatement.executeQuery();
                ArrayList<Friends> friends = new ArrayList<>();
                while(set.next()) {
                    Friends friend = new Friends(uniqueId, UUID.fromString(set.getString("FRIENDUUID")), set.getString("DATE"));
                    friends.add(friend);
                }
                logger.info(new Throwable("Amount getFriend: " + friends.size()));
                return friends;
            } catch (SQLException e) {
                logger.warning("A few SQL things went wrong while creating friend request ", e);
                return null;
            }
        }, service);
    }


    public CompletableFuture<Void> deleteFriend(UUID uniqueId, UUID friendUniqueId) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = pool.getConnection()) {
                PreparedStatement preparedStatement = connection.prepareStatement(DELETE_FRIEND);
                preparedStatement.setString(1, uniqueId.toString());
                preparedStatement.setString(2, friendUniqueId.toString());
                preparedStatement.executeUpdate();
                return null;
            } catch (SQLException e) {
                logger.warning("A few SQL things went wrong while creating friendrequest ", e);
                return null;
            }
        }, service);
    }

    public CompletableFuture<Boolean> isAlreadyFriend(UUID uniqueId, UUID friendUniqueId) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = pool.getConnection()) {
                PreparedStatement preparedStatement = connection.prepareStatement(SELECT_FRIENDPLAYER_REQUEST);
                preparedStatement.setString(1, uniqueId.toString());
                preparedStatement.setString(2, friendUniqueId.toString());
                ResultSet set = preparedStatement.executeQuery();
                return set.next() && set.getString("UUID") != null && set.getString("FRIENDUUID") != null;
            } catch (SQLException e) {
                logger.warning("A few SQL things went wrong while creating friendrequest ", e);
                return false;
            }
        }, service);
    }


    public CompletableFuture<FriendRequest> createFriendRequest(UUID uniqueId, UUID requestUniqueId, String date) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = pool.getConnection()) {
                PreparedStatement preparedStatement = connection.prepareStatement(INSERT_FRIENDREQUEST);
                preparedStatement.setString(1, uniqueId.toString());
                preparedStatement.setString(2, requestUniqueId.toString());
                preparedStatement.setString(3, date);
                preparedStatement.executeUpdate();
                return new FriendRequest(uniqueId, requestUniqueId, date);
            } catch (SQLException e) {
                logger.warning("A few SQL things went wrong while creating friend ", e);
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
                return null;
            } catch (SQLException e) {
                logger.warning("A few SQL things went wrong while creating friendrequest ", e);
                return null;
            }
        }, service);
    }

    public CompletableFuture<Boolean> isFriendRequested(UUID uniqueId, UUID requesterUniqueId) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = pool.getConnection()) {
                PreparedStatement preparedStatement = connection.prepareStatement(SELECT_FRIENDREQUEST);
                preparedStatement.setString(1, uniqueId.toString());
                preparedStatement.setString(2, requesterUniqueId.toString());
                ResultSet set = preparedStatement.executeQuery();
                return set.next() && set.getString("UUID") != null && set.getString("REQUEST") != null;
            } catch (SQLException e) {
                logger.warning("A few SQL things went wrong while creating friend request ", e);
                return false;
            }
        }, service);
    }

    public CompletableFuture<Boolean> isFriendRequested(UUID uniqueId) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = pool.getConnection()) {
                PreparedStatement preparedStatement = connection.prepareStatement(SELECT_FRIENDREQUEST_SINGLE);
                preparedStatement.setString(1, uniqueId.toString());
                ResultSet set = preparedStatement.executeQuery();
                List<UUID> friendUnique = new ArrayList<>();
                while (set.next()) {
                    friendUnique.add(UUID.fromString(set.getString("UUID")));
                }
                if(friendUnique.contains(uniqueId)) {
                    return true;
                }
                return false;
            } catch (SQLException e) {
                logger.warning("A few SQL things went wrong while creating friendrequest ", e);
                return false;
            }
        }, service);
    }

    public CompletableFuture<Boolean> isFriendRequestedByRequestor(UUID uniqueId) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = pool.getConnection()) {
                PreparedStatement preparedStatement = connection.prepareStatement(SELECT_FRIENDREQUEST_SINGLE);
                preparedStatement.setString(1, uniqueId.toString());
                ResultSet set = preparedStatement.executeQuery();
                ArrayList<FriendRequest> friendRequests = getFriendRequest(uniqueId).join();
                while (set.next()) {
                    String request = set.getString("REQUEST");
                    for (FriendRequest friendRequest : friendRequests) {
                        if(friendRequest.getRequestUUID().equals(UUID.fromString(request))) {
                            createFriend(uniqueId, UUID.fromString(request), TimeHelper.BAN_TIME_FORMATTER.format(OffsetDateTime.now()));
                            return true;
                        }
                    }
                }
                return false;
            } catch (SQLException e) {
                logger.warning("A few SQL things went wrong while creating friendrequest ", e);
                return false;
            }
        }, service);
    }

    public CompletableFuture<ArrayList<FriendRequest>> getFriendRequest(UUID uniqueId) {
        ArrayList<FriendRequest> friendRequests = new ArrayList<>();
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = pool.getConnection()) {
                PreparedStatement preparedStatement = connection.prepareStatement(SELECT_FRIENDREQUEST_SINGLE);
                preparedStatement.setString(1, uniqueId.toString());
                ResultSet set = preparedStatement.executeQuery();
                ArrayList<FriendRequest> friends = new ArrayList<>();
                while(set.next()) {
                    FriendRequest friend = new FriendRequest(uniqueId, UUID.fromString(set.getString("UUID").equals(uniqueId.toString()) ? set.getString("FRIENDUUID") : set.getString("UUID")), set.getString("DATE"));
                    friends.add(friend);
                }
                logger.info(new Throwable("Amount getFriend: " + friends.size()));
                return friends;
            } catch (SQLException e) {
                logger.warning("A few SQL things went wrong while creating friendrequest ", e);
                return null;
            }
        }, service);
    }

    public CompletableFuture<ArrayList<FriendRequest>> getFriendRequestByRequesterId(UUID uniqueId) {
        ArrayList<FriendRequest> friendRequests = new ArrayList<>();
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = pool.getConnection()) {
                PreparedStatement preparedStatement = connection.prepareStatement(SELECT_FRIENDREQUEST_REQUESTOR);
                preparedStatement.setString(1, uniqueId.toString());
                ResultSet set = preparedStatement.executeQuery();
                ArrayList<FriendRequest> friends = new ArrayList<>();
                while(set.next()) {
                    FriendRequest friend = new FriendRequest(uniqueId, UUID.fromString(set.getString("UUID").equals(uniqueId.toString()) ? set.getString("FRIENDUUID") : set.getString("UUID")), set.getString("DATE"));
                    friends.add(friend);
                }
                logger.info(new Throwable("Amount getFriend: " + friends.size()));
                return friends;
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

    public CompletableFuture<FriendSettings> getFriendSettings(UUID uniqueId) {
        return CompletableFuture.supplyAsync(() -> {
            if (friendSettingsCache.has(uniqueId)) {
                return friendSettingsCache.get(uniqueId);
            }
            try (Connection connection = pool.getConnection()) {
                PreparedStatement statement = connection.prepareStatement(SELECT_FRIENDSETTINGS);
                statement.setString(1, uniqueId.toString());
                ResultSet set = statement.executeQuery();
                if (set.next()) {
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
            try (Connection connection = pool.getConnection()) {
                PreparedStatement preparedStatement = connection.prepareStatement(UPDATE__FRIENDSETTINGS);
                preparedStatement.setBoolean(1, jump);
                preparedStatement.setBoolean(2, messages);
                preparedStatement.setBoolean(3, requests);
                preparedStatement.setString(4, uniqueId.toString());
                preparedStatement.executeUpdate();
                if (friendSettingsCache.has(uniqueId)) {
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

    public CompletableFuture<Player> createPlayer(UUID uniqueId, String ip, int coins, int level, String language, long onlineTime, boolean verified) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = pool.getConnection()) {
                PreparedStatement statement = connection.prepareStatement(INSERT_PLAYER);
                statement.setString(1, uniqueId.toString());
                statement.setString(2, encrypt(ip));
                statement.setString(3, encrypt(coins));
                statement.setString(4,encrypt(level));
                statement.setString(5, encrypt(language));
                statement.setString(6, encrypt(onlineTime));
                statement.setString(7, encrypt(verified));
                statement.executeUpdate();
                Player player = new Player(uniqueId, ip, onlineTime, coins, language, level, verified);
                playerCache.put(uniqueId, player);
                return player;
            } catch (SQLException e) {
                logger.warning("A few SQL things went wrong while creating Player", e);
                return null;
            }

        }, service);
    }

    public boolean checkPlayer(UUID uniqueId) {
        try (Connection connection = pool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(SELECT_PLAYER);
            statement.setString(1, uniqueId.toString());
            ResultSet set = statement.executeQuery();
            if (set.next()) {
                return true;
            }
            return false;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public CompletableFuture<Player> getPlayer(UUID uniqueId) {
        return CompletableFuture.supplyAsync(() -> {
            if (playerCache.has(uniqueId)) {
                return playerCache.get(uniqueId);
            }
            try (Connection connection = pool.getConnection()) {
                PreparedStatement statement = connection.prepareStatement(SELECT_PLAYER);
                statement.setString(1, uniqueId.toString());
                ResultSet set = statement.executeQuery();
                if (set.next()) {
                    String ip = decrypt(set.getString("IP"));
                    int coins = Integer.parseInt(decrypt(set.getString("COINS")));
                    int level = Integer.parseInt(decrypt(set.getString("LEVEL")));
                    String language = decrypt(set.getString("LANGUAGE"));
                    long onlineTime = Long.parseLong(decrypt(set.getString("ONLINETIME")));
                    boolean verified = Boolean.parseBoolean(decrypt(set.getString("VERIFIED")));
                    Player player = new Player(uniqueId, ip, onlineTime, coins, language, level, verified);
                    playerCache.put(uniqueId, player);
                    return player;
                }
                return null;
            } catch (SQLException e) {
                logger.warning("A few SQL things went wrong while get Player", e);
                return null;
            }
        }, service);
    }

    public CompletableFuture<Player> getPlayer(String ip) {
        return CompletableFuture.supplyAsync(() -> {
            if (playerIpCache.has(ip)) {
                return playerIpCache.get(ip);
            }
            try (Connection connection = pool.getConnection()) {
                PreparedStatement statement = connection.prepareStatement(SELECT_IP_PLAYER);
                statement.setString(1, ip);
                ResultSet set = statement.executeQuery();
                if (set.next()) {
                    UUID uniqueId = UUID.fromString(set.getString("UUID"));
                    int coins = Integer.parseInt(decrypt(set.getString("COINS")));
                    int level = Integer.parseInt(decrypt(set.getString("LEVEL")));
                    String language = String.valueOf(decrypt(set.getString("LANGUAGE")));
                    long onlineTime = Long.parseLong(decrypt(set.getString("ONLINETIME")));
                    boolean verified = Boolean.parseBoolean(decrypt(set.getString("VERIFIED")));
                    Player player = new Player(uniqueId, ip, onlineTime, coins, language, level, verified);
                    playerIpCache.put(ip, player);
                    return player;
                }
                return null;
            } catch (SQLException e) {
                logger.warning("A few SQL things went wrong while get Player", e);
                return null;
            }
        }, service);
    }

    public CompletableFuture<Boolean> deletePlayer(UUID uniqueId) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = pool.getConnection()) {
                PreparedStatement statement = connection.prepareStatement(DELETE_PLAYER);
                statement.setString(1, uniqueId.toString());
                statement.executeUpdate();
                return true;
            } catch (SQLException e) {
                logger.warning("A few SQL things went wrong while delete Player", e);
                return false;
            }
        }, service);
    }

    public CompletableFuture<Player> updatePlayer(Player player) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = pool.getConnection()) {
                PreparedStatement statement = connection.prepareStatement(UPDATE_PLAYER);
                statement.setString(1, String.valueOf(encrypt(player.getIP())));
                statement.setString(2, encrypt(player.getCoins()));
                statement.setString(3, encrypt(player.getLevel()));
                statement.setString(4, String.valueOf(encrypt(player.getLanguage())));
                statement.setString(5, encrypt(player.getOnlineTime()));
                statement.setString(6, encrypt(player.getVerified()));
                statement.setString(7, player.getUUID().toString());
                statement.executeUpdate();
                playerCache.put(player.getUUID(), player);
                return player;
            } catch (SQLException e) {
                logger.warning("A few SQL things went wrong while delete Player", e);
                return null;
            }
        }, service);
    }

    public CompletableFuture<Ban> banPlayer(final UUID uniqueId, final UUID owner, final int banId, final String reason, final OffsetDateTime time, final OffsetDateTime creationTime) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = pool.getConnection()) {
                PreparedStatement statement = connection.prepareStatement(INSERT_PLAYER_BAN);
                statement.setString(1, uniqueId.toString());
                statement.setString(2, encrypt(owner.toString()));
                statement.setString(3, encrypt(String.valueOf(banId)));
                statement.setString(4, encrypt(reason));
                statement.setString(5, encrypt(TimeHelper.toString(time)));
                statement.setString(6, encrypt(TimeHelper.toString(creationTime)));
                statement.executeUpdate();
                Ban ban = new Ban(uniqueId, owner, getBan(uniqueId).join().getID(), banId, reason, time, creationTime);
                banCache.put(uniqueId, ban);
                return ban;
            } catch (SQLException e) {
                logger.warning("A few SQL things went wrong while ban Player", e);
                return null;
            }
        }, service);
    }

    public CompletableFuture<Boolean> deleteBan(final UUID uniqueId) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = pool.getConnection()) {
                PreparedStatement statement = connection.prepareStatement(DELETE_PLAYER_BAN);
                statement.setString(1, uniqueId.toString());
                statement.executeUpdate();
                if (banCache.has(uniqueId)) {
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
            if (banCache.has(uniqueId)) {
                return banCache.get(uniqueId);
            }
            try (Connection connection = pool.getConnection()) {
                PreparedStatement statement = connection.prepareStatement(SELECT_PLAYER_BAN);
                statement.setString(1, uniqueId.toString());
                ResultSet set = statement.executeQuery();
                if (set.next()) {
                    int id = set.getInt("ID");
                    UUID owner = UUID.fromString(decrypt(set.getString("STAFF")));
                    int banId = Integer.parseInt(decrypt(set.getString("BANID")));
                    String reason = decrypt(set.getString("REASON"));
                    OffsetDateTime time = TimeHelper.fromString(decrypt(set.getString("TIME")));
                    OffsetDateTime creationTime = TimeHelper.fromString(decrypt(set.getString("CREATIONTIME")));
                    Ban ban = new Ban(uniqueId, owner, id, banId, reason, time, creationTime);
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

    private String encrypt(Object clear)  {
        return ProxyPlugin.getInstance().getSecure().powerfulHash(String.valueOf(clear));
    }

    private String decrypt(String hashed)  {
        return ProxyPlugin.getInstance().getSecure().powerfulDecrypt(hashed);
    }
}
