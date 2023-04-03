package net.galacticprojects.bungeecord.listener;

import dev.derklaro.aerogel.Injector;
import eu.cloudnetservice.driver.inject.InjectionLayer;
import eu.cloudnetservice.driver.provider.CloudServiceProvider;
import eu.cloudnetservice.driver.registry.ServiceRegistry;
import eu.cloudnetservice.modules.bridge.player.PlayerManager;
import net.galacticprojects.bungeecord.ProxyPlugin;
import net.galacticprojects.bungeecord.party.Party;
import net.galacticprojects.bungeecord.party.PartyManager;
import net.galacticprojects.common.database.SQLDatabase;
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

    private final ProxyPlugin plugin;
    private final Injector injector;

    public ServerSwitchListener(ProxyPlugin plugin) {
        this.injector = InjectionLayer.ext().injector();
        this.plugin = plugin;
    }
    @EventHandler
    public void onServerSwitch(ServerSwitchEvent event) {
        ProxiedPlayer player = event.getPlayer();

        SQLDatabase database = plugin.getCommonPlugin().getDatabaseRef().get();

        if(!(database.getPlayer(player.getUniqueId()).join().getVerified())) {
            return;
        }

        for (int i = 0; i != 14; i++) {
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
            for(UUID uniqueId : party.getMember()) {
                ProxyServer.getInstance().getPlayer(uniqueId).connect(ProxyServer.getInstance().getServerInfo(player.getServer().getInfo().getName()));
            }
        }, 1L, TimeUnit.SECONDS);
    }

}
