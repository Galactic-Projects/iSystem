package net.galacticprojects.bungeecord.message;

import me.lauriichan.laylib.localization.MessageProvider;
import me.lauriichan.laylib.localization.source.Message;

public class SystemMessage {

    private SystemMessage() {
        throw new UnsupportedOperationException();
    }

    @Message(id = "system.maintenance.version", content = "&7► &4&lMAINTENANCE")
    public static MessageProvider SYSTEM_MAINTENANCE_VERSION;
    @Message(id = "system.maintenance.1", content = "Not yet set")
    public static MessageProvider SYSTEM_MAINTENANCE_1;
    @Message(id = "system.maintenance.2", content = "Not yet set")
    public static MessageProvider SYSTEM_MAINTENANCE_2;
    @Message(id = "system.motd.1", content = "Not yet set")
    public static MessageProvider SYSTEM_MOTD_1;
    @Message(id = "system.motd.2", content = "Not yet set")
    public static MessageProvider SYSTEM_MOTD_2;

    @Message(id = "system.maintenance.kick.currently", content = {
            "&8「 &5&lGALACTIC&d&lPROJECTS &8」&r",
            "&7The &bnetwork &7is &3currently &7in &cmaintenance mode&7.",
            "&7",
            "&7► &cReason &8■ &4$reason",
            "&7► &cEnd Date &8■ &4$enddate",
            "",
            "&7Want to contact us?",
            "&7► &dWebsite &8■ &5www.GalacticProjects.net"
    })
    public static MessageProvider SYSTEM_MAINTENACE_KICK_CURRENTLY;

    @Message(id = "system.maintenance.kick.now", content = {
            "&8「 &5&lGALACTIC&d&lPROJECTS &8」&r",
            "&7The &bnetwork &7is &3now &7in &cmaintenance mode&7.",
            "&7",
            "&7► &cReason &8■ &4$reason",
            "&7► &cEnd Date &8■ &4$enddate",
            "",
            "&7Want to contact us?",
            "&7► &dWebsite &8■ &5www.GalacticProjects.net"
    })
    public static MessageProvider SYSTEM_MAINTENACE_KICK_NOW;


    @Message(id = "system.service.full", content = {
            "&8「 &5&lGALACTIC&d&lPROJECTS &8」&r",
            "&cOur network is full. ",
            "&7",
            "&cPlease wait until you have a chance or buy a higher rank",
            "at our store (at the lobby or web) to get access to our server at any time!"
    })
    public static MessageProvider SYSTEM_SERVICE_FULL;

    @Message(id = "system.service.fulltotally", content = {
            "&8「 &5&lGALACTIC&d&lPROJECTS &8」&r",
            "&cOur network is absolutely full. ",
            "&7",
            "&cYou must be a team member to get access to absolutely full network.",
    })
    public static MessageProvider SYSTEM_SERVICE_ABSOLUTFULL;

    @Message(id = "system.service.fulltotally", content = {
            "&8「 &5&lGALACTIC&d&lPROJECTS &8」&r",
            "&cYou have been kicked from our network!",
            "&7",
            "&cTo ensure the security of the network, you have been kicked to make space for a teammate!",
    })
    public static MessageProvider SYSTEM_SERVICE_MAKESPACE;

    @Message(id = "system.maintenance.kick.reason", content = "Maintenance")
    public static MessageProvider SYSTEM_MAINTENANCE_KICK_REASON;


    @Message(id = "system.tablist.header", content = {
            " &7 ",
            " &8「 &5&lGALACTIC&d&lPROJECTS &7■ &fYour &bnetwork &fwith &dgalactical &5power &8」",
            " &8► &fConnected to &b$server &7■ &fCurrently online &b$online&f/&3$max &8◄ ",
            " &7 "
    })
    public static MessageProvider SYSTEM_TABLIST_HEADER;

    @Message(id = "system.tablist.footer", content = {
            " &7 ",
            " &8► &fWebsite &7× &dwww.galacticalprojects.net &8■ &fForum &7× &dforum.galacticalprojects.net &8◄ ",
            " &8► &fShop &7× &dshop.galacticalprojects.net &8◄ ",
            " &7"
    })
    public static MessageProvider SYSTEM_TABLIST_FOOTER;
    @Message(id = "system.party.action.kick", content = "he will be kicked out of the party")
    public static MessageProvider SYSTEM_PARTY_ACTION_KICK;
    @Message(id = "system.party.action.kick", content = "the party will be deleted")
    public static MessageProvider SYSTEM_PARTY_ACTION_DELETE;
    @Message(id = "system.party.action.kick", content = "Leader")
    public static MessageProvider SYSTEM_PARTY_LEADER;
    @Message(id = "system.party.action.kick", content = "Moderator")
    public static MessageProvider SYSTEM_PARTY_MODERATOR;
    @Message(id = "system.party.action.kick", content = "Member")
    public static MessageProvider SYSTEM_PARTY_MEMBER;
    @Message(id = "system.party.member.leave", content = {
            " $party.prefix ",
            "&7The $member $player left the Server",
            "He/She has now 5 minutes time otherwise $action",
            " $party.prefix "
    })
    public static MessageProvider SYSTEM_PARTY_MEMBER_LEAVE;

    @Message(id = "system.party.action.rejoined", content = "$party.prefix &7$member $player $7has rejoined, party won't be deleted!")
    public static MessageProvider SYSTEM_PARTY_REJOINED;

    @Message(id = "system.no.permission", content = "You do not have permission to execute this command!")
    public static MessageProvider SYSTEM_NO_PERMISSION;

    @Message(id = "verify.prefix", content = "&8&l「 &5&lVERIFY &8&l」&7")
    public static MessageProvider VERIFY_PREFIX;

    @Message(id = "verify.code", content = "$#verify.prefix &7Your &bverification code &7is &3$code&7.")
    public static MessageProvider VERIFY_CODE;

    @Message(id = "verify.error.delay", content = "$#verify.prefix &cAn error occurred while trying to verify. Please rejoin or contact a staff member!")
    public static MessageProvider VERIFY_ERROR;
    @Message(id = "verify.error.delay", content = "$#verify.prefix &cUnfortunately, you did not succeed in entering the verification code within the one minute time limit.")
    public static MessageProvider VERIFY_DELAY;

    @Message(id = "verify.error.wrong", content = "$#verify.prefix &cThe code is wrong. Please try again!")
    public static MessageProvider VERIFY_WRONG;

    @Message(id = "verify.success", content = "$#verify.prefix &7You have &asuccessfully &3verified &7that you are not a &brobot&7.")
    public static MessageProvider VERIFY_SUCCESS;
}
