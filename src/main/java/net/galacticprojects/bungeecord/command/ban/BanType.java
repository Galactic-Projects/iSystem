package net.galacticprojects.bungeecord.command.ban;

public enum BanType {

    NBAN("nban"), SBAN("sban"), NMUTE("nmute"), SMUTE("smute");

    String name;

    BanType(String name) {
        this.name = name;
    }

    public static BanType fromString(String string) {
        try {
            return BanType.valueOf(string.toUpperCase());
        } catch(IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }
}
