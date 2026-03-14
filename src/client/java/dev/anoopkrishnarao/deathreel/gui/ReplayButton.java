package dev.anoopkrishnarao.deathreel.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public class ReplayButton extends Button.Plain {

    private static final int DOT_RADIUS = 3;
    private static final int DOT_GAP = 5;
    private static final int BORDER_RADIUS = 4;

    public ReplayButton(int x, int y, int width, int height, Component message, OnPress onPress) {
        super(x, y, width, height, message, onPress, DEFAULT_NARRATION);
    }

    @Override
    protected void renderContents(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderDefaultSprite(guiGraphics);

        // Calculate total content width: text + gap + dot diameter
        int textWidth = Minecraft.getInstance().font.width(this.getMessage());
        int totalContentWidth = textWidth + DOT_GAP + BORDER_RADIUS * 2 + 1;

        // Center the whole content block
        int contentStartX = this.getX() + (this.getWidth() - totalContentWidth) / 2;
        int centerY = this.getY() + this.getHeight() / 2;

        // Draw text manually at the correct position
        int textColor = this.active ? this.getMessage().getStyle().getColor() != null
                ? this.getMessage().getStyle().getColor().getValue() | 0xFF000000
                : 0xFFFFFFFF
                : 0xFFA0A0A0;
        guiGraphics.drawString(Minecraft.getInstance().font,
                this.getMessage(), contentStartX, centerY - 4, textColor);

        // Dot position — to the right of text
        int dotX = contentStartX + textWidth + DOT_GAP + BORDER_RADIUS;
        int dotY = centerY;

        // Pulsing red dot
        long time = System.currentTimeMillis();
        float pulse = (float)(Math.sin(time / 500.0) * 0.5 + 0.5);
        int r = (int)(0xAA + pulse * (0xFF - 0xAA));
        int redColor = (0xFF << 24) | (r << 16);
        int borderColor = (0xFF << 24) | (0x33 << 16);

        // Border circle
        for (int dy = -BORDER_RADIUS; dy <= BORDER_RADIUS; dy++) {
            int dx = (int) Math.sqrt(BORDER_RADIUS * BORDER_RADIUS - dy * dy);
            guiGraphics.fill(dotX - dx, dotY + dy, dotX + dx + 1, dotY + dy + 1, borderColor);
        }

        // Inner pulsing circle
        for (int dy = -DOT_RADIUS; dy <= DOT_RADIUS; dy++) {
            int dx = (int) Math.sqrt(DOT_RADIUS * DOT_RADIUS - dy * dy);
            guiGraphics.fill(dotX - dx, dotY + dy, dotX + dx + 1, dotY + dy + 1, redColor);
        }
    }
}