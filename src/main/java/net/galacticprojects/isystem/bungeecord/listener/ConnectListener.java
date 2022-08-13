package net.galacticprojects.isystem.bungeecord.listener;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.driver.permission.IPermissible;
import de.dytanic.cloudnet.driver.permission.IPermissionManagement;
import de.dytanic.cloudnet.driver.permission.IPermissionUser;
import de.dytanic.cloudnet.driver.permission.Permission;
import de.dytanic.cloudnet.ext.bridge.BridgePlayerManager;
import de.dytanic.cloudnet.ext.bridge.BridgePlayerProvider;
import de.dytanic.cloudnet.ext.bridge.player.CloudPlayer;
import de.dytanic.cloudnet.ext.bridge.player.ICloudPlayer;
import de.dytanic.cloudnet.ext.bridge.player.IPlayerManager;
import net.galacticprojects.isystem.bungeecord.config.MainConfiguration;
import net.galacticprojects.isystem.bungeecord.config.languages.EnglishConfiguration;
import net.galacticprojects.isystem.bungeecord.iProxy;
import net.galacticprojects.isystem.database.MySQL;
import net.galacticprojects.isystem.database.model.Ban;
import net.galacticprojects.isystem.database.model.Player;
import net.galacticprojects.isystem.utils.JavaInstance;
import net.galacticprojects.isystem.utils.Languages;
import net.galacticprojects.isystem.utils.TabManager;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.*;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.protocol.packet.Login;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.OffsetDateTime;
import java.util.concurrent.TimeUnit;

public class ConnectListener implements Listener {

    public MySQL mySQL = JavaInstance.get(MySQL.class);
    public TabManager tabManager;
    public MainConfiguration mainConfiguration = JavaInstance.get(MainConfiguration.class);
    public EnglishConfiguration englishConfiguration = JavaInstance.get(EnglishConfiguration.class);


