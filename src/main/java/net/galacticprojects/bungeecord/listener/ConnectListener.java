package net.galacticprojects.bungeecord.listener;

import me.lauriichan.laylib.localization.Key;
import net.galacticprojects.bungeecord.ProxyPlugin;
import net.galacticprojects.bungeecord.message.BanMessage;
import net.galacticprojects.bungeecord.message.CommandMessages;
import net.galacticprojects.common.database.SQLDatabase;
import net.galacticprojects.common.database.model.Ban;
import net.galacticprojects.common.database.model.Player;
import net.galacticprojects.common.util.TimeHelper;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import java.sql.SQLData;
import java.util.UUID;

public class ConnectListener implements Listener {

    private ProxyPlugin plugin;

    public ConnectListener(ProxyPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onConnect(PostLoginEvent event) {
        ProxiedPlayer player = event.getPlayer();
        UUID uniqueId = player.getUniqueId();
        SQLDatabase database = plugin.getCommonPlugin().getDatabaseRef().get();
        Player playerData = database.getPlayer(uniqueId).join();

        if(playerData == null) {
            database.createPlayer(uniqueId, player.getAddress().getAddress().getHostAddress().toString(), 1000, 1, "en-uk", 0L);
            return;
        }
        Ban ban = database.getBan(uniqueId).join();
        if(ban == null) {
            return;
        }
        BanMessage message = BanMessage.valueOf("COMMAND_BAN_ID_" + ban.getId());
        player.disconnect(new TextComponent(plugin.getCommonPlugin().getMessageManager().translate(CommandMessages.COMMAND_PLAYER_BANNED, playerData.getLanguage(), Key.of("time", TimeHelper.BAN_TIME_FORMATTER.format(ban.getTime())), Key.of("reason", message))));
    }

}
