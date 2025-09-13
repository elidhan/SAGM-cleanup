package net.elidhan.anim_guns.client.render;

import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.ModelIdentifier;
import software.bernie.geckolib.cache.GeckoLibCache;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.util.RenderUtils;
import net.elidhan.anim_guns.AnimatedGuns;
import net.elidhan.anim_guns.client.AttachmentRenderType;
import net.elidhan.anim_guns.client.MuzzleFlashRenderType;
import net.elidhan.anim_guns.client.model.GunModel;
import net.elidhan.anim_guns.item.GunItem;
import net.elidhan.anim_guns.mixininterface.IFClientPlayerWithGun;
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

public class GunRenderer extends GeoItemRenderer<GunItem> implements GeoRenderer<GunItem>
{
    private VertexConsumerProvider bufferSource;
    private ModelTransformationMode transformType;

    public GunRenderer(Identifier identifier)
    {
        super(new GunModel(identifier));
    }

    float aimProgress = 0.0f;
    private float sightAdjust = 0.0f;
    private float sightAdjustForward = 0.0f;
    private float muzzleFlashAdjust = 0.0f;

    String sightID = "";
    String gripID = "";
    String muzzleID = "";

    private BakedModel bakedGunModel = null;

    boolean isSilenced = false;
    boolean isScoped = false;

    @Override
    protected void renderInGui(ModelTransformationMode transformType, MatrixStack poseStack, VertexConsumerProvider bufferSource, int packedLight, int packedOverlay)
    {
        if(currentItemStack.isEmpty() || !(currentItemStack.getItem() instanceof GunItem)) return;
        Identifier itemID = Identifier.of("anim_guns",((GunItem)currentItemStack.getItem()).getID());
        BakedModel bakedModel = MinecraftClient.getInstance().getBakedModelManager().getModel(new ModelIdentifier(itemID.withPath(itemID.getPath() + "_gui"), "inventory"));

        poseStack.push();

        bakedModel.getTransformation().getTransformation(transformType).apply(false, poseStack);

        RenderLayer renderLayer = RenderLayers.getItemLayer(currentItemStack, true);
        VertexConsumer vertexConsumer = ItemRenderer.getDirectItemGlintConsumer(bufferSource, renderLayer, true, false);
        MinecraftClient.getInstance().getItemRenderer().renderBakedItemModel(bakedModel, currentItemStack, 15728880, packedOverlay, poseStack, vertexConsumer);

        poseStack.pop();
    }

    @Override
    public void render(ItemStack stack, ModelTransformationMode transformType, MatrixStack poseStack, VertexConsumerProvider bufferSource, int packedLight, int packedOverlay)
    {
        MinecraftClient client = MinecraftClient.getInstance();
        float delta = client.getTickDelta();
        ClientPlayerEntity player = client.player;

        this.bufferSource = bufferSource;
        this.transformType = transformType;

        sightID = ((GunItem)stack.getItem()).getSightID(stack);
        gripID = ((GunItem)stack.getItem()).getGripID(stack);
        muzzleID = ((GunItem)stack.getItem()).getMuzzleID(stack);

        isSilenced = ((GunItem)stack.getItem()).isSilenced(stack);
        isScoped = ((GunItem)stack.getItem()).isScoped(stack);

        bakedGunModel = MinecraftClient.getInstance().getItemRenderer().getModel(stack, player.getWorld(), player, 0);

        float posX = 0;
        float posY = 0;
        float posZ = 0;

        if (bakedGunModel != null)
        {
            posX = bakedGunModel.getTransformation().firstPersonRightHand.translation.x;
            posY = bakedGunModel.getTransformation().firstPersonRightHand.translation.y;
            posZ = bakedGunModel.getTransformation().firstPersonRightHand.translation.z;
        }

        float prevAimTick = (float)((IFClientPlayerWithGun)player).getPrevAimTick();
        float aimTick = (float)((IFClientPlayerWithGun)player).getAimTick();

        aimProgress = MathHelper.clamp((prevAimTick + (aimTick - prevAimTick) * delta)/3f, 0f, 1f);

        // compute sight adjustments now so aimTransforms can use them
        computeSightAdjustments(getGeoModel().getBone("sightPos").orElse(null), posZ);

        if (this.transformType == ModelTransformationMode.FIRST_PERSON_RIGHT_HAND) aimTransforms(poseStack, aimProgress, posX, posY);

        super.render(stack, transformType, poseStack, bufferSource, packedLight, packedOverlay);
    }

    @Override
    public void renderRecursively(MatrixStack poseStack, GunItem animatable, GeoBone bone, RenderLayer renderType, VertexConsumerProvider bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha)
    {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;
        VertexConsumer buffer1 = this.bufferSource.getBuffer(renderType);

        /*
        DistanceX from 0 to center of in-game screen = -8.9675
        DistanceY from 0 to center of in-game screen = 0.50875
        centeredY = (Distance from Y-value 0 in Blockbench to y-center of screen in-game) - (FirstPersonRightHand Y value) - (Iron Sight bone positional Y-value)
         */

        //Get Aim Progress

        //alpha = getCurrentItemStack().getOrCreateNbt().getBoolean("isScoped") && ((IFPlayerWithGun)player).isAiming() ? MathHelper.clamp(1-(f*4), 0 ,1) : 1;

        BakedGeoModel attachmentModel;
        GeoBone attachmentBone;

        //Does different things depending on which bone is being rendered
        switch (bone.getName())
        {
            case "sight_default" -> bone.setHidden(!sightID.equals("default"));
            case "sight_default_down" -> bone.setHidden(sightID.equals("default"));
            case "flash" ->
            {
                bone.setHidden(isSilenced);
                buffer1 = this.bufferSource.getBuffer(MuzzleFlashRenderType.getMuzzleFlash());
                poseStack.translate(0,0,muzzleFlashAdjust);
            }
            case "sightPos" ->
            {
                attachmentModel = GeckoLibCache.getBakedModels().get(new Identifier(AnimatedGuns.MOD_ID,"geo/"+sightID+".geo.json"));

                if (attachmentModel != null)
                {
                    attachmentBone = attachmentModel.getBone("sight").orElse(null);
                    GeoBone reticle = attachmentModel.getBone("reticle").orElse(null);
                    GeoBone scopeBack = attachmentModel.getBone("scopeBack").orElse(null);

                    buffer1 = this.bufferSource.getBuffer(RenderLayer.getEntityTranslucent(new Identifier(AnimatedGuns.MOD_ID, "textures/misc/" + sightID + ".png")));

                    if (attachmentBone != null)
                    {
                        poseStack.push();

                        poseStack.translate(bone.getPivotX() / 16f, bone.getPivotY() / 16f, bone.getPivotZ() / 16f);
                        if (aimProgress < 0.625f || scopeBack == null || this.transformType != ModelTransformationMode.FIRST_PERSON_RIGHT_HAND)
                        {
                            super.renderCubesOfBone(poseStack, attachmentBone, buffer1, packedLight, packedOverlay, red, green, blue, alpha);
                        }
                        if (scopeBack != null)
                        {
                            super.renderCubesOfBone(poseStack, scopeBack, buffer1, packedLight, packedOverlay, red, green, blue, alpha);
                        }
                        if (aimProgress > 0.85f && reticle != null)
                        {
                            super.renderCubesOfBone(poseStack, reticle, this.bufferSource.getBuffer(AttachmentRenderType.getReticle(sightID)), packedLight, packedOverlay, red, green, blue, alpha);
                        }

                        poseStack.pop();
                    }
                }
            }
            case "muzzlePos" ->
            {
                muzzleFlashAdjust = 0.0f;

                attachmentModel = GeckoLibCache.getBakedModels().get(new Identifier(AnimatedGuns.MOD_ID,"geo/"+muzzleID+".geo.json"));

                if (attachmentModel == null) return;

                attachmentBone = attachmentModel.getBone("muzzle").orElse(null);
                GeoBone muzzleEnd = attachmentModel.getBone("end").orElse(null);

                buffer1 = this.bufferSource.getBuffer(RenderLayer.getEntityTranslucent(new Identifier(AnimatedGuns.MOD_ID, "textures/misc/"+muzzleID+".png")));

                poseStack.push();

                if(attachmentBone != null)
                {
                    poseStack.translate(bone.getPivotX()/16f, bone.getPivotY()/16f, bone.getPivotZ()/16f);
                    if (muzzleEnd != null) muzzleFlashAdjust = muzzleEnd.getPivotZ()/16f;

                    super.renderCubesOfBone(poseStack, attachmentBone, buffer1, packedLight, packedOverlay, red, green, blue, alpha);
                }

                poseStack.pop();
            }
            case "gripPos" ->
            {
                attachmentModel = GeckoLibCache.getBakedModels().get(new Identifier(AnimatedGuns.MOD_ID,"geo/"+gripID+".geo.json"));

                if (attachmentModel == null) return;

                attachmentBone = attachmentModel.getBone("grip").orElse(null);

                buffer1 = this.bufferSource.getBuffer(RenderLayer.getEntityTranslucent(new Identifier(AnimatedGuns.MOD_ID, "textures/misc/"+gripID+".png")));

                poseStack.push();

                if(attachmentBone != null)
                {
                    poseStack.translate(bone.getPivotX()/16f, bone.getPivotY()/16f, bone.getPivotZ()/16f);

                    super.renderCubesOfBone(poseStack, attachmentBone, buffer1, packedLight, packedOverlay, red, green, blue, alpha);
                }

                poseStack.pop();
            }
            case "leftArm", "rightArm" ->
            {
                bone.setHidden(true);
                if (this.transformType != ModelTransformationMode.FIRST_PERSON_RIGHT_HAND) break;

                poseStack.push();

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
                poseStack.pop();
            }
        }

        super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer1, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
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
        float adjustZ =  (10.6f/16f - (adsAdjustForward/16f)) + sightAdjustForward/16f;

        poseStack.translate(centeredX * f, centeredY * f, adjustZ * f);
    }

    private void computeSightAdjustments(GeoBone sightPosBone, float posZ)
    {
        //can't believe I actually trusted ChatGPT with helping me fix the stupid sight adjust bug (fuck item frames)
        //the kicker? it actually solved it

        sightAdjust = 0.0f;
        sightAdjustForward = 0.0f;

        if (sightPosBone == null) return;

        try
        {
            BakedGeoModel attachmentModel = GeckoLibCache.getBakedModels().get(new Identifier(AnimatedGuns.MOD_ID, "geo/" + sightID + ".geo.json"));

            if (attachmentModel == null) return;

            GeoBone attachADSNode = attachmentModel.getBone("ads_node").orElse(null);
            GeoBone adsNode = getGeoModel().getBone("ads_node").orElse(null);

            if (attachADSNode == null || adsNode == null) return;

            sightAdjust = adsNode.getPivotY() - sightPosBone.getPivotY() - attachADSNode.getPivotY();
            sightAdjustForward = sightPosBone.getPivotZ() + attachADSNode.getPivotZ();
            sightAdjustForward = Math.abs(sightAdjustForward - adsNode.getPivotZ()) - posZ * 16;

        }
        catch (Exception e)
        {
            sightAdjust = 0.0f;
            sightAdjustForward = 0.0f;
        }
    }


    //====================//
}