package net.galacticprojects.spigot.listener;

import net.galacticprojects.database.MySQL;
import net.galacticprojects.spigot.utils.SubtitleAPI;
import net.galacticprojects.utils.JavaInstance;
import net.labymod.serverapi.bukkit.event.LabyModPlayerJoinEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class LabyModListener implements Listener{

    MySQL mySQL = JavaInstance.get(MySQL.class);

    @EventHandler
    public void onLabyJoin(LabyModPlayerJoinEvent event) {
        Player player = event.getPlayer();
        SubtitleAPI.setSubtitle(player, player.getUniqueId(), "");
    }

}
