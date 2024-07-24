package net.elidhan.anim_guns.mixin.client;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import net.elidhan.anim_guns.item.GunItem;
import net.elidhan.anim_guns.mixininterface.IFPlayerWithGun;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
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

    @Inject(method = "renderHand", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/GameRenderer;bobView(Lnet/minecraft/client/util/math/MatrixStack;F)V", shift = At.Shift.BEFORE))
    private void bobViewGun(MatrixStack matrices, Camera camera, float tickDelta, CallbackInfo ci)
    {
        if (this.client.player instanceof IFPlayerWithGun playerWIthGun
                && ((ClientPlayerEntity)playerWIthGun).getMainHandStack().getItem() instanceof GunItem
                && !playerWIthGun.isAiming())
        {
            gunBobView(matrices, tickDelta);
        }
    }

    @SuppressWarnings("unused")
    @WrapWithCondition(method = "renderHand", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/GameRenderer;bobView(Lnet/minecraft/client/util/math/MatrixStack;F)V"))
    private boolean vanillaBobView(GameRenderer instance, MatrixStack matrices, float tickDelta)
    {
        return !(this.client.player.getMainHandStack().getItem() instanceof GunItem);
    }

    private void gunBobView(MatrixStack matrices, float tickDelta) {
        if (!(this.client.getCameraEntity() instanceof PlayerEntity playerEntity)) {
            return;
        }
        float f = (playerEntity.horizontalSpeed - playerEntity.prevHorizontalSpeed);
        float g = -(playerEntity.horizontalSpeed + f * tickDelta);
        float h = MathHelper.lerp(tickDelta, playerEntity.prevStrideDistance, playerEntity.strideDistance) * 0.25f;

        matrices.translate(MathHelper.sin(g * (float)Math.PI) * h * 0.5f, -Math.abs(MathHelper.cos(g * (float)Math.PI) * h), 0.0);

        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(MathHelper.sin(g * (float)Math.PI) * h * 3.0f));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(Math.abs(MathHelper.cos(g * (float)Math.PI - 0.2f) * h) * 5.0f));
    }
}