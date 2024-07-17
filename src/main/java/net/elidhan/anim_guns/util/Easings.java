package net.elidhan.anim_guns.util;

public class Easings
{
    //TODO: Make this shit
    public static float easeOutCubic(float input)
    {
        return (float) (1 - Math.pow(1 - input, 3));
    }
}
