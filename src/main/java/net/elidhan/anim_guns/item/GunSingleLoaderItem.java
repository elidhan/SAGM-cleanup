package net.elidhan.anim_guns.item;

import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;

public class GunSingleLoaderItem extends GunItem
{

    public GunSingleLoaderItem(Settings settings, float damage, int fireRate, int reloadTime, int[] reloadStages, float[] spread, float[] recoil)
    {
        super(settings, damage, fireRate, reloadTime, reloadStages, spread, recoil);
    }

    @Override
    public void shoot(ServerPlayerEntity player, ItemStack stack)
    {
        super.shoot(player, stack);
    }
}
