package net.galacticprojects.bungeecord;

import me.lauriichan.wildcard.systemcore.data.io.message.Message;
import me.lauriichan.wildcard.systemcore.util.Tuple;
import net.galacticprojects.common.util.StringFormatter;
import net.galacticprojects.common.database.model.Ban;
import net.galacticprojects.common.util.TimeHelper;

public class Messages {

    private Messages() {
        throw new UnsupportedOperationException();
    }

    public static final String SERVER_NAME = "GalacticProjects";

    public static final Message PREFIX;

    public static final Message GENERAL_BAN_FORMAT;
    public static final Message GENERAL_BAN_TEXT_JOIN;
    public static final Message GENERAL_BAN_TEXT_KICK;
    public static final Message GENERAL_BAN_TEXT_TIME;
    public static final Message GENERAL_BAN_TEXT_PERMANENT;
    public static final Message GENERAL_BAN_HEADER_TEXT;
    public static final Message GENERAL_BAN_HEADER_FILL;
    public static final Message GENERAL_BAN_SECTION_LEFT;
    public static final Message GENERAL_BAN_SECTION_RIGHT;
    public static final Message GENERAL_BAN_SECTION_REASON;
    public static final Message GENERAL_BAN_SECTION_DURATION;

    public static final Message COMMAND_BAN_UNBAN_UNSUCCESSFUL;
    public static final Message COMMAND_BAN_UNBAN_SUCCESSFUL;
    public static final Message COMMAND_BAN_BANNED_TIME;
    public static final Message COMMAND_BAN_BANNED_PERMANENT;
    public static final Message COMMAND_BAN_ID_NOT_EXIST;

    static {
        PREFIX = Message.register("name", "&8「 &5&lGALACTIC&d&lPROJECTS &8」 &r");

        GENERAL_BAN_FORMAT = Message.register("general.ban.format", new String[] {
                "&8$header",
                "",
                "&7$text",
                "",
                "$#general.ban.section.left $#general.ban.section.duration $#general.ban.section.right",
                "",
                "&7$duration",
                "",
                "$#general.ban.section.left $#general.ban.section.reason $#general.ban.section.right",
                "",
                "&7$reason",
                "",
                "&8$header"
        });
        GENERAL_BAN_TEXT_JOIN = Message.register("general.ban.text.join", "&7Du bist gebannt");
        GENERAL_BAN_TEXT_KICK = Message.register("general.ban.text.kick", "&7Du wurdest gebannt");
        GENERAL_BAN_TEXT_TIME = Message.register("general.ban.text.time", "&7Bis zum $time");
        GENERAL_BAN_TEXT_PERMANENT = Message.register("general.ban.text.permanent", "&4PERMANENT");
        GENERAL_BAN_HEADER_TEXT = Message.register("general.ban.header.text", "&8< &7$#name &8>");
        GENERAL_BAN_HEADER_FILL = Message.register("general.ban.header.fill", "=");
        GENERAL_BAN_SECTION_LEFT = Message.register("general.ban.section.left", "&8<===]");
        GENERAL_BAN_SECTION_RIGHT = Message.register("general.ban.section.right", "&8[===>");
        GENERAL_BAN_SECTION_REASON = Message.register("general.ban.section.reason", "&cGrund");
        GENERAL_BAN_SECTION_DURATION = Message.register("general.ban.section.duration", "&cDauer");
        COMMAND_BAN_UNBAN_UNSUCCESSFUL = Message.register("command.ban.unban.unsuccessful", "$#prefix &c$player &7wurde nicht gebannt");
        COMMAND_BAN_UNBAN_SUCCESSFUL = Message.register("command.ban.unban.successful",
                "$#prefix &7Du hast &c$player &aerfolgreich &7entbannt");
        COMMAND_BAN_BANNED_TIME = Message.register("command.ban.banned.time", "$#prefix &7Du hast &c$player &7bis zum &e$time &7gebannt");
        COMMAND_BAN_BANNED_PERMANENT = Message.register("command.ban.banned.permanent",
                "$#prefix &7Du hast &c$player &4PERMANENT &7gebannt");
        COMMAND_BAN_ID_NOT_EXIST = Message.register("command.ban.id.not.exist", "$#prefix &7Die Ban-Id &e$id &7existiert nicht");
    }

    public static Tuple<String, Object>[] buildBanPlaceholders(Ban ban, boolean kick) {
        return new Tuple[] {
                Tuple.of("header",
                        StringFormatter.centerColored(GENERAL_BAN_HEADER_TEXT.asColoredMessageString(),
                                GENERAL_BAN_HEADER_FILL.asString().charAt(0), 50)),
                Tuple.of("reason", ban.getReason()),
                Tuple.of("duration",
                        ban.isPermanent() ? GENERAL_BAN_TEXT_PERMANENT.asString()
                                : GENERAL_BAN_TEXT_TIME.asMessageString(Tuple.of("time", TimeHelper.BAN_TIME_FORMATTER.format(ban.getTime())))),
                Tuple.of("text", kick ? GENERAL_BAN_TEXT_KICK.asMessageString() : GENERAL_BAN_TEXT_JOIN.asMessageString())
        };
    }

}
