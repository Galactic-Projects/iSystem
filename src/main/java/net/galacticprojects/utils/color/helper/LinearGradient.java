package net.galacticprojects.utils.color.helper;


import net.galacticprojects.utils.color.ColorHelper;

public class LinearGradient extends ColorIterator {
    private final float[] color0;

    private final float[] color1;

    private final float steps;

    private int step;

    public LinearGradient(final float[] color0, final float[] color1, final int steps) {
        this.color0 = color0;
        this.color1 = color1;
        this.steps = steps;
    }

    @Override
    public boolean hasNext() {
        return this.step != this.steps;
    }

    @Override
    public float[] nextColor() {
        if (!hasNext()) {
            return null;
        }
        final float ratio = ++this.step / this.steps;
        final float[] output = new float[3];
        output[0] = ColorHelper.interpolate(ratio, this.color0[0], this.color1[0]);
        output[1] = ColorHelper.interpolate(ratio, this.color0[1], this.color1[1]);
        output[2] = ColorHelper.interpolate(ratio, this.color0[2], this.color1[2]);
        return output;
    }

    @Override
    public void reset() {
        this.step = 0;
    }

    @Override
    public String next() {
        return ColorHelper.toHexColor(nextColor());
    }
}
