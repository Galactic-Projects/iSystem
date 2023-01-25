package net.galacticprojects.spigot.message;

import me.lauriichan.laylib.localization.MessageProvider;
import me.lauriichan.laylib.localization.source.Message;

public class Messages {


    @Message(id = "prefix.system", content = "&8&l「 &4&lSYSTEM &8&l」&7")
    public static MessageProvider SYSTEM_PREFIX;
    @Message(id = "command.errors.exist", content = "$#prefix.system &cThere is no command with that name!")
    public static MessageProvider NO_COMMAND;

}
