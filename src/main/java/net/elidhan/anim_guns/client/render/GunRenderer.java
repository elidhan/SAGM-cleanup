package net.elidhan.anim_guns.client.render;

import mod.azure.azurelib.cache.AzureLibCache;
import mod.azure.azurelib.cache.object.BakedGeoModel;
import mod.azure.azurelib.cache.object.GeoBone;
import mod.azure.azurelib.renderer.GeoItemRenderer;
import mod.azure.azurelib.renderer.GeoRenderer;
import mod.azure.azurelib.util.RenderUtils;
import net.elidhan.anim_guns.AnimatedGuns;
import net.elidhan.anim_guns.client.AttachmentRenderType;
import net.elidhan.anim_guns.client.MuzzleFlashRenderType;
import net.elidhan.anim_guns.client.RecoilHandler;
import net.elidhan.anim_guns.client.model.GunModel;
import net.elidhan.anim_guns.item.GunItem;
import net.elidhan.anim_guns.mixininterface.IFPlayerWithGun;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.*;
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

    private float sightAdjust = 0.0f;

    @Override
    public void render(ItemStack stack, ModelTransformationMode transformType, MatrixStack poseStack, VertexConsumerProvider bufferSource, int packedLight, int packedOverlay)
    {
        this.bufferSource = bufferSource;

        if (transformType != ModelTransformationMode.FIRST_PERSON_RIGHT_HAND)
            return;

        super.render(stack, transformType, poseStack, bufferSource, packedLight, packedOverlay);
    }

    @Override
    public void renderRecursively(MatrixStack poseStack, GunItem animatable, GeoBone bone, RenderLayer renderType, VertexConsumerProvider bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha)
    {
        MinecraftClient client = MinecraftClient.getInstance();
        float delta = client.getTickDelta();
        ClientPlayerEntity player = client.player;
        VertexConsumer buffer1 = this.bufferSource.getBuffer(RenderLayer.getEntityTranslucent(getGeoModel().getTextureResource(animatable)));

        //Attachments test
        String sightID = this.getAnimatable().getSightID(this.getCurrentItemStack());
        String gripID = this.getAnimatable().getGripID(this.getCurrentItemStack());
        String muzzleID = this.getAnimatable().getMuzzleID(this.getCurrentItemStack());

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

        //Get Aim Progress
        float prevAimTick = (float)((IFPlayerWithGun)player).getPreviousAimTick();
        float aimTick = (float)((IFPlayerWithGun)player).getAimTick();

        float f = MathHelper.clamp((prevAimTick + (aimTick - prevAimTick) * delta)/4f, 0f, 1f);
        alpha = getCurrentItemStack().getOrCreateNbt().getBoolean("isScoped") && ((IFPlayerWithGun)player).isAiming() ? MathHelper.clamp(1-(f*4), 0 ,1) : 1;

        poseStack.push();

        //Does different things depending on which bone is being rendered
        switch (bone.getName())
        {
            case "gunbody", "magazine2" ->
            {
                aimTransforms(poseStack, f, posX, posY);
                recoilTransforms(
                        poseStack,
                        RecoilHandler.getInstance().getVMRotSide(delta, f),
                        RecoilHandler.getInstance().getVMRotUp(delta, f),
                        RecoilHandler.getInstance().getVMMoveUp(delta, f),
                        RecoilHandler.getInstance().getVMMoveBack(delta, f));
            }
            case "sight_default" -> bone.setHidden(!sightID.equals("default"));
            case "muzzleflash" ->
            {
                buffer1 = this.bufferSource.getBuffer(MuzzleFlashRenderType.getMuzzleFlash());
                aimTransforms(poseStack, f, posX, posY);
            }
            case "sightPos" ->
            {
                sightAdjust = 0.0f;
                BakedGeoModel attachmentModel = AzureLibCache.getBakedModels().get(new Identifier(AnimatedGuns.MOD_ID,"geo/"+sightID+".geo.json"));
                if (attachmentModel == null) return;
                GeoBone attachmentBone = attachmentModel.getBone("sight").orElse(null);
                GeoBone reticle = attachmentModel.getBone("reticle").orElse(null);

                if(attachmentBone != null)
                {
                    buffer1 = this.bufferSource.getBuffer(AttachmentRenderType.getAttachment(sightID));

                    sightAdjust = getGeoModel().getBone("ads_node").orElse(null).getPivotY() - bone.getPivotY() - attachmentModel.getBone("ads_node").orElse(null).getPivotY();

                    poseStack.translate(bone.getPivotX()/16f, bone.getPivotY()/16f, bone.getPivotZ()/16f);

                    super.renderCubesOfBone(poseStack, attachmentBone, this.bufferSource.getBuffer(AttachmentRenderType.getAttachment(sightID)), packedLight, packedOverlay, red, green, blue, alpha);

                    if (reticle != null)
                    {
                        super.renderCubesOfBone(poseStack, reticle, this.bufferSource.getBuffer(AttachmentRenderType.getReticle(sightID)), packedLight, packedOverlay, red, green, blue, alpha);
                    }
                }
            }
            case "leftArm", "rightArm" ->
            {
                bone.setHidden(true);

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

                playerArm.setPivot(bone.getPivotX(), bone.getPivotY(), bone.getPivotZ());
                playerArm.setAngles(0, 0, 0);
                playerArm.render(poseStack, arm, packedLight, packedOverlay, 1, 1, 1, alpha);

                playerSleeve.setPivot(bone.getPivotX(), bone.getPivotY(), bone.getPivotZ());
                playerSleeve.setAngles(0, 0, 0);
                playerSleeve.render(poseStack, sleeve, packedLight, packedOverlay, 1, 1, 1, alpha);
            }
        }

        super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer1, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
        poseStack.pop();
    }

    //=====Transforms=====//
    private void aimTransforms(MatrixStack poseStack, float f, float posX, float posY)
    {
        GeoBone adsNode = getGeoModel().getBone("ads_node").orElse(null);
        float adsAdjustHeight = 0f;
        float adsAdjustForward = 0f;

        if (adsNode != null)
        {
            adsAdjustHeight = adsNode.getPivotY();
            adsAdjustForward = adsNode.getPivotZ();
        }

        float centeredX = ((-8.96325f)-(posX*16))/16f;
        float centeredY = 0.50875f - posY - (adsAdjustHeight/16f) + sightAdjust/16f;
        float adjustZ = 10.6f/16f - adsAdjustForward/16f;

        poseStack.translate(centeredX * f, centeredY * f, adjustZ * f);
    }
    private void recoilTransforms(MatrixStack poseStack, float rotX, float rotY, float moveY, float moveZ)
    {
        poseStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(rotY));
        poseStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(rotX));
        poseStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(rotX));
        poseStack.translate(0,moveY,(moveZ/16f));
    }
    //====================//
}