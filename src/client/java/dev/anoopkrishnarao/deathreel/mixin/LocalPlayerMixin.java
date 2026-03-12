package dev.anoopkrishnarao.deathreel.mixin;

import dev.anoopkrishnarao.deathreel.DeathReelClient;
import net.minecraft.client.player.LocalPlayer;
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

            if (DeathReelClient.RECORDER.getSize() % 100 == 0
                    && DeathReelClient.RECORDER.getSize() > 0) {
                DeathReelClient.LOGGER.info("DeathReel: Recording... frames="
                        + DeathReelClient.RECORDER.getSize());
            }
        } else if (!deathHandled && DeathReelClient.RECORDER.getSize() > 20) {
            deathHandled = true;
            DeathReelClient.LOGGER.info("DeathReel: Player died! Frames recorded: "
                    + DeathReelClient.RECORDER.getSize());
            DeathReelClient.RECORDER.snapshot();
            DeathReelClient.RECORDER.reset();
        }
    }
}