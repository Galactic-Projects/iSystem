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

	@Message(id = "command.language.changed", content = "$#plugin.prefix Your language has been successfully changed to $language")
	public static MessageProvider COMMAND_LANGUAGE_CHANGED;

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

	@Message(id = "command.onlinetime.success", content = "$#plugin.prefix Your &bonline-time &7is &c$daystime Day(s)&7, &c$hourtime Hour(s)&7 and &c$minutetime Minute(s)&7.")
	public static MessageProvider COMMAND_ONLINETIME_SUCCESS;

	@Message(id = "party.name", content = "&b&lParty")
	public static MessageProvider PLUGIN_NAME;
	@Message(id = "party.prefix", content = "&8「 $#party.name &8」&r")
	public static MessageProvider PLUGIN_PREFIX;

	@Message(id = "command.party.no-party", content = "$#party.prefix You are currently not in any party")
	public static MessageProvider COMMAND_PARTY_NO_PARTY;
	@Message(id = "command.party.not-leader", content = "$#party.prefix You aren't the leader of the party")
	public static MessageProvider COMMAND_PARTY_NOT_LEADER;
	@Message(id = "command.party.not-leader-or-moderator", content = "$#party.prefix You aren't the leader or moderator of the party")
	public static MessageProvider COMMAND_PARTY_NOT_LEADER_OR_MODERATOR;
	@Message(id = "command.party.created", content = "$#party.prefix The party $party was created successfully")
	public static MessageProvider COMMAND_PARTY_CREATED;
	@Message(id = "command.party.invite", content = "$#party.prefix You have invited $player to your party")
	public static MessageProvider COMMAND_PARTY_INVITE;
	@Message(id = "command.party.invited", content = "$#party.prefix You were invited to the party $party")
	public static MessageProvider COMMAND_PARTY_INVITED;
	@Message(id = "command.party.not-invited", content = "$#party.prefix You were not invited by any party")
	public static MessageProvider COMMAND_PARTY_NOT_INVITED;
	@Message(id = "command.party.decline.target", content = "$#party.prefix You have declined the invitation from $party")
	public static MessageProvider COMMAND_PARTY_DECLINE_TARGET;
	@Message(id = "command.party.decline.leader", content = "$#party.prefix The player $player has declined your invitation")
	public static MessageProvider COMMAND_PARTY_DECLINE_LEADER;
	@Message(id = "command.party.public.unavailable", content = "$#party.prefix Currently you can't publish parties")
	public static MessageProvider COMMAND_PARTY_PUBLIC_UNAVAILABLE;
	@Message(id = "command.party.accept.target", content = "$#party.prefix You have accepted the invitation to the party $party")
	public static MessageProvider COMMAND_PARTY_ACCEPT_TARGET;
	@Message(id = "command.party.accept.leader", content = "$#party.prefix The player $player has accepted your invitation")
	public static MessageProvider COMMAND_PARTY_ACCEPT_LEADER;
	@Message(id = "command.party.promote.target", content = "$#party.prefix You have been promoted to moderator by $player")
	public static MessageProvider COMMAND_PARTY_PROMOTE_TARGET;
	@Message(id = "command.party.promote.leader", content = "$#party.prefix You promoted $player to moderator")
	public static MessageProvider COMMAND_PARTY_PROMOTE_LEADER;
	@Message(id = "command.party.promote.target", content = "$#party.prefix You have been demoted to member by $player")
	public static MessageProvider COMMAND_PARTY_PROMOTE_UNAVAILABLE;
	@Message(id = "command.party.promote.target", content = "$#party.prefix The promotion feature is currently not available")
	public static MessageProvider COMMAND_PARTY_DEMOTE_TARGET;
	@Message(id = "command.party.demote.leader", content = "$#party.prefix You have demoted $player to member")
	public static MessageProvider COMMAND_PARTY_DEMOTE_LEADER;
	@Message(id = "command.party.demote.unavailable", content = "$#party.prefix The demotion feature is currently not available")
	public static MessageProvider COMMAND_PARTY_DEMOTE_UNAVAILABLE;
	@Message(id = "command.party.deleted", content = "$#party.prefix The Party $party was deleted")
	public static MessageProvider COMMAND_PARTY_DELETED;
}
