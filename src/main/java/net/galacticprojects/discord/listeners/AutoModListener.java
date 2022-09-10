package net.galacticprojects.discord.listeners;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import net.galacticprojects.bungeecord.config.AutoModConfiguration;
import net.galacticprojects.database.MySQL;
import net.galacticprojects.discord.log.DiscordLogger;
import net.galacticprojects.discord.log.LoggerType;
import net.galacticprojects.log.LogType;
import net.galacticprojects.log.Logger;
import net.galacticprojects.utils.JavaInstance;

import java.util.concurrent.TimeUnit;

public class AutoModListener extends ListenerAdapter {

    AutoModConfiguration configuration = JavaInstance.get(AutoModConfiguration.class);
    MySQL sql = JavaInstance.get(MySQL.class);

    @SubscribeEvent
    public void onGuildMessageReceived(MessageReceivedEvent event) {
        User user = event.getAuthor();
        Member member = event.getMember();
        String message = event.getMessage().getContentRaw().toLowerCase();

        for(String blacklisted : configuration.getBlacklistedWords5()) {
            if (message.contains(blacklisted.toLowerCase())) {
                event.getMessage().delete().reason("Watch your words!").queue();
                event.getGuild().timeoutFor(user, 7, TimeUnit.DAYS).reason("Blacklisted 5").queue();
                // 3 warns
                return;
            }
        }

        for(String blacklisted : configuration.getBlacklistedWords4()) {
            if (message.contains(blacklisted.toLowerCase())) {
                event.getMessage().delete().reason("Watch your words!").queue();
                event.getGuild().timeoutFor(user, 3, TimeUnit.DAYS).reason("Blacklisted 4").queue();
                //2 warns
                return;
            }
        }

        for(String blacklisted : configuration.getBlacklistedWords3()) {
            if (message.contains(blacklisted.toLowerCase())) {
                event.getMessage().delete().reason("Watch your words!").queue();
                event.getGuild().timeoutFor(user, 1, TimeUnit.DAYS).reason("Blacklisted 3").queue();
                //warn
                return;
            }
        }

        for(String blacklisted : configuration.getBlacklistedWords2()) {
            if (message.contains(blacklisted.toLowerCase())) {
                event.getMessage().delete().reason("Watch your words!").queue();
                event.getGuild().timeoutFor(user, 2, TimeUnit.HOURS).reason("Blacklisted 1").queue();
                EmbedBuilder builder = new EmbedBuilder();
                builder.addField("Created by ", "AutoMod", true);
                builder.addField("Warned user ", user.getAsTag(), true);
                builder.addField("Reason ", "Offend rule on blacklisted word 2", true);
                builder.addField("Message", message, true);
                DiscordLogger.log(LoggerType.DISCORD_WARN, builder);
                Logger.log(LogType.PLAYER, "DiscordBot", "Timeouted player " + user.getAsTag() + " for offend rule on blacklisted word 2, he wrote: " + message);
                //warn
                return;
            }
        }

        for(String blacklisted : configuration.getBlacklistedWords1()) {
            if (message.contains(blacklisted.toLowerCase())) {
                event.getMessage().delete().reason("Watch your words!").queue();
                EmbedBuilder builder = new EmbedBuilder();
                builder.addField("Created by ", "AutoMod", true);
                builder.addField("Warned user ", user.getAsTag(), true);
                builder.addField("Reason ", "Offend rule on blacklisted word 1", true);
                builder.addField("Message", message, true);
                DiscordLogger.log(LoggerType.DISCORD_WARN, builder);
                Logger.log(LogType.PLAYER, "DiscordBot", "Deleted message from player " + user.getAsTag() + " for offend rule on blacklisted word 1, he wrote: " + message);
                //warn
            }
        }
    }

}
