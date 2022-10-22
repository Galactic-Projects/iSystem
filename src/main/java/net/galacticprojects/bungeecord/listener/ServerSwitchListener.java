package net.galacticprojects.bungeecord.listener;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.driver.service.ServiceInfoSnapshot;
import de.dytanic.cloudnet.ext.bridge.player.IPlayerManager;
import net.galacticprojects.bungeecord.ProxyPlugin;
import net.galacticprojects.bungeecord.party.Party;
import net.galacticprojects.bungeecord.party.PartyManager;
import net.galacticprojects.common.util.ComponentParser;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import org.w3c.dom.Text;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ServerSwitchListener implements Listener {

    ProxyPlugin plugin;

    public ServerSwitchListener(ProxyPlugin plugin) {
        this.plugin = plugin;
    }
    @EventHandler
    public void onServerSwitch(ServerSwitchEvent event) {
        ProxiedPlayer player = event.getPlayer();

        for(int i = 0; i != 99; i++){
            player.sendMessage(ComponentParser.parse(""));
        }

        Party party = new PartyManager(player).getParty();

        if(party == null) {
            return;
        }
        if(party.getLeader() != player.getUniqueId()) {
            return;
        }
        ProxyServer.getInstance().getScheduler().schedule(plugin, () -> {
            IPlayerManager manager = CloudNetDriver.getInstance().getServicesRegistry().getFirstService(IPlayerManager.class);
            UUID serviceUUID = manager.getOnlinePlayer(player.getUniqueId()).getConnectedService().getUniqueId();
            for(UUID uniqueId : party.getMember()) {
                manager.getOnlinePlayer(uniqueId).getPlayerExecutor().connect(CloudNetDriver.getInstance().getCloudServiceProvider().getCloudService(serviceUUID).getName());
            }
        }, 1L, TimeUnit.SECONDS);
    }

}
