package net.galacticprojects.bungeecord.command.provider;

import me.lauriichan.laylib.command.Actor;
import me.lauriichan.laylib.command.IProviderArgumentType;
import net.galacticprojects.bungeecord.ProxyPlugin;

public final class ProxyPluginProvider implements IProviderArgumentType<ProxyPlugin> {

	private final ProxyPlugin plugin;

	public ProxyPluginProvider(final ProxyPlugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public ProxyPlugin provide(Actor<?> actor) {
		return plugin;
	}

}
