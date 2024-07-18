package net.elidhan.anim_guns.util;

import net.minecraft.util.math.MathHelper;

public class Easings
{
    //TODO: Make this shit
    public static float easeOutCubic(float input)
    {
        return (float) (1 - Math.pow(1 - input, 3));
    }

    public static float easeOutBack(float input, float overShootMult)
    {
        overShootMult = MathHelper.clamp(overShootMult, 0.7f, 2f);
        float f = overShootMult + 1;
        return (float)(f * Math.pow(input, 3) - overShootMult * (Math.pow(input, 2)));
        //return (float)(1f + f * Math.pow(input - 1f, 3f) + overShootMult * Math.pow(input - 1f, 2f));
    }
}
