package net.galacticprojects.isystem.bungeecord.command;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.driver.permission.*;
import net.galacticprojects.isystem.bungeecord.config.languages.EnglishConfiguration;
import net.galacticprojects.isystem.database.MySQL;
import net.galacticprojects.isystem.utils.JavaInstance;
import net.galacticprojects.isystem.utils.MojangProfileService;
import net.galacticprojects.isystem.utils.TimeHelper;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class OnlineTimeCommand extends Command implements TabExecutor {
    public OnlineTimeCommand() {
        super("onlinetime");
    }

    public EnglishConfiguration englishConfiguration = JavaInstance.get(EnglishConfiguration.class);
    public MySQL mySQL = JavaInstance.get(MySQL.class);
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) sender;

            switch (mySQL.getPlayer(player.getUniqueId()).join().getLanguages()) {
                case GERMAN -> {
                }
                case ENGLISH -> {
                    if (args.length == 0) {
                        long milliseconds = mySQL.getPlayer(player.getUniqueId()).join().getOnlineTime();
                        long seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds) - TimeUnit.MILLISECONDS.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds));
                        long minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds) - TimeUnit.MILLISECONDS.toMinutes(TimeUnit.MILLISECONDS.toHours(seconds));
                        long hours = TimeUnit.MILLISECONDS.toHours(milliseconds) - TimeUnit.MILLISECONDS.toHours(TimeUnit.MILLISECONDS.toDays(milliseconds));
                        long days = TimeUnit.MILLISECONDS.toDays(seconds);
                        long month = days / 30 - TimeUnit.MILLISECONDS.toDays(seconds);
                        long year = month / 12 - days;

                        player.sendMessage(new TextComponent(englishConfiguration.getOnlineTimeSuccessSelf().replaceAll("%year%", "" + year).replaceAll("%month%", "" + month).replaceAll("%day%", "" + days).replaceAll("%hour%", "" + hours).replaceAll("%minute%", "" + minutes)));
                    } else if (args.length == 1) {
                        if (args[0].equalsIgnoreCase("on")) {
                            if (!(mySQL.getPlayer(player.getUniqueId()).join().isShowtime())) {
                                mySQL.updateShowTime(player.getUniqueId(), true);
                                player.sendMessage(new TextComponent(englishConfiguration.getOnlineTimeSuccessEnabled()));
                            } else {
                                player.sendMessage(new TextComponent(englishConfiguration.getOnlineTimeErrorsAlreadyEnabled()));
                            }
                        } else if (args[0].equalsIgnoreCase("off")) {
                            if (mySQL.getPlayer(player.getUniqueId()).join().isShowtime()) {
                                mySQL.updateShowTime(player.getUniqueId(), false);
                                player.sendMessage(new TextComponent(englishConfiguration.getOnlineTimeSuccessDisabled()));
                            } else {
                                player.sendMessage(new TextComponent(englishConfiguration.getOnlineTimeErrorsAlreadyDisabled()));
                            }
                        } else {

                            UUID uuid = mySQL.getPlayerFromName(args[0]).join().getUUID();

                            if (mySQL.getPlayer(uuid).join().isShowtime() || player.hasPermission("*")) {
                                long milliseconds = mySQL.getPlayer(uuid).join().getOnlineTime();
                                long seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds) - TimeUnit.MILLISECONDS.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds));
                                long minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds) - TimeUnit.MILLISECONDS.toMinutes(TimeUnit.MILLISECONDS.toHours(seconds));
                                long hours = TimeUnit.MILLISECONDS.toHours(milliseconds) - TimeUnit.MILLISECONDS.toHours(TimeUnit.MILLISECONDS.toDays(milliseconds));
                                long days = TimeUnit.MILLISECONDS.toDays(seconds);
                                long month = days / 30 - TimeUnit.MILLISECONDS.toDays(seconds);
                                long year = month / 12 - days;

                                IPermissionManagement iPermissionManagement = CloudNetDriver.getInstance().getPermissionManagement();
                                String display = "&f";
                                for (PermissionUserGroupInfo group : Objects.requireNonNull(iPermissionManagement.getUser(uuid)).getGroups()) {
                                    if (Objects.requireNonNull(iPermissionManagement.getUser(uuid)).inGroup(group.getGroup())) {
                                        IPermissionGroup iPermissionGroup = iPermissionManagement.getGroup(group.getGroup());
                                        if (iPermissionGroup != null) {
                                            display = iPermissionGroup.getDisplay();
                                        }
                                    }
                                }

                                player.sendMessage(new TextComponent(englishConfiguration.getOnlineTimeSuccessOthers().replaceAll("%player%", display + mySQL.getPlayer(uuid).join().getName()).replaceAll("%year%", "" + year).replaceAll("%month%", "" + month).replaceAll("%day%", "" + days).replaceAll("%hour%", "" + hours).replaceAll("%minute%", "" + minutes)));

                            } else {
                                player.sendMessage(new TextComponent(englishConfiguration.getOnlineTimeErrorsNotEnabled()));
                            }
                        }
                    } else {
                        player.sendMessage(new TextComponent(englishConfiguration.getOnlineTimeUsage()));
                    }
                }
                case FRENCH, SPANISH -> {

                }
            }
        } else {
            sender.sendMessage(new TextComponent(englishConfiguration.getErrorsPlayer()));
        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        if(args.length == 1) {
            List<String> players = new ArrayList<>();
            players.add("on");
            players.add("off");
            for(ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                players.add(all.getName());
            }
            return players;
        }
        return null;
    }
}
