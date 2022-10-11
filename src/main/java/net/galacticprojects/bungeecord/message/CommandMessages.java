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

	@Message(id = "maintenance.prefix", content = "&8&l「 &c&lMAINTENANCE &8&l」&7")
	public static MessageProvider MAINTENANCE_PREFIX;

	@Message(id = "command.maintenance.off", content = "$#maintenance.prefix &7The &4maintenace mode &7is now &cdisabled&7.")
	public static MessageProvider COMMAND_MAINTENANCE_OFF;
	@Message(id = "command.maintenance.on", content = "$#maintenance.prefix &7The &4maintenace mode &7is now &aenabled&7 with &4reason &c$reason&7.")
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

	@Message(id = "command.onlinetime.success", content = "$#plugin.prefix The &bonline-time &7by &b$player&7is &7is &c$daystime Day(s)&7, &c$hourtime Hour(s)&7 and &c$minutetime Minute(s)&7.")
	public static MessageProvider COMMAND_ONLINETIME_SUCCESS_OTHERS;

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

	@Message(id = "teamchat.prefix", content = "&8&l「 &9&lTEAMCHAT &8&l」&7")
	public static MessageProvider TEAMCHAT_PREFIX;
	@Message(id = "command.teamchat.left", content = "$#teamchat.prefix Joa nh bisch halt raus")
	public static MessageProvider COMMAND_TEAMCHAT_LEFT;
	@Message(id = "command.teamchat.join", content = "$#teamchat.prefix Joa bisch drin")
	public static MessageProvider COMMAND_TEAMCHAT_JOIN;

	@Message(id = "friend.prefix", content = "&8&l「 &e&lFRIEND &8&l」&7")
	public static MessageProvider FRIEND_PREFIX;

	@Message(id = "command.friend.toggle.requests.off", content = "$#friend.prefix &7You &asuccessfully &7switched your &4friend requests &7to &coff&7.")
	public static MessageProvider FRIEND_TOGGLE_REQUESTS_OFF;

	@Message(id = "command.friend.toggle.requests.already.off", content = "$#friend.prefix &cThe &4friend requests &care already &4off&c.")
	public static MessageProvider FRIEND_TOGGLE_ALREADY_REQUESTS_OFF;
	@Message(id = "command.friend.toggle.requests.on", content = "$#friend.prefix &7You &asuccessfully &7switched your &4friend requests &7to &con&7.")
	public static MessageProvider FRIEND_TOGGLE_REQUESTS_ON;

	@Message(id = "command.friend.toggle.requests.already.on", content = "$#friend.prefix &cThe &4friend requests &care already &4on&c.")
	public static MessageProvider FRIEND_TOGGLE_ALREADY_REQUESTS_ON;

	@Message(id = "command.friend.toggle.messages.off", content = "$#friend.prefix &7You &asuccessfully &7switched your &4friend private messages &7to &coff&c.")
	public static MessageProvider FRIEND_TOGGLE_MESSAGES_OFF;

	@Message(id = "command.friend.toggle.requests.already.off", content = "$#friend.prefix &cThe &4friend private messages &care already &4off&c.")
	public static MessageProvider FRIEND_TOGGLE_ALREADY_MESSAGES_OFF;
	@Message(id = "command.friend.toggle.messages.on", content = "$#friend.prefix &7You &asuccessfully &7switched your &4friend private messages &7to &con&7.")
	public static MessageProvider FRIEND_TOGGLE_MESSAGES_ON;

	@Message(id = "command.friend.toggle.requests.already.on", content = "$#friend.prefix &cThe &4friend private messages &care already &4on&c.")
	public static MessageProvider FRIEND_TOGGLE_ALREADY_MESSAGES_ON;

	@Message(id = "command.friend.toggle.jump.off", content = "$#friend.prefix &7You &asuccessfully &7switched your &4friend jump &7to &coff&7.")
	public static MessageProvider FRIEND_TOGGLE_JUMP_OFF;

	@Message(id = "command.friend.toggle.jump.already.off", content = "$#friend.prefix &cThe &4friend jump &care already &4off&c.")
	public static MessageProvider FRIEND_TOGGLE_ALREADY_JUMP_OFF;
	@Message(id = "command.friend.toggle.jump.on", content = "$#friend.prefix &7You &asuccessfully &7switched your &4friend jump &7to &con&7.")
	public static MessageProvider FRIEND_TOGGLE_JUMP_ON;

	@Message(id = "command.friend.toggle.jump.already.on", content = "$#friend.prefix &cThe &4friend jump &care already &4on&c.")
	public static MessageProvider FRIEND_TOGGLE_ALREADY_JUMP_ON;

	@Message(id = "command.friend.request.accept", content = "&a&lACCEPT")
	public static MessageProvider FRIEND_REQUEST_ACCEPT;

	@Message(id = "command.friend.request.deny", content = "&c&lDENY")
	public static MessageProvider FRIEND_REQUEST_DENY;

	@Message(id = "command.friend.pending.requests.list", content = {
			"$#friend.prefix &7Your currently pending requests to other players&7:",
			"$input"
	})
	public static MessageProvider FRIEND_PENDING_REQUEST_LIST;

	@Message(id = "command.friend.requests.list", content = {
			"$#friend.prefix &7Your currently friend requests&7:",
			"$input"
	})
	public static MessageProvider FRIEND_REQUESTS_LIST;

	@Message(id =  "command.friend.pending.requests.no", content = "$#friend.prefix &cYou haven't sent any requests to other players!")
	public static MessageProvider FRIEND_PENDING_REQUEST_NO;

	@Message(id =  "command.friend.requests.none", content = "$#friend.prefix &cYou haven't any friend requests!")
	public static MessageProvider FRIEND_NONE_REQUEST;

	@Message(id =  "command.friend.requests.disabled", content = "$#friend.prefix &cThis player disabled friend requests!")
	public static MessageProvider FRIEND_REQUEST_DISABLED;

	@Message(id =  "command.friend.requests.already", content = "$#friend.prefix &cYou sent already a friend requests!")
	public static MessageProvider FRIEND_REQUEST_ALREADY;

	@Message(id =  "command.friend.requests.sent", content = "$#friend.prefix &7You &3sent &7a &cfriend request &7to &b$target&7.")
	public static MessageProvider FRIEND_REQUEST_SENT;

	@Message(id =  "command.friend.requests.sent.target", content = {
			"$#friend.prefix &7You have a new friend request:",
			"$input"
	})
	public static MessageProvider FRIEND_REQUEST_SENT_TARGET;

	@Message(id =  "command.friend.request.notsent", content = "$#friend.prefix &cYou didn't send this player a friend request.")
	public static MessageProvider FRIEND_REQUEST_NOTSENT;

	@Message(id =  "command.friend.request.deleted", content = "$#friend.prefix &cYou deleted the friend request from &4$player&7.")
	public static MessageProvider FRIEND_REQUEST_DELETED;

	@Message(id =  "command.friend.request.deny.notsent", content = "$#friend.prefix &cThis player didn't send you a friend request.")
	public static MessageProvider FRIEND_REQUEST_DENY_NOTSENT;

	@Message(id =  "command.friend.request.deny.removed", content = "$#friend.prefix &7You &3denied &7the friend request from &b$player&7.")
	public static MessageProvider FRIEND_REQUEST_REMOVED;

}
