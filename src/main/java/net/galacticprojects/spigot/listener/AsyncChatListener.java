package net.galacticprojects.spigot.listener;

import com.syntaxphoenix.syntaxapi.utils.java.UniCode;
import dev.derklaro.aerogel.Injector;
import eu.cloudnetservice.driver.inject.InjectionLayer;
import eu.cloudnetservice.driver.permission.PermissionManagement;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Objects;

public class AsyncChatListener implements Listener {

    private final Injector injector;

    public AsyncChatListener() {
        this.injector = InjectionLayer.ext().injector();
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        String group = injector.instance(PermissionManagement.class).highestPermissionGroup(Objects.requireNonNull(injector.instance(PermissionManagement.class).user(event.getPlayer().getUniqueId()))).prefix();
        event.setFormat(ChatColor.translateAlternateColorCodes('&', group + event.getPlayer().getName() + " §8" + UniCode.ARROWS_RIGHT + " §7" + event.getMessage()));
    }
}
