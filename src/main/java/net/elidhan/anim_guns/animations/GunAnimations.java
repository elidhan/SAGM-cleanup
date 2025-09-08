package net.elidhan.anim_guns.animations;

import software.bernie.geckolib.core.animation.Animation;
import  software.bernie.geckolib.core.animation.RawAnimation;

public final class GunAnimations
{
    public static final RawAnimation IDLE = RawAnimation.begin().thenPlay("idle");
    public static final RawAnimation FIRING = RawAnimation.begin().then("firing", Animation.LoopType.HOLD_ON_LAST_FRAME);
    public static final RawAnimation RELOADING = RawAnimation.begin().then("reloading", Animation.LoopType.HOLD_ON_LAST_FRAME);
    public static final RawAnimation RELOAD_0 = RawAnimation.begin().then("reload_0", Animation.LoopType.PLAY_ONCE);
    public static final RawAnimation RELOAD_1 = RawAnimation.begin().then("reload_1", Animation.LoopType.PLAY_ONCE);
    public static final RawAnimation RELOAD_2 = RawAnimation.begin().then("reload_2", Animation.LoopType.HOLD_ON_LAST_FRAME);
    public static final RawAnimation MELEE = RawAnimation.begin().then("melee", Animation.LoopType.HOLD_ON_LAST_FRAME);
}