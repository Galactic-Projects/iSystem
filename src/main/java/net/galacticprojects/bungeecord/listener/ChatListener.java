package net.galacticprojects.bungeecord.listener;

import eu.cloudnetservice.driver.CloudNetDriver;
import eu.cloudnetservice.driver.permission.PermissionGroup;
import eu.cloudnetservice.modules.bridge.player.CloudPlayer;
import eu.cloudnetservice.modules.bridge.player.PlayerManager;
import me.lauriichan.laylib.command.CommandManager;
import me.lauriichan.laylib.localization.Key;
import net.galacticprojects.bungeecord.ProxyPlugin;
import net.galacticprojects.bungeecord.command.TeamChatCommand;
import net.galacticprojects.bungeecord.config.PluginConfiguration;
import net.galacticprojects.bungeecord.message.CommandMessages;
import net.galacticprojects.bungeecord.util.TimeHelper;
import net.galacticprojects.common.CommonPlugin;
import net.galacticprojects.common.database.SQLDatabase;
import net.galacticprojects.common.database.model.Chatlog;
import net.galacticprojects.common.database.model.FriendSettings;
import net.galacticprojects.common.database.model.Player;
import net.galacticprojects.common.util.ComponentParser;
import net.galacticprojects.common.util.MojangProfileService;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class ChatListener implements Listener {
    private ProxyPlugin plugin;

    public ChatListener(ProxyPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onChat(ChatEvent event) {
        SQLDatabase database = plugin.getCommonPlugin().getDatabaseRef().get();
        CommonPlugin commonPlugin = plugin.getCommonPlugin();

        if (database == null) {
            commonPlugin.getLogger().error(new Throwable("Database is unavailable!"));
            return;
        }

        if (event.getSender() instanceof ProxiedPlayer player) {
            CloudPlayer cloudPlayer = CloudNetDriver.instance().serviceRegistry().firstProvider(PlayerManager.class).onlinePlayer(player.getUniqueId());
            PermissionGroup info = CloudNetDriver.instance().permissionManagement().highestPermissionGroup(Objects.requireNonNull(CloudNetDriver.instance().permissionManagement().user(player.getUniqueId())));

            if (cloudPlayer == null) {
                return;
            }

            if (event.getMessage().startsWith("/") || commonPlugin.getCommandManager().getProcess(player.getUniqueId()) != null) {
                return;
            }

            commonPlugin.getDatabaseRef().asOptional().ifPresent(sqlDatabase -> {
                UUID uniqueId = player.getUniqueId();
                String name = player.getName();
                String ip = player.getAddress().getAddress().getHostAddress();
                String server = Objects.requireNonNull(cloudPlayer.connectedService()).serverName();
                String timestamp = TimeHelper.toString(OffsetDateTime.now());

                Chatlog chatlog = null;
                if (TeamChatCommand.TEAMCHAT.contains(player.getUniqueId()) && event.getMessage().startsWith("@")) {
                    chatlog = new Chatlog(uniqueId, name, ip, server, timestamp, "(TEAMCHAT) " + event.getMessage().replaceAll("@", ""));
                } else {
                    chatlog = new Chatlog(uniqueId, name, ip, server, timestamp, event.getMessage());
                }

                sqlDatabase.createChatlog(chatlog);
            });

            if (TeamChatCommand.TEAMCHAT.contains(player.getUniqueId()) && player.hasPermission("system.teamchat") && event.getMessage().startsWith("@")) {
                commonPlugin.getDatabaseRef().asOptional().ifPresent(sql -> {
                    for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                        sql.getPlayer(all.getUniqueId()).thenAccept(playerData -> {
                            if (TeamChatCommand.TEAMCHAT.contains(all.getUniqueId()) || all.hasPermission("system.teamchat")) {
                                Key playerName = Key.of("player", info.prefix() + player.getName());
                                Key message = Key.of("message", event.getMessage().replaceAll("@", ""));

                                all.sendMessage(ComponentParser.parse(commonPlugin.getMessageManager().translate(CommandMessages.COMMAND_TEAMCHAT_FORMAT, playerData.getLanguage(), playerName, message)));
                            }

                        });
                    }
                });
                event.setCancelled(true);
            }

        }
    }
}
