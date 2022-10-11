package net.galacticprojects.bungeecord.command;


import me.lauriichan.laylib.command.annotation.Action;
import me.lauriichan.laylib.command.annotation.Argument;
import me.lauriichan.laylib.command.annotation.Command;
import me.lauriichan.laylib.command.annotation.Param;
import me.lauriichan.laylib.localization.Key;
import net.galacticprojects.bungeecord.ProxyPlugin;
import net.galacticprojects.bungeecord.command.impl.BungeeActor;
import net.galacticprojects.bungeecord.message.CommandMessages;
import net.galacticprojects.bungeecord.util.TimeHelper;
import net.galacticprojects.common.CommonPlugin;
import net.galacticprojects.common.database.model.FriendRequest;
import net.galacticprojects.common.database.model.FriendSettings;
import net.galacticprojects.common.util.ComponentParser;
import net.galacticprojects.common.util.MojangProfileService;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.checkerframework.checker.units.qual.A;
import org.w3c.dom.Text;

import java.lang.reflect.Proxy;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.UUID;

@Command(name = "friend",  description = "Manage your friends!")
public class FriendCommand {

    @Action("toggle")
    public void toggle(BungeeActor<?> actor, CommonPlugin common, ProxyPlugin plugin, @Argument(name = "on/off", params = {
            @Param(type = 0, name = "minimum", stringValue = "off"),
            @Param(type = 0, name = "maximum", stringValue = "on")
    }) String name) {

        ProxiedPlayer playerA = actor.as(ProxiedPlayer.class).getHandle();
        UUID uniqueId = playerA.getUniqueId();


        common.getDatabaseRef().asOptional().ifPresent(sql -> {
            sql.getPlayer(uniqueId).thenAccept(playerData -> {
                FriendSettings friendSettings = sql.getFriendSettings(uniqueId).join();

                if (name.equalsIgnoreCase("off")) {
                    if (!(sql.getFriendSettings(uniqueId).join().isRequests())) {
                        playerA.sendMessage(ComponentParser.parse(common.getMessageManager().translate(CommandMessages.FRIEND_TOGGLE_ALREADY_REQUESTS_OFF, playerData.getLanguage())));
                        return;
                    }
                    sql.updateFriendSettings(uniqueId, friendSettings.isJump(), friendSettings.isMessages(), false);
                    playerA.sendMessage(ComponentParser.parse(common.getMessageManager().translate(CommandMessages.FRIEND_TOGGLE_REQUESTS_OFF, playerData.getLanguage())));
                } else if (name.equalsIgnoreCase("on")) {
                    if (sql.getFriendSettings(uniqueId).join().isRequests()) {
                        playerA.sendMessage(ComponentParser.parse(common.getMessageManager().translate(CommandMessages.FRIEND_TOGGLE_ALREADY_REQUESTS_ON, playerData.getLanguage())));
                        return;
                    }
                    sql.updateFriendSettings(uniqueId, friendSettings.isJump(), friendSettings.isMessages(), true);
                    playerA.sendMessage(ComponentParser.parse(common.getMessageManager().translate(CommandMessages.FRIEND_TOGGLE_REQUESTS_ON, playerData.getLanguage())));
                }
            });
        });
    }

    @Action("togglemessage")
    public void toggleMessage(BungeeActor<?> actor, CommonPlugin common, ProxyPlugin plugin, @Argument(name = "on/off", params = {
            @Param(type = 0, name = "value", stringValue = "off"),
            @Param(type = 0, name = "value", stringValue = "on")
    }) String name) {

        ProxiedPlayer playerA = actor.as(ProxiedPlayer.class).getHandle();
        UUID uniqueId = playerA.getUniqueId();

        common.getDatabaseRef().asOptional().ifPresent(sql -> {
            sql.getPlayer(uniqueId).thenAccept(playerData -> {
                FriendSettings friendSettings = sql.getFriendSettings(uniqueId).join();

                if (name.equalsIgnoreCase("off")) {
                    if (!(sql.getFriendSettings(uniqueId).join().isMessages())) {
                        playerA.sendMessage(ComponentParser.parse(common.getMessageManager().translate(CommandMessages.FRIEND_TOGGLE_ALREADY_MESSAGES_OFF, playerData.getLanguage())));
                        return;
                    }
                    sql.updateFriendSettings(uniqueId, friendSettings.isJump(), false, friendSettings.isRequests());
                    playerA.sendMessage(ComponentParser.parse(common.getMessageManager().translate(CommandMessages.FRIEND_TOGGLE_MESSAGES_OFF, playerData.getLanguage())));
                } else if (name.equalsIgnoreCase("on")) {
                    if (sql.getFriendSettings(uniqueId).join().isMessages()) {
                        playerA.sendMessage(ComponentParser.parse(common.getMessageManager().translate(CommandMessages.FRIEND_TOGGLE_ALREADY_MESSAGES_ON, playerData.getLanguage())));
                        return;
                    }
                    sql.updateFriendSettings(uniqueId, friendSettings.isJump(), true, friendSettings.isRequests());
                    playerA.sendMessage(ComponentParser.parse(common.getMessageManager().translate(CommandMessages.FRIEND_TOGGLE_MESSAGES_ON, playerData.getLanguage())));
                }

            });
        });
    }

