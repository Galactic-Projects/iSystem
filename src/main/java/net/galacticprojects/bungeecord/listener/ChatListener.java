package net.galacticprojects.bungeecord.listener;

import dev.derklaro.aerogel.Inject;
import dev.derklaro.aerogel.Injector;
import eu.cloudnetservice.driver.inject.InjectionLayer;
import eu.cloudnetservice.driver.permission.PermissionGroup;
import eu.cloudnetservice.driver.permission.PermissionManagement;
import eu.cloudnetservice.driver.registry.ServiceRegistry;
import eu.cloudnetservice.modules.bridge.player.CloudPlayer;
import eu.cloudnetservice.modules.bridge.player.PlayerManager;
import me.lauriichan.laylib.command.CommandManager;
import me.lauriichan.laylib.localization.Key;
import net.galacticprojects.bungeecord.ProxyPlugin;
import net.galacticprojects.bungeecord.command.SystemCommand;
import net.galacticprojects.bungeecord.command.TeamChatCommand;
import net.galacticprojects.bungeecord.config.PluginConfiguration;
import net.galacticprojects.bungeecord.message.CommandMessages;
import net.galacticprojects.bungeecord.message.SystemMessage;
import net.galacticprojects.bungeecord.util.TimeHelper;
import net.galacticprojects.common.CommonPlugin;
import net.galacticprojects.common.database.SQLDatabase;
import net.galacticprojects.common.database.model.Chatlog;
import net.galacticprojects.common.database.model.FriendSettings;
import net.galacticprojects.common.database.model.Player;
import net.galacticprojects.common.util.ComponentParser;
import net.galacticprojects.common.util.MojangProfileService;
import net.galacticprojects.common.util.color.ComponentColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
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
    private final Injector injector;

    public ChatListener(ProxyPlugin plugin) {
        this.plugin = plugin;
        this.injector = InjectionLayer.ext().injector();
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
            PermissionGroup info = injector.instance(PermissionManagement.class).highestPermissionGroup(Objects.requireNonNull(injector.instance(PermissionManagement.class).user(player.getUniqueId())));

            if (info == null) {
                return;
            }

            if (event.getMessage().startsWith("/") || commonPlugin.getCommandManager().getProcess(player.getUniqueId()) != null) {
                return;
            }
            UUID uniqueId = player.getUniqueId();

            database.getPlayer(uniqueId).thenAccept(playerData -> {
                if (!(playerData.getVerified())) {
                    event.setCancelled(true);
                    if (!(plugin.getVerify().contains(uniqueId))) {
                        player.sendMessage(new TextComponent(ComponentColor.apply(commonPlugin.getMessageManager().translate(SystemMessage.VERIFY_ERROR, playerData.getLanguage()))));
                        return;
                    }

                    if (!(event.getMessage().equals(plugin.getVerify().get(uniqueId)))) {
                        player.sendMessage(new TextComponent(ComponentColor.apply(commonPlugin.getMessageManager().translate(SystemMessage.VERIFY_WRONG, playerData.getLanguage()))));
                        return;
                    }

                    plugin.getVerify().remove(uniqueId);
                    playerData.setVerified(true);
                    database.updatePlayer(playerData);
                    player.sendMessage(new TextComponent(ComponentColor.apply(commonPlugin.getMessageManager().translate(SystemMessage.VERIFY_SUCCESS, playerData.getLanguage()))));
                }


                String name = player.getName();
                String ip = player.getAddress().getAddress().getHostAddress();
                String server = ProxyPlugin.getInstance().getProxy().getPlayer(player.getUniqueId()).getServer().getInfo().getName();
                String timestamp = TimeHelper.toString(OffsetDateTime.now());

                Chatlog chatlog = null;
                if (TeamChatCommand.TEAMCHAT.contains(player.getUniqueId()) && event.getMessage().startsWith("@")) {
                    chatlog = new Chatlog(uniqueId, name, ip, server, timestamp, "(TEAMCHAT) " + event.getMessage().replaceAll("@", ""));
                } else {
                    chatlog = new Chatlog(uniqueId, name, ip, server, timestamp, event.getMessage());
                }

                database.createChatlog(chatlog);
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
