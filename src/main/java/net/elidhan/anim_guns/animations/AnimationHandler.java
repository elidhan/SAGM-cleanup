package net.elidhan.anim_guns.animations;

import net.elidhan.anim_guns.network.ModNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

public class AnimationHandler
{
    public static void playAnim(ServerPlayerEntity player, ItemStack itemStack, long id, String animation)
    {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeLong(id);
        buf.writeItemStack(itemStack);
        buf.writeString(animation);

        ServerPlayNetworking.send(player, ModNetworking.S2C_PLAYANIM, buf);
    }

    public static void stopAnim(ServerPlayerEntity player, ItemStack itemStack, long id)
    {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeLong(id);
        buf.writeItemStack(itemStack);

        ServerPlayNetworking.send(player, ModNetworking.S2C_STOPANIM, buf);
    }
}
