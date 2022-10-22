package net.galacticprojects.bungeecord.command;

import me.lauriichan.laylib.command.annotation.Action;
import me.lauriichan.laylib.command.annotation.Argument;
import me.lauriichan.laylib.command.annotation.Command;
import me.lauriichan.laylib.command.annotation.Param;
import net.galacticprojects.bungeecord.ProxyPlugin;
import net.galacticprojects.bungeecord.command.impl.BungeeActor;
import net.galacticprojects.bungeecord.config.ReportConfiguration;
import net.galacticprojects.bungeecord.config.info.ReportInfo;
import net.galacticprojects.common.CommonPlugin;
import net.galacticprojects.common.util.MojangProfileService;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;

@Command(name = "report", description = "Report a player")
public class ReportCommand {

    @Action("create")
    public void create(BungeeActor<?> actor, CommonPlugin common, ProxyPlugin plugin, @Argument(name = "player") String name, @Argument(name = "reason", params = {
            @Param(type = 3, name = "minimum", intValue = 1),
            @Param(type = 3, name = "maximum", intValue = 5)
    }) int reportReason) {
        ProxiedPlayer player = actor.as(ProxiedPlayer.class).getHandle();
        UUID uniqueId = player.getUniqueId();
        UUID uniqueIdTarget = MojangProfileService.getUniqueId(name);
        ReportConfiguration reportConfiguration = plugin.getReportConfiguration();
        ReportInfo reportInfo = reportConfiguration.getInfo(reportReason);
    }
}
