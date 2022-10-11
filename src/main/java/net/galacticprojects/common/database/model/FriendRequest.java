package net.galacticprojects.common.database.model;

import java.util.UUID;

public class FriendRequest {

    private final UUID uniqueId;

    private final UUID requestUniqueId;

    private final String date;

    public FriendRequest(UUID uniqueId, UUID requestUniqueId, String date){
        this.uniqueId = uniqueId;
        this.requestUniqueId = requestUniqueId;
        this.date = date;
    }

    public UUID getUUID() {
        return uniqueId;
    }

    public UUID getRequestUUID() {
        return requestUniqueId;
    }

    public String getDate() {
        return date;
    }
}
