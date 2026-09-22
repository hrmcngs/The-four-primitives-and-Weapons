package the_four_primitives_and_weapons.mixin;

import java.util.SortedMap;
import com.mojang.blaze3d.vertex.BufferBuilder;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.client.renderer.RenderType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import the_four_primitives_and_weapons.client.KeitoGlint;

@Mixin(RenderBuffers.class)
public abstract class KeitoGlintBuffersMixin {
    @Shadow @Final private SortedMap<RenderType, BufferBuilder> fixedBuffers;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void tfpaw$registerGlintBuffers(CallbackInfo ci) {
        KeitoGlint.addBuffers(fixedBuffers);
    }
}
