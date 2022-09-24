package net.galacticprojects.common.command.message;

import me.lauriichan.laylib.localization.MessageProvider;
import me.lauriichan.laylib.localization.source.Message;

public final class CommandMessages {

    private CommandMessages() {
        throw new UnsupportedOperationException();
    }

    @Message(id = "plugin.name", content = "&4&lSYSTEM")
    public static MessageProvider PLUGIN_NAME;
    @Message(id = "plugin.prefix", content = "&8「 $#plugin.name &8」&r")
    public static MessageProvider PLUGIN_PREFIX;

    @Message(id = "command.help.command.none", content = "$#plugin.prefix The command '&c$command&7' doesn't exist!")
    public static MessageProvider COMMAND_HELP_NONE;
    @Message(id = "command.help.command.empty", content = {
            "&8=< $#plugin.name &8>-< &7$command",
            " ",
            "&7$description",
            " ",
            "&8=< $#plugin.name &8>-< &7$command"
    })
    public static MessageProvider COMMAND_HELP_EMPTY;
    @Message(id = "command.help.command.tree", content = {
            "&8=< $#plugin.name &8>-< &7$command",
            " ",
            "&7$description",
            " ",
            "&7This command has following subcommands:",
            "$tree",
            " ",
            "&8=< $#plugin.name &8>-< &7$command"
    })
    public static MessageProvider COMMAND_HELP_TREE;
    @Message(id = "command.help.command.executable", content = {
            "&8=< $#plugin.name &8>-< &7$command",
            " ",
            "&7$description",
            " ",
            "&7This command can be executed.",
            " ",
            "&8=< $#plugin.name &8>-< &7$command"
    })
    public static MessageProvider COMMAND_HELP_EXECUATABLE;
    @Message(id = "command.help.command.tree-executable", content = {
            "&8=< $#plugin.name &8>-< &7$command",
            " ",
            "&7$description",
            " ",
            "&7This command can be executed and has following subcommands:",
            "$tree",
            " ",
            "&8=< $#plugin.name &8>-< &7$command"
    })
    public static MessageProvider COMMAND_HELP_TREE$EXECUTABLE;
    @Message(id = "command.help.tree.format", content = "&8- &7$name")
    public static MessageProvider COMMAND_HELP_TREE_FORMAT;

    @Message(id = "command.translation.reload.start", content = "$#plugin.prefix Translation reload started")
    public static MessageProvider COMMAND_TRANSLATION_RELOAD_START;
    @Message(id = "command.translation.reload.end", content = {
            "&8=< $#plugin.name &8>-< &7Translations",
            " ",
            "&8Newly loaded: &7$new",
            "&8Reloaded: &7$loaded",
            "&8Unloaded: &7$unloaded"
    })
    public static MessageProvider COMMAND_TRANSLATION_RELOAD_END;

}
