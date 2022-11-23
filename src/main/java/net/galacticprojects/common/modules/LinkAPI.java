package net.galacticprojects.common.modules;

import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import eu.cloudnetservice.driver.permission.Permission;
import eu.cloudnetservice.driver.permission.PermissionUser;
import net.galacticprojects.bungeecord.ProxyPlugin;
import net.galacticprojects.bungeecord.config.BotConfiguration;
import net.galacticprojects.bungeecord.config.PluginConfiguration;
import net.galacticprojects.bungeecord.listener.ChatListener;
import net.galacticprojects.common.database.SQLDatabase;
import net.galacticprojects.common.database.model.LinkPlayer;
import net.galacticprojects.common.modules.discord.EmbedCreator;
import net.galacticprojects.common.util.MojangProfileService;
import net.galacticprojects.common.util.TimeHelper;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.UUID;
import java.util.prefs.PreferencesFactory;

public class LinkAPI {

    private boolean error = false;
    private String identifier = null;

    public LinkAPI() {}

    public void verifyDiscord(String tag) {
        if (ProxyPlugin.getInstance().getDiscordBot().getJDA() == null) {
            return;
        }


    }

    public void verifyTeamSpeak(String ip, PermissionUser permissionUser) {
        if (ProxyPlugin.getInstance().getTeamSpeakBot().getAPI() == null) {
            error = true;
            return;
        }

        HashMap<String, String> teamspeakClients = new HashMap<>();

        for (final Client client : ProxyPlugin.getInstance().getTeamSpeakBot().getAPI().getClients()) {
            teamspeakClients.put(client.getIp(), client.getUniqueIdentifier());

            if (!teamspeakClients.containsKey(ip)) {
                error = true;
                return;
            }

            for (Permission permission : permissionUser.permissions()) {
                int id = ProxyPlugin.getInstance().getBotConfiguration().getTeamSpeakGroupByPermission(permission.name());
                System.out.println("ID" + id + " ClientID: " + client.getDatabaseId());

                if (id < 9) {
                    error = true;
                    return;
                }

                if (client.isInServerGroup(id)) {
                    error = true;
                    return;
                }

                ProxyPlugin.getInstance().getTeamSpeakBot().getAPI().addClientToServerGroup(id, client.getDatabaseId());
                identifier = client.getUniqueIdentifier();
                teamspeakClients.clear();
                error = false;

            }
        }
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
