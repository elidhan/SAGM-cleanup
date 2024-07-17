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

    public void shot(float recoilX, float recoilY)
    {
        recoilAmountX = recoilX * (random.nextBoolean() ? 1 : -1);
        recoilAmountY = recoilY;
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

        if (recoilAmountX > 0)
            recoilAmountX = MathHelper.clamp(recoilAmountX - Easings.easeOutCubic(recoilAmountX * client.getTickDelta() * client.getLastFrameDuration()), 0, recoilAmountX);
        else
            recoilAmountX = MathHelper.clamp(recoilAmountX - Easings.easeOutCubic(recoilAmountX * client.getTickDelta() * client.getLastFrameDuration()), recoilAmountX, 0);

        //System.out.println(recoilAmountX);

        if (recoilAmountY > 0) recoilAmountY = MathHelper.clamp(recoilAmountY - Easings.easeOutCubic(recoilAmountY * client.getTickDelta() * client.getLastFrameDuration()), 0, recoilAmountY);

        float delta = MathHelper.lerp(client.getTickDelta(), prevRecoilTick, recoilTick);

        player.setPitch(player.getPitch() - recoilAmountY);
        player.setYaw(player.getYaw() - recoilAmountX*0.5f);
        player.prevPitch = player.getPitch();
    }

    public float getViewmodelRecoilX(float delta)
    {
        return recoilAmountX;
    }
    public float getViewmodelRecoilY(float delta)
    {
        return recoilAmountY;
    }
}
