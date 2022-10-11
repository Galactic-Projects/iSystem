package net.galacticprojects.bungeecord.command;

import me.lauriichan.laylib.command.Actor;
import me.lauriichan.laylib.command.annotation.*;
import me.lauriichan.laylib.localization.Key;
import net.galacticprojects.bungeecord.ProxyPlugin;
import net.galacticprojects.bungeecord.command.impl.BungeeActor;
import net.galacticprojects.bungeecord.config.PluginConfiguration;
import net.galacticprojects.bungeecord.message.SystemMessage;
import net.galacticprojects.bungeecord.util.TimeHelper;
import net.galacticprojects.common.CommonPlugin;
import net.galacticprojects.common.database.SQLDatabase;
import net.galacticprojects.common.database.model.Player;
import net.galacticprojects.common.util.ComponentParser;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.lang.reflect.Proxy;
import java.time.OffsetDateTime;


@Command(name = "maintenance")
public final class MaintenanceCommand {

	@Action("")
	@Description("command.description.maintenance")
	@Permission("system.command.maintenance")
	public void maintenance(Actor<?> actor, CommonPlugin common, ProxyPlugin plugin, @Argument(name = "reason", optional = true) String reason, @Argument(name = "days", params = {
			@Param(type = 2, name = "minimum", intValue = 1),
			@Param(type = 2, name = "maximum", intValue = 365)
	}) int days) {
		PluginConfiguration config = plugin.getPluginConfiguration();
		SQLDatabase database = plugin.getCommonPlugin().getDatabaseRef().get();

		if(database == null) {
			common.getLogger().error(new Throwable("Database is unavailable!"));
			return;
		}
		
		if(!config.isMaintenance()) {
			config.setMaintenance(true);
			config.setMaintenanceReason(reason = (reason == null ? "Maintenance" : reason));
			config.setDays(TimeHelper.toString(OffsetDateTime.now().plusDays(days)));
			actor.sendTranslatedMessage("command.maintenance.on", Key.of("reason", reason), Key.of("enddate", TimeHelper.BAN_TIME_FORMATTER.format(TimeHelper.fromString(config.getDays()))));

			for(ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
				if (!(all.hasPermission("system.maintenance.bypass") || all.hasPermission("system.maintenance.*"))) {
					Player playerData = database.getPlayer(all.getUniqueId()).join();
					String language = "en-uk";
					if(playerData.getLanguage() != null) {
						language = playerData.getLanguage();
					}
					all.disconnect(ComponentParser.parse(common.getMessageManager().translate(SystemMessage.SYSTEM_MAINTENACE_KICK_NOW, language, Key.of("enddate", TimeHelper.BAN_TIME_FORMATTER.format(TimeHelper.fromString(config.getDays()))), Key.of("reason", reason))));
				}
			}
			return;
		}
		config.setMaintenance(false);
		actor.sendTranslatedMessage("command.maintenance.off");
	}
}
