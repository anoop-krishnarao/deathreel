package dev.anoopkrishnarao.deathreel.recorder;

import net.minecraft.world.entity.Pose;
import net.minecraft.world.item.ItemStack;

public class PlayerFrame {
    // Position
    public final double x, y, z;
    // Rotation
    public final float yaw, pitch;
    // Velocity
    public final double vx, vy, vz;
    // States
    public final boolean isSneaking, isSprinting, isOnGround;
    // Pose
    public final Pose pose;
    // Items
    public final ItemStack mainHandItem, offHandItem;

    public PlayerFrame(
            double x, double y, double z,
            float yaw, float pitch,
            double vx, double vy, double vz,
            boolean isSneaking, boolean isSprinting, boolean isOnGround,
            Pose pose,
            ItemStack mainHandItem, ItemStack offHandItem) {
        this.x = x; this.y = y; this.z = z;
        this.yaw = yaw; this.pitch = pitch;
        this.vx = vx; this.vy = vy; this.vz = vz;
        this.isSneaking = isSneaking;
        this.isSprinting = isSprinting;
        this.isOnGround = isOnGround;
        this.pose = pose;
        this.mainHandItem = mainHandItem.copy();
        this.offHandItem = offHandItem.copy();
    }
}