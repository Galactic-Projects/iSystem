package net.galacticprojects.bungeecord.config;

import me.lauriichan.wildcard.systemcore.util.JavaInstance;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.galacticprojects.bungeecord.iProxy;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;


import java.io.File;
import java.io.IOException;

public class DiscordConfiguration {

    public File dconfig;
    public Configuration discordConfiguration;

    private String token;
    private Activity.ActivityType activity;
    private String activityName;
    private OnlineStatus status;
    private String channelLogLink;
    private String channelLogBan;
    private String channelLogReport;
    private String channelLogDiscordWarn;
    private String channelLogDiscordBan;

    public DiscordConfiguration() throws IOException {
        JavaInstance.put(this);

        try {
            dconfig = new File(iProxy.getPluginPath(), "discord.yml");

            if (!(dconfig.exists())) {
                dconfig.createNewFile();
            }

            discordConfiguration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(dconfig);

            if (!(discordConfiguration.contains("System.Token"))) {
                discordConfiguration.set("System.Token", " ");
            }

            if (!(discordConfiguration.contains("System.Activity"))) {
                discordConfiguration.set("System.Activity", "WATCHING");
            }

            if (!(discordConfiguration.contains("System.ActivityName"))) {
                discordConfiguration.set("System.ActivityName", "PORNHUB PREMIUM");
            }

            if (!(discordConfiguration.contains("System.Status"))) {
                discordConfiguration.set("System.Status", "ONLINE");
            }

            if (!(discordConfiguration.contains("System.Channel.Logs.Link"))) {
                discordConfiguration.set("System.Channel.Logs.Link", "");
            }

            if (!(discordConfiguration.contains("System.Channel.Logs.Ban"))) {
                discordConfiguration.set("System.Channel.Logs.Ban", "");
            }

            if (!(discordConfiguration.contains("System.Channel.Logs.Report"))) {
                discordConfiguration.set("System.Channel.Logs.Report", "");
            }

            if (!(discordConfiguration.contains("System.Channel.Logs.DiscordWarn"))) {
                discordConfiguration.set("System.Channel.Logs.DiscordWarn", "");
            }

            if (!(discordConfiguration.contains("System.Channel.Logs.DiscordBan"))) {
                discordConfiguration.set("System.Channel.Logs.DiscordBan", "");
            }

            ConfigurationProvider.getProvider(YamlConfiguration.class).save(discordConfiguration, dconfig);
            load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void load() throws IOException {
        try {
            discordConfiguration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(dconfig);

            token = discordConfiguration.getString("System.Token");
            activity = Activity.ActivityType.valueOf(discordConfiguration.getString("System.Activity"));
            activityName = discordConfiguration.getString("System.ActivityName");
            status = OnlineStatus.valueOf(discordConfiguration.getString("System.Status"));
            channelLogLink = discordConfiguration.getString("System.Channel.Logs.Link");
            channelLogBan = discordConfiguration.getString("System.Channel.Logs.Ban");
            channelLogReport = discordConfiguration.getString("System.Channel.Logs.Report");
            channelLogDiscordWarn = discordConfiguration.getString("System.Channel.Logs.DiscordWarn");
            channelLogDiscordBan = discordConfiguration.getString("System.Channel.Logs.DiscordBan");
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

    public String getToken() {
        return token;
    }

    public Activity.ActivityType getActivity() {
        return activity;
    }

    public String getActivityName() {
        return activityName;
    }

    public OnlineStatus getStatus() {
        return status;
    }

    public String getChannelLogLink() {
        return channelLogLink;
    }

    public String getChannelLogBan() {
        return channelLogBan;
    }
    public String getChannelLogReport() {
        return channelLogBan;
    }

    public String getChannelLogDiscordWarn() {
        return channelLogDiscordWarn;
    }

    public String getChannelLogDiscordBan() {
        return channelLogDiscordBan;
    }
}