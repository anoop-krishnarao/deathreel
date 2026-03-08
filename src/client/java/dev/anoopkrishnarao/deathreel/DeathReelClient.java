package dev.anoopkrishnarao.deathreel;

import dev.anoopkrishnarao.deathreel.recorder.FrameRecorder;
import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeathReelClient implements ClientModInitializer {
	public static final String MOD_ID = "deathreel";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final FrameRecorder RECORDER = new FrameRecorder();

	@Override
	public void onInitializeClient() {
		LOGGER.info("DeathReel initialized.");
	}
}