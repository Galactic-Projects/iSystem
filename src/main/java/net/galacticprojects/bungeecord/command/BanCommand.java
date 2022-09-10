package net.galacticprojects.bungeecord.command;

import com.syntaxphoenix.syntaxapi.utils.java.lang.StringBuilder;
import net.galacticprojects.bungeecord.command.ban.BanType;
import net.galacticprojects.bungeecord.config.MainConfiguration;
import net.galacticprojects.database.MySQL;
import net.galacticprojects.bungeecord.config.languages.EnglishConfiguration;
import net.galacticprojects.utils.JavaInstance;
import net.galacticprojects.utils.color.Color;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.time.OffsetDateTime;

public class BanCommand extends Command implements TabExecutor {

    public BanCommand() {
        super("ban", "");
    }

    String staff;

    MySQL mySQL = JavaInstance.get(MySQL.class);
    public EnglishConfiguration englishConfiguration = JavaInstance.get(EnglishConfiguration.class);
    public MainConfiguration mainConfiguration = JavaInstance.get(MainConfiguration.class);

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (!(sender.hasPermission("*"))) {
            if (sender instanceof ProxiedPlayer) {
                switch (mySQL.getPlayer(((ProxiedPlayer) sender).getUniqueId()).join().getLanguages()) {
                    case ENGLISH -> {
                        sender.sendMessage(new TextComponent(englishConfiguration.getErrorsPermission()));
                    }
                    case GERMAN -> {

                    }
                    case FRENCH, SPANISH -> {

                    }
                }
                return;
            }
            sender.sendMessage(new TextComponent(englishConfiguration.getErrorsPermission()));
            return;
        }

        if (args.length < 3) {
            StringBuilder builder = new StringBuilder();
            builder.append(mainConfiguration.getBanPrefix()).append(" &6&lInformation \n");
            builder.append("&e/ban (player) (id) \n");
            builder.append("&e/ban (player) (custom message) \n");
            builder.append("&aOur ids: \n");
            builder.append("");
            sender.sendMessage("");
        }

        switch (args[1].toLowerCase()) {
            case "": {

            }
        }

    }

    public void banPlayer(ProxiedPlayer player, CommandSender sender, String staff, String reason, String server, BanType type, int duration) {

        switch (type.name().toLowerCase()) {
            case "nban": {
                if (duration == 0 || duration == -1) {
                    OffsetDateTime time = OffsetDateTime.now().plusYears(100);
                    String ip = player.getAddress().toString().replace(":" + player.getAddress().getPort(), "");
                    mySQL.createBan(player.getUniqueId(), staff, ip, reason, server, type, time, OffsetDateTime.now(), -1);
                    StringBuilder builder = new StringBuilder();
                    builder.append("");
                    player.disconnect(new TextComponent(Color.apply(builder.toStringClear())));
                    sender.sendMessage(new TextComponent(""));
                    return;
                }

                OffsetDateTime time = OffsetDateTime.now().plusDays(duration);
                String ip = player.getAddress().toString().replace(":" + player.getAddress().getPort(), "");
                mySQL.createBan(player.getUniqueId(), staff, ip, reason, server, type, time, OffsetDateTime.now(), duration);
                StringBuilder builder = new StringBuilder();
                builder.append("");
                player.disconnect(new TextComponent(Color.apply(builder.toStringClear())));
                sender.sendMessage(new TextComponent(""));
                break;
            }
            case "sban": {
                if (duration == 0 || duration == -1) {
                    OffsetDateTime time = OffsetDateTime.now().plusYears(100);
                    String ip = player.getAddress().toString().replace(":" + player.getAddress().getPort(), "");
                    mySQL.createBan(player.getUniqueId(), staff, ip, reason, server, type, time, OffsetDateTime.now(), -1);
                    StringBuilder builder = new StringBuilder();
                    builder.append("");
                    player.disconnect(new TextComponent(Color.apply(builder.toStringClear())));
                    sender.sendMessage(new TextComponent(""));
                    return;
                }

                OffsetDateTime time = OffsetDateTime.now().plusDays(duration);
                String ip = player.getAddress().toString().replace(":" + player.getAddress().getPort(), "");
                mySQL.createBan(player.getUniqueId(), staff, ip, reason, server, type, time, OffsetDateTime.now(), duration);
                StringBuilder builder = new StringBuilder();
                builder.append("");
                player.disconnect(new TextComponent(Color.apply(builder.toStringClear())));
                sender.sendMessage(new TextComponent(""));
                break;
            }
            case "nmute": {

                if (duration == 0 || duration == -1) {
                    OffsetDateTime time = OffsetDateTime.now().plusYears(100);
                    String ip = player.getAddress().toString().replace(":" + player.getAddress().getPort(), "");
                    mySQL.createBan(player.getUniqueId(), staff, ip, reason, server, type, time, OffsetDateTime.now(), -1);
                    StringBuilder builder = new StringBuilder();
                    builder.append("");
                    player.sendMessage(new TextComponent(Color.apply(builder.toStringClear())));
                    sender.sendMessage(new TextComponent(""));
                    return;
                }

                OffsetDateTime time = OffsetDateTime.now().plusDays(duration);
                String ip = player.getAddress().toString().replace(":" + player.getAddress().getPort(), "");
                mySQL.createBan(player.getUniqueId(), staff, ip, reason, server, type, time, OffsetDateTime.now(), duration);
                StringBuilder builder = new StringBuilder();
                builder.append("");
                player.sendMessage(new TextComponent(Color.apply(builder.toStringClear())));
                sender.sendMessage(new TextComponent(""));
                break;
            }
            case "smute": {

                if (duration == 0 || duration == -1) {
                    OffsetDateTime time = OffsetDateTime.now().plusYears(100);
                    String ip = player.getAddress().toString().replace(":" + player.getAddress().getPort(), "");
                    mySQL.createBan(player.getUniqueId(), staff, ip, reason, server, type, time, OffsetDateTime.now(), -1);
                    StringBuilder builder = new StringBuilder();
                    builder.append("");
                    player.sendMessage(new TextComponent(Color.apply(builder.toStringClear())));
                    sender.sendMessage(new TextComponent(""));
                    return;
                }

                OffsetDateTime time = OffsetDateTime.now().plusDays(duration);
                String ip = player.getAddress().toString().replace(":" + player.getAddress().getPort(), "");
                mySQL.createBan(player.getUniqueId(), staff, ip, reason, server, type, time, OffsetDateTime.now(), duration);
                StringBuilder builder = new StringBuilder();
                builder.append("");
                player.sendMessage(new TextComponent(Color.apply(builder.toStringClear())));
                sender.sendMessage(new TextComponent(""));
            }
            break;
        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        return null;
    }
}