package net.galacticprojects.isystem.bungeecord.listener;

import net.dv8tion.jda.api.events.DisconnectEvent;
import net.galacticprojects.isystem.database.MySQL;
import net.galacticprojects.isystem.database.model.Ban;
import net.galacticprojects.isystem.utils.JavaInstance;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.*;
import net.md_5.bungee.api.plugin.Listener;
import org.bukkit.event.EventHandler;

public class ConnectListener implements Listener {

    MySQL mySQL = JavaInstance.get(MySQL.class);

    @EventHandler
    public void onConnect(PostLoginEvent event) {
        ProxiedPlayer player = event.getPlayer();

        checkBan(player);

    }

    @EventHandler
    public void onDiscord(PlayerDisconnectEvent event) {
        ProxiedPlayer player = event.getPlayer();
    }

    @EventHandler
    public void onServerConnect(ServerConnectEvent event) {
        ProxiedPlayer player = event.getPlayer();
    }

    @EventHandler
    public void onServerDisconnect(ServerDisconnectEvent event) {
        ProxiedPlayer player = event.getPlayer();
    }

    @EventHandler
    public void onHandshake(PlayerHandshakeEvent event) {
        ProxiedPlayer player = (ProxiedPlayer) event.getConnection();
    }

    public void checkBan(ProxiedPlayer player) {

        Ban ban = mySQL.getBanned(player.getUniqueId()).join();

        if(ban == null) {
            return;
        }
        if(ban.isExpired()) {
            mySQL.deleteBan(player.getUniqueId());
            return;
        }
        switch(ban.getType().name()) {
            case "nban": {

                switch (Boolean.toString(ban.isPermanent())) {
                    case "true": {

                        break;
                    }
                    case "false": {

                    }
                }

                break;
            }
            case "sban": {

                switch (Boolean.toString(ban.isPermanent())) {
                    case "true": {

                        break;
                    }
                    case "false": {

                    }
                }
                break;
            }
            case "nmute": {

                switch (Boolean.toString(ban.isPermanent())) {
                    case "true": {

                        break;
                    }
                    case "false": {

                    }
                }
                break;
            }
            case "smute": {

                switch (Boolean.toString(ban.isPermanent())) {
                    case "true": {

                        break;
                    }
                    case "false": {

                    }
                }
            }
        }
    }


}
