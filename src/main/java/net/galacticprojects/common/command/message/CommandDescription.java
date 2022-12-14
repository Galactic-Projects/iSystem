package net.galacticprojects.common.command.message;

import me.lauriichan.laylib.localization.source.IMessageDefinition;

public enum CommandDescription implements IMessageDefinition {

	COMMAND_DESCRIPTION_TRANSLATION("Reload translations"),
	COMMAND_DESCRIPTION_HELP("Gives all information available about a command (without arguments)");


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
