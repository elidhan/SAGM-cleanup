package net.elidhan.anim_guns.client.render;

import mod.azure.azurelib.renderer.GeoItemRenderer;
import mod.azure.azurelib.renderer.GeoRenderer;
import net.elidhan.anim_guns.client.model.GunModel;
import net.elidhan.anim_guns.item.GunItem;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class GunRenderer extends GeoItemRenderer<GunItem> implements GeoRenderer<GunItem>
{
    public GunRenderer(Identifier identifier)
    {
        super(new GunModel(identifier));
    }

    @Override
    public void render(ItemStack stack, ModelTransformationMode transformType, MatrixStack poseStack, VertexConsumerProvider bufferSource, int packedLight, int packedOverlay)
    {
        super.render(stack, transformType, poseStack, bufferSource, packedLight, packedOverlay);
    }
}