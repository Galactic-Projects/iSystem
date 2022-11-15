package net.galacticprojects.spigot.listener;

import com.syntaxphoenix.syntaxapi.utils.java.UniCode;
import eu.cloudnetservice.driver.CloudNetDriver;
import net.galacticprojects.common.util.color.ColorParser;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.jetbrains.annotations.Async;

public class AsyncChatListener implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        String group = CloudNetDriver.instance().permissionManagement().highestPermissionGroup(CloudNetDriver.instance().permissionManagement().user(event.getPlayer().getUniqueId())).prefix();
        event.setFormat(ChatColor.translateAlternateColorCodes('&', group + " " + event.getPlayer().getName() + " §8" + UniCode.ARROWS_RIGHT + " §7" + event.getMessage()));
    }
}
