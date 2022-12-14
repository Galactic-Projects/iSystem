package net.galacticprojects.common.database;

public enum SQLTable {

    BAN_TABLE("PlayerBans"), PLAYER_TABLE("Player"),
    PLAYER_HISTORY("PlayerHistory"), PLAYER_CHATLOG("PlayerChatLog"),
    FRIENDS_TABLE("Friends"), FRIENDSREQUEST_TABLE("FriendsRequest"),
    FRIENDS_SETTINGS("FriendsSettings"), REPORT_TABLE("Report"),
    LINK_TABLE("PlayerLink"), LOBBY_TABLE("Lobby")
    ;
    private final String defaultTableName;
    private String tableName;

    SQLTable(final String defaultTableName) {
        this.defaultTableName = defaultTableName;
    }

    public String defaultTableName() {
        return defaultTableName;
    }

    public String tableName() {
        if(tableName == null) {
            return (tableName = defaultTableName);
        }
        return tableName;
    }

    public void tableName(String tableName){
        this.tableName = tableName;
    }

    @Override
    public String toString() {
        return tableName;
    }

}
