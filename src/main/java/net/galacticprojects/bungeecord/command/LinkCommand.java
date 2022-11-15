package net.galacticprojects.bungeecord.command;

import me.lauriichan.laylib.command.annotation.Action;
import me.lauriichan.laylib.command.annotation.Argument;
import me.lauriichan.laylib.command.annotation.Command;
import net.galacticprojects.bungeecord.ProxyPlugin;
import net.galacticprojects.bungeecord.command.impl.BungeeActor;
import net.galacticprojects.common.CommonPlugin;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;

@Command(name = "link", description = "Link your accounts")
public class LinkCommand {

    @Action("discord")
    public void discord(BungeeActor<?> actor, CommonPlugin common, ProxyPlugin plugin, @Argument(name = "discord tag") String tag) {
        ProxiedPlayer player = actor.as(ProxiedPlayer.class).getHandle();
        UUID uniqueId = player.getUniqueId();


    }

    @Action("teamspeak")
    public void teamspeak(BungeeActor<?> actor, CommonPlugin common, ProxyPlugin plugin) {
        ProxiedPlayer player = actor.as(ProxiedPlayer.class).getHandle();
        UUID uniqueId = player.getUniqueId();

    }
}
