package net.galacticprojects.bungeecord.listener;

import me.lauriichan.laylib.localization.Key;
import me.lauriichan.laylib.localization.MessageProvider;
import net.galacticprojects.bungeecord.ProxyPlugin;
import net.galacticprojects.bungeecord.message.SystemMessage;
import net.galacticprojects.bungeecord.party.Party;
import net.galacticprojects.bungeecord.party.PartyManager;
import net.galacticprojects.bungeecord.util.Countdown;
import net.galacticprojects.common.CommonPlugin;
import net.galacticprojects.common.util.ComponentParser;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.ArrayList;
import java.util.UUID;

public class DisconnectListener implements Listener {

    ProxyPlugin plugin;

    public DisconnectListener(ProxyPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDisconnect(PlayerDisconnectEvent event) {
        ProxiedPlayer player = event.getPlayer();

        PartyManager manager = new PartyManager(player);
        Party party = manager.getParty();

        CommonPlugin commonPlugin = plugin.getCommonPlugin();
        Countdown.online.remove(player.getUniqueId());

        if (party == null) {
            return;
        }

        Countdown.setCountdown(player.getUniqueId(), party, 60 * 5);
        ArrayList<UUID> members = party.getMember();
        members.add(party.getLeader());
        for (UUID member : members) {
            ProxiedPlayer players = ProxyServer.getInstance().getPlayer(member);
            if (players != null) {
                if (player.getUniqueId() == party.getLeader()) {
                    commonPlugin.getDatabaseRef().asOptional().ifPresent(sqlDatabase -> {
                        sqlDatabase.getPlayer(players.getUniqueId()).thenAccept(playerData -> {
                            players.sendMessage(ComponentParser.parse(commonPlugin.getMessageManager().translate(SystemMessage.SYSTEM_PARTY_MEMBER_LEAVE, playerData.getLanguage(), Key.of("member", SystemMessage.SYSTEM_PARTY_LEADER), Key.of("action", SystemMessage.SYSTEM_PARTY_ACTION_DELETE)))); //SystemMessage.SYSTEM_PARTY_MEMBER_LEAVE);
                        });
                    });
                } else {
                    commonPlugin.getDatabaseRef().asOptional().ifPresent(sqlDatabase -> {
                        sqlDatabase.getPlayer(players.getUniqueId()).thenAccept(playerData -> {
                            players.sendMessage(ComponentParser.parse(commonPlugin.getMessageManager().translate(SystemMessage.SYSTEM_PARTY_MEMBER_LEAVE, playerData.getLanguage(), Key.of("member", SystemMessage.SYSTEM_PARTY_MEMBER), Key.of("action", SystemMessage.SYSTEM_PARTY_ACTION_KICK)))); //SystemMessage.SYSTEM_PARTY_MEMBER_LEAVE);
                        });
                    });
                }
            }
        }
    }
}
