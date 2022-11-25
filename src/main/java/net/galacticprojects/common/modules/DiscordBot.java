package net.galacticprojects.common.modules;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.Compression;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import net.galacticprojects.bungeecord.ProxyPlugin;
import net.galacticprojects.bungeecord.config.BotConfiguration;
import net.galacticprojects.common.modules.discord.VerifyListener;

public class DiscordBot {

    private final JDABuilder jdaBuilder;
    private static JDA jda;
    private static String prefix = "+";

    public DiscordBot() {
        try {
            BotConfiguration configuration = ProxyPlugin.getInstance().getBotConfiguration();
            jdaBuilder = JDABuilder.createDefault(configuration.getBotToken());
            jdaBuilder.enableIntents(GatewayIntent.DIRECT_MESSAGES, GatewayIntent.GUILD_MEMBERS, GatewayIntent.DIRECT_MESSAGE_REACTIONS, GatewayIntent.DIRECT_MESSAGES, GatewayIntent.GUILD_EMOJIS_AND_STICKERS, GatewayIntent.GUILD_MESSAGE_TYPING, GatewayIntent.GUILD_PRESENCES);
            jdaBuilder.disableCache(CacheFlag.MEMBER_OVERRIDES, CacheFlag.VOICE_STATE);
            jdaBuilder.setBulkDeleteSplittingEnabled(false);
            jdaBuilder.setCompression(Compression.NONE);
            jdaBuilder.setActivity(Activity.of(Activity.ActivityType.valueOf(configuration.getActivity()), configuration.getActivityValue()));
            jdaBuilder.setStatus(OnlineStatus.valueOf(configuration.getStatus()));
            jdaBuilder.addEventListeners(new VerifyListener());
            jdaBuilder.setAutoReconnect(true);
            jda = jdaBuilder.build();

            jda.awaitReady();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public JDA getJDA() {
        return jda;
    }

    public void shutdown() {
        if(jda == null) {
            ProxyPlugin.getInstance().getCommonPlugin().getLogger().error("DISCORD BOT: Is null, no shutdown executed!");
            return;
        }

        jdaBuilder.setAutoReconnect(false);
        jdaBuilder.setStatus(OnlineStatus.OFFLINE);
        jdaBuilder.setEnableShutdownHook(true);
        jda.shutdown();
    }

}
