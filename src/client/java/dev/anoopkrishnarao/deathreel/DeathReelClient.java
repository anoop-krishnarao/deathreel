package dev.anoopkrishnarao.deathreel;

import dev.anoopkrishnarao.deathreel.recorder.FrameRecorder;
import dev.anoopkrishnarao.deathreel.recorder.PlayerFrame;
import dev.anoopkrishnarao.deathreel.replay.GhostPlayer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.monster.zombie.Zombie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeathReelClient implements ClientModInitializer {
	public static final String MOD_ID = "deathreel";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final FrameRecorder RECORDER = new FrameRecorder();
	public static PlayerFrame[] lastSnapshot = null;
	public static GhostPlayer activeGhost = null;
	public static EntityType<Zombie> GHOST_ENTITY_TYPE;

	@Override
	public void onInitializeClient() {
		GHOST_ENTITY_TYPE = Registry.register(
				BuiltInRegistries.ENTITY_TYPE,
				Identifier.parse("deathreel:ghost"),
				EntityType.Builder.<Zombie>of(
								(type, level) -> new Zombie(EntityType.ZOMBIE, level),
								MobCategory.MISC)
						.sized(0.6f, 1.8f)
						.noSummon()
						.build(ResourceKey.create(
								net.minecraft.core.registries.Registries.ENTITY_TYPE,
								Identifier.parse("deathreel:ghost")
						))
		);

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