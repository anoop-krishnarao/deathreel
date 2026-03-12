package dev.anoopkrishnarao.deathreel.mixin;

import dev.anoopkrishnarao.deathreel.DeathReelClient;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.zombie.Zombie;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LocalPlayerMixin {
    private boolean deathHandled = false;

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        if (!((LivingEntity)(Object) this instanceof LocalPlayer player)) return;

        if (player.getHealth() > 0) {
            deathHandled = false;
            DeathReelClient.RECORDER.record(player);
        } else if (!deathHandled && DeathReelClient.RECORDER.getSize() > 20) {
            deathHandled = true;
            DeathReelClient.LOGGER.info("DeathReel: Player died! Frames recorded: "
                    + DeathReelClient.RECORDER.getSize());
            DeathReelClient.RECORDER.snapshot();

            // Spawn a zombie as a temporary ghost placeholder
            ClientLevel clientLevel = (ClientLevel) player.level();
            Zombie ghost = new Zombie(EntityType.ZOMBIE, clientLevel);
            ghost.setPos(player.getX(), player.getY(), player.getZ());
            ghost.setNoAi(true);
            clientLevel.addEntity(ghost);
            DeathReelClient.LOGGER.info("DeathReel: Ghost spawned at death position.");

            DeathReelClient.RECORDER.reset();
        }
    }
}