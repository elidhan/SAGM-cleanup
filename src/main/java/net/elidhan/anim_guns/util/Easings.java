package net.elidhan.anim_guns.util;

public class Easings
{
    //TODO: Make this shit
    public static float easeOutCubic(float x)
    {
        return (float) (1 - Math.pow(1 - x, 3));
    }
}