    @Action("togglejump")
    public void toggleJump(BungeeActor<?> actor, CommonPlugin common, ProxyPlugin plugin, @Argument(name = "on/off", params = {
            @Param(type = 0, name = "minimum", stringValue = "off"),
            @Param(type = 0, name = "maximum", stringValue = "on")
    }) String name) {
        ProxiedPlayer playerA = actor.as(ProxiedPlayer.class).getHandle();
        UUID uniqueId = playerA.getUniqueId();

        common.getDatabaseRef().asOptional().ifPresent(sql -> {
            sql.getPlayer(uniqueId).thenAccept(playerData -> {
                FriendSettings friendSettings = sql.getFriendSettings(uniqueId).join();

                if(name.equalsIgnoreCase("off")) {
                    if(!(sql.getFriendSettings(uniqueId).join().isJump())){
                        playerA.sendMessage(ComponentParser.parse(common.getMessageManager().translate(CommandMessages.FRIEND_TOGGLE_ALREADY_JUMP_OFF, playerData.getLanguage())));
                        return;
                    }
                    sql.updateFriendSettings(uniqueId, false, friendSettings.isMessages(), friendSettings.isRequests());
                    playerA.sendMessage(ComponentParser.parse(common.getMessageManager().translate(CommandMessages.FRIEND_TOGGLE_JUMP_OFF, playerData.getLanguage())));
                } else if(name.equalsIgnoreCase("on")) {
                    if(sql.getFriendSettings(uniqueId).join().isJump()){
                        playerA.sendMessage(ComponentParser.parse(common.getMessageManager().translate(CommandMessages.FRIEND_TOGGLE_ALREADY_JUMP_ON, playerData.getLanguage())));
                        return;
                    }
                    sql.updateFriendSettings(uniqueId, true, friendSettings.isMessages(), friendSettings.isRequests());
                    playerA.sendMessage(ComponentParser.parse(common.getMessageManager().translate(CommandMessages.FRIEND_TOGGLE_JUMP_ON, playerData.getLanguage())));
                }

            });
        });
    }

    @Action("list")
    public void list(BungeeActor<?> actor, CommonPlugin common, ProxyPlugin plugin) {

    }

    @Action("clear")
    public void clear(BungeeActor<?> actor, CommonPlugin common, ProxyPlugin plugin) {

    }

