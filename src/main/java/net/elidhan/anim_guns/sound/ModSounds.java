package net.elidhan.anim_guns.sound;

import net.elidhan.anim_guns.AnimatedGuns;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ModSounds
{
    //SHOT SOUNDS

    public static SoundEvent SHOT_PISTOL_LIGHT = registerSoundEvent("shot_pistol_light");
    public static SoundEvent SHOT_PISTOL_HEAVY = registerSoundEvent("shot_pistol_heavy");
    public static SoundEvent SHOT_PISTOL_SERVICE = registerSoundEvent("shot_pistol_service");

    public static SoundEvent SHOT_REVOLVER_COMPACT = registerSoundEvent("shot_revolver_compact");
    public static SoundEvent SHOT_REVOLVER = registerSoundEvent("shot_revolver");
    public static SoundEvent SHOT_REVOLVER_COLT = registerSoundEvent("shot_revolver_colt");

    public static SoundEvent SHOT_SHOTGUN_DOUBLEBARREL = registerSoundEvent("shot_shotgun_doublebarrel");
    public static SoundEvent SHOT_SHOTGUN_COMBAT = registerSoundEvent("shot_shotgun_combat");
    public static SoundEvent SHOT_SHOTGUN_RIOT = registerSoundEvent("shot_shotgun_riot");

    public static SoundEvent SHOT_SMG_MACHINEPISTOL = registerSoundEvent("shot_smg_machinepistol");
    public static SoundEvent SHOT_SMG_RAPID = registerSoundEvent("shot_smg_rapid");
    public static SoundEvent SHOT_SMG_HEAVY = registerSoundEvent("shot_smg_heavy");

    public static SoundEvent SHOT_ASSAULT = registerSoundEvent("shot_assault");
    public static SoundEvent SHOT_ASSAULT_CLASSIC = registerSoundEvent("shot_assault_classic");
    public static SoundEvent SHOT_ASSAULT_DESERT = registerSoundEvent("shot_assault_desert");

    public static SoundEvent SHOT_SNIPER_CLASSIC = registerSoundEvent("shot_sniper_classic");
    public static SoundEvent SHOT_SNIPER_ARCTIC = registerSoundEvent("shot_sniper_arctic");

    public static SoundEvent SHOT_DMR_BRUSHGUN = registerSoundEvent("shot_dmr_brushgun");
    public static SoundEvent SHOT_DMR_MARKSMAN = registerSoundEvent("shot_dmr_marksman");
    public static SoundEvent SHOT_DMR_IRONCURTAIN = registerSoundEvent("shot_dmr_ironcurtain");

    //RELOAD SOUNDS
    
    public static SoundEvent RLD_PISTOL_1_REMOVE = registerSoundEvent("pistol_1_remove");
    public static SoundEvent RLD_PISTOL_1_INSERT = registerSoundEvent("pistol_1_insert");
    public static SoundEvent RLD_PISTOL_1_SLIDEFORWARD = registerSoundEvent("pistol_1_slideforward");

    public static SoundEvent RLD_REVOLVER_1_READY = registerSoundEvent("revolver_1_ready");
    public static SoundEvent RLD_REVOLVER_1_REMOVE = registerSoundEvent("revolver_1_remove");
    public static SoundEvent RLD_REVOLVER_1_INSERT = registerSoundEvent("revolver_1_insert");
    public static SoundEvent RLD_REVOLVER_1_FINISH = registerSoundEvent("revolver_1_finish");

    public static SoundEvent RLD_SHOTGUN_1_REMOVE = registerSoundEvent("shotgun_1_remove");
    public static SoundEvent RLD_SHOTGUN_1_INSERT = registerSoundEvent("shotgun_1_insert");
    public static SoundEvent RLD_SHOTGUN_1_FINISH = registerSoundEvent("shotgun_1_finish");

    public static SoundEvent RLD_SHOTGUN_SHELL_INSERT = registerSoundEvent("shotgun_shell_insert");
    public static SoundEvent RLD_SHOTGUN_2_PUMPBACK = registerSoundEvent("shotgun_2_pumpback");
    public static SoundEvent RLD_SHOTGUN_2_PUMPFORWARD = registerSoundEvent("shotgun_2_pumpforward");

    public static SoundEvent RLD_SMG_1_REMOVE = registerSoundEvent("smg_1_remove");
    public static SoundEvent RLD_SMG_1_INSERT = registerSoundEvent("smg_1_insert");
    public static SoundEvent RLD_SMG_1_BOLTBACK = registerSoundEvent("smg_1_boltback");
    public static SoundEvent RLD_SMG_1_BOLTFORWARD = registerSoundEvent("smg_1_boltforward");
    
    public static SoundEvent RLD_AR_1_REMOVE = registerSoundEvent("ar_1_remove");
    public static SoundEvent RLD_AR_1_INSERT = registerSoundEvent("ar_1_insert");
    public static SoundEvent RLD_AR_1_BOLTFORWARD = registerSoundEvent("ar_1_boltforward");

    public static SoundEvent RLD_AR_2_REMOVE = registerSoundEvent("ar_2_remove");
    public static SoundEvent RLD_AR_2_INSERT = registerSoundEvent("ar_2_insert");
    public static SoundEvent RLD_AR_2_BOLTBACK = registerSoundEvent("ar_2_boltback");
    public static SoundEvent RLD_AR_2_BOLTFORWARD = registerSoundEvent("ar_2_boltforward");

    public static SoundEvent RLD_SNIPER_1_INSERT = registerSoundEvent("sniper_1_insert");
    public static SoundEvent RLD_SNIPER_1_BOLTBACK = registerSoundEvent("sniper_1_boltback");
    public static SoundEvent RLD_SNIPER_1_BOLTFORWARD = registerSoundEvent("sniper_1_boltforward");

    private static SoundEvent registerSoundEvent(String name)
    {
        Identifier id = new Identifier(AnimatedGuns.MOD_ID, name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static void registerSounds()
    {
        AnimatedGuns.LOGGER.info("Registering ModSounds for " + AnimatedGuns.MOD_ID);
    }
}
