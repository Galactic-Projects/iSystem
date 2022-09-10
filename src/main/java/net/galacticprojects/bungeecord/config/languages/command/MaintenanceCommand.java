package net.galacticprojects.bungeecord.config.languages.command;

import net.galacticprojects.bungeecord.config.MainConfiguration;
import net.galacticprojects.bungeecord.config.languages.EnglishConfiguration;
import net.galacticprojects.database.MySQL;
import net.galacticprojects.utils.JavaInstance;
import net.galacticprojects.utils.TimeHelper;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class MaintenanceCommand extends Command implements TabExecutor {

    public MySQL mySQL = JavaInstance.get(MySQL.class);
    public MainConfiguration mainConfiguration = JavaInstance.get(MainConfiguration.class);
    public EnglishConfiguration englishConfiguration = JavaInstance.get(EnglishConfiguration.class);

    public MaintenanceCommand() {
        super("maintenance", "*");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (sender instanceof ProxiedPlayer) {
            switch (mySQL.getPlayer(((ProxiedPlayer) sender).getUniqueId()).join().getLanguages()) {
                case GERMAN, SPANISH -> {

                }
                case ENGLISH -> {
                    if (!(sender.hasPermission("*"))) {
                        sender.sendMessage(new TextComponent(englishConfiguration.getErrorsPermission()));
                        return;
                    }

                    if (args.length == 0) {
                        sender.sendMessage(new TextComponent(englishConfiguration.getMaintenaceUsage()));
                    } else if (args.length == 1) {
                        switch (args[0].toLowerCase()) {
                            case "off" -> {
                                if (mainConfiguration.isMaintenanceEnabled()) {
                                    try {
                                        mainConfiguration.configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(mainConfiguration.config);
                                        mainConfiguration.configuration.set("System.Maintenance.Enabled", false);
                                        ConfigurationProvider.getProvider(YamlConfiguration.class).save(mainConfiguration.configuration, mainConfiguration.config);
                                        mainConfiguration.reload();

                                        sender.sendMessage(new TextComponent(englishConfiguration.getMaintenanceSuccessfulTurnedOff()));
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    sender.sendMessage(new TextComponent(englishConfiguration.getMaintenanceErrorsAlreadyOff()));
                                }
                            }
                            case "on" -> {
                                sender.sendMessage(new TextComponent(englishConfiguration.getMaintenaceUsage()));
                            }
                        }
                    } else if (args.length == 2) {
                        switch (args[1].toLowerCase()) {
                            case "off", "on" -> {
                                sender.sendMessage(new TextComponent(englishConfiguration.getMaintenaceUsage()));
                            }
                        }
                    } else {
                        String time = TimeHelper.BAN_TIME_FORMATTER.format(OffsetDateTime.now().plusHours(Long.parseLong(args[1])));
                        String message = "";
                        for (int i = 2; i != args.length; i++) {
                            if (i == 2) {
                                message = args[i];
                            } else {
                                message = message + " " + args[i];
                            }
                        }

                        if (!(mainConfiguration.isMaintenanceEnabled())) {
                            try {
                                mainConfiguration.configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(mainConfiguration.config);
                                mainConfiguration.configuration.set("System.Maintenance.Enabled", true);
                                ConfigurationProvider.getProvider(YamlConfiguration.class).save(mainConfiguration.configuration, mainConfiguration.config);
                                englishConfiguration.englishConfiguration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(englishConfiguration.english);
                                englishConfiguration.englishConfiguration.set("Messages.System.Maintenance.Reason", message);
                                englishConfiguration.englishConfiguration.set("Messages.System.Maintenance.EndDate", time);
                                ConfigurationProvider.getProvider(YamlConfiguration.class).save(englishConfiguration.englishConfiguration, englishConfiguration.english);
                                englishConfiguration.reload();
                                mainConfiguration.reload();

                                sender.sendMessage(new TextComponent(englishConfiguration.getMaintenanceSuccessfulTurnedOn().replaceAll("%reason%", message).replaceAll("%enddate%", time)));

                                for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                                    if (!(all.hasPermission("*"))) {
                                        all.disconnect(new TextComponent(englishConfiguration.getKickMaintenanceNow().replaceAll("%reason%", message).replaceAll("%enddate%", time)));
                                    }
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            sender.sendMessage(new TextComponent(englishConfiguration.getMaintenanceErrorsAlreadyOn()));
                        }
                    }
                }
                case FRENCH -> {
                }
            }
        } else {
            if (!(sender.hasPermission("*"))) {
                sender.sendMessage(new TextComponent(englishConfiguration.getErrorsPermission()));
                return;
            }

            if (args.length == 0) {
                sender.sendMessage(new TextComponent(englishConfiguration.getMaintenaceUsage()));
            } else if (args.length == 1) {
                switch (args[0].toLowerCase()) {
                    case "off" -> {
                        if (mainConfiguration.isMaintenanceEnabled()) {
                            try {
                                mainConfiguration.configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(mainConfiguration.config);
                                mainConfiguration.configuration.set("System.Maintenance.Enabled", false);
                                ConfigurationProvider.getProvider(YamlConfiguration.class).save(mainConfiguration.configuration, mainConfiguration.config);
                                mainConfiguration.reload();

                                sender.sendMessage(new TextComponent(englishConfiguration.getMaintenanceSuccessfulTurnedOff()));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            sender.sendMessage(new TextComponent(englishConfiguration.getMaintenanceErrorsAlreadyOff()));
                        }
                    }
                    case "on" -> {
                        sender.sendMessage(new TextComponent(englishConfiguration.getMaintenaceUsage()));
                    }
                }
            } else if (args.length == 2) {
                switch (args[1].toLowerCase()) {
                    case "off", "on" -> {
                        sender.sendMessage(new TextComponent(englishConfiguration.getMaintenaceUsage()));
                    }
                }
            } else {
                String time = TimeHelper.BAN_TIME_FORMATTER.format(OffsetDateTime.now().plusHours(Long.parseLong(args[1])));
                String message = "";
                for (int i = 2; i != args.length; i++) {
                    if (i == 2) {
                        message = args[i];
                    } else {
                        message = message + " " + args[i];
                    }
                }

                if (!(mainConfiguration.isMaintenanceEnabled())) {
                    try {
                        mainConfiguration.configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(mainConfiguration.config);
                        mainConfiguration.configuration.set("System.Maintenance.Enabled", true);
                        ConfigurationProvider.getProvider(YamlConfiguration.class).save(mainConfiguration.configuration, mainConfiguration.config);
                        englishConfiguration.englishConfiguration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(englishConfiguration.english);
                        englishConfiguration.englishConfiguration.set("Messages.System.Maintenance.Reason", message);
                        englishConfiguration.englishConfiguration.set("Messages.System.Maintenance.EndDate", time);
                        ConfigurationProvider.getProvider(YamlConfiguration.class).save(englishConfiguration.englishConfiguration, englishConfiguration.english);
                        englishConfiguration.reload();
                        mainConfiguration.reload();

                        sender.sendMessage(new TextComponent(englishConfiguration.getMaintenanceSuccessfulTurnedOn().replaceAll("%reason%", message).replaceAll("%enddate%", time)));

                        for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                            if (!(all.hasPermission("*"))) {
                                all.disconnect(new TextComponent(englishConfiguration.getKickMaintenanceNow().replaceAll("%reason%", message).replaceAll("%enddate%", time)));
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    sender.sendMessage(new TextComponent(englishConfiguration.getMaintenanceErrorsAlreadyOn()));
                }
            }
        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            List<String> enable = new ArrayList<>();
            enable.add("on");
            enable.add("off");
            return enable;
        } else if (args.length == 2) {
            List<String> time = new ArrayList<>();
            int i = 1;
            i++;
            time.add(String.valueOf(i));
            return time;
        } else if(args.length >= 3) {
            List<String> players = new ArrayList<>();
            for(ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                players.add(all.getName());
            }
            return players;
        }
        return null;
    }
}
