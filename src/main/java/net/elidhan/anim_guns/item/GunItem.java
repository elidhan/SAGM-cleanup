package net.elidhan.anim_guns.item;

import mod.azure.azurelib.animatable.GeoItem;
import mod.azure.azurelib.animatable.SingletonGeoAnimatable;
import mod.azure.azurelib.animatable.client.RenderProvider;
import mod.azure.azurelib.cache.AnimatableIdCache;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animation.*;
import mod.azure.azurelib.core.object.PlayState;
import mod.azure.azurelib.util.AzureLibUtil;
import net.elidhan.anim_guns.AnimatedGuns;
import net.elidhan.anim_guns.animations.AnimationHandler;
import net.elidhan.anim_guns.animations.GunAnimations;
import net.elidhan.anim_guns.client.render.GunRenderer;
import net.elidhan.anim_guns.entity.projectile.BulletProjectileEntity;
import net.elidhan.anim_guns.mixininterface.IFPlayerWithGun;
import net.elidhan.anim_guns.network.ModNetworking;
import net.elidhan.anim_guns.util.BulletUtil;
import net.fabricmc.fabric.api.item.v1.FabricItem;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class GunItem extends Item implements FabricItem, GeoItem
{
    private final String id;
    private final float damage;
    private final int fireRate;
    private final int magSize;
    private final int reloadTime;
    private final int[] reloadStages;
    private final float[] spread;
    private final float[] recoil;
    private final float[] viewModelRecoil;

    protected final Supplier<Object> renderProvider = GeoItem.makeRenderer(this);
    protected final AnimatableInstanceCache animationCache = AzureLibUtil.createInstanceCache(this);

    public GunItem(Settings settings, String id, float damage, int fireRate, int magSize, int reloadTime, int[] reloadStages, float[] spread, float[] recoil, float[] viewModelRecoil)
    {
        super(settings);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);

        this.id = id;
        this.damage = damage;
        this.fireRate = fireRate;
        this.magSize = magSize;
        this.reloadTime = reloadTime;
        this.reloadStages = reloadStages; //Reload stages exactly 4 values
        this.spread = spread; //Spread array should contain exactly 2 values
        this.recoil = recoil; //Recoil array should have exactly 3 values, 3rd value is aim recoil multiplier for view model
        this.viewModelRecoil = viewModelRecoil; // Should contain 5 values: rotate up-down, rotate side, move up-down, move forward, and duration
    }

    //=====Give ID=====//
    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected)
    {
        //Assign ID if no ID
        if(!stack.getOrCreateNbt().contains(ID_NBT_KEY, NbtElement.NUMBER_TYPE) && world instanceof ServerWorld)
            stack.getOrCreateNbt().putLong(ID_NBT_KEY, AnimatableIdCache.getFreeId((ServerWorld) world));
    }
    //=================//

    //=====Shooty Shooty=====//
    public void shoot(ServerPlayerEntity player, ItemStack stack)
    {
        if (!stack.getOrCreateNbt().contains("ammo")) stack.getOrCreateNbt().putInt("ammo", 0);

        if (stack.getOrCreateNbt().getInt("ammo") <= 0) return;
        if(player.getItemCooldownManager().isCoolingDown(this)) return;

        player.getItemCooldownManager().set(this, this.fireRate);

        //Actually shoot
        BulletProjectileEntity bullet = new BulletProjectileEntity(player, player.getWorld(), this.damage, 1);
        bullet.setPosition(player.getX(), player.getEyeY(), player.getZ());

        double spreadX = -spread[0] + Math.random() * (spread[0] - (-spread[0]));
        double spreadY = -spread[1] + Math.random() * (spread[1] - (-spread[1]));

        Vec3d vertiSpread = BulletUtil.vertiSpread(player, spreadX);
        Vec3d horiSpread = BulletUtil.horiSpread(player, spreadY);

        Vec3d result = player.getRotationVector().add(vertiSpread).add(horiSpread);

        bullet.setVelocity(result.getX(), result.getY(), result.getZ(), 20, 0);
        bullet.setBaseVel(bullet.getVelocity());
        bullet.setOwner(player);

        player.getWorld().spawnEntity(bullet);

        //Bullet -1
        if (!player.isCreative()) stack.getOrCreateNbt().putInt("ammo", stack.getOrCreateNbt().getInt("ammo")-1);

        //Animation
        AnimationHandler.playAnim(player, stack, GeoItem.getId(stack), "firing");
        //Recoil
        ServerPlayNetworking.send(player, ModNetworking.S2C_RECOIL, PacketByteBufs.empty());
    }
    //======================//

    public void tickReload(ItemStack gun, NbtCompound gunNbt) {}
    public void stopReload(ItemStack gun, NbtCompound gunNbt) {}

    //=====Aiming stuff=====//
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand)
    {
        if (hand != Hand.MAIN_HAND) return TypedActionResult.fail(user.getStackInHand(hand));

        if(world instanceof ServerWorld && user instanceof IFPlayerWithGun player && !player.isReloading())
            player.toggleAim(!player.isAiming());

        return TypedActionResult.pass(user.getStackInHand(hand));
    }

    //=====Getters=====//
    public int getReloadTime() {return reloadTime;}
    public float getRecoilX() {return recoil[0];}
    public float getRecoilY() {return recoil[1];}
    public String getID() {return this.id;}
    public int getMagSize() {return magSize;}
    public float getAimVMRecoilMult() {return this.recoil[2];}
    public float[] getViewModelRecoil() {return this.viewModelRecoil;}
    //=================//

    //=====Stuff=====//
    @Override
    public boolean isUsedOnRelease(ItemStack stack) {return false;}
    @Override
    public int getMaxUseTime(ItemStack stack) {return 72000;}
    @Override
    public boolean allowNbtUpdateAnimation(PlayerEntity player, Hand hand, ItemStack oldStack, ItemStack newStack) {return false;}
    //==============//

    //=====AzureLib Stuff=====//
    @Override
    public void createRenderer(Consumer<Object> consumer)
    {
        consumer.accept(new RenderProvider()
        {
            private final GunRenderer renderer = new GunRenderer(new Identifier(AnimatedGuns.MOD_ID, id));

            @Override
            public BuiltinModelItemRenderer getCustomRenderer()
            {
                return this.renderer;
            }
        });
    }
    @Override
    public Supplier<Object> getRenderProvider() {return this.renderProvider;}
    private PlayState predicate(AnimationState<GunItem> event)
    {
        if (event.getController().getCurrentAnimation() == null || event.getController().getAnimationState() == AnimationController.State.STOPPED)
            event.getController().tryTriggerAnimation("idle");

        return PlayState.CONTINUE;
    }
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers)
    {
        AnimationController<GunItem> controller = new AnimationController<>(this, "controller", 1, this::predicate)
                .triggerableAnim("idle", GunAnimations.IDLE)
                .triggerableAnim("firing", GunAnimations.FIRING);

        controllers.add(controller);
    }
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {return this.animationCache;}
    //==========================//
}
