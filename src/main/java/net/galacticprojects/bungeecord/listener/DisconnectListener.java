package net.galacticprojects.bungeecord.listener;

import net.galacticprojects.bungeecord.party.Party;
import net.galacticprojects.bungeecord.party.PartyManager;
import net.galacticprojects.bungeecord.util.Countdown;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class DisconnectListener implements Listener {

    @EventHandler
    public void onDisconnect(PlayerDisconnectEvent event) {
        ProxiedPlayer player = event.getPlayer();

        PartyManager manager = new PartyManager(player);
        Party party = manager.getParty();
        if(party == null) {
            return;
        }
        Countdown.setCountdown(player.getUniqueId(), party, 60 * 5);
    }

}
