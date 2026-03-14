package dev.anoopkrishnarao.deathreel.mixin;

import dev.anoopkrishnarao.deathreel.DeathReelClient;
import dev.anoopkrishnarao.deathreel.gui.ReplayButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DeathScreen.class)
public class DeathScreenMixin extends Screen {

    private static final int VANILLA_BUTTON_WIDTH = 150;
    private static final int REPLAY_BUTTON_WIDTH = 150;
    private static final int BUTTON_HEIGHT = 20;
    private static final int REPLAY_BUTTON_COLOR = 0xFFFF00;

    @Unique
    private ReplayButton deathReelReplayButton = null;

    protected DeathScreenMixin() {
        super(Component.empty());
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void onInit(CallbackInfo ci) {
        int centerX = this.width / 2;
        int buttonY = this.height / 4 + 72;
        int gap = 4;
        int totalWidth = VANILLA_BUTTON_WIDTH * 2 + REPLAY_BUTTON_WIDTH + gap * 2;
        int startX = centerX - totalWidth / 2;

        // Reposition existing buttons
        int buttonIndex = 0;
        for (var widget : this.children()) {
            if (widget instanceof Button button) {
                if (buttonIndex == 0) {
                    button.setX(startX);
                    button.setY(buttonY);
                    button.setWidth(VANILLA_BUTTON_WIDTH);
                } else if (buttonIndex == 1) {
                    button.setX(startX + VANILLA_BUTTON_WIDTH + gap);
                    button.setY(buttonY);
                    button.setWidth(VANILLA_BUTTON_WIDTH);
                }
                buttonIndex++;
            }
        }

        // Add Death Replay button with pulsing REC dot
        deathReelReplayButton = new ReplayButton(
                startX + VANILLA_BUTTON_WIDTH * 2 + gap * 2, buttonY,
                REPLAY_BUTTON_WIDTH, BUTTON_HEIGHT,
                Component.literal("Death Replay")
                        .withStyle(Style.EMPTY.withColor(REPLAY_BUTTON_COLOR)),
                button -> {
                    DeathReelClient.LOGGER.info("DeathReel: Opening replay viewer.");
                }
        );

        deathReelReplayButton.active = false;
        this.addRenderableWidget(deathReelReplayButton);
    }

    @Inject(method = "setButtonsActive", at = @At("TAIL"))
    private void onSetButtonsActive(boolean active, CallbackInfo ci) {
        if (deathReelReplayButton != null) {
            deathReelReplayButton.active = active && DeathReelClient.lastSnapshot != null;
        }
    }
}