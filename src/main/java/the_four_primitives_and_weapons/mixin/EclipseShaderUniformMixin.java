package the_four_primitives_and_weapons.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.function.DoubleSupplier;
import the_four_primitives_and_weapons.client.SolarEclipseLighting;

/** Optional Oculus integration; reflection keeps Oculus out of mandatory dependencies. */
@Pseudo
@Mixin(targets = "net.irisshaders.iris.uniforms.CommonUniforms", remap = false)
public abstract class EclipseShaderUniformMixin {
    @Inject(method = "generalCommonUniforms", at = @At("HEAD"), remap = false)
    private static void eclipse$register(@Coerce Object holder, @Coerce Object notifier, @Coerce Object directives, CallbackInfo ci) {
        try {
            Class<?> frequency = Class.forName("net.irisshaders.iris.gl.uniform.UniformUpdateFrequency");
            Class<?> uniforms = Class.forName("net.irisshaders.iris.gl.uniform.UniformHolder");
            uniforms.getMethod("uniform1f", frequency, String.class, DoubleSupplier.class).invoke(holder,
                frequency.getField("PER_FRAME").get(null), "tfpawEclipseStrength", (DoubleSupplier) SolarEclipseLighting::strength);
        } catch (ReflectiveOperationException exception) {
            throw new IllegalStateException("Cannot register eclipse lighting uniform", exception);
        }
    }
}
