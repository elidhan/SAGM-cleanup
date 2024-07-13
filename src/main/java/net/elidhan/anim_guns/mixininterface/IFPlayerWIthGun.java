package net.elidhan.anim_guns.mixininterface;


public interface IFPlayerWIthGun
{
    void startReload();
    void stopReload();
    void setReloading(boolean reloading);
    boolean isReloading();
    int getReloadProgressTick();

    void toggleAim(boolean b);
    int getAimTick();
    int getPreviousAimTick();
    boolean isAiming();

    void melee();
    int getMeleeProgress();


}
