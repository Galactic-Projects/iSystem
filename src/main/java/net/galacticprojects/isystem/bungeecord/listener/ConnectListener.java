package net.galacticprojects.isystem.bungeecord.listener;

import de.dytanic.cloudnet.ext.bridge.BridgePlayerManager;
import net.dv8tion.jda.api.events.DisconnectEvent;
import net.galacticprojects.isystem.bungeecord.iProxy;
import net.galacticprojects.isystem.database.MySQL;
import net.galacticprojects.isystem.database.model.Ban;
import net.galacticprojects.isystem.database.model.Player;
import net.galacticprojects.isystem.utils.JavaInstance;
import net.galacticprojects.isystem.utils.Languages;
import net.galacticprojects.isystem.utils.TabManager;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.*;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.time.OffsetDateTime;
import java.util.concurrent.TimeUnit;

public class ConnectListener implements Listener {

    public MySQL mySQL = JavaInstance.get(MySQL.class);
    public TabManager tabManager;

    @EventHandler
    public void onConnect(PostLoginEvent event) {
        ProxiedPlayer player = event.getPlayer();

        checkPlayer(player);
        checkBan(player);
        setTablist(player);
        updateTablist();
    }

    @EventHandler
    public void onDisconnect(PlayerDisconnectEvent event) {
        ProxiedPlayer player = event.getPlayer();

        removeTablist(player);
        updateTablist();
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
    public void onSwitchServer(ServerSwitchEvent event) {
        ProxiedPlayer player = event.getPlayer();
        checkBan(player);
        setTablist(player);
    }

    public void setTablist(ProxiedPlayer player) {
        ProxyServer.getInstance().getScheduler().schedule(iProxy.getInstance(), new Runnable() {
            @Override
            public void run() {
                tabManager = new TabManager(player);
                tabManager.setTablist();
            }
        }, 20, TimeUnit.MILLISECONDS);
    }

    public void removeTablist(ProxiedPlayer player) {
        tabManager = new TabManager(player);
        tabManager.removeTablist();
    }

    public void updateTablist() {
        ProxyServer.getInstance().getScheduler().schedule(iProxy.getInstance(), new Runnable() {
            @Override
            public void run() {
                for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
                    tabManager = new TabManager(player);
                    tabManager.updateTablist();
                }
            }
        }, 20, TimeUnit.MILLISECONDS);
    }

    public void checkPlayer(ProxiedPlayer player) {
        Player playerDB = mySQL.getPlayer(player.getUniqueId()).join();


        if (playerDB == null) {
            return;
        }

        mySQL.createPlayer(player.getUniqueId(), player.getName(), player.getAddress().getAddress().getHostAddress(), "", 0, Languages.ENGLISH, OffsetDateTime.now(), "ONLINE ON" + player.getServer().getInfo().getName(), OffsetDateTime.now(), false, false);


    }

    public void checkBan(ProxiedPlayer player) {

        Ban ban = mySQL.getBanned(player.getUniqueId()).join();

        if (ban == null) {
            return;
        }
        if (ban.isExpired()) {
            mySQL.deleteBan(player.getUniqueId());
            return;
        }
        switch (ban.getType().name()) {
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
