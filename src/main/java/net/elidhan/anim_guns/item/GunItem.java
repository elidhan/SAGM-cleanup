package net.elidhan.anim_guns.item;

import mod.azure.azurelib.animatable.GeoItem;
import mod.azure.azurelib.animatable.SingletonGeoAnimatable;
import mod.azure.azurelib.animatable.client.RenderProvider;
import mod.azure.azurelib.cache.AnimatableIdCache;
import mod.azure.azurelib.constant.DataTickets;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animation.*;
import mod.azure.azurelib.core.object.PlayState;
import mod.azure.azurelib.util.AzureLibUtil;
import net.elidhan.anim_guns.AnimatedGuns;
import net.elidhan.anim_guns.animations.AnimationHandler;
import net.elidhan.anim_guns.client.render.GunRenderer;
import net.elidhan.anim_guns.entity.projectile.BulletProjectileEntity;
import net.elidhan.anim_guns.mixininterface.IFPlayerWithGun;
import net.elidhan.anim_guns.network.ModNetworking;
import net.elidhan.anim_guns.util.AttachmentUtil;
import net.elidhan.anim_guns.util.BulletUtil;
import net.fabricmc.fabric.api.item.v1.FabricItem;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ClickType;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class GunItem extends Item implements FabricItem, GeoItem
{
    private final String id;
    private final Item ammoItem;
    private final float damage;
    private final int shotCount;
    private final int fireRate;
    private final int magSize;
    private final int reloadTime;

    protected final SoundEvent shotSound;
    protected final SoundEvent[] reloadSounds;

    private final Vector2f spread;
    private final Vector2f cameraRecoil;
    private final Vector3f viewModelRecoilMult;

    private final AttachmentItem.AttachType[] acceptedAttachmentTypes;
    private final fireType firingType;

    protected final Supplier<Object> renderProvider = GeoItem.makeRenderer(this);
    protected final AnimatableInstanceCache animationCache = AzureLibUtil.createInstanceCache(this);

    public GunItem(Settings settings, String id, Item ammoItem, float damage, int shotCount, int fireRate, int magSize, int reloadTime, SoundEvent shotSound, SoundEvent[] reloadSounds, Vector2f spread, Vector2f cameraRecoil, Vector3f viewModelRecoilMult, AttachmentItem.AttachType[] acceptedAttachmentTypes, fireType firingType)
    {
        super(settings);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);

        this.id = id;

        this.ammoItem = ammoItem;
        this.damage = damage;
        this.shotCount = shotCount;
        this.fireRate = fireRate;
        this.magSize = magSize;
        this.reloadTime = reloadTime;
        this.spread = spread; //Spread array should contain exactly 2 values
        this.cameraRecoil = cameraRecoil; //Recoil array should have exactly 2 values

        this.shotSound = shotSound;
        this.reloadSounds = reloadSounds;

        this.viewModelRecoilMult = viewModelRecoilMult; //Viewmodel recoil multiplier when aiming

        this.acceptedAttachmentTypes = acceptedAttachmentTypes;
        this.firingType = firingType;
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
        World world = player.getWorld();
        if (world.isClient()) return;

        if (!stack.getOrCreateNbt().contains("ammo")) stack.getOrCreateNbt().putInt("ammo", 0);

        if (stack.getOrCreateNbt().getInt("ammo") <= 0) return;
        if(player.getItemCooldownManager().isCoolingDown(this)) return;

        player.getItemCooldownManager().set(this, this.fireRate);

        //Actually shoot
        for (int i = 0; i < this.shotCount; i++)
        {
            BulletProjectileEntity bullet = new BulletProjectileEntity(player, player.getWorld(), this.damage, shotCount);
            bullet.setPosition(player.getX(), player.getEyeY(), player.getZ());

            double spreadX = -spread.x + Math.random() * (spread.x - (-spread.x));
            double spreadY = -spread.y + Math.random() * (spread.y - (-spread.y));

            Vec3d horiSpread = BulletUtil.horiSpread(player, spreadX);
            Vec3d vertiSpread = BulletUtil.vertiSpread(player, spreadY);

            Vec3d result = player.getRotationVector().add(vertiSpread).add(horiSpread);
            //Vec3d vec3d = result.normalize().add(bullet.random.nextTriangular(0.0, 0), bullet.random.nextTriangular(0.0, 0), bullet.random.nextTriangular(0.0, 0)).multiply(20);

            bullet.setVelocity(result.getX(), result.getY(), result.getZ(), 20, 0);
            bullet.setOwner(player);

            world.spawnEntity(bullet);
        }

        //Bullet -1
        if (!player.isCreative()) stack.getOrCreateNbt().putInt("ammo", stack.getOrCreateNbt().getInt("ammo")-1);
        if (player instanceof IFPlayerWithGun player1 && player1.isReloading())
        {
            player1.stopReload();
        }

        world.playSound(null, player.getPos().getX(), player.getPos().getY(), player.getPos().getZ(), this.shotSound, SoundCategory.PLAYERS, 1, 1);

        //Animation
        AnimationHandler.playAnim(player, stack, GeoItem.getId(stack), "firing");
        //Recoil
        ServerPlayNetworking.send(player, ModNetworking.S2C_SHOT, PacketByteBufs.empty());
    }
    //======================//

    //=====Reloading=====//
    public void tickReload(ServerPlayerEntity player, ItemStack stack, int tick)
    {}
    //===================//

    //=====Attachments Stuff=====//
    public String getSightID(ItemStack currentItemStack)
    {
        if (currentItemStack.getOrCreateNbt().getString("sightID").isEmpty()) currentItemStack.getOrCreateNbt().putString("sightID", "default");

        return currentItemStack.getOrCreateNbt().getString("sightID");
    }
    public String getGripID(ItemStack currentItemStack)
    {
        if (currentItemStack.getOrCreateNbt().getString("gripID").isEmpty()) currentItemStack.getOrCreateNbt().putString("gripID", "default");

        return currentItemStack.getOrCreateNbt().getString("gripID");
    }
    public String getMuzzleID(ItemStack currentItemStack)
    {
        if (currentItemStack.getOrCreateNbt().getString("muzzleID").isEmpty()) currentItemStack.getOrCreateNbt().putString("muzzleID", "default");

        return currentItemStack.getOrCreateNbt().getString("muzzleID");
    }

    public boolean isSilenced(ItemStack gun)
    {
        NbtCompound nbtCompound = gun.getOrCreateNbt();
        NbtList nbtList = nbtCompound.getList("Items", NbtElement.COMPOUND_TYPE);

        Optional<NbtCompound> optional = nbtList.stream().filter(NbtCompound.class::isInstance).map(NbtCompound.class::cast).filter(item ->
                (ItemStack.fromNbt(item).getItem() instanceof AttachmentItem)
                && ((((AttachmentItem)ItemStack.fromNbt(item).getItem()).silencesGun()))).findFirst();

        return optional.isPresent();
    }

    public boolean isScoped(ItemStack gun)
    {
        NbtCompound nbtCompound = gun.getOrCreateNbt();

        return nbtCompound.getBoolean("isScoped");
    }

    @Override
    public boolean onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference)
    {
        if (clickType != ClickType.RIGHT) return false;

        if (otherStack.isEmpty())
        {
            AttachmentUtil.removeFirstStack(stack).ifPresent(itemStack ->
            {
                player.playSound(SoundEvents.ITEM_BUNDLE_REMOVE_ONE, 0.8f, 0.8f + player.getWorld().getRandom().nextFloat() * 0.4f);
                cursorStackReference.set(itemStack);
            });
        }
        else
        {
            int i = AttachmentUtil.addAttachment(stack, otherStack, this.acceptedAttachmentTypes);

            if (i > 0)
            {
                otherStack.decrement(i);
                player.playSound(SoundEvents.ITEM_BUNDLE_INSERT, 0.8f, 0.8f + player.getWorld().getRandom().nextFloat() * 0.4f);
            }

        }

        return true;
    }

    //===========================//

    //=====Aiming stuff=====//
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand)
    {
        if (hand != Hand.MAIN_HAND) return TypedActionResult.fail(user.getStackInHand(hand));

        if(user instanceof IFPlayerWithGun player && !player.isReloading())
            player.toggleAim(!player.isAiming());

        return TypedActionResult.pass(user.getStackInHand(hand));
    }

    //=====Getters=====//
    public fireType getFiringType() {return firingType;}
    public int getReloadTime() {return reloadTime;}
    public String getID() {return this.id;}
    public int getMagSize() {return magSize;}
    public Item getAmmoItem() {return ammoItem;}

    public float getRecoilX()
    {
        return cameraRecoil.x;
    }
    public float getRecoilY()
    {
        return cameraRecoil.y;
    }
    public float getRecoilMult(ItemStack stack)
    {
        NbtCompound nbtCompound = stack.getOrCreateNbt();
        NbtList list = nbtCompound.getList("Items", NbtElement.COMPOUND_TYPE);

        float recoilMult = 1.0f;

        List<NbtCompound> l = list.stream().filter(NbtCompound.class::isInstance).map(NbtCompound.class::cast).toList();

        for (NbtCompound n : l)
        {
            recoilMult *= ((AttachmentItem) ItemStack.fromNbt(n).getItem()).getRecoilMult();
        }

        return recoilMult;
    }
    public Vector3f getAimVMRecoilMult() {return this.viewModelRecoilMult;}
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

    @Override
    public boolean isPerspectiveAware()
    {
        return true;
    }

    protected PlayState predicate(AnimationState<GunItem> animationState)
    {
        if (animationState.getData(DataTickets.ITEM_RENDER_PERSPECTIVE) != ModelTransformationMode.FIRST_PERSON_RIGHT_HAND)
        {
            return PlayState.STOP;
        }
        else
        {
            if (animationState.getController().getAnimationState() == AnimationController.State.STOPPED)
                animationState.getController().tryTriggerAnimation("idle");
        }

        return PlayState.CONTINUE;
    }
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {}
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {return this.animationCache;}
    //==========================//

    public enum fireType
    {
        AUTO,
        SEMI
    }
}
