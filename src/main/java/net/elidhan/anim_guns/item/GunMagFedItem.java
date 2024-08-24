package net.elidhan.anim_guns.item;

import mod.azure.azurelib.core.animation.AnimatableManager;
import mod.azure.azurelib.core.animation.AnimationController;
import net.elidhan.anim_guns.animations.GunAnimations;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class GunMagFedItem extends GunItem
{
    public GunMagFedItem(Settings settings, String id, float damage, int shotCount, int fireRate, int magSize, int reloadTime, Vector2f spread, Vector2f cameraRecoil, Vector3f viewModelRecoilMult, AttachmentItem.AttachType[] acceptedAttachmentTypes)
    {
        super(settings, id, damage, shotCount, fireRate, magSize, reloadTime, spread, cameraRecoil, viewModelRecoilMult, acceptedAttachmentTypes);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers)
    {
        AnimationController<GunItem> controller = new AnimationController<>(this, this.getID()+"_controller", 1, super::predicate)
                .receiveTriggeredAnimations()
                .triggerableAnim("idle", GunAnimations.IDLE)
                .triggerableAnim("firing", GunAnimations.FIRING)
                .triggerableAnim("reloading", GunAnimations.RELOADING);

        controllers.add(controller);
    }
}
