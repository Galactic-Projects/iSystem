package net.galacticprojects.bungeecord.command;

import me.lauriichan.laylib.command.annotation.Action;
import me.lauriichan.laylib.command.annotation.Argument;
import me.lauriichan.laylib.command.annotation.Command;
import me.lauriichan.laylib.command.annotation.Permission;
import me.lauriichan.laylib.localization.Key;
import net.galacticprojects.bungeecord.ProxyPlugin;
import net.galacticprojects.bungeecord.command.impl.BungeeActor;
import net.galacticprojects.bungeecord.message.CommandMessages;
import net.galacticprojects.common.CommonPlugin;
import net.galacticprojects.common.util.ComponentParser;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
@Command(name = "broadcast", description = "Broadcast a message over the whole network!")
public class BroadcastCommand {

    @Action("")
    @Permission("system.command.broadcast")
    public void broadcast(BungeeActor<?> actor, CommonPlugin common, ProxyPlugin plugin, @Argument(name = "message") String message) {

        for(ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
            common.getDatabaseRef().asOptional().ifPresent(sql ->{
                sql.getPlayer(all.getUniqueId()).thenAccept(playerData -> {
                   all.sendMessage(ComponentParser.parse(common.getMessageManager().translate(CommandMessages.BROADCAST_COMMAND, playerData.getLanguage(), Key.of("message", message))));
                });
            });
        }
    }
}
