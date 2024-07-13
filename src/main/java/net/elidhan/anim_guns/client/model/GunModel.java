package net.elidhan.anim_guns.client.model;

import mod.azure.azurelib.core.animation.AnimatableManager;
import mod.azure.azurelib.core.animation.AnimationState;
import mod.azure.azurelib.model.DefaultedItemGeoModel;
import net.elidhan.anim_guns.AnimatedGuns;
import net.elidhan.anim_guns.item.GunItem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.Perspective;
import net.minecraft.util.Identifier;

public class GunModel extends DefaultedItemGeoModel<GunItem>
{
    /**
     * Create a new instance of this model class.<br>
     * The asset path should be the truncated relative path from the base folder.<br>
     * E.G.
     * <pre>{@code
     * 	new ResourceLocation("myMod", "armor/obsidian")
     * }</pre>
     *
     * @param assetSubpath
     */
    public GunModel(Identifier assetSubpath) {
        super(assetSubpath);
    }

    @Override
    public Identifier getModelResource(GunItem object)
    {
        return new Identifier(AnimatedGuns.MOD_ID, "geo/testgun.geo.json");
    }

    @Override
    public Identifier getTextureResource(GunItem object)
    {
        return new Identifier(AnimatedGuns.MOD_ID, "textures/item/testgun.png");
    }

    @Override
    public Identifier getAnimationResource(GunItem animatable)
    {
        return new Identifier(AnimatedGuns.MOD_ID, "animations/testgun.animation.json");
    }

    @Override
    public void handleAnimations(GunItem animatable, long instanceId, AnimationState<GunItem> animationState)
    {
        Perspective perspective = MinecraftClient.getInstance().options.getPerspective();

        if(!perspective.isFirstPerson())
        {
            return;
        }

        super.handleAnimations(animatable, instanceId, animationState);
    }
}