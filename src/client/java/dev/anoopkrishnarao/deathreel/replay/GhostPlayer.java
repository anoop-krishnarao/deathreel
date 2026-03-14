package dev.anoopkrishnarao.deathreel.replay;

import dev.anoopkrishnarao.deathreel.recorder.PlayerFrame;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.phys.Vec3;
import net.minecraft.client.player.RemotePlayer;

public class GhostPlayer {
    private final RemotePlayer entity;
    private final PlayerFrame[] frames;
    private int currentFrame = 0;
    private boolean finished = false;

    public GhostPlayer(RemotePlayer entity, PlayerFrame[] frames) {
        this.entity = entity;
        this.frames = frames;
    }

    public void tick() {
        if (finished || currentFrame >= frames.length) {
            finished = true;
            return;
        }
        PlayerFrame frame = frames[currentFrame];

        // Position
        entity.setPos(frame.x, frame.y, frame.z);

        // Rotation
        entity.setYRot(frame.yaw);
        entity.setXRot(frame.pitch);
        entity.yHeadRot = frame.yaw;

        // Velocity
        entity.setDeltaMovement(new Vec3(frame.vx, frame.vy, frame.vz));

        // Pose
        entity.setPose(frame.pose);

        // Sneaking/sprinting
        entity.setShiftKeyDown(frame.isSneaking);
        entity.setSprinting(frame.isSprinting);

        currentFrame++;
    }

    public boolean isFinished() { return finished; }
    public RemotePlayer getEntity() { return entity; }
}