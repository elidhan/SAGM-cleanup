package net.elidhan.anim_guns.item;

import net.elidhan.anim_guns.AnimatedGuns;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems
{
    public static final Item PISTOL_HEAVY = registerItem("pistol_heavy", new GunMagFedItem(new Item.Settings(),
            "pistol_heavy",
            8,
            5,
            7,
            20,
            new int[] {1,2,3,4},
            new float[] {1,1},
            new float[] {2.5f,5f,2f}, //Recoil, must be 3 values exactly, recoilX, recoilY, and aim multiplier for up kick
            new float[] {18.75f, 0.75f, 5f, 3f, 5} // Should contain 5 values: rotate up-down, rotate side, move up-down, move backward, and duration
    ));

    public static final Item PISTOL_LIGHT = registerItem("pistol_light", new GunMagFedItem(new Item.Settings(),
            "pistol_light",
            5,
            4,
            17,
            20,
            new int[] {1,2,3,4},
            new float[] {1,1},
            new float[] {0.875f,2.5f,0.75f}, //Recoil, must be 3 values exactly, recoilX, recoilY, and aim multiplier for up kick
            new float[] {15f, 0.25f, 3f, 2.5f, 4} // Should contain 5 values: rotate up-down, rotate side, move up-down, move backward, and duration
    ));

    public static final Item ASSAULTRIFLE_LIGHT = registerItem("assaultrifle_light", new GunMagFedItem(new Item.Settings(),
            "assaultrifle_light",
            6,
            2,
            30,
            20,
            new int[] {1,1,1,1}, //Reload Stages, must be 4 values exactly
            new float[] {1,1}, //Spread, must be 2 values exactly
            new float[] {1.5f,2.5f,0.25f}, //Recoil, must be 3 values exactly, recoilX, recoilY, and aim multiplier for up kick
            new float[] {1.75f, 0.25f, 0.125f, 1f, 4} // Should contain 5 values: rotate up-down, rotate side, move up-down, move backward, and duration
    ));

    public static final Item ASSAULTRIFLE_HEAVY = registerItem("assaultrifle_heavy", new GunMagFedItem(new Item.Settings(),
            "assaultrifle_heavy",
            10,
            3,
            20,
            20,
            new int[] {1,1,1,1}, //Reload Stages, must be 4 values exactly
            new float[] {1,1}, //Spread, must be 2 values exactly
            new float[] {1.5f,3.25f,0.25f}, //Recoil, must be 3 values exactly, recoilX, recoilY, and aim multiplier for up kick
            new float[] {2.25f, 0.75f, 0.125f, 1.5f, 6} // Should contain 5 values: rotate up-down, rotate side, move up-down, move backward, and duration
    ));

    public static final Item ASSAULTRIFLE_RUS = registerItem("assaultrifle_rus", new GunMagFedItem(new Item.Settings(),
            "assaultrifle_rus",
            7,
            2,
            30,
            20,
            new int[] {1,1,1,1}, //Reload Stages, must be 4 values exactly
            new float[] {1,1}, //Spread, must be 2 values exactly
            new float[] {2.5f,3.25f,0.25f}, //Recoil, must be 3 values exactly, recoilX, recoilY, and aim multiplier for viewmodel up kick
            new float[] {1.25f, 0.75f, 0.25f, 1.75f, 6} // Should contain 5 values: rotate up-down, rotate side, move up-down, move backward, and duration
    ));

    public static final Item SNIPER_CLASSIC = registerItem("sniper_classic", new GunSingleLoaderItem(new Item.Settings(),
            "sniper_classic",
            22,
            20,
            5,
            20,
            new int[] {1,2,3,4},
            new float[] {1,1},
            new float[] {1.5f,5.25f,0f}, //Recoil, must be 3 values exactly, recoilX, recoilY, and aim multiplier for up kick
            new float[] {12.5f, 1f, 0.375f, 4f, 8} // Should contain 5 values: rotate up-down, rotate side, move up-down, move backward, and duration
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
