package net.galacticprojects.bungeecord.util;

import net.galacticprojects.bungeecord.ProxyPlugin;
import net.galacticprojects.common.database.SQLDatabase;
import net.galacticprojects.common.database.model.Player;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class OnlineTime {

    Map<UUID, Long> time = new HashMap<>();

    ProxyPlugin plugin;



    public OnlineTime(ProxyPlugin plugin) {
        this.plugin = plugin;
        onlineCount();
    }

    ArrayList<UUID> uniqueIds = new ArrayList<>();

    public void onlineCount() {
        ProxyServer.getInstance().getScheduler().schedule(plugin, () -> {
            for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
                if (player != null) {
                    if (!(time.containsKey(player.getUniqueId()))) {
                        plugin.getCommonPlugin().getDatabaseRef().asOptional().ifPresent(sqlDatabase -> {
                            sqlDatabase.getPlayer(player.getUniqueId()).thenAccept(playerData -> {
                                long onlineTime = Long.parseLong(playerData.getOnlineTime());
                                onlineTime = onlineTime + 60;
                                time.put(player.getUniqueId(), onlineTime);
                                playerData.setOnlineTime(String.valueOf(onlineTime));
                                sqlDatabase.updatePlayer(playerData);
                                time.remove(player.getUniqueId());
                            });
                        });
                    }
                }
            }
        }, 1L, 1L, TimeUnit.MINUTES);
    }
}
