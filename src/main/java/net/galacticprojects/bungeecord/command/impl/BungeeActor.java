package net.galacticprojects.bungeecord.command.impl;

import java.sql.SQLData;
import java.util.UUID;

import me.lauriichan.laylib.localization.Key;
import me.lauriichan.laylib.localization.MessageProvider;
import net.galacticprojects.bungeecord.ProxyPlugin;
import net.galacticprojects.common.CommonPlugin;
import net.galacticprojects.common.database.SQLDatabase;
import net.galacticprojects.common.database.model.Player;
import net.galacticprojects.common.util.ComponentParser;

import me.lauriichan.laylib.command.Action;
import me.lauriichan.laylib.command.ActionMessage;
import me.lauriichan.laylib.command.Actor;
import me.lauriichan.laylib.localization.MessageManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

public class BungeeActor<P extends CommandSender> extends Actor<P> {

    public BungeeActor(final P handle, final MessageManager messageManager) {
        super(handle, messageManager);
    }

    @Override
    public UUID getId() {
        Actor<ProxiedPlayer> actor = as(ProxiedPlayer.class);
        if (actor.isValid()) {
            return actor.getHandle().getUniqueId();
        }
        return IMPL_ID;
    }

    @Override
    public String getName() {
        Actor<ProxiedPlayer> actor = as(ProxiedPlayer.class);
        if (actor.isValid()) {
            ProxiedPlayer entity = actor.getHandle();
            return entity.getName();
        }
        return handle.getName();
    }

    public void sendTranslatedMessage(MessageProvider provider, CommonPlugin plugin, Key... placeholders) {
        sendMessage(messageManager.translate(provider, getLanguage(), placeholders));
    }

    public String getTranslatedMessageAsString(String messageId, String language, Key... placeholders) {
        return this.messageManager.translate(messageId, language, placeholders);
    }

    public String getLanguage(CommonPlugin plugin) {
        SQLDatabase database = plugin.getDatabaseRef().get();
        if(database != null) {
        Player player = database.getPlayer(getId()).join();
        return player.getLanguage();
        }
        return "en-uk";
    }

    @Override
    public void sendMessage(String message) {
        handle.sendMessage(ComponentParser.parse(message));
    }

    @Override
    public void sendActionMessage(ActionMessage message) {
        if (message == null) {
            return;
        }
        String content = message.message();
        if (content == null || content.isBlank()) {
            handle.sendMessage(new TextComponent(" "));
            return;
        }
        ClickEvent click = null;
        HoverEvent hover = null;
        if (message.clickAction() != null) {
            Action clickAction = message.clickAction();
            switch (clickAction.getType()) {
                case CLICK_COPY:
                    click = new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, clickAction.getValueAsString());
                    break;
                case CLICK_FILE:
                    click = new ClickEvent(ClickEvent.Action.OPEN_FILE, clickAction.getValueAsString());
                    break;
                case CLICK_RUN:
                    click = new ClickEvent(ClickEvent.Action.RUN_COMMAND, clickAction.getValueAsString());
                    break;
                case CLICK_SUGGEST:
                    click = new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, clickAction.getValueAsString());
                    break;
                case CLICK_URL:
                    click = new ClickEvent(ClickEvent.Action.OPEN_URL, clickAction.getValueAsString());
                    break;
                default:
                    break;
            }
        }
        if (message.hoverAction() != null) {
            Action hoverAction = message.hoverAction();
            switch (hoverAction.getType()) {
                case HOVER_TEXT:
                    hover = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ComponentParser.parse(hoverAction.getValueAsString())));
                    break;
                default:
                    break;
            }
        }
        handle.sendMessage(ComponentParser.parse(message.message(), click, hover));
    }

    @Override
    public boolean hasPermission(String permission) {
        return handle.hasPermission(permission);
    }

}