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
	@Message(id = "command.maintenance.already.off", content = "$#maintenance.prefix &cThe maintenance mode is already disabled!")
	public static MessageProvider COMMAND_MAINTENANCE_ALREADY_OFF;
	@Message(id = "command.maintenance.on", content = "$#maintenance.prefix &7The &4maintenace mode &7is now &aenabled&7 with &4reason &c$reason&7.")
	public static MessageProvider COMMAND_MAINTENANCE_ON;
	@Message(id = "command.maintenance.already.on", content = "$#maintenance.prefix &cThe maintenance mode is already enabled!")
	public static MessageProvider COMMAND_MAINTENANCE_ALREADY_ON;


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

	@Message(id = "command.onlinetime.success.self", content = "$#plugin.prefix Your &bonline-time &7is &c$daystime Day(s)&7, &c$hourtime Hour(s)&7 and &c$minutetime Minute(s)&7.")
	public static MessageProvider COMMAND_ONLINETIME_SUCCESS;

	@Message(id = "command.onlinetime.success.others", content = "$#plugin.prefix The &bonline-time &7by &b$player &7is &c$daystime Day(s)&7, &c$hourtime Hour(s)&7 and &c$minutetime Minute(s)&7.")
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
	@Message(id = "command.teamchat.left", content = "$#teamchat.prefix &7You &cleft &7the &bteamchat&7.")
	public static MessageProvider COMMAND_TEAMCHAT_LEFT;
	@Message(id = "command.teamchat.join", content = "$#teamchat.prefix &7You &aentered &7the &bteamchat&7.")
	public static MessageProvider COMMAND_TEAMCHAT_JOIN;

	@Message(id = "command.teamchat.format", content = "$#teamchat.prefix &b$player &7» &f$message")
	public static MessageProvider COMMAND_TEAMCHAT_FORMAT;

	@Message(id = "friend.prefix", content = "&8&l「 &e&lFRIEND &8&l」&7")
	public static MessageProvider FRIEND_PREFIX;

	@Message(id = "command.friend.toggle.requests.off", content = "$#friend.prefix &7You &asuccessfully &7switched your &4friend requests &7to &coff&7.")
	public static MessageProvider FRIEND_TOGGLE_REQUESTS_OFF;
	@Message(id = "command.friend.toggle.requests.on", content = "$#friend.prefix &7You &asuccessfully &7switched your &4friend requests &7to &con&7.")
	public static MessageProvider FRIEND_TOGGLE_REQUESTS_ON;
	@Message(id = "command.friend.toggle.messages.off", content = "$#friend.prefix &7You &asuccessfully &7switched your &4friend private messages &7to &coff&c.")
	public static MessageProvider FRIEND_TOGGLE_MESSAGES_OFF;
	@Message(id = "command.friend.toggle.messages.on", content = "$#friend.prefix &7You &asuccessfully &7switched your &4friend private messages &7to &con&7.")
	public static MessageProvider FRIEND_TOGGLE_MESSAGES_ON;

	@Message(id = "command.friend.toggle.jump.off", content = "$#friend.prefix &7You &asuccessfully &7switched your &4friend jump &7to &coff&7.")
	public static MessageProvider FRIEND_TOGGLE_JUMP_OFF;
	@Message(id = "command.friend.toggle.jump.on", content = "$#friend.prefix &7You &asuccessfully &7switched your &4friend jump &7to &con&7.")
	public static MessageProvider FRIEND_TOGGLE_JUMP_ON;

	@Message(id = "command.friend.request.accept", content = "&a&lACCEPT")
	public static MessageProvider FRIEND_REQUEST_ACCEPT;

	@Message(id = "command.friend.request.deny", content = "&c&lDENY")
	public static MessageProvider FRIEND_REQUEST_DENY;

	@Message(id = "command.friend.pending.requests.list", content = "$#friend.prefix &7Your currently pending requests to other players&7:")
	public static MessageProvider FRIEND_PENDING_REQUEST_LIST;

	@Message(id = "command.friend.requests.list", content = "$#friend.prefix &7Your currently friend requests&7:")
	public static MessageProvider FRIEND_REQUESTS_LIST;

	@Message(id = "command.friend.pending.requests.no", content = "$#friend.prefix &cYou didn't send any requests to other players!")
	public static MessageProvider FRIEND_PENDING_REQUEST_NO;

	@Message(id = "command.friend.requests.none", content = "$#friend.prefix &cYou didn't any friend requests!")
	public static MessageProvider FRIEND_NONE_REQUEST;

	@Message(id = "command.friend.requests.disabled", content = "$#friend.prefix &cThis player disabled friend requests!")
	public static MessageProvider FRIEND_REQUEST_DISABLED;

	@Message(id = "command.friend.requests.already", content = "$#friend.prefix &cYou already sent a friend requests!")
	public static MessageProvider FRIEND_REQUEST_ALREADY;

	@Message(id = "command.friend.requests.sent", content = "$#friend.prefix &7You &3sent &7a &cfriend request &7to &b$target&7.")
	public static MessageProvider FRIEND_REQUEST_SENT;

	@Message(id = "command.friend.requests.sent.both", content = "$#friend.prefix &7You both &3sended &7a &cfriend request&7, now you are &bfriends&7.")
	public static MessageProvider FRIEND_BOTH_SENDED;

	@Message(id = "command.friend.requests.sent.target", content = "$#friend.prefix &7You have a new friend request:")
	public static MessageProvider FRIEND_REQUEST_SENT_TARGET;

	@Message(id = "command.friend.request.notsent", content = "$#friend.prefix &cYou didn't send this player a friend request.")
	public static MessageProvider FRIEND_REQUEST_NOTSENT;

	@Message(id = "command.friend.request.deleted", content = "$#friend.prefix &7You &3deleted &7the &cfriend request &7from &b$player&7.")
	public static MessageProvider FRIEND_REQUEST_DELETED;

	@Message(id = "command.friend.request.deleted.all", content = "$#friend.prefix &7You &3deleted &ball &7your &cfriend requests&7.")
	public static MessageProvider FRIEND_REQUEST_DELETED_ALL;

	@Message(id = "command.friend.request.norequests", content = "$#friend.prefix &cYou haven't any friend requests!")
	public static MessageProvider FRIEND_REQUEST_NO_REQUESTS;

	@Message(id = "command.friend.request.deny.notsent", content = "$#friend.prefix &cThis player didn't send you a friend request.")
	public static MessageProvider FRIEND_REQUEST_DENY_NOTSENT;

	@Message(id = "command.friend.request.deny.removed", content = "$#friend.prefix &7You &3denied &7the friend request from &b$player&7.")
	public static MessageProvider FRIEND_REQUEST_REMOVED;

	@Message(id = "command.friend.request.accept.single", content = "$#friend.prefix &7You &3accepted &7the &cfriend requests&7 from &b$player&7.")
	public static MessageProvider FRIEND_REQUEST_ACCEPT_SINGLE;

	@Message(id = "command.friend.request.accept.target", content = "$#friend.prefix &7&b$player&7 have &3accepted &7your &cfriend request&7.")
	public static MessageProvider FRIEND_REQUEST_ACCEPT_TARGET;

	@Message(id = "command.friend.request.accept.all", content = "$#friend.prefix &7You &3accepted &ball &7your &cfriend requests&7.")
	public static MessageProvider FRIEND_REQUEST_ACCEPT_ALL;

	@Message(id = "command.friend.main.already", content = "$#friend.prefix &cThis player is already your friend!")
	public static MessageProvider FRIEND_ALREADY;

	@Message(id = "command.friend.main.none", content = "$#friend.prefix &cThis player isn't your friend!")
	public static MessageProvider FRIEND_NONE;

	@Message(id = "command.friend.main.list.none", content = "$#friend.prefix &cYou haven't any friends!")
	public static MessageProvider FRIEND_NONE_LIST;

	@Message(id = "command.friend.main.deleted", content = "$#friend.prefix &7You &3deleted &7the &cfriendship &7with &b$player&7.")
	public static MessageProvider FRIEND_DELETE;

	@Message(id = "command.friend.main.list.cleared.accept", content = "$#friend.prefix &7Your &cfriend list &3has been cleared &7by &3yourself&7.")
	public static MessageProvider FRIEND_LIST_CLEARED;

	@Message(id = "command.friend.main.list.cleared.deny", content = "$#friend.prefix &cYou canceled to want clear your &4friend list&c.")
	public static MessageProvider FRIEND_LIST_CLEARED_DENY;

	@Message(id = "command.friend.main.list", content = "$#friend.prefix &7Your friends:")
	public static MessageProvider FRIEND_LIST;

	@Message(id = "coins.prefix", content = "&8&l「 &9&lCOINS &8&l」&7")
	public static MessageProvider COINS_PREFIX;

	@Message(id = "command.coins.errors.negative", content = "$#coins.prefix &cThe amount must be over zero!")
	public static MessageProvider COINS_ERRORS_NEGATIVE;

	@Message(id = "command.coins.errors.enough", content = "$#coins.prefix &cNot enough coins!")
	public static MessageProvider COINS_ERRORS_ENOUGH;

	@Message(id = "command.coins.errors.already", content = "$#coins.prefix &cThis player has already the amount that you want to set!")
	public static MessageProvider COINS_ERRORS_ALREADY;
	@Message(id = "command.coins.errors.number", content = "$#coins.prefix &cFormat must be a number!")
	public static MessageProvider COINS_ERRORS_NUMBER;

	@Message(id = "command.coins.errors.small", content = "$#level.prefix &cYou can't remove this amount from the player coins, because it will be smaller than 0!")
	public static MessageProvider COINS_ERRORS_SMALL;

	@Message(id = "command.coins.errors.number", content = "$#coins.prefix &cYou can't send yourself coins!")
	public static MessageProvider COINS_ERRORS_SELF;

	@Message(id = "command.coins.name", content = "Galactic Credits")
	public static MessageProvider COINS_NAME;

	@Message(id = "command.coins.show.single", content = "$#coins.prefix &7Your &3coins &7are &b$input $#command.coins.name&7.")
	public static MessageProvider COINS_SHOW;

	@Message(id = "command.coins.show.others", content = "$#coins.prefix &7The &3coins &7by &c$player &7is &b$input $#command.coins.name&7.")
	public static MessageProvider COINS_SHOW_OTHERS;

	@Message(id = "command.coins.pay.sent.player", content = "$#coins.prefix &7You &3sent &c$player &b$amount $#command.coins.name&7.")
	public static MessageProvider COINS_OTHERS_SENT_PLAYER;

	@Message(id = "command.coins.pay.sent.target", content = "$#coins.prefix &b$player &csent &7you &3$amount $#command.coins.name&7.")
	public static MessageProvider COINS_OTHERS_SENT_TARGET;

	@Message(id = "command.coins.set.player", content = "$#coins.prefix &7You &7set &b$player &7a new amount of &ccoins &7from &3$oldamount &7to &3$amount $#command.coins.name&7.")
	public static MessageProvider COINS_OTHERS_SET_PLAYER;

	@Message(id = "command.coins.set.target", content = "$#coins.prefix &b$player &7set you a new amount of &ccoins &7from &3$oldamount &7to &3$amount $#command.coins.name&7.")
	public static MessageProvider COINS_OTHERS_SET_TARGET;

	@Message(id = "command.coins.add.player", content = "$#coins.prefix &7You &7added &b$player &7an amount of &ccoins &7from &3$oldamount &7to &3$amount $#command.coins.name&7.")
	public static MessageProvider COINS_OTHERS_ADD_PLAYER;

	@Message(id = "command.coins.add.target", content = "$#coins.prefix &b$player &7added you an amount of &ccoins &7from &3$oldamount &7to &3$amount $#command.coins.name&7.")
	public static MessageProvider COINS_OTHERS_ADD_TARGET;

	@Message(id = "command.coins.remove.player", content = "$#coins.prefix &7You &7removed &b$player &7an amount of &ccoins &7from &3$oldamount &7to &3$amount $#command.coins.name&7.")
	public static MessageProvider COINS_OTHERS_REMOVE_PLAYER;

	@Message(id = "command.coins.remove.target", content = "$#coins.prefix &b$player &7removed you an amount of &ccoins &7from &3$oldamount &7to &3$amount $#command.coins.name&7.")
	public static MessageProvider COINS_OTHERS_REMOVE_TARGET;

	@Message(id = "level.prefix", content = "&8&l「 &5&lLEVEL &8&l」&7")
	public static MessageProvider LEVEL_PREFIX;

	@Message(id = "command.level.errors.negative", content = "$#level.prefix &cThe amount must be over zero!")
	public static MessageProvider LEVEL_ERRORS_NEGATIVE;

	@Message(id = "command.level.errors.already", content = "$#level.prefix &cThis player has already the amount that you want to set!")
	public static MessageProvider LEVEL_ERRORS_ALREADY;

	@Message(id = "command.level.errors.number", content = "$#level.prefix &cFormat must be a number!")
	public static MessageProvider LEVEL_ERRORS_NUMBER;

	@Message(id = "command.level.errors.small", content = "$#level.prefix &cYou can't remove this amount from the player level, because it will be smaller than 1!")
	public static MessageProvider LEVEL_ERRORS_SMALL;

	@Message(id = "command.level.errors.big", content = "$#level.prefix &cYou can't give yourself a level that is over Level 100!")
	public static MessageProvider LEVEL_ERRORS_BIG;

	@Message(id = "command.level.show.single", content = "$#level.prefix &7Your &3level &7are &bLevel $input&7.")
	public static MessageProvider LEVEL_SHOW;

	@Message(id = "command.level.show.others", content = "$#level.prefix &7The &3level &7by &c$player &7is &bLevel $input&7.")
	public static MessageProvider LEVEL_SHOW_OTHERS;

	@Message(id = "command.level.set.player", content = "$#level.prefix &7You &7set &b$player &7a new &cLevel &7from &3$oldamount &7to &3$amount Level&7.")
	public static MessageProvider LEVEL_OTHERS_SET_PLAYER;

	@Message(id = "command.level.set.target", content = "$#level.prefix &b$player &7set you a new &clevel &7from &3$oldamount &7to &3Level $amount&7.")
	public static MessageProvider LEVEL_OTHERS_SET_TARGET;

	@Message(id = "command.level.add.player", content = "$#level.prefix &7You &7added &b$player &7a &clevel &7from &3$oldamount &7to &3Level $amount&7.")
	public static MessageProvider LEVEL_OTHERS_ADD_PLAYER;

	@Message(id = "command.level.add.target", content = "$#level.prefix &b$player &7added you a &clevel &7from &3$oldamount &7to &3$amount Level&7.")
	public static MessageProvider LEVEL_OTHERS_ADD_TARGET;

	@Message(id = "command.level.remove.player", content = "$#level.prefix &7You &7removed &b$player &7a &clevel &7from &3$oldamount &7to &3Level $amount&7.")
	public static MessageProvider LEVEL_OTHERS_REMOVE_PLAYER;

	@Message(id = "command.level.remove.target", content = "$#level.prefix &b$player &7removed you a &clevel &7from &3$oldamount &7to &3Level $amount&7.")
	public static MessageProvider LEVEL_OTHERS_REMOVE_TARGET;

	@Message(id = "ping.prefix", content = "&8&l「 &3&lPING &8&l」&7")
	public static MessageProvider PING_PREFIX;

	@Message(id = "command.ping.online", content = "$#ping.prefix &cPlayer isn't online.")
	public static MessageProvider PING_ONLINE;

	@Message(id = "command.ping.self", content = "$#ping.prefix &7Your &3ping &7is &b$input ms&7.")
	public static MessageProvider PING_SELF;

	@Message(id = "command.ping.others", content = "$#ping.prefix &7The &3ping &7by &c$player &7is &b$input ms&7.")
	public static MessageProvider PING_OTHERS;

	@Message(id = "command.broadcast.prefix", content = "&8「 &5&lGALACTIC&d&lPROJECTS &8」&b&l")
	public static MessageProvider BROADCAST_PREFIX;

	@Message(id = "command.broadcast.format", content = {
			"&7",
			"$#command.broadcast.prefix $message",
			"&7"
	})
	public static MessageProvider BROADCAST_COMMAND;

	@Message(id = "system.necessary.network.restart.almost", content = {
			"&8「 &5&lGALACTIC&d&lPROJECTS &8」 &4&lIMPORTANT NOTIFICATION",
			"",
			"&cOur &4network &cwill be &4automatically restart &cin",
			"&c➥ $hour Hour(s), $minute Minute(s), $second Second(s)",
			"",
			"&8「 &5&lGALACTIC&d&lPROJECTS &8」 &4&lIMPORTANT NOTIFICATION",
	})
	public static MessageProvider SYSTEM_NETWORK_RESTART_ALMOST;

	@Message(id = "system.necessary.network.restart.now", content = {
			"&8「 &5&lGALACTIC&d&lPROJECTS &8」 &4&lIMPORTANT NOTIFICATION",
			"",
			"&cOur &4network &cwill be &4automatically restart &cnow.",
			"",
			"&8「 &5&lGALACTIC&d&lPROJECTS &8」 &4&lIMPORTANT NOTIFICATION",
	})
	public static MessageProvider SYSTEM_NETWORK_RESTART_NOW;

	@Message(id = "prefix.history", content = "&8&l「 &9&lHISTORY &8&l」&7")
	public static MessageProvider PREFIX_HISTORY;

	@Message(id = "command.history.nohistory", content = "$#prefix.history &cThis player hasn't ever had a history!")
	public static MessageProvider COMMAND_HISTORY_NOHISTORY;

	@Message(id = "command.history.formatprefix", content = "$#prefix.history &6The history by&e $player &6is:")
	public static MessageProvider COMMAND_HISTORY_FORMATPREFIX;

	@Message(id = "command.history.format", content = "&7» &eID &8■ &c$id &7❘ &eSender &8■ &c$staff &7❘ &eType &8■ &c$type &7❘ &eReason &8■ &c$reason &7❘ &eTime &8■ &c$time &7❘ &eCreated at &8■ &c$creationTime")
	public static MessageProvider COMMAND_HISTORY_FORMAT;

	@Message(id = "command.history.deleted", content = "$#prefix.history &7You &asuccessfully &bdeleted &7the &3history &7by &c$player&7.")
	public static MessageProvider COMMAND_HISTORY_DELETED;

	@Message(id = "prefix.report", content = "&8&l「 &4&lREPORT &8&l」&7")
	public static MessageProvider PREFIX_REPORT;

	@Message(id = "command.report.errors.already", content = "$#prefix.report &cThis report is already being processed!")
	public static MessageProvider REPORT_ERRORS_ALREADY;

	@Message(id = "command.report.errors.offline", content = "$#prefix.report &cThis player has gone offline!")
	public static MessageProvider REPORT_ERRORS_GONE;

	@Message(id = "command.report.errors.processed", content = "$#prefix.report &cThis report wasn't processed!")
	public static MessageProvider REPORT_ERRORS_PROCESSED;

	@Message(id = "command.report.errors.reason", content = "$#prefix.report &cThis isn't a reason!")
	public static MessageProvider REPORT_ERRORS_REASON;

	@Message(id = "command.report.errors.online", content = "$#prefix.report &cThis player isn't online!")
	public static MessageProvider REPORT_ERRORS_ONLINE;

	@Message(id = "command.report.reported", content = "$#prefix.report &7You &asuccessfully &3reported &b$player$&7.")
	public static MessageProvider REPORT_SUCCESSFULLY;

	@Message(id = "command.report.teleport", content = "&a&lTELEPORT")
	public static MessageProvider REPORT_TELEPORT;

	@Message(id = "command.report.ignore", content = "&c&lIGNORE")
	public static MessageProvider REPORT_IGNORE;

	@Message(id = "command.report.teamformat", content = {
			"$#prefix.report &6New Report: ",
			"&8» &ePlayer &8■ &c$player &7❘ &eCreator &8■ &c$creator &7❘ &eReason &8■ &c$reason"
	})
	public static MessageProvider REPORT_TEAMFORMAT;

	@Message(id = "command.report.commandline", content = "&8» &a$accept &7❘ &c$deny" )
	public static MessageProvider REPORT_COMMANDLINE;

	@Message(id = "command.report.process.finish", content = "$#prefix.report &aYou are processing the report from &2$player&a. If you are finished, press $button&a." )
	public static MessageProvider REPORT_PROCESS_FINISHED;

	@Message(id = "command.report.process.finishbutton", content = "&2[CLOSE REPORT]" )
	public static MessageProvider REPORT_PROCESS_FINISHBUTTON;

	@Message(id = "command.report.process.closed", content = "$#prefix.report &7Your &creport &7of &b$player &7has been &3closed&7." )
	public static MessageProvider REPORT_PROCESS_CLOSED;

	@Message(id = "command.report.errors.connected", content = "$#prefix.report &cYou are on the same server as the suspect." )
	public static MessageProvider REPORT_ERRORS_CONNECTED;

	@Message(id = "prefix.system", content = "&8&l「 &1&lSYSTEM &8&l」&7")
	public static MessageProvider PREFIX_SYSTEM;

	@Message(id = "command.administration.config.reloaded", content = "$#prefix.system &7Successfully &3reloaded &bconfiguration&7.")
	public static MessageProvider SYSTEM_ADMINISTRATION_CONFIG_RELOADED;

	@Message(id = "command.administration.errors.already", content = "$#prefix.system &cYour amount is already that amount!")
	public static MessageProvider SYSTEM_ADMINISTRATION_ERRORS_ALREADY;

	@Message(id = "command.administration.errors.small", content = "$#prefix.system &cThe player amount is too big for this amount, that you want to set!")
	public static MessageProvider SYSTEM_ADMINISTRATION_ERRORS_SMALL;

	@Message(id = "command.administration.errors.same", content = "$#prefix.system &cThe player amount is the same as the max-players amount!")
	public static MessageProvider SYSTEM_ADMINISTRATION_ERRORS_SAME;

	@Message(id = "command.administration.errors.realistic", content = "$#prefix.system &cPlease be realistic!")
	public static MessageProvider SYSTEM_ADMINISTRATION_ERRORS_REALISTIC;

	@Message(id = "command.administration.success.setted", content = "$#prefix.system &7You &asuccessfully &3set &cplayers-max amount &7from &b$oamount &7to &b$namount&7.")
	public static MessageProvider SYSTEM_ADMINISTRATION_SUCCESS_SETTED;
}
