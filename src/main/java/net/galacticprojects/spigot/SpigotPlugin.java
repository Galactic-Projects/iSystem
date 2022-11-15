package net.galacticprojects.spigot;

import net.galacticprojects.bungeecord.config.PluginConfiguration;
import net.galacticprojects.common.CommonPlugin;
import net.galacticprojects.common.message.MessageProviderFactoryImpl;
import net.galacticprojects.spigot.command.HelpCommand;
import net.galacticprojects.spigot.command.SubtitleCommand;
import net.galacticprojects.spigot.command.impl.BukkitCommandInjector;
import net.galacticprojects.spigot.command.provider.SpigotPluginProvider;
import net.galacticprojects.spigot.listener.AsyncChatListener;
import net.galacticprojects.spigot.listener.ConnectionListener;
import net.galacticprojects.spigot.listener.LabyModListener;
import net.galacticprojects.spigot.message.CommandDescription;
import net.galacticprojects.spigot.message.Messages;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import me.lauriichan.laylib.command.ArgumentRegistry;
import me.lauriichan.laylib.command.CommandManager;
import me.lauriichan.laylib.localization.MessageManager;
import me.lauriichan.laylib.localization.source.AnnotationMessageSource;
import me.lauriichan.laylib.localization.source.EnumMessageSource;
import me.lauriichan.laylib.logger.ISimpleLogger;

public class SpigotPlugin extends JavaPlugin {
	
	private CommonPlugin common;

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
    	// Other stuff after this
    	createConfigurations();
    	reloadConfigurations();
    	registerListeners();
    	registerArgumentTypes();
    	registerCommands();
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
	}

	public void reloadConfigurations() {
	}

    private void registerListeners() {
        PluginManager manager = Bukkit.getPluginManager();
        manager.registerEvents(new ConnectionListener(), this);
        manager.registerEvents(new LabyModListener(), this);
		manager.registerEvents(new AsyncChatListener(), this);
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
    
}
