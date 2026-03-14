package dev.anoopkrishnarao.deathreel;

import dev.anoopkrishnarao.deathreel.recorder.FrameRecorder;
import dev.anoopkrishnarao.deathreel.recorder.PlayerFrame;
import dev.anoopkrishnarao.deathreel.replay.GhostPlayer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeathReelClient implements ClientModInitializer {
	public static final String MOD_ID = "deathreel";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final FrameRecorder RECORDER = new FrameRecorder();
	public static PlayerFrame[] lastSnapshot = null;
	public static GhostPlayer activeGhost = null;

	@Override
	public void onInitializeClient() {
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (activeGhost != null) {
				activeGhost.tick();
				if (activeGhost.isFinished()) {
					activeGhost.getEntity().discard();
					activeGhost = null;
					LOGGER.info("DeathReel: Replay finished.");
				}
			}
		});

		LOGGER.info("DeathReel initialized.");
	}
}