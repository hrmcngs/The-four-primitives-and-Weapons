package the_four_primitives_and_weapons.mixin;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LightTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import the_four_primitives_and_weapons.client.SolarEclipseLighting;

@Mixin(LightTexture.class)
public abstract class EclipseLightTextureMixin {
    @Redirect(method = "updateLightTexture", at = @At(value = "INVOKE",
        target = "Lnet/minecraft/client/multiplayer/ClientLevel;getSkyDarken(F)F"))
    private float eclipse$skyLight(ClientLevel level, float partialTick) {
        return level.getSkyDarken(partialTick) * SolarEclipseLighting.brightness();
    }
}
