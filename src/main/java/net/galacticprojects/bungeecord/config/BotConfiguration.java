package net.galacticprojects.bungeecord.config;

import me.lauriichan.laylib.logger.ISimpleLogger;
import net.galacticprojects.common.config.BaseConfiguration;

import java.io.File;

public class BotConfiguration extends BaseConfiguration {

    private String botToken;
    private String activity;
    private String activityValue;
    private String status;
    private long channelCReport;
    private long channelCBan;
    private long channelCLink;
    private long channelNReport;
    private long channelNBan;
    private long channelNLink;

    private String host;
    private int port;
    private int qport;
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
        channelCReport = (long) config.getValueOrDefault("discord.channels.community.report", Long.parseLong("0"));
        channelCBan = (long) config.getValueOrDefault("discord.channels.community.ban", Long.parseLong("0"));
        channelCLink = (long) config.getValueOrDefault("discord.channels.community.link", Long.parseLong("0"));
        channelNReport = (long) config.getValueOrDefault("discord.channels.network.report", Long.parseLong("0"));
        channelNBan = (long) config.getValueOrDefault("discord.channels.network.ban", Long.parseLong("0"));
        channelNLink = (long) config.getValueOrDefault("discord.channels.network.link", Long.parseLong("0"));

        host = config.getValueOrDefault("teamspeak.host", "localhost");
        port = (int) config.getValueOrDefault("teamspeak.ports.normal", Integer.parseInt("9987"));
        qport = (int) config.getValueOrDefault("teamspeak.ports.query", Integer.parseInt("10011"));
        user = config.getValueOrDefault("teamspeak.user", "serveradmin");
        password = config.getValueOrDefault("teamspeak.password", "your_super_secret_password");
        name = config.getValueOrDefault("teamspeak.name", "Galactic Projects | Bot");
    }

    @Override
    protected void onSave() throws Throwable {
        config.setValue("discord.token", botToken);
        config.setValue("discord.activity.mode", activity);
        config.setValue("discord.activity.value", activityValue);
        config.setValue("discord.channels.community.report", channelCReport);
        config.setValue("discord.channels.community.ban", channelCBan);
        config.setValue("discord.channels.community.link", channelCLink);
        config.setValue("discord.channels.network.report", channelNReport);
        config.setValue("discord.channels.network.ban", channelNBan);
        config.setValue("discord.channels.network.link", channelNLink);
        config.setValue("teamspeak.host", host);
        config.setValue("teamspeak.ports.normal", port);
        config.setValue("teamspeak.ports.query", qport);
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

    public long getChannelCBan() {
        return channelCBan;
    }

    public void setChannelCBan(long channelCBan) {
        this.channelCBan = channelCBan;
    }

    public long getChannelCLink() {
        return channelCLink;
    }

    public void setChannelCLink(long channelCLink) {
        this.channelCLink = channelCLink;
    }

    public long getChannelCReport() {
        return channelCReport;
    }

    public void setChannelCReport(long channelCReport) {
        this.channelCReport = channelCReport;
    }


    public long getChannelNBan() {
        return channelNBan;
    }

    public void setChannelNBan(long channelNBan) {
        this.channelNBan = channelNBan;
    }

    public long getChannelNLink() {
        return channelNLink;
    }

    public void setChannelNLink(long channelNLink) {
        this.channelNLink = channelNLink;
    }

    public long getChannelNReport() {
        return channelNReport;
    }

    public void setChannelNReport(long channelNReport) {
        this.channelNReport = channelNReport;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getHost() {
        return host;
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

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getQport() {
        return qport;
    }

    public void setQport(int qport) {
        this.qport = qport;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
