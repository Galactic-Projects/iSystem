package net.galacticprojects.bungeecord.listener;

import dev.derklaro.aerogel.Inject;
import dev.derklaro.aerogel.Injector;
import eu.cloudnetservice.driver.inject.InjectionLayer;
import me.lauriichan.laylib.localization.Key;
import net.galacticprojects.bungeecord.ProxyPlugin;
import net.galacticprojects.bungeecord.channel.LobbyMessage;
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
import net.galacticprojects.common.database.model.*;
import net.galacticprojects.common.util.CodeGenerator;
import net.galacticprojects.common.util.ComponentParser;
import net.galacticprojects.common.util.Verify;
import net.galacticprojects.common.util.color.ColorParser;
import net.galacticprojects.common.util.color.ComponentColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import net.md_5.bungee.event.EventHandler;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ConnectListener implements Listener {

    private ProxyPlugin plugin;
    private static TablistManager tablist;
    private ScheduledTask verification = null;


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
        Verify verify = plugin.getVerify();

        Ban ban = database.getBan(uniqueId).join();
        if (ban != null) {
            if (!ban.isExpired()) {
                BanMessage message = BanMessage.valueOf("COMMAND_BAN_ID_" + ban.getBanID());
                player.disconnect(ComponentParser.parse(plugin.getCommonPlugin().getMessageManager().translate(CommandMessages.COMMAND_PLAYER_BANNED, playerData.getLanguage(), Key.of("time", TimeHelper.format(playerData.getLanguage()).format(ban.getTime())), Key.of("reason", message))));
                return;
            }
            database.deleteBan(ban.getPlayer()).join();
        }

        for (int i = 0; i != 99; i++) {
            player.sendMessage(ComponentParser.parse(""));
        }

        String ip = player.getAddress().getAddress().getHostAddress();

        if (playerData == null) {
            playerData = database.createPlayer(uniqueId, ip, "1000", "1", "en-uk", "0", "false").join();
        }

        if (!(Objects.equals(ip, playerData.getIP()))) {
            playerData.setIp(ip);
            database.updatePlayer(playerData);
        }

        if (playerData.getLanguage() != null) {
            language = playerData.getLanguage();
        }

        if (!Countdown.online.contains(player.getUniqueId())) {
            Countdown.online.add(player.getUniqueId());
        }

        tablist = new TablistManager(plugin, player);
        // ArrayList<Friends> arrayList = database.getFriend(player.getUniqueId()).join();

        if (configuration.isMaintenance()) {
            if (!(player.hasPermission("system.maintenance.bypass") || player.hasPermission("system.maintenance.*"))) {
                Key reason = Key.of("reason", commonPlugin.getMessageManager().translate(SystemMessage.SYSTEM_MAINTENANCE_KICK_REASON, language, Key.of("reason", configuration.getMaintenanceReason())));
                Key end_date = Key.of("enddate", TimeHelper.format(language).format(TimeHelper.fromString(configuration.getDays())));
                player.disconnect(ComponentParser.parse(commonPlugin.getMessageManager().translate(SystemMessage.SYSTEM_MAINTENACE_KICK_CURRENTLY, language, end_date, reason)));
                return;
            }
        }

        if (ProxyServer.getInstance().getPlayers().size() >= Integer.parseInt(configuration.getPlayerAmount()) && !player.hasPermission("system.bypass.full")) {
            player.disconnect(ComponentParser.parse(commonPlugin.getMessageManager().translate(SystemMessage.SYSTEM_SERVICE_FULL, language)));
            return;
        }

        if (ProxyServer.getInstance().getPlayers().size() >= (Integer.parseInt(configuration.getPlayerAmount()) + 25) && !player.hasPermission("system.bypass.total.full")) {
            player.disconnect(ComponentParser.parse(commonPlugin.getMessageManager().translate(SystemMessage.SYSTEM_SERVICE_ABSOLUTFULL, language)));
            return;
        }

        if (ProxyServer.getInstance().getPlayers().size() >= (Integer.parseInt(configuration.getPlayerAmount()) + 25) && player.hasPermission("system.bypass.total.full")) {
            ProxiedPlayer looser = ProxyServer.getInstance().getPlayers().stream().skip((int) (ProxyServer.getInstance().getPlayers().size() * Math.random())).findFirst().orElse(null);
            if (looser == null) {
                return;
            }
            database.getPlayer(looser.getUniqueId()).thenAccept(data -> {
                looser.disconnect(ComponentParser.parse(commonPlugin.getMessageManager().translate(SystemMessage.SYSTEM_SERVICE_MAKESPACE, data.getLanguage())));
            });
        }


        if (!(playerData.isVerified())) {
            String code = new CodeGenerator().generate(8);
            verify.add(uniqueId, code);
            player.sendMessage(new TextComponent(ComponentColor.apply(commonPlugin.getMessageManager().translate(SystemMessage.VERIFY_CODE, playerData.getLanguage(), Key.of("code", code)))));

            Player finalPlayerData = playerData;
            verification = ProxyServer.getInstance().getScheduler().schedule(plugin, new Runnable() {
                @Override
                public void run() {
                    if (!(verify.contains(uniqueId))) {
                        verification.cancel();
                        return;
                    }
                    verify.remove(uniqueId);
                    player.disconnect(new TextComponent(ComponentColor.apply(commonPlugin.getMessageManager().translate(SystemMessage.VERIFY_DELAY, finalPlayerData.getLanguage()))));
                }
            }, 1, TimeUnit.MINUTES);
            return;
        }


        FriendSettings friendSettings = database.getFriendSettings(uniqueId).join();
        if (friendSettings == null) {
            friendSettings = database.createFriendSettings(uniqueId).join();
            return;
        }

        LinkPlayer linkPlayer = database.getLinkedPlayer(uniqueId).join();
        if (linkPlayer == null) {
            linkPlayer = database.createLinkPlayer(uniqueId).join();
            return;
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

    public static TablistManager tablist() {
        return tablist;
    }
}
