package net.elidhan.anim_guns.client.render;

import mod.azure.azurelib.cache.AzureLibCache;
import mod.azure.azurelib.cache.object.BakedGeoModel;
import mod.azure.azurelib.cache.object.GeoBone;
import mod.azure.azurelib.renderer.GeoItemRenderer;
import mod.azure.azurelib.renderer.GeoRenderer;
import net.elidhan.anim_guns.client.model.GunModel;
import net.elidhan.anim_guns.item.GunItem;
import net.elidhan.anim_guns.mixininterface.IFPlayerWIthGun;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class GunRenderer extends GeoItemRenderer<GunItem> implements GeoRenderer<GunItem>
{
    public GunRenderer(Identifier identifier)
    {
        super(new GunModel(identifier));
    }

    @Override
    public void render(ItemStack stack, ModelTransformationMode transformType, MatrixStack poseStack, VertexConsumerProvider bufferSource, int packedLight, int packedOverlay)
    {
        if(!transformType.isFirstPerson()) return;

        super.render(stack, transformType, poseStack, bufferSource, packedLight, packedOverlay);
    }

    @Override
    public void renderRecursively(MatrixStack poseStack, GunItem animatable, GeoBone bone, RenderLayer renderType, VertexConsumerProvider bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha)
    {
        MinecraftClient client = MinecraftClient.getInstance();
        float delta = client.getTickDelta();

        BakedModel model = client.getItemRenderer().getModel(getCurrentItemStack(), client.player.getWorld(), client.player, 0);
        float posX = model.getTransformation().firstPersonRightHand.translation.x;
        float posY = model.getTransformation().firstPersonRightHand.translation.y;
        float posZ = model.getTransformation().firstPersonRightHand.translation.z;

        float centered = (-8.9625f)-(posX*16);

        poseStack.push();
        //
        //Get Aim Progress
        float f = MathHelper.lerp(delta, (float)((IFPlayerWIthGun)client.player).getPreviousAimTick(), (float)((IFPlayerWIthGun)client.player).getAimTick());
        //Aim
        if (bone.getName().equals("gunbody") || bone.getName().equals("magazine2") || bone.getName().equals("muzzleflash"))
            poseStack.translate((centered/16f) * f/2, 0, 0);
        //
        super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
        poseStack.pop();
    }
}