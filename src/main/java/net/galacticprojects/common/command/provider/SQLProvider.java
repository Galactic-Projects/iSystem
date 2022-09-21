package net.galacticprojects.common.command.provider;

import me.lauriichan.laylib.command.Actor;
import me.lauriichan.laylib.command.IProviderArgumentType;
import net.galacticprojects.common.config.SQLConfiguration;
import net.galacticprojects.common.database.SQLDatabase;
import net.galacticprojects.common.util.Ref;

public class SQLProvider implements IProviderArgumentType<Ref<SQLDatabase>> {

    private final SQLConfiguration configuration;
    
    public SQLProvider(final SQLConfiguration configuration) {
    	this.configuration = configuration;
	}

    @Override
    public Ref<SQLDatabase> provide(Actor<?> actor) {
        return configuration.getDatabaseRef();
    }
}
