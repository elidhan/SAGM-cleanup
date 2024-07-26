package net.elidhan.anim_guns.network;

import mod.azure.azurelib.animatable.GeoItem;
import mod.azure.azurelib.core.animatable.GeoAnimatable;
import mod.azure.azurelib.core.animation.AnimationController;
import net.elidhan.anim_guns.AnimatedGuns;
import net.elidhan.anim_guns.animations.AnimationHandler;
import net.elidhan.anim_guns.client.RecoilHandler;
import net.elidhan.anim_guns.item.GunItem;
import net.elidhan.anim_guns.item.GunMagFedItem;
import net.elidhan.anim_guns.item.GunSingleLoaderItem;
import net.elidhan.anim_guns.mixininterface.IFPlayerWithGun;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class ModNetworking
{
    //Client-to-Server
    public static final Identifier C2S_RELOAD = new Identifier(AnimatedGuns.MOD_ID, "c2s_reload");
    public static final Identifier C2S_MELEE = new Identifier(AnimatedGuns.MOD_ID, "c2s_melee");
    public static final Identifier C2S_SHOOT = new Identifier(AnimatedGuns.MOD_ID, "c2s_shoot");
    public static void registerC2SPackets()
    {
        ServerPlayNetworking.registerGlobalReceiver(C2S_RELOAD, (server, player, serverPlayNetworkHandler, buf, packetSender) ->
        {
            if (player.getMainHandStack().getOrCreateNbt().getInt("ammo") < ((GunItem)player.getMainHandStack().getItem()).getMagSize() && !((IFPlayerWithGun) player).isReloading())
            {
                ((IFPlayerWithGun) player).startReload();

                if (player.getMainHandStack().getItem() instanceof GunMagFedItem)
                    AnimationHandler.playAnim(player, (player).getMainHandStack(), GeoItem.getId((player).getMainHandStack()), "reloading");
                else if (player.getMainHandStack().getItem() instanceof GunSingleLoaderItem)
                    AnimationHandler.playAnim(player, (player).getMainHandStack(), GeoItem.getId((player).getMainHandStack()), "reload_0");
            }
        });
        ServerPlayNetworking.registerGlobalReceiver(C2S_MELEE, (server, player, serverPlayNetworkHandler, buf, packetSender) ->
        {
            if (player instanceof IFPlayerWithGun && ((IFPlayerWithGun) player).getMeleeProgress() <= 0)
                ((IFPlayerWithGun) player).melee();
        });
        ServerPlayNetworking.registerGlobalReceiver(C2S_SHOOT, ((server, player, handler, buf, responseSender) ->
        {
            GunItem gun = (GunItem)(player.getMainHandStack().getItem());
            gun.shoot(player, player.getMainHandStack());
        }));
    }
    //Server-to-Client
    public static final Identifier S2C_RECOIL = new Identifier(AnimatedGuns.MOD_ID, "s2c_recoil");
    public static final Identifier S2C_PLAYANIM = new Identifier(AnimatedGuns.MOD_ID, "s2c_playanim");
    public static void registerS2CPackets()
    {
        ClientPlayNetworking.registerGlobalReceiver(S2C_RECOIL, ((client, handler, buf, responseSender) ->
        {
            client.execute(() ->
            {
                GunItem gun = (GunItem)(client.player.getMainHandStack().getItem());
                RecoilHandler.getInstance().shot(gun.getRecoilX(),gun.getRecoilY(),gun.getViewModelRecoil());
            });
        }));
        ClientPlayNetworking.registerGlobalReceiver(S2C_PLAYANIM, ((client, handler, buf, responseSender) ->
        {
            long id = buf.readLong();
            ItemStack stack = buf.readItemStack();
            String animation = buf.readString();

            client.execute(() ->
            {
                GeoAnimatable animatable = (GeoAnimatable)stack.getItem();

                AnimationController<GeoAnimatable> animationController = animatable.getAnimatableInstanceCache().getManagerForId(id).getAnimationControllers().get("controller");

                animationController.setTransitionLength(animation.equals("firing") ? 0 : 1);

                if(animationController.getCurrentAnimation().animation().name().equals(animation) && !animation.equals("idle"))
                {
                    animationController.forceAnimationReset();
                }
                else
                {
                    animationController.tryTriggerAnimation(animation);
                }
            });
        }));
    }
}
