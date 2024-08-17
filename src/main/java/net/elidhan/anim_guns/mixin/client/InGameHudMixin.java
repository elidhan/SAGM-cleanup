package net.elidhan.anim_guns.mixin.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.elidhan.anim_guns.AnimatedGuns;
import net.elidhan.anim_guns.client.RecoilHandler;
import net.elidhan.anim_guns.item.GunItem;
import net.elidhan.anim_guns.mixininterface.IFPlayerWithGun;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin
{
    @Shadow @Final private MinecraftClient client;

    @Unique
    private int scaledWidth;
    @Unique
    private int scaledHeight;
    @Unique
    private static final Identifier GUN_SCOPE = new Identifier(AnimatedGuns.MOD_ID, "textures/misc/gun_scope.png");
    @Unique
    private float gunScopeScale;

    @Unique
    public void simple_Animated_Guns$renderGunScopeOverlay(float scale) {
        float progress = MathHelper.clamp(scale, 0, 1);
        float f;
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorTexProgram);
        RenderSystem.setShaderTexture(0, GUN_SCOPE);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        float g = f = (float)Math.min(this.scaledWidth, this.scaledHeight);
        float h = Math.min((float)this.scaledWidth / f, (float)this.scaledHeight / g) * scale;
        float i = f * h;
        float j = g * h;
        float k = ((float)this.scaledWidth - i) / 2.0f;
        float l = ((float)this.scaledHeight - j) / 2.0f;
        float m = k + i;
        float n = l + j;
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE);
        bufferBuilder.vertex(k, n, -90.0).color(255,255,255,(int)(255 * progress)).texture(0.0f, 1.0f).next();
        bufferBuilder.vertex(m, n, -90.0).color(255,255,255,(int)(255 * progress)).texture(1.0f, 1.0f).next();
        bufferBuilder.vertex(m, l, -90.0).color(255,255,255,(int)(255 * progress)).texture(1.0f, 0.0f).next();
        bufferBuilder.vertex(k, l, -90.0).color(255,255,255,(int)(255 * progress)).texture(0.0f, 0.0f).next();
        tessellator.draw();
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        bufferBuilder.vertex(0.0, this.scaledHeight, -90.0).color(0, 0, 0, (int)(255 * progress)).next();
        bufferBuilder.vertex(this.scaledWidth, this.scaledHeight, -90.0).color(0, 0, 0, (int)(255 * progress)).next();
        bufferBuilder.vertex(this.scaledWidth, n, -90.0).color(0, 0, 0, (int)(255 * progress)).next();
        bufferBuilder.vertex(0.0, n, -90.0).color(0, 0, 0, (int)(255 * progress)).next();
        bufferBuilder.vertex(0.0, l, -90.0).color(0, 0, 0, (int)(255 * progress)).next();
        bufferBuilder.vertex(this.scaledWidth, l, -90.0).color(0, 0, 0, (int)(255 * progress)).next();
        bufferBuilder.vertex(this.scaledWidth, 0.0, -90.0).color(0, 0, 0, (int)(255 * progress)).next();
        bufferBuilder.vertex(0.0, 0.0, -90.0).color(0, 0, 0, (int)(255 * progress)).next();
        bufferBuilder.vertex(0.0, n, -90.0).color(0, 0, 0, (int)(255 * progress)).next();
        bufferBuilder.vertex(k, n, -90.0).color(0, 0, 0, (int)(255 * progress)).next();
        bufferBuilder.vertex(k, l, -90.0).color(0, 0, 0, (int)(255 * progress)).next();
        bufferBuilder.vertex(0.0, l, -90.0).color(0, 0, 0, (int)(255 * progress)).next();
        bufferBuilder.vertex(m, n, -90.0).color(0, 0, 0, (int)(255 * progress)).next();
        bufferBuilder.vertex(this.scaledWidth, n, -90.0).color(0, 0, 0, (int)(255 * progress)).next();
        bufferBuilder.vertex(this.scaledWidth, l, -90.0).color(0, 0, 0, (int)(255 * progress)).next();
        bufferBuilder.vertex(m, l, -90.0).color(0, 0, 0, (int)(255 * progress)).next();
        tessellator.draw();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void render(DrawContext context, float tickDelta, CallbackInfo ci)
    {
        this.scaledWidth = this.client.getWindow().getScaledWidth();
        this.scaledHeight = this.client.getWindow().getScaledHeight();

        float f = this.client.getLastFrameDuration();
        this.gunScopeScale = MathHelper.lerp(f, this.gunScopeScale, 0.75f);
        boolean aimingScopedGun = this.client.player != null
                && this.client.player.getMainHandStack().getItem() instanceof GunItem
                && this.client.player.getMainHandStack().getOrCreateNbt().getBoolean("isScoped")
                && (this.client.player instanceof IFPlayerWithGun player && player.isAiming());

        if (this.client.options.getPerspective().isFirstPerson())
        {
            if (aimingScopedGun)
            {

            }
            else
            {
                this.gunScopeScale = 0f;
            }
        }
    }

    @Inject(method = "renderCrosshair", at = @At("HEAD"), cancellable = true)
    private void hideCrossHair(DrawContext context, CallbackInfo ci)
    {
        if (this.client.player instanceof IFPlayerWithGun player && player.isAiming()) ci.cancel();
    }
}
