package net.galacticprojects.bungeecord.listener;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.driver.service.ServiceInfoSnapshot;
import de.dytanic.cloudnet.ext.bridge.player.IPlayerManager;
import net.galacticprojects.bungeecord.party.Party;
import net.galacticprojects.bungeecord.party.PartyManager;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.UUID;

public class ServerSwitchListener implements Listener {

    @EventHandler
    public void onServerSwitch(ServerSwitchEvent event) {
        ProxiedPlayer player = event.getPlayer();

        Party party = new PartyManager(player).getParty();

        if(party == null) {
            return;
        }
        if(party.getLeader() != player.getUniqueId()) {
            return;
        }
        IPlayerManager manager = CloudNetDriver.getInstance().getServicesRegistry().getFirstService(IPlayerManager.class);
        UUID serviceUUID = manager.getOnlinePlayer(player.getUniqueId()).getConnectedService().getUniqueId();
        for(UUID uniqueId : party.getMember()) {
            manager.getOnlinePlayer(uniqueId).getPlayerExecutor().connect(CloudNetDriver.getInstance().getCloudServiceProvider().getCloudService(serviceUUID).getName());
        }
    }

}
