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
import java.util.*;

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

    private DynamicArray<String> infos = new DynamicArray<>();

    private DynamicArray<String> groupArray = new DynamicArray<>();
    private DynamicArray<Long> longInfos = new DynamicArray<>();

    private HashMap<String, String> groupHash = new HashMap<>();

    private HashMap<Long, String> serverHash = new HashMap<>();
    private final DynamicArray<Integer> teamspeakInfo = new DynamicArray<>();
    private final DynamicArray<Integer> teamspeakGroupArray = new DynamicArray<>();
    private final HashMap<String, Integer> teamspeakGroupHash = new HashMap<>();

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
        loadDiscordInfo();
        loadTeamSpeakInfo();
    }

    private void loadDiscordInfo() throws Throwable {
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
            JsonValue<?> rawGroup = object.get("group");
            JsonValue<?> rawId = object.get("id");
            JsonValue<?> rawServerName = object.get("server-name");
            JsonValue<?> rawServer = object.get("server");
            String id = (rawId == null ? "N/A" : rawId.getValue().toString());
            String group = (rawGroup == null ? "N/A" : rawGroup.getValue().toString());
            String serverName = (rawServerName == null ? "N/A" : rawServer.getValue().toString());
            long server = (rawServer == null ? 0 : ((JsonNumber<?>) rawServer).getValue().longValue());
            infos.add(id);
            groupArray.add(group);
            groupHash.put(id, group);
            serverHash.put(server, serverName);
            longInfos.add(server);
        }
    }

    private void loadTeamSpeakInfo() throws Throwable {
        teamspeakInfo.clear();
        JsonArray array = (JsonArray) config.get("verification.teamspeak.groups");
        if(array == null) {
            config.set("verification.teamspeak.groups", new JsonArray());
            return;
        }
        for(JsonValue<?> value : array) {
            if(value == null || !value.hasType(ValueType.OBJECT)) {
                continue;
            }
            JsonObject object = (JsonObject) value;
            JsonValue<?> rawId = object.get("id");
            JsonValue<?> rawPermission = object.get("permissions");
            int id = (rawId == null ? 0 : ((JsonNumber<?>) rawId).getValue().intValue());
            String permission = (rawPermission == null ? "N/A" : rawPermission.getValue().toString());
            teamspeakInfo.add(id);
            teamspeakGroupArray.add(id);
            teamspeakGroupHash.put(permission, id);
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
        ArrayList<String> groups = new ArrayList<>();
        for(int i = 0; i < infos.length(); i++) {
            groups.add(infos.get(i));
        }
        return groups;
    }

    public String getDiscordGroupName(String id) {
        return groupHash.get(id);
    }

    public String getServerName(long serverId) {
        return serverHash.get(serverId);
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

    public ArrayList<Integer> getTeamSpeakGroups() {
        ArrayList<Integer> groups = new ArrayList<>();
        for(int i = 0; i != teamspeakGroupArray.length(); i++) {
            groups.add(teamspeakGroupArray.get(i));
        }
        return groups;
    }

    public Integer getTeamSpeakGroupByPermission(String permission) {
        return teamspeakGroupHash.get(permission);
    }

    public HashMap<String, Integer> getTeamspeakGroupHash() {
        return teamspeakGroupHash;
    }

    public DynamicArray<Integer> getTeamspeakInfo() {
        return teamspeakInfo;
    }

    public void setTeamspeakGroupHash(ArrayList<Integer> service) {
        teamspeakInfo.clear();
        for(int i = 0; i < service.size(); i++) {
            teamspeakInfo.add(service.get(i));
        }
        save();
    }

}
