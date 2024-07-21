package net.elidhan.anim_guns.client.render;

import mod.azure.azurelib.cache.object.GeoBone;
import mod.azure.azurelib.renderer.GeoItemRenderer;
import mod.azure.azurelib.renderer.GeoRenderer;
import mod.azure.azurelib.util.RenderUtils;
import net.elidhan.anim_guns.client.MuzzleFlashRenderType;
import net.elidhan.anim_guns.client.RecoilHandler;
import net.elidhan.anim_guns.client.model.GunModel;
import net.elidhan.anim_guns.item.GunItem;
import net.elidhan.anim_guns.mixininterface.IFPlayerWithGun;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
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
import net.minecraft.util.math.RotationAxis;

public class GunRenderer extends GeoItemRenderer<GunItem> implements GeoRenderer<GunItem>
{
    private VertexConsumerProvider bufferSource;

    public GunRenderer(Identifier identifier)
    {
        super(new GunModel(identifier));
    }

    private float sprintProgress = 0.0f;

    @Override
    public void render(ItemStack stack, ModelTransformationMode transformType, MatrixStack poseStack, VertexConsumerProvider bufferSource, int packedLight, int packedOverlay)
    {
        this.bufferSource = bufferSource;

        if (transformType != ModelTransformationMode.FIRST_PERSON_RIGHT_HAND || (((IFPlayerWithGun)MinecraftClient.getInstance().player).isAiming() && MinecraftClient.getInstance().player.getMainHandStack().getOrCreateNbt().getBoolean("isScoped"))) return;

        super.render(stack, transformType, poseStack, bufferSource, packedLight, packedOverlay);
    }

