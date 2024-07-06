package net.elidhan.anim_guns.network;

import net.elidhan.anim_guns.AnimatedGuns;
import net.elidhan.anim_guns.item.GunItem;
import net.elidhan.anim_guns.mixininterface.IFPlayerWIthGun;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;

public class ModNetworking
{
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

        });
        ServerPlayNetworking.registerGlobalReceiver(C2S_AIM, (server, player, serverPlayNetworkHandler, buf, packetSender) ->
        {

        });
        ServerPlayNetworking.registerGlobalReceiver(C2S_SHOOT, ((server, player, handler, buf, responseSender) ->
        {
            ((GunItem)(player.getMainHandStack().getItem())).shoot(player);
        }));
    }

    public static final Identifier S2C_RECOIL = new Identifier(AnimatedGuns.MOD_ID, "s2c_recoil");
    public static void registerS2CPackets()
    {
        ClientPlayNetworking.registerGlobalReceiver(S2C_RECOIL, ((client, handler, buf, responseSender) ->
        {

        }));
    }
}
