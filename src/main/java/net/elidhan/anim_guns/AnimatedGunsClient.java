package net.elidhan.anim_guns;

import net.elidhan.anim_guns.client.RecoilHandler;
import net.elidhan.anim_guns.client.render.BulletRenderer;
import net.elidhan.anim_guns.event.client.ClientPreAttackHandler;
import net.elidhan.anim_guns.network.ModNetworking;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.api.event.client.player.ClientPreAttackCallback;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import org.lwjgl.glfw.GLFW;

public class AnimatedGunsClient implements ClientModInitializer
{
	public static KeyBinding reloadKey = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.anim_guns.reload", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_R, KeyBinding.GAMEPLAY_CATEGORY));
	public static KeyBinding meleeKey = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.anim_guns.melee", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_V, KeyBinding.GAMEPLAY_CATEGORY));

	@Override
	public void onInitializeClient()
	{
		//Intercept Attack mouse key
		ClientPreAttackCallback.EVENT.register(new ClientPreAttackHandler());

		ModNetworking.registerS2CPackets();

		EntityRendererRegistry.register(AnimatedGuns.BulletEntityType, BulletRenderer::new);

		WorldRenderEvents.START.register(context -> RecoilHandler.getInstance().render(MinecraftClient.getInstance()));

		ClientTickEvents.START_CLIENT_TICK.register(client ->
		{
			RecoilHandler.getInstance().tick();

			while (reloadKey.wasPressed())
			{
				ClientPlayNetworking.send(ModNetworking.C2S_RELOAD, PacketByteBufs.empty());
			}
			while (meleeKey.wasPressed())
			{
				if (client.player != null && client.interactionManager != null && client.crosshairTarget != null && client.crosshairTarget.getType() == HitResult.Type.ENTITY)
				{
					client.interactionManager.attackEntity(client.player, ((EntityHitResult)client.crosshairTarget).getEntity());
				}
				ClientPlayNetworking.send(ModNetworking.C2S_MELEE, PacketByteBufs.empty());
			}
		});
	}
}