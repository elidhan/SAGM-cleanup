package net.elidhan.anim_guns.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import org.joml.Quaternionf;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;


public class BulletUtil
{
    public static Vec3d horiSpread(PlayerEntity player, double x_spread)
    {
        Vec3d vec3d = player.getOppositeRotationVector(1.0f);
        Quaternionf quaternion = (new Quaternionf()).setAngleAxis((x_spread * 0.017453292F), vec3d.x, vec3d.y, vec3d.z);
        Vec3d vec3d2 = player.getRotationVec(1.0f);
        Vector3f vec3f = vec3d2.toVector3f().rotate(quaternion);
        vec3f.rotate(quaternion);

        return new Vec3d(vec3f);
    }
    public static Vec3d vertiSpread(PlayerEntity player, double y_spread)
    {
        Vec3d vec3d = getOppositeRotationVector(0,player.getYaw(1.0f)-90);
        Quaternionf quaternion = (new Quaternionf()).setAngleAxis((y_spread * 0.017453292F), vec3d.x, vec3d.y, vec3d.z);
        Vec3d vec3d2 = player.getRotationVec(1.0f);
        Vector3f vec3f = vec3d2.toVector3f().rotate(quaternion);
        vec3f.rotate(quaternion);

        return new Vec3d(vec3f);
    }

    protected static Vec3d getOppositeRotationVector(float pitch, float yaw) {
        float f = pitch * ((float)Math.PI / 180);
        float g = -yaw * ((float)Math.PI / 180);
        float h = MathHelper.cos(g);
        float i = MathHelper.sin(g);
        float j = MathHelper.cos(f);
        float k = MathHelper.sin(f);
        return new Vec3d(i * j, -k, h * j);
    }

}