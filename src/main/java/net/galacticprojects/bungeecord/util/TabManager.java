package net.galacticprojects.bungeecord.util;

import net.galacticprojects.bungeecord.config.MainConfiguration;
import net.galacticprojects.common.databaseLegacy.MySQL;
import net.galacticprojects.common.database.model.Player;
import net.galacticprojects.bungeecord.config.languages.EnglishConfiguration;
import net.galacticprojects.common.util.color.Color;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class TabManager {

    private final ProxiedPlayer player;
    private MySQL mySQL = JavaInstance.get(MySQL.class);
    private Player playerDB;
    private String header;
    private String footer;
    private final String group;
    private final String server;

    private EnglishConfiguration englishConfiguration;
    private MainConfiguration mainConfiguration;

    public TabManager(ProxiedPlayer player, String group, String server) {
        JavaInstance.put(this);
        this.englishConfiguration = JavaInstance.get(EnglishConfiguration.class);
        this.mainConfiguration = JavaInstance.get(MainConfiguration.class);
        this.player = player;
        this.group = group;
        this.server = server;
        playerDB = mySQL.getPlayer(player.getUniqueId()).join();
        if(playerDB == null) {
            this.header = englishConfiguration.getTablistHeader();
            this.footer = englishConfiguration.getTablistFooter();
            return;
        }
        switch (playerDB.getLanguages()) {
            case ENGLISH -> {
                this.header = englishConfiguration.getTablistHeader();
                this.footer = englishConfiguration.getTablistFooter();

            }
            case GERMAN -> {
                this.header = Color.apply("");
                this.footer = Color.apply("");

            }
            case FRENCH, SPANISH -> {
                this.header = Color.apply("&fNot translated yet");
                this.footer = Color.apply("&fNot translated yet");
            }
        }
    }

    public void setTablist() {

        TextComponent head = new TextComponent(this.header.replaceAll("%server%", server).replaceAll("%group%", group).replaceAll("%online%", "" + ProxyServer.getInstance().getPlayers().size()).replaceAll("%maxplayers%", "" + mainConfiguration.getMaxPlayers()));
        TextComponent foot = new TextComponent(this.footer.replaceAll("%server%", server).replaceAll("%group%", group).replaceAll("%online%", "" + ProxyServer.getInstance().getPlayers().size()).replaceAll("%maxplayers%", "" + mainConfiguration.getMaxPlayers()));
        player.setTabHeader(head, foot);
    }

    public void removeTablist() {
        player.resetTabHeader();
    }

    public void updateTablist() {
        setTablist();
    }
}
