package net.elidhan.anim_guns.network;

import mod.azure.azurelib.animatable.GeoItem;
import mod.azure.azurelib.core.animatable.GeoAnimatable;
import mod.azure.azurelib.core.animation.AnimationController;
import net.elidhan.anim_guns.AnimatedGuns;
import net.elidhan.anim_guns.animations.AnimationHandler;
import net.elidhan.anim_guns.client.RecoilHandler;
import net.elidhan.anim_guns.item.*;
import net.elidhan.anim_guns.mixininterface.IFPlayerWithGun;
import net.elidhan.anim_guns.util.InventoryUtil;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Identifier;
import org.joml.Vector3f;

public class ModNetworking
{
    //Client-to-Server
    public static final Identifier C2S_RELOAD = new Identifier(AnimatedGuns.MOD_ID, "c2s_reload");
    public static final Identifier C2S_MELEE = new Identifier(AnimatedGuns.MOD_ID, "c2s_melee");
    public static final Identifier C2S_SHOOT = new Identifier(AnimatedGuns.MOD_ID, "c2s_shoot");
    public static final Identifier C2S_PARTICLES = new Identifier(AnimatedGuns.MOD_ID, "c2s_particles");
    public static final Identifier C2S_SELECT_BLUEPRINT = new Identifier(AnimatedGuns.MOD_ID, "c2s_select_blueprint");
    public static void registerC2SPackets()
    {
        ServerPlayNetworking.registerGlobalReceiver(C2S_SELECT_BLUEPRINT, (server, player, serverPlayNetworkHandler, buf, packetSender) ->
        {
            int i = buf.readInt();
            Item blueprint = BlueprintItem.BLUEPRINT_ITEM_LIST.get(i);

            if (player.getMainHandStack().getItem() instanceof BlueprintItem || player.getMainHandStack().getItem() instanceof BlueprintBundleItem) {
                player.getMainHandStack().decrement(1);
            } else if (player.getOffHandStack().getItem() instanceof BlueprintItem || player.getOffHandStack().getItem() instanceof BlueprintBundleItem) {
                player.getOffHandStack().decrement(1);
            }

            if (player.getInventory().getEmptySlot() > -1) {
                player.giveItemStack(new ItemStack(blueprint));
            } else {
                player.dropItem(new ItemStack(blueprint), false, true);
            }

        });
        ServerPlayNetworking.registerGlobalReceiver(C2S_RELOAD, (server, player, serverPlayNetworkHandler, buf, packetSender) ->
        {
            if (player.getMainHandStack().getItem() instanceof GunItem gun
                    && player.getMainHandStack().getOrCreateNbt().getInt("ammo") < ((GunItem)player.getMainHandStack().getItem()).getMagSize()
                    && InventoryUtil.itemCountInInventory(player, gun.getAmmoItem()) > 0
                    && !((IFPlayerWithGun) player).isReloading())
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
            if (player.getMainHandStack().getItem() instanceof GunItem gun) gun.shoot(player, player.getMainHandStack());
        }));
        ServerPlayNetworking.registerGlobalReceiver(C2S_PARTICLES, (server, player, serverPlayNetworkHandler, buf, packetSender) ->
        {

        });
    }
    //Server-to-Client
    public static final Identifier S2C_SHOT = new Identifier(AnimatedGuns.MOD_ID, "s2c_shot");
    public static final Identifier S2C_PLAYANIM = new Identifier(AnimatedGuns.MOD_ID, "s2c_playanim");
    public static final Identifier S2C_STOPANIM = new Identifier(AnimatedGuns.MOD_ID, "s2c_stopanim");
    public static void registerS2CPackets()
    {
        ClientPlayNetworking.registerGlobalReceiver(S2C_SHOT, ((client, handler, buf, responseSender) ->
                client.execute(() ->
                {
                    if (client.player != null && client.player.getMainHandStack().getItem() instanceof GunItem gun)
                    {
                        RecoilHandler.getInstance().shot(
                                gun.getRecoilX() * gun.getRecoilMult(client.player.getMainHandStack()) * (((IFPlayerWithGun) client.player).isAiming() ? 0.5f : 1f),
                                gun.getRecoilY() * gun.getRecoilMult(client.player.getMainHandStack()) * (((IFPlayerWithGun) client.player).isAiming() ? 0.5f : 1f));
                    }
                })));
        ClientPlayNetworking.registerGlobalReceiver(S2C_PLAYANIM, ((client, handler, buf, responseSender) ->
        {
            long id = buf.readLong();
            ItemStack stack = buf.readItemStack();
            String animation = buf.readString();

            client.execute(() ->
            {
                {
                    GunItem animatable = (GunItem)stack.getItem();

                    AnimationController<GeoAnimatable> animationController = animatable.getAnimatableInstanceCache().getManagerForId(id).getAnimationControllers().get(animatable.getID()+"_controller");

                    animationController.setTransitionLength(animation.equals("firing") ? 0 : animation.equals("idle") ? 20 : 1);

                    if (animationController.getCurrentAnimation() == null)
                    {
                        animationController.tryTriggerAnimation(animation);
                        return;
                    }

                    if(animationController.getCurrentAnimation().animation().name().equals(animation) && !animation.equals("idle"))
                    {
                        animationController.forceAnimationReset();
                    }
                    else
                    {
                        animationController.tryTriggerAnimation(animation);
                    }
                }
            });
        }));
        ClientPlayNetworking.registerGlobalReceiver(S2C_STOPANIM, ((client, handler, buf, responseSender) ->
        {
            long id = buf.readLong();
            ItemStack stack = buf.readItemStack();

            client.execute(() ->
            {
                GunItem animatable = (GunItem)stack.getItem();

                AnimationController<GeoAnimatable> animationController = animatable.getAnimatableInstanceCache().getManagerForId(id).getAnimationControllers().get(animatable.getID()+"_controller");

                animationController.stop();
            });
        }));
    }
}
