package net.galacticprojects.common.util;

import de.dytanic.cloudnet.ext.bridge.event.BridgeProxyPlayerLoginRequestEvent;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import net.galacticprojects.bungeecord.ProxyPlugin;
import net.galacticprojects.bungeecord.config.BotConfiguration;
import net.galacticprojects.common.discord.AudioEcho;

public class DiscordBot {

    private final JDABuilder jdaBuilder;
    private static JDA jda;
    private static String prefix = "+";

    public DiscordBot() {
        BotConfiguration configuration = ProxyPlugin.getInstance().getBotConfiguration();
        jdaBuilder = JDABuilder.createDefault(configuration.getBotToken());
        for(GatewayIntent gatewayIntent : jdaBuilder.build().getGatewayIntents()) {
            jdaBuilder.enableIntents(gatewayIntent);
        }
        jdaBuilder.setActivity(Activity.of(Activity.ActivityType.valueOf(configuration.getActivity()), configuration.getActivityValue()));
        jdaBuilder.setStatus(OnlineStatus.valueOf(configuration.getStatus()));
        jdaBuilder.enableCache(CacheFlag.VOICE_STATE);
        jdaBuilder.setAutoReconnect(true);
        registerEvents();
        jda = jdaBuilder.build();
    }

    public static JDA getJDA() {
        return jda;
    }

    private JDABuilder getJDABuilder() {
        return jdaBuilder;
    }

    public void shutdown() {
        if(jda == null) {
            ProxyPlugin.getInstance().getCommonPlugin().getLogger().error("DISCORD BOT: Is null, no shutdown executed!");
            return;
        }

        jdaBuilder.setAutoReconnect(false);
        jdaBuilder.setStatus(OnlineStatus.OFFLINE);
        jdaBuilder.setEnableShutdownHook(true);
    }

    private void registerEvents() {
        getJDABuilder().addEventListeners(new AudioEcho());
    }
}
