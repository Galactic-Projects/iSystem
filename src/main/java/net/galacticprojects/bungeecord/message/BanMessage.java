package net.galacticprojects.bungeecord.message;

import me.lauriichan.laylib.localization.source.IMessageDefinition;

public enum BanMessage implements IMessageDefinition {

    COMMAND_BAN_ID_1("Unallowed Clientmodifications"), COMMAND_BAN_ID_2("t"), COMMAND_BAN_ID_3("t"), COMMAND_BAN_ID_4("t"), COMMAND_BAN_ID_5("t"), COMMAND_BAN_ID_6("t"), COMMAND_BAN_ID_7("t"), COMMAND_BAN_ID_8("t"), COMMAND_BAN_ID_9("t");

    private final String fallback;

private BanMessage() {this(null);}
    private BanMessage(String fallback) {this.fallback = fallback;}

    @Override
    public String id() {
        return name().toLowerCase().replace('_', '.').replace('$', '_');
    }

    @Override
    public String fallback() {
        return fallback;
    }
}
