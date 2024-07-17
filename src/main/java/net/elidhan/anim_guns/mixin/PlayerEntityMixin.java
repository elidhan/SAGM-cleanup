package net.elidhan.anim_guns.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.elidhan.anim_guns.item.GunItem;
import net.elidhan.anim_guns.mixininterface.IFPlayerWithGun;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
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
    private static final TrackedData<Integer> AIM_TICK = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.INTEGER);
    @Unique
    private static final TrackedData<Integer> PREVIOUS_AIM_TICK = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.INTEGER);
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
        this.dataTracker.set(PREVIOUS_AIM_TICK, this.dataTracker.get(AIM_TICK));

        if(meleeTick > 0)
            meleeTick--;

        if(!this.dataTracker.get(IS_AIMING) && this.dataTracker.get(AIM_TICK) > 0)
            this.dataTracker.set(AIM_TICK, Math.max(0, this.dataTracker.get(AIM_TICK)-1));
        else if (this.dataTracker.get(IS_AIMING) && this.dataTracker.get(AIM_TICK) < 2)
            this.dataTracker.set(AIM_TICK, Math.max(0, this.dataTracker.get(AIM_TICK)+1));

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

        if (getReloadProgressTick() >= ((GunItem)(this.currentGun.getItem())).getReloadTime())
        {
            this.currentGun.getOrCreateNbt().putInt("ammo", ((GunItem)this.currentGun.getItem()).getMagSize());

            stopReload();
            return;
        }

        setReloadProgressTick(getReloadProgressTick()+1);
    }
    @Unique
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
        if (b) this.setSprinting(false);
    }
    @Override
    public boolean isAiming() {return this.dataTracker.get(IS_AIMING);}
    @Override
    public int getAimTick() {return this.dataTracker.get(AIM_TICK);}
    @Override
    public int getPreviousAimTick() {return this.dataTracker.get(PREVIOUS_AIM_TICK);}
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
        this.dataTracker.startTracking(AIM_TICK, 0);
        this.dataTracker.startTracking(PREVIOUS_AIM_TICK, 0);
    }
}