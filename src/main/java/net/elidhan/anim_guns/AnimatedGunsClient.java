package net.elidhan.anim_guns;

import net.elidhan.anim_guns.event.client.ClientPreAttackHandler;
import net.elidhan.anim_guns.network.ModNetworking;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.event.client.player.ClientPreAttackCallback;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import org.lwjgl.glfw.GLFW;

public class AnimatedGunsClient implements ClientModInitializer
{
	public static KeyBinding reloadKey = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.anim_guns.reload", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_R, KeyBinding.GAMEPLAY_CATEGORY));
	public static KeyBinding meleeKey = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.anim_guns.melee", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_V, KeyBinding.GAMEPLAY_CATEGORY));

	//Packets and stuff
	public static final Identifier C2S_RELOAD = new Identifier(AnimatedGuns.MOD_ID, "c2s_reload");
	public static final Identifier C2S_MELEE = new Identifier(AnimatedGuns.MOD_ID, "c2s_melee");

	@Override
	public void onInitializeClient()
	{
		//Intercept Attack mouse key
		ClientPreAttackCallback.EVENT.register(new ClientPreAttackHandler());

		//Reg
		ModNetworking.registerS2CPackets();

		//KeyBinds
		ClientTickEvents.START_CLIENT_TICK.register(client ->
		{
			while (reloadKey.wasPressed())
			{
				ClientPlayNetworking.send(C2S_RELOAD, PacketByteBufs.empty());
			}
			while (meleeKey.wasPressed())
			{
				if (client.player != null && client.interactionManager != null && client.crosshairTarget != null && client.crosshairTarget.getType() == HitResult.Type.ENTITY)
				{
					client.interactionManager.attackEntity(client.player, ((EntityHitResult)client.crosshairTarget).getEntity());
				}
				ClientPlayNetworking.send(C2S_MELEE, PacketByteBufs.empty());
			}
		});
	}
}