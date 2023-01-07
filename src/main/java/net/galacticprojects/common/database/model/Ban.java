package net.galacticprojects.common.database.model;

import net.galacticprojects.common.util.MojangProfileService;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

public class Ban {

    private final UUID player;
    private final UUID owner;
    private final int id;
    private final int banid;
    private final String reason;
    private final OffsetDateTime time;
    private final OffsetDateTime creationTime;

    public Ban(final UUID player, final UUID owner, final int id, final int banid, final String reason, final OffsetDateTime time, final OffsetDateTime creationTime) {
        this.player = player;
        this.owner = owner;
        this.id = id;
        this.banid = banid;
        this.reason = reason;
        this.time = time;
        this.creationTime = creationTime;
    }

    public boolean isPermanent() {
        return time == null;
    }

    public boolean isExpired() {
        return time != null && OffsetDateTime.now().isAfter(time);
    }

    public UUID getPlayer() {
        return player;
    }

    public String getPlayerName() {
        return MojangProfileService.getName(player);
    }

    public Optional<ProxiedPlayer> getPlayerAsPlayer() {
        return Optional.ofNullable(player).map(ProxyServer.getInstance()::getPlayer);
    }

    public UUID getOwner() {
        return owner;
    }

    public String getOwnerName() {
        if (!isOwnerPlayer()) {
            return "GalacticProjects";
        }
        return MojangProfileService.getName(owner);
    }

    public boolean isOwnerPlayer() {
        return owner != null;
    }

    public Optional<ProxiedPlayer> getOwnerAsPlayer() {
        return Optional.ofNullable(owner).map(ProxyServer.getInstance()::getPlayer);
    }

    public String getReason() {
        return reason;
    }

    public OffsetDateTime getTime() {
        return time;
    }

    public OffsetDateTime getCreationTime() {
        return creationTime;
    }

    public int getID() {
        return id;
    }

    public int getBanID() {
        return banid;
    }
}
