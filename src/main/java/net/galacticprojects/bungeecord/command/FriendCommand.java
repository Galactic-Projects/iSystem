package net.galacticprojects.bungeecord.command;


import me.lauriichan.laylib.command.Actor;
import me.lauriichan.laylib.command.annotation.Action;
import me.lauriichan.laylib.command.annotation.Argument;
import me.lauriichan.laylib.command.annotation.Command;
import me.lauriichan.laylib.localization.Key;
import net.galacticprojects.bungeecord.ProxyPlugin;
import net.galacticprojects.bungeecord.command.impl.BungeeActor;
import net.galacticprojects.bungeecord.message.CommandMessages;
import net.galacticprojects.bungeecord.util.TimeHelper;
import net.galacticprojects.common.CommonPlugin;
import net.galacticprojects.common.database.model.FriendRequest;
import net.galacticprojects.common.database.model.FriendSettings;
import net.galacticprojects.common.database.model.Friends;
import net.galacticprojects.common.util.ComponentParser;
import net.galacticprojects.common.util.MojangProfileService;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.UUID;

@Command(name = "friend",  description = "Manage your friends!")
public class FriendCommand {

    @Action("toggle")
    public void toggle(BungeeActor<?> actor, CommonPlugin common, ProxyPlugin plugin) {
        ProxiedPlayer bungeePlayer = actor.as(ProxiedPlayer.class).getHandle();

        if (bungeePlayer == null) {
            return;
        }

        UUID uniqueId = bungeePlayer.getUniqueId();

        common.getDatabaseRef().asOptional().ifPresent(sql -> {
            sql.getPlayer(uniqueId).thenAccept(playerData -> {
                FriendSettings friendSettings = sql.getFriendSettings(uniqueId).join();

                if (sql.getFriendSettings(uniqueId).join().isRequests()) {
                    sql.updateFriendSettings(uniqueId, friendSettings.isJump(), friendSettings.isMessages(), false);
                    actor.sendTranslatedMessage(CommandMessages.FRIEND_TOGGLE_REQUESTS_ON);
                } else {
                    sql.updateFriendSettings(uniqueId, friendSettings.isJump(), friendSettings.isMessages(), true);
                    actor.sendTranslatedMessage(CommandMessages.FRIEND_TOGGLE_REQUESTS_OFF);
                }
            });
        });
    }

    @Action("togglemessage")
    public void toggleMessage(BungeeActor<?> actor, CommonPlugin common, ProxyPlugin plugin) {
        ProxiedPlayer bungeePlayer = actor.as(ProxiedPlayer.class).getHandle();

        if (bungeePlayer == null) {
            return;
        }

        UUID uniqueId = bungeePlayer.getUniqueId();

        common.getDatabaseRef().asOptional().ifPresent(sql -> {
            sql.getPlayer(uniqueId).thenAccept(playerData -> {
                FriendSettings friendSettings = sql.getFriendSettings(uniqueId).join();

                if (sql.getFriendSettings(uniqueId).join().isMessages()) {

                    sql.updateFriendSettings(uniqueId, friendSettings.isJump(), false, friendSettings.isRequests());
                    actor.sendTranslatedMessage(CommandMessages.FRIEND_TOGGLE_MESSAGES_OFF);
                } else {
                    sql.updateFriendSettings(uniqueId, friendSettings.isJump(), true, friendSettings.isRequests());
                    actor.sendTranslatedMessage(CommandMessages.FRIEND_TOGGLE_MESSAGES_ON);
                }

            });
        });
    }

    @Action("togglejump")
    public void toggleJump(BungeeActor<?> actor, CommonPlugin common, ProxyPlugin plugin) {
        ProxiedPlayer bungeePlayer = actor.as(ProxiedPlayer.class).getHandle();

        if (bungeePlayer == null) {
            return;
        }

        UUID uniqueId = bungeePlayer.getUniqueId();

        common.getDatabaseRef().asOptional().ifPresent(sql -> {
            sql.getPlayer(uniqueId).thenAccept(playerData -> {
                FriendSettings friendSettings = sql.getFriendSettings(uniqueId).join();

                if (sql.getFriendSettings(uniqueId).join().isJump()) {
                    sql.updateFriendSettings(uniqueId, false, friendSettings.isMessages(), friendSettings.isRequests());
                    actor.sendTranslatedMessage(CommandMessages.FRIEND_TOGGLE_JUMP_OFF);
                } else {
                    sql.updateFriendSettings(uniqueId, true, friendSettings.isMessages(), friendSettings.isRequests());
                    actor.sendTranslatedMessage(CommandMessages.FRIEND_TOGGLE_JUMP_ON);
                }

            });
        });
    }

