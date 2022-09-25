package net.galacticprojects.bungeecord;

import java.io.File;

import me.lauriichan.laylib.command.ArgumentRegistry;
import me.lauriichan.laylib.command.CommandManager;
import me.lauriichan.laylib.localization.MessageManager;
import me.lauriichan.laylib.localization.source.*;
import me.lauriichan.laylib.logger.ISimpleLogger;
import net.galacticprojects.bungeecord.command.*;
import net.galacticprojects.bungeecord.command.impl.BungeeCommandInjector;
import net.galacticprojects.bungeecord.command.provider.ProxyPluginProvider;
import net.galacticprojects.bungeecord.config.BanConfiguration;
import net.galacticprojects.bungeecord.config.PluginConfiguration;
import net.galacticprojects.bungeecord.listener.ConnectListener;
import net.galacticprojects.bungeecord.listener.PingListener;
import net.galacticprojects.bungeecord.message.BanMessage;
import net.galacticprojects.bungeecord.message.CommandMessages;
import net.galacticprojects.bungeecord.message.SystemMessage;
import net.galacticprojects.bungeecord.message.TimeMessage;
import net.galacticprojects.bungeecord.util.OnlineTime;
import net.galacticprojects.bungeecord.util.TablistManager;
import net.galacticprojects.common.CommonPlugin;
import net.galacticprojects.common.database.model.Ban;
import net.galacticprojects.common.message.MessageProviderFactoryImpl;
import net.galacticprojects.spigot.message.CommandDescription;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import org.checkerframework.checker.units.qual.A;

public class ProxyPlugin extends Plugin {
	
	private CommonPlugin common;
	
	private PluginConfiguration pluginConfiguration;
	private BanConfiguration banConfiguration;
	private static ProxyPlugin plugin;

	@Override
	public void onLoad() {
		common = new CommonPlugin(getLogger(), getDataFolder(), getFile());
	}
	
	/*
	 * Startup
	 */
	
    @Override
    public void onEnable() {
		plugin = this;
		registerMessages();
		common.getCommandManager().setInjector(new BungeeCommandInjector(common.getCommandManager(), common.getMessageManager(), this));
    	common.start();
    	// Other stuff after this
    	createConfigurations();
    	reloadConfigurations();
    	registerListeners();
    	registerArgumentTypes();
    	registerCommands();
		new OnlineTime(this);
    }
    
    private void registerMessages() {
    	MessageManager messageManager = common.getMessageManager();
    	MessageProviderFactoryImpl factory = common.getMessageProviderFactory();
    	// Register messages below
		messageManager.register(new EnumMessageSource(BanMessage.class, factory));
    	messageManager.register(new EnumMessageSource(CommandDescription.class, factory));
		messageManager.register(new AnnotationMessageSource(TimeMessage.class, factory));
		messageManager.register(new AnnotationMessageSource(CommandMessages.class, factory));
		messageManager.register(new AnnotationMessageSource(SystemMessage.class, factory));
    }

	private void createConfigurations() {
		ISimpleLogger logger = common.getLogger();
		File dataFolder = getDataFolder();
		// Create configs below
		pluginConfiguration = new PluginConfiguration(logger, dataFolder);
		banConfiguration = new BanConfiguration(logger, dataFolder);
	}

	public void reloadConfigurations() {
		pluginConfiguration.reload();
		banConfiguration.reload();
	}

    private void registerListeners() {
        PluginManager manager = getProxy().getPluginManager();
        manager.registerListener(this, new ConnectListener(this));
		manager.registerListener(this, new PingListener(this));
    }
    
    private void registerArgumentTypes() {
    	ArgumentRegistry registry = common.getCommandManager().getRegistry();
    	registry.setProvider(new ProxyPluginProvider(this));
    }
    
    private void registerCommands() {
    	CommandManager commandManager = common.getCommandManager();
		commandManager.register(BungeeHelpCommand.class);
    	commandManager.register(MaintenanceCommand.class);
		commandManager.register(BanCommand.class);
		commandManager.register(LanguageCommand.class);
		commandManager.register(OnlineTimeCommand.class);
    }
    
    /*
     * Shutdown
     */

    @Override
    public void onDisable() {
		pluginConfiguration.save();
    	common.stop();
    }
    
    /*
     * Getter
     */
    
    public PluginConfiguration getPluginConfiguration() {
		return pluginConfiguration;
	}
	public BanConfiguration getBanConfiguration() {return banConfiguration;}

	public CommonPlugin getCommonPlugin() {return common;}

	public static ProxyPlugin getInstance() {
return plugin;
	}

}
