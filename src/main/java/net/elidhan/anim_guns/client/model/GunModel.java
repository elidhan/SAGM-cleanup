package net.elidhan.anim_guns.client.model;

import mod.azure.azurelib.core.animation.AnimatableManager;
import mod.azure.azurelib.core.animation.AnimationState;
import mod.azure.azurelib.core.molang.MolangParser;
import mod.azure.azurelib.model.DefaultedItemGeoModel;
import net.elidhan.anim_guns.AnimatedGuns;
import net.elidhan.anim_guns.item.GunItem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
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
        return new Identifier(AnimatedGuns.MOD_ID, "geo/"+object.getID()+".geo.json");
    }

    @Override
    public Identifier getTextureResource(GunItem object)
    {
        return new Identifier(AnimatedGuns.MOD_ID, "textures/item/"+object.getID()+".png");
    }

    @Override
    public Identifier getAnimationResource(GunItem animatable)
    {
        return new Identifier(AnimatedGuns.MOD_ID, "animations/"+animatable.getID()+".animation.json");
    }

    @Override
    public void handleAnimations(GunItem animatable, long instanceId, AnimationState<GunItem> animationState)
    {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;
        Perspective perspective = client.options.getPerspective();
        AnimatableManager<GunItem> animatableManager = animatable.getAnimatableInstanceCache().getManagerForId(instanceId);

        if(!perspective.isFirstPerson())
            animatableManager.tryTriggerAnimation("controller","idle");

        super.handleAnimations(animatable, instanceId, animationState);
    }

    @Override
    public void applyMolangQueries(GunItem animatable, double animTime)
    {
        super.applyMolangQueries(animatable, animTime);
    }
}