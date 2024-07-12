package net.elidhan.anim_guns.network;

import mod.azure.azurelib.core.animatable.GeoAnimatable;
import mod.azure.azurelib.core.animation.AnimationController;
import net.elidhan.anim_guns.AnimatedGuns;
import net.elidhan.anim_guns.item.GunItem;
import net.elidhan.anim_guns.mixininterface.IFPlayerWIthGun;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class ModNetworking
{
    //Client-to-Server
    public static final Identifier C2S_RELOAD = new Identifier(AnimatedGuns.MOD_ID, "c2s_reload");
    public static final Identifier C2S_MELEE = new Identifier(AnimatedGuns.MOD_ID, "c2s_melee");
    public static final Identifier C2S_AIM = new Identifier(AnimatedGuns.MOD_ID, "c2s_aim");
    public static final Identifier C2S_SHOOT = new Identifier(AnimatedGuns.MOD_ID, "c2s_shoot");
    public static void registerC2SPackets()
    {
        ServerPlayNetworking.registerGlobalReceiver(C2S_RELOAD, (server, player, serverPlayNetworkHandler, buf, packetSender) ->
        {
            if (!((IFPlayerWIthGun) player).isReloading())
                ((IFPlayerWIthGun)player).startReload();
        });
        ServerPlayNetworking.registerGlobalReceiver(C2S_MELEE, (server, player, serverPlayNetworkHandler, buf, packetSender) ->
        {
            if (player instanceof IFPlayerWIthGun && ((IFPlayerWIthGun) player).getMeleeProgress() <= 0)
                ((IFPlayerWIthGun) player).melee();
        });
        ServerPlayNetworking.registerGlobalReceiver(C2S_AIM, (server, player, serverPlayNetworkHandler, buf, packetSender) ->
        {

        });
        ServerPlayNetworking.registerGlobalReceiver(C2S_SHOOT, ((server, player, handler, buf, responseSender) ->
        {
            ((GunItem)(player.getMainHandStack().getItem())).shoot(player, player.getMainHandStack());
        }));
    }
    //Server-to-Client
    public static final Identifier S2C_RECOIL = new Identifier(AnimatedGuns.MOD_ID, "s2c_recoil");
    public static final Identifier S2C_PLAYANIM = new Identifier(AnimatedGuns.MOD_ID, "s2c_playanim");
    public static void registerS2CPackets()
    {
        ClientPlayNetworking.registerGlobalReceiver(S2C_RECOIL, ((client, handler, buf, responseSender) ->
        {

        }));
        ClientPlayNetworking.registerGlobalReceiver(S2C_PLAYANIM, ((client, handler, buf, responseSender) ->
        {
            long id = buf.readLong();
            ItemStack stack = buf.readItemStack();
            String animation = buf.readString();

            GeoAnimatable animatable = (GeoAnimatable)stack.getItem();

            AnimationController<GeoAnimatable> animationController = animatable.getAnimatableInstanceCache().getManagerForId(id).getAnimationControllers().get("controller");

            if(animationController.getCurrentAnimation().animation().name().equals(animation) && !animation.equals("idle"))
            {
                animationController.forceAnimationReset();
            }
            else
            {
                animationController.tryTriggerAnimation(animation);
            }
        }));
    }
}
