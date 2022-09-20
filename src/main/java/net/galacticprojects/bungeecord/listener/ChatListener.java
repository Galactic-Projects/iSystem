package net.galacticprojects.bungeecord.listener;

import me.lauriichan.wildcard.systemcore.util.JavaInstance;
import net.galacticprojects.common.databaseLegacy.MySQL;
import net.galacticprojects.common.database.model.Ban;
import net.galacticprojects.common.database.model.Player;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ChatListener implements Listener {

    public MySQL mySQL = JavaInstance.get(MySQL.class);
    private Player playerDB;
    private Ban ban;

    @EventHandler
    public void onChat(ChatEvent event) {
        ProxiedPlayer player = (ProxiedPlayer) event.getSender();
        playerDB = mySQL.getPlayer(player.getUniqueId()).join();

        if(playerDB == null) {
            return;
        }

        checkBanned(player);
    }

    public void checkBanned(ProxiedPlayer player) {

    }
}
