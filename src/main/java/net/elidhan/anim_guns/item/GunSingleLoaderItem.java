package net.elidhan.anim_guns.item;

import mod.azure.azurelib.core.animation.AnimatableManager;
import mod.azure.azurelib.core.animation.AnimationController;
import net.elidhan.anim_guns.animations.GunAnimations;

public class GunSingleLoaderItem extends GunItem
{
    private final int[] reloadStages;

    public GunSingleLoaderItem(Settings settings, String id, float damage, int fireRate, int magSize, int reloadTime, int[] reloadStages, float[] spread, float[] recoil, float[] viewModelRecoil)
    {
        super(settings, id, damage, fireRate, magSize, reloadTime, spread, recoil, viewModelRecoil);
        this.reloadStages = reloadStages; //Reload stages exactly 4 values
    }

    public int getReloadStageTick(int stage)
    {
        return this.reloadStages[stage];
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers)
    {
        AnimationController<GunItem> controller = new AnimationController<>(this, "controller", 1, super::predicate)
                .triggerableAnim("idle", GunAnimations.IDLE)
                .triggerableAnim("firing", GunAnimations.FIRING)
                .triggerableAnim("reload_0", GunAnimations.RELOAD_0)
                .triggerableAnim("reload_1", GunAnimations.RELOAD_1)
                .triggerableAnim("reload_2", GunAnimations.RELOAD_2);

        controllers.add(controller);
    }
}
