package net.elidhan.anim_guns.client;

import net.elidhan.anim_guns.util.Easings;
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

    private final Random random = new Random();
    private float recoilAmountX = 0;
    private float recoilAmountY = 0;
    private int recoilTick = 0;
    private int prevRecoilTick = 0;
    private int cameraRecoilTick = 0;
    private int prevCameraRecoilTick = 0;

    public void shot(float recoilX, float recoilY)
    {
        recoilAmountX = recoilX * (random.nextBoolean() ? 1 : -1);
        recoilAmountY = recoilY;
        recoilTick = (int)recoilY*4;

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
        float actualRecoilX = delta * recoilAmountX * 1.5f * client.getLastFrameDuration();
        float actualRecoilY = delta * recoilAmountY * 1.5f * client.getLastFrameDuration();

        player.setPitch(player.getPitch() - actualRecoilY);
        player.setYaw(player.getYaw() - actualRecoilX);
        player.prevPitch = player.getPitch();
    }

    public float getViewmodelRecoil(float delta)
    {
        return Easings.easeOutCubic(MathHelper.lerp(delta, (float)prevRecoilTick, (float)recoilTick));
    }
}
