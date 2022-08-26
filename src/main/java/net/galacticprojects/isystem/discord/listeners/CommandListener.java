package net.galacticprojects.isystem.discord.listeners;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.galacticprojects.isystem.discord.Bot;
import net.galacticprojects.isystem.discord.logger.DiscordLogger;
import net.galacticprojects.isystem.discord.logger.LoggerType;
import net.galacticprojects.isystem.logger.LogType;
import net.galacticprojects.isystem.logger.Logger;

public class CommandListener extends ListenerAdapter {

    JDA jda = Bot.JDA;

    public void onMessageReceive(MessageReceivedEvent event) {

        String message = event.getMessage().getContentRaw();

        if(!message.startsWith("=")) {
            return;
        }

        String command = message.replace("=", "").split(" ")[0].toLowerCase();

        Member member = event.getMember();

        if(command.equalsIgnoreCase("warn")) {
            if (!member.getRoles().contains(jda.getRoleById(977326642693300274L))) {
                Logger.log(LogType.PLAYER, "DiscordBot", member.getAsMention() + " tried to warn a user without the permissions!");
                return;
            }
            String[] args = event.getMessage().getContentRaw().replace(command, "").replace("=", "").split(" ");
            User target = jda.getUserById(Long.parseLong(args[0]));
            if (target == null) {
                event.getChannel().sendMessage(args[0] + " is null").queue();
                return;
            }
            Logger.log(LogType.WARNING, "DiscordBot", member.getAsMention() + " warned " + target.getAsTag());
            event.getChannel().sendMessage("Der Spieler " + target.getName() + " wurde erfolgreich verwarnt!");
            EmbedBuilder builder = new EmbedBuilder();
            builder.addField("Created by ", member.getAsMention(), true);
            builder.addField("Warned user ", target.getAsTag(), true);
            builder.addField("reason ", args[1].replaceAll("'", ""), true);
            DiscordLogger.log(LoggerType.DISCORD_WARN, builder);
            PrivateChannel channel = target.openPrivateChannel().complete();
            channel.sendMessage("You You have been warned!\n Reason: " + args[1].replaceAll("'", "")).queue();
            return;
        }
    }

}