    @Action("requests")
    public void requests(BungeeActor<?> actor, CommonPlugin common, ProxyPlugin plugin) {
        ProxiedPlayer playerA = actor.as(ProxiedPlayer.class).getHandle();
        UUID uniqueId = playerA.getUniqueId();

        common.getDatabaseRef().asOptional().ifPresent(sql -> {
            sql.getPlayer(uniqueId).thenAccept(playerData -> {
                if(sql.getFriendRequestByRequesterId(uniqueId).join().size() == 0) {
                    playerA.sendMessage(ComponentParser.parse(common.getMessageManager().translate(CommandMessages.FRIEND_NONE_REQUEST, playerData.getLanguage())));
                    return;
                }

                Key values = null;
                TextComponent accept = new TextComponent(ComponentParser.parse(common.getMessageManager().translate(CommandMessages.FRIEND_REQUEST_ACCEPT, playerData.getLanguage())));
                accept.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, "/friend accept"));
                TextComponent deny = new TextComponent(ComponentParser.parse(common.getMessageManager().translate(CommandMessages.FRIEND_REQUEST_DENY, playerData.getLanguage())));
                accept.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, "/friend deny"));
                for(int i = 0; i != sql.getFriendRequestByRequesterId(uniqueId).join().size(); i++){
                    UUID request = sql.getFriendRequestByRequesterId(uniqueId).join().get(i).getUUID();
                    String date = sql.getFriendRequestByRequesterId(uniqueId).join().get(i).getDate();
                    accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, MojangProfileService.getName(request)));
                    deny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, MojangProfileService.getName(request)));
                    TextComponent message = new TextComponent();
                    message.addExtra("&b" + MojangProfileService.getName(request));
                    message.addExtra(" &7▪ &3");
                    message.addExtra(date);
                    message.addExtra(accept);
                    message.addExtra(" &8▏&7");
                    message.addExtra(deny);
                    message.addExtra("\n");
                    values = Key.of("input",  message);
                }

                playerA.sendMessage(ComponentParser.parse(common.getMessageManager().translate(CommandMessages.FRIEND_REQUESTS_LIST, playerData.getLanguage(), values)));
            });
        });
    }

    @Action("pendingrequests")
    public void pendingRequests(BungeeActor<?> actor, CommonPlugin  common, ProxyPlugin plugin) {
        ProxiedPlayer playerA = actor.as(ProxiedPlayer.class).getHandle();
        UUID uniqueId = playerA.getUniqueId();

        common.getDatabaseRef().asOptional().ifPresent(sql -> {
            sql.getPlayer(uniqueId).thenAccept(playerData -> {
                if(sql.getFriendRequest(uniqueId).join().size() == 0) {
                    playerA.sendMessage(ComponentParser.parse(common.getMessageManager().translate(CommandMessages.FRIEND_PENDING_REQUEST_NO, playerData.getLanguage())));
                    return;
                }

                Key values = null;
                for(int i = 0; i != sql.getFriendRequest(uniqueId).join().size(); i++){
                    UUID request = sql.getFriendRequest(uniqueId).join().get(i).getRequestUUID();
                    String date = String.valueOf(TimeHelper.fromString(sql.getFriendRequest(uniqueId).join().get(i).getDate()));
                    values = Key.of("input", "&b" + MojangProfileService.getName(request) + " &7▪ &3" + date + "\n");
                }

                playerA.sendMessage(ComponentParser.parse(common.getMessageManager().translate(CommandMessages.FRIEND_PENDING_REQUEST_LIST, playerData.getLanguage(), values)));
            });
        });
    }

    @Action("add")
    public void add(BungeeActor<?> actor, CommonPlugin common, ProxyPlugin plugin, @Argument(name="player") String player) {
        ProxiedPlayer playerA = actor.as(ProxiedPlayer.class).getHandle();
        UUID uniqueId = playerA.getUniqueId();
        UUID uniqueTarget = MojangProfileService.getUniqueId(player);

        common.getDatabaseRef().asOptional().ifPresent(sql -> {
            sql.getPlayer(uniqueId).thenAccept(playerData -> {
                if(!(sql.getFriendSettings(uniqueTarget).join().isRequests())){
                    playerA.sendMessage(ComponentParser.parse(common.getMessageManager().translate(CommandMessages.FRIEND_REQUEST_DISABLED, playerData.getLanguage())));
                    return;
                }

                ArrayList<FriendRequest> requests = sql.getFriendRequest(uniqueId).join();

                UUID request = null;
                for(int i = 0; i < requests.size(); i++) {
                    if(requests.get(i).getRequestUUID() == uniqueTarget) {
                        request = requests.get(i).getRequestUUID();
                    }
                }

                if(request != null){
                    playerA.sendMessage(ComponentParser.parse(common.getMessageManager().translate(CommandMessages.FRIEND_REQUEST_ALREADY, playerData.getLanguage())));
                } else {

                    sql.createFriendRequest(uniqueId, uniqueTarget, TimeHelper.toString(OffsetDateTime.now()));
                    playerA.sendMessage(ComponentParser.parse(common.getMessageManager().translate(CommandMessages.FRIEND_REQUEST_SENT, playerData.getLanguage(), Key.of("target", MojangProfileService.getName(uniqueTarget)))));

                    ProxiedPlayer target = ProxyServer.getInstance().getPlayer(player);
                    if (target == null) {
                        return;
                    }

                    TextComponent accept = new TextComponent(ComponentParser.parse(common.getMessageManager().translate(CommandMessages.FRIEND_REQUEST_ACCEPT, playerData.getLanguage())));
                    accept.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, "/friend accept"));
                    TextComponent deny = new TextComponent(ComponentParser.parse(common.getMessageManager().translate(CommandMessages.FRIEND_REQUEST_DENY, playerData.getLanguage())));
                    accept.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, "/friend deny"));
                    TextComponent message = new TextComponent();
                    message.addExtra("&b" + playerA.getName());
                    message.addExtra(" &7▪ &3");
                    message.addExtra(String.valueOf(TimeHelper.fromString(TimeHelper.toString(OffsetDateTime.now()))));
                    message.addExtra(accept);
                    message.addExtra(" &8▏&7");
                    message.addExtra(deny);
                    message.addExtra("\n");
                    Key values = Key.of("input", message);

                    sql.getPlayer(uniqueTarget).thenAccept(targetPlayerData -> {
                        target.sendMessage(ComponentParser.parse(common.getMessageManager().translate(CommandMessages.FRIEND_REQUEST_SENT_TARGET, playerData.getLanguage(), values)));
                    });
                }
            });
        });
    }

    @Action("cancelrequest")
    public void cancelRequest(BungeeActor<?> actor, CommonPlugin common, ProxyPlugin plugin, @Argument(name = "player") String player) {
        ProxiedPlayer playerA = actor.as(ProxiedPlayer.class).getHandle();
        UUID uniqueId = playerA.getUniqueId();
        UUID uniqueTarget = MojangProfileService.getUniqueId(player);

        common.getDatabaseRef().asOptional().ifPresent(sql -> {
            sql.getPlayer(uniqueId).thenAccept(playerData -> {
                if (sql.getFriendRequest(uniqueId).join().contains(uniqueTarget)) {
                    sql.deleteFriendRequest(uniqueId, uniqueTarget);
                    playerA.sendMessage(ComponentParser.parse(common.getMessageManager().translate(CommandMessages.FRIEND_REQUEST_DELETED, playerData.getLanguage(), Key.of("player", MojangProfileService.getName(uniqueTarget)))));
                } else {
                    playerA.sendMessage(ComponentParser.parse(common.getMessageManager().translate(CommandMessages.FRIEND_REQUEST_NOTSENT, playerData.getLanguage())));
                }
            });
        });
    }

    @Action("remove")
    public void remove(BungeeActor<?> actor, CommonPlugin common, ProxyPlugin plugin, @Argument(name="player") String player) {

    }

    @Action("acceptall")
    public void acceptall(BungeeActor<?> actor, CommonPlugin common, ProxyPlugin plugin) {

    }

    @Action("denyall")
    public void denyall(BungeeActor<?> actor, CommonPlugin common, ProxyPlugin plugin) {

    }

    @Action("accept")
    public void accept(BungeeActor<?> actor, CommonPlugin common, ProxyPlugin plugin, @Argument(name="player") String player) {

    }


    @Action("deny")
    public void deny(BungeeActor<?> actor, CommonPlugin common, ProxyPlugin plugin, @Argument(name="player") String player) {
        ProxiedPlayer playerA = actor.as(ProxiedPlayer.class).getHandle();
        UUID uniqueId = playerA.getUniqueId();
        UUID uniqueTarget = MojangProfileService.getUniqueId(player);

        common.getDatabaseRef().asOptional().ifPresent(sql -> {
            sql.getPlayer(uniqueId).thenAccept(playerData -> {
                ArrayList<FriendRequest> requests = sql.getFriendRequest(uniqueId).join();

                UUID request = null;
                for(int i = 0; i < requests.size(); i++) {
                    if(requests.get(i).getRequestUUID() == uniqueTarget) {
                        request = requests.get(i).getRequestUUID();
                        break;
                    }
                }

                if(request != null){
                    sql.deleteFriendRequest(uniqueId, uniqueTarget);
                    playerA.sendMessage(ComponentParser.parse(common.getMessageManager().translate(CommandMessages.FRIEND_REQUEST_REMOVED, playerData.getLanguage(), Key.of("player", MojangProfileService.getName(uniqueTarget)))));
                    return;
                }

                playerA.sendMessage(ComponentParser.parse(common.getMessageManager().translate(CommandMessages.FRIEND_REQUEST_DENY_NOTSENT, playerData.getLanguage())));
            });
        });
    }

}
