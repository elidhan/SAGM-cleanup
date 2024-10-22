package net.elidhan.anim_guns;

import net.elidhan.anim_guns.config.ModConfigs;
import net.elidhan.anim_guns.entity.projectile.BulletProjectileEntity;
import net.elidhan.anim_guns.item.ModItems;
import net.elidhan.anim_guns.network.ModNetworking;
import net.elidhan.anim_guns.screen.BlueprintScreenHandler;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnimatedGuns implements ModInitializer
{
	public static final String MOD_ID = "anim_guns";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final EntityType<BulletProjectileEntity> BulletEntityType = Registry.register(Registries.ENTITY_TYPE, new Identifier(AnimatedGuns.MOD_ID, "bullet"), FabricEntityTypeBuilder.<BulletProjectileEntity>create(SpawnGroup.MISC, BulletProjectileEntity::new).dimensions(EntityDimensions.fixed(0.0625f, 0.0625f)).trackRangeBlocks(4).trackedUpdateRate(10).build());

	public static final ScreenHandlerType<BlueprintScreenHandler> BLUEPRINT_SCREEN_HANDLER_TYPE = ScreenHandlerType.register(MOD_ID+":blueprint_screen", BlueprintScreenHandler::new);

	@Override
	public void onInitialize()
	{
		LOGGER.info("Init S.A.G.M");

		//Reg
		ModConfigs.registerConfigs();

		ModItems.registerModItems();
		ModItems.registerModItemGroups();
		ModNetworking.registerC2SPackets();
	}
}