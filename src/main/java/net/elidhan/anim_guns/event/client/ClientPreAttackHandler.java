package net.elidhan.anim_guns.event.client;

import net.elidhan.anim_guns.item.GunItem;
import net.elidhan.anim_guns.network.ModNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.event.client.player.ClientPreAttackCallback;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;

public class ClientPreAttackHandler implements ClientPreAttackCallback
{
    @Override
    public boolean onClientPlayerPreAttack(MinecraftClient client, ClientPlayerEntity player, int clickCount)
    {
        ItemStack mainHandItem = player.getMainHandStack();
        if (mainHandItem.getItem() instanceof GunItem)
        {
            ClientPlayNetworking.send(ModNetworking.C2S_SHOOT, PacketByteBufs.empty());

            return true;
        }

        return false;
    }
}
