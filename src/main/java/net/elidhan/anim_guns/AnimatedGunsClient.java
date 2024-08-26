package net.elidhan.anim_guns;

import net.elidhan.anim_guns.client.RecoilHandler;
import net.elidhan.anim_guns.client.render.BulletRenderer;
import net.elidhan.anim_guns.client.render.GunGUIRenderer;
import net.elidhan.anim_guns.event.client.ClientPreAttackHandler;
import net.elidhan.anim_guns.item.ModItems;
import net.elidhan.anim_guns.network.ModNetworking;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.api.event.client.player.ClientPreAttackCallback;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import org.lwjgl.glfw.GLFW;

public class AnimatedGunsClient implements ClientModInitializer
{
	public static KeyBinding reloadKey = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.anim_guns.reload", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_R, KeyBinding.GAMEPLAY_CATEGORY));
	public static KeyBinding meleeKey = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.anim_guns.melee", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_V, KeyBinding.GAMEPLAY_CATEGORY));

	static void registerGunWorldViewRenderer(Item gun)
	{
		//model
		Identifier gunId = Registries.ITEM.getId(gun);
		var renderer = new GunGUIRenderer(gunId);
		ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(renderer);
		BuiltinItemRendererRegistry.INSTANCE.register(gun, renderer);

		ModelLoadingPlugin.register(pluginContext ->
				pluginContext.addModels((new ModelIdentifier(gunId.withPath(gunId.getPath() + "_gui"), "inventory"))));
	}

	@Override
	public void onInitializeClient()
	{
		//Register gun non-first-person perspective renderers
		registerGunWorldViewRenderer(ModItems.DEV_PISTOL_2);
		registerGunWorldViewRenderer(ModItems.DEV_REVOLVER);
		registerGunWorldViewRenderer(ModItems.DEV_SHOTGUN);
		registerGunWorldViewRenderer(ModItems.DEV_SHOTGUN2);
		registerGunWorldViewRenderer(ModItems.DEV_ASSAULTRIFLE);
		registerGunWorldViewRenderer(ModItems.DEV_ASSAULTRIFLE_2);
		registerGunWorldViewRenderer(ModItems.DEV_ASSAULTRIFLE_3);
		registerGunWorldViewRenderer(ModItems.DEV_SNIPER);

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