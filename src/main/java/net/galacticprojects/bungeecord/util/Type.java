package net.galacticprojects.bungeecord.util;

public enum Type {

    BAN("BAN"),
    UNBAN("UNBAN");

    private final String format;

    private Type() {
        this(null);
    }
    private Type(String format) {
        this.format = format;
    }

    public String formatString() {
        return format;
    }
}
