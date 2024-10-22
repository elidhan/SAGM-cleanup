package net.elidhan.anim_guns.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mod.azure.azurelib.animatable.GeoItem;
import net.elidhan.anim_guns.animations.AnimationHandler;
import net.elidhan.anim_guns.item.GunItem;
import net.elidhan.anim_guns.item.GunSingleLoaderItem;
import net.elidhan.anim_guns.mixininterface.IFPlayerWithGun;
import net.elidhan.anim_guns.util.InventoryUtil;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements IFPlayerWithGun
{
    @Unique
    private static final TrackedData<Boolean> IS_AIMING = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    @Unique
    private static final TrackedData<Integer> RELOAD_TICK = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.INTEGER);
    @Unique
    private static final TrackedData<Boolean> IS_RELOADING = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    @Unique
    private ItemStack currentGun = ItemStack.EMPTY;
    @Unique
    private int meleeTick = 0;

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {super(entityType, world);}

    //=====Tick=====//
    @Inject(method = "tickMovement", at = @At("HEAD"))
    public void getCurrentGun(CallbackInfo ci)
    {
        if(this.getMainHandStack().getOrCreateNbt().get("AzureLibID") != this.currentGun.getOrCreateNbt().get("AzureLibID") && this.getWorld() instanceof ServerWorld)
        {
            toggleAim(false);
            stopReload();
        }

        this.currentGun = this.getMainHandStack();
    }
    @Inject(method = "tickMovement", at = @At("TAIL"))
    public void tickMovement(CallbackInfo ci)
    {
        if(meleeTick > 0)
            meleeTick--;

        if(isReloading())
            tickReload();
    }

    //=====Reloading=====//
    @Override
    public void startReload()
    {
        toggleAim(false);
        setReloading(true);
    }
    @Override
    public void stopReload()
    {
        setReloadProgressTick(0);
        setReloading(false);
    }
    @Unique
    private void tickReload()
    {
        if (!(this.getWorld() instanceof ServerWorld)) return;

        GunItem gun = (GunItem)this.currentGun.getItem();

        if(!(gun instanceof GunSingleLoaderItem) && InventoryUtil.itemCountInInventory((ServerPlayerEntity)(Object)this, gun.getAmmoItem()) <= 0)
        {
            if (this.currentGun.getItem() instanceof GunItem) AnimationHandler.stopAnim((ServerPlayerEntity)(Object)this, this.currentGun, GeoItem.getId(this.currentGun));
            stopReload();
        }

        gun.tickReload((ServerPlayerEntity)(Object) this, this.currentGun, getReloadProgressTick());

        if (getReloadProgressTick() >= ((GunItem)(this.currentGun.getItem())).getReloadTime())
        {
            int ammoNeeded = gun.getMagSize() - this.currentGun.getOrCreateNbt().getInt("ammo");
            int ammoAvailable = InventoryUtil.itemCountInInventory((ServerPlayerEntity)(Object)this, gun.getAmmoItem());
            int ammoToPut = Math.min(ammoAvailable, ammoNeeded);

            this.currentGun.getOrCreateNbt().putInt("ammo", ammoToPut + this.currentGun.getOrCreateNbt().getInt("ammo"));
            InventoryUtil.removeItemFromInventory((ServerPlayerEntity)(Object)this, gun.getAmmoItem(), ammoToPut);

            stopReload();
            return;
        }

        setReloadProgressTick(getReloadProgressTick()+1);
    }
    @Override
    public void setReloadProgressTick(int reloadTick) {this.dataTracker.set(RELOAD_TICK, reloadTick);}
    @Override
    public int getReloadProgressTick() {return this.dataTracker.get(RELOAD_TICK);}
    @Override
    public void setReloading(boolean reloading) {this.dataTracker.set(IS_RELOADING, reloading);}
    @Override
    public boolean isReloading() {return this.dataTracker.get(IS_RELOADING);}
    //====================//

    //=======Melee========//
    @Override
    public void melee()
    {
        toggleAim(false);
        meleeTick = 10;
    }
    @Override
    public int getMeleeProgress() {return meleeTick;}

    @SuppressWarnings("all")
    @WrapOperation(method = "attack", constant = @Constant(classValue = SwordItem.class))
    private boolean sweepMeleeIfGun(Object obj, Operation<Boolean> original)
    {
        return original.call(obj) || obj instanceof GunItem;
    }
    @Inject(method = "resetLastAttackedTicks", at = @At("HEAD"), cancellable = true)
    private void dontResetIfGun(CallbackInfo ci)
    {
        if(this.getMainHandStack().getItem() instanceof GunItem) ci.cancel();
    }
    //===================//

    //=======Aiming=======//
    @Override
    public void toggleAim(boolean b)
    {
        this.dataTracker.set(IS_AIMING, b);

        if (b)
        {
            this.setSprinting(false);
        }
    }
    @Override
    public boolean isAiming() {return this.dataTracker.get(IS_AIMING);}
    //====================//

    //=====Dropping Gun=====//
    @Inject(method = "dropItem(Lnet/minecraft/item/ItemStack;ZZ)Lnet/minecraft/entity/ItemEntity;", at = @At("HEAD"))
    public void dropGun(ItemStack stack, boolean throwRandomly, boolean retainOwnership, CallbackInfoReturnable<ItemEntity> cir)
    {
        if (stack.getOrCreateNbt().getInt("AzureLibID") == this.currentGun.getOrCreateNbt().getInt("AzureLibID"))
        {
            this.currentGun = ItemStack.EMPTY;
        }
    }

    //======================//

    //=====Data Track=====//
    @Inject(method = "initDataTracker", at = @At("HEAD"))
    private void initDataTracker(CallbackInfo ci)
    {
        this.dataTracker.startTracking(IS_AIMING, false);
        this.dataTracker.startTracking(IS_RELOADING, false);
        this.dataTracker.startTracking(RELOAD_TICK, 0);
    }
}