package net.elidhan.anim_guns.client;

import net.elidhan.anim_guns.AnimatedGuns;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;

public class MuzzleFlashRenderType extends RenderLayer
{
    public MuzzleFlashRenderType(String name, VertexFormat vertexFormat, VertexFormat.DrawMode drawMode, int expectedBufferSize, boolean hasCrumbling, boolean translucent, Runnable startAction, Runnable endAction)
    {
        super(name, vertexFormat, drawMode, expectedBufferSize, hasCrumbling, translucent, startAction, endAction);
    }

    private static final RenderLayer MUZZLE_FLASH = of(AnimatedGuns.MOD_ID+":muzzle_flash", VertexFormats.POSITION_COLOR_TEXTURE_LIGHT, VertexFormat.DrawMode.QUADS, 256, true, false, MultiPhaseParameters.builder().program(RenderPhase.EYES_PROGRAM).texture(new RenderPhase.Texture(new Identifier(AnimatedGuns.MOD_ID, "textures/misc/muzzleflash.png"), false, false)).transparency(TRANSLUCENT_TRANSPARENCY).cull(RenderPhase.DISABLE_CULLING).build(false));
    private static final RenderLayer MUZZLE_SMOKE = of(AnimatedGuns.MOD_ID+":muzzle_smoke", VertexFormats.POSITION_COLOR_TEXTURE, VertexFormat.DrawMode.QUADS, 256, true, false, MultiPhaseParameters.builder().program(RenderPhase.POSITION_COLOR_TEXTURE_PROGRAM).texture(new RenderPhase.Texture(new Identifier(AnimatedGuns.MOD_ID, "textures/misc/muzzlesmoke.png"), false, false)).transparency(TRANSLUCENT_TRANSPARENCY).cull(RenderPhase.DISABLE_CULLING).build(false));

    public static RenderLayer getMuzzleFlash()
    {
        return MUZZLE_FLASH;
    }
    public static RenderLayer getMuzzleSmoke()
    {
        return MUZZLE_SMOKE;
    }
}