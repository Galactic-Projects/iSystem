package net.galacticprojects.isystem.database.model;

import net.galacticprojects.isystem.bungeecord.command.ban.BanType;

import java.time.OffsetDateTime;
import java.util.UUID;

public class Ban {

    private final UUID player;
    private final String staff;
    private final String ip;

    private final String reason;
    private final BanType type;
    private final OffsetDateTime endTime;
    private final OffsetDateTime creationTime;
    private final int duration;

    public Ban(UUID player, String staff, String ip, String reason, BanType type, OffsetDateTime endTime, OffsetDateTime creationTime, int duration) {
        this.player = player;
        this.staff = staff;
        this.ip = ip;
        this.reason = reason;
        this.type = type;
        this.endTime = endTime;
        this.creationTime = creationTime;
        this.duration = duration;
    }

    public UUID getPlayer() {
        return player;
    }

    public boolean isPermanent() {
        return endTime == null;
    }

    public boolean isExpired() {
        return endTime != null && OffsetDateTime.now().isAfter(endTime);
    }

    public String getStaff() {
        return staff;
    }

    public String getIp() {
        return ip;
    }

    public String getReason() {
        return reason;
    }

    public BanType getType() {
        return type;
    }

    public OffsetDateTime getEndTime() {
        return endTime;
    }

    public OffsetDateTime getCreationTime() {
        return creationTime;
    }

    public int getDuration() {
        return duration;
    }


}
