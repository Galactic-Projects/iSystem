package net.galacticprojects.utils.color.helper;

import net.galacticprojects.utils.color.ColorHelper;

import java.util.Iterator;

public abstract class ColorIterator implements Iterator<String> {
    public abstract void reset();

    public abstract float[] nextColor();

    @Override
    public String next() {
        return ColorHelper.toHexColor(nextColor());
    }
}
