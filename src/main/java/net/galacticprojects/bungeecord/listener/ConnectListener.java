package net.galacticprojects.bungeecord.listener;

import me.lauriichan.laylib.localization.Key;
import net.galacticprojects.bungeecord.ProxyPlugin;
import net.galacticprojects.bungeecord.config.PluginConfiguration;
import net.galacticprojects.bungeecord.message.SystemMessage;
import net.galacticprojects.bungeecord.util.TablistManager;
import net.galacticprojects.bungeecord.util.TimeHelper;
import net.galacticprojects.common.CommonPlugin;
import net.galacticprojects.common.database.SQLDatabase;
import net.galacticprojects.common.database.model.Player;
import net.galacticprojects.common.util.ComponentParser;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.sql.SQLData;
import java.time.OffsetDateTime;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ConnectListener implements Listener {

    private ProxyPlugin plugin;

    public ConnectListener(ProxyPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onConnect(PostLoginEvent event) {
        ProxiedPlayer player = event.getPlayer();
        UUID uniqueId = player.getUniqueId();
        SQLDatabase database = plugin.getCommonPlugin().getDatabaseRef().get();
        PluginConfiguration configuration = plugin.getPluginConfiguration();
        CommonPlugin commonPlugin = plugin.getCommonPlugin();
        Player playerData = database.getPlayer(uniqueId).join();
        String language = "en-uk";
        if (playerData.getLanguage() != null) {
            language = playerData.getLanguage();
        }

        new TablistManager(plugin, player);

        if (configuration.isMaintenance()) {
            if (!(player.hasPermission("system.maintenance.bypass") || player.hasPermission("system.maintenance.*"))) {
                Key reason = Key.of("reason", commonPlugin.getMessageManager().translate(SystemMessage.SYSTEM_MAINTENANCE_KICK_REASON, language, Key.of("reason", configuration.getMaintenanceReason())));
                Key enddate = Key.of("enddate", TimeHelper.format(language).format(TimeHelper.fromString(configuration.getDays())));
                String finalLanguage = language;
                player.disconnect(ComponentParser.parse(commonPlugin.getMessageManager().translate(SystemMessage.SYSTEM_MAINTENACE_KICK_CURRENTLY, finalLanguage, enddate, reason)));
                //CloudNetDriver.getInstance(). //(BridgePlayerManager.getInstance().getOnlinePlayer(uniqueId), commonPlugin.getMessageManager().translate(SystemMessage.SYSTEM_MAINTENACE_KICK_CURRENTLY, finalLanguage, enddate, reason));
            }
        }

        if (playerData == null) {
            database.createPlayer(uniqueId, player.getAddress().getAddress().getHostAddress().toString(), 1000, 1, "en-uk", 0L).isDone();
            return;
        }
        /*Ban ban = database.getBan(uniqueId).join();
        if (ban != null) {
            BanMessage message = BanMessage.valueOf("COMMAND_BAN_ID_" + ban.getId());
            player.disconnect(new TextComponent(ComponentParser.parse(plugin.getCommonPlugin().getMessageManager().translate(CommandMessages.COMMAND_PLAYER_BANNED, playerData.getLanguage(), Key.of("time", TimeHelper.BAN_TIME_FORMATTER.format(ban.getTime())), Key.of("reason", message)))));
        }*/

    }

}
