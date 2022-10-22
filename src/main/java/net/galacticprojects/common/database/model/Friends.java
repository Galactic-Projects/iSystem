package net.galacticprojects.common.database.model;

import java.util.UUID;

public class Friends {

    private final UUID uuid;
    private final UUID friendUniqueId;
    private final String date;

    public Friends(UUID uuid, UUID friendUniqueId, String date) {
        this.uuid = uuid;
        this.friendUniqueId = friendUniqueId;
        this.date = date;
    }

    public UUID getUUID() {
        return uuid;
    }

    public UUID getFriendUniqueId() {
        return friendUniqueId;
    }

    public String getDate() {
        return date;
    }
}
