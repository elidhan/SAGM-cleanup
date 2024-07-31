package net.elidhan.anim_guns.client;

import net.elidhan.anim_guns.util.Easings;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.MathHelper;
import org.joml.Vector3f;
import org.joml.Vector4f;

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
    private float cameraRecoilX = 0;
    private float cameraRecoilY = 0;
    private int cameraRecoilTick = 0;
    private int prevCameraRecoilTick = 0;

    //Gun viewmodel recoil
    private Vector4f viewModelRecoil = new Vector4f(0,0,0,0);
    private Vector3f viewModelRecoilMult = new Vector3f(0,0,0);
    private int viewModelRecoilTick = 0;
    private int prevViewModelRecoilTick = 0;
    private int viewModelRecoilDuration = 0;

    public void shot(float recoilX, float recoilY, Vector4f viewModelRecoil, Vector3f viewModelRecoilMult, int viewModelRecoilDuration)
    {
        int leftOrRight = random.nextBoolean() ? 1 : -1;
        cameraRecoilX = recoilX * leftOrRight;
        cameraRecoilY = recoilY;
        cameraRecoilTick = 3;

        this.viewModelRecoil = viewModelRecoil;
        this.viewModelRecoilMult = viewModelRecoilMult;
        this.viewModelRecoil.x *= leftOrRight;
        this.viewModelRecoilTick = this.viewModelRecoilDuration = viewModelRecoilDuration;
    }

    public void tick()
    {
        prevCameraRecoilTick = cameraRecoilTick;
        prevViewModelRecoilTick = viewModelRecoilTick;

        if (cameraRecoilTick > 0) cameraRecoilTick--;
        if (viewModelRecoilTick > 0) viewModelRecoilTick--;
    }

    public void render(MinecraftClient client)
    {
        ClientPlayerEntity player = client.player;

        if (player == null) return;

        float f = Easings.easeOutCubic(MathHelper.lerp(client.getTickDelta(), (float) prevCameraRecoilTick, (float) cameraRecoilTick)/3);

        float actualRecoilX = cameraRecoilX * f;
        float actualRecoilY = cameraRecoilY * f;

        if (cameraRecoilTick > 0)
        {
            player.setYaw(player.getYaw() - actualRecoilX);
            player.setPitch(player.getPitch() - actualRecoilY);
        }
        player.prevPitch = player.getPitch();
    }

    public float getVMMoveUp(float delta, float aimProgress)
    {
        return viewModelRecoil.w * (1 + (this.viewModelRecoilMult.y - 1) * aimProgress) * Easings.easeOutBack(MathHelper.lerp(delta, (float) prevViewModelRecoilTick, (float) viewModelRecoilTick)/(viewModelRecoilDuration == 0 ? 1 : viewModelRecoilDuration), 1.7f) / 16;
    }
    public float getVMRotSide(float delta, float aimProgress)
    {
        return viewModelRecoil.x * (1 + (this.viewModelRecoilMult.x - 1) * aimProgress) * Easings.easeOutBack(MathHelper.lerp(delta, (float) prevViewModelRecoilTick, (float) viewModelRecoilTick)/(viewModelRecoilDuration == 0 ? 1 : viewModelRecoilDuration), 1.7f);
    }
    public float getVMRotUp(float delta, float aimProgress)
    {
        return viewModelRecoil.y * (1 + (this.viewModelRecoilMult.y - 1) * aimProgress) * Easings.easeOutBack(MathHelper.lerp(delta, (float) prevViewModelRecoilTick, (float) viewModelRecoilTick)/(viewModelRecoilDuration == 0 ? 1 : viewModelRecoilDuration), 1.7f);
    }
    public float getVMMoveBack(float delta, float aimProgress)
    {
        return viewModelRecoil.z * (1 + (this.viewModelRecoilMult.z - 1) * aimProgress) * Easings.easeOutBack(MathHelper.lerp(delta, (float) prevViewModelRecoilTick, (float) viewModelRecoilTick)/(viewModelRecoilDuration == 0 ? 1 : viewModelRecoilDuration), 1.7f);
    }
}
