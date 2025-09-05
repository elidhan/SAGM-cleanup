package net.elidhan.anim_guns.mixin.client;

import net.elidhan.anim_guns.item.GunItem;
import net.elidhan.anim_guns.mixininterface.IFClientPlayerWithGun;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(GameRenderer.class)
public class GameRendererMixin
{
    @Shadow
    @Final
    MinecraftClient client;

    @Unique
    private boolean isRenderHand = false;

    @Inject(method = "renderWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/GameRenderer;bobView(Lnet/minecraft/client/util/math/MatrixStack;F)V", shift = At.Shift.BEFORE))
    public void isRenderWorld(float tickDelta, long limitTime, MatrixStack matrices, CallbackInfo ci)
    {
        isRenderHand = false;
    }
    @Inject(method = "renderHand", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/GameRenderer;bobView(Lnet/minecraft/client/util/math/MatrixStack;F)V", shift =  At.Shift.BEFORE))
    public void isRenderHand(MatrixStack matrices, Camera camera, float tickDelta, CallbackInfo ci)
    {
        isRenderHand = true;
    }

    @Inject(method = "bobView", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;getCameraEntity()Lnet/minecraft/entity/Entity;", ordinal = 1, shift = At.Shift.AFTER), cancellable = true)
    private void bobIfGun(MatrixStack matrices, float tickDelta, CallbackInfo ci)
    {
        if (!isRenderHand) return;

        if (this.client.player.getMainHandStack().getItem() instanceof GunItem && this.client.player instanceof IFClientPlayerWithGun clientPlayerWithGun)
        {
            float f1 = clientPlayerWithGun.getAimTick();
            float f2 = clientPlayerWithGun.getPrevAimTick();

            float aimProgress = MathHelper.clamp((f2 + (f1 - f2) * tickDelta)/3f, 0f, 1f);

            if (!(this.client.getCameraEntity() instanceof PlayerEntity playerEntity))
            {
                return;
            }
            float f = (playerEntity.horizontalSpeed - playerEntity.prevHorizontalSpeed);
            float g = -(playerEntity.horizontalSpeed + f * tickDelta);
            float h = MathHelper.lerp(tickDelta, playerEntity.prevStrideDistance, playerEntity.strideDistance) * (0.25f * (1f - aimProgress));

            matrices.translate(MathHelper.sin(g * (float)Math.PI) * h * 0.5f, -Math.abs(MathHelper.cos(g * (float)Math.PI) * h), 0.0);

            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(MathHelper.sin(g * (float)Math.PI) * h * 3.0f));
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(Math.abs(MathHelper.cos(g * (float)Math.PI - 0.2f) * h) * 5.0f));

            ci.cancel();
        }
    }
}