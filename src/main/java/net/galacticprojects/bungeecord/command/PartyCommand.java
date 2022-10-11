package net.galacticprojects.bungeecord.command;

import me.lauriichan.laylib.command.annotation.Action;
import me.lauriichan.laylib.command.annotation.Argument;
import me.lauriichan.laylib.command.annotation.Command;
import me.lauriichan.laylib.localization.Key;
import net.galacticprojects.bungeecord.ProxyPlugin;
import net.galacticprojects.bungeecord.command.impl.BungeeActor;
import net.galacticprojects.bungeecord.message.CommandMessages;
import net.galacticprojects.bungeecord.party.Party;
import net.galacticprojects.bungeecord.party.PartyManager;
import net.galacticprojects.common.CommonPlugin;
import net.galacticprojects.common.database.model.Player;
import net.galacticprojects.common.util.ComponentParser;
import net.galacticprojects.common.util.MojangProfileService;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

@Command(name = "party", description = "command.party.desc")
public class PartyCommand {

    HashMap<UUID, Party> invite = new HashMap<>();

    @Action("create")
    public void create(BungeeActor<?> actor, CommonPlugin commonPlugin, ProxyPlugin plugin, @Argument(name = "Party Name", optional = true) String name) {

        ProxiedPlayer player = actor.as(ProxiedPlayer.class).getHandle();
        Party party;
        PartyManager manager = new PartyManager(player);
        if(manager.getParty() != null) {

            return;
        }
        if(name == null || name.equals("") || name.equals(" ")) {
            party = new Party(player, new ArrayList<>());
        } else {
            party = new Party(player, new ArrayList<>(), name);
        }
        new PartyManager(party);
        commonPlugin.getDatabaseRef().asOptional().ifPresent(sql -> {
            sql.getPlayer(player.getUniqueId()).thenAccept(playerData -> {
                player.sendMessage(ComponentParser.parse(commonPlugin.getMessageManager().translate(CommandMessages.COMMAND_PARTY_CREATED, playerData.getLanguage(), Key.of("party", party.getName()))));
            });
        });

    }

    @Action("invite")
    public void invite(BungeeActor<?> actor, CommonPlugin commonPlugin, ProxyPlugin plugin, @Argument(name = "player") String name) {
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(MojangProfileService.getUniqueId(name));
        ProxiedPlayer leader = actor.as(ProxiedPlayer.class).getHandle();
        if(player == null) {
            commonPlugin.getDatabaseRef().asOptional().ifPresent(sql -> {
                sql.getPlayer(leader.getUniqueId()).thenAccept(playerData -> {
                    leader.sendMessage(ComponentParser.parse(commonPlugin.getMessageManager().translate(CommandMessages.COMMAND_GENERAL_PLAYER_NOT_FOUND, playerData.getLanguage())));
                });
            });
            return;
        }
        PartyManager manager = new PartyManager(leader);
        if(manager.getParty() == null) {
            commonPlugin.getDatabaseRef().asOptional().ifPresent(sql -> {
                sql.getPlayer(player.getUniqueId()).thenAccept(playerData -> {
                    player.sendMessage(ComponentParser.parse(commonPlugin.getMessageManager().translate(CommandMessages.COMMAND_PARTY_NO_PARTY, playerData.getLanguage())));
                });
            });
            return;
        }
        if(manager.getParty().getLeader() != leader.getUniqueId()) {
            commonPlugin.getDatabaseRef().asOptional().ifPresent(sql -> {
                sql.getPlayer(player.getUniqueId()).thenAccept(playerData -> {
                    player.sendMessage(ComponentParser.parse(commonPlugin.getMessageManager().translate(CommandMessages.COMMAND_PARTY_NOT_LEADER, playerData.getLanguage())));
                });
            });
            return;
        }
        String partyName = manager.getParty().getName();
        invite.put(player.getUniqueId(), manager.getParty());
        commonPlugin.getDatabaseRef().asOptional().ifPresent(sql -> {
            Player playerData = sql.getPlayer(player.getAddress().getAddress().getHostAddress()).join();
            Player leaderData = sql.getPlayer(leader.getAddress().getAddress().getHostAddress()).join();
            String language = playerData.getLanguage();
            if(language == null) {
                language = "en-uk";
            }
            String leaderLanguage = leaderData.getLanguage();
            if(leaderLanguage == null) {
                leaderLanguage = "en-uk";
            }
                player.sendMessage(ComponentParser.parse(commonPlugin.getMessageManager().translate(CommandMessages.COMMAND_PARTY_INVITED, language, Key.of("party", partyName))));
            leader.sendMessage(ComponentParser.parse(commonPlugin.getMessageManager().translate(CommandMessages.COMMAND_PARTY_INVITE, leaderLanguage, Key.of("player", player.getDisplayName()))));
        });
    }

