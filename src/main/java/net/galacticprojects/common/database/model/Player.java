package net.galacticprojects.common.database.model;

import java.time.OffsetDateTime;
import java.util.UUID;

public class Player {

    private final UUID uuid;
    private final String ip;
    private final long onlineTime;

    private final int coins;
    private final String language;
    private final int level;

    public Player (UUID uuid, String ip, long onlineTime, int coins, String language, int level) {
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

    public long getOnlineTime() { return onlineTime; }


    public int getCoins() {
        return coins;
    }

    public String getLanguage() {return language;}
    public int getLevel() {return level;}
}
