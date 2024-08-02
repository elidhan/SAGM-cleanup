package net.elidhan.anim_guns.client;

import net.elidhan.anim_guns.AnimatedGuns;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;

public class AttachmentRenderType extends RenderLayer
{
    public AttachmentRenderType(String name, VertexFormat vertexFormat, VertexFormat.DrawMode drawMode, int expectedBufferSize, boolean hasCrumbling, boolean translucent, Runnable startAction, Runnable endAction)
    {
        super(name, vertexFormat, drawMode, expectedBufferSize, hasCrumbling, translucent, startAction, endAction);
    }

    public static RenderLayer getReticle(String attachmentID)
    {
        return RenderLayer.of(AnimatedGuns.MOD_ID+":reticle", VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, VertexFormat.DrawMode.QUADS, 256, true, false, MultiPhaseParameters.builder().program(RenderPhase.EYES_PROGRAM).texture(new RenderPhase.Texture(new Identifier(AnimatedGuns.MOD_ID, "textures/misc/"+attachmentID+".png"), false, false)).transparency(RenderPhase.TRANSLUCENT_TRANSPARENCY).build(false));
    }

    public static RenderLayer getAttachment(String attachmentID)
    {
        if (attachmentID.equals("default")) return RenderLayer.getEntityTranslucent(new Identifier(AnimatedGuns.MOD_ID + ":textures/misc/sight_holo.png"));

        MultiPhaseParameters multiPhaseParameters = MultiPhaseParameters.builder().program(RenderPhase.ENTITY_TRANSLUCENT_PROGRAM).texture(new RenderPhase.Texture(new Identifier(AnimatedGuns.MOD_ID, "textures/misc/"+attachmentID+".png"), false, false)).lightmap(ENABLE_LIGHTMAP).transparency(RenderPhase.TRANSLUCENT_TRANSPARENCY).build(false);
        return RenderLayer.of(AnimatedGuns.MOD_ID+":"+attachmentID, VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, VertexFormat.DrawMode.QUADS, 256, true, false, multiPhaseParameters);
    }
}