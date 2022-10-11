package net.galacticprojects.bungeecord.listener;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.ext.bridge.player.ICloudPlayer;
import de.dytanic.cloudnet.ext.bridge.player.IPlayerManager;
import me.lauriichan.laylib.command.CommandManager;
import net.galacticprojects.bungeecord.ProxyPlugin;
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
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import org.checkerframework.checker.units.qual.A;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.UUID;

public class ChatListener implements Listener {
    private ProxyPlugin plugin;

    public ChatListener(ProxyPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onChat(ChatEvent event) {
        CommandManager commandManager =  new CommandManager();
        SQLDatabase database = plugin.getCommonPlugin().getDatabaseRef().get();
        CommonPlugin commonPlugin = plugin.getCommonPlugin();

        if (database == null) {
            commonPlugin.getLogger().error(new Throwable("Database is unavailable!"));
            return;
        }

        if(event.getSender() instanceof ProxiedPlayer player) {
            if (event.getMessage().startsWith("/") || commandManager.getProcess(player.getUniqueId()) != null) {
                return;
            }

            commonPlugin.getDatabaseRef().asOptional().ifPresent(sql -> {
                    ICloudPlayer cloudPlayer = CloudNetDriver.getInstance().getServicesRegistry().getFirstService(IPlayerManager.class).getOnlinePlayer(player.getUniqueId());

                    if (cloudPlayer == null) {
                        return;
                    }

                    UUID uniqueId = player.getUniqueId();
                    String name = player.getName();
                    String ip  = player.getAddress().getAddress().getHostAddress();
                    String server = cloudPlayer.getConnectedService().getServiceId().getName();
                    String timestamp = TimeHelper.toString(OffsetDateTime.now());

                    Chatlog chatlog = new Chatlog(uniqueId, name, ip, server, timestamp, event.getMessage());
                    sql.createChatlog(chatlog);
            });
        }
    }
}
