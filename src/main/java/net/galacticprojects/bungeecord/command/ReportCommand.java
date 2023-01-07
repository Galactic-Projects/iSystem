package net.galacticprojects.bungeecord.command;

import eu.cloudnetservice.driver.CloudNetDriver;
import eu.cloudnetservice.modules.bridge.player.CloudPlayer;
import eu.cloudnetservice.modules.bridge.player.PlayerManager;
import me.lauriichan.laylib.command.annotation.*;
import me.lauriichan.laylib.localization.Key;
import net.galacticprojects.bungeecord.ProxyPlugin;
import net.galacticprojects.bungeecord.command.impl.BungeeActor;
import net.galacticprojects.bungeecord.config.ReportConfiguration;
import net.galacticprojects.bungeecord.config.info.ReportInfo;
import net.galacticprojects.bungeecord.message.CommandMessages;
import net.galacticprojects.bungeecord.util.Checkmode;
import net.galacticprojects.bungeecord.util.Type;
import net.galacticprojects.common.CommonPlugin;
import net.galacticprojects.common.database.SQLDatabase;
import net.galacticprojects.common.database.model.Report;
import net.galacticprojects.common.modules.discord.EmbedCreator;
import net.galacticprojects.common.util.ComponentParser;
import net.galacticprojects.common.util.MojangProfileService;
import net.galacticprojects.common.util.TimeHelper;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.time.OffsetDateTime;
import java.util.UUID;

@Command(name = "report", description = "Report a player")
public class ReportCommand {

    private Checkmode checkmode;

