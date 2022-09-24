package net.galacticprojects.bungeecord.command.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import me.lauriichan.laylib.command.CommandManager;
import me.lauriichan.laylib.command.Node;
import me.lauriichan.laylib.command.NodeCommand;
import me.lauriichan.laylib.command.util.Triple;
import me.lauriichan.laylib.localization.MessageManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

public final class BungeeCommandBridge extends Command implements TabExecutor {

	private final CommandManager commandManager;
	private final MessageManager messageManager;
	private final String name;

	public BungeeCommandBridge(final CommandManager commandManager, final MessageManager messageManager,
			final String name, final List<String> aliases) {
		super(name, null, aliases.toArray(String[]::new));
		this.commandManager = commandManager;
		this.messageManager = messageManager;
		this.name = name;
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		commandManager.createProcess(new BungeeActor<>(sender, messageManager), name, args);
	}

	@Override
	public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
		Triple<NodeCommand, Node, String> triple = commandManager.findNode(name, args);
		if (triple == null) {
			return Collections.emptyList();
		}
		if (!triple.getB().hasChildren()) {
			return Collections.emptyList();
		}
		return Arrays.asList(triple.getB().getNames());
	}

}
