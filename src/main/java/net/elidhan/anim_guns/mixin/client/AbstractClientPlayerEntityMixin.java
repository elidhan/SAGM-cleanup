package net.elidhan.anim_guns.mixin.client;

import com.mojang.authlib.GameProfile;
import net.elidhan.anim_guns.mixininterface.IFClientPlayerWithGun;
import net.elidhan.anim_guns.mixininterface.IFPlayerWithGun;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(AbstractClientPlayerEntity.class)
public abstract class AbstractClientPlayerEntityMixin extends PlayerEntity implements IFClientPlayerWithGun
{
    @Unique
    private int aimTick = 0;
    @Unique
    private int prevAimTick = 0;

    public AbstractClientPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile)
    {
        super(world, pos, yaw, gameProfile);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void tick(CallbackInfo ci)
    {
        this.prevAimTick = this.aimTick;

        if (((IFPlayerWithGun)this).isAiming() && this.aimTick < 4)
            this.aimTick++;
        else if (!((IFPlayerWithGun)this).isAiming() && this.aimTick > 0)
            this.aimTick--;
    }

    @Override
    public int getAimTick()
    {
        return aimTick;
    }

    @Override
    public int getPrevAimTick()
    {
        return prevAimTick;
    }

    @Inject(method = "getFovMultiplier", at = @At("TAIL"), cancellable = true)
    public void zoomLevel(CallbackInfoReturnable<Float> ci){
        ItemStack gun = this.getMainHandStack();

        if(this instanceof IFPlayerWithGun player && player.isAiming() && MinecraftClient.getInstance().options.getPerspective().isFirstPerson())
        {
            NbtCompound nbtCompound = gun.getOrCreateNbt();
            if(nbtCompound.getBoolean("isScoped"))
            {
                ci.setReturnValue(0.0625f);
            }
            else{
                ci.setReturnValue(0.75f);
            }
        }
    }
}