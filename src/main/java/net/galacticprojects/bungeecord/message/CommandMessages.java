package net.galacticprojects.bungeecord.message;

import me.lauriichan.laylib.localization.MessageProvider;
import me.lauriichan.laylib.localization.source.Message;

public final class CommandMessages {

	private CommandMessages() {
		throw new UnsupportedOperationException();
	}

	@Message(id = "command.process.cancel.unavailable", content = "t")
	public static MessageProvider COMMAND_PROCESS_CANCEL_UNAVAILABLE;

	@Message(id = "command.process.cancel.success", content = "t")
	public static MessageProvider COMMAND_PROCESS_CANCEL_SUCCESS;

	@Message(id = "command.process.create.no-command", content = "t")
	public static MessageProvider COMMAND_PROCESS_CREATE_NO_COMMAND;

	@Message(id = "command.process.create.no-action", content = "t")
	public static MessageProvider COMMAND_PROCESS_CREATE_NO_ACTION;

	@Message(id = "command.process.create.success", content = "t")
	public static MessageProvider COMMAND_PROCESS_CREATE_SUCCESS;

	@Message(id = "command.process.skip.unskippable", content = "t")
	public static MessageProvider COMMAND_PROCESS_SKIP_UNSKIPPABLE;

	@Message(id = "command.process.skip.success", content = "t")
	public static MessageProvider COMMAND_PROCESS_SKIP_SUCCESS;

	@Message(id = "command.process.input.suggestion", content = "t")
	public static MessageProvider COMMAND_PROCESS_INPUT_SUGGESTION;

	@Message(id = "command.process.input.user", content = "t")
	public static MessageProvider COMMAND_PROCESS_INPUT_USER;

	@Message(id = "command.process.input.failed", content = "t")
	public static MessageProvider COMMAND_PROCESS_INPUT_FAILED;

	@Message(id = "command.process.suggest.header", content = "t")
	public static MessageProvider COMMAND_PROCESS_SUGGEST_HEADER;

	@Message(id = "command.process.suggest.format", content = "t")
	public static MessageProvider COMMAND_PROCESS_SUGGEST_FORMAT;

	@Message(id = "command.process.argument.specify", content = "t")
	public static MessageProvider COMMAND_PROCESS_ARGUMENT_SPECIFY;

	@Message(id = "command.process.argument.cancelable.message", content = "t")
	public static MessageProvider COMMAND_PROCESS_ARGUMENT_CANCELABLE_MESSAGE;

	@Message(id = "command.process.argument.cancelable.hover", content = "t")
	public static MessageProvider COMMAND_PROCESS_ARGUMENT_CANCELABLE_HOVER;

	@Message(id = "command.process.argument.optional.message", content = "t")
	public static MessageProvider COMMAND_PROCESS_ARGUMENT_OPTIONAL_MESSAGE;

	@Message(id = "command.process.argument.optional.hover", content = "t")
	public static MessageProvider COMMAND_PROCESS_ARGUMENT_OPTIONAL_HOVER;

	@Message(id = "command.process.execution.failed", content = "t")
	public static MessageProvider COMMAND_PROCESS_EXECUTION_FAILED;

	@Message(id = "command.maintenance.off", content = "&#ff0000Joa leude jz is nich mehr maintenance")
	public static MessageProvider COMMAND_MAINTENANCE_OFF;
	
	@Message(id = "command.maintenance.on", content = "&#0000ffJoa leude jz is mehr maintenance, weil &#5577ff$reason")
	public static MessageProvider COMMAND_MAINTENANCE_ON;
	
	@Message(id = "command.maintenance.idk", content = {
			"$#command.maintenance.on",
			"$#command.maintenance.off"
	})
	public static MessageProvider COMMAND_MAINTENANCE_IDK;

	@Message(id = "generic.command.ban-player-not-found", content = {
			"//to get Player '$player'"
	})
	public static MessageProvider COMMAND_BAN_PLAYER_NOT_FOUND;

	@Message(id = "generic.command.ban-player-already-banned", content = {
			"//to get Player '$player'"
	})
	public static MessageProvider COMMAND_BAN_PLAYER_ALREADY_BANNED;

	@Message(id = "generic.command.ban-id-not-found", content = {
			"t"
	})
	public static MessageProvider COMMAND_BAN_ID_NOT_FOUND;

	@Message(id = "generic.command.ban-player-successfully", content = {
			"//to get Reason '$reason'",
			"//to get Player '$player'",
			"//to get Time '$time'"
	})
	public static MessageProvider COMMAND_BAN_PLAYER_SUCCESSFULLY;

	@Message(id = "command.ban.id.1", content = {
			"t"
	})
	public static MessageProvider COMMAND_BAN_ID_1;
	@Message(id = "command.ban.id.2", content = {
		"t"
	})
	public static MessageProvider COMMAND_BAN_ID_2;
	@Message(id = "command.ban.id.3", content = {
		"t"
	})
	public static MessageProvider COMMAND_BAN_ID_3;
	@Message(id = "command.ban.id.4", content = {
			"t"
	})
	public static MessageProvider COMMAND_BAN_ID_4;
	@Message(id = "command.ban.id.5", content = {
			"t"
	})
	public static MessageProvider COMMAND_BAN_ID_5;
	@Message(id = "command.ban.id.6", content = {
			"t"
	})
	public static MessageProvider COMMAND_BAN_ID_6;
	@Message(id = "command.ban.id.7", content = {
			"t"
	})
	public static MessageProvider COMMAND_BAN_ID_7;
	@Message(id = "command.ban.id.8", content = {
			"t"
	})
	public static MessageProvider COMMAND_BAN_ID_8;
	@Message(id = "command.ban.id.9", content = {
			"t"
	})
	public static MessageProvider COMMAND_BAN_ID_9;
}
