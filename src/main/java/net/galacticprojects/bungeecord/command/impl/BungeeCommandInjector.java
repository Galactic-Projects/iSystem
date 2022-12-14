package net.galacticprojects.bungeecord.command.impl;

import java.util.concurrent.ConcurrentHashMap;

import net.galacticprojects.common.CommonPlugin;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import me.lauriichan.laylib.command.CommandManager;
import me.lauriichan.laylib.command.ICommandInjector;
import me.lauriichan.laylib.command.NodeCommand;
import me.lauriichan.laylib.localization.MessageManager;

public final class BungeeCommandInjector implements ICommandInjector {

    private final BungeeCommandListener listener;

    private final CommandManager commandManager;
    private final MessageManager messageManager;
    
    private final PluginManager pluginManager;
    private final Plugin plugin;
    
    private final CommonPlugin common;
    
    private final ConcurrentHashMap<String, BungeeCommandBridge> commands = new ConcurrentHashMap<>();

    public BungeeCommandInjector(final CommandManager commandManager, final MessageManager messageManager, final CommonPlugin common, final Plugin plugin) {
        this.listener = new BungeeCommandListener(common, commandManager, messageManager);
        this.common = common;
        this.messageManager = messageManager;
        this.commandManager = commandManager;
        this.plugin = plugin;
        this.pluginManager = plugin.getProxy().getPluginManager();
        pluginManager.registerListener(plugin, listener);
    }

    @Override
    public void inject(NodeCommand nodeCommand) {
    	if(commands.containsKey(nodeCommand.getName())) {
    		return;
    	}
    	BungeeCommandBridge command = new BungeeCommandBridge(common, commandManager, messageManager, nodeCommand.getName(), nodeCommand.getAliases());
        commands.put(nodeCommand.getName(), command);
        pluginManager.registerCommand(plugin, command);
    }
    
    @Override
    public void uninject(NodeCommand nodeCommand) {
    	BungeeCommandBridge command = commands.remove(nodeCommand.getName());
    	if(command == null) {
    		return;
    	}
    	pluginManager.unregisterCommand(command);
    }
}
