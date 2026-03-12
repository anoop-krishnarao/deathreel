package dev.anoopkrishnarao.deathreel.entity;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;

public class GhostEntity extends PathfinderMob {

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