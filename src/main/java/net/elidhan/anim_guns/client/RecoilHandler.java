package net.elidhan.anim_guns.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.MathHelper;

import java.util.Random;

public class RecoilHandler
{
    private static RecoilHandler recoilHandler;

    public static RecoilHandler getInstance()
    {
        if (recoilHandler == null)
            recoilHandler = new RecoilHandler();

        return recoilHandler;
    }

    private Random random = new Random();
    private float recoilAmount = 0;
    private int recoilTick = 0;
    private int prevRecoilTick = 0;
    private int cameraRecoilTick = 0;
    private int prevCameraRecoilTick = 0;

    public void shot(float recoil)
    {
        recoilAmount = recoil;
        recoilTick = (int)recoil*4;

        cameraRecoilTick = 2;
    }

    public void tick()
    {
        prevRecoilTick = recoilTick;
        prevCameraRecoilTick = cameraRecoilTick;

        if (recoilTick > 0) recoilTick--;
        if (cameraRecoilTick > 0) cameraRecoilTick--;
    }

    public void render(MinecraftClient client)
    {
        ClientPlayerEntity player = client.player;

        if (player == null) return;

        float delta = MathHelper.lerp(client.getTickDelta(), prevCameraRecoilTick, cameraRecoilTick);
        float actualRecoil = delta * recoilAmount * 1.5f * client.getLastFrameDuration();

        player.setPitch(player.getPitch() - actualRecoil);
        player.prevPitch = player.getPitch();
    }

    public float getViewmodelRecoil(float delta)
    {
        return MathHelper.lerp(delta, (float)prevRecoilTick, (float)recoilTick);
    }
}
