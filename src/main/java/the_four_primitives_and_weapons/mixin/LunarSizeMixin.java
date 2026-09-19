package the_four_primitives_and_weapons.mixin;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.world.level.Level;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import the_four_primitives_and_weapons.world.LunarCycle;
import the_four_primitives_and_weapons.world.AstronomicalEvents;
import the_four_primitives_and_weapons.client.AstronomicalSkyRenderer;

/** Changes only the vanilla moon quad; its texture, phase and weather fading stay intact. */
@Mixin(LevelRenderer.class)
public abstract class LunarSizeMixin {
    @Shadow private ClientLevel level;

    @ModifyConstant(method = "renderSky", constant = @Constant(floatValue = 20.0F), require = 1, allow = 1)
    private float lunarCycle$moonSize(float originalSize) {
        if (level == null || !Level.OVERWORLD.equals(level.dimension())) return originalSize;
        return originalSize * LunarCycle.sizeMultiplier(level.getDayTime());
    }

    @Inject(method = "renderSky", at = @At(value = "INVOKE",
        target = "Lnet/minecraft/client/multiplayer/ClientLevel;getMoonPhase()I"), require = 1)
    private void lunarCycle$tintMoon(PoseStack pose, Matrix4f projection, float partialTick,
            Camera camera, boolean foggy, Runnable setupFog, CallbackInfo ci) {
        if (level == null || !Level.OVERWORLD.equals(level.dimension())) return;
        AstronomicalSkyRenderer.renderSolarEclipse(level, pose, partialTick);
        var tint = AstronomicalEvents.moonTint(level.getDayTime(), level.getMoonPhase());
        float eclipse = level.getMoonPhase() == 0 ? AstronomicalEvents.lunarEclipseStrength(level.getDayTime()) : 0F;
        RenderSystem.setShaderColor(tint.red * (1F - 0.65F * eclipse),
            tint.green * (1F - 0.94F * eclipse), tint.blue * (1F - 0.975F * eclipse),
            1F - level.getRainLevel(partialTick));
    }

    @Inject(method = "renderSky", at = @At(value = "INVOKE",
        target = "Lnet/minecraft/client/multiplayer/ClientLevel;getStarBrightness(F)F"), require = 1)
    private void lunarCycle$afterMoon(PoseStack pose, Matrix4f projection, float partialTick,
            Camera camera, boolean foggy, Runnable setupFog, CallbackInfo ci) {
        if (level == null || !Level.OVERWORLD.equals(level.dimension())) return;
        RenderSystem.setShaderColor(1F, 1F, 1F, 1F - level.getRainLevel(partialTick));
        AstronomicalSkyRenderer.renderMeteors(level, pose, partialTick);
    }
}
