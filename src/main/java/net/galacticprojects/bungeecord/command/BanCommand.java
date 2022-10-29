package net.galacticprojects.bungeecord.command;

import me.lauriichan.laylib.command.Actor;
import me.lauriichan.laylib.command.annotation.*;
import me.lauriichan.laylib.localization.IMessage;
import me.lauriichan.laylib.localization.Key;
import me.lauriichan.laylib.localization.MessageProvider;
import net.galacticprojects.bungeecord.ProxyPlugin;
import net.galacticprojects.bungeecord.command.impl.BungeeActor;
import net.galacticprojects.bungeecord.config.BanConfiguration;
import net.galacticprojects.bungeecord.config.info.BanInfo;
import net.galacticprojects.bungeecord.message.BanMessage;
import net.galacticprojects.bungeecord.message.CommandMessages;
import net.galacticprojects.bungeecord.util.Type;
import net.galacticprojects.common.CommonPlugin;
import net.galacticprojects.common.database.SQLDatabase;
import net.galacticprojects.common.database.model.Ban;
import net.galacticprojects.common.database.model.Player;
import net.galacticprojects.common.discord.api.EmbedCreator;
import net.galacticprojects.common.util.ComponentParser;
import net.galacticprojects.common.util.MojangProfileService;
import net.galacticprojects.common.util.TimeHelper;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.sql.SQLData;
import java.time.OffsetDateTime;
import java.util.UUID;

@Command(name = "ban", description = "command.description.ban")
public class BanCommand {

    @Action("create")
    @Permission("system.ban")
    public void banCreate(BungeeActor<?> actor, CommonPlugin common, ProxyPlugin plugin, @Argument(name = "player") String name, @Argument(name = "ban id", params = {
            @Param(type = 3, name = "minimum", intValue = 1),
            @Param(type = 3, name = "maximum", intValue = 99)
    }) int banId) {

        if (!actor.isValid()) {
            return;
        }
        CommandSender sender = actor.getHandle();
        UUID uniqueId = MojangProfileService.getUniqueId(name.startsWith("!") ? name.substring(1) : name);
        if (uniqueId == null) {
            actor.sendTranslatedMessage(CommandMessages.COMMAND_GENERAL_PLAYER_NOT_FOUND, Key.of("player", name));
            return;
        }
        BanConfiguration banConfiguration = plugin.getBanConfiguration();
        BanInfo info = banConfiguration.getInfo(banId);
        if (info == null) {
            actor.sendTranslatedMessage(CommandMessages.COMMAND_BAN_ID_NOT_FOUND, Key.of("id", banId));
            return;
        }

        SQLDatabase database = common.getDatabaseRef().get();
        if (database == null) {
            common.getLogger().error(new Throwable("Database ist unavailable"));
            return;
        }

        if(database.getPlayer(uniqueId).join() == null) {
            actor.sendTranslatedMessage(CommandMessages.COMMAND_GENERAL_PLAYER_NOT_FOUND, Key.of("player", name));
            return;
        }

        database.getBan(uniqueId).thenAccept(existingBan -> {
            if (existingBan != null) {
                actor.sendTranslatedMessage(CommandMessages.COMMAND_BAN_CREATE_ALREADY_BANNED, Key.of("player", name));
                return;
            }
            OffsetDateTime creation = OffsetDateTime.now();
            OffsetDateTime time = OffsetDateTime.now().plusHours(info.getHours());
            database.banPlayer(uniqueId, actor.getId(), info.getReason(), time, creation).thenAccept(ban -> {
                if (ban == null) {
                    // ERROR, NOTHING HAPPENS!
                    return;
                }
                database.createHistory(uniqueId, actor.getId(), Type.BAN, info.getReason(), time, creation);
                database.getPlayer(uniqueId).thenAccept(players -> {
                    new EmbedCreator(plugin.getBotConfiguration().getChannelCBan(), MojangProfileService.getName(uniqueId), actor.getName(), info.getReason(), players.getIP(), info.getHours(), TimeHelper.toString(OffsetDateTime.now()));
                    new EmbedCreator(plugin.getBotConfiguration().getChannelNBan(), MojangProfileService.getName(uniqueId), actor.getName(), info.getReason(), players.getIP(), info.getHours(), TimeHelper.toString(OffsetDateTime.now()));
                });

                BanMessage message = BanMessage.valueOf("COMMAND_BAN_ID_" + banId);
                String message1 = common.getMessageManager().translate(message.id(), actor.getLanguage());
                ProxiedPlayer proxiedPlayer = ProxyServer.getInstance().getPlayer(uniqueId);
                actor.sendTranslatedMessage(CommandMessages.COMMAND_BAN_CREATE_SUCCESS, Key.of("player", name), Key.of("time", TimeHelper.BAN_TIME_FORMATTER.format(time)), Key.of("reason", message1));
                if (proxiedPlayer != null) {
                    proxiedPlayer.disconnect(ComponentParser.parse(common.getMessageManager().translate(CommandMessages.COMMAND_PLAYER_BANNED.getId(), "en-uk", Key.of("reason", message.id()), Key.of("time", TimeHelper.BAN_TIME_FORMATTER.format(time)))));
                }
            });
        });
    }

    @Action("delete")
    @Permission("system.ban")
    public void banDelete(Actor<?> actor, CommonPlugin common, ProxyPlugin plugin, @Argument(name = "player") String name) {
        Actor<CommandSender> senderActor = actor.as(CommandSender.class);
        if (!senderActor.isValid()) {
            return;
        }
        CommandSender sender = senderActor.getHandle();
        UUID uniqueId = MojangProfileService.getUniqueId(name.startsWith("!") ? name.substring(1) : name);
        if (uniqueId == null) {
            senderActor.sendTranslatedMessage(CommandMessages.COMMAND_GENERAL_PLAYER_NOT_FOUND, Key.of("player", name));
            return;
        }
        SQLDatabase database = common.getDatabaseRef().get();
        if (database == null) {
            // TODO: DB not availble
            return;
        }
        database.getBan(uniqueId).thenAccept(existingBan -> {
            if (existingBan == null) {
                senderActor.sendTranslatedMessage(CommandMessages.COMMAND_BAN_NOT_BANNED, Key.of("player", name));
                return;
            }
            database.deleteBan(uniqueId);
            database.createHistory(uniqueId, actor.getId(), Type.UNBAN, "-/-", OffsetDateTime.now(), OffsetDateTime.now());
            senderActor.sendTranslatedMessage(CommandMessages.COMMAND_BAN_DELETE_SUCCESS, Key.of("player", name));
        });
    }

}