    @Action("create")
    public void create(BungeeActor<?> actor, CommonPlugin common, ProxyPlugin plugin, @Argument(name = "player") String name, @Argument(name = "reason", params = {
            @Param(type = 3, name = "minimum", intValue = 1),
            @Param(type = 3, name = "maximum", intValue = 5)
    }) int reportReason) {
        ProxiedPlayer player = actor.as(ProxiedPlayer.class).getHandle();
        UUID uniqueId = player.getUniqueId();
        UUID uniqueIdTarget = MojangProfileService.getUniqueId(name);
        ProxiedPlayer target = ProxyServer.getInstance().getPlayer(uniqueIdTarget);
        ReportConfiguration reportConfiguration = plugin.getReportConfiguration();
        ReportInfo reportInfo = reportConfiguration.getInfo(reportReason);

        SQLDatabase database = common.getDatabaseRef().get();
        if (database == null) {
            common.getLogger().error(new Throwable("Database ist unavailable"));
            return;
        }

        if (target == null) {
            actor.sendTranslatedMessage(CommandMessages.REPORT_ERRORS_ONLINE);
            return;
        }

        if (reportInfo.getReason() == null) {
            actor.sendTranslatedMessage(CommandMessages.REPORT_ERRORS_REASON);
            return;
        }


        actor.sendTranslatedMessage(CommandMessages.REPORT_SUCCESSFULLY, Key.of("player", target.getName()));
        database.createReport(target.getUniqueId(), player.getUniqueId(), reportInfo.getReason(), false, TimeHelper.toString(OffsetDateTime.now()));
        database.createHistory(target.getUniqueId(), player.getUniqueId(), Type.REPORT, reportInfo.getReason(), OffsetDateTime.now(), OffsetDateTime.now());
        new EmbedCreator(plugin.getBotConfiguration().getChannelCReport(), target.getName(), player.getName(), reportInfo.getReason(), target.getAddress().getAddress().getHostAddress(), TimeHelper.BAN_TIME_FORMATTER.format(OffsetDateTime.now()));
        new EmbedCreator(plugin.getBotConfiguration().getChannelNReport(), target.getName(), player.getName(), reportInfo.getReason(), target.getAddress().getAddress().getHostAddress(), TimeHelper.BAN_TIME_FORMATTER.format(OffsetDateTime.now()));
        TextComponent accept = new TextComponent();
        TextComponent deny = new TextComponent();
        for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
            if (all.hasPermission("system.command.report")) {
                database.getPlayer(all.getUniqueId()).thenAccept(allData -> {
                    all.sendMessage(ComponentParser.parse(common.getMessageManager().translate(CommandMessages.REPORT_TEAMFORMAT, allData.getLanguage(), Key.of("creator", player.getName()), Key.of("player", target.getName()), Key.of("reason", reportInfo.getReason()))));
                    String acceptM = common.getMessageManager().translate(CommandMessages.REPORT_TELEPORT, allData.getLanguage());
                    accept.setText(acceptM);
                    accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/report check"));
                    String denyM = common.getMessageManager().translate(CommandMessages.REPORT_IGNORE, allData.getLanguage());
                    deny.setText(denyM);
                    deny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/report ignore"));
                    all.sendMessage(ComponentParser.parse(common.getMessageManager().translate(CommandMessages.REPORT_COMMANDLINE, allData.getLanguage(), Key.of("accept", accept.toLegacyText()), Key.of("deny", deny.toLegacyText()))));
                });
            }
        }
    }

    @Action("check")
    @Permission("system.command.report")
    public void check(BungeeActor<?> actor, CommonPlugin common, ProxyPlugin plugin, @Argument(name = "player") String name) {
        ProxiedPlayer player = actor.as(ProxiedPlayer.class).getHandle();
        UUID uniqueId = player.getUniqueId();
        UUID uniqueIdTarget = MojangProfileService.getUniqueId(name);
        ProxiedPlayer target = ProxyServer.getInstance().getPlayer(uniqueIdTarget);

        SQLDatabase database = common.getDatabaseRef().get();
        if (database == null) {
            common.getLogger().error(new Throwable("Database ist unavailable"));
            return;
        }

        if (target == null) {
            actor.sendTranslatedMessage(CommandMessages.REPORT_ERRORS_GONE);
            return;
        }

        database.getReport(uniqueIdTarget).thenAccept(reportData -> {
            if (reportData.isStatus()) {
                actor.sendTranslatedMessage(CommandMessages.REPORT_ERRORS_ALREADY);
                return;
            }

            database.updateReport(new Report(reportData.getID(), uniqueIdTarget, reportData.getCreator(), reportData.getReason(), true, reportData.getTimestamp()));
            CloudPlayer cloudPlayer = CloudNetDriver.instance().serviceRegistry().firstProvider(PlayerManager.class).onlinePlayer(player.getUniqueId());
            CloudPlayer targetCloudPlayer = CloudNetDriver.instance().serviceRegistry().firstProvider(PlayerManager.class).onlinePlayer(target.getUniqueId());
            if (cloudPlayer == null || targetCloudPlayer == null) {
                return;
            }
            if (cloudPlayer.connectedService() == targetCloudPlayer.connectedService()) {
                actor.sendTranslatedMessage(CommandMessages.REPORT_ERRORS_CONNECTED);
                return;
            }
            cloudPlayer.loginService(targetCloudPlayer.connectedService());

            TextComponent finish = new TextComponent();
            database.getPlayer(player.getUniqueId()).thenAccept(playerData -> {
                finish.setText(common.getMessageManager().translate(CommandMessages.REPORT_PROCESS_FINISHBUTTON, playerData.getLanguage()));
                finish.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/report finish"));
                actor.sendTranslatedMessage(CommandMessages.REPORT_PROCESS_FINISHED, Key.of("player", MojangProfileService.getName(reportData.getUUID())), Key.of("button", finish.toLegacyText()));
            });
            checkmode = new Checkmode();
            checkmode.enable(uniqueId);
        });
    }

    @Action("finish")
    @Permission("system.command.report")
    public void finish(BungeeActor<?> actor, CommonPlugin common, ProxyPlugin plugin, @Argument(name = "player") String name) {
        ProxiedPlayer player = actor.as(ProxiedPlayer.class).getHandle();
        UUID targetUnique = MojangProfileService.getUniqueId(name);
        SQLDatabase database = common.getDatabaseRef().get();

        if (database == null) {
            common.getLogger().error(new Throwable("Database ist unavailable"));
            return;
        }


        database.getReport(targetUnique).thenAccept(reportData -> {
            UUID uniqueId = reportData.getCreator();
            ProxiedPlayer creator = ProxyServer.getInstance().getPlayer(uniqueId);

            if (creator == null) {
                return;
            }
            if(reportData.isStatus()) {

                database.createHistory(targetUnique, player.getUniqueId(), Type.REPORT_CLOSED, "-/-", OffsetDateTime.now(), OffsetDateTime.now());
                database.deleteReport(targetUnique, reportData.getCreator());
                database.getPlayer(creator.getUniqueId()).thenAccept(playerData -> {
                    creator.sendMessage(ComponentParser.parse(common.getMessageManager().translate(CommandMessages.REPORT_PROCESS_CLOSED, playerData.getLanguage(), Key.of("player", MojangProfileService.getName(targetUnique)))));
                });
                checkmode = new Checkmode();
                checkmode.disable(player.getUniqueId());
                return;
            }
            actor.sendTranslatedMessage("command.report.errors.processed");
        });


    }

    @Action("ignore")
    @Permission("system.command.report")
    public void ignore(BungeeActor<?> actor, CommonPlugin common, ProxyPlugin plugin){
        ProxiedPlayer player = actor.as(ProxiedPlayer.class).getHandle();
        for(int i = 0; i != 9; i++) {
            player.sendMessage(new TextComponent(""));
        }
    }
}
