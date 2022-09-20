package net.galacticprojects.bungeecord.util;

import de.dytanic.cloudnet.ext.bridge.player.CloudPlayer;
import net.galacticprojects.common.util.MojangProfileService;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;

public class iPlayer {

    ProxiedPlayer player;

    public iPlayer(CommandSender sender) {
        player = (ProxiedPlayer) sender;
    }

    public iPlayer(ProxiedPlayer player) {
        this.player = player;
    }

    public iPlayer(String name) {
        this.player = ProxyServer.getInstance().getPlayer(MojangProfileService.getUniqueId(name));
    }

    public iPlayer(UUID uniqueId) {
        this.player = ProxyServer.getInstance().getPlayer(uniqueId);
    }

    public iPlayer(CloudPlayer cloudPlayer) {
        this.player = ProxyServer.getInstance().getPlayer(cloudPlayer.getPlayerExecutor().getPlayerUniqueId());
    }

    /**
     * Checks if the player has the permission in the string
     * @return boolean
     */
    public boolean hasPermission(String permission) {
        return false;
    }

    /**
     * returns the player's group as a string
     * @return String
     */
    public String getGroup() {
        return null;
    }

    /**
     * Sets the group from the player to the one that is in the String
     */
    public void setGroup() {

    }

    /**
     * returns the player's UniqueId as a string
     * @return String
     */
    public String getUniqueIdAsString() {
        return null;
    }

    /**
     * returns the player's UniqueId
     * @return UUID
     */
    public UUID getUniqueId() {
        return null;
    }
}
