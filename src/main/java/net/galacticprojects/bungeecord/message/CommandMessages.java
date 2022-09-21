package net.galacticprojects.bungeecord.message;

import me.lauriichan.laylib.localization.MessageProvider;
import me.lauriichan.laylib.localization.source.Message;

public final class CommandMessages {

	private CommandMessages() {
		throw new UnsupportedOperationException();
	}
	
	@Message(id = "command.maintenance.off", content = "&#ff0000Joa leude jz is nich mehr maintenance")
	public static MessageProvider COMMAND_MAINTENANCE_OFF;
	
	@Message(id = "command.maintenance.on", content = "&#0000ffJoa leude jz is mehr maintenance, weil &#5577ff$reason")
	public static MessageProvider COMMAND_MAINTENANCE_ON;
	
	@Message(id = "command.maintenance.idk", content = {
			"$#command.maintenance.on",
			"$#command.maintenance.off"
	})
	public static MessageProvider COMMAND_MAINTENANCE_IDK;
	
}
