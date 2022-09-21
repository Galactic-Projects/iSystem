package net.galacticprojects.common.command.provider;

import me.lauriichan.laylib.command.Actor;
import me.lauriichan.laylib.command.IProviderArgumentType;
import net.galacticprojects.common.CommonPlugin;

public final class CommonPluginProvider implements IProviderArgumentType<CommonPlugin> {
	
	private final CommonPlugin plugin;
	
	public CommonPluginProvider(final CommonPlugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public CommonPlugin provide(Actor<?> actor) {
		return plugin;
	}

}
