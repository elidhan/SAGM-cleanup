package net.elidhan.anim_guns.mixininterface;


public interface IFPlayerWIthGun
{
    void startReload();
    void stopReload();
    void setReloading(boolean reloading);
    boolean isReloading();
    int getReloadProgressTick();

    void tickAim();
    void stopAim();
    int getAimTick();

    void melee();
    int getMeleeProgress();
}
