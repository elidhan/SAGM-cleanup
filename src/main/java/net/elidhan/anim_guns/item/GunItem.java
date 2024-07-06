package net.elidhan.anim_guns.item;

import mod.azure.azurelib.animatable.GeoItem;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animation.AnimatableManager;
import net.elidhan.anim_guns.mixininterface.IFPlayerWIthGun;
import net.fabricmc.fabric.api.item.v1.FabricItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class GunItem extends Item implements FabricItem, GeoItem
{
    private final float damage;
    private final int fireRate;
    private final int reloadTime;
    private final int[] reloadStages;
    private final float[] spread;
    private final float[] recoil;
    public GunItem(Settings settings, float damage, int fireRate, int reloadTime, int[] reloadStages, float[] spread, float[] recoil)
    {
        super(settings);
        this.damage = damage;
        this.fireRate = fireRate;
        this.reloadTime = reloadTime;
        this.reloadStages = reloadStages;
        this.spread = spread;
        this.recoil = recoil;
    }

    public void shoot(PlayerEntity player)
    {
        if(player.getItemCooldownManager().isCoolingDown(this)) return;

        player.getItemCooldownManager().set(this, this.fireRate);
        player.sendMessage(Text.literal("Shot"));
    }
    public void tickReload(ItemStack gun, NbtCompound gunNbt) {}
    public void stopReload(ItemStack gun, NbtCompound gunNbt) {}

    //Aiming stuff
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand)
    {
        user.setCurrentHand(hand);
        return TypedActionResult.pass(user.getStackInHand(hand));
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks)
    {
        if (world.isClient()) return;

        if(user instanceof IFPlayerWIthGun player && !player.isReloading())
        {
            player.tickAim();
        }
    }
    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks)
    {
        if (world.isClient()) return;

        if(user instanceof IFPlayerWIthGun player)
        {
            player.stopAim();
        }
    }

    //Getters

    public int getReloadTime()
    {
        return reloadTime;
    }

    //Stuff
    @Override
    public boolean isUsedOnRelease(ItemStack stack) {return false;}
    @Override
    public int getMaxUseTime(ItemStack stack) {return 72000;}
    @Override
    public boolean allowNbtUpdateAnimation(PlayerEntity player, Hand hand, ItemStack oldStack, ItemStack newStack) {return false;}

    //AzureLib Stuff
    @Override
    public void createRenderer(Consumer<Object> consumer)
    {

    }
    @Override
    public Supplier<Object> getRenderProvider()
    {
        return null;
    }
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar)
    {

    }
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache()
    {
        return null;
    }
}
