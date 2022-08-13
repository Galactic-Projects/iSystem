package net.galacticprojects.isystem.bungeecord.listener;

import net.galacticprojects.isystem.database.MySQL;
import net.galacticprojects.isystem.database.model.Ban;
import net.galacticprojects.isystem.database.model.Player;
import net.galacticprojects.isystem.utils.JavaInstance;
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
        ban = mySQL.getBanned(player.getUniqueId()).join();
        playerDB = mySQL.getPlayer(player.getUniqueId()).join();

        if (ban == null) {
            return;
        }
        if (ban.isExpired()) {
            mySQL.deleteBan(player.getUniqueId());
            return;
        }
        switch (ban.getType().name()) {
            case "nmute" -> {

                switch (Boolean.toString(ban.isPermanent())) {
                    case "true" -> {

                    }
                    case "false" -> {

                    }
                }
            }
            case "smute" -> {

                switch (Boolean.toString(ban.isPermanent())) {
                    case "true" -> {

                    }
                    case "false" -> {

                    }
                }
            }
        }
    }
}
