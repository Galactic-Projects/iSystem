package net.galacticprojects.common.database.model;

import java.time.OffsetDateTime;
import java.util.UUID;

public class LinkPlayer {

    private final UUID uniqueId;
    private String discordTag;
    private OffsetDateTime discordTime;
    private boolean discordLinked;
    private String teamspeakIdentifier;
    private String teamspeakIp;
    private OffsetDateTime teamspeakTime;

    private boolean teamspeakLinked;


    public LinkPlayer(UUID uniqueId, String discordTag, OffsetDateTime discordTime, boolean discordLinked, String teamspeakIdentifier, String teamspeakIp, OffsetDateTime teamspeakTime, boolean teamspeakLinked) {
        this.uniqueId = uniqueId;
        this.discordTag = discordTag;
        this.discordLinked = discordLinked;
        this.teamspeakIdentifier = teamspeakIdentifier;
        this.teamspeakIp = teamspeakIp;
        this.teamspeakTime = teamspeakTime;
        this.teamspeakLinked = teamspeakLinked;
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public String getDiscordTag() {
        return discordTag;
    }

    public OffsetDateTime getDiscordTime() {
        return discordTime;
    }

    public boolean isDiscordLinked() {
        return discordLinked;
    }

    public String getTeamspeakIdentifier() {
        return teamspeakIdentifier;
    }

    public String getTeamspeakIp() {
        return teamspeakIp;
    }

    public OffsetDateTime getTeamspeakTime() {
        return teamspeakTime;
    }

    public boolean isTeamspeakLinked() {
        return teamspeakLinked;
    }

    public void setDiscordTag(String discordTag) {
        this.discordTag = discordTag;
    }

    public void setDiscordTime(OffsetDateTime discordTime) {
        this.discordTime = discordTime;
    }

    public void setDiscordLinked(boolean discordLinked) {
        this.discordLinked = discordLinked;
    }

    public void setTeamspeakIdentifier(String teamspeakIdentifier) {
        this.teamspeakIdentifier = teamspeakIdentifier;
    }

    public void setTeamspeakIp(String teamspeakIp) {
        this.teamspeakIp = teamspeakIp;
    }

    public void setTeamspeakTime(OffsetDateTime teamspeakTime) {
        this.teamspeakTime = teamspeakTime;
    }

    public void setTeamspeakLinked(boolean teamspeakLinked) {
        this.teamspeakLinked = teamspeakLinked;
    }
}
