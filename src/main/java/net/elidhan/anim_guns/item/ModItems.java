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
            30,
            20,
            new int[] {1,1,1,1}, //Reload Stages, must be 4 values exactly
            new float[] {1,1}, //Spread, must be 2 values exactly
            new float[] {1.5f,2.5f,0.1f}, //Recoil, must be 3 values exactly, recoilX, recoilY, and aim multiplier for up kick
            new float[] {1.75f, 0.5f, 0.125f, 1f, 3} // Should contain 5 values: rotate up-down, rotate side, move up-down, move backward, and duration
    ));

    public static final Item PISTOL_HEAVY = registerItem("pistol_heavy", new GunMagFedItem(new Item.Settings(),
            "pistol_heavy",
            8,
            5,
            7,
            20,
            new int[] {1,2,3,4},
            new float[] {1,1},
            new float[] {2.5f,5f,1f}, //Recoil, must be 3 values exactly, recoilX, recoilY, and aim multiplier for up kick
            new float[] {15f, 1.25f, 0.5f, 1f, 4} // Should contain 5 values: rotate up-down, rotate side, move up-down, move backward, and duration
    ));

    public static final Item SNIPER_CLASSIC = registerItem("sniper_classic", new GunMagFedItem(new Item.Settings(),
            "sniper_classic",
            22,
            20,
            5,
            20,
            new int[] {1,2,3,4},
            new float[] {1,1},
            new float[] {1.5f,5.25f,0f}, //Recoil, must be 3 values exactly, recoilX, recoilY, and aim multiplier for up kick
            new float[] {8.75f, 0.5f, 0.25f, 2.5f, 6} // Should contain 5 values: rotate up-down, rotate side, move up-down, move backward, and duration
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
