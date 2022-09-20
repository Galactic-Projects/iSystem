package net.galacticprojects.bungeecord.listener;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.driver.permission.IPermissionManagement;
import de.dytanic.cloudnet.driver.permission.IPermissionUser;
import de.dytanic.cloudnet.ext.bridge.player.ICloudPlayer;
import de.dytanic.cloudnet.ext.bridge.player.IPlayerManager;
import me.lauriichan.wildcard.systemcore.inject.Inject;
import me.lauriichan.wildcard.systemcore.util.JavaInstance;
import net.galacticprojects.bungeecord.Messages;
import net.galacticprojects.bungeecord.iProxy;
import net.galacticprojects.common.database.model.Ban;
import net.galacticprojects.common.database.model.Player;
import net.galacticprojects.bungeecord.config.MainConfiguration;
import net.galacticprojects.bungeecord.config.languages.EnglishConfiguration;
import net.galacticprojects.common.databaseLegacy.MySQL;
import net.galacticprojects.bungeecord.util.TabManager;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.*;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ConnectListener implements Listener {

    @Inject
    public MySQL mySQL;
    public TabManager tabManager;
    public MainConfiguration mainConfiguration = JavaInstance.get(MainConfiguration.class);
    public EnglishConfiguration englishConfiguration = JavaInstance.get(EnglishConfiguration.class);


    @EventHandler
    public void onLogin(LoginEvent event) {
        if (mainConfiguration.isMaintenanceEnabled()) {
            String ip = event.getConnection().getAddress().getAddress().getHostAddress();
            IPermissionManagement permissionManagement = CloudNetDriver.getInstance().getPermissionManagement();


            if (mySQL.getPlayer(mySQL.getPlayerFromIp(ip).join().getUUID()).join() == null) {
                event.setCancelReason(new TextComponent(englishConfiguration.getKickMaintenanceCurrently().replaceAll("%reason%", englishConfiguration.getMaintenanceReason()).replaceAll("%enddate%", englishConfiguration.getMaintenanceEndDate())));
                return;
            }
            IPermissionUser iPermissionUser = permissionManagement.getUser(mySQL.getPlayerFromIp(ip).join().getUUID());
            if (iPermissionUser != null) {
                if (!(permissionManagement.hasPermission(iPermissionUser, "*"))) {
                    switch (mySQL.getPlayer(mySQL.getPlayerFromIp(ip).join().getUUID()).join().getLanguages()) {
                        case ENGLISH -> {
                            event.setCancelReason(new TextComponent(englishConfiguration.getKickMaintenanceCurrently().replaceAll("%reason%", englishConfiguration.getMaintenanceReason()).replaceAll("%enddate%", englishConfiguration.getMaintenanceEndDate())));

                        }
                        case GERMAN, SPANISH, FRENCH -> {

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
                if (cloudPlayer != null) {
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
                if (cloudPlayer != null) {
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
                if (cloudPlayer != null) {
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
                    if (cloudPlayer != null) {
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


                mySQL.createPlayer(player.getUniqueId(), player.getName(), ip, getCountry(ip), 0, Languages.ENGLISH, OffsetDateTime.now(), englishConfiguration.getFormatOnline().replaceAll("%server%", cloudPlayer.getConnectedService().getServiceId().getName().toUpperCase()), OffsetDateTime.now(), 0, false, false, false, false);

                Player playerDB = mySQL.getPlayer(player.getUniqueId()).join();

                if (playerDB == null) {
                    return;
                }

                if (playerDB.getUUID() == player.getUniqueId()) {
                    if (playerDB.getName() != player.getName() || playerDB.getIP() != ip || playerDB.getCountry() != getCountry(ip) || !Objects.equals(playerDB.getLatestJoin(), OffsetDateTime.now())) {
                        mySQL.updatePlayer(player.getUniqueId(), player.getName(), ip, getCountry(ip), Languages.ENGLISH, mySQL.getPlayer(player.getUniqueId()).join().getFirstJoin(), englishConfiguration.getFormatOnline().replaceAll("%server%", cloudPlayer.getConnectedService().getServiceId().getName().toUpperCase()), OffsetDateTime.now(), playerDB.getCoins(), playerDB.isReport(), playerDB.isReport(), playerDB.isShowtime(), playerDB.isVerified());
                    }
                }

                if(!(playerDB.isVerified())) {


                    return;
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
        UUID uniqueId = player.getUniqueId();
        Ban ban = mySQL.getBan(uniqueId).join();
        if(ban == null) {
            return;
        }
        Messages.GENERAL_BAN_FORMAT.kick(player, Messages.buildBanPlaceholders(ban, false));
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
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return "Illegal URL; Internal error";
        } catch (IOException e) {
            e.printStackTrace();
            return "Unknown";
        }
    }
}
