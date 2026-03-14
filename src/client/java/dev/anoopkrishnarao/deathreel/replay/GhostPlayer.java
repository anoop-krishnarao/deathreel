package dev.anoopkrishnarao.deathreel.replay;

import dev.anoopkrishnarao.deathreel.recorder.PlayerFrame;
import net.minecraft.client.player.RemotePlayer;

public class GhostPlayer {
    private final RemotePlayer entity;
    private final PlayerFrame[] frames;
    private int currentFrame = 0;
    private boolean finished = false;
    private boolean paused = false;

    public GhostPlayer(RemotePlayer entity, PlayerFrame[] frames) {
        this.entity = entity;
        this.frames = frames;
    }

    public void tick() {
        if (finished || paused) return;
        if (currentFrame >= frames.length) {
            finished = true;
            return;
        }
        PlayerFrame frame = frames[currentFrame];
        entity.setPos(frame.x, frame.y, frame.z);
        entity.setYRot(frame.yaw);
        entity.setXRot(frame.pitch);
        entity.yHeadRot = frame.yaw;
        entity.setDeltaMovement(
                new net.minecraft.world.phys.Vec3(frame.vx, frame.vy, frame.vz));
        entity.setPose(frame.pose);
        entity.setShiftKeyDown(frame.isSneaking);
        entity.setSprinting(frame.isSprinting);
        currentFrame++;
    }

    public void seekTo(int frame) {
        this.currentFrame = Math.max(0, Math.min(frame, frames.length - 1));
        this.finished = false;
        // Apply frame immediately
        PlayerFrame f = frames[currentFrame];
        entity.setPos(f.x, f.y, f.z);
        entity.setYRot(f.yaw);
        entity.setXRot(f.pitch);
        entity.yHeadRot = f.yaw;
    }

    public void restart() {
        seekTo(0);
        paused = false;
        finished = false;
    }

    public void setPaused(boolean paused) { this.paused = paused; }
    public boolean isPaused() { return paused; }
    public boolean isFinished() { return finished; }
    public int getCurrentFrame() { return currentFrame; }
    public int getTotalFrames() { return frames.length; }
    public RemotePlayer getEntity() { return entity; }
}