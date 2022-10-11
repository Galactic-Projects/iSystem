package net.galacticprojects.common.database.model;

import java.util.UUID;

public class FriendSettings {

    private final UUID uniqueId;

    private final boolean jump;

    private final boolean messages;

    private final boolean requests;

    public FriendSettings(UUID uniqueId, boolean jump, boolean messages, boolean requests) {
        this.uniqueId = uniqueId;
        this.jump = jump;
        this.messages = messages;
        this.requests = requests;
    }

    public UUID getUUID() {
        return uniqueId;
    }

    public boolean isJump() {
        return jump;
    }

    public boolean isMessages() {
        return messages;
    }

    public boolean isRequests() {
        return requests;
    }
}