    @Action("accept")
    public void accept(BungeeActor<?> actor, CommonPlugin commonPlugin, ProxyPlugin plugin) {
        ProxiedPlayer player = actor.as(ProxiedPlayer.class).getHandle();
        if(!invite.containsKey(player.getUniqueId())) {
            commonPlugin.getDatabaseRef().asOptional().ifPresent(sql -> {
                sql.getPlayer(player.getUniqueId()).thenAccept(playerData -> {
                    player.sendMessage(ComponentParser.parse(commonPlugin.getMessageManager().translate(CommandMessages.COMMAND_PARTY_NOT_INVITED, playerData.getLanguage())));
                });
            });
            return;
        }
        Party party = invite.get(player.getUniqueId());
        party.addMember(player.getUniqueId());
        invite.remove(player.getUniqueId());
        commonPlugin.getDatabaseRef().asOptional().ifPresent(sql -> {
            Player playerData = sql.getPlayer(player.getUniqueId()).join();
            Player leaderData = sql.getPlayer(party.getLeader()).join();
            ProxyServer.getInstance().getPlayer(party.getLeader()).sendMessage(ComponentParser.parse(commonPlugin.getMessageManager().translate(CommandMessages.COMMAND_PARTY_ACCEPT_LEADER, leaderData.getLanguage(), Key.of("party", party.getName()), Key.of("player", player.getDisplayName()))));
                player.sendMessage(ComponentParser.parse(commonPlugin.getMessageManager().translate(CommandMessages.COMMAND_PARTY_ACCEPT_TARGET, playerData.getLanguage(), Key.of("party", party.getName()))));
        });
    }

    @Action("decline")
    public void decline(BungeeActor<?> actor, CommonPlugin commonPlugin, ProxyPlugin plugin) {
        ProxiedPlayer player = actor.as(ProxiedPlayer.class).getHandle();
        if(!invite.containsKey(player.getUniqueId())) {
            commonPlugin.getDatabaseRef().asOptional().ifPresent(sql -> {
                sql.getPlayer(player.getUniqueId()).thenAccept(playerData -> {
                    player.sendMessage(ComponentParser.parse(commonPlugin.getMessageManager().translate(CommandMessages.COMMAND_PARTY_NOT_INVITED, playerData.getLanguage())));
                });
            });
            return;
        }
        Party party = invite.get(player.getUniqueId());
        invite.remove(player.getUniqueId());
        commonPlugin.getDatabaseRef().asOptional().ifPresent(sql -> {
            Player playerData = sql.getPlayer(player.getUniqueId()).join();
            Player leaderData = sql.getPlayer(party.getLeader()).join();
                    ProxyServer.getInstance().getPlayer(party.getLeader()).sendMessage(ComponentParser.parse(commonPlugin.getMessageManager().translate(CommandMessages.COMMAND_PARTY_DECLINE_LEADER, leaderData.getLanguage(), Key.of("party", party.getName()), Key.of("player", player.getDisplayName()))));
                player.sendMessage(ComponentParser.parse(commonPlugin.getMessageManager().translate(CommandMessages.COMMAND_PARTY_DECLINE_TARGET, playerData.getLanguage(), Key.of("party", party.getName()))));
        });
    }

