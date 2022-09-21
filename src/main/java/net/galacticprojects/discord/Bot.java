package net.galacticprojects.discord;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.galacticprojects.bungeecord.config.DiscordConfiguration;
import net.galacticprojects.discord.listeners.AutoModListener;
import net.galacticprojects.discord.listeners.CommandListener;
import net.galacticprojects.log.LogType;
import net.galacticprojects.log.Logger;

import javax.security.auth.login.LoginException;

public class Bot {

    public static JDABuilder BUILDER;
    public static net.dv8tion.jda.api.JDA JDA;
    public DiscordConfiguration configuration;

    public static long WARNID;

    public Bot() {
        configuration = JavaInstance.get(DiscordConfiguration.class);
        JavaInstance.put(this);
        if (configuration.getToken() != null) {
            Logger.log(LogType.ACTION, "DiscordBot", "Starting...");
            JDABuilder builder = JDABuilder.createDefault(configuration.getToken());
                Logger.log(LogType.ACTION, "DiscordBot", "Started! Trying to update Activity and Status");
                builder.setActivity(Activity.of(configuration.getActivity(), configuration.getActivityName()));
                builder.setStatus(configuration.getStatus());
                for(GatewayIntent intent : GatewayIntent.values()) {
                    builder.enableIntents(intent);
                }
                builder.addEventListeners(new CommandListener());
                builder.addEventListeners(new AutoModListener());
                try {
                    JDA = builder.build();
                } catch(LoginException e) {
                    e.printStackTrace();
                }
            return;
        }
        Logger.log(LogType.WARNING, "DiscordBot", "No Token was found!");
    }

    public void shutdown() {
            Logger.log(LogType.ACTION, "DiscordBot", "Stopping...");
            BUILDER.setStatus(OnlineStatus.OFFLINE);
            JDA.shutdown();
            Logger.log(LogType.ACTION, "DiscordBot", "Updated Status and disconnected");
    }
}
