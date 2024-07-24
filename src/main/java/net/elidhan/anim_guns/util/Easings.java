package net.elidhan.anim_guns.util;

public class Easings
{
    //TODO: Make this shit
    public static float easeInOutSine(float input)
    {
        return (float) -(Math.cos(Math.PI * input) - 1) / 2;
    }

    public static float easeInCubic(float input)
    {
        return (float) (1 - Math.pow(1 - input, 3));
    }

    public static float easeOutCubic(float input)
    {
        return (float) Math.pow(input, 3);
    }

    public static float easeOutQuart(float input)
    {
        return (float) Math.pow(input, 4);
    }

    public static float easeOutBack(float input, float overShootMult)
    {
        overShootMult = 0.625f;
        float f = overShootMult + 1;
        return (float) (f * Math.pow(input, 3) - overShootMult * input);
        //return (float)(f * Math.pow(input, 3) - overShootMult * (Math.pow(input, 2)));
        //return (float)(1f + f * Math.pow(input - 1f, 3f) + overShootMult * Math.pow(input - 1f, 2f));
    }
}