    @EventHandler
    public void onLogin(LoginEvent event) {
        if (mainConfiguration.isMaintenanceEnabled()) {
            IPermissionManagement permissionManagement = CloudNetDriver.getInstance().getPermissionManagement();
            IPermissionUser iPermissionUser = permissionManagement.getUser(mySQL.getPlayerFromIp(event.getConnection().getAddress().getAddress().getHostAddress()).join().getUUID());
            if(iPermissionUser != null) {
                if (!(permissionManagement.hasPermission(iPermissionUser, "*"))) {
                    switch (mySQL.getPlayerFromIp(event.getConnection().getAddress().getAddress().getHostAddress()).join().getLanguages()) {
                        case GERMAN:
                        case SPANISH: {

                            break;
                        }
                        case ENGLISH: {
                            event.setCancelReason(new TextComponent(englishConfiguration.getKickMaintenanceCurrently().replaceAll("%reason%", englishConfiguration.getMaintenanceReason()).replaceAll("%enddate%", englishConfiguration.getMaintenanceEndDate())));
                            break;
                        }
                        case FRENCH: {

                        }
                    }
                }
            }
        }
    }

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
        updatePlayer(player, englishConfiguration.getFormatOffline());
    }

    @EventHandler
    public void onServerConnect(ServerConnectEvent event) {
        ProxiedPlayer player = event.getPlayer();
        ProxyServer.getInstance().getScheduler().schedule(iProxy.getInstance(), new Runnable() {
            @Override
            public void run() {
                IPlayerManager playerManager = CloudNetDriver.getInstance().getServicesRegistry().getFirstService(IPlayerManager.class);
                ICloudPlayer cloudPlayer = playerManager.getOnlinePlayer(player.getUniqueId());
                if(cloudPlayer != null) {
                    updatePlayer(player, englishConfiguration.getFormatOnline().replaceAll("%server%", cloudPlayer.getConnectedService().getServiceId().getName().toUpperCase()));
                }
            }
        }, 1, TimeUnit.SECONDS);
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
        ProxyServer.getInstance().getScheduler().schedule(iProxy.getInstance(), new Runnable() {
            @Override
            public void run() {
                IPlayerManager playerManager = CloudNetDriver.getInstance().getServicesRegistry().getFirstService(IPlayerManager.class);
                ICloudPlayer cloudPlayer = playerManager.getOnlinePlayer(player.getUniqueId());
                if(cloudPlayer != null) {
                    updatePlayer(player, englishConfiguration.getFormatOnline().replaceAll("%server%", cloudPlayer.getConnectedService().getServiceId().getName().toUpperCase()));
                }
            }
        }, 1, TimeUnit.SECONDS);
    }

    public void setTablist(ProxiedPlayer player) {
        ProxyServer.getInstance().getScheduler().schedule(iProxy.getInstance(), new Runnable() {
            @Override
            public void run() {
                IPlayerManager playerManager = CloudNetDriver.getInstance().getServicesRegistry().getFirstService(IPlayerManager.class);
                ICloudPlayer cloudPlayer = playerManager.getOnlinePlayer(player.getUniqueId());
                if(cloudPlayer != null) {
                    tabManager = new TabManager(player, cloudPlayer.getConnectedService().getServiceId().getName(), cloudPlayer.getConnectedService().getServiceId().getTaskName());
                    tabManager.setTablist();
                }
            }
        }, 1, TimeUnit.SECONDS);
    }

    public void removeTablist(ProxiedPlayer player) {
        tabManager = new TabManager(player, "", "");
        tabManager.removeTablist();
    }

    public void updateTablist() {
        ProxyServer.getInstance().getScheduler().schedule(iProxy.getInstance(), new Runnable() {
            @Override
            public void run() {
                for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
                    IPlayerManager playerManager = CloudNetDriver.getInstance().getServicesRegistry().getFirstService(IPlayerManager.class);
                    ICloudPlayer cloudPlayer = playerManager.getOnlinePlayer(player.getUniqueId());
                    if(cloudPlayer != null) {
                        tabManager = new TabManager(player, cloudPlayer.getConnectedService().getServiceId().getTaskName(), cloudPlayer.getConnectedService().getServiceId().getName());
                        tabManager.updateTablist();
                    }
                }
            }
        }, 1, TimeUnit.SECONDS);
    }

    public void checkPlayer(ProxiedPlayer player) {
        ProxyServer.getInstance().getScheduler().schedule(iProxy.getInstance(), new Runnable() {
            @Override
            public void run() {
                IPlayerManager playerManager = CloudNetDriver.getInstance().getServicesRegistry().getFirstService(IPlayerManager.class);
                ICloudPlayer cloudPlayer = playerManager.getOnlinePlayer(player.getUniqueId());
                String ip = player.getAddress().getAddress().getHostAddress();

                if(mySQL.getPlayer(player.getUniqueId()) == null) {
                    mySQL.createPlayer(player.getUniqueId(), player.getName(), ip, getCountry(ip), 0, Languages.ENGLISH, OffsetDateTime.now(), englishConfiguration.getFormatOnline().replaceAll("%server%", cloudPlayer.getConnectedService().getServiceId().getName().toUpperCase()), OffsetDateTime.now(), false, false, false);
                }

                Player playerDB = mySQL.getPlayer(player.getUniqueId()).join();

                if (playerDB == null) {
                    return;
                }

                if (playerDB.getUUID() == player.getUniqueId()) {
                    if(playerDB.getName() != player.getName() || playerDB.getIP() != ip || playerDB.getCountry() != getCountry(ip)) {
                        mySQL.updatePlayer(player.getUniqueId(), player.getName(), ip, getCountry(ip), Languages.ENGLISH, mySQL.getPlayer(player.getUniqueId()).join().getFirstJoin(), englishConfiguration.getFormatOnline().replaceAll("%server%", cloudPlayer.getConnectedService().getServiceId().getName().toUpperCase()), OffsetDateTime.now(), false, false, false);
                    }
                }
            }
        }, 1, TimeUnit.SECONDS);
    }

    public void updatePlayer(ProxiedPlayer player, String service) {
        ProxyServer.getInstance().getScheduler().schedule(iProxy.getInstance(), new Runnable() {
            @Override
            public void run() {
                mySQL.updateServer(player.getUniqueId(), service);
            }
        }, 1, TimeUnit.SECONDS);
    }

    public void checkBan(ProxiedPlayer player) {
        ProxyServer.getInstance().getScheduler().schedule(iProxy.getInstance(), new Runnable() {
            @Override
            public void run() {
                Ban ban = mySQL.getBanned(player.getUniqueId()).join();

                if (ban == null) {
                    return;
                }
                if (ban.isExpired()) {
                    mySQL.deleteBan(player.getUniqueId());
                    return;
                }
                switch (ban.getType().name()) {
                    case "nban" -> {

                        switch (Boolean.toString(ban.isPermanent())) {
                            case "true" -> {

                                break;
                            }
                            case "false" -> {

                            }
                        }

                        break;
                    }
                    case "sban" -> {

                        switch (Boolean.toString(ban.isPermanent())) {
                            case "true" -> {

                                break;
                            }
                            case "false" -> {

                            }
                        }
                    }
                }
            }
        }, 20, TimeUnit.MILLISECONDS);
    }

    public String getCountry(String IP) {
        try {
            String apiURL = String.format("http://ip-api.com/json/%s", IP);
            URL url = new URL(apiURL);
            BufferedReader stream = new BufferedReader(new InputStreamReader(url.openStream()));
            String websiteResponse = stream.readLine();
            stream.close();
            JsonObject resp = new Gson().fromJson(websiteResponse, JsonObject.class);
            if (resp != null && resp.has("country")) return resp.get("country").getAsString().toUpperCase();
            return null;
        } catch(MalformedURLException e) {
            e.printStackTrace();
            return "Illegal URL; Internal error";
        } catch(IOException e) {
            e.printStackTrace();
            return "unknown";
        }
    }
}
