package me.lauriichan.minecraft.wildcard.migration;

import java.text.DecimalFormat;

public class Date {

    private static final String FORMAT = "%s%s%s%s%s";
    private static final DecimalFormat NUMBER_FORMAT = new DecimalFormat("00");

    public Date() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static long of(final int hour, final int minute, final int day, final int month, final int year) {
        return Long.parseLong(String.format(FORMAT, Math.max(year, 2000), NUMBER_FORMAT.format(Math.max(Math.min(month, 12), 1)),
                NUMBER_FORMAT.format(Math.max(Math.min(day, 31), 1)), NUMBER_FORMAT.format(Math.max(Math.min(hour, 23), 0)),
                NUMBER_FORMAT.format(Math.max(Math.min(minute, 59), 0))));
    }

}
