package net.galacticprojects.isystem.database.model;

import net.galacticprojects.isystem.utils.Languages;

import java.time.OffsetDateTime;
import java.util.UUID;

public class Player {

    private final UUID uuid;
    private final String name;
    private final String ip;
    private final String country;
    private final long onlineTime;
    private final Languages languages;
    private final OffsetDateTime firstJoin;
    private final String serverOnline;
    private final OffsetDateTime latestJoin;
    private final boolean report;
    private final boolean teamchat;

    public Player (UUID uuid, String name, String ip, String country, long onlineTime, Languages languages, OffsetDateTime firstJoin, String serverOnline, OffsetDateTime latestJoin, boolean report, boolean teamchat) {
        this.uuid = uuid;
        this.name = name;
        this.ip = ip;
        this.country = country;
        this.onlineTime = onlineTime;
        this.languages = languages;
        this.firstJoin = firstJoin;
        this.serverOnline = serverOnline;
        this.latestJoin = latestJoin;
        this.report = report;
        this.teamchat = teamchat;
    }

    public UUID getUUID() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public String getIP() {
        return ip;
    }

    public String getCountry() {
        return country;
    }

    public long getOnlineTime() { return onlineTime; }

    public Languages getLanguages() {
        return languages;
    }

    public OffsetDateTime getFirstJoin() {
        return firstJoin;
    }

    public String getServerOnline() {
        return serverOnline;
    }

    public OffsetDateTime getLatestJoin() {
        return latestJoin;
    }

    public boolean isReport() {
        return report;
    }

    public boolean isTeamchat() {
        return teamchat;
    }
}
