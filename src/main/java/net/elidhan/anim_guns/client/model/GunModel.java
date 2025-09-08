package net.elidhan.anim_guns.client.model;

import software.bernie.geckolib.core.molang.MolangParser;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import net.elidhan.anim_guns.AnimatedGuns;
import net.elidhan.anim_guns.client.RecoilHandler;
import net.elidhan.anim_guns.item.GunItem;
import net.elidhan.anim_guns.mixininterface.IFClientPlayerWithGun;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.joml.Vector3f;

public class GunModel extends DefaultedItemGeoModel<GunItem>
{
    public GunModel(Identifier assetSubpath) {
        super(assetSubpath);
    }

    @Override
    public Identifier getModelResource(GunItem object)
    {
        return new Identifier(AnimatedGuns.MOD_ID, "geo/"+object.getID()+".geo.json");
    }

    @Override
    public Identifier getTextureResource(GunItem object)
    {
        return new Identifier(AnimatedGuns.MOD_ID, "textures/item/"+object.getID()+".png");
    }

    @Override
    public Identifier getAnimationResource(GunItem animatable)
    {
        return new Identifier(AnimatedGuns.MOD_ID, "animations/"+animatable.getID()+".animation.json");
    }

    @Override
    public void applyMolangQueries(GunItem animatable, double animTime)
    {
        MolangParser parser = MolangParser.INSTANCE;
        MinecraftClient mc = MinecraftClient.getInstance();
        PlayerEntity player = mc.player;
        float aimProgress = 0;

        if (player instanceof IFClientPlayerWithGun player1)
        {
            //Get Aim Progress
            float prevAimTick = player1.getPrevAimTick();
            float aimTick = player1.getAimTick();

            aimProgress = MathHelper.clamp((prevAimTick + (aimTick - prevAimTick) * mc.getTickDelta())/3f, 0f, 1f);
        }

        if (player != null)
        {
            ItemStack gunStack = player.getMainHandStack();
            if (gunStack.getItem() instanceof GunItem gun)
            {
                Vector3f viewModelRecoilMult = gun.getAimVMRecoilMult();

                float xPosMult = (1 + (viewModelRecoilMult.y() - 1) * aimProgress);
                float xRotMult = (1 + (viewModelRecoilMult.y() - 1) * aimProgress);
                float yRotMult = (1 + (viewModelRecoilMult.x() - 1) * aimProgress) * RecoilHandler.getInstance().getLeftOrRight();
                float zPosMult = (1 + (viewModelRecoilMult.z() - 1) * aimProgress);

                parser.setValue("query.x_pos_mult", () -> xPosMult);
                parser.setValue("query.x_rot_mult", () -> xRotMult);
                parser.setValue("query.y_rot_mult", () -> yRotMult);
                parser.setValue("query.z_pos_mult", () -> zPosMult);
            }
        }

        super.applyMolangQueries(animatable, animTime);
    }
}