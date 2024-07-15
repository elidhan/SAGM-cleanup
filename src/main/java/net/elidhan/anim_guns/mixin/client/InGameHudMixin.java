package net.elidhan.anim_guns.mixin.client;

import net.elidhan.anim_guns.mixininterface.IFPlayerWithGun;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin
{
    @Shadow @Final private MinecraftClient client;

    @Inject(method = "renderCrosshair", at = @At("HEAD"), cancellable = true)
    private void hideCrossHair(DrawContext context, CallbackInfo ci)
    {
        if(this.client.player instanceof IFPlayerWithGun player && player.isAiming()) ci.cancel();
    }
}
