package net.elidhan.anim_guns.item;

import net.elidhan.anim_guns.AnimatedGuns;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems
{
    public static final Item TESTGUN = registerItem("testgun", new GunItem(new Item.Settings(),
            "testgun",
            6,
            2,
            20,
            new int[] {1,1,1,1}, //Reload Stages
            new float[] {1,1}, //Spread
            new float[] {1,1} //Recoil
    ));

    private static Item registerItem(String name, Item item)
    {
        return Registry.register(Registries.ITEM, new Identifier(AnimatedGuns.MOD_ID, name), item);
    }

    public static void registerModItems()
    {
        AnimatedGuns.LOGGER.info("Registering Items for " + AnimatedGuns.MOD_ID);
    }

    public static void registerModItemGroups()
    {
        //TODO: Add items and item groups
    }
}
