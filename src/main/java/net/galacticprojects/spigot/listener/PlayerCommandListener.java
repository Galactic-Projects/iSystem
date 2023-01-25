package net.galacticprojects.spigot.listener;

import net.galacticprojects.common.database.SQLDatabase;
import net.galacticprojects.common.database.model.Player;
import net.galacticprojects.common.util.ClassUtil;
import net.galacticprojects.common.util.JavaAccess;
import net.galacticprojects.spigot.SpigotPlugin;
import net.galacticprojects.spigot.command.impl.version.VersionConstant;
import net.galacticprojects.spigot.message.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerCommandSendEvent;

import java.lang.reflect.Method;

public class PlayerCommandListener implements Listener {

    private final Method craftServerGetCommandMap = ClassUtil.getMethod(ClassUtil.findClass(VersionConstant.craftClassPath("CraftServer")), "getCommandMap");

    @EventHandler
    public void onCommandSend(PlayerCommandSendEvent event) {
        event.getCommands().clear();
        event.getCommands().addAll(SpigotPlugin.getProperties().getAllowedCommands());
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        SQLDatabase database = SpigotPlugin.getInstance().getDatabaseRef().get();

        if(database == null) {
            System.out.println("No Database connection!");
            return;
        }

        SimpleCommandMap simpleCommandMap = (SimpleCommandMap) JavaAccess.invoke(Bukkit.getServer(), craftServerGetCommandMap);
        String command = event.getMessage().replace("/", "").toLowerCase().split(" ")[0];

        Player player = database.getPlayer(event.getPlayer().getUniqueId()).join();

        if((simpleCommandMap.getCommand(command) == null) || (!(SpigotPlugin.getProperties().getAllowedCommands().contains(command)))) {
            SpigotPlugin.getInstance().getMessageManager().translate(Messages.NO_COMMAND, player.getLanguage());
            return;
        }

        if(SpigotPlugin.getProperties().getAllowedCommands().contains(command)) {
            event.setCancelled(false);
            return;
        }

        event.setCancelled(true);
    }
}
