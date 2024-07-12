package net.elidhan.anim_guns.mixin;

import mod.azure.azurelib.core.utils.MathUtils;
import net.elidhan.anim_guns.item.GunItem;
import net.elidhan.anim_guns.mixininterface.IFPlayerWIthGun;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements IFPlayerWIthGun
{
    private static final TrackedData<Boolean> IS_AIMING = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Integer> AIM_TICK = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> PREVIOUS_AIM_TICK = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> RELOAD_TICK = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Boolean> IS_RELOADING = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private ItemStack currentGun;
    private int meleeTick;
    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {super(entityType, world);}

    @Inject(method = "tickMovement", at = @At("HEAD"))
    public void getCurrentGun(CallbackInfo ci)
    {
        if (this.getMainHandStack().getItem() instanceof GunItem)
            this.currentGun = this.getMainHandStack();
        else
            this.currentGun = ItemStack.EMPTY;
    }
    @Inject(method = "tickMovement", at = @At("TAIL"))
    public void tickMovement(CallbackInfo ci)
    {
        this.dataTracker.set(PREVIOUS_AIM_TICK, this.dataTracker.get(AIM_TICK));

        if(meleeTick > 0)
            meleeTick--;

        if(!this.dataTracker.get(IS_AIMING) && this.dataTracker.get(AIM_TICK) > 0)
            this.dataTracker.set(AIM_TICK, MathUtils.clamp(this.dataTracker.get(AIM_TICK) - 1, 0, 4));
        else if (this.dataTracker.get(IS_AIMING) && this.dataTracker.get(AIM_TICK) < 4)
            this.dataTracker.set(AIM_TICK, MathUtils.clamp(this.dataTracker.get(AIM_TICK) + 1, 0, 4));

        if(isReloading())
            tickReload();
    }

    //=====Reloading=====//

    @Override
    public void startReload() {setReloading(true);}
    @Override
    public void stopReload()
    {
        setReloadProgressTick(0);
        setReloading(false);
    }
    private void tickReload()
    {
        if (this.currentGun == ItemStack.EMPTY)
        {
            stopReload();
            return;
        }

        if (getReloadProgressTick() >= ((GunItem)(currentGun.getItem())).getReloadTime())
        {
            stopReload();
            return;
        }
        setReloadProgressTick(getReloadProgressTick()+1);
    }
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
        meleeTick = 10;
        this.sendMessage(Text.literal("Melee"));
    }
    @Override
    public int getMeleeProgress() {return meleeTick;}
    //===================//

    //=======Aiming=======//
    @Override
    public void startAim()
    {
        sendMessage(Text.literal("AimTicks: "+(this.dataTracker.get(AIM_TICK))));
        this.dataTracker.set(IS_AIMING, true);
    }
    @Override
    public void stopAim() {this.dataTracker.set(IS_AIMING, false);}
    @Override
    public boolean isAiming() {return this.dataTracker.get(IS_AIMING);}
    @Override
    public int getAimTick() {return this.dataTracker.get(AIM_TICK);}
    @Override
    public int getPreviousAimTick() {return this.dataTracker.get(PREVIOUS_AIM_TICK);}
    //====================//

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