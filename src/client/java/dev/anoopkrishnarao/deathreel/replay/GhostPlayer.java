package dev.anoopkrishnarao.deathreel.replay;

import dev.anoopkrishnarao.deathreel.recorder.PlayerFrame;
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
        entity.setPos(frame.x, frame.y, frame.z);
        entity.setYRot(frame.yaw);
        entity.setXRot(frame.pitch);
        entity.yHeadRot = frame.yaw;
        currentFrame++;
    }

    public boolean isFinished() { return finished; }
    public RemotePlayer getEntity() { return entity; }
}