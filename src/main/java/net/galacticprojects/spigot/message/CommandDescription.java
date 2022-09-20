package net.galacticprojects.spigot.message;

import me.lauriichan.laylib.localization.source.IMessageDefinition;

public enum CommandDescription implements IMessageDefinition {
    ;

    private final String fallback;

    private CommandDescription() {
        this(null);
    }
    private CommandDescription(String fallback) {
        this.fallback = fallback;
    }

    @Override
    public String id() {
        return name().toLowerCase().replace('_', '.').replace('$', '_');
    }

    @Override
    public String fallback() {
        return fallback;
    }
}
