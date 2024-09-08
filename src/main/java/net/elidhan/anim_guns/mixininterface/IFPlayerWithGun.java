package net.elidhan.anim_guns.mixininterface;


public interface IFPlayerWithGun
{
    void startReload();
    void stopReload();
    void setReloading(boolean reloading);
    boolean isReloading();
    int getReloadProgressTick();
    void setReloadProgressTick(int tick);

    void toggleAim(boolean b);
    boolean isAiming();

    void melee();
    int getMeleeProgress();


}
