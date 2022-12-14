package net.galacticprojects.bungeecord;

import java.io.File;
import java.time.OffsetDateTime;
import java.util.concurrent.TimeUnit;

import eu.cloudnetservice.driver.CloudNetDriver;
import eu.cloudnetservice.driver.service.ServiceInfoSnapshot;
import me.lauriichan.laylib.command.ArgumentRegistry;
import me.lauriichan.laylib.command.CommandManager;
import me.lauriichan.laylib.localization.Key;
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
import net.galacticprojects.bungeecord.util.TimeHelper;
import net.galacticprojects.common.CommonPlugin;
import net.galacticprojects.common.message.MessageProviderFactoryImpl;
import net.galacticprojects.common.secure.GalacticSecure;
import net.galacticprojects.common.util.ComponentParser;
import net.galacticprojects.common.modules.DiscordBot;
import net.galacticprojects.common.modules.TeamSpeakBot;
import net.galacticprojects.spigot.message.CommandDescription;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
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

	@Override
	public void onLoad() {
		common = new CommonPlugin(getLogger(), getDataFolder(), getFile());
	}

	/*
	 * Startup
	 */

	@Override
	public void onEnable() {
		getProxy().registerChannel("BungeeCord");
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
		countDown();
		discordBot = new DiscordBot();
		teamSpeakBot = new TeamSpeakBot();
		try {
			secure = new GalacticSecure();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
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

	public void countDown() {
		ProxyServer.getInstance().getScheduler().schedule(this, () -> {
			OffsetDateTime offsetDateTime = OffsetDateTime.now();
			String time = TimeHelper.BAN_TIME_FORMATTER.format(offsetDateTime);

			if (time.contains("04:00:00")) {
				for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
					common.getDatabaseRef().asOptional().ifPresent(sql -> {
						sql.getPlayer(player.getUniqueId()).thenAccept(data -> {
							player.sendMessage(ComponentParser.parse(common.getMessageManager().translate(CommandMessages.SYSTEM_NETWORK_RESTART_ALMOST, data.getLanguage(),
									Key.of("hour", 1), Key.of("minute", 0), Key.of("second", 0))));
						});
					});
				}
				return;
			}

			if (time.contains("04:15:00")) {
				for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
					common.getDatabaseRef().asOptional().ifPresent(sql -> {
						sql.getPlayer(player.getUniqueId()).thenAccept(data -> {
							player.sendMessage(ComponentParser.parse(common.getMessageManager().translate(CommandMessages.SYSTEM_NETWORK_RESTART_ALMOST, data.getLanguage(),
									Key.of("hour", 0), Key.of("minute", 45), Key.of("second", 0))));
						});
					});
				}
				return;
			}

			if (time.contains("04:30:00")) {
				for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
					common.getDatabaseRef().asOptional().ifPresent(sql -> {
						sql.getPlayer(player.getUniqueId()).thenAccept(data -> {
							player.sendMessage(ComponentParser.parse(common.getMessageManager().translate(CommandMessages.SYSTEM_NETWORK_RESTART_ALMOST, data.getLanguage(),
									Key.of("hour", 0), Key.of("minute", 30), Key.of("second", 0))));
						});
					});
				}
				return;
			}

			if (time.contains("04:45:00")) {
				for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
					common.getDatabaseRef().asOptional().ifPresent(sql -> {
						sql.getPlayer(player.getUniqueId()).thenAccept(data -> {
							player.sendMessage(ComponentParser.parse(common.getMessageManager().translate(CommandMessages.SYSTEM_NETWORK_RESTART_ALMOST, data.getLanguage(),
									Key.of("hour", 0), Key.of("minute", 15), Key.of("second", 0))));
						});
					});
				}
				return;
			}

			if (time.contains("04:50:00")) {
				for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
					common.getDatabaseRef().asOptional().ifPresent(sql -> {
						sql.getPlayer(player.getUniqueId()).thenAccept(data -> {
							player.sendMessage(ComponentParser.parse(common.getMessageManager().translate(CommandMessages.SYSTEM_NETWORK_RESTART_ALMOST, data.getLanguage(),
									Key.of("hour", 0), Key.of("minute", 10), Key.of("second", 0))));
						});
					});
				}
				return;
			}

			if (time.contains("04:55:00")) {
				for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
					common.getDatabaseRef().asOptional().ifPresent(sql -> {
						sql.getPlayer(player.getUniqueId()).thenAccept(data -> {
							player.sendMessage(ComponentParser.parse(common.getMessageManager().translate(CommandMessages.SYSTEM_NETWORK_RESTART_ALMOST, data.getLanguage(),
									Key.of("hour", 0), Key.of("minute", 5), Key.of("second", 0))));
						});
					});
				}
				return;
			}

			if (time.contains("04:59:00")) {
				for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
					common.getDatabaseRef().asOptional().ifPresent(sql -> {
						sql.getPlayer(player.getUniqueId()).thenAccept(data -> {
							player.sendMessage(ComponentParser.parse(common.getMessageManager().translate(CommandMessages.SYSTEM_NETWORK_RESTART_ALMOST, data.getLanguage(),
									Key.of("hour", 0), Key.of("minute", 1), Key.of("second", 0))));
						});
					});
				}
				return;
			}

			if (time.contains("04:59:30")) {
				for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
					common.getDatabaseRef().asOptional().ifPresent(sql -> {
						sql.getPlayer(player.getUniqueId()).thenAccept(data -> {
							player.sendMessage(ComponentParser.parse(common.getMessageManager().translate(CommandMessages.SYSTEM_NETWORK_RESTART_ALMOST, data.getLanguage(),
									Key.of("hour", 0), Key.of("minute", 0), Key.of("second", 30))));
						});
					});
					return;
				}

				if (time.contains("04:59:50")) {
					for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
						common.getDatabaseRef().asOptional().ifPresent(sql -> {
							sql.getPlayer(player.getUniqueId()).thenAccept(data -> {
								player.sendMessage(ComponentParser.parse(common.getMessageManager().translate(CommandMessages.SYSTEM_NETWORK_RESTART_ALMOST, data.getLanguage(),
										Key.of("hour", 0), Key.of("minute", 0), Key.of("second", 10))));
							});
						});
					}
				}
			}

			if (time.contains("04:59:55")) {
				for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
					common.getDatabaseRef().asOptional().ifPresent(sql -> {
						sql.getPlayer(player.getUniqueId()).thenAccept(data -> {
							player.sendMessage(ComponentParser.parse(common.getMessageManager().translate(CommandMessages.SYSTEM_NETWORK_RESTART_ALMOST, data.getLanguage(),
									Key.of("hour", 0), Key.of("minute", 0), Key.of("second", 5))));
						});
					});
				}
			}

			if (time.contains("05:00:00")) {
				for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
					common.getDatabaseRef().asOptional().ifPresent(sql -> {
						sql.getPlayer(player.getUniqueId()).thenAccept(data -> {
							player.sendMessage(ComponentParser.parse(common.getMessageManager().translate(CommandMessages.SYSTEM_NETWORK_RESTART_NOW, data.getLanguage())));
						});
					});
				}
				ProxyServer.getInstance().getScheduler().schedule(this, new Runnable() {
					@Override
					public void run() {
						CloudNetDriver.instance().permissionManagement().reload();
						CloudNetDriver.instance().groupConfigurationProvider().reload();
						CloudNetDriver.instance().moduleProvider().stopAll();
						for (ServiceInfoSnapshot serviceInfoSnapshots : CloudNetDriver.instance().cloudServiceProvider().services()) {

						}
						CloudNetDriver.instance().moduleProvider().startAll();
					}
				}, 1, TimeUnit.SECONDS);
			}

		}, 1L, 1L, TimeUnit.SECONDS);
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

	public static ProxyPlugin getInstance() {
		return plugin;
	}

}
