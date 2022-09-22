package net.galacticprojects.bungeecord.command;

import me.lauriichan.laylib.command.Actor;
import me.lauriichan.laylib.command.annotation.*;
import me.lauriichan.laylib.localization.Key;
import net.galacticprojects.bungeecord.ProxyPlugin;
import net.galacticprojects.bungeecord.config.BanConfiguration;
import net.galacticprojects.bungeecord.config.info.BanInfo;
import net.galacticprojects.bungeecord.entity.CommandPlayer;
import net.galacticprojects.common.database.model.Ban;
import net.galacticprojects.common.util.MojangProfileService;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;

import java.sql.SQLData;
import java.time.OffsetDateTime;
import java.util.UUID;

@Command(name = "ban", description = "command.description.ban")
public class BanCommand {

    @Action("create")
    @Permission("system.ban")
    public void banCreate(Actor<?> actor, ProxyPlugin plugin, @Argument(name = "player") String name, @Argument(index = 1, params = {
            @Param(type = 3, name = "min", intValue = 1),
            @Param(type = 3,name = "max", intValue = 99)
    }) int banId) {

        Actor<CommandSender> senderActor = actor.as(CommandSender.class);
        if(!senderActor.isValid()) {
            return;
        }
        CommandSender sender = senderActor.getHandle();
        UUID uniqueId = MojangProfileService.getUniqueId(name.startsWith("!") ? name.substring(1) : name);
        if(uniqueId == null) {
            sender.sendMessage(new TextComponent(senderActor.getTranslatedMessageAsString("generic.command.ban-player-not-found", Key.of("player", name))));
            return;
        }
        CommandPlayer commandPlayer = new CommandPlayer(uniqueId);
        if(commandPlayer.getBan() != null) {
            sender.sendMessage(new TextComponent(senderActor.getTranslatedMessageAsString("generic.command.ban-player-already-banned", Key.of("player", name))));
            return;
        }
        BanConfiguration banConfiguration = plugin.getBanConfiguration();
        BanInfo info = banConfiguration.getInfo(banId);
        if(info == null) {
            sender.sendMessage(new TextComponent(senderActor.getTranslatedMessageAsString("generic.command.ban-id-not-found")));
            return;
        }
        OffsetDateTime creation = OffsetDateTime.now();
        OffsetDateTime time = info.getHours() == 0 ? null : creation.plusHours(info.getHours());
        commandPlayer.banPlayer(actor.getId(), info.getReason(), time, creation);
        sender.sendMessage(new TextComponent(senderActor.getTranslatedMessageAsString("generic.command.ban-player-successfully", Key.of("player", name), Key.of("time", info.getHours() == 0 ? null : creation.plusHours(info.getHours())), Key.of("reason", info.getReason()))));
    }

    @Action("delete")
    @Permission("system.ban")
    public void banDelete(Actor<?> actor, @Argument(name = "player") String name) {
        Actor<CommandSender> senderActor = actor.as(CommandSender.class);
        if(!senderActor.isValid()) {
            return;
        }
        CommandSender sender = senderActor.getHandle();
        UUID uniqueId = MojangProfileService.getUniqueId(name.startsWith("!") ? name.substring(1) : name);
        if(uniqueId == null) {
            sender.sendMessage(new TextComponent(senderActor.getTranslatedMessageAsString("generic.command.ban-player-not-found", Key.of("player", name))));
            return;
        }
        CommandPlayer commandPlayer = new CommandPlayer(uniqueId);
        if(commandPlayer.getBan() == null) {
            sender.sendMessage(new TextComponent(senderActor.getTranslatedMessageAsString("generic.command.ban-player-not-banned")));
            return;
        }
        commandPlayer.deleteBan();
        sender.sendMessage(new TextComponent(senderActor.getTranslatedMessageAsString("generic.command.ban-player-successfully-unbanned")));
    }

}
