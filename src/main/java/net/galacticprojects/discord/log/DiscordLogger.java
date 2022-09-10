package net.galacticprojects.discord.log;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;
import net.galacticprojects.discord.Bot;

public class DiscordLogger {

    private static final JDA jda = Bot.JDA;

    public static void log(LoggerType type, EmbedBuilder message) {
        TextChannel channel = jda.getTextChannelById(type.id);
        if (channel != null) {
            channel.sendMessageEmbeds(message.build()).complete();
        }
    }

}
