package dev.anoopkrishnarao.deathreel.gui;

import com.mojang.authlib.GameProfile;
import dev.anoopkrishnarao.deathreel.DeathReelClient;
import dev.anoopkrishnarao.deathreel.recorder.PlayerFrame;
import dev.anoopkrishnarao.deathreel.replay.GhostPlayer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.network.chat.Component;

public class ReplayViewerScreen extends Screen {

    private final Screen previousScreen;

    private static final int CONTROL_BAR_HEIGHT = 36;
    private static final int BUTTON_HEIGHT = 20;
    private static final int PADDING = 8;

    private int frameX, frameY, frameWidth, frameHeight;
    private int controlBarY;

    private Button playPauseButton;

    public ReplayViewerScreen(Screen previousScreen) {
        super(Component.literal("Death Replay"));
        this.previousScreen = previousScreen;
    }

    @Override
    protected void init() {
        controlBarY = this.height - CONTROL_BAR_HEIGHT;
        frameX = PADDING;
        frameY = PADDING;
        frameWidth = this.width - PADDING * 2;
        frameHeight = controlBarY - PADDING * 2;

        int buttonY = controlBarY + (CONTROL_BAR_HEIGHT - BUTTON_HEIGHT) / 2;

        // Hide HUD while replay viewer is open
        this.minecraft.options.hideGui = true;

        // Start or restart ghost replay
        startReplay();

        // Play/Pause button — bottom center
        playPauseButton = Button.builder(
                        Component.literal("⏸ Pause"),
                        button -> togglePlayPause()
                )
                .bounds(this.width / 2 - 40, buttonY, 80, BUTTON_HEIGHT)
                .build();
        this.addRenderableWidget(playPauseButton);

        // Close button — bottom right
        this.addRenderableWidget(
                Button.builder(
                                Component.literal("✕ Close"),
                                button -> closeViewer()
                        )
                        .bounds(this.width - 90 - PADDING, buttonY, 90, BUTTON_HEIGHT)
                        .build()
        );
    }

    private void closeViewer() {
        this.minecraft.options.hideGui = false;
        if (DeathReelClient.activeGhost != null) {
            DeathReelClient.activeGhost.getEntity().discard();
            DeathReelClient.activeGhost = null;
        }
        this.minecraft.setScreen(previousScreen);
    }

    @Override
    public void onClose() {
        closeViewer();
    }

    private void startReplay() {
        if (DeathReelClient.lastSnapshot == null) return;

        // Discard any existing ghost
        if (DeathReelClient.activeGhost != null) {
            DeathReelClient.activeGhost.getEntity().discard();
            DeathReelClient.activeGhost = null;
        }

        // Spawn new ghost
        ClientLevel level = this.minecraft.level;
        if (level == null) return;

        GameProfile profile = this.minecraft.player.getGameProfile();
        RemotePlayer ghost = new RemotePlayer(level, profile);
        PlayerFrame firstFrame = DeathReelClient.lastSnapshot[0];
        ghost.setPos(firstFrame.x, firstFrame.y, firstFrame.z);
        level.addEntity(ghost);

        DeathReelClient.activeGhost = new GhostPlayer(ghost, DeathReelClient.lastSnapshot);
        DeathReelClient.LOGGER.info("DeathReel: Replay started from viewer.");
    }

    private void togglePlayPause() {
        if (DeathReelClient.activeGhost == null) return;
        boolean nowPaused = !DeathReelClient.activeGhost.isPaused();
        DeathReelClient.activeGhost.setPaused(nowPaused);
        playPauseButton.setMessage(
                Component.literal(nowPaused ? "▶ Play" : "⏸ Pause")
        );
    }

    @Override
    public void tick() {
        super.tick();
        if (DeathReelClient.activeGhost != null
                && DeathReelClient.activeGhost.isFinished()
                && playPauseButton != null) {
            playPauseButton.setMessage(Component.literal("▶ Play"));
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        // Only darken the control bar, not the video frame
        guiGraphics.fill(0, controlBarY, this.width, this.height, 0xEE111111);

        // Video frame border only (no fill — world shows through)
        guiGraphics.renderOutline(frameX, frameY, frameWidth, frameHeight, 0xFFFFFFFF);

        // Control bar title
        guiGraphics.drawString(this.font,
                Component.literal("☠ Death Replay"),
                PADDING,
                controlBarY + (CONTROL_BAR_HEIGHT - 8) / 2,
                0xFFFF00);

        // Render buttons on top
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean isPauseScreen() { return false; }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        // No background
    }
}