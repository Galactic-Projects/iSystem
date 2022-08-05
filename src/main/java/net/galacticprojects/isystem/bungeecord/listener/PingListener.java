package net.galacticprojects.isystem.bungeecord.listener;

import net.galacticprojects.isystem.bungeecord.command.MaintenanceCommand;
import net.galacticprojects.isystem.bungeecord.config.MainConfiguration;
import net.galacticprojects.isystem.database.MySQL;
import net.galacticprojects.isystem.database.model.Player;
import net.galacticprojects.isystem.utils.JavaInstance;
import net.galacticprojects.isystem.utils.Languages;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerHandshakeEvent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.HashMap;
import java.util.UUID;

public class PingListener implements Listener {

    private static HashMap<UUID, Languages> languages = new HashMap<UUID, Languages>();
    private MySQL mySQL = JavaInstance.get(MySQL.class);
    private Player playerDB;

    @EventHandler
    public void onServerPing(ProxyPingEvent event) {
        ServerPing ping = event.getResponse();
        ServerPing.Players players = ping.getPlayers();
        ServerPing.Protocol protocol = ping.getVersion();
        playerDB = mySQL.getPlayer(event.getConnection().getUniqueId()).join();

        if (JavaInstance.get(MainConfiguration.class).isMaintenanceEnabled()) {
            switch (playerDB.getLanguages()) {
                case ENGLISH: {

                    break;
                }
                case GERMAN: {

                    break;
                }
                case FRENCH: {

                    break;
                }
                case SPANISH: {

                    break;
                }
            }
        }

    }

    @EventHandler
    public void onHandshake(PlayerHandshakeEvent event) {
        ProxiedPlayer player = (ProxiedPlayer) event.getConnection();


    }
}
