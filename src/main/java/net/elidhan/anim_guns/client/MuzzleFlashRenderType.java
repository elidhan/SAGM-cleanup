package net.elidhan.anim_guns.client;

import net.elidhan.anim_guns.AnimatedGuns;
import net.minecraft.client.render.*;
import net.minecraft.util.Identifier;

public class MuzzleFlashRenderType extends RenderLayer
{
    public MuzzleFlashRenderType(String name, VertexFormat vertexFormat, VertexFormat.DrawMode drawMode, int expectedBufferSize, boolean hasCrumbling, boolean translucent, Runnable startAction, Runnable endAction)
    {
        super(name, vertexFormat, drawMode, expectedBufferSize, hasCrumbling, translucent, startAction, endAction);
    }

    private static final RenderLayer MUZZLE_FLASH = of("anim_guns:muzzle_flash", VertexFormats.POSITION_COLOR_TEXTURE_LIGHT, VertexFormat.DrawMode.QUADS, 256, false, true, MultiPhaseParameters.builder().program(RenderPhase.POSITION_COLOR_TEXTURE_LIGHTMAP_PROGRAM).texture(new RenderPhase.Texture(new Identifier(AnimatedGuns.MOD_ID, "textures/misc/muzzleflash.png"), false,false)).transparency(RenderPhase.TRANSLUCENT_TRANSPARENCY).build(false));

    public static RenderLayer getMuzzleFlash()
    {
        return MUZZLE_FLASH;
    }
}