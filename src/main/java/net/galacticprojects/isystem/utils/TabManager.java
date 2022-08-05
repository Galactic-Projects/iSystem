package net.galacticprojects.isystem.utils;

import net.galacticprojects.isystem.bungeecord.config.languages.EnglishConfiguration;
import net.galacticprojects.isystem.database.MySQL;
import net.galacticprojects.isystem.database.model.Player;
import net.galacticprojects.isystem.utils.color.Color;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.awt.*;

public class TabManager {

    private final ProxiedPlayer player;
    private MySQL mySQL = JavaInstance.get(MySQL.class);
    private Player playerDB;
    private final String header;
    private final String footer;

    private EnglishConfiguration englishConfiguration;

    public TabManager(ProxiedPlayer player) {
        JavaInstance.put(this);
        this.englishConfiguration = JavaInstance.get(EnglishConfiguration.class);
        this.player = player;
        playerDB = mySQL.getPlayer(player.getUniqueId()).join();
        switch (playerDB.getLanguages()) {
            case ENGLISH: {
                this.header = englishConfiguration.getTablistHeader();
                this.footer = englishConfiguration.getTablistFooter();
                break;
            }
            case GERMAN: {
                this.header = Color.apply("");
                this.footer = Color.apply("");
                break;
            }
            case FRENCH:
            case SPANISH: {
                this.header = Color.apply("&fNot translated yet");
                this.footer = Color.apply("&fNot translated yet");
                break;
            }
            default: {
                this.header = englishConfiguration.getTablistHeader();
                this.footer = englishConfiguration.getTablistFooter();
            }
        }
    }

    public void setTablist() {
        TextComponent head = new TextComponent(this.header);
        TextComponent foot = new TextComponent(this.footer);
        player.setTabHeader(head, foot);
    }

    public void removeTablist() {
        player.resetTabHeader();
    }

    public void updateTablist() {
        setTablist();
    }
}
