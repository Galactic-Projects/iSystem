package net.galacticprojects.spigot.command.provider;

import me.lauriichan.laylib.command.Actor;
import me.lauriichan.laylib.command.IProviderArgumentType;
import net.galacticprojects.spigot.SpigotPlugin;

public final class SpigotPluginProvider implements IProviderArgumentType<SpigotPlugin> {

	private final SpigotPlugin plugin;
	
	public SpigotPluginProvider(final SpigotPlugin plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public SpigotPlugin provide(Actor<?> actor) {
		return plugin;
	}

}
