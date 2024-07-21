package net.elidhan.anim_guns.item;

import net.elidhan.anim_guns.AnimatedGuns;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItems
{
    public static final Item HARDENED_IRON_INGOT = registerItem("hardened_iron_ingot", new Item(new FabricItemSettings().maxCount(64)));
    public static final Item HARDENED_IRON_NUGGET = registerItem("hardened_iron_nugget", new Item(new FabricItemSettings().maxCount(64)));
    public static final Item PLASTIC = registerItem("plastic", new Item(new FabricItemSettings().maxCount(64)));
    public static final Item ENRICHED_IRON = registerItem("enriched_iron", new Item(new FabricItemSettings().maxCount(64)));

    public static final Item PISTOL_GRIP = registerItem("pistol_grip", new Item(new FabricItemSettings().maxCount(64)));
    public static final Item GUN_SCOPE = registerItem("gun_scope", new Item(new FabricItemSettings().maxCount(64)));

    public static final Item LONG_BARREL = registerItem("long_barrel", new Item(new FabricItemSettings().maxCount(64)));
    public static final Item SHORT_BARREL = registerItem("short_barrel", new Item(new FabricItemSettings().maxCount(64)));
    public static final Item HEAVY_BARREL = registerItem("heavy_barrel", new Item(new FabricItemSettings().maxCount(64)));
    public static final Item MULTI_BARREL = registerItem("multi_barrel", new Item(new FabricItemSettings().maxCount(64)));
    public static final Item WOODEN_STOCK = registerItem("wooden_stock", new Item(new FabricItemSettings().maxCount(64)));
    public static final Item MODERN_STOCK = registerItem("modern_stock", new Item(new FabricItemSettings().maxCount(64)));
    public static final Item WOODEN_HANDGUARD = registerItem("wooden_handguard", new Item(new FabricItemSettings().maxCount(64)));
    public static final Item MODERN_HANDGUARD = registerItem("modern_handguard", new Item(new FabricItemSettings().maxCount(64)));
    public static final Item PISTOL_MAGAZINE = registerItem("pistol_magazine", new Item(new FabricItemSettings().maxCount(64)));
    public static final Item REVOLVER_CHAMBER = registerItem("revolver_chamber", new Item(new FabricItemSettings().maxCount(64)));
    public static final Item SMG_MAGAZINE = registerItem("smg_magazine", new Item(new FabricItemSettings().maxCount(64)));
    public static final Item RIFLE_MAGAZINE = registerItem("rifle_magazine", new Item(new FabricItemSettings().maxCount(64)));
    public static final Item TUBE_MAGAZINE = registerItem("tube_magazine", new Item(new FabricItemSettings().maxCount(64)));
    public static final Item LMG_AMMO_BOX = registerItem("lmg_ammo_box", new Item(new FabricItemSettings().maxCount(64)));
    /*
    public static final Item BLUEPRINT_BUNDLE = registerItem("blueprint_bundle", new BlueprintBundleItem(new FabricItemSettings().maxCount(1)));
    public static final Item PISTOL_BLUEPRINT = registerItem("blueprint_pistol_light", new BlueprintItem(new FabricItemSettings().maxCount(1)));
    public static final Item SERVICE_PISTOL_BLUEPRINT = registerItem("blueprint_pistol_service", new BlueprintItem(new FabricItemSettings().maxCount(1)));
    public static final Item HEAVY_PISTOL_BLUEPRINT = registerItem("blueprint_pistol_heavy", new BlueprintItem(new FabricItemSettings().maxCount(1)));
    public static final Item MAGNUM_REVOLVER_BLUEPRINT = registerItem("blueprint_revolver_magnum", new BlueprintItem(new FabricItemSettings().maxCount(1)));
    public static final Item OLD_ARMY_REVOLVER_BLUEPRINT = registerItem("blueprint_revolver_coltarmy", new BlueprintItem(new FabricItemSettings().maxCount(1)));
    public static final Item MACHINE_PISTOL_BLUEPRINT = registerItem("blueprint_smg_machinepistol", new BlueprintItem(new FabricItemSettings().maxCount(1)));
    public static final Item HEAVY_SMG_BLUEPRINT = registerItem("blueprint_smg_heavy", new BlueprintItem(new FabricItemSettings().maxCount(1)));
    public static final Item RAPID_SMG_BLUEPRINT = registerItem("blueprint_smg_rapid", new BlueprintItem(new FabricItemSettings().maxCount(1)));
    public static final Item LIGHT_ASSAULT_RIFLE_BLUEPRINT = registerItem("blueprint_assaultrifle_light", new BlueprintItem(new FabricItemSettings().maxCount(1)));
    public static final Item HEAVY_ASSAULT_RIFLE_BLUEPRINT = registerItem("blueprint_assaultrifle_heavy", new BlueprintItem(new FabricItemSettings().maxCount(1)));
    public static final Item WAR_TORN_ASSAULT_RIFLE_BLUEPRINT = registerItem("blueprint_assaultrifle_rus", new BlueprintItem(new FabricItemSettings().maxCount(1)));
    public static final Item COMBAT_SHOTGUN_BLUEPRINT = registerItem("blueprint_shotgun_combat", new BlueprintItem(new FabricItemSettings().maxCount(1)));
    public static final Item RIOT_SHOTGUN_BLUEPRINT = registerItem("blueprint_shotgun_riot", new BlueprintItem(new FabricItemSettings().maxCount(1)));
    public static final Item DOUBLE_BARRELED_SHOTGUN_BLUEPRINT = registerItem("blueprint_shotgun_doublebarrel", new BlueprintItem(new FabricItemSettings().maxCount(1)));
    public static final Item CLASSIC_SNIPER_RIFLE_BLUEPRINT = registerItem("blueprint_sniper_classic", new BlueprintItem(new FabricItemSettings().maxCount(1)));
    public static final Item ARCTIC_RIFLE_BLUEPRINT = registerItem("blueprint_sniper_arctic", new BlueprintItem(new FabricItemSettings().maxCount(1)));
    public static final Item BRUSH_GUN_BLUEPRINT = registerItem("blueprint_sniper_cowboy", new BlueprintItem(new FabricItemSettings().maxCount(1)));
    public static final Item MARKSMAN_RIFLE_BLUEPRINT = registerItem("blueprint_sniper_marksman", new BlueprintItem(new FabricItemSettings().maxCount(1)));
    public static final Item MOTHERLAND_MARKSMAN_RIFLE_BLUEPRINT = registerItem("blueprint_sniper_dragunov", new BlueprintItem(new FabricItemSettings().maxCount(1)));
    public static final Item LMG_BLUEPRINT = registerItem("blueprint_lmg_m60", new BlueprintItem(new FabricItemSettings().maxCount(1)));
    public static final Item MINIGUN_BLUEPRINT = registerItem("blueprint_lmg_minigun", new BlueprintItem(new FabricItemSettings().maxCount(1)));
    public static final Item ANTI_MATERIEL_RIFLE_BLUEPRINT = registerItem("blueprint_amr_classic", new BlueprintItem(new FabricItemSettings().maxCount(1)));
     */

    public static final Item STANDARD_HANDGUN_BULLET = registerItem("standard_handgun_cartridge", new Item(new FabricItemSettings().maxCount(64)));
    public static final Item HEAVY_HANDGUN_BULLET = registerItem("heavy_handgun_cartridge", new Item(new FabricItemSettings().maxCount(64)));
    public static final Item STANDARD_RIFLE_BULLET = registerItem("standard_rifle_cartridge", new Item(new FabricItemSettings().maxCount(64)));
    public static final Item HEAVY_RIFLE_BULLET = registerItem("heavy_rifle_cartridge", new Item(new FabricItemSettings().maxCount(64)));
    public static final Item SHOTGUN_SHELL = registerItem("shotgun_shell", new Item(new FabricItemSettings().maxCount(64)));

    public static final Item DEV_ASSAULTRIFLE = registerItem("dev_assaultrifle", new GunMagFedItem(new FabricItemSettings().maxCount(1),
            "dev_assaultrifle",
            6,
            2,
            30,
            20,
            new int[] {1,1,1,1}, //Reload Stages, must be 4 values exactly
            new float[] {1,1}, //Spread, must be 2 values exactly
            new float[] {1.5f,2.5f,0.5f}, //Recoil, must be 3 values exactly, recoilX, recoilY, and aim multiplier for up kick
            new float[] {1.25f, 0.375f, -0.25f, 1.25f, 4} // Should contain 5 values: rotate up-down, rotate side, move up-down, move backward, and duration
    ));

    public static final Item PISTOL_LIGHT = registerItem("pistol_light", new GunMagFedItem(new FabricItemSettings().maxCount(1),
            "pistol_light",
            5,
            4,
            17,
            20,
            new int[] {1,2,3,4},
            new float[] {1,1},
            new float[] {0.875f,2.5f,1f}, //Recoil, must be 3 values exactly, recoilX, recoilY, and aim multiplier for up kick
            new float[] {12.5f, 1f, 2.5f, 2f, 4} // Should contain 5 values: rotate up-down, rotate side, move up-down, move backward, and duration
    ));

    public static final Item PISTOL_HEAVY = registerItem("pistol_heavy", new GunMagFedItem(new FabricItemSettings().maxCount(1),
            "pistol_heavy",
            10,
            5,
            7,
            20,
            new int[] {1,2,3,4},
            new float[] {1,1},
            new float[] {2.5f,5f,1f}, //Recoil, must be 3 values exactly, recoilX, recoilY, and aim multiplier for up kick
            new float[] {18.75f, 1.5f, 5f, 1f, 5} // Should contain 5 values: rotate up-down, rotate side, move up-down, move backward, and duration
    ));

    public static final Item SMG_MP = registerItem("smg_machinepistol", new GunMagFedItem(new FabricItemSettings().maxCount(1),
            "smg_machinepistol",
            5,
            1,
            30,
            20,
            new int[] {1,2,3,4},
            new float[] {1,1},
            new float[] {1f,2.5f,0.625f}, //Recoil, must be 3 values exactly, recoilX, recoilY, and aim multiplier for up kick
            new float[] {2.5f, 1f, 1f, 0.25f, 6} // Should contain 5 values: rotate up-down, rotate side, move up-down, move backward, and duration
    ));

    public static final Item SMG_RAPID = registerItem("smg_rapid", new GunMagFedItem(new FabricItemSettings().maxCount(1),
            "smg_rapid",
            4,
            1,
            30,
            20,
            new int[] {1,2,3,4},
            new float[] {1,1},
            new float[] {1.25f,1.875f,0.125f}, //Recoil, must be 3 values exactly, recoilX, recoilY, and aim multiplier for up kick
            new float[] {1.5f, 0.5f, 0.25f, 0.875f, 6} // Should contain 5 values: rotate up-down, rotate side, move up-down, move backward, and duration
    ));

    public static final Item ASSAULTRIFLE_LIGHT = registerItem("assaultrifle_light", new GunMagFedItem(new FabricItemSettings().maxCount(1),
            "assaultrifle_light",
            6,
            2,
            30,
            20,
            new int[] {1,1,1,1}, //Reload Stages, must be 4 values exactly
            new float[] {1,1}, //Spread, must be 2 values exactly
            new float[] {1.5f,2.5f,0.5f}, //Recoil, must be 3 values exactly, recoilX, recoilY, and aim multiplier for up kick
            new float[] {1.25f, 0.375f, 0.125f, 0.75f, 4} // Should contain 5 values: rotate up-down, rotate side, move up-down, move backward, and duration
    ));

    public static final Item ASSAULTRIFLE_HEAVY = registerItem("assaultrifle_heavy", new GunMagFedItem(new FabricItemSettings().maxCount(1),
            "assaultrifle_heavy",
            10,
            3,
            20,
            20,
            new int[] {1,1,1,1}, //Reload Stages, must be 4 values exactly
            new float[] {1,1}, //Spread, must be 2 values exactly
            new float[] {1.5f,3.25f,0.5f}, //Recoil, must be 3 values exactly, recoilX, recoilY, and aim multiplier for up kick
            new float[] {2.25f, 0.75f, 0.125f, 1.25f, 6} // Should contain 5 values: rotate up-down, rotate side, move up-down, move backward, and duration
    ));

    public static final Item ASSAULTRIFLE_RUS = registerItem("assaultrifle_rus", new GunMagFedItem(new FabricItemSettings().maxCount(1),
            "assaultrifle_rus",
            7,
            2,
            30,
            20,
            new int[] {1,1,1,1}, //Reload Stages, must be 4 values exactly
            new float[] {1,1}, //Spread, must be 2 values exactly
            new float[] {2.5f,3.25f,0.5f}, //Recoil, must be 3 values exactly, recoilX, recoilY, and aim multiplier for viewmodel up kick
            new float[] {1.5f, 0.75f, 0.25f, 1f, 6} // Should contain 5 values: rotate up-down, rotate side, move up-down, move backward, and duration
    ));

    public static final Item SNIPER_CLASSIC = registerItem("sniper_classic", new GunSingleLoaderItem(new Item.Settings().maxCount(1),
            "sniper_classic",
            22,
            20,
            5,
            20,
            new int[] {1,2,3,4},
            new float[] {1,1},
            new float[] {2f,5.25f,0f}, //Recoil, must be 3 values exactly, recoilX, recoilY, and aim multiplier for up kick
            new float[] {8.75f, 1.5f, 2f, 3f, 8} // Should contain 5 values: rotate up-down, rotate side, move up-down, move backward, and duration
    ));

    public static final Item SNIPER_MARKSMAN = registerItem("sniper_marksman", new GunMagFedItem(new Item.Settings().maxCount(1),
            "sniper_marksman",
            12,
            5,
            20,
            20,
            new int[] {1,2,3,4},
            new float[] {1,1},
            new float[] {2.5f,3.25f,0f}, //Recoil, must be 3 values exactly, recoilX, recoilY, and aim multiplier for up kick
            new float[] {8.75f, 1.5f, -1.875f, 2.5f, 8} // Should contain 5 values: rotate up-down, rotate side, move up-down, move backward, and duration
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
        final ItemGroup GUNS = FabricItemGroup.builder().icon(() -> new ItemStack(ModItems.PISTOL_HEAVY)).entries((displayContext, entries) -> {
            entries.add(new ItemStack(ModItems.STANDARD_HANDGUN_BULLET));
            entries.add(new ItemStack(ModItems.HEAVY_HANDGUN_BULLET));
            entries.add(new ItemStack(ModItems.STANDARD_RIFLE_BULLET));
            entries.add(new ItemStack(ModItems.HEAVY_RIFLE_BULLET));
            entries.add(new ItemStack(ModItems.SHOTGUN_SHELL));
            entries.add(new ItemStack(ModItems.PISTOL_LIGHT));
            entries.add(new ItemStack(ModItems.PISTOL_HEAVY));
            //entries.add(new ItemStack(ModItems.SERVICE_PISTOL));
            //entries.add(new ItemStack(ModItems.MAGNUM_REVOLVER));
            //entries.add(new ItemStack(ModItems.OLD_ARMY_REVOLVER));
            entries.add(new ItemStack(ModItems.SMG_MP));
            //entries.add(new ItemStack(ModItems.HEAVY_SMG));
            entries.add(new ItemStack(ModItems.SMG_RAPID));
            entries.add(new ItemStack(ModItems.ASSAULTRIFLE_LIGHT));
            entries.add(new ItemStack(ModItems.ASSAULTRIFLE_HEAVY));
            entries.add(new ItemStack(ModItems.ASSAULTRIFLE_RUS));
            //entries.add(new ItemStack(ModItems.DOUBLE_BARRELED_SHOTGUN));
            //entries.add(new ItemStack(ModItems.COMBAT_SHOTGUN));
            //entries.add(new ItemStack(ModItems.RIOT_SHOTGUN));
            entries.add(new ItemStack(ModItems.SNIPER_CLASSIC));
            //entries.add(new ItemStack(ModItems.ARCTIC_SNIPER_RIFLE));
            //entries.add(new ItemStack(ModItems.BRUSH_GUN));
            entries.add(new ItemStack(ModItems.SNIPER_MARKSMAN));
            //entries.add(new ItemStack(ModItems.MOTHERLAND_MARKSMAN_RIFLE));
            //entries.add(new ItemStack(ModItems.LMG));
            //entries.add(new ItemStack(ModItems.ANTI_MATERIEL_RIFLE));
            //entries.add(new ItemStack(ModItems.MINIGUN));
        }).displayName(Text.translatable("guns")).build();
        final ItemGroup CRAFTING = FabricItemGroup.builder().icon(() -> new ItemStack(ModItems.PISTOL_HEAVY)).entries((displayContext, entries) -> {
            entries.add(new ItemStack(ModItems.HARDENED_IRON_INGOT));
            entries.add(new ItemStack(ModItems.HARDENED_IRON_NUGGET));
            entries.add(new ItemStack(ModItems.PLASTIC));
            entries.add(new ItemStack(ModItems.ENRICHED_IRON));
            entries.add(new ItemStack(ModItems.PISTOL_GRIP));
            entries.add(new ItemStack(ModItems.GUN_SCOPE));
            entries.add(new ItemStack(ModItems.LONG_BARREL));
            entries.add(new ItemStack(ModItems.SHORT_BARREL));
            entries.add(new ItemStack(ModItems.HEAVY_BARREL));
            entries.add(new ItemStack(ModItems.MULTI_BARREL));
            entries.add(new ItemStack(ModItems.WOODEN_STOCK));
            entries.add(new ItemStack(ModItems.MODERN_STOCK));
            entries.add(new ItemStack(ModItems.WOODEN_HANDGUARD));
            entries.add(new ItemStack(ModItems.MODERN_HANDGUARD));
            entries.add(new ItemStack(ModItems.PISTOL_MAGAZINE));
            entries.add(new ItemStack(ModItems.REVOLVER_CHAMBER));
            entries.add(new ItemStack(ModItems.SMG_MAGAZINE));
            entries.add(new ItemStack(ModItems.RIFLE_MAGAZINE));
            entries.add(new ItemStack(ModItems.TUBE_MAGAZINE));
            entries.add(new ItemStack(ModItems.LMG_AMMO_BOX));
            /*
            entries.add(new ItemStack(ModItems.BLUEPRINT_BUNDLE));
            entries.add(new ItemStack(ModItems.PISTOL_BLUEPRINT));
            entries.add(new ItemStack(ModItems.SERVICE_PISTOL_BLUEPRINT));
            entries.add(new ItemStack(ModItems.HEAVY_PISTOL_BLUEPRINT));
            entries.add(new ItemStack(ModItems.MAGNUM_REVOLVER_BLUEPRINT));
            entries.add(new ItemStack(ModItems.OLD_ARMY_REVOLVER_BLUEPRINT));
            entries.add(new ItemStack(ModItems.MACHINE_PISTOL_BLUEPRINT));
            entries.add(new ItemStack(ModItems.HEAVY_SMG_BLUEPRINT));
            entries.add(new ItemStack(ModItems.RAPID_SMG_BLUEPRINT));
            entries.add(new ItemStack(ModItems.LIGHT_ASSAULT_RIFLE_BLUEPRINT));
            entries.add(new ItemStack(ModItems.HEAVY_ASSAULT_RIFLE_BLUEPRINT));
            entries.add(new ItemStack(ModItems.WAR_TORN_ASSAULT_RIFLE_BLUEPRINT));
            entries.add(new ItemStack(ModItems.COMBAT_SHOTGUN_BLUEPRINT));
            entries.add(new ItemStack(ModItems.RIOT_SHOTGUN_BLUEPRINT));
            entries.add(new ItemStack(ModItems.DOUBLE_BARRELED_SHOTGUN_BLUEPRINT));
            entries.add(new ItemStack(ModItems.CLASSIC_SNIPER_RIFLE_BLUEPRINT));
            entries.add(new ItemStack(ModItems.ARCTIC_RIFLE_BLUEPRINT));
            entries.add(new ItemStack(ModItems.BRUSH_GUN_BLUEPRINT));
            entries.add(new ItemStack(ModItems.MARKSMAN_RIFLE_BLUEPRINT));
            entries.add(new ItemStack(ModItems.MOTHERLAND_MARKSMAN_RIFLE_BLUEPRINT));
            entries.add(new ItemStack(ModItems.LMG_BLUEPRINT));
            entries.add(new ItemStack(ModItems.MINIGUN_BLUEPRINT));
            entries.add(new ItemStack(ModItems.ANTI_MATERIEL_RIFLE_BLUEPRINT));
             */
        }).displayName(Text.translatable("crafting")).build();
        final ItemGroup ATTACHMENTS = FabricItemGroup.builder().icon(() -> new ItemStack(ModItems.PISTOL_HEAVY)).entries((displayContext, entries) -> {
            //entries.add(new ItemStack(ModItems.SIGHT_HOLO));
            //entries.add(new ItemStack(ModItems.GRIP_FOREGRIP));
            //entries.add(new ItemStack(ModItems.MUZZLE_MBRAKE));
        }).displayName(Text.translatable("attachments")).build();

        Registry.register(Registries.ITEM_GROUP, new Identifier(AnimatedGuns.MOD_ID, "anim_guns.guns"), GUNS);
        Registry.register(Registries.ITEM_GROUP, new Identifier(AnimatedGuns.MOD_ID, "anim_guns.crafting"), CRAFTING);
        Registry.register(Registries.ITEM_GROUP, new Identifier(AnimatedGuns.MOD_ID, "anim_guns.attachments"), ATTACHMENTS);
    }
}
