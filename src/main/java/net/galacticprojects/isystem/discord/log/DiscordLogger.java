package net.galacticprojects.isystem.discord.log;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;
import net.galacticprojects.isystem.discord.Bot;

public class DiscordLogger {

    private static JDA jda = Bot.JDA;

    public static void log(LoggerType type, EmbedBuilder message) {
        TextChannel channel = jda.getTextChannelById(type.id);
        channel.sendMessage(message.build()).complete();
    }

}
