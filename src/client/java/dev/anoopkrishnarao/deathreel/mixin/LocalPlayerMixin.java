package dev.anoopkrishnarao.deathreel.mixin;

import com.mojang.authlib.GameProfile;
import dev.anoopkrishnarao.deathreel.DeathReelClient;
import dev.anoopkrishnarao.deathreel.recorder.PlayerFrame;
import dev.anoopkrishnarao.deathreel.replay.GhostPlayer;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.world.entity.LivingEntity;
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
            DeathReelClient.lastSnapshot = DeathReelClient.RECORDER.snapshot();

            // Spawn ghost using player's skin
            ClientLevel clientLevel = (ClientLevel) player.level();
            GameProfile profile = player.getGameProfile();
            RemotePlayer ghost = new RemotePlayer(clientLevel, profile);
            ghost.setPos(player.getX(), player.getY(), player.getZ());
            ghost.setYRot(player.getYRot());
            ghost.setXRot(player.getXRot());
            ghost.yHeadRot = player.getYRot();
            clientLevel.addEntity(ghost);
            DeathReelClient.activeGhost = new GhostPlayer(ghost, DeathReelClient.lastSnapshot);
            DeathReelClient.LOGGER.info("DeathReel: Ghost spawned, starting replay.");

            DeathReelClient.RECORDER.reset();
        }
    }
}