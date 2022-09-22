package net.galacticprojects.bungeecord.entity;

import me.lauriichan.laylib.command.Actor;
import me.lauriichan.laylib.localization.Key;
import net.galacticprojects.bungeecord.ProxyPlugin;
import net.galacticprojects.bungeecord.command.impl.BungeeActor;
import net.galacticprojects.common.database.SQLDatabase;
import net.galacticprojects.common.database.model.Ban;
import net.galacticprojects.common.database.model.Player;
import net.galacticprojects.common.util.MojangProfileService;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.sql.SQLData;
import java.time.OffsetDateTime;
import java.util.UUID;

public class CommandPlayer {

    UUID uniqueId;

    ProxyPlugin plugin;
    String name;

    public CommandPlayer(ProxyPlugin plugin) {
        this.plugin = plugin;
    }

    public CommandPlayer(ProxiedPlayer player) {
        this.uniqueId = player.getUniqueId();
    }

    public CommandPlayer(UUID uniqueId) {
        this.uniqueId = uniqueId;
    }

    public CommandPlayer(Actor<?> sender) {
        this.name = sender.getName();
    }
    public CommandPlayer(String name) {
        this.name = name;
    }

    /**
     * bans the player and kicks him if he is online
     *
     * @return the Ban information
     */
    public Ban banPlayer(final UUID owner, final String reason, final OffsetDateTime time, final OffsetDateTime creationTime) {
        if(uniqueId == null) {
            this.uniqueId = MojangProfileService.getUniqueId(name);
        }
            if(getBan() != null) {
                return null;
            }
            ProxiedPlayer player = ProxyServer.getInstance().getPlayer(uniqueId);
            if(player != null) {
                player.disconnect(new TextComponent(plugin.getCommonPlugin().getMessageManager().translate("generic.player.ban.kick", "en-uk", Key.of("reason", reason))));
            }
            SQLDatabase database = plugin.getCommonPlugin().getDatabaseRef().get();
            return database.banPlayer(uniqueId, owner, reason, time, creationTime).join();
    }

    /**
     * Gets the ban information from the player if it exists
     *
     * @return the Ban information
     */
    public Ban getBan() {
        if(uniqueId == null) {
            this.uniqueId = MojangProfileService.getUniqueId(name);
        }
        SQLDatabase database = plugin.getCommonPlugin().getDatabaseRef().get();
        return database.getBan(uniqueId).join();
    }

    /**
     * Deletes the ban of the player if he is banned
     *
     * @return true or false
     */
    public boolean deleteBan() {
        if(uniqueId == null) {
            this.uniqueId = MojangProfileService.getUniqueId(name);
        }
        if(getBan() == null) {
            return false;
        }
        SQLDatabase database = plugin.getCommonPlugin().getDatabaseRef().get();
        return database.deleteBan(uniqueId).join();
    }

    public String getLanguage() {
        String language = "en-uk";
        if(getPlayerData().getLanguage() != null) {
            language = getPlayerData().getLanguage();
            return language;
        }
        return language;
    }

    public Player getPlayerData() {
        if(uniqueId == null) {
            this.uniqueId = MojangProfileService.getUniqueId(name);
        }
        SQLDatabase database = plugin.getCommonPlugin().getDatabaseRef().get();
        return database.getPlayer(uniqueId).join();
    }

}