    @Action("list")
    public void list(BungeeActor<?> actor, CommonPlugin common, ProxyPlugin plugin) {
        ProxiedPlayer bungeePlayer = actor.as(ProxiedPlayer.class).getHandle();

        if (bungeePlayer == null) {
            return;
        }

        UUID uniqueId = bungeePlayer.getUniqueId();

        common.getDatabaseRef().asOptional().ifPresent(sql -> {
            sql.getPlayer(uniqueId).thenAccept(playerData -> {
                if (!(sql.hasFriends(uniqueId).join())) {
                    actor.sendTranslatedMessage(CommandMessages.FRIEND_NONE_LIST);
                    return;
                }

                actor.sendTranslatedMessage(CommandMessages.FRIEND_LIST);
                for (Friends friends : sql.getFriend(uniqueId).join()) {
                    UUID uniqueIdFriend = friends.getFriendUniqueId();
                    OffsetDateTime time = TimeHelper.fromString(friends.getDate());
                    String date = TimeHelper.BAN_TIME_FORMATTER.format(time);
                    ProxiedPlayer friend = ProxyServer.getInstance().getPlayer(uniqueIdFriend);

                    TextComponent message = new TextComponent();
                    message.setText("§b" + MojangProfileService.getName(uniqueIdFriend));
                    message.addExtra(" §7▪ §3" + date);
                    message.addExtra("\n§7» ");

                    if (friend == null) {
                        message.addExtra("§cOFFLINE");
                        message.addExtra(" §7«\n");
                        bungeePlayer.sendMessage(message);
                        return;
                    }

                    message.addExtra("§aONLINE");
                    message.addExtra(" §7«\n");

                    bungeePlayer.sendMessage(message);
                }
            });
        });
    }

    @Action("clear")
    public void clear(BungeeActor<?> actor, CommonPlugin common, ProxyPlugin plugin, @Argument(name = "yes,true/no,false?") boolean clear) {
        ProxiedPlayer bungeePlayer = actor.as(ProxiedPlayer.class).getHandle();

        if (bungeePlayer == null) {
            return;
        }

        UUID uniqueId = bungeePlayer.getUniqueId();

        common.getDatabaseRef().asOptional().ifPresent(sql -> {
            sql.getPlayer(uniqueId).thenAccept(playerData -> {
                if (!(clear)) {
                    actor.sendTranslatedMessage(CommandMessages.FRIEND_LIST_CLEARED_DENY);
                    return;
                }

                for (Friends friends : sql.getFriend(uniqueId).join()) {
                    UUID uniqueIdFriend = friends.getFriendUniqueId();
                    sql.deleteFriend(uniqueId, uniqueIdFriend);
                    sql.deleteFriend(uniqueIdFriend, uniqueId);
                }
                actor.sendTranslatedMessage(CommandMessages.FRIEND_LIST_CLEARED);
            });
        });


    }

    @Action("requests")
    public void requests(BungeeActor<?> actor, CommonPlugin common, ProxyPlugin plugin) {
        ProxiedPlayer bungeePlayer = actor.as(ProxiedPlayer.class).getHandle();

        if (bungeePlayer == null) {
            return;
        }

        UUID uniqueId = bungeePlayer.getUniqueId();

        common.getDatabaseRef().asOptional().ifPresent(sql -> {
            sql.getPlayer(uniqueId).thenAccept(playerData -> {
                if (!(sql.isFriendRequestedByRequestor(uniqueId).join())) {
                    actor.sendTranslatedMessage(CommandMessages.FRIEND_NONE_REQUEST);
                    return;
                }

                TextComponent message = null;
                actor.sendTranslatedMessage(CommandMessages.FRIEND_REQUESTS_LIST);
                for (FriendRequest friendRequest : sql.getFriendRequestByRequesterId(uniqueId).join()) {
                    UUID request = friendRequest.getUUID();

                    TextComponent accept = new TextComponent();
                    accept.setText(common.getMessageManager().translate(CommandMessages.FRIEND_REQUEST_ACCEPT, playerData.getLanguage()).replaceAll("&", "§"));
                    accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/friend accept"));
                    TextComponent deny = new TextComponent();
                    deny.setText(common.getMessageManager().translate(CommandMessages.FRIEND_REQUEST_DENY, playerData.getLanguage()).replaceAll("&", "§"));
                    deny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/friend deny"));

                    OffsetDateTime time = TimeHelper.fromString(friendRequest.getDate());
                    String date = TimeHelper.BAN_TIME_FORMATTER.format(time);

                    message = new TextComponent();
                    message.setText("§b" + MojangProfileService.getName(request));
                    message.addExtra(" §7▪ §3" + date);
                    message.addExtra("\n§7» ");
                    message.addExtra(accept);
                    message.addExtra(" §8▏§7 ");
                    message.addExtra(deny);
                    message.addExtra(" §7«\n");
                    bungeePlayer.sendMessage(message);
                }
            });
        });
    }

