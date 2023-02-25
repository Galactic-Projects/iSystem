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
import net.galacticprojects.bungeecord.config.BotConfiguration;
import net.galacticprojects.bungeecord.config.PluginConfiguration;
import net.galacticprojects.bungeecord.config.ReportConfiguration;
import net.galacticprojects.bungeecord.listener.*;
import net.galacticprojects.bungeecord.message.BanMessage;
import net.galacticprojects.bungeecord.message.CommandMessages;
import net.galacticprojects.bungeecord.message.SystemMessage;
import net.galacticprojects.bungeecord.message.TimeMessage;
import net.galacticprojects.bungeecord.util.Countdown;
import net.galacticprojects.bungeecord.util.OnlineTime;
import net.galacticprojects.bungeecord.util.RestartCounter;
import net.galacticprojects.common.CommonPlugin;
import net.galacticprojects.common.message.MessageProviderFactoryImpl;
import net.galacticprojects.common.secure.GalacticSecure;
import net.galacticprojects.common.modules.DiscordBot;
import net.galacticprojects.common.modules.TeamSpeakBot;
import net.galacticprojects.common.util.Verify;
import net.galacticprojects.spigot.message.CommandDescription;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

public class ProxyPlugin extends Plugin {

	private CommonPlugin common;

	private PluginConfiguration pluginConfiguration;
	private GalacticSecure secure;
	private BanConfiguration banConfiguration;
	private ReportConfiguration reportConfiguration;
	private BotConfiguration botConfiguration;
	private static ProxyPlugin plugin;
	private static DiscordBot discordBot;
	private static TeamSpeakBot teamSpeakBot;
	private static Verify verify;

	@Override
	public void onLoad() {
		common = new CommonPlugin(getLogger(), getDataFolder(), getFile());
	}

	/*
	 * Startup
	 */

	@Override
	public void onEnable() {
		ProxyServer.getInstance().registerChannel("BungeeCord");
		ProxyServer.getInstance().registerChannel("AntiCheat");
		plugin = this;
		registerMessages();
		common.getCommandManager().setInjector(new BungeeCommandInjector(common.getCommandManager(), common.getMessageManager(), common, this));
		common.start();
		// Other stuff after this
		createConfigurations();
		reloadConfigurations();
		registerListeners();
		registerArgumentTypes();
		registerCommands();
		Countdown.setupCountdown(this);
		new OnlineTime(this);
		new RestartCounter();
		discordBot = new DiscordBot();
		teamSpeakBot = new TeamSpeakBot();
		try {
			secure = new GalacticSecure();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		verify = new Verify();
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
		reportConfiguration = new ReportConfiguration(logger, dataFolder);
		botConfiguration = new BotConfiguration(logger, dataFolder);
	}

	public void reloadConfigurations() {
		pluginConfiguration.reload();
		banConfiguration.reload();
		reportConfiguration.reload();
		botConfiguration.reload();
	}

	private void registerListeners() {
		PluginManager manager = getProxy().getPluginManager();
		manager.registerListener(this, new MessageListener(this));
		manager.registerListener(this, new ConnectListener(this));
		manager.registerListener(this, new PingListener(this));
		manager.registerListener(this, new ServerSwitchListener(this));
		manager.registerListener(this, new DisconnectListener(this));
		manager.registerListener(this, new ChatListener(this));
	}

	private void registerArgumentTypes() {
		ArgumentRegistry registry = common.getCommandManager().getRegistry();
		registry.setProvider(new ProxyPluginProvider(this));
	}

	private void registerCommands() {
		CommandManager commandManager = common.getCommandManager();
		commandManager.register(BanCommand.class);
		commandManager.register(BroadcastCommand.class);
		commandManager.register(BungeeHelpCommand.class);
		commandManager.register(CoinsCommand.class);
		commandManager.register(FriendCommand.class);
		commandManager.register(HistoryCommand.class);
		commandManager.register(LanguageCommand.class);
		commandManager.register(LevelCommand.class);
		commandManager.register(MaintenanceCommand.class);
		commandManager.register(OnlineTimeCommand.class);
		commandManager.register(PartyCommand.class);
		commandManager.register(PingCommand.class);
		commandManager.register(ReportCommand.class);
		commandManager.register(SystemCommand.class);
		commandManager.register(TeamChatCommand.class);
		commandManager.register(LinkCommand.class);
	}

	/*
	 * Shutdown
	 */

	@Override
	public void onDisable() {
		pluginConfiguration.save();
		common.stop();
		discordBot.shutdown();
		teamSpeakBot.shutdown();
	}

	/*
	 * Getter
	 */

	public GalacticSecure getSecure() {
		return secure;
	}

	public PluginConfiguration getPluginConfiguration() {
		return pluginConfiguration;
	}

	public BanConfiguration getBanConfiguration() {
		return banConfiguration;
	}

	public ReportConfiguration getReportConfiguration() {
		return reportConfiguration;
	}

	public BotConfiguration getBotConfiguration() {
		return botConfiguration;
	}

	public TeamSpeakBot getTeamSpeakBot() {
		return teamSpeakBot;
	}

	public DiscordBot getDiscordBot() {
		return discordBot;
	}

	public CommonPlugin getCommonPlugin() {
		return common;
	}

	public Verify getVerify() {
		return verify;
	}

	public static ProxyPlugin getInstance() {
		return plugin;
	}
}
