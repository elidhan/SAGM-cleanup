package net.elidhan.anim_guns.item;

public class GunSingleLoaderItem extends GunItem
{
    public GunSingleLoaderItem(Settings settings, String id, float damage, int fireRate, int magSize, int reloadTime, int[] reloadStages, float[] spread, float[] recoil, float[] viewModelRecoil) {
        super(settings, id, damage, fireRate, magSize, reloadTime, reloadStages, spread, recoil, viewModelRecoil);
    }
}
