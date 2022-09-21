package net.galacticprojects.bungeecord;

import java.io.File;

import me.lauriichan.laylib.command.ArgumentRegistry;
import me.lauriichan.laylib.command.CommandManager;
import me.lauriichan.laylib.localization.MessageManager;
import me.lauriichan.laylib.localization.source.*;
import me.lauriichan.laylib.logger.ISimpleLogger;
import net.galacticprojects.bungeecord.command.BanCommand;
import net.galacticprojects.bungeecord.command.MaintenanceCommand;
import net.galacticprojects.bungeecord.command.impl.BungeeCommandInjector;
import net.galacticprojects.bungeecord.command.provider.ProxyPluginProvider;
import net.galacticprojects.bungeecord.config.PluginConfiguration;
import net.galacticprojects.common.CommonPlugin;
import net.galacticprojects.common.config.SQLConfiguration;
import net.galacticprojects.common.message.MessageProviderFactoryImpl;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

public class ProxyPlugin extends Plugin {
	
	private CommonPlugin common;
	
	private PluginConfiguration pluginConfiguration;

	@Override
	public void onLoad() {
		common = new CommonPlugin(getLogger(), getDataFolder());
		common.getCommandManager().setInjector(new BungeeCommandInjector(common.getCommandManager(), common.getMessageManager(), this));
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
    	
    }

	private void createConfigurations() {
		ISimpleLogger logger = common.getLogger();
		File dataFolder = getDataFolder();
		// Create configs below
		pluginConfiguration = new PluginConfiguration(logger, dataFolder);
	}

	public void reloadConfigurations() {
		pluginConfiguration.reload();
	}

    private void registerListeners() {
        PluginManager manager = getProxy().getPluginManager();
        
    }
    
    private void registerArgumentTypes() {
    	ArgumentRegistry registry = common.getCommandManager().getRegistry();
    	registry.setProvider(new ProxyPluginProvider(this));
    }
    
    private void registerCommands() {
    	CommandManager commandManager = common.getCommandManager();
    	commandManager.register(MaintenanceCommand.class);
		commandManager.register(BanCommand.class);
    }
    
    /*
     * Shutdown
     */

    @Override
    public void onDisable() {
    	
    	// Other stuff before this
    	common.stop();
    }
    
    /*
     * Getter
     */
    
    public PluginConfiguration getPluginConfiguration() {
		return pluginConfiguration;
	}

}
