package net.galacticprojects.bungeecord.config;

import me.lauriichan.wildcard.systemcore.util.JavaInstance;
import net.galacticprojects.bungeecord.iProxy;
import net.galacticprojects.common.util.color.Color;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class MainConfiguration {

    public File config;
    public Configuration configuration;

    private int maxPlayers;
    private boolean maintenanceEnabled;
    private String systemPrefix;
    private String chatPrefix;
    private String menuPrefix;
    private String onlineTimePrefix;
    private String maintenancePrefix;
    private String historyPrefix;
    private String friendsPrefix;
    private String partyPrefix;
    private String clanPrefix;
    private String teamchatPrefix;
    private String reportPrefix;
    private String banPrefix;
    private String chatlogPrefix;
    private String linkPrefix;


    public MainConfiguration() throws IOException {
        JavaInstance.put(this);

        try {
            config = new File(iProxy.getPluginPath(), "config.yml");

            if (!(config.exists())) {
                config.createNewFile();
            }

            configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(config);

            if (!(configuration.contains("System.MaxPlayers"))) {
                configuration.set("System.MaxPlayers", 50);
            }

            if (!(configuration.contains("System.Maintenance.Enabled"))) {
                configuration.set("System.Maintenance.Enabled", false);
            }

            if (!(configuration.contains("Messages.Prefix.System"))) {
                configuration.set("Messages.Prefix.System", "&8「 &4&lSYSTEM &8」&r");
            }

            if (!(configuration.contains("Messages.Prefix.Chat"))) {
                configuration.set("Messages.Prefix.Chat", "&8「 &5&lG&d&lP &8」&r");
            }

            if (!(configuration.contains("Messages.Prefix.Menu"))) {
                configuration.set("Messages.Prefix.Menu", "&8「 &5&lGALACTIC&d&lPROJECTS &8」&r");
            }

            if (!(configuration.contains("Messages.Prefix.OnlineTime"))) {
                configuration.set("Messages.Prefix.OnlineTime", "&8「 &0&lONLINETIME &8」&r");
            }

            if (!(configuration.contains("Messages.Prefix.Maintenance"))) {
                configuration.set("Messages.Prefix.Maintenance", "&8「 &c&lMAINTENANCE &8 &r");
            }

            if (!(configuration.contains("Messages.Prefix.History"))) {
                configuration.set("Messages.Prefix.History", "&8「 &f&lHISTORY &8」&r");
            }

            if (!(configuration.contains("Messages.Prefix.Friends"))) {
                configuration.set("Messages.Prefix.Friends", "&8「 &9&lFRIENDS &8」&r");
            }

            if (!(configuration.contains("Messages.Prefix.Party"))) {
                configuration.set("Messages.Prefix.Party", "&8「 &b&lPARTY &8 &r");
            }

            if (!(configuration.contains("Messages.Prefix.Clan"))) {
                configuration.set("Messages.Prefix.Clan", "&8「 &3&lCLAN &8」&r");
            }

            if (!(configuration.contains("Messages.Prefix.TeamChat"))) {
                configuration.set("Messages.Prefix.TeamChat", "&8「 &2&lTEAMCHAT &8」&r");
            }

            if (!(configuration.contains("Messages.Prefix.Report"))) {
                configuration.set("Messages.Prefix.Report", "&8「 &1&lREPORT &8」&r");
            }

            if (!(configuration.contains("Messages.Prefix.Ban"))) {
                configuration.set("Messages.Prefix.Ban", "&8「 &6&lBANSYSTEM &8」&r");
            }

            if (!(configuration.contains("Messages.Prefix.ChatLog"))) {
                configuration.set("Messages.Prefix.ChatLog", "&8「 &a&lCHATLOG &8」&r");
            }

            if (!(configuration.contains("Messages.Prefix.Link"))) {
                configuration.set("Messages.Prefix.Link", "&8「 &e&lLINK &8」&r");
            }

            ConfigurationProvider.getProvider(YamlConfiguration.class).save(configuration, config);
            load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void load() throws IOException {
        try {
            configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(config);

            maxPlayers = configuration.getInt("System.MaxPlayers");
            maintenanceEnabled = configuration.getBoolean("System.Maintenance.Enabled");
            systemPrefix = Color.apply(configuration.getString("Messages.Prefix.System"));
            chatPrefix = Color.apply(configuration.getString("Messages.Prefix.Chat"));
            menuPrefix = Color.apply(configuration.getString("Messages.Prefix.Menu"));
            onlineTimePrefix = Color.apply(configuration.getString("Messages.Prefix.OnlineTime"));
            maintenancePrefix = Color.apply(configuration.getString("Messages.Prefix.Maintenance"));
            historyPrefix = Color.apply(configuration.getString("Messages.Prefix.History"));
            friendsPrefix = Color.apply(configuration.getString("Messages.Prefix.Friends"));
            partyPrefix = Color.apply(configuration.getString("Messages.Prefix.Party"));
            clanPrefix = Color.apply(configuration.getString("Messages.Prefix.Clan"));
            teamchatPrefix = Color.apply(configuration.getString("Messages.Prefix.TeamChat"));
            reportPrefix = Color.apply(configuration.getString("Messages.Prefix.Report"));
            banPrefix = Color.apply(configuration.getString("Messages.Prefix.Ban"));
            chatlogPrefix = Color.apply(configuration.getString("Messages.Prefix.ChatLog"));
            linkPrefix = Color.apply(configuration.getString("Messages.Prefix.Link"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void reload() throws IOException {
        try {
            load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public boolean isMaintenanceEnabled() {
        return maintenanceEnabled;
    }
    public String getSystemPrefix() {
        return systemPrefix;
    }

    public String getChatPrefix() {
        return chatPrefix;
    }

    public String getMenuPrefix() {
        return menuPrefix;
    }

    public String getOnlineTimePrefix() {
        return onlineTimePrefix;
    }

    public String getMaintenancePrefix() {
        return maintenancePrefix;
    }

    public String getHistoryPrefix() {
        return historyPrefix;
    }

    public String getFriendsPrefix() {
        return friendsPrefix;
    }

    public String getPartyPrefix() {
        return partyPrefix;
    }

    public String getClanPrefix() {
        return clanPrefix;
    }

    public String getTeamchatPrefix() {
        return teamchatPrefix;
    }

    public String getReportPrefix() {
        return reportPrefix;
    }

    public String getBanPrefix() {
        return banPrefix;
    }

    public String getChatlogPrefix() {
        return chatlogPrefix;
    }

    public String getLinkPrefix() {
        return linkPrefix;
    }
}