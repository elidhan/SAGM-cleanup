package net.elidhan.anim_guns.client.render;

import mod.azure.azurelib.renderer.GeoItemRenderer;
import mod.azure.azurelib.renderer.GeoRenderer;
import net.elidhan.anim_guns.client.model.AttachmentModel;
import net.elidhan.anim_guns.item.AttachmentItem;
import net.minecraft.util.Identifier;

public class AttachmentRenderer extends GeoItemRenderer<AttachmentItem> implements GeoRenderer<AttachmentItem>
{
    public AttachmentRenderer(Identifier identifier)
    {
        super(new AttachmentModel(identifier));
    }
}