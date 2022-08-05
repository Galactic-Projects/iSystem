package net.galacticprojects.isystem.utils.color.helper;

import net.galacticprojects.isystem.utils.color.ColorHelper;

import java.util.Iterator;

public abstract class ColorIterator implements Iterator<String> {
    public abstract void reset();

    public abstract float[] nextColor();

    @Override
    public String next() {
        return ColorHelper.toHexColor(nextColor());
    }
}
