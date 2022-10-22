package net.galacticprojects.bungeecord.command;

import me.lauriichan.laylib.command.annotation.Action;
import me.lauriichan.laylib.command.annotation.Argument;
import me.lauriichan.laylib.command.annotation.Command;
import me.lauriichan.laylib.localization.Key;
import net.galacticprojects.bungeecord.ProxyPlugin;
import net.galacticprojects.bungeecord.command.impl.BungeeActor;
import net.galacticprojects.bungeecord.message.CommandMessages;
import net.galacticprojects.common.CommonPlugin;
import net.galacticprojects.common.util.MojangProfileService;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.bukkit.command.ProxiedCommandSender;

import java.util.UUID;

@Command(name = "ping", description = "See your ping")
public class PingCommand {

    @Action("")
    public void ping(BungeeActor<?> actor, CommonPlugin common, ProxyPlugin plugin, @Argument(name = "player", optional = true) String name)  {
        ProxiedPlayer player = actor.as(ProxiedPlayer.class).getHandle();
        UUID uniqueId = player.getUniqueId();

        if(name != null) {
            UUID uniqueIdTarget = MojangProfileService.getUniqueId(name);
            ProxiedPlayer target = ProxyServer.getInstance().getPlayer(uniqueIdTarget);

            if(target == null) {
                actor.sendTranslatedMessage(CommandMessages.PING_ONLINE);
                return;
            }

            actor.sendTranslatedMessage(CommandMessages.PING_OTHERS, Key.of("player",  target.getName()), Key.of("input",  target.getPing()));
            return;
        }

        actor.sendTranslatedMessage(CommandMessages.PING_SELF, Key.of("input", player.getPing()));
    }
}
