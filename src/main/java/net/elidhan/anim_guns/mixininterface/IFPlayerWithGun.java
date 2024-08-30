package net.elidhan.anim_guns.mixininterface;


public interface IFPlayerWithGun
{
    void startReload();
    void stopReload();
    void setReloading(boolean reloading);
    boolean isReloading();
    int getReloadProgressTick();

    void toggleAim(boolean b);
    boolean isAiming();

    void melee();
    int getMeleeProgress();


}
