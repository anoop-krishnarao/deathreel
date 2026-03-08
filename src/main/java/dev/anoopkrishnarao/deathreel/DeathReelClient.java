package dev.anoopkrishnarao.deathreel;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeathReelClient implements ClientModInitializer {
	public static final String MOD_ID = "deathreel";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitializeClient() {
		LOGGER.info("DeathReel initialized.");
	}
}
