package net.galacticprojects.spigot.command;

import me.lauriichan.laylib.command.*;
import me.lauriichan.laylib.command.annotation.Action;
import me.lauriichan.laylib.command.annotation.Argument;
import me.lauriichan.laylib.command.annotation.Command;
import me.lauriichan.laylib.command.annotation.Permission;
import me.lauriichan.laylib.command.util.Triple;
import me.lauriichan.laylib.localization.Key;

@Command(name = "spigothelp", description = "command.description.help", aliases = "shelp")
public class HelpCommand {

    @Action("")
    @Permission("system.help")
    public void help(CommandManager commandManager, Actor<?> actor, @Argument(name = "command") String command) {
        Triple<NodeCommand, Node, String> triple = commandManager.findNode(command);
        if (triple == null) {
            actor.sendTranslatedMessage("command.help.command.none", Key.of("command", command));
            return;
        }
        Node node = triple.getB();
        NodeAction action = node.getAction();
        if (action == null) {
            if (!node.hasChildren()) {
                actor.sendTranslatedMessage("command.help.command.empty", Key.of("command", command),
                        Key.of("description", "$#" + triple.getA().getDescription()));
                return;
            }
            actor.sendTranslatedMessage("command.help.command.tree", Key.of("command", triple.getC()),
                    Key.of("description", "$#" + triple.getA().getDescription()), Key.of("tree", generateTree(actor, node.getNames())));
            return;
        }
        if (node.hasChildren()) {
            actor.sendTranslatedMessage("command.help.command.tree-executable", Key.of("command", triple.getC()),
                    Key.of("description", "$#" + action.getDescription()), Key.of("tree", generateTree(actor, node.getNames())));
            return;
        }
        actor.sendTranslatedMessage("command.help.command.executable", Key.of("command", triple.getC()),
                Key.of("description", "$#" + action.getDescription()));
    }

    private String generateTree(Actor<?> actor, String[] names) {
        StringBuilder builder = new StringBuilder();
        for (int index = 0; index < names.length; index++) {
            builder.append(actor.getTranslatedMessageAsString("command.help.tree.format", Key.of("name", names[index])));
            if (index + 1 != names.length) {
                builder.append('\n');
            }
        }
        return builder.toString();
    }
}
