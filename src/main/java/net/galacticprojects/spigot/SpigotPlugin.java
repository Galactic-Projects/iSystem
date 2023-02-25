package net.galacticprojects.spigot;

import com.fasterxml.jackson.databind.deser.std.DateDeserializers;
import dev.derklaro.aerogel.Injector;
import eu.cloudnetservice.driver.inject.InjectionLayer;
import net.galacticprojects.bungeecord.config.PluginConfiguration;
import net.galacticprojects.common.CommonPlugin;
import net.galacticprojects.common.database.SQLDatabase;
import net.galacticprojects.common.message.MessageProviderFactoryImpl;
import net.galacticprojects.common.secure.GalacticSecure;
import net.galacticprojects.spigot.command.HelpCommand;
import net.galacticprojects.spigot.command.SubtitleCommand;
import net.galacticprojects.spigot.command.impl.BukkitCommandInjector;
import net.galacticprojects.spigot.command.provider.SpigotPluginProvider;
import net.galacticprojects.spigot.configuration.CommandProperties;
import net.galacticprojects.spigot.listener.AsyncChatListener;
import net.galacticprojects.spigot.listener.ConnectionListener;
import net.galacticprojects.spigot.listener.LabyModListener;
import net.galacticprojects.spigot.listener.PlayerCommandListener;
import net.galacticprojects.spigot.message.CommandDescription;
import net.galacticprojects.spigot.message.Messages;

import java.io.File;
import java.util.Properties;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import me.lauriichan.laylib.command.ArgumentRegistry;
import me.lauriichan.laylib.command.CommandManager;
import me.lauriichan.laylib.localization.MessageManager;
import me.lauriichan.laylib.localization.source.AnnotationMessageSource;
import me.lauriichan.laylib.localization.source.EnumMessageSource;
import me.lauriichan.laylib.logger.ISimpleLogger;

public class SpigotPlugin extends JavaPlugin {

	private static CommonPlugin common;
	private GalacticSecure secure;
	private static CommandProperties properties;

	private static Plugin plugin;


	@Override
	public void onLoad() {
		common = new CommonPlugin(getLogger(), getDataFolder(), getFile());
		common.getCommandManager().setInjector(new BukkitCommandInjector(common.getCommandManager(), common.getMessageManager(), this));
	}

	/*
	 * Startup
	 */

	@Override
	public void onEnable() {
		registerMessages();
		common.start();
		plugin = this;
		// Other stuff after this
		createConfigurations();
		reloadConfigurations();
		registerListeners();
		registerArgumentTypes();
		registerCommands();
		try {
			secure = new GalacticSecure();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void registerMessages() {
		MessageManager messageManager = common.getMessageManager();
		MessageProviderFactoryImpl factory = common.getMessageProviderFactory();
		// Register messages below
		messageManager.register(new EnumMessageSource(CommandDescription.class, factory));
		messageManager.register(new AnnotationMessageSource(Messages.class, factory));
	}


	private void createConfigurations() {
		ISimpleLogger logger = common.getLogger();
		File dataFolder = getDataFolder();
		// Create configs below
		dataFolder.mkdir();

		try {
			properties = new CommandProperties();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	public void reloadConfigurations() {
	}

	private void registerListeners() {
		PluginManager manager = Bukkit.getPluginManager();
		manager.registerEvents(new ConnectionListener(), this);
		manager.registerEvents(new LabyModListener(), this);
		manager.registerEvents(new AsyncChatListener(), this);
		manager.registerEvents(new PlayerCommandListener(), this);
	}

	private void registerArgumentTypes() {
		ArgumentRegistry registry = common.getCommandManager().getRegistry();
		registry.setProvider(new SpigotPluginProvider(this));
	}

	private void registerCommands() {
		CommandManager commandManager = common.getCommandManager();
		commandManager.register(HelpCommand.class);
		commandManager.register(SubtitleCommand.class);
	}


	/*
	 * Shutdown
	 */
	@Override
	public void onDisable() {

	}


	/*
	 * Getter
	 */
	public GalacticSecure getSecure() {
		return secure;
	}
	public static Plugin getPlugin() {
		return plugin;
	}
	public static CommandProperties getProperties() {
		return properties;
	}
	public static CommonPlugin getInstance() {
		return common;
	}
}
