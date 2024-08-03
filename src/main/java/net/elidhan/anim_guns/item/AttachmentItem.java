package net.elidhan.anim_guns.item;

import mod.azure.azurelib.core.animatable.GeoAnimatable;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animation.AnimatableManager;
import net.minecraft.item.Item;

public class AttachmentItem extends Item implements GeoAnimatable
{
    private final String id;
    private final float recoilMult;
    private final float spreadMult;
    private final AttachType attachType;
    private final boolean silencesGun;

    public AttachmentItem(Settings settings, String id, float recoilMult, float spreadMult, AttachType attachType, boolean silencesGun)
    {
        super(settings);
        this.id = id;
        this.recoilMult = recoilMult;
        this.spreadMult = spreadMult;
        this.attachType = attachType;
        this.silencesGun = silencesGun;
    }

    public String getId()
    {
        return this.id;
    }

    public float getRecoilMult()
    {
        return this.recoilMult;
    }

    public float getSpreadMult()
    {
        return this.spreadMult;
    }

    public boolean silencesGun()
    {
        return silencesGun;
    }

    public AttachType getAttachType()
    {
        return this.attachType;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {}

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {return null;}

    @Override
    public double getTick(Object o) {return 0;}

    public enum AttachType
    {
        SIGHT,
        SCOPE,
        GRIP,
        MUZZLE
    }
}