package net.elidhan.anim_guns.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.MathHelper;

import java.util.Random;

@Environment(EnvType.CLIENT)
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

    private float leftOrRight = 0.0f;

    //Player camera recoil
    private float cameraRecoilX = 0;
    private float cameraRecoilY = 0;
    private int cameraRecoilTick = 0;
    private int prevCameraRecoilTick = 0;

    public void shot(float recoilX, float recoilY)
    {
        leftOrRight = (random.nextBoolean() ? 1 : -1) * (float)(0.25f + Math.random() * (1f - (0.25f)));
        cameraRecoilX = recoilX * leftOrRight;
        cameraRecoilY = recoilY;
        cameraRecoilTick = 2;
    }

    public float getLeftOrRight()
    {
        return this.leftOrRight;
    }

    public void tick()
    {
        prevCameraRecoilTick = cameraRecoilTick;

        if (cameraRecoilTick > 0) cameraRecoilTick--;
    }

    public void render(MinecraftClient client)
    {
        ClientPlayerEntity player = client.player;

        if (player == null) return;

        float f = MathHelper.lerp(client.getTickDelta(), (float) prevCameraRecoilTick, (float) cameraRecoilTick);

        float actualRecoilX = cameraRecoilX * f * client.getLastFrameDuration() * 0.625f;
        float actualRecoilY = cameraRecoilY * f * client.getLastFrameDuration() * 0.625f;

        if (cameraRecoilTick > 0)
        {
            player.setYaw(player.getYaw() - actualRecoilX);
            player.setPitch(player.getPitch() - actualRecoilY);
        }
        player.prevPitch = player.getPitch();
    }
}
