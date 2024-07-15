package net.elidhan.anim_guns;

import net.elidhan.anim_guns.item.ModItems;
import net.elidhan.anim_guns.network.ModNetworking;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnimatedGuns implements ModInitializer
{
	public static final String MOD_ID = "anim_guns";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize()
	{
		LOGGER.info("Init S.A.G.M");

		//Reg
		ModItems.registerModItems();
		ModItems.registerModItemGroups();
		ModNetworking.registerC2SPackets();
	}
}