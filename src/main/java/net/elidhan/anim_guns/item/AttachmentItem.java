package net.elidhan.anim_guns.item;

import mod.azure.azurelib.animatable.GeoItem;
import mod.azure.azurelib.animatable.client.RenderProvider;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animation.AnimatableManager;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.core.object.PlayState;
import mod.azure.azurelib.util.AzureLibUtil;
import net.elidhan.anim_guns.AnimatedGuns;
import net.elidhan.anim_guns.client.render.AttachmentRenderer;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class AttachmentItem extends Item implements GeoItem
{
    private final int id;
    private final float recoilMult;
    private final float spreadMult;
    private final AttachType attachType;
    private final boolean silencesGun;
    protected final Supplier<Object> renderProvider = GeoItem.makeRenderer(this);
    protected final AnimatableInstanceCache animationCache = AzureLibUtil.createInstanceCache(this);

    public AttachmentItem(Settings settings, int id, float recoilMult, float spreadMult, AttachType attachType, boolean silencesGun)
    {
        super(settings);
        this.id = id;
        this.recoilMult = recoilMult;
        this.spreadMult = spreadMult;
        this.attachType = attachType;
        this.silencesGun = silencesGun;
    }

    public int getId()
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

    public AttachType getAttachType()
    {
        return this.attachType;
    }

    public String attachTypeString()
    {
        switch (this.attachType)
        {
            case SIGHT ->
            {
                return "si";
            }
            case GRIP ->
            {
                return "gr";
            }
            case MUZZLE ->
            {
                return "mz";
            }
        }
        return "si";
    }

    @Override
    public void createRenderer(Consumer<Object> consumer)
    {
        consumer.accept(new RenderProvider()
        {
            private final AttachmentRenderer renderer = new AttachmentRenderer(new Identifier(AnimatedGuns.MOD_ID, attachTypeString() + "_" + getId()));

            @Override
            public BuiltinModelItemRenderer getCustomRenderer()
            {
                return this.renderer;
            }
        });
    }

    @Override
    public Supplier<Object> getRenderProvider()
    {
        return this.renderProvider;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar)
    {
        AnimationController<AttachmentItem> controller = new AnimationController<>(this, "controller", 1, event -> PlayState.CONTINUE);
        controllerRegistrar.add(controller);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache()
    {
        return this.animationCache;
    }

    @Override
    public double getTick(Object o)
    {
        return 0;
    }

    public enum AttachType
    {
        SIGHT,
        GRIP,
        MUZZLE
    }
}