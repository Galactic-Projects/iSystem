package net.galacticprojects.common.database.model;

import java.util.UUID;

public class Report {

    private final UUID uuid;
    private final UUID creator;
    private final String reason;
    private final boolean status;
    private final String timestamp;

    public Report(UUID uuid, UUID creator, String reason, boolean status, String timestamp) {
        this.uuid = uuid;
        this.creator = creator;
        this.reason = reason;
        this.status = status;
        this.timestamp = timestamp;
    }

    public UUID getUUID() {
        return uuid;
    }

    public UUID getCreator() {
        return creator;
    }

    public String getReason() {
        return reason;
    }

    public boolean isStatus() {
        return status;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