    @Action("pendingrequests")
    public void pendingRequests(BungeeActor<?> actor, CommonPlugin common, ProxyPlugin plugin) {
        ProxiedPlayer bungeePlayer = actor.as(ProxiedPlayer.class).getHandle();

        if (bungeePlayer == null) {
            return;
        }

        UUID uniqueId = bungeePlayer.getUniqueId();

        common.getDatabaseRef().asOptional().ifPresent(sql -> {
            sql.getPlayer(uniqueId).thenAccept(playerData -> {
                if (!(sql.isFriendRequested(uniqueId).join())) {
                    actor.sendTranslatedMessage(CommandMessages.FRIEND_PENDING_REQUEST_NO);
                    return;
                }

                String message = null;
                actor.sendTranslatedMessage(CommandMessages.FRIEND_PENDING_REQUEST_LIST);
                for (FriendRequest friendRequest : sql.getFriendRequest(uniqueId).join()) {
                    UUID request = friendRequest.getRequestUUID();
                    OffsetDateTime time = TimeHelper.fromString(friendRequest.getDate());
                    String date = TimeHelper.BAN_TIME_FORMATTER.format(time);
                    message = "§b" + MojangProfileService.getName(request) + " §7▪ §3" + date + "\n";
                    actor.sendMessage(message);
                }
            });
        });
    }

    @Action("add")
    public void add(BungeeActor<?> actor, CommonPlugin common, ProxyPlugin plugin, @Argument(name = "player") String player) {
        ProxiedPlayer bungeePlayer = actor.as(ProxiedPlayer.class).getHandle();

        if (bungeePlayer == null) {
            return;
        }

        UUID uniqueId = bungeePlayer.getUniqueId();
        UUID uniqueTarget = MojangProfileService.getUniqueId(player);

        common.getDatabaseRef().asOptional().ifPresent(sql -> {
            sql.getPlayer(uniqueId).thenAccept(playerData -> {
                ProxiedPlayer target = ProxyServer.getInstance().getPlayer(player);

                if (!(sql.getFriendSettings(uniqueTarget).join().isRequests())) {
                    actor.sendTranslatedMessage(CommandMessages.FRIEND_REQUEST_DISABLED);
                    return;
                }

                if (sql.isFriendRequested(uniqueId, uniqueTarget).join()) {
                    actor.sendTranslatedMessage(CommandMessages.FRIEND_REQUEST_ALREADY);
                    return;
                }

                if (sql.isAlreadyFriend(uniqueId, uniqueTarget).join() || sql.isAlreadyFriend(uniqueTarget, uniqueId).join()) {
                    actor.sendTranslatedMessage(CommandMessages.FRIEND_ALREADY);
                    return;
                }

                if (sql.isFriendRequested(uniqueTarget, uniqueId).join() || sql.isFriendRequested(uniqueId, uniqueTarget).join()) {
                    sql.createFriend(uniqueId, uniqueTarget, TimeHelper.toString(OffsetDateTime.now()));
                    sql.createFriend(uniqueTarget, uniqueId, TimeHelper.toString(OffsetDateTime.now()));
                    sql.deleteFriendRequest(uniqueId, uniqueTarget);
                    sql.deleteFriendRequest(uniqueTarget, uniqueId);
                    actor.sendTranslatedMessage(CommandMessages.FRIEND_BOTH_SENDED);
                    if (target == null) {
                        return;
                    }
                    sql.getPlayer(uniqueTarget).thenAccept(targetPlayerData -> {
                        target.sendMessage(ComponentParser.parse(common.getMessageManager().translate(CommandMessages.FRIEND_BOTH_SENDED, targetPlayerData.getLanguage())));
                    });
                    return;
                }


                sql.createFriendRequest(uniqueId, uniqueTarget, TimeHelper.toString(OffsetDateTime.now()));
                actor.sendTranslatedMessage(CommandMessages.FRIEND_REQUEST_SENT, Key.of("target", MojangProfileService.getName(uniqueTarget)));

                if (target == null) {
                    return;
                }
                sql.getPlayer(uniqueTarget).thenAccept(targetPlayerData -> {
                    TextComponent accept = new TextComponent();
                    accept.setText(common.getMessageManager().translate(CommandMessages.FRIEND_REQUEST_ACCEPT, targetPlayerData.getLanguage()).replaceAll("&", "§"));
                    accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/friend accept"));
                    TextComponent deny = new TextComponent();
                    deny.setText(common.getMessageManager().translate(CommandMessages.FRIEND_REQUEST_DENY, targetPlayerData.getLanguage()).replaceAll("&", "§"));
                    deny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/friend deny"));

                    TextComponent message = new TextComponent();
                    OffsetDateTime time = TimeHelper.fromString(TimeHelper.toString(OffsetDateTime.now()));
                    String date = TimeHelper.BAN_TIME_FORMATTER.format(time);
                    message.setText("§b" + bungeePlayer.getName());
                    message.addExtra(" §7▪ §3" + date);
                    message.addExtra("\n§7» ");
                    message.addExtra(accept);
                    message.addExtra(" §8▏§7 ");
                    message.addExtra(deny);
                    message.addExtra(" §7«\n");

                    target.sendMessage(ComponentParser.parse(common.getMessageManager().translate(CommandMessages.FRIEND_REQUEST_SENT_TARGET, targetPlayerData.getLanguage())));
                    target.sendMessage(message);
                });
            });
        });
    }

