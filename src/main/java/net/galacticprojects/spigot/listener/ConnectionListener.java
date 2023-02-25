package net.galacticprojects.spigot.listener;

import dev.derklaro.aerogel.Injector;
import eu.cloudnetservice.driver.inject.InjectionLayer;
import eu.cloudnetservice.driver.permission.PermissionManagement;
import eu.cloudnetservice.driver.permission.PermissionUser;
import net.galacticprojects.common.util.MojangProfileService;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

public class ConnectionListener implements Listener {

    private final Injector injector;

    public ConnectionListener() {
        this.injector = InjectionLayer.ext().injector();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        final UUID uniqueId = event.getPlayer().getUniqueId();
        final String name = MojangProfileService.getName(uniqueId);
        final PermissionUser permissionUser = injector.instance(PermissionManagement.class).user(uniqueId);
        final String tag = ChatColor.translateAlternateColorCodes('&', injector.instance(PermissionManagement.class).highestPermissionGroup(permissionUser).prefix() + " " + name);
        event.getPlayer().setDisplayName(fixName(tag));
    }

    private String fixName(String value) {
        if (value.length() < 16)
            return value;
        return value.substring(0, 16);
    }
}
