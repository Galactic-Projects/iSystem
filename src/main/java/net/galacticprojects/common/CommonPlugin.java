package net.galacticprojects.common;

import me.lauriichan.laylib.command.ArgumentRegistry;
import me.lauriichan.laylib.command.CommandManager;
import me.lauriichan.laylib.localization.MessageManager;
import me.lauriichan.laylib.localization.source.AnnotationMessageSource;
import me.lauriichan.laylib.localization.source.EnumMessageSource;
import me.lauriichan.laylib.logger.ISimpleLogger;
import me.lauriichan.laylib.logger.JavaSimpleLogger;
import net.galacticprojects.common.command.TranslationReloadCommand;
import net.galacticprojects.common.command.message.CommandDescription;
import net.galacticprojects.common.command.message.CommandManagerMessages;
import net.galacticprojects.common.command.message.CommandMessages;
import net.galacticprojects.common.command.provider.CommonPluginProvider;
import net.galacticprojects.common.command.provider.SQLProvider;
import net.galacticprojects.common.config.SQLConfiguration;
import net.galacticprojects.common.database.SQLDatabase;
import net.galacticprojects.common.database.migration.impl.MigrationManager;
import net.galacticprojects.common.message.MessageProviderFactoryImpl;
import net.galacticprojects.common.message.MessageTranslationManager;
import net.galacticprojects.common.util.Ref;
import net.galacticprojects.common.util.source.Resources;

import java.io.File;
import java.util.logging.Logger;

public final class CommonPlugin {

	private static final Ref<CommonPlugin> COMMON = Ref.of();

	public static CommonPlugin instance() {
		return COMMON.get();
	}

	private final ISimpleLogger logger;
	private final CommandManager commandManager;

	private final MessageManager messageManager;
	private final MessageProviderFactoryImpl messageProviderFactory;
	
	private final MessageTranslationManager translationManager;
	private final MigrationManager migrationManager;

	private final File dataFolder;
	private final Resources resources;
	private SQLConfiguration sqlConfiguration;

	public CommonPlugin(final Logger logger, final File dataFolder, File jarFile) {
		if (COMMON.isPresent()) {
			throw new UnsupportedOperationException("Instance already exists");
		}
		COMMON.set(this);
		this.dataFolder = dataFolder;
		this.logger = new JavaSimpleLogger(logger);
		this.resources = new Resources(dataFolder, jarFile, logger);
		this.commandManager = new CommandManager(this.logger);
		this.messageManager = new MessageManager();
		this.messageProviderFactory = new MessageProviderFactoryImpl(this.logger);
		this.translationManager = new MessageTranslationManager(messageManager, this.logger, dataFolder);
		this.migrationManager = new MigrationManager(this.logger, resources);
	}

	public MigrationManager getMigrationManager() {
		return migrationManager;
	}

	public Resources getResources() {
		return resources;
	}

	public ISimpleLogger getLogger() {
		return logger;
	}

	public CommandManager getCommandManager() {
		return commandManager;
	}
	
	public MessageProviderFactoryImpl getMessageProviderFactory() {
		return messageProviderFactory;
	}

	public MessageManager getMessageManager() {
		return messageManager;
	}

	public Ref<SQLDatabase> getDatabaseRef() {
		return sqlConfiguration.getDatabaseRef();
	}

	public MessageTranslationManager getTranslationManager() {
		return translationManager;
	}

	public final void start() {
		registerMessages();
		translationManager.reload();
		createConfigurations();
		reloadConfigurations();
		registerArgumentTypes();
		registerCommands();
	}
	
	private void registerMessages() {
		messageManager.register(new EnumMessageSource(CommandDescription.class, messageProviderFactory));
		messageManager.register(new EnumMessageSource(CommandManagerMessages.class, messageProviderFactory));
		messageManager.register(new AnnotationMessageSource(CommandMessages.class, messageProviderFactory));
	}

	private void createConfigurations() {
		sqlConfiguration = new SQLConfiguration(logger, dataFolder);
	}

	public void reloadConfigurations() {
		sqlConfiguration.reload();
	}

	private void registerArgumentTypes() {
		ArgumentRegistry argumentRegistry = commandManager.getRegistry();
		argumentRegistry.setProvider(new SQLProvider(sqlConfiguration));
		argumentRegistry.setProvider(new CommonPluginProvider(this));
	}

	private void registerCommands() {
		commandManager.register(TranslationReloadCommand.class);
	}

	public final void stop() {
		sqlConfiguration.getDatabaseRef().asOptional().ifPresent(database -> database.shutdown());
		COMMON.set(null);
	}

}
