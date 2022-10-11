package net.galacticprojects.common.database.model;

import java.util.UUID;

public class Chatlog {

    private final UUID uuid;
    private final String name;
    private final String ip;
    private final String server;
    private final String timestamp;
    private final String message;

    public Chatlog(UUID uuid, String name, String ip, String server, String timestamp, String message) {
        this.uuid = uuid;
        this.name = name;
        this.ip = ip;
        this.server = server;
        this.timestamp = timestamp;
        this.message = message;
    }

    public UUID getUUID() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public String getIp() {
        return ip;
    }

    public String getServer() {
        return server;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }
}
