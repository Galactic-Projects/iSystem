package net.galacticprojects.common.database.model;

import java.time.OffsetDateTime;
import java.util.UUID;

public class Player {

    private final UUID uuid;
    private String ip;
    private String onlineTime;

    private String coins;
    private String language;
    private String level;

    public Player (UUID uuid, String ip, String onlineTime, String coins, String language, String level) {
        this.uuid = uuid;
        this.ip = ip;
        this.onlineTime = onlineTime;
        this.coins = coins;
        this.language = language;
        this.level = level;
    }

    public UUID getUUID() {
        return uuid;
    }

    public String getIP() {
        return ip;
    }

    public String getOnlineTime() { return onlineTime; }


    public String getCoins() {
        return coins;
    }

    public String getLanguage() {return language;}
    public String getLevel() { return level;}

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setOnlineTime(String onlineTime) {
        this.onlineTime = onlineTime;
    }

    public void setCoins(String coins) {
        this.coins = coins;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
