package net.elidhan.anim_guns.config;

import com.mojang.datafixers.util.Pair;
import net.elidhan.anim_guns.AnimatedGuns;

public class ModConfigs {
    public static SimpleConfig CONFIG;
    private static ModConfigProvider configs;

    public static float DMG_PISTOL_LIGHT;
    public static float DMG_PISTOL_HEAVY;
    public static float DMG_PISTOL_SERVICE;

    public static float DMG_REVOLVER_COMPACT;
    public static float DMG_REVOLVER_MAGNUM;
    public static float DMG_REVOLVER_COLT;

    public static float DMG_SHOTGUN_DOUBLEBARREL;
    public static float DMG_SHOTGUN_COMBAT;
    public static float DMG_SHOTGUN_RIOT;

    public static float DMG_SMG_MACHINEPISTOL;
    public static float DMG_SMG_RAPID;
    public static float DMG_SMG_HEAVY;

    public static float DMG_ASSAULT_CLASSIC;
    public static float DMG_ASSAULT_LIGHT;
    public static float DMG_ASSAULT_DESERT;

    public static float DMG_SNIPER_CLASSIC;
    public static float DMG_SNIPER_ARCTIC;
    public static float DMG_SNIPER_COWBOY;
    public static float DMG_SNIPER_MARKSMAN;
    public static float DMG_SNIPER_DRAGUNOV;

    public static float DMG_LMG_M60;
    public static float DMG_LMG_MINIGUN;
    public static float DMG_AMR_CLASSIC;

    public static void registerConfigs() {
        configs = new ModConfigProvider();
        createConfigs();

        CONFIG = SimpleConfig.of(AnimatedGuns.MOD_ID + "_config").provider(configs).request();

        assignConfigs();
    }

    private static void createConfigs() {
        configs.addKeyValuePair(new Pair<>("damage.pistol_light", 5.5), "double");
        configs.addKeyValuePair(new Pair<>("damage.pistol_heavy", 11), "double");
        configs.addKeyValuePair(new Pair<>("damage.pistol_service", 7), "double");

        configs.addKeyValuePair(new Pair<>("damage.revolver_compact", 7), "double");
        configs.addKeyValuePair(new Pair<>("damage.revolver_magnum", 12), "double");
        configs.addKeyValuePair(new Pair<>("damage.revolver_coltarmy", 12), "double");

        configs.addKeyValuePair(new Pair<>("damage.smg_machinepistol", 5), "double");
        configs.addKeyValuePair(new Pair<>("damage.smg_rapid", 5), "double");
        configs.addKeyValuePair(new Pair<>("damage.smg_heavy", 6.7), "double");

        configs.addKeyValuePair(new Pair<>("damage.shotgun_doublebarrel", 6), "double");
        configs.addKeyValuePair(new Pair<>("damage.shotgun_combat", 6), "double");
        configs.addKeyValuePair(new Pair<>("damage.shotgun_riot", 4), "double");

        configs.addKeyValuePair(new Pair<>("damage.assaultrifle_light", 7), "double");
        configs.addKeyValuePair(new Pair<>("damage.assaultrifle_heavy", 11), "double");
        configs.addKeyValuePair(new Pair<>("damage.assaultrifle_rus", 8), "double");

        configs.addKeyValuePair(new Pair<>("damage.sniper_classic", 22), "double");
        configs.addKeyValuePair(new Pair<>("damage.sniper_arctic", 26), "double");

        configs.addKeyValuePair(new Pair<>("damage.sniper_cowboy", 16), "double");
        configs.addKeyValuePair(new Pair<>("damage.sniper_marksman", 12.5), "double");
        configs.addKeyValuePair(new Pair<>("damage.sniper_dragunov", 16), "double");

        configs.addKeyValuePair(new Pair<>("damage.lmg_m60", 7), "double");
        configs.addKeyValuePair(new Pair<>("damage.lmg_minigun", 5), "double");
        configs.addKeyValuePair(new Pair<>("damage.amr_classic", 40), "double");
    }

    private static void assignConfigs() {
        DMG_PISTOL_LIGHT = (float) CONFIG.getOrDefault("damage.pistol_light", 5.5);
        DMG_PISTOL_HEAVY = (float) CONFIG.getOrDefault("damage.pistol_heavy", 11);
        DMG_PISTOL_SERVICE = (float) CONFIG.getOrDefault("damage.pistol_service", 7);

        DMG_REVOLVER_COMPACT = (float) CONFIG.getOrDefault("damage.revolver_compact", 7);
        DMG_REVOLVER_MAGNUM = (float) CONFIG.getOrDefault("damage.revolver_magnum", 12);
        DMG_REVOLVER_COLT = (float) CONFIG.getOrDefault("damage.revolver_coltarmy", 12);

        DMG_SHOTGUN_DOUBLEBARREL = (float) CONFIG.getOrDefault("damage.shotgun_doublebarrel", 6);
        DMG_SHOTGUN_COMBAT = (float) CONFIG.getOrDefault("damage.shotgun_combat", 6);
        DMG_SHOTGUN_RIOT = (float) CONFIG.getOrDefault("damage.shotgun_riot", 4);

        DMG_SMG_MACHINEPISTOL = (float) CONFIG.getOrDefault("damage.smg_machinepistol", 5);
        DMG_SMG_RAPID = (float) CONFIG.getOrDefault("damage.smg_rapid", 5);
        DMG_SMG_HEAVY = (float) CONFIG.getOrDefault("damage.smg_heavy", 6.7);

        DMG_ASSAULT_CLASSIC = (float) CONFIG.getOrDefault("damage.assaultrifle_rus", 8);
        DMG_ASSAULT_LIGHT = (float) CONFIG.getOrDefault("damage.assaultrifle_light", 7);
        DMG_ASSAULT_DESERT = (float) CONFIG.getOrDefault("damage.assaultrifle_heavy", 11);

        DMG_SNIPER_CLASSIC = (float) CONFIG.getOrDefault("damage.sniper_classic", 22);
        DMG_SNIPER_ARCTIC = (float) CONFIG.getOrDefault("damage.sniper_arctic", 26);

        DMG_SNIPER_COWBOY = (float) CONFIG.getOrDefault("damage.sniper_cowboy", 16);
        DMG_SNIPER_MARKSMAN = (float) CONFIG.getOrDefault("damage.sniper_marksman", 12.5);
        DMG_SNIPER_DRAGUNOV = (float) CONFIG.getOrDefault("damage.sniper_dragunov", 16);

        DMG_LMG_M60 = (float) CONFIG.getOrDefault("damage.lmg_m60", 7);
        DMG_LMG_MINIGUN = (float) CONFIG.getOrDefault("damage.lmg_minigun", 5);
        DMG_AMR_CLASSIC = (float) CONFIG.getOrDefault("damage.amr_classic", 40);

        System.out.println("All " + configs.getConfigsList().size() + " have been set properly");
    }
}