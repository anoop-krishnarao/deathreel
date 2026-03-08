package dev.anoopkrishnarao.deathreel.recorder;

import net.minecraft.world.item.ItemStack;

public class PlayerFrame {
    public final double x, y, z;
    public final float yaw, pitch;
    public final boolean isSneaking;
    public final boolean isSprinting;
    public final boolean isOnGround;
    public final ItemStack mainHandItem;
    public final ItemStack offHandItem;

    public PlayerFrame(
            double x, double y, double z,
            float yaw, float pitch,
            boolean isSneaking, boolean isSprinting, boolean isOnGround,
            ItemStack mainHandItem, ItemStack offHandItem
    ) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.isSneaking = isSneaking;
        this.isSprinting = isSprinting;
        this.isOnGround = isOnGround;
        this.mainHandItem = mainHandItem.copy();
        this.offHandItem = offHandItem.copy();
    }
}