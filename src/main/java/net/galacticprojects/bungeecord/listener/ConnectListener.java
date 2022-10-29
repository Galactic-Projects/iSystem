package net.galacticprojects.bungeecord.listener;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.ext.bridge.player.IPlayerManager;
import me.lauriichan.laylib.localization.Key;
import net.galacticprojects.bungeecord.ProxyPlugin;
import net.galacticprojects.bungeecord.config.PluginConfiguration;
import net.galacticprojects.bungeecord.message.BanMessage;
import net.galacticprojects.bungeecord.message.CommandMessages;
import net.galacticprojects.bungeecord.message.SystemMessage;
import net.galacticprojects.bungeecord.party.Party;
import net.galacticprojects.bungeecord.party.PartyManager;
import net.galacticprojects.bungeecord.util.Countdown;
import net.galacticprojects.bungeecord.util.TablistManager;
import net.galacticprojects.bungeecord.util.TimeHelper;
import net.galacticprojects.common.CommonPlugin;
import net.galacticprojects.common.database.SQLDatabase;
import net.galacticprojects.common.database.model.Ban;
import net.galacticprojects.common.database.model.FriendRequest;
import net.galacticprojects.common.database.model.FriendSettings;
import net.galacticprojects.common.database.model.Player;
import net.galacticprojects.common.util.ComponentParser;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import org.bukkit.block.data.type.Bed;

import java.sql.SQLData;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ConnectListener implements Listener {

    private ProxyPlugin plugin;

    public ConnectListener(ProxyPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onConnect(PostLoginEvent event) {
        ProxiedPlayer player = event.getPlayer();
        UUID uniqueId = player.getUniqueId();
        SQLDatabase database = plugin.getCommonPlugin().getDatabaseRef().get();
        PluginConfiguration configuration = plugin.getPluginConfiguration();
        CommonPlugin commonPlugin = plugin.getCommonPlugin();
        String language = "en-uk";
        Player playerData = database.getPlayer(uniqueId).join();

        for(int i = 0; i != 99; i++){
            player.sendMessage(ComponentParser.parse(""));
        }

        if(playerData == null) {
            playerData = database.createPlayer(uniqueId, player.getAddress().getAddress().getHostAddress(), 1000, 1, "en-uk", 0L).join();
        }

        if (playerData.getLanguage() != null) {
            language = playerData.getLanguage();
        }

        if(!Countdown.online.contains(player.getUniqueId())) {
            Countdown.online.add(player.getUniqueId());
        }

        new TablistManager(plugin, player);

        if (configuration.isMaintenance()) {
            if (!(player.hasPermission("system.maintenance.bypass") || player.hasPermission("system.maintenance.*"))) {
                Key reason = Key.of("reason", commonPlugin.getMessageManager().translate(SystemMessage.SYSTEM_MAINTENANCE_KICK_REASON, language, Key.of("reason", configuration.getMaintenanceReason())));
                Key enddate = Key.of("enddate", TimeHelper.format(language).format(TimeHelper.fromString(configuration.getDays())));
                player.disconnect(ComponentParser.parse(commonPlugin.getMessageManager().translate(SystemMessage.SYSTEM_MAINTENACE_KICK_CURRENTLY, language, enddate, reason)));
                //CloudNetDriver.getInstance(). //(BridgePlayerManager.getInstance().getOnlinePlayer(uniqueId), commonPlugin.getMessageManager().translate(SystemMessage.SYSTEM_MAINTENACE_KICK_CURRENTLY, finalLanguage, enddate, reason));
            }
        }

        FriendSettings friendSettings = database.getFriendSettings(uniqueId).join();
        if(friendSettings == null) {
            friendSettings = database.createFriendSettings(uniqueId).join();
            return;
        }

        Ban ban = database.getBan(uniqueId).join();
        if (ban != null) {
            if (ban.isExpired()) {
                database.deleteBan(ban.getPlayer()).join();
                return;
            }
            BanMessage message = BanMessage.valueOf("COMMAND_BAN_ID_" + ban.getId());
            player.disconnect(ComponentParser.parse(plugin.getCommonPlugin().getMessageManager().translate(CommandMessages.COMMAND_PLAYER_BANNED, playerData.getLanguage(), Key.of("time", TimeHelper.BAN_TIME_FORMATTER.format(ban.getTime())), Key.of("reason", message))));
        }

        PartyManager manager = new PartyManager(player);
        Party party = manager.getParty();

        Countdown.online.add(player.getUniqueId());

        if (party == null) {
            return;
        }

        Countdown.checkCountdown(player.getUniqueId());
        ArrayList<UUID> members = party.getMember();
        members.add(party.getLeader());
        for (UUID member : members) {
            ProxiedPlayer players = ProxyServer.getInstance().getPlayer(member);
            if (players != null) {
                if (player.getUniqueId() == party.getLeader()) {
                    commonPlugin.getDatabaseRef().asOptional().ifPresent(sqlDatabase -> {
                        sqlDatabase.getPlayer(players.getUniqueId()).thenAccept(partyMembers -> {
                            players.sendMessage(ComponentParser.parse(commonPlugin.getMessageManager().translate(SystemMessage.SYSTEM_PARTY_MEMBER, partyMembers.getLanguage(), Key.of("member", SystemMessage.SYSTEM_PARTY_LEADER), Key.of("action", SystemMessage.SYSTEM_PARTY_REJOINED))));
                        });
                    });
                }
            }
        }
    }
}
