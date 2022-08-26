package net.galacticprojects.isystem.discord;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.galacticprojects.isystem.bungeecord.config.DiscordConfiguration;
import net.galacticprojects.isystem.discord.listeners.CommandListener;
import net.galacticprojects.isystem.logger.LogType;
import net.galacticprojects.isystem.logger.Logger;
import net.galacticprojects.isystem.utils.JavaInstance;

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
