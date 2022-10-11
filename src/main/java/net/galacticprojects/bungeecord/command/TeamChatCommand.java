package net.galacticprojects.bungeecord.command;

import me.lauriichan.laylib.command.annotation.Action;
import me.lauriichan.laylib.command.annotation.Command;
import net.galacticprojects.bungeecord.command.impl.BungeeActor;
import net.galacticprojects.bungeecord.message.CommandMessages;
import net.galacticprojects.bungeecord.message.SystemMessage;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import java.util.UUID;
import java.util.ArrayList;
import net.galacticprojects.common.CommonPlugin;
import net.galacticprojects.common.util.ComponentParser;

@Command(name = "teamchat")
public class TeamChatCommand {
    public static ArrayList<UUID> TEAMCHAT = new ArrayList<>();

    @Action("")
    public void teamChat(BungeeActor<?> actor, CommonPlugin plugin) {
        ProxiedPlayer player = actor.as(ProxiedPlayer.class).getHandle();
        if(player.hasPermission("system.teamchat")) {
             if(TEAMCHAT.contains(player.getUniqueId())) {
                 TEAMCHAT.remove(player.getUniqueId());
                 plugin.getDatabaseRef().asOptional().ifPresent(sql -> {
                     sql.getPlayer(player.getUniqueId()).thenAccept(playerData -> {
                         player.sendMessage(ComponentParser.parse(plugin.getMessageManager().translate(CommandMessages.COMMAND_TEAMCHAT_LEFT, playerData.getLanguage())));
                     });
                 });
                return;
             }
             TEAMCHAT.add(player.getUniqueId());
             plugin.getDatabaseRef().asOptional().ifPresent(sql -> {
                 sql.getPlayer(player.getUniqueId()).thenAccept(playerData -> {
                     player.sendMessage(ComponentParser.parse(plugin.getMessageManager().translate(CommandMessages.COMMAND_TEAMCHAT_LEFT, playerData.getLanguage())));
                 });
             });
           return;
        }
        plugin.getDatabaseRef().asOptional().ifPresent(sql -> {
            sql.getPlayer(player.getUniqueId()).thenAccept(playerData -> {
                player.sendMessage(ComponentParser.parse(plugin.getMessageManager().translate(SystemMessage.SYSTEM_NO_PERMISSION, playerData.getLanguage())));
            });
        });
    }


}
