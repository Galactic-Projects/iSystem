package net.galacticprojects.bungeecord.command;

import me.lauriichan.laylib.command.annotation.*;
import me.lauriichan.laylib.localization.Key;
import net.galacticprojects.bungeecord.ProxyPlugin;
import net.galacticprojects.bungeecord.command.impl.BungeeActor;
import net.galacticprojects.bungeecord.message.CommandMessages;
import net.galacticprojects.bungeecord.message.SystemMessage;
import net.galacticprojects.common.CommonPlugin;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;

@Command(name = "system", description = "Manage the system!")
public class SystemCommand {

    @Action("setmaxplayers")
    @Permission("system.command.administration")
    public void setMaxPlayers(BungeeActor<?> actor, CommonPlugin common, ProxyPlugin plugin, @Argument(name = "amount", params = {
            @Param(type=1, name = "minimum", intValue = 0)
    }) int amount) {
        ProxiedPlayer player = actor.as(ProxiedPlayer.class).getHandle();
        UUID uniqueId = player.getUniqueId();
        int playerAmount = Integer.parseInt(plugin.getPluginConfiguration().getPlayerAmount());

        if(amount < ProxyServer.getInstance().getOnlineCount()) {
            actor.sendTranslatedMessage(CommandMessages.SYSTEM_ADMINISTRATION_ERRORS_SMALL);
            return;
        }

        if (amount == ProxyServer.getInstance().getOnlineCount()) {
            actor.sendTranslatedMessage(CommandMessages.SYSTEM_ADMINISTRATION_ERRORS_SAME);
            return;
        }

        if(amount < 1000000) {
            actor.sendTranslatedMessage(CommandMessages.SYSTEM_ADMINISTRATION_ERRORS_REALISTIC);
            return;
        }

        if(amount == playerAmount) {
            actor.sendTranslatedMessage(CommandMessages.SYSTEM_ADMINISTRATION_ERRORS_ALREADY);
            return;
        }

        plugin.getPluginConfiguration().setPlayerAmount(amount);
        plugin.getPluginConfiguration().save();
        actor.sendTranslatedMessage(CommandMessages.SYSTEM_ADMINISTRATION_SUCCESS_SETTED, Key.of("oamount", playerAmount), Key.of("namount", amount));
    }

    @Action("reload")
    @Permission("system.command.administration")
    public void reload(BungeeActor<?> actor, CommonPlugin common, ProxyPlugin plugin) {
        ProxiedPlayer player = actor.as(ProxiedPlayer.class).getHandle();

        plugin.reloadConfigurations();
        actor.sendTranslatedMessage(CommandMessages.SYSTEM_ADMINISTRATION_CONFIG_RELOADED);
    }
}
