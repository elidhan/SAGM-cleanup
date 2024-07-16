package net.elidhan.anim_guns.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.MathHelper;

public class RecoilHandler
{
    private static RecoilHandler recoilHandler;

    public static RecoilHandler getInstance()
    {
        if (recoilHandler == null)
            recoilHandler = new RecoilHandler();

        return recoilHandler;
    }

    private float recoilAmount = 0;
    private int recoilTick = 0;
    private int prevRecoilTick = 0;

    public void shot(float recoil)
    {
        recoilAmount = recoil;
        recoilTick = 2;
    }

    public void tick()
    {
        prevRecoilTick = recoilTick;
        if (recoilTick > 0) recoilTick--;
    }

    public void render(MinecraftClient client)
    {
        ClientPlayerEntity player = client.player;

        if (player == null) return;

        float delta = MathHelper.lerp(client.getTickDelta(), prevRecoilTick, recoilTick);
        float actualRecoil = delta * recoilAmount * 1.5f * client.getLastFrameDuration();

        player.setPitch(player.getPitch() - actualRecoil);
        player.prevPitch = player.getPitch();
    }
}