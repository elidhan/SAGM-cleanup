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


        if(maxAmmo < 20)
        {
            drawContext.drawTextWithShadow(client.textRenderer, text,
                    scaledWidth/2 + client.textRenderer.getWidth(text) + (93 + (maxAmmo*4)),
                    scaledHeight - 35,
                    16777215);

            for (int i = 0; i < maxAmmo; i++)
            {
                int k = (i < maxAmmo-ammoCount) ? 16 : 0;

                drawContext.drawTexture(AMMO_ICONS,
                        (scaledWidth/2) + (i*5 - i ) + client.textRenderer.getWidth(text) + 92,
                        scaledHeight - 39,
                        3, k, 6, 16);
            }
        }
        else
        {
            drawContext.drawTextWithShadow(client.textRenderer, text,
                    scaledWidth/2 + client.textRenderer.getWidth(text) + 115 + (maxAmmo - 20),
                    scaledHeight - 35,
                    16777215);

            for (int i = 0; i < maxAmmo; i++)
            {
                int j = i / (maxAmmo/2);
                int k = (i < maxAmmo-ammoCount) ? 8 : 0;

                drawContext.drawTexture(AMMO_ICONS,
                        (scaledWidth/2) + (i*3-i) + client.textRenderer.getWidth(text) + 92 - (j * maxAmmo),
                        scaledHeight - (39-(8*j)),
                        11, k, 3, 8);
            }
        }
    }
}
