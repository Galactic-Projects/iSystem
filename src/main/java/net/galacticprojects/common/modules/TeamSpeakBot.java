package net.galacticprojects.common.modules;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3ApiAsync;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import net.galacticprojects.bungeecord.ProxyPlugin;
import net.galacticprojects.bungeecord.config.BotConfiguration;

public class TeamSpeakBot {

    private final TS3Query ts3Query;
    private final TS3Api api;
    private TS3ApiAsync asyncApi;

    public TeamSpeakBot() {
        BotConfiguration configuration = ProxyPlugin.getInstance().getBotConfiguration();
        TS3Config ts3Config = new TS3Config();
        ts3Config.setHost(configuration.getHost());

        ts3Query = new TS3Query(ts3Config);
        ts3Query.connect();


        api = ts3Query.getApi();
        asyncApi = ts3Query.getAsyncApi();
        api.login(configuration.getUser(), configuration.getPassword());
        api.selectVirtualServerById(1);
        api.setNickname(configuration.getName());
    }

    public void shutdown () {
        if(!(ts3Query.isConnected())) {
            ProxyPlugin.getInstance().getCommonPlugin().getLogger().error("TEAMSPEAK BOT: Is null, no shutdown executed!");
            return;
        }

        api.logout();
        ts3Query.exit();
        asyncApi = null;
    }

    public TS3Api getAPI() {
        return api;
    }

    public TS3ApiAsync getAsyncAPI() {
        return asyncApi;
    }
}
