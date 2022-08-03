package net.galacticprojects.isystem.bungeecord.command;

import de.dytanic.cloudnet.CloudNet;
import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.driver.network.INetworkServer;
import de.dytanic.cloudnet.driver.service.ServiceInfoSnapshot;
import de.dytanic.cloudnet.ext.bridge.BridgePlayerManager;
import de.dytanic.cloudnet.ext.bridge.node.CloudNetBridgeModule;
import de.dytanic.cloudnet.ext.bridge.player.CloudPlayer;
import de.dytanic.cloudnet.ext.bridge.player.ICloudPlayer;
import de.dytanic.cloudnet.ext.bridge.player.IPlayerManager;
import de.dytanic.cloudnet.ext.bridge.player.NetworkConnectionInfo;
import de.dytanic.cloudnet.service.ICloudService;
import net.galacticprojects.isystem.bungeecord.config.MainConfiguration;
import net.galacticprojects.isystem.utils.JavaInstance;
import net.galacticprojects.isystem.utils.color.Color;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class LobbyCommand extends Command {
    public LobbyCommand() {
        super("lobby", null, Arrays.toString(Objects.requireNonNull(JavaInstance.get(MainConfiguration.class)).getFallback().toArray()));
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!( sender instanceof ProxiedPlayer)) {
            sender.sendMessage(new TextComponent(Color.apply("")));
            return;
        }

        for(ServiceInfoSnapshot service : CloudNetDriver.getInstance().getCloudServiceProvider().getCloudServices("fallback")) {
            IPlayerManager playerManager = CloudNetDriver.getInstance().getServicesRegistry().getFirstService(IPlayerManager.class);
            ICloudPlayer cloudPlayer = playerManager.getOnlinePlayer(((ProxiedPlayer) sender).getUniqueId());

            if (cloudPlayer == null) {
                sender.sendMessage(new TextComponent(Color.apply("")));
                return;
            }

            cloudPlayer.getPlayerExecutor().connect(service.getServiceId().getName());
            sender.sendMessage(new TextComponent(Color.apply("")));
        }
    }
}
