package dev.anoopkrishnarao.deathreel.gui;

import dev.anoopkrishnarao.deathreel.DeathReelClient;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ReplayViewerScreen extends Screen {

    private final Screen previousScreen;

    private static final int CONTROL_BAR_HEIGHT = 36;
    private static final int BUTTON_HEIGHT = 20;
    private static final int PADDING = 8;

    private int frameX, frameY, frameWidth, frameHeight;
    private int controlBarY;

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

        // Close button — bottom right
        this.addRenderableWidget(
                Button.builder(
                                Component.literal("✕ Close"),
                                button -> this.minecraft.setScreen(previousScreen)
                        )
                        .bounds(this.width - 90 - PADDING, buttonY, 90, BUTTON_HEIGHT)
                        .build()
        );
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        // Dark overlay
        guiGraphics.fill(0, 0, this.width, this.height, 0xCC000000);

        // Video frame
        guiGraphics.fill(frameX, frameY,
                frameX + frameWidth, frameY + frameHeight, 0xFF1A1A1A);
        guiGraphics.renderOutline(frameX, frameY,
                frameWidth, frameHeight, 0xFFFFFFFF);

        // Placeholder text
        guiGraphics.drawCenteredString(this.font,
                Component.literal("Replay Viewer"),
                frameX + frameWidth / 2,
                frameY + frameHeight / 2,
                0xFFFFFFFF);

        // Control bar background
        guiGraphics.fill(0, controlBarY, this.width, this.height, 0xFF111111);

        // Title in control bar
        guiGraphics.drawString(this.font,
                Component.literal("☠ Death Replay"),
                PADDING,
                controlBarY + (CONTROL_BAR_HEIGHT - 8) / 2,
                0xFFFF00);

        // Render buttons on top
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        // No background
    }
}