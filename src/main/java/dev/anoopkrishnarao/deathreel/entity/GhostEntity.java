package dev.anoopkrishnarao.deathreel.entity;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;

public class GhostEntity extends PathfinderMob {

    public static final EntityType<GhostEntity> TYPE = EntityType.Builder
            .<GhostEntity>of(GhostEntity::new, MobCategory.MISC)
            .sized(0.6f, 1.8f)
            .noSummon()
            .build(ResourceKey.create(
                    Registries.ENTITY_TYPE,
                    Identifier.parse("deathreel:ghost")
            ));

    public GhostEntity(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level);
        this.setNoAi(true);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket(
            net.minecraft.server.level.ServerEntity serverEntity) {
        throw new UnsupportedOperationException("GhostEntity is client-only");
    }
}
