package net.galacticprojects.bungeecord.util;

import net.galacticprojects.bungeecord.ProxyPlugin;
import net.galacticprojects.bungeecord.party.Party;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.bukkit.block.data.type.Bed;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class Countdown {

    public static HashMap<UUID, Double> countdown;
    public static ArrayList<UUID> online = new ArrayList<>();

    private static ProxyPlugin PLUGIN;

    private static int taskId;

    public static void setupCountdown(ProxyPlugin plugin) {
        countdown = new HashMap<>();
        PLUGIN = plugin;
    }

    public static void setCountdown(UUID player, Party party, int seconds) {
        double delay = System.currentTimeMillis() + (seconds * 1000);
        countdown.put(player, delay);
        taskId = ProxyServer.getInstance().getScheduler().schedule(PLUGIN, () -> {
            if(countdown.get(player) != 0) {
                if(online.contains(player)) {
                    countdown.remove(player, countdown.get(player));
                    online.remove(player);
                    ProxyServer.getInstance().getScheduler().cancel(taskId);
                    return;
                }
                return;
            }
            if(!checkCountdown(player)) {

                if(party.getLeader() == player) {
                    party.deleteParty();
                    ProxyServer.getInstance().getScheduler().cancel(taskId);
                    return;
                }
                party.removeMember(player);
                ProxiedPlayer leader = ProxyServer.getInstance().getPlayer(party.getLeader());
                leader.sendMessage();
                for(UUID members : party.getMember()) {
                    ProxiedPlayer member = ProxyServer.getInstance().getPlayer(members);
                    member.sendMessage();
                }
                ProxyServer.getInstance().getScheduler().cancel(taskId);
            }
        }, 1L, 1L, TimeUnit.SECONDS).getId();
    }

    public static boolean checkCountdown(UUID player) {
        if (!countdown.containsKey(player)
                || countdown.get(player) <= System.currentTimeMillis()) {
            return false;
        }
        return true;
    }

}
