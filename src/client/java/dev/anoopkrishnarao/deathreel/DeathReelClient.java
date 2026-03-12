package dev.anoopkrishnarao.deathreel;

import dev.anoopkrishnarao.deathreel.entity.GhostEntity;
import dev.anoopkrishnarao.deathreel.recorder.FrameRecorder;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeathReelClient implements ClientModInitializer {
	public static final String MOD_ID = "deathreel";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final FrameRecorder RECORDER = new FrameRecorder();
	public static EntityType<GhostEntity> GHOST_ENTITY_TYPE;

	@Override
	public void onInitializeClient() {
		GHOST_ENTITY_TYPE = Registry.register(
				BuiltInRegistries.ENTITY_TYPE,
				Identifier.parse("deathreel:ghost"),
				EntityType.Builder.<GhostEntity>of(GhostEntity::new, MobCategory.MISC)
						.sized(0.6f, 1.8f)
						.noSummon()
						.build(net.minecraft.resources.ResourceKey.create(
								net.minecraft.core.registries.Registries.ENTITY_TYPE,
								Identifier.parse("deathreel:ghost")
						))
		);
		LOGGER.info("DeathReel initialized.");
	}
}