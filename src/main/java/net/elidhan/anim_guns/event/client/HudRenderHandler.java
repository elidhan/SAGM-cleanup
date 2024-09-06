package net.elidhan.anim_guns.event.client;

import net.elidhan.anim_guns.AnimatedGuns;
import net.elidhan.anim_guns.item.GunItem;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.awt.*;

public class HudRenderHandler implements HudRenderCallback
{
    private static final Identifier AMMO_ICONS = new Identifier(AnimatedGuns.MOD_ID, "textures/gui/ammo_icons.png");

    @Override
    public void onHudRender(DrawContext drawContext, float tickDelta)
    {
        MinecraftClient client = MinecraftClient.getInstance();
        if (!(client.player.getMainHandStack().getItem() instanceof GunItem)) return;

        int ammoCount = client.player.getMainHandStack().getOrCreateNbt().getInt("ammo");
        int maxAmmo = ((GunItem)client.player.getMainHandStack().getItem()).getMagSize();

        int scaledWidth = drawContext.getScaledWindowWidth();
        int scaledHeight = drawContext.getScaledWindowHeight();

        Text text = Text.translatable(ammoCount + "/" + maxAmmo);

        drawContext.drawTextWithShadow(client.textRenderer, text, scaledWidth/2 + 93,scaledHeight - 22  , 16777215);
        if(ammoCount < 20)
        {
            for (int i = 0; i < ammoCount; i++)
            {
                drawContext.drawTexture(AMMO_ICONS, scaledWidth/2 + 95 + (i*6)-(i), scaledHeight - 18, 0, 0, 8, 16);
            }
        }
    }
}
