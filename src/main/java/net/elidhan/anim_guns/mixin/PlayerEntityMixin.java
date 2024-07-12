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
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements IFPlayerWIthGun
{
    @Shadow public abstract void sendMessage(Text message, boolean overlay);

    private static final TrackedData<Integer> RELOAD_TICK = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private int reloadTick;
    private int aimTick;
    private boolean isReloading;
    private boolean isAiming = false;
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
        if(meleeTick > 0)
            meleeTick--;
        if(!isAiming && aimTick > 0)
            aimTick--;

        if(isReloading())
            tickReload();
    }

    //=====Reloading=====//

    @Override
    public void startReload()
    {
        setReloading(true);
    }
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
        sendMessage(Text.literal(String.valueOf(getReloadProgressTick())));
        setReloadProgressTick(getReloadProgressTick()+1);
    }
    public void setReloadProgressTick(int reloadTick)
    {
        this.reloadTick = reloadTick;
    }
    @Override
    public int getReloadProgressTick()
    {
        return this.reloadTick;
    }
    @Override
    public void setReloading(boolean reloading) {this.isReloading = reloading;}
    @Override
    public boolean isReloading()
    {
        return this.isReloading;
    }
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
    public void tickAim()
    {
        this.isAiming = true;
        sendMessage(Text.literal("Aim Ticks: "+(this.aimTick)));
        this.aimTick = MathUtils.clamp(++this.aimTick, 1, 20);
    }
    @Override
    public void stopAim() {this.isAiming = false;}
    @Override
    public int getAimTick() {return this.aimTick;}
    //====================//
}