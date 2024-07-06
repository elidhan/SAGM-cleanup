package net.elidhan.anim_guns.item;

import net.minecraft.entity.player.PlayerEntity;

public class GunMagFedItem extends GunItem
{


    public GunMagFedItem(Settings settings, float damage, int fireRate, int reloadTime, int[] reloadStages, float[] spread, float[] recoil)
    {
        super(settings, damage, fireRate, reloadTime, reloadStages, spread, recoil);
    }

    @Override
    public void shoot(PlayerEntity player)
    {

    }
}
