package net.elidhan.anim_guns.item;

import mod.azure.azurelib.core.animation.AnimatableManager;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.util.ClientUtils;
import net.elidhan.anim_guns.animations.GunAnimations;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class GunSingleLoaderItem extends GunItem
{
    private final int[] reloadStages;

    public GunSingleLoaderItem(Settings settings, String id, float damage, int shotCount, int fireRate, int magSize, int reloadTime, SoundEvent shotSound, SoundEvent[] reloadSounds, int[] reloadStages, Vector2f spread, Vector2f cameraRecoil, Vector3f viewModelRecoilMult, AttachmentItem.AttachType[] acceptedAttachmentTypes)
    {
        super(settings, id, damage, shotCount, fireRate, magSize, reloadTime, shotSound, reloadSounds, spread, cameraRecoil, viewModelRecoilMult, acceptedAttachmentTypes);
        this.reloadStages = reloadStages;
    }

    public int getReloadStageTick(int stage)
    {
        return this.reloadStages[stage];
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers)
    {
        AnimationController<GunItem> controller = new AnimationController<>(this, this.getID()+"_controller", 1, super::predicate)
                .receiveTriggeredAnimations()
                .triggerableAnim("idle", GunAnimations.IDLE)
                .triggerableAnim("firing", GunAnimations.FIRING)
                .triggerableAnim("reload_0", GunAnimations.RELOAD_0)
                .triggerableAnim("reload_1", GunAnimations.RELOAD_1)
                .triggerableAnim("reload_2", GunAnimations.RELOAD_2).setSoundKeyframeHandler(soundKeyframeEvent ->
                {
                    ClientPlayerEntity player = (ClientPlayerEntity) ClientUtils.getClientPlayer();
                    if (player != null)
                    {
                        switch (soundKeyframeEvent.getKeyframeData().getSound())
                        {
                            case "reload_ready" -> player.playSound(this.reloadSounds[0], SoundCategory.PLAYERS, 1, 1); //optional, can be empty
                            case "remove" -> player.playSound(this.reloadSounds[1], SoundCategory.PLAYERS, 1, 1);
                            case "insert" -> player.playSound(this.reloadSounds[2], SoundCategory.PLAYERS, 1, 1);
                            case "bolt_pull" -> player.playSound(this.reloadSounds[3], SoundCategory.PLAYERS, 1, 1);
                            case "bolt_release" -> player.playSound(this.reloadSounds[4], SoundCategory.PLAYERS, 1, 1);
                            case "reload_finish" -> player.playSound(this.reloadSounds[5], SoundCategory.PLAYERS, 1, 1); //optional, can be empty
                        }
                    }
                });

        controllers.add(controller);
    }
}
