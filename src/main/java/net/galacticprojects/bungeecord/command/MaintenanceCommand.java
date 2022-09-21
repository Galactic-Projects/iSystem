package net.galacticprojects.bungeecord.command;

import me.lauriichan.laylib.command.annotation.*;
import me.lauriichan.laylib.localization.Key;
import net.galacticprojects.bungeecord.ProxyPlugin;
import net.galacticprojects.bungeecord.command.impl.BungeeActor;
import net.galacticprojects.bungeecord.config.PluginConfiguration;
import net.galacticprojects.common.CommonPlugin;
import net.md_5.bungee.api.connection.ProxiedPlayer;


@Command(name = "maintenance", description = "command.description.maintenance")
public final class MaintenanceCommand {

	@Action("")
	@Permission("system.command.maintenance")
	public void maintenance(CommonPlugin common, ProxyPlugin plugin, @Argument(name = "reason", optional = true) String reason) {
		PluginConfiguration config = plugin.getPluginConfiguration();
		
		if(!config.isMaintenance()) {
			config.setMaintenance(true);
			config.setMaintenanceReason(reason = (reason == null ? "Maintenance" : reason));
			Iterable<ProxiedPlayer> players = plugin.getProxy().getPlayers();
			for(ProxiedPlayer player : players) {
				new BungeeActor<ProxiedPlayer>(player, common.getMessageManager()).sendTranslatedMessage("command.maintenance.on", Key.of("reason", reason));
			}
			return;
		}
		config.setMaintenance(false);
		Iterable<ProxiedPlayer> players = plugin.getProxy().getPlayers();
		for(ProxiedPlayer player : players) {
			new BungeeActor<ProxiedPlayer>(player, common.getMessageManager()).sendTranslatedMessage("command.maintenance.off");
		}
	}
	
	
	
}
