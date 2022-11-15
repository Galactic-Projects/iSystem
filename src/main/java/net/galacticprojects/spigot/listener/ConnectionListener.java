package net.galacticprojects.spigot.listener;

import com.mojang.authlib.GameProfile;
import eu.cloudnetservice.driver.CloudNetDriver;
import eu.cloudnetservice.driver.permission.PermissionGroup;
import eu.cloudnetservice.driver.permission.PermissionUser;
import net.galacticprojects.common.util.MojangProfileService;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import javax.swing.*;
import java.lang.reflect.Field;
import java.util.UUID;

public class ConnectionListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        final UUID uniqueId = event.getPlayer().getUniqueId();
        final String name = MojangProfileService.getName(uniqueId);
        final PermissionUser permissionUser = CloudNetDriver.instance().permissionManagement().user(uniqueId);
        final String tag = ChatColor.translateAlternateColorCodes('&', CloudNetDriver.instance().permissionManagement().highestPermissionGroup(permissionUser).prefix() + " " + name);
        event.getPlayer().setDisplayName(fixName(tag));
    }

    private String fixName(String value) {
        if (value.length() < 16)
            return value;
        return value.substring(0, 16);
    }
}
