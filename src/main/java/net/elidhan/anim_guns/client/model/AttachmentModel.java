package net.elidhan.anim_guns.client.model;

import mod.azure.azurelib.model.DefaultedItemGeoModel;
import net.elidhan.anim_guns.AnimatedGuns;
import net.elidhan.anim_guns.item.AttachmentItem;
import net.minecraft.util.Identifier;

public class AttachmentModel extends DefaultedItemGeoModel<AttachmentItem>
{
    /**
     * Create a new instance of this model class.<br>
     * The asset path should be the truncated relative path from the base folder.<br>
     * E.G.
     * <pre>{@code
     * 	new ResourceLocation("myMod", "armor/obsidian")
     * }</pre>
     *
     * @param assetSubpath
     */
    public AttachmentModel(Identifier assetSubpath) {
        super(assetSubpath);
    }

    @Override
    public Identifier getModelResource(AttachmentItem object)
    {
        return new Identifier(AnimatedGuns.MOD_ID, "geo/attach_"+object.attachTypeString()+"_"+object.getId()+".geo.json");
    }

    @Override
    public Identifier getTextureResource(AttachmentItem object)
    {
        return new Identifier(AnimatedGuns.MOD_ID, "textures/misc/attach_"+object.attachTypeString()+"_"+object.getId()+".png");
    }
}