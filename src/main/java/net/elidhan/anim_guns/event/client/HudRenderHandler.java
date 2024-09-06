package net.elidhan.anim_guns.event.client;

import net.elidhan.anim_guns.AnimatedGuns;
import net.elidhan.anim_guns.item.GunItem;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class HudRenderHandler implements HudRenderCallback
{
    private static final Identifier AMMO_ICONS = new Identifier(AnimatedGuns.MOD_ID, "textures/gui/ammo_icons.png");

    @Override
    public void onHudRender(DrawContext drawContext, float tickDelta)
    {
        MinecraftClient client = MinecraftClient.getInstance();
        if (!(client.player.getMainHandStack().getItem() instanceof GunItem gun)) return;

        int ammoCount = client.player.getMainHandStack().getOrCreateNbt().getInt("ammo");
        int maxAmmo = gun.getMagSize();

        int scaledWidth = drawContext.getScaledWindowWidth();
        int scaledHeight = drawContext.getScaledWindowHeight();

        Text text = Text.translatable(String.valueOf(ammoCount));
        if (ammoCount < 10) text = Text.translatable("0"+ammoCount);

        drawContext.drawTextWithShadow(client.textRenderer, text, scaledWidth - client.textRenderer.getWidth(text) - 12,scaledHeight - 32  , 16777215);
        if(maxAmmo < 20)
        {
            for (int i = 0; i < maxAmmo; i++)
            {
                drawContext.drawTexture(AMMO_ICONS, scaledWidth - (i*6 - i) - client.textRenderer.getWidth(text) - 20, scaledHeight - 36, 2, 16, 7, 16);
            }
            for (int i = 0; i < ammoCount; i++)
            {
                drawContext.drawTexture(AMMO_ICONS, scaledWidth - (i*6 - i) - client.textRenderer.getWidth(text) - 20, scaledHeight - 36, 2, 0, 7, 16);
            }
        }
        else
        {
            for (int i = 0; i < maxAmmo; i++)
            {
                int j = i / (maxAmmo / 2);
                drawContext.drawTexture(AMMO_ICONS, scaledWidth - (i*3 - i ) - client.textRenderer.getWidth(text) - 20 + (j * maxAmmo), scaledHeight - (28 + (8 * j)), 11, 8, 3, 8);
            }
            for (int i = 0; i < ammoCount; i++)
            {
                int j = i / (maxAmmo / 2);
                drawContext.drawTexture(AMMO_ICONS, scaledWidth - (i*3 - i ) - client.textRenderer.getWidth(text) - 20 + (j * maxAmmo), scaledHeight - (28 + (8 * j)), 11, 0, 3, 8);
            }
        }
    }
}
