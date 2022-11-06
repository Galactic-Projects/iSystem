package net.galacticprojects.bungeecord.config;

import com.syntaxphoenix.syntaxapi.json.JsonArray;
import com.syntaxphoenix.syntaxapi.json.JsonObject;
import com.syntaxphoenix.syntaxapi.json.JsonValue;
import com.syntaxphoenix.syntaxapi.json.ValueType;
import com.syntaxphoenix.syntaxapi.json.io.JsonParser;
import com.syntaxphoenix.syntaxapi.json.value.JsonNumber;
import me.lauriichan.laylib.logger.ISimpleLogger;
import net.galacticprojects.common.config.BaseConfiguration;
import net.galacticprojects.common.config.impl.json.JsonConfig;
import net.galacticprojects.common.util.DynamicArray;
import org.apache.commons.collections4.map.HashedMap;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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

    private String communityGuild;
    private String networkGuild;

    private ArrayList<String> discordGroups = new ArrayList<>();
    private ArrayList<String> teamspeakGroups = new ArrayList<>();

    DynamicArray<String> infos = new DynamicArray<>();
    DynamicArray<Long> longInfos = new DynamicArray<>();


    public BotConfiguration(ISimpleLogger logger, File dataFolder) {
        super(logger, dataFolder, "bot.json");
    }

    @Override
    protected void onLoad() throws Throwable {
        botToken = config.getValueOrDefault("discord.token", "YOUR.BOT.TOKEN.HERE");
        activity = config.getValueOrDefault("discord.activity.mode", "WATCHING");
        activityValue = config.getValueOrDefault("discord.activity.value", "GALACTIC PROJECTS");
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
        communityGuild = config.getValueOrDefault("verification.discord.guilds.community", "0");
        networkGuild = config.getValueOrDefault("verification.discord.guilds.network", "0");
        teamspeakGroups = config.getValueOrDefault("verification.teamspeak.groups", teamspeakGroups);
        loadInfos();
    }

    private void loadInfos() throws Throwable {
        infos.clear();
        JsonArray array = (JsonArray) config.get("verification.discord.groups");
        if(array == null) {
            config.set("verification.discord.groups", new JsonArray());
            return;
        }
        for(JsonValue<?> value : array) {
            if(value == null || !value.hasType(ValueType.OBJECT)) {
                continue;
            }
            JsonObject object = (JsonObject) value;
            JsonValue<?> rawGroups = object.get("groups");
            JsonValue<?> rawServer = object.get("server");
            String groups = (rawGroups == null ? "N/A" : rawGroups.getValue().toString());
            long server = (rawServer == null ? 0 : ((JsonNumber<?>) rawServer).getValue().longValue());
            infos.add(groups);
            longInfos.add(server);
        }

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
        config.setValue("verification.discord.guilds.community", communityGuild);
        config.setValue("verification.discord.guilds.network", networkGuild);
        config.setValue("verification.teamspeak.groups", teamspeakGroups);
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

    public String getCommunityGuild() {
        return communityGuild;
    }

    public void setCommunityGuild(String communityGuild) {
        this.communityGuild = communityGuild;
    }

    public String getNetworkGuild() {
        return networkGuild;
    }

    public void setNetworkGuild(String networkGuild) {
        this.networkGuild = networkGuild;
    }

    public ArrayList<String> getDiscordGroups() {
        for(int i = 0; i < infos.length(); i++) {
            discordGroups.add(infos.get(i));
        }
        return discordGroups;
    }

    public ArrayList<Long> getServer() {
        ArrayList<Long> servers = new ArrayList<>();
        for(int i = 0; i < longInfos.length(); i++) {
            servers.add(longInfos.get(i));
        }
        return servers;
    }

    public void setLongInfos(ArrayList<Long> servers) {
        longInfos.clear();
        for(int i = 0; i < servers.size(); i++) {
            longInfos.add(servers.get(i));
        }
        save();
    }

    public void setDiscordGroups(ArrayList<String> discordGroups) {
        this.discordGroups = discordGroups;
        save();
    }

    public ArrayList<String> getTeamspeakGroups() {
        return teamspeakGroups;
    }

    public void setTeamspeakGroups(ArrayList<String> teamspeakGroups) {
        this.teamspeakGroups = teamspeakGroups;
    }
}
