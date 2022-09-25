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
}
