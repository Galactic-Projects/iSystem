package net.galacticprojects.bungeecord.command.ban;

public enum BanType {

    NBAN, SBAN, NMUTE, SMUTE;

    private final String name = name().toLowerCase();

    public final String getName() {
        return name;
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
