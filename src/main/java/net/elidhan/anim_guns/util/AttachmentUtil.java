package net.elidhan.anim_guns.util;

import net.elidhan.anim_guns.item.AttachmentItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;

import java.util.Optional;
import java.util.stream.Stream;

public class AttachmentUtil
{
    public static int addAttachment(ItemStack gun, ItemStack attachment, AttachmentItem.AttachType[] acceptedAttachmentTypes)
    {
        if (attachment.isEmpty() || !(attachment.getItem() instanceof AttachmentItem)) return 0;

        NbtCompound nbtCompound = gun.getOrCreateNbt();

        if (!nbtCompound.contains("Items")) nbtCompound.put("Items", new NbtList());

        int i = getExistingAttachments(gun);int k = Math.min(attachment.getCount(), (3 - i));

        if (k == 0) return 0;

        NbtList nbtList = nbtCompound.getList("Items", NbtElement.COMPOUND_TYPE);
        Optional<NbtCompound> existingAttach = checkExistingAttachType(attachment, nbtList);

        if (existingAttach.isPresent() || !acceptedAttachment(((AttachmentItem)attachment.getItem()).getAttachType(), acceptedAttachmentTypes))
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

    public static Optional<ItemStack> removeFirstStack(ItemStack gun)
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

    public static int getExistingAttachments(ItemStack gun)
    {
        return getAttachments(gun).mapToInt(ItemStack::getCount).sum();
    }

    public static Stream<ItemStack> getAttachments(ItemStack gun)
    {
        NbtCompound nbtCompound = gun.getNbt();
        if (nbtCompound == null)
        {
            return Stream.empty();
        }
        NbtList nbtList = nbtCompound.getList("Items", NbtElement.COMPOUND_TYPE);
        return nbtList.stream().map(NbtCompound.class::cast).map(ItemStack::fromNbt);
    }

    public static Optional<NbtCompound> checkExistingAttachType(ItemStack attachment, NbtList items)
    {
        if (attachment.isOf(Items.BUNDLE)) {
            return Optional.empty();
        }

        return items.stream().filter(NbtCompound.class::isInstance).map(NbtCompound.class::cast).filter(item ->
                        (ItemStack.fromNbt(item).getItem() instanceof AttachmentItem)
                                && (((AttachmentItem)ItemStack.fromNbt(item).getItem()).getAttachType() == ((AttachmentItem)attachment.getItem()).getAttachType()
                                || ((AttachmentItem)ItemStack.fromNbt(item).getItem()).getAttachType().equals(AttachmentItem.AttachType.SCOPE) && ((AttachmentItem)attachment.getItem()).getAttachType().equals(AttachmentItem.AttachType.SIGHT)
                                || ((AttachmentItem)ItemStack.fromNbt(item).getItem()).getAttachType().equals(AttachmentItem.AttachType.SIGHT) && ((AttachmentItem)attachment.getItem()).getAttachType().equals(AttachmentItem.AttachType.SCOPE)))
                .findFirst();
    }

    public static boolean acceptedAttachment(AttachmentItem.AttachType attachType, AttachmentItem.AttachType[] acceptedAttachmentTypes)
    {
        for (AttachmentItem.AttachType attachType2 : acceptedAttachmentTypes)
        {
            if (attachType == attachType2) return true;
        }
        return false;
    }
}
