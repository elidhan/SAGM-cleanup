package net.elidhan.anim_guns.item;

import mod.azure.azurelib.core.animation.AnimatableManager;
import mod.azure.azurelib.core.animation.AnimationController;
import net.elidhan.anim_guns.animations.GunAnimations;

public class GunMagFedItem extends GunItem
{
    public GunMagFedItem(Settings settings, String id, float damage, int fireRate, int magSize, int reloadTime, float[] spread, float[] recoil, float[] viewModelRecoil)
    {
        super(settings, id, damage, fireRate, magSize, reloadTime, spread, recoil, viewModelRecoil);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers)
    {
        AnimationController<GunItem> controller = new AnimationController<>(this, "controller", 1, super::predicate)
                .triggerableAnim("idle", GunAnimations.IDLE)
                .triggerableAnim("firing", GunAnimations.FIRING)
                .triggerableAnim("reloading", GunAnimations.RELOADING);

        controllers.add(controller);
    }
}
