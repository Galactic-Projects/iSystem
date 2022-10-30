package net.galacticprojects.bungeecord.config;

import me.lauriichan.laylib.logger.ISimpleLogger;
import net.galacticprojects.common.config.BaseConfiguration;

import java.io.File;

public class BotConfiguration extends BaseConfiguration {

    private String botToken;
    private String activity;
    private String activityValue;
    private String status;
    private String channelCReport;
    private String channelCBan;
    private String channelCLink;
    private String channelNReport;
    private String channelNBan;
    private String channelNLink;

    private String host;
    private String port;
    private String user;
    private String password;
    private String name;

    public BotConfiguration(ISimpleLogger logger, File dataFolder) {
        super(logger, dataFolder, "bot.json");
    }

    @Override
    protected void onLoad() throws Throwable {

        botToken = config.getValueOrDefault("discord.token", "YOUR.BOT.TOKEN.HERE");
        activity = config.getValueOrDefault("discord.activity.mode", "WATCHING");
        activityValue = config.getValueOrDefault("discord.activity.value",  "GALACTIC PROJECTS");
        status = config.getValueOrDefault("discord.status.value", "ONLINE");
        channelCReport = config.getValueOrDefault("discord.channels.community.report", "0");
        channelCBan = config.getValueOrDefault("discord.channels.community.ban", "0");
        channelCLink = config.getValueOrDefault("discord.channels.community.link", "0");
        channelNReport = config.getValueOrDefault("discord.channels.network.report", "0");
        channelNBan = config.getValueOrDefault("discord.channels.network.ban", "0");
        channelNLink = config.getValueOrDefault("discord.channels.network.link", "0");
        host = config.getValueOrDefault("teamspeak.host", "localhost");
        port = config.getValueOrDefault("teamspeak.port", "9987");
        user = config.getValueOrDefault("teamspeak.user", "serveradmin");
        password = config.getValueOrDefault("teamspeak.password", "your_super_secret_password");
        name = config.getValueOrDefault("teamspeak.name", "Galactic Projects | Bot");
    }

    @Override
    protected void onSave() throws Throwable {
        config.setValue("discord.token", botToken);
        config.setValue("discord.activity.mode", activity);
        config.setValue("discord.activity.value", activityValue);
        config.setValue("discord.status.value", status);
        config.setValue("discord.channels.community.report", channelCReport);
        config.setValue("discord.channels.community.ban", channelCBan);
        config.setValue("discord.channels.community.link", channelCLink);
        config.setValue("discord.channels.network.report", channelNReport);
        config.setValue("discord.channels.network.ban", channelNBan);
        config.setValue("discord.channels.network.link", channelNLink);
        config.setValue("teamspeak.host", host);
        config.setValue("teamspeak.port", port);
        config.setValue("teamspeak.user", user);
        config.setValue("teamspeak.password", password);
        config.setValue("teamspeak.name", name);
    }

    public String getBotToken() {
        return botToken;
    }

    public void setBotToken(String botToken) {
        this.botToken = botToken;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getActivityValue() {
        return activityValue;
    }


    public void setActivityValue(String activityValue) {
        this.activityValue = activityValue;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getChannelCBan() {
        return channelCBan;
    }

    public void setChannelCBan(String channelCBan) {
        this.channelCBan = channelCBan;
    }

    public String getChannelCLink() {
        return channelCLink;
    }

    public void setChannelCLink(String channelCLink) {
        this.channelCLink = channelCLink;
    }

    public String getChannelCReport() {
        return channelCReport;
    }

    public void setChannelCReport(String channelCReport) {
        this.channelCReport = channelCReport;
    }


    public String getChannelNBan() {
        return channelNBan;
    }

    public void setChannelNBan(String channelNBan) {
        this.channelNBan = channelNBan;
    }

    public String getChannelNLink() {
        return channelNLink;
    }

    public void setChannelNLink(String channelNLink) {
        this.channelNLink = channelNLink;
    }

    public String getChannelNReport() {
        return channelNReport;
    }

    public void setChannelNReport(String channelNReport) {
        this.channelNReport = channelNReport;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getHost() {
        return host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
