package net.galacticprojects.bungeecord.config.info;

public class BanInfo {

    private final String reason;
    private final long hours;

    public BanInfo(final String reason, final long hours) {
        this.reason = reason;
        this.hours = hours;
    }

    public long getHours() {
        return hours;
    }

    public String getReason() {
        return reason;
    }

}