    @Action("promote")
    public void promote(BungeeActor<?> actor, CommonPlugin commonPlugin, ProxyPlugin plugin, @Argument(name = "player") String name) {
        ProxiedPlayer player = actor.as(ProxiedPlayer.class).getHandle();
        commonPlugin.getDatabaseRef().asOptional().ifPresent(sql -> {
            sql.getPlayer(player.getUniqueId()).thenAccept(playerData -> {
                player.sendMessage(ComponentParser.parse(commonPlugin.getMessageManager().translate(CommandMessages.COMMAND_PARTY_PROMOTE_UNAVAILABLE, playerData.getLanguage())));
            });
        });

        /*ProxiedPlayer player = actor.as(ProxiedPlayer.class).getHandle();
        PartyManager manager = new PartyManager(player);
        Party party = manager.getParty();
        if(party == null) {
            player.sendMessage();
            return;
        }
        if(party.getLeader() != player.getUniqueId()) {
            player.sendMessage();
            return;
        }
        ProxiedPlayer target = ProxyServer.getInstance().getPlayer(MojangProfileService.getUniqueId(name));
        party.setModerator(target.getUniqueId());
        player.sendMessage();
        target.sendMessage();*/
    }

    @Action("public")
    public void publish(BungeeActor<?> actor, CommonPlugin commonPlugin, ProxyPlugin plugin, @Argument(name = "public") boolean visibility) {
        ProxiedPlayer player = actor.as(ProxiedPlayer.class).getHandle();
        commonPlugin.getDatabaseRef().asOptional().ifPresent(sql -> {
            sql.getPlayer(player.getUniqueId()).thenAccept(playerData -> {
                player.sendMessage(ComponentParser.parse(commonPlugin.getMessageManager().translate("command.party.not.available", playerData.getLanguage())));
            });
        });
    }

    @Action("delete")
    public void delete(BungeeActor<?> actor, CommonPlugin commonPlugin, ProxyPlugin plugin) {
        ProxiedPlayer player = actor.as(ProxiedPlayer.class).getHandle();
        PartyManager manager = new PartyManager(player);
        if(manager.getParty() == null) {
            commonPlugin.getDatabaseRef().asOptional().ifPresent(sql -> {
                sql.getPlayer(player.getUniqueId()).thenAccept(playerData -> {
                    player.sendMessage(ComponentParser.parse(commonPlugin.getMessageManager().translate(CommandMessages.COMMAND_PARTY_NO_PARTY, playerData.getLanguage())));
                });
            });
            return;
        }
        if(manager.getParty().getLeader() != player.getUniqueId()) {
            commonPlugin.getDatabaseRef().asOptional().ifPresent(sql -> {
                sql.getPlayer(player.getUniqueId()).thenAccept(playerData -> {
                    player.sendMessage(ComponentParser.parse(commonPlugin.getMessageManager().translate(CommandMessages.COMMAND_PARTY_NOT_LEADER, playerData.getLanguage())));
                });
            });
            return;
        }
        commonPlugin.getDatabaseRef().asOptional().ifPresent(sql -> {
            sql.getPlayer(player.getUniqueId()).thenAccept(playerData -> {
                player.sendMessage(ComponentParser.parse(commonPlugin.getMessageManager().translate(CommandMessages.COMMAND_PARTY_DELETED, playerData.getLanguage(), Key.of("party", manager.getParty().getName()))));

            for(UUID uniqueId : manager.getParty().getMember()) {
                sql.getPlayer(uniqueId).thenAccept(playerDatas -> {
                        ProxyServer.getInstance().getPlayer(uniqueId).sendMessage(ComponentParser.parse(commonPlugin.getMessageManager().translate(CommandMessages.COMMAND_PARTY_DELETED, playerDatas.getLanguage(), Key.of("party", manager.getParty().getName()))));
                    });
                }
            });
        });
        manager.getParty().deleteParty();
    }
}