    @Action("cancelrequest")
    public void cancelRequest(BungeeActor<?> actor, CommonPlugin common, ProxyPlugin plugin, @Argument(name = "player") String player) {
        ProxiedPlayer bungeePlayer = actor.as(ProxiedPlayer.class).getHandle();

        if (bungeePlayer == null) {
            return;
        }

        UUID uniqueId = bungeePlayer.getUniqueId();
        UUID uniqueTarget = MojangProfileService.getUniqueId(player);

        common.getDatabaseRef().asOptional().ifPresent(sql -> {
            sql.getPlayer(uniqueId).thenAccept(playerData -> {
                if (!(sql.isFriendRequested(uniqueId, uniqueTarget).join())) {
                    actor.sendTranslatedMessage(CommandMessages.FRIEND_REQUEST_NOTSENT);
                    return;
                }

                sql.deleteFriendRequest(uniqueId, uniqueTarget);
                actor.sendTranslatedMessage(CommandMessages.FRIEND_REQUEST_DELETED, Key.of("player", MojangProfileService.getName(uniqueTarget)));
            });
        });

    }

    @Action("remove")
    public void remove(BungeeActor<?> actor, CommonPlugin common, ProxyPlugin plugin, @Argument(name = "player") String player) {
        ProxiedPlayer bungeePlayer = actor.as(ProxiedPlayer.class).getHandle();

        if (bungeePlayer == null) {
            return;
        }

        UUID uniqueId = bungeePlayer.getUniqueId();
        UUID uniqueIdTarget = MojangProfileService.getUniqueId(player);

        common.getDatabaseRef().asOptional().ifPresent(sql -> {
            sql.getPlayer(uniqueId).thenAccept(playerData -> {
                if(!(sql.isAlreadyFriend(uniqueId, uniqueIdTarget).join())) {
                    actor.sendTranslatedMessage(CommandMessages.FRIEND_NONE);
                    return;
                }

                sql.deleteFriend(uniqueId, uniqueIdTarget);
                sql.deleteFriend(uniqueIdTarget, uniqueId);
                actor.sendTranslatedMessage(CommandMessages.FRIEND_DELETE, Key.of("player", bungeePlayer.getUniqueId()));
            });
        });
    }

    @Action("acceptall")
    public void acceptall(BungeeActor<?> actor, CommonPlugin common, ProxyPlugin plugin) {
        ProxiedPlayer bungeePlayer = actor.as(ProxiedPlayer.class).getHandle();

        if (bungeePlayer == null) {
            return;
        }

        UUID uniqueId = bungeePlayer.getUniqueId();

        common.getDatabaseRef().asOptional().ifPresent(sql -> {
            sql.getPlayer(uniqueId).thenAccept(playerData -> {
                if (!(sql.isFriendRequested(uniqueId).join())) {
                    actor.sendTranslatedMessage(CommandMessages.FRIEND_REQUEST_NO_REQUESTS);
                    return;
                }

                for (FriendRequest requests : sql.getFriendRequestByRequesterId(uniqueId).join()) {
                    UUID requestId = requests.getUUID();
                    sql.createFriend(uniqueId, requestId, TimeHelper.toString(OffsetDateTime.now()));
                    sql.createFriend(requestId, uniqueId, TimeHelper.toString(OffsetDateTime.now()));
                    sql.deleteFriendRequest(requestId, uniqueId);
                }
                actor.sendTranslatedMessage(CommandMessages.FRIEND_REQUEST_ACCEPT_ALL);
            });
        });
    }

