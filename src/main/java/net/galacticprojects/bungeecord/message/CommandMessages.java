package net.galacticprojects.bungeecord.message;

import me.lauriichan.laylib.localization.MessageProvider;
import me.lauriichan.laylib.localization.source.Message;

public final class CommandMessages {

	private CommandMessages() {
		throw new UnsupportedOperationException();
	}

	@Message(id = "command.general.player-not-found", content = {
			"//to get Player '$player'"
	})
	public static MessageProvider COMMAND_GENERAL_PLAYER_NOT_FOUND;

	@Message(id = "command.maintenance.off", content = "&#ff0000Joa leude jz is nich mehr maintenance")
	public static MessageProvider COMMAND_MAINTENANCE_OFF;
	@Message(id = "command.maintenance.on", content = "&#0000ffJoa leude jz is mehr maintenance, weil &#5577ff$reason")
	public static MessageProvider COMMAND_MAINTENANCE_ON;


	@Message(id = "command.ban.not-banned", content = "$player is not banned")
	public static MessageProvider COMMAND_BAN_NOT_BANNED;
	@Message(id = "command.ban.create.already-banned", content = {
			"//to get Player '$player'"
	})
	public static MessageProvider COMMAND_BAN_CREATE_ALREADY_BANNED;

	@Message(id = "command.ban.id.not-found", content = {
			"t"
	})
	public static MessageProvider COMMAND_BAN_ID_NOT_FOUND;

	@Message(id = "command.ban.create.success", content = {
			"The Player $player was successfully banned!",
			"Reason: $reason",
			"Time: $time"
	})
	public static MessageProvider COMMAND_BAN_CREATE_SUCCESS;

	@Message(id = "command.ban.delete.success", content = "$#plugin.prefix The ban of $player was successfully deleted")
	public static MessageProvider COMMAND_BAN_DELETE_SUCCESS;

	@Message(id = "command.player.banned", content = {
			"&8「 &5&lGALACTIC&d&lPROJECTS &8」",
			"&r",
			"You have been banned",
			"Reason: $reason",
			"Time: $time",
			"&r",
			"&7If you think that you were banned wrongly, or you want to apologize for your actions, you can submit a ban request in the Discord support or on the website."
	})
	public static MessageProvider COMMAND_PLAYER_BANNED;
}
