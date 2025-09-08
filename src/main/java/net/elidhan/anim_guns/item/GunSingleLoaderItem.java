package net.elidhan.anim_guns.item;

import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.util.ClientUtils;
import net.elidhan.anim_guns.animations.AnimationHandler;
import net.elidhan.anim_guns.animations.GunAnimations;
import net.elidhan.anim_guns.mixininterface.IFPlayerWithGun;
import net.elidhan.anim_guns.util.InventoryUtil;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class GunSingleLoaderItem extends GunItem
{
    private final int[] reloadStages;

    public GunSingleLoaderItem(Settings settings, String id, Item ammoItem, float damage, int shotCount, int fireRate, int magSize, int reloadTime, SoundEvent shotSound, SoundEvent[] reloadSounds, int[] reloadStages, Vector2f spread, Vector2f cameraRecoil, Vector3f viewModelRecoilMult, AttachmentItem.AttachType[] acceptedAttachmentTypes, GunItem.fireType fireType)
    {
        super(settings, id, ammoItem, damage, shotCount, fireRate, magSize, reloadTime, shotSound, reloadSounds, spread, cameraRecoil, viewModelRecoilMult, acceptedAttachmentTypes, fireType);
        this.reloadStages = reloadStages;
    }

    @Override
    public void tickReload(ServerPlayerEntity player, ItemStack stack, int tick)
    {
        if (tick == getReloadStageTick(0))
            AnimationHandler.playAnim(player, stack, GeoItem.getId(stack), "reload_1");
        if (tick == getReloadStageTick(1))
        {
            stack.getOrCreateNbt().putInt("ammo", stack.getOrCreateNbt().getInt("ammo") + 1);
            InventoryUtil.removeItemFromInventory(player, this.getAmmoItem(), 1);

            if (stack.getOrCreateNbt().getInt("ammo") < this.getMagSize())
            {
                AnimationHandler.playAnim(player, stack, GeoItem.getId(stack), "reload_1");
                ((IFPlayerWithGun)player).setReloadProgressTick(getReloadStageTick(0));
            }
            else
            {
                AnimationHandler.playAnim(player, stack, GeoItem.getId(stack), "reload_2");
            }
        }
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