    @Override
    public void renderRecursively(MatrixStack poseStack, GunItem animatable, GeoBone bone, RenderLayer renderType, VertexConsumerProvider bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha)
    {
        MinecraftClient client = MinecraftClient.getInstance();
        float delta = client.getTickDelta();
        ClientPlayerEntity player = client.player;
        VertexConsumer buffer1 = this.bufferSource.getBuffer(renderType);
        GunItem gun = (GunItem) getCurrentItemStack().getItem();

        //This bunch of code just to dynamically center guns regardless of their translations in 1st person view and in edit mode
        BakedModel model = client.getItemRenderer().getModel(getCurrentItemStack(), player.getWorld(), player, 0);
        float posX = model.getTransformation().firstPersonRightHand.translation.x;
        float posY = model.getTransformation().firstPersonRightHand.translation.y;

        /*
        Need to account for sight attachment heights in the future

        DistanceX from 0 to center of in-game screen = -8.9675
        DistanceY from 0 to center of in-game screen = 0.50875
        Must take into account iron sight/sight attachment height values (taken from BlockBench's edit mode) and adjust accordingly
        Must also take into account the first person right-hand translation values

        centeredY = (Distance from Y-value 0 in Blockbench to y-center of screen in-game) - (FirstPersonRightHand Y value) - (Iron Sight bone positional Y-value)
         */

        poseStack.push();
        //Get Aim Progress
        float prevAimTick = (float)((IFPlayerWithGun)player).getPreviousAimTick();
        float aimTick = (float)((IFPlayerWithGun)player).getAimTick();
        float f = MathHelper.clamp((prevAimTick + (aimTick - prevAimTick) * delta)/2f, 0f, 1f);
        //float f = MathHelper.clamp(Easings.easeOutQuart(MathHelper.lerp(delta,prevAimTick,aimTick)/2f),0f,1f);

        //Get Sprint Progress
        sprintProgress = MathHelper.lerp(delta / 8f, sprintProgress, player.isSprinting() ? 1 : 0);

        //Recoil duration
        //float f1 = player.getItemCooldownManager().getCooldownProgress(gun, delta);

        //Does different things depending on which bone is being rendered
        switch (bone.getName())
        {
            case "gunbody", "magazine2" ->
            {
                //Apply Transforms
                sprintTransforms(poseStack, sprintProgress);
                aimTransforms(poseStack, f, posX, posY);
                recoilTransforms(
                        poseStack,
                        RecoilHandler.getInstance().getVMRotSide(delta),
                        RecoilHandler.getInstance().getVMRotUp(delta),
                        RecoilHandler.getInstance().getVMMoveUp(delta),
                        RecoilHandler.getInstance().getVMMoveBack(delta),
                        Math.abs(1f-f)+gun.getAimVMRecoilMult());
            }
            case "muzzleflash" ->
            {
                aimTransforms(poseStack, f, posX, posY);
                buffer1 = this.bufferSource.getBuffer(MuzzleFlashRenderType.getMuzzleFlash());
            }
            case "leftArm", "rightArm" ->
            {
                bone.setHidden(true);
                bone.setChildrenHidden(false);

                PlayerEntityRenderer playerEntityRenderer = (PlayerEntityRenderer) client.getEntityRenderDispatcher().getRenderer(player);
                PlayerEntityModel<AbstractClientPlayerEntity> playerEntityModel = playerEntityRenderer.getModel();
                ModelPart playerArm = bone.getName().equals("leftArm") ? playerEntityModel.leftArm : playerEntityModel.rightArm;
                ModelPart playerSleeve = bone.getName().equals("leftArm") ? playerEntityModel.leftSleeve : playerEntityModel.rightSleeve;

                RenderUtils.translateMatrixToBone(poseStack, bone);
                RenderUtils.translateToPivotPoint(poseStack, bone);
                RenderUtils.rotateMatrixAroundBone(poseStack, bone);
                RenderUtils.scaleMatrixForBone(poseStack, bone);
                RenderUtils.translateAwayFromPivotPoint(poseStack, bone);

                Identifier playerSkin = player.getSkinTexture();
                VertexConsumer arm = this.bufferSource.getBuffer(RenderLayer.getEntitySolid(playerSkin));
                VertexConsumer sleeve = this.bufferSource.getBuffer(RenderLayer.getEntityTranslucent(playerSkin));

                poseStack.scale(0.67f, 1.33f, 0.67f);
                poseStack.translate(bone.getName().equals("leftArm") ? -0.25 : 0.25, -0.43625, 0.1625);
                if(bone.getName().equals("leftArm"))
                {
                    leftArmAimTransforms(poseStack, f);
                }
                playerArm.setPivot(bone.getPivotX(), bone.getPivotY(), bone.getPivotZ());
                playerArm.setAngles(0, 0, 0);
                playerArm.render(poseStack, arm, packedLight, packedOverlay, 1, 1, 1, 1);

                playerSleeve.setPivot(bone.getPivotX(), bone.getPivotY(), bone.getPivotZ());
                playerSleeve.setAngles(0, 0, 0);
                playerSleeve.render(poseStack, sleeve, packedLight, packedOverlay, 1, 1, 1, 1);

            }
        }

        super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer1, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
        poseStack.pop();
    }

    private void sprintTransforms(MatrixStack poseStack, float f)
    {
        poseStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-22.5f * f));
        poseStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(77.5f * f));
        poseStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(22.5f * f));
        poseStack.translate(0,-0.5f / 16 * f,0);
    }

    private void aimTransforms(MatrixStack poseStack, float f, float posX, float posY)
    {
        GeoBone ironSightBone = getGeoModel().getBone("sight_default").orElse(null);
        float ironSightAdjust = 0f;
        if (ironSightBone != null)
        {
            ironSightAdjust = ironSightBone.getPivotY();
        }

        float centeredX = ((-8.96325f)-(posX*16))/16f;
        float centeredY = 0.50875f - posY - (ironSightAdjust/16f);
        poseStack.translate(centeredX * f, centeredY * f, 0);
    }

    private void recoilTransforms(MatrixStack poseStack, float rotX, float rotY, float moveY, float moveZ, float upMult)
    {
        poseStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(rotY*upMult));
        poseStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(rotX*upMult));
        poseStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((rotX)*upMult));
        poseStack.translate(0,(moveY)*upMult,(moveZ)/16f);
    }

    private void leftArmAimTransforms(MatrixStack poseStack, float f)
    {
        poseStack.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(22.5f * f));
        poseStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(2.25f * f));
        poseStack.translate(0f/16f * f, -2.5f/16f * f,5f/16f * f);
    }
}