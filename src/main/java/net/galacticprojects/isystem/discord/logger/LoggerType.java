package net.galacticprojects.isystem.discord.logger;

import net.galacticprojects.isystem.bungeecord.config.DiscordConfiguration;
import net.galacticprojects.isystem.utils.JavaInstance;

import java.util.Objects;

public enum LoggerType {

    LINK(Long.parseLong(Objects.requireNonNull(JavaInstance.get(DiscordConfiguration.class)).getChannelLogLink())),
    BAN(Long.parseLong(Objects.requireNonNull(JavaInstance.get(DiscordConfiguration.class)).getChannelLogBan())),
    REPORT(Long.parseLong(Objects.requireNonNull(JavaInstance.get(DiscordConfiguration.class)).getChannelLogReport())),
    DISCORD_WARN(Long.parseLong(Objects.requireNonNull(JavaInstance.get(DiscordConfiguration.class)).getChannelLogDiscordWarn())),
    DISCORD_BAN(Long.parseLong(Objects.requireNonNull(JavaInstance.get(DiscordConfiguration.class)).getChannelLogDiscordBan()));

    long id;

    LoggerType(long id) {this.id = id;}

    Long getId(LoggerType type) {return type.id;}
}
