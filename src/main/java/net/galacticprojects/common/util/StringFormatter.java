package net.galacticprojects.common.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import com.syntaxphoenix.syntaxapi.utils.java.lang.StringBuilder;
import me.lauriichan.wildcard.systemcore.util.BukkitColor;

public class StringFormatter {

    public static String[] splitToSentencesColored(final String value, final int length) {
        final Iterator<String[]> iterator = Arrays.<String>stream(value.split(" ")).map(map -> {
            (new String[1])[0] = map;
            return map.contains("\n") ? map.split("\n") : new String[1];
        }).iterator();
        final StringBuilder builder = new StringBuilder();
        final ArrayList<String> sentences = new ArrayList<>();
        int current = 0;
        while (iterator.hasNext()) {
            final String[] words = iterator.next();
            for (int index = 0; index < words.length; index++) {
                final String word = words[index];
                final int wordLength = BukkitColor.strip(word).length();
                if (current + wordLength > length) {
                    sentences.add(builder.toStringClear());
                    current = 0;
                }
                if (index != 0) {
                    sentences.add(builder.toStringClear());
                    current = 0;
                }
                if (current != 0) {
                    builder.append(' ');
                    current++;
                }
                builder.append(word);
                current += wordLength;
            }
        }
        if (builder.length() != 0) {
            sentences.add(builder.toStringClear());
        }
        final String[] array = sentences.<String>toArray(new String[sentences.size()]);
        sentences.clear();
        for (final String line : array) {
            int lineLength;
            if ((lineLength = BukkitColor.strip(line).length()) <= 50) {
                sentences.add(line);
            } else {
                final char[] chars = line.toCharArray();
                int index = 0;
                while (lineLength > 50) {
                    final char[] data = new char[50];
                    System.arraycopy(chars, index, data, 0, data.length);
                    sentences.add(new String(data));
                    index += 50;
                    lineLength -= 50;
                }
                if (lineLength != 0) {
                    final char[] data = new char[lineLength];
                    System.arraycopy(chars, index, data, 0, data.length);
                    sentences.add(new String(data));
                }
            }
        }
        return sentences.<String>toArray(new String[sentences.size()]);
    }

    public static String splitToSentencesColoredAsString(final String value, final int length) {
        return String.join("\n", splitToSentencesColored(value, length));
    }

    public static String[] splitToSentences(final String value, final int length) {
        final Iterator<String[]> iterator = Arrays.<String>stream(value.split(" ")).map(map -> {
            (new String[1])[0] = map;
            return map.contains("\n") ? map.split("\n") : new String[1];
        }).iterator();
        final StringBuilder builder = new StringBuilder();
        final ArrayList<String> sentences = new ArrayList<>();
        int current = 0;
        while (iterator.hasNext()) {
            final String[] words = iterator.next();
            for (int index = 0; index < words.length; index++) {
                final String word = words[index];
                final int wordLength = word.length();
                if (current + wordLength > length) {
                    sentences.add(builder.toStringClear());
                    current = 0;
                }
                if (index != 0) {
                    sentences.add(builder.toStringClear());
                    current = 0;
                }
                if (current != 0) {
                    builder.append(' ');
                    current++;
                }
                builder.append(word);
                current += wordLength;
                if (index != 0) {
                    sentences.add(builder.toStringClear());
                    current = 0;
                }
            }
        }
        if (builder.length() != 0) {
            sentences.add(builder.toStringClear());
        }
        final String[] array = sentences.<String>toArray(new String[sentences.size()]);
        sentences.clear();
        for (final String line : array) {
            int lineLength;
            if ((lineLength = line.length()) <= 50) {
                sentences.add(line);
            } else {
                final char[] chars = line.toCharArray();
                int index = 0;
                while (lineLength > 50) {
                    final char[] data = new char[50];
                    System.arraycopy(chars, index, data, 0, data.length);
                    sentences.add(new String(data));
                    index += 50;
                    lineLength -= 50;
                }
                if (lineLength != 0) {
                    final char[] data = new char[lineLength];
                    System.arraycopy(chars, index, data, 0, data.length);
                    sentences.add(new String(data));
                }
            }
        }
        return sentences.<String>toArray(new String[sentences.size()]);
    }

    public static String splitToSentencesAsString(final String value, final int length) {
        return String.join("\n", splitToSentences(value, length));
    }

    public static String center(final String value, final char character, final int length) {
        int size = length - value.length();
        if (size <= 0) {
            return value;
        }
        if (size % 2 == 1) {
            size++;
        }
        final int amount = size / 2;
        final StringBuilder builder = new StringBuilder();
        for (int index = 0; index < amount; index++) {
            builder.append(character);
        }
        final String add = builder.toStringClear();
        return add + value + add;
    }

    public static String centerColored(final String value, final char character, final int length) {
        int size = length - BukkitColor.strip(value).length();
        if (size <= 0) {
            return value;
        }
        if (size % 2 == 1) {
            size++;
        }
        final int amount = size / 2;
        final StringBuilder builder = new StringBuilder();
        for (int index = 0; index < amount; index++) {
            builder.append(character);
        }
        final String add = builder.toStringClear();
        return add + value + add;
    }

}
