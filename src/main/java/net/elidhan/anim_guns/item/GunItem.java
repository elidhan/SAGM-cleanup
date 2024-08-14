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
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ClickType;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class GunItem extends Item implements FabricItem, GeoItem
{
    private final String id;
    private final float damage;
    private final int fireRate;
    private final int magSize;
    private final int reloadTime;

    private final Vector2f spread;
    private final Vector2f cameraRecoil;
    private final Vector4f viewModelRecoil;
    private final Vector3f viewModelRecoilMult;
    private final int viewModelRecoilDuration;

    private final AttachmentItem.AttachType[] acceptedAttachmentTypes;

    protected final Supplier<Object> renderProvider = GeoItem.makeRenderer(this);
    protected final AnimatableInstanceCache animationCache = AzureLibUtil.createInstanceCache(this);

    public GunItem(Settings settings, String id, float damage, int fireRate, int magSize, int reloadTime, Vector2f spread, Vector2f cameraRecoil, Vector4f viewModelRecoil, Vector3f viewModelRecoilMult, int viewModelRecoilDuration, AttachmentItem.AttachType[] acceptedAttachmentTypes)
    {
        super(settings);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);

        this.id = id;
        this.damage = damage;
        this.fireRate = fireRate;
        this.magSize = magSize;
        this.reloadTime = reloadTime;
        this.spread = spread; //Spread array should contain exactly 2 values
        this.cameraRecoil = cameraRecoil; //Recoil array should have exactly 2 values
        this.viewModelRecoil = viewModelRecoil; // Should contain 5 values: rotate up-down, rotate side, move up-down, move forward, and duration
        this.viewModelRecoilMult = viewModelRecoilMult; //Viewmodel recoil multiplier when aiming
        this.viewModelRecoilDuration = viewModelRecoilDuration;
        this.acceptedAttachmentTypes = acceptedAttachmentTypes;
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

        double spreadX = -spread.x + Math.random() * (spread.x - (-spread.x));
        double spreadY = -spread.y + Math.random() * (spread.y - (-spread.y));

        Vec3d vertiSpread = BulletUtil.vertiSpread(player, spreadX);
        Vec3d horiSpread = BulletUtil.horiSpread(player, spreadY);

        Vec3d result = player.getRotationVector().add(vertiSpread).add(horiSpread);

        bullet.setVelocity(result.getX(), result.getY(), result.getZ(), 20, 0);
        bullet.setBaseVel(bullet.getVelocity());
        bullet.setOwner(player);

        player.getWorld().spawnEntity(bullet);

        //Bullet -1
        if (!player.isCreative()) stack.getOrCreateNbt().putInt("ammo", stack.getOrCreateNbt().getInt("ammo")-1);
        if (player instanceof IFPlayerWithGun player1 && player1.isReloading())
        {
            player1.stopReload();
        }

        //Animation
        AnimationHandler.playAnim(player, stack, GeoItem.getId(stack), "firing");
        //Recoil
        ServerPlayNetworking.send(player, ModNetworking.S2C_RECOIL, PacketByteBufs.empty());
    }
    //======================//

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

    @Override
    public boolean onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference)
    {
        if (clickType != ClickType.RIGHT) return false;

        if (otherStack.isEmpty())
        {
            removeFirstStack(stack).ifPresent(itemStack ->
            {
                player.playSound(SoundEvents.ITEM_BUNDLE_REMOVE_ONE, 0.8f, 0.8f + player.getWorld().getRandom().nextFloat() * 0.4f);
                cursorStackReference.set(itemStack);
            });
        }
        else
        {
            int i = addAttachment(stack, otherStack);

            if (i > 0)
            {
                otherStack.decrement(i);
                player.playSound(SoundEvents.ITEM_BUNDLE_INSERT, 0.8f, 0.8f + player.getWorld().getRandom().nextFloat() * 0.4f);
            }

        }

        return true;
    }

    private int addAttachment(ItemStack gun, ItemStack attachment)
    {
        if (attachment.isEmpty() || !(attachment.getItem() instanceof AttachmentItem)) return 0;

        NbtCompound nbtCompound = gun.getOrCreateNbt();

        if (!nbtCompound.contains("Items")) nbtCompound.put("Items", new NbtList());

        int i = getExistingAttachments(gun);int k = Math.min(attachment.getCount(), (3 - i));

        if (k == 0) return 0;

        NbtList nbtList = nbtCompound.getList("Items", NbtElement.COMPOUND_TYPE);
        Optional<NbtCompound> existingAttach = checkExistingAttachType(attachment, nbtList);

        if (existingAttach.isPresent() || !acceptedAttachment(((AttachmentItem)attachment.getItem()).getAttachType()))
        {
            return 0;
        }
        else
        {
            String attachID = ((AttachmentItem)attachment.getItem()).getId();
            AttachmentItem.AttachType attachType = ((AttachmentItem) attachment.getItem()).getAttachType();

            switch(attachType)
            {
                case SIGHT -> nbtCompound.putString("sightID", attachID);
                case SCOPE ->
                {
                    nbtCompound.putString("sightID", attachID);
                    nbtCompound.putBoolean("isScoped", true);
                }
                case GRIP -> nbtCompound.putString("gripID", attachID);
                case MUZZLE -> nbtCompound.putString("muzzleID", attachID);
            }

            ItemStack itemStack2 = attachment.copyWithCount(k);
            NbtCompound nbtCompound3 = new NbtCompound();
            itemStack2.writeNbt(nbtCompound3);
            nbtList.add(0, nbtCompound3);
        }

        return 1;
    }

    private Optional<ItemStack> removeFirstStack(ItemStack gun)
    {
        NbtCompound nbtCompound = gun.getOrCreateNbt();

        if (!nbtCompound.contains("Items")) return Optional.empty();

        NbtList nbtList = nbtCompound.getList("Items", NbtElement.COMPOUND_TYPE);

        if (nbtList.isEmpty()) return Optional.empty();

        NbtCompound nbtCompound2 = nbtList.getCompound(0);
        ItemStack attachment = ItemStack.fromNbt(nbtCompound2);
        nbtList.remove(0);

        AttachmentItem.AttachType attachType = ((AttachmentItem) attachment.getItem()).getAttachType();

        switch(attachType)
        {
            case SIGHT -> nbtCompound.putString("sightID", "default");
            case SCOPE ->
            {
                nbtCompound.putString("sightID", "default");
                nbtCompound.putBoolean("isScoped", false);
            }
            case GRIP -> nbtCompound.putString("gripID", "default");
            case MUZZLE -> nbtCompound.putString("muzzleID", "default");
        }

        if (nbtList.isEmpty()) gun.removeSubNbt("Items");

        return Optional.of(attachment);
    }

    private int getExistingAttachments(ItemStack gun)
    {
        return getAttachments(gun).mapToInt(ItemStack::getCount).sum();
    }

    private Stream<ItemStack> getAttachments(ItemStack gun)
    {
        NbtCompound nbtCompound = gun.getNbt();
        if (nbtCompound == null)
        {
            return Stream.empty();
        }
        NbtList nbtList = nbtCompound.getList("Items", NbtElement.COMPOUND_TYPE);
        return nbtList.stream().map(NbtCompound.class::cast).map(ItemStack::fromNbt);
    }

    private Optional<NbtCompound> checkExistingAttachType(ItemStack attachment, NbtList items)
    {
        if (attachment.isOf(Items.BUNDLE)) {
            return Optional.empty();
        }

        //items.stream().filter(NbtCompound.class::isInstance).map(NbtCompound.class::cast).filter(item -> ItemStack.canCombine(ItemStack.fromNbt(item), stack)).findFirst();
        return items.stream().filter(NbtCompound.class::isInstance).map(NbtCompound.class::cast).filter(item ->
                        (ItemStack.fromNbt(item).getItem() instanceof AttachmentItem)
                                && (((AttachmentItem)ItemStack.fromNbt(item).getItem()).getAttachType() == ((AttachmentItem)attachment.getItem()).getAttachType()
                        || ((AttachmentItem)ItemStack.fromNbt(item).getItem()).getAttachType().equals(AttachmentItem.AttachType.SCOPE) && ((AttachmentItem)attachment.getItem()).getAttachType().equals(AttachmentItem.AttachType.SIGHT)
                        || ((AttachmentItem)ItemStack.fromNbt(item).getItem()).getAttachType().equals(AttachmentItem.AttachType.SIGHT) && ((AttachmentItem)attachment.getItem()).getAttachType().equals(AttachmentItem.AttachType.SCOPE)))
                .findFirst();
    }

    private boolean acceptedAttachment(AttachmentItem.AttachType attachType)
    {
        for (AttachmentItem.AttachType attachType2 : acceptedAttachmentTypes)
        {
            if (attachType == attachType2) return true;
        }
        return false;
    }

    //===========================//

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
    public String getID() {return this.id;}
    public int getMagSize() {return magSize;}

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
    public Vector4f getViewModelRecoil() {return this.viewModelRecoil;}
    public int getViewModelRecoilDuration() {return this.viewModelRecoilDuration;}
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
