package net.galacticprojects.bungeecord.command;

import com.syntaxphoenix.syntaxapi.utils.java.lang.StringBuilder;
import net.galacticprojects.bungeecord.config.MainConfiguration;
import net.galacticprojects.bungeecord.config.languages.EnglishConfiguration;
import net.galacticprojects.database.MySQL;
import net.galacticprojects.utils.JavaInstance;
import net.galacticprojects.utils.color.Color;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SystemCommand extends Command implements TabExecutor {

    public SystemCommand() {
        super("system", "");
    }

    public MySQL mySQL = JavaInstance.get(MySQL.class);
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
                    case GERMAN, FRENCH, SPANISH -> {

                    }
                }
                return;
            }
            sender.sendMessage(new TextComponent(englishConfiguration.getErrorsPermission()));
            return;
        }

        if (args.length == 0 || args.length >= 3) {
            StringBuilder builder = new StringBuilder();
            builder.append(mainConfiguration.getSystemPrefix()).append(" &6&lInformation \n");
            builder.append("&e/system reload <Config, Permissions, MySQL, Discord, Languages, Blacklisted> \n");
            builder.append("&e/system setMaxPlayers <Number> \n");
            builder.append("&e/system  <Number> \n");
            builder.append("&e/system  <Number> \n");
            builder.append("&e/system  <Number> \n");
            sender.sendMessage(new TextComponent(Color.apply(builder.toStringClear())));
        }

        if(args.length == 1) {
            switch(args[0].toLowerCase()) {
                case "reload" -> {
                    switch(mySQL.getPlayer(((ProxiedPlayer) sender).getUniqueId()).join().getLanguages()) {
                        case ENGLISH -> {

                        }
                        case SPANISH, GERMAN, FRENCH -> {

                        }
                    }
                }
                case "setmaxplayers" -> {
                    switch(mySQL.getPlayer(((ProxiedPlayer) sender).getUniqueId()).join().getLanguages()) {
                        case ENGLISH -> {

                        }
                        case SPANISH, GERMAN, FRENCH -> {

                        }
                    }
                }
            }
        }

        if(args.length == 2) {
            switch(args[0].toLowerCase()) {
                case "reload" -> {
                    switch(mySQL.getPlayer(((ProxiedPlayer) sender).getUniqueId()).join().getLanguages()) {
                        case ENGLISH -> {

                        }
                        case SPANISH, GERMAN, FRENCH -> {

                        }
                    }
                }
                case "setmaxplayers" -> {
                    switch(mySQL.getPlayer(((ProxiedPlayer) sender).getUniqueId()).join().getLanguages()) {
                        case ENGLISH -> {
                            try {
                                int max = Integer.parseInt(args[1]);

                                if (mainConfiguration.getMaxPlayers() == max) {
                                    sender.sendMessage(new TextComponent(englishConfiguration.getAdministratorErrorAlreadyNumber()));
                                    return;
                                }

                                if (ProxyServer.getInstance().getPlayers().size() > max) {
                                    sender.sendMessage(new TextComponent(englishConfiguration.getAdministratorErrorNoLesserThan()));
                                    return;
                                }

                                if (max < 0 && (!(mainConfiguration.isMaintenanceEnabled()))) {
                                    sender.sendMessage(new TextComponent(englishConfiguration.getAdministratorErrorNegativeNumber()));
                                    return;
                                }

                                if (mainConfiguration.isMaintenanceEnabled()) {
                                    sender.sendMessage(new TextComponent(englishConfiguration.getAdministratorErrorMaintenance()));
                                    return;
                                }

                                mainConfiguration.configuration.set("System.MaxPlayers", max);
                                ConfigurationProvider.getProvider(YamlConfiguration.class).save(mainConfiguration.configuration, mainConfiguration.config);
                                mainConfiguration.reload();
                                sender.sendMessage(new TextComponent(englishConfiguration.getAdministratorSuccessSetNumber().replaceAll("%amount%", String.valueOf(max))));
                            } catch (NumberFormatException e) {
                                sender.sendMessage(new TextComponent(englishConfiguration.getAdministratorErrorNoNumber()));
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        case SPANISH, GERMAN, FRENCH -> {

                        }
                    }
                }
            }
        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            List<String> subcommands = new ArrayList<>();
            subcommands.add("setmaxplayers");
            subcommands.add("reload");
            return subcommands;
        } else if (args.length == 2) {
            if(args[0].equalsIgnoreCase("setmaxplayers")) {
                List<String> players = new ArrayList<>();
                players.add(String.valueOf(ProxyServer.getInstance().getPlayers().size()));
                return players;
            } // Config, Permissions, MySQL, Discord, Languages, Blacklisted
            if(args[0].equalsIgnoreCase("reload")) {
                List<String> subcommands = new ArrayList<>();
                subcommands.add("Config");
                subcommands.add("Permissions");
                subcommands.add("MySQL");
                subcommands.add("Discord");
                subcommands.add("Languages");
                subcommands.add("Blacklisted");
                return subcommands;
            }
        }
        return null;
    }
}
