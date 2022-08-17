package net.galacticprojects.isystem.bungeecord.listener;

import net.galacticprojects.isystem.bungeecord.command.MaintenanceCommand;
import net.galacticprojects.isystem.bungeecord.config.MainConfiguration;
import net.galacticprojects.isystem.bungeecord.config.languages.EnglishConfiguration;
import net.galacticprojects.isystem.database.MySQL;
import net.galacticprojects.isystem.database.model.Player;
import net.galacticprojects.isystem.utils.JavaInstance;
import net.galacticprojects.isystem.utils.Languages;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerHandshakeEvent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.UUID;

public class PingListener implements Listener {

    private static HashMap<UUID, Languages> languages = new HashMap<UUID, Languages>();
    private MySQL mySQL = JavaInstance.get(MySQL.class);
    private Player playerDB;
    private MainConfiguration mainConfiguration;
    private EnglishConfiguration englishConfiguration;


    @EventHandler
    public void onServerPing(ProxyPingEvent event) {
        ServerPing ping = event.getResponse();
        ServerPing.Players players = ping.getPlayers();
        ServerPing.Protocol protocol = ping.getVersion();

        playerDB = mySQL.getPlayerFromIp(event.getConnection().getAddress().getAddress().getHostAddress()).join();
        mainConfiguration = JavaInstance.get(MainConfiguration.class);
        englishConfiguration = JavaInstance.get(EnglishConfiguration.class);

        if (JavaInstance.get(MainConfiguration.class).isMaintenanceEnabled()) {
            if (playerDB == null) {
                protocol.setProtocol(2);
                ping.setDescriptionComponent(new TextComponent(englishConfiguration.getMotdsMaintenanceLineOne() + "\n" + englishConfiguration.getMotdsMaintenanceLineTwo()));
                protocol.setName(englishConfiguration.getMaintenanceVersion());
                players.setMax(mainConfiguration.getMaxPlayers());
                ping.setVersion(protocol);
                ping.setPlayers(players);
                return;
            }
            switch (playerDB.getLanguages()) {
                case GERMAN -> {

                }
                case FRENCH -> {

                }
                case SPANISH -> {

                }
                case ENGLISH -> {
                    protocol.setProtocol(2);
                    ping.setDescriptionComponent(new TextComponent(englishConfiguration.getMotdsMaintenanceLineOne() + "\n" + englishConfiguration.getMotdsMaintenanceLineTwo()));
                    protocol.setName(englishConfiguration.getMaintenanceVersion());
                    players.setMax(mainConfiguration.getMaxPlayers());
                    ping.setVersion(protocol);
                    ping.setPlayers(players);
                }
            }
        } else {
            if (playerDB == null) {
                ping.setDescriptionComponent(new TextComponent(englishConfiguration.getMotdsNormalLineOne() + "\n" + englishConfiguration.getMotdsNormalLineTwo()));
                players.setMax(mainConfiguration.getMaxPlayers());
                ping.setPlayers(players);
                return;
            }
            switch (playerDB.getLanguages()) {
                case GERMAN -> {

                }
                case FRENCH -> {

                }
                case SPANISH -> {

                }
                case ENGLISH -> {
                    ping.setDescriptionComponent(new TextComponent(englishConfiguration.getMotdsNormalLineOne() + "\n" + englishConfiguration.getMotdsNormalLineTwo()));
                    players.setMax(mainConfiguration.getMaxPlayers());
                    ping.setPlayers(players);
                }
            }
        }
    }
}
