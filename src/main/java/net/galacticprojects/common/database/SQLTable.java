package net.galacticprojects.common.database;

public enum SQLTable {

    BAN_TABLE("PlayerBans"), PLAYER_TABLE("Player"), PARTY_TABLE("Party"),
    FRIENDS_TABLE("Friends"), REPORT_TABLE("Report")
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