    @Action("denyall")
    public void denyall(BungeeActor<?> actor, CommonPlugin common, ProxyPlugin plugin) {
        ProxiedPlayer bungeePlayer = actor.as(ProxiedPlayer.class).getHandle();

        if (bungeePlayer == null) {
            return;
        }

        UUID uniqueId = bungeePlayer.getUniqueId();

        common.getDatabaseRef().asOptional().ifPresent(sql -> {
            sql.getPlayer(uniqueId).thenAccept(playerData -> {
                if (!(sql.isFriendRequested(uniqueId).join())) {
                    actor.sendTranslatedMessage(CommandMessages.FRIEND_REQUEST_NO_REQUESTS);
                    return;
                }

                for (FriendRequest requests : sql.getFriendRequestByRequesterId(uniqueId).join()) {
                    UUID requestId = requests.getUUID();
                    sql.deleteFriendRequest(requestId, uniqueId);
                }
                actor.sendTranslatedMessage(CommandMessages.FRIEND_REQUEST_DELETED_ALL);
            });
        });
    }

    @Action("accept")
    public void accept(BungeeActor<?> actor, CommonPlugin common, ProxyPlugin plugin, @Argument(name = "player") String player) {
        ProxiedPlayer bungeePlayer = actor.as(ProxiedPlayer.class).getHandle();

        if (bungeePlayer == null) {
            return;
        }

        UUID uniqueId = bungeePlayer.getUniqueId();
        UUID uniqueTarget = MojangProfileService.getUniqueId(player);

        common.getDatabaseRef().asOptional().ifPresent(sql -> {
            sql.getPlayer(uniqueId).thenAccept(playerData -> {
                ProxiedPlayer target = ProxyServer.getInstance().getPlayer(player);
                if (target == null) {
                    return;
                }

                if (!(sql.isFriendRequested(uniqueTarget, uniqueId).join())) {
                    actor.sendTranslatedMessage(CommandMessages.FRIEND_REQUEST_DENY_NOTSENT);
                    return;
                }

                if (sql.isAlreadyFriend(uniqueTarget, uniqueId).join()) {
                    actor.sendTranslatedMessage(CommandMessages.FRIEND_ALREADY);
                    return;
                }


                sql.createFriend(uniqueId, uniqueTarget, TimeHelper.toString(OffsetDateTime.now()));
                sql.createFriend(uniqueTarget, uniqueId, TimeHelper.toString(OffsetDateTime.now()));
                sql.deleteFriendRequest(uniqueTarget, uniqueId);

                sql.getPlayer(target.getUniqueId()).thenAccept(targetData -> {
                    target.sendMessage(ComponentParser.parse(common.getMessageManager().translate(CommandMessages.FRIEND_REQUEST_ACCEPT_TARGET, targetData.getLanguage(), Key.of("player", actor.getName()))));
                });
                actor.sendTranslatedMessage(CommandMessages.FRIEND_REQUEST_ACCEPT_ALL);
            });
        });
    }


    @Action("deny")
    public void deny(BungeeActor<?> actor, CommonPlugin common, ProxyPlugin plugin, @Argument(name = "player") String player) {
        ProxiedPlayer bungeePlayer = actor.as(ProxiedPlayer.class).getHandle();

        if (bungeePlayer == null) {
            return;
        }

        UUID uniqueId = bungeePlayer.getUniqueId();
        UUID uniqueTarget = MojangProfileService.getUniqueId(player);

        common.getDatabaseRef().asOptional().ifPresent(sql -> {
            sql.getPlayer(uniqueId).thenAccept(playerData -> {
                if (!(sql.isFriendRequested(uniqueTarget, uniqueId).join())) {
                    actor.sendTranslatedMessage(CommandMessages.FRIEND_REQUEST_DENY_NOTSENT);
                    return;
                }

                sql.deleteFriendRequest(uniqueTarget, uniqueId);
                actor.sendTranslatedMessage(CommandMessages.FRIEND_REQUEST_REMOVED, Key.of("player", MojangProfileService.getName(uniqueTarget)));
            });
        });
    }
}
