package net.galacticprojects.bungeecord.util;

import me.lauriichan.laylib.localization.MessageManager;
import net.galacticprojects.bungeecord.ProxyPlugin;
import net.galacticprojects.common.CommonPlugin;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.SignStyle;
import java.time.format.TextStyle;
import java.util.HashMap;

import static java.time.temporal.ChronoField.*;

public class TimeHelper {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    public static final DateTimeFormatter BAN_TIME_FORMATTER;

    public static String LANGUAGE = "en-uk";

    private static ProxyPlugin plugin = ProxyPlugin.getInstance();

    private static CommonPlugin commonPlugin = plugin.getCommonPlugin();

    static {
        HashMap<Long, String> dow = new HashMap<>();
        MessageManager manager = commonPlugin.getMessageManager();
        dow.put(1L, manager.translate("timehelper.monday", LANGUAGE));
        dow.put(2L, manager.translate("timehelper.tuesday", LANGUAGE));
        dow.put(3L, manager.translate("timehelper.wednesday", LANGUAGE));
        dow.put(4L, manager.translate("timehelper.thursday", LANGUAGE));
        dow.put(5L, manager.translate("timehelper.friday", LANGUAGE));
        dow.put(6L, manager.translate("timehelper.saturday", LANGUAGE));
        dow.put(7L, manager.translate("timehelper.sunday", LANGUAGE));
        HashMap<Long, String> moy = new HashMap<>();
        moy.put(1L, manager.translate("timehelper.january", LANGUAGE));
        moy.put(2L, manager.translate("timehelper.february", LANGUAGE));
        moy.put(3L, manager.translate("timehelper.march", LANGUAGE));
        moy.put(4L, manager.translate("timehelper.april", LANGUAGE));
        moy.put(5L, manager.translate("timehelper.may", LANGUAGE));
        moy.put(6L, manager.translate("timehelper.june", LANGUAGE));
        moy.put(7L, manager.translate("timehelper.july", LANGUAGE));
        moy.put(8L, manager.translate("timehelper.august", LANGUAGE));
        moy.put(9L, manager.translate("timehelper.september", LANGUAGE));
        moy.put(10L, manager.translate("timehelper.october", LANGUAGE));
        moy.put(11L, manager.translate("timehelper.november", LANGUAGE));
        moy.put(12L, manager.translate("timehelper.december", LANGUAGE));
        BAN_TIME_FORMATTER = new DateTimeFormatterBuilder().parseCaseInsensitive().parseLenient().appendText(DAY_OF_WEEK, dow)
                .appendLiteral(", ").appendValue(DAY_OF_MONTH, 1, 2, SignStyle.NOT_NEGATIVE).appendLiteral(". ")
                .appendText(MONTH_OF_YEAR, moy).appendLiteral(' ').appendValue(YEAR, 4, 16, SignStyle.NOT_NEGATIVE).appendLiteral(" - ").appendValue(HOUR_OF_DAY, 2)
                .appendLiteral(':').appendValue(MINUTE_OF_HOUR, 2).appendLiteral(':').appendValue(SECOND_OF_MINUTE, 2).appendLiteral(" (")
                .appendZoneText(TextStyle.SHORT).appendLiteral(')').toFormatter().withZone(ZoneId.of("Europe/Berlin"));
    }

    public static DateTimeFormatter format(String language) {
        HashMap<Long, String> dow = new HashMap<>();
        MessageManager manager = commonPlugin.getMessageManager();
        dow.put(1L, manager.translate("timehelper.monday", language));
        dow.put(2L, manager.translate("timehelper.tuesday", language));
        dow.put(3L, manager.translate("timehelper.wednesday", language));
        dow.put(4L, manager.translate("timehelper.thursday", language));
        dow.put(5L, manager.translate("timehelper.friday", language));
        dow.put(6L, manager.translate("timehelper.saturday", language));
        dow.put(7L, manager.translate("timehelper.sunday", language));
        HashMap<Long, String> moy = new HashMap<>();
        moy.put(1L, manager.translate("timehelper.january", language));
        moy.put(2L, manager.translate("timehelper.february", language));
        moy.put(3L, manager.translate("timehelper.march", language));
        moy.put(4L, manager.translate("timehelper.april", language));
        moy.put(5L, manager.translate("timehelper.may", language));
        moy.put(6L, manager.translate("timehelper.june", language));
        moy.put(7L, manager.translate("timehelper.july", language));
        moy.put(8L, manager.translate("timehelper.august", language));
        moy.put(9L, manager.translate("timehelper.september", language));
        moy.put(10L, manager.translate("timehelper.october", language));
        moy.put(11L, manager.translate("timehelper.november", language));
        moy.put(12L, manager.translate("timehelper.december", language));
        return new DateTimeFormatterBuilder().parseCaseInsensitive().parseLenient().appendText(DAY_OF_WEEK, dow)
                .appendLiteral(", ").appendValue(DAY_OF_MONTH, 1, 2, SignStyle.NOT_NEGATIVE).appendLiteral(". ")
                .appendText(MONTH_OF_YEAR, moy).appendLiteral(' ').appendValue(YEAR, 4, 16, SignStyle.NOT_NEGATIVE).appendLiteral(" - ").appendValue(HOUR_OF_DAY, 2)
                .appendLiteral(':').appendValue(MINUTE_OF_HOUR, 2).appendLiteral(':').appendValue(SECOND_OF_MINUTE, 2).appendLiteral(" (")
                .appendZoneText(TextStyle.SHORT).appendLiteral(')').toFormatter().withZone(ZoneId.of("UTC"));
    }

    private TimeHelper() {
        throw new UnsupportedOperationException();
    }

    public static OffsetDateTime fromString(final String string) {
        return string == null ? null : OffsetDateTime.parse(string, FORMATTER);
    }

    public static String toString(final OffsetDateTime time) {
        return time == null ? null : FORMATTER.format(time);
    }
}