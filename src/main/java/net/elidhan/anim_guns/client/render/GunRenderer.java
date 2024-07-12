package net.elidhan.anim_guns.client.render;

import mod.azure.azurelib.cache.object.GeoBone;
import mod.azure.azurelib.core.utils.MathUtils;
import mod.azure.azurelib.renderer.GeoItemRenderer;
import mod.azure.azurelib.renderer.GeoRenderer;
import net.elidhan.anim_guns.client.model.GunModel;
import net.elidhan.anim_guns.item.GunItem;
import net.elidhan.anim_guns.mixininterface.IFPlayerWIthGun;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class GunRenderer extends GeoItemRenderer<GunItem> implements GeoRenderer<GunItem>
{
    public GunRenderer(Identifier identifier)
    {
        super(new GunModel(identifier));
    }

    @Override
    public void renderRecursively(MatrixStack poseStack, GunItem animatable, GeoBone bone, RenderLayer renderType, VertexConsumerProvider bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha)
    {
        MinecraftClient client = MinecraftClient.getInstance();
        float delta = client.getTickDelta();

        poseStack.push();

        float f = MathHelper.lerp(delta, (float)((IFPlayerWIthGun)client.player).getPreviousAimTick(), (float)((IFPlayerWIthGun)client.player).getAimTick());

        //do translation/rotation stuff here
        if (bone.getName().equals("gunbody") || bone.getName().equals("magazine2") || bone.getName().equals("muzzleflash"))
            poseStack.translate(-f/16, 0, 0);

        super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
        poseStack.pop();
    }

    float lerp(float a, float b, float f)
    {
        return a * (1f - f) + (b * f);
    }
}