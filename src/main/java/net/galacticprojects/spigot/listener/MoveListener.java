package net.galacticprojects.spigot.listener;

import net.galacticprojects.common.databaseLegacy.MySQL;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class MoveListener implements Listener {

    public MySQL mySQL = JavaInstance.get(MySQL.class);

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        net.galacticprojects.common.database.model.Player playerDB = mySQL.getPlayer(player.getUniqueId()).join();

        if (playerDB == null) {
            return;
        }

        if(playerDB.isVerified()) {
            return;
        }

        event.setCancelled(true);
        player.teleport(player);
    }
}
