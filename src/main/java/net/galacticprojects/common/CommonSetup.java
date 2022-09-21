package net.galacticprojects.common;

import me.lauriichan.laylib.command.CommandManager;
import me.lauriichan.laylib.localization.MessageManager;
import me.lauriichan.laylib.logger.ISimpleLogger;
import me.lauriichan.laylib.logger.JavaSimpleLogger;
import net.galacticprojects.common.command.TranslationReloadCommand;
import net.galacticprojects.common.config.SQLConfiguration;
import net.galacticprojects.common.database.SQLDatabase;
import net.galacticprojects.common.message.MessageTranslationManager;
import net.galacticprojects.common.util.Ref;

import java.io.File;
import java.util.logging.Logger;

public final class CommonSetup {

    private static final Ref<CommonSetup> COMMON = Ref.of();

    public static CommonSetup instance() {
        return COMMON.get();
    }

    private final ISimpleLogger logger;
    private final MessageManager messageManager;
    private final CommandManager commandManager;
    private final MessageTranslationManager translationManager;

    private final File dataFolder;
    private SQLConfiguration sqlConfiguration;

    public CommonSetup(final Logger logger, final File dataFolder){
        if(COMMON.isPresent()){
            throw new UnsupportedOperationException("Instance already exists");
        }
        COMMON.set(this);
        this.dataFolder = dataFolder;
        this.logger = new JavaSimpleLogger(logger);
        this.messageManager = new MessageManager();
        this.commandManager = new CommandManager(this.logger);
        this.translationManager = new MessageTranslationManager(messageManager, this.logger, dataFolder);
    }

    public ISimpleLogger getLogger() {
        return logger;
    }

    public CommandManager getCommandManager() {
        return commandManager;
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
        commandManager.register(TranslationReloadCommand.class);
        translationManager.reload();
        createConfigurations();
        reloadConfigurations();
    }

    private void createConfigurations() {
        sqlConfiguration = new SQLConfiguration(logger, dataFolder);
    }

    public void reloadConfigurations() {
        sqlConfiguration.reload();
    }

    public final void stop(){
        COMMON.set(null);
    }


}
