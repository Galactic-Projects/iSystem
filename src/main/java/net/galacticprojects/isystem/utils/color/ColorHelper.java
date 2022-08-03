package net.galacticprojects.isystem.utils.color;

import com.syntaxphoenix.syntaxapi.utils.java.lang.StringBuilder;
import de.syntaxtnt.color.helper.ColorIterator;
import de.syntaxtnt.color.helper.LinearGradient;

public class ColorHelper {
    public static final String HEX_FORMAT = "%02X";

    public static String toHexColor(final float[] color) {
        final StringBuilder builder = new StringBuilder("#");
        builder.append(String.format("%02X", Integer.valueOf(Math.min(255, Math.abs(Math.round(color[0] * 255.0F))))));
        builder.append(String.format("%02X", Integer.valueOf(Math.min(255, Math.abs(Math.round(color[1] * 255.0F))))));
        builder.append(String.format("%02X", Integer.valueOf(Math.min(255, Math.abs(Math.round(color[2] * 255.0F))))));
        return builder.toStringClear();
    }

    public static String hexToMinecraftColor(String hexColor) {
        if (hexColor.startsWith("#")) {
            hexColor = hexColor.replace("#", "");
        }
        if (hexColor.length() < 6) {
            return null;
        }
        final StringBuilder builder = new StringBuilder("");
        final char[] chars = hexColor.toCharArray();
        for (final char character : chars) {
            builder.append('§').append(character);
        }
        return builder.toStringClear();
    }

    public static float[] fromHexColor(String color) {
        final float[] output = new float[3];
        if (color.startsWith("#")) {
            color = color.replace("#", "");
        }
        if (color.length() < 6) {
            return output;
        }
        output[0] = Integer.parseInt(color.substring(0, 2), 16) / 255.0F;
        output[1] = Integer.parseInt(color.substring(2, 4), 16) / 255.0F;
        output[2] = Integer.parseInt(color.substring(4, 6), 16) / 255.0F;
        return output;
    }

    public static String color(final String value, final String hexColor0, final String hexColor1) {
        final StringBuilder builder = new StringBuilder();
        final char[] chars = value.toCharArray();
        final LinearGradient gradient = new LinearGradient(fromHexColor(hexColor0), fromHexColor(hexColor1), chars.length);
        for (final char character : chars) {
            if (gradient.hasNext()) {
                builder.append(hexToMinecraftColor(gradient.next()));
            }
            builder.append(character);
        }
        return builder.toStringClear();
    }

    public static String color(final String value, final String hexColor) {
        final StringBuilder builder = new StringBuilder();
        final char[] chars = value.toCharArray();
        final LinearGradient gradient = new LinearGradient(fromHexColor(hexColor), fromHexColor(hexColor), chars.length);
        for (final char character : chars) {
            if (gradient.hasNext()) {
                builder.append(hexToMinecraftColor(gradient.next()));
            }
            builder.append(character);
        }
        return builder.toStringClear();
    }

    public static float interpolate(final float ratio, final float color0, final float color1) {
        return color0 * ratio + color1 * (1.0F - ratio);
    }

    public static ColorIterator linearGradient(final String hexColor0, final String hexColor1, final int steps) {
        return new LinearGradient(fromHexColor(hexColor0), fromHexColor(hexColor1), steps);
    }

    public static ColorIterator linearGradient(final String hexColor, final int steps) {
        return new LinearGradient(fromHexColor(hexColor), fromHexColor(hexColor), steps);
    }
}
