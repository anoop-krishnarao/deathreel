package dev.anoopkrishnarao.deathreel.recorder;

import net.minecraft.client.player.LocalPlayer;

public class FrameRecorder {
    private static final int BUFFER_SIZE = 1200; // 60 seconds * 20 ticks

    private final PlayerFrame[] frames = new PlayerFrame[BUFFER_SIZE];
    private int head = 0;
    private int size = 0;
    private boolean snapshotted = false;

    public void record(LocalPlayer player) {
        frames[head] = new PlayerFrame(
                player.getX(),
                player.getY(),
                player.getZ(),
                player.getYRot(),
                player.getXRot(),
                player.isCrouching(),
                player.isSprinting(),
                player.onGround(),
                player.getMainHandItem(),
                player.getOffhandItem()
        );
        head = (head + 1) % BUFFER_SIZE;
        if (size < BUFFER_SIZE) size++;
    }

    public PlayerFrame[] snapshot() {
        snapshotted = true;
        PlayerFrame[] result = new PlayerFrame[size];
        for (int i = 0; i < size; i++) {
            result[i] = frames[(head - size + i + BUFFER_SIZE) % BUFFER_SIZE];
        }
        return result;
    }

    public void reset() {
        head = 0;
        size = 0;
        snapshotted = false;
    }

    public int getSize() {
        return size;
    }

    public boolean hasSnapshotted() {
        return snapshotted;
    }
}