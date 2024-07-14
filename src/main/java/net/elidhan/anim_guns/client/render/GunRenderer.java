package net.elidhan.anim_guns.client.render;

import mod.azure.azurelib.cache.object.GeoBone;
import mod.azure.azurelib.renderer.GeoItemRenderer;
import mod.azure.azurelib.renderer.GeoRenderer;
import mod.azure.azurelib.util.RenderUtils;
import net.elidhan.anim_guns.client.model.GunModel;
import net.elidhan.anim_guns.item.GunItem;
import net.elidhan.anim_guns.mixininterface.IFPlayerWIthGun;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class GunRenderer extends GeoItemRenderer<GunItem> implements GeoRenderer<GunItem>
{
    private VertexConsumerProvider bufferSource;

    public GunRenderer(Identifier identifier)
    {
        super(new GunModel(identifier));
    }

    @Override
    public void render(ItemStack stack, ModelTransformationMode transformType, MatrixStack poseStack, VertexConsumerProvider bufferSource, int packedLight, int packedOverlay)
    {
        this.bufferSource = bufferSource;

        if(transformType != ModelTransformationMode.FIRST_PERSON_RIGHT_HAND) return;

        super.render(stack, transformType, poseStack, bufferSource, packedLight, packedOverlay);
    }

    @Override
    public void renderRecursively(MatrixStack poseStack, GunItem animatable, GeoBone bone, RenderLayer renderType, VertexConsumerProvider bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha)
    {
        MinecraftClient client = MinecraftClient.getInstance();
        float delta = client.getTickDelta();

        //This bunch of code just to dynamically center guns regardless of their translations in 1st person view and in edit mode
        BakedModel model = client.getItemRenderer().getModel(getCurrentItemStack(), client.player.getWorld(), client.player, 0);
        float posX = model.getTransformation().firstPersonRightHand.translation.x;
        float posY = model.getTransformation().firstPersonRightHand.translation.y;

        GeoBone ironSightBone = getGeoModel().getBone("sight_default").orElse(null);
        float ironSightAdjust = 0f;
        if (ironSightBone != null)
        {
            ironSightAdjust = ironSightBone.getPivotY();
        }
        //Need to account for sight attachment heights in the future

        //DistanceX from 0 to center of in-game screen = -8.975
        //DistanceY from 0 to center of in-game screen = 0.50875
        //Must take into account iron sight/sight attachment height values (taken from BlockBench's edit mode) and adjust accordingly
        //Must also take into account the first person right-hand translation values

        //centeredY = (Distance from Y-value 0 in Blockbench to y-center of screen in-game) - (FirstPersonRightHand Y value) - (Iron Sight bone positional Y-value)
        float centeredX = ((-8.9675f)-(posX*16))/16f;
        float centeredY = 0.50875f - posY - (ironSightAdjust/16f);

        poseStack.push();
        //Get Aim Progress
        float f = MathHelper.lerp(delta, (float)((IFPlayerWIthGun)client.player).getPreviousAimTick(), (float)((IFPlayerWIthGun)client.player).getAimTick());

        //Does different things depending on which bone is being rendered
        switch (bone.getName())
        {
            case "gunbody", "magazine2", "muzzleflash" -> poseStack.translate(centeredX * f / 2, centeredY * f / 2, 0);
            case "leftArm", "rightArm" ->
            {
                bone.setHidden(true);
                bone.setChildrenHidden(false);

                PlayerEntityRenderer playerEntityRenderer = (PlayerEntityRenderer) client.getEntityRenderDispatcher().getRenderer(client.player);
                PlayerEntityModel<AbstractClientPlayerEntity> playerEntityModel = playerEntityRenderer.getModel();
                ModelPart playerArm = bone.getName().equals("leftArm") ? playerEntityModel.leftArm : playerEntityModel.rightArm;
                ModelPart playerSleeve = bone.getName().equals("leftArm") ? playerEntityModel.leftSleeve : playerEntityModel.rightSleeve;

                RenderUtils.translateMatrixToBone(poseStack, bone);
                RenderUtils.translateToPivotPoint(poseStack, bone);
                RenderUtils.rotateMatrixAroundBone(poseStack, bone);
                RenderUtils.scaleMatrixForBone(poseStack, bone);
                RenderUtils.translateAwayFromPivotPoint(poseStack, bone);

                Identifier playerSkin = client.player.getSkinTexture();
                VertexConsumer arm = this.bufferSource.getBuffer(RenderLayer.getEntitySolid(playerSkin));
                VertexConsumer sleeve = this.bufferSource.getBuffer(RenderLayer.getEntityTranslucent(playerSkin));

                poseStack.scale(0.67f, 1.33f, 0.67f);
                poseStack.translate(bone.getName().equals("leftArm") ? -0.25 : 0.25, -0.43625, 0.1625);
                playerArm.setPivot(bone.getPivotX(), bone.getPivotY(), bone.getPivotZ());
                playerArm.setAngles(0, 0, 0);
                playerArm.render(poseStack, arm, packedLight, packedOverlay, 1, 1, 1, 1);

                playerSleeve.setPivot(bone.getPivotX(), bone.getPivotY(), bone.getPivotZ());
                playerSleeve.setAngles(0, 0, 0);
                playerSleeve.render(poseStack, sleeve, packedLight, packedOverlay, 1, 1, 1, 1);
            }
        }

        super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
        poseStack.pop();
    }
}