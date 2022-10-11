package net.galacticprojects.bungeecord.util;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.ext.bridge.BridgePlayerManager;
import de.dytanic.cloudnet.ext.bridge.BridgePlayerProvider;
import de.dytanic.cloudnet.ext.bridge.player.ICloudPlayer;
import de.dytanic.cloudnet.ext.bridge.player.IPlayerManager;
import me.lauriichan.laylib.localization.Key;
import net.galacticprojects.bungeecord.ProxyPlugin;
import net.galacticprojects.bungeecord.config.PluginConfiguration;
import net.galacticprojects.common.database.SQLDatabase;
import net.galacticprojects.common.database.model.Player;
import net.galacticprojects.common.util.ComponentParser;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.protocol.AbstractPacketHandler;
import net.md_5.bungee.protocol.DefinedPacket;
import net.md_5.bungee.protocol.PacketWrapper;
import net.md_5.bungee.protocol.packet.PlayerListHeaderFooter;
import net.md_5.bungee.protocol.packet.TabCompleteResponse;

import java.nio.ByteBuffer;
import java.sql.SQLData;
import java.util.concurrent.TimeUnit;

public class TablistManager {

    public TablistManager(ProxyPlugin plugin, ProxiedPlayer player) {
        SQLDatabase database = plugin.getCommonPlugin().getDatabaseRef().get();

        if (database == null) {
            plugin.getCommonPlugin().getLogger().error("Database is not available!");
            return;
        }
        Player playerData = database.getPlayer(player.getUniqueId()).join();

        ProxyServer.getInstance().getScheduler().schedule(plugin, new Runnable() {
            @Override
            public void run() {
                if(ProxyServer.getInstance().getPlayers().size() == 0) {
                    return;
                }

                String language = "en-uk";

                if (playerData.getLanguage() != null) {
                    language = playerData.getLanguage();
                }
                ICloudPlayer cloudPlayer = CloudNetDriver.getInstance().getServicesRegistry().getFirstService(IPlayerManager.class).getOnlinePlayer(player.getUniqueId());

                if(cloudPlayer == null) {
                    return;
                }

                Key server = Key.of("server", cloudPlayer.getConnectedService().getTaskName());
                Key players = Key.of("online", plugin.getProxy().getOnlineCount());
                Key maxPlayers = Key.of("max", plugin.getPluginConfiguration().getPlayerAmount());

                String header = plugin.getCommonPlugin().getMessageManager().translate("system.tablist.header", language, server, players, maxPlayers);
                String footer = plugin.getCommonPlugin().getMessageManager().translate("system.tablist.footer", language);
                player.setTabHeader(ComponentParser.parse(header), ComponentParser.parse(footer));
            }
        }, 1L, 1L, TimeUnit.SECONDS);
    }


    public void reset(ProxiedPlayer player) {
        player.resetTabHeader();
    }

}
