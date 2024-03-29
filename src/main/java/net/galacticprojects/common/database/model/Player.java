package net.galacticprojects.common.database.model;

import java.time.OffsetDateTime;
import java.util.UUID;

public class Player {

    private final UUID uuid;
    private String ip;
    private long onlineTime;

    private int coins;
    private String language;
    private int level;
    private boolean verified;

    public Player (UUID uuid, String ip, long onlineTime, int coins, String language, int level, boolean verified) {
        this.uuid = uuid;
        this.ip = ip;
        this.onlineTime = onlineTime;
        this.coins = coins;
        this.language = language;
        this.level = level;
        this.verified = verified;
    }

    public UUID getUUID() {
        return uuid;
    }

    public String getIP() {
        return ip;
    }

    public long getOnlineTime() { return onlineTime; }


    public int getCoins() {
        return coins;
    }

    public String getLanguage() {return language;}
    public int getLevel() { return level;}

    public boolean getVerified() {
        return this.verified;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setOnlineTime(long onlineTime) {
        this.onlineTime = onlineTime;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }
}
