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

    //Player camera recoil
    private float recoilAmountX = 0;
    private float recoilAmountY = 0;

    //Gun viewmodel recoil
    private float[] viewModelRecoil = {0,0,0,0,0};
    private int recoilTick = 0;
    private int prevRecoilTick = 0;
    private int duration = 0;

    public void shot(float recoilX, float recoilY, float[] viewModelRecoil)
    {
        int leftOrRight = random.nextBoolean() ? 1 : -1;
        recoilAmountX = recoilX * leftOrRight;
        recoilAmountY = recoilY;
        this.viewModelRecoil = viewModelRecoil;
        this.viewModelRecoil[1] *= leftOrRight;

        recoilTick = (int)viewModelRecoil[4];
        duration = (int)viewModelRecoil[4];
    }

    public void reset()
    {
        recoilTick = 0;
        prevRecoilTick = 0;
        duration = 0;
        this.viewModelRecoil = new float[] {0,0,0,0,0};
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

        if (recoilAmountY > 0) recoilAmountY = MathHelper.clamp(recoilAmountY - Easings.easeOutCubic(recoilAmountY * client.getTickDelta() * client.getLastFrameDuration()), 0, recoilAmountY);

        player.setPitch(player.getPitch() - recoilAmountY*0.625f);
        player.setYaw(player.getYaw() - recoilAmountX*0.5f);
        player.prevPitch = player.getPitch();
    }

    public float getVMRotUp(float delta)
    {

        return viewModelRecoil[0] * Easings.easeOutBack(MathHelper.lerp(delta, (float)prevRecoilTick, (float)recoilTick)/(duration == 0 ? 1 : duration), viewModelRecoil[0]/64);
    }
    public float getVMRotSide(float delta)
    {
        return viewModelRecoil[1] * Easings.easeOutBack(MathHelper.lerp(delta, (float)prevRecoilTick, (float)recoilTick)/(duration == 0 ? 1 : duration), viewModelRecoil[1]/64);
    }
    public float getVMMoveUp(float delta)
    {
        return viewModelRecoil[2] * Easings.easeOutBack(MathHelper.lerp(delta, (float)prevRecoilTick, (float)recoilTick)/(duration == 0 ? 1 : duration), viewModelRecoil[2])/64;
    }
    public float getVMMoveBack(float delta)
    {
        return viewModelRecoil[3] * Easings.easeOutBack(MathHelper.lerp(delta, (float)prevRecoilTick, (float)recoilTick)/(duration == 0 ? 1 : duration), viewModelRecoil[3]/64);
    }
}
