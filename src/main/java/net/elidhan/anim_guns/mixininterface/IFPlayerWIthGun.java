package net.elidhan.anim_guns.mixininterface;


public interface IFPlayerWIthGun
{
    void startReload();
    void stopReload();
    void setReloading(boolean reloading);
    boolean isReloading();
    int getReloadProgressTick();

    void startAim();
    void stopAim();
    int getAimTick();
    int getPreviousAimTick();
    boolean isAiming();

    void melee();
    int getMeleeProgress();


}
