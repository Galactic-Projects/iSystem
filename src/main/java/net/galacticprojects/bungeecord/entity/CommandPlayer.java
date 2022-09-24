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

    ProxyPlugin plugin = ProxyPlugin.getInstance();
    String name;

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
