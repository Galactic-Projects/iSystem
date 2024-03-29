package net.galacticprojects.common.modules;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3ApiAsync;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import eu.cloudnetservice.driver.permission.Permission;
import eu.cloudnetservice.driver.permission.PermissionUser;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.PrivateChannel;
import net.galacticprojects.bungeecord.ProxyPlugin;
import net.galacticprojects.bungeecord.config.BotConfiguration;
import net.galacticprojects.bungeecord.config.PluginConfiguration;
import net.galacticprojects.bungeecord.listener.ChatListener;
import net.galacticprojects.common.database.SQLDatabase;
import net.galacticprojects.common.database.model.LinkPlayer;
import net.galacticprojects.common.database.model.Player;
import net.galacticprojects.common.modules.discord.EmbedCreator;
import net.galacticprojects.common.util.MojangProfileService;
import net.galacticprojects.common.util.TimeHelper;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.awt.*;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.UUID;
import java.util.prefs.PreferencesFactory;

public class LinkAPI {

    private boolean error = false;
    private String identifier = null;

    public LinkAPI() {}

    public void verifyDiscord(String tag, UUID uniqueId) {
        if (ProxyPlugin.getInstance().getDiscordBot().getJDA() == null) {
            return;
        }
        SQLDatabase database = ProxyPlugin.getInstance().getCommonPlugin().getDatabaseRef().get();

        if(database == null) {
            error = true;
            return;
        }

        LinkPlayer linkPlayer = database.getLinkedPlayer(uniqueId).join();
        if(linkPlayer == null) {
            linkPlayer = database.createLinkPlayer(uniqueId).join();
        }
        linkPlayer.setDiscordLinked(true);
        linkPlayer.setDiscordTag(tag);
        linkPlayer.setDiscordTime(OffsetDateTime.now());
        database.updateLinkPlayer(linkPlayer);

        Player systemPlayer = database.getPlayer(uniqueId).join();
        JDA jda = ProxyPlugin.getInstance().getDiscordBot().getJDA();
        HashMap<String, Long> members = new HashMap<>();
        Guild guild = jda.getGuilds().listIterator().next();

        if(guild == null) {
            error = true;
            return;
        }

        for (Member member : guild.loadMembers().get()) {
            members.put(member.getUser().getAsTag().toLowerCase(), member.getIdLong());
        }

        User user = jda.getUserById(members.get(tag.toLowerCase()));
        PrivateChannel channel = user.openPrivateChannel().complete();
        EmbedBuilder builder = new EmbedBuilder();

        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(uniqueId);
        builder.setColor(Color.green);
        builder.setTitle("Galactic Projects - Verification");
        builder.setThumbnail("https://www.galacticprojects.net/img/0108221659359489510D3E38-29B1-47512x.png");
        builder.addField("Minecraft Player", player.getDisplayName(), true);
        builder.addField("Discord User", tag, true);
        builder.addField("Verified", ":white_check_mark:", true);
        builder.addField("Time", net.galacticprojects.bungeecord.util.TimeHelper.format(systemPlayer.getLanguage()).format(OffsetDateTime.now()), true);
        builder.setFooter("Copyright © | Since 2022 - GalacticProjects.net");
        Message message = channel.sendMessageEmbeds(builder.build()).complete();

    }

    public void verifyTeamSpeak(String ip, PermissionUser permissionUser) {
        TS3Api api = ProxyPlugin.getInstance().getTeamSpeakBot().getAPI();
        TS3ApiAsync apiAsync = ProxyPlugin.getInstance().getTeamSpeakBot().getAsyncAPI();

        if (api == null || apiAsync == null) {
            error = true;
            return;
        }

        HashMap<String, String> teamspeakClients = new HashMap<>();
        int databaseId = -1;

        for (final Client client : apiAsync.getClients().getUninterruptibly()) {
            teamspeakClients.put(client.getIp(), client.getUniqueIdentifier());

            if (!teamspeakClients.containsKey(ip)) {
                error = true;
                return;
            }

            databaseId = client.getDatabaseId();
        }


        for (Permission permission : permissionUser.permissions()) {
            int id = ProxyPlugin.getInstance().getBotConfiguration().getTeamSpeakGroupByPermission(permission.name());

            if(databaseId == -1) {
                error = true;
                return;
            }

            System.out.println("ID" + id + " ClientID: " + databaseId);

            if (id < 9) {
                error = true;
                return;
            }

            api.addClientToServerGroup(id, databaseId);
            api.sendPrivateMessage(databaseId, "BOT >> Successfully verified!");

            error = false;
        }
        teamspeakClients.clear();
        databaseId = -1;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
}
