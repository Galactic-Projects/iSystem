package net.galacticprojects.bungeecord.listener;

import eu.cloudnetservice.driver.CloudNetDriver;
import eu.cloudnetservice.modules.bridge.player.PlayerManager;
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

        for(int i = 0; i != 14; i++){
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
            PlayerManager manager = CloudNetDriver.instance().serviceRegistry().firstProvider(PlayerManager.class);
            UUID serviceUUID = manager.onlinePlayer(player.getUniqueId()).connectedService().uniqueId();
            for(UUID uniqueId : party.getMember()) {
                manager.onlinePlayer(uniqueId).playerExecutor().connect(CloudNetDriver.instance().cloudServiceProvider().service(serviceUUID).serviceId().taskName());
            }
        }, 1L, TimeUnit.SECONDS);
    }

}
