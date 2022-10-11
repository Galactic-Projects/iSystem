package net.galacticprojects.bungeecord.command.impl;

import java.util.Arrays;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import me.lauriichan.laylib.command.Actor;
import me.lauriichan.laylib.command.CommandManager;
import me.lauriichan.laylib.command.CommandProcess;
import me.lauriichan.laylib.localization.MessageManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.Connection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public final class BungeeCommandListener implements Listener {

	private final CommonPlugin common;

	private final CommandManager commandManager;
	private final MessageManager messageManager;

	public BungeeCommandListener(final CommonPlugin common, final CommandManager commandManager, final MessageManager messageManager) {
		this.common = common;
		this.commandManager = commandManager;
		this.messageManager = messageManager;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onChat(ChatEvent event) {
		Connection connection = event.getSender();
		if (!(connection instanceof CommandSender)) {
			return;
		}
		CommandSender sender = (CommandSender) connection;
		if (sender instanceof ProxiedPlayer player) {
			CommandProcess process = commandManager.getProcess(player.getUniqueId());
			if (process == null) {
				return;
			}
			event.setCancelled(true);
			BungeeActor<ProxiedPlayer> actor = new BungeeActor<>(player, common, messageManager);
			if (event.isCommand()) {
				String[] args = event.getMessage().split(" ");
				if (args[0].equalsIgnoreCase("/cancel")) {
					commandManager.cancelProcess(actor);
					return;
				}
				if (args[0].equalsIgnoreCase("/skip")) {
					commandManager.handleProcessSkip(actor, process);
					return;
				}
				if (args.length > 1 && args[0].equalsIgnoreCase("/suggestion")) {
					commandManager.handleProcessInput(actor, process, Arrays.stream(args).skip(1)
							.filter(Predicate.not(String::isBlank)).collect(Collectors.joining(" ")), true);
					return;
				}
				commandManager.handleProcessInput(actor, process, event.getMessage());
				return;
			}
			commandManager.handleProcessInput(actor, process, event.getMessage());
			return;
		}
		CommandProcess process = commandManager.getProcess(Actor.IMPL_ID);
		if (process == null) {
			return;
		}
		event.setCancelled(true);
		BungeeActor<CommandSender> actor = new BungeeActor<>(sender, common, messageManager);
		String[] args = event.getMessage().split(" ");
		if (args[0].equalsIgnoreCase("/cancel")) {
			commandManager.cancelProcess(actor);
			return;
		}
		if (args[0].equalsIgnoreCase("/skip")) {
			commandManager.handleProcessSkip(actor, process);
			return;
		}
		if (args.length > 1 && args[0].equalsIgnoreCase("/suggestion")) {
			commandManager.handleProcessInput(actor, process,
					Arrays.stream(args).skip(1).filter(Predicate.not(String::isBlank)).collect(Collectors.joining(" ")),
					true);
			return;
		}
		commandManager.handleProcessInput(actor, process, event.getMessage());
	}

}
