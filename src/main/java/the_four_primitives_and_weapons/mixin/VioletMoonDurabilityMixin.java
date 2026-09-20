package the_four_primitives_and_weapons.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import the_four_primitives_and_weapons.world.AstronomyData;
import the_four_primitives_and_weapons.world.AstronomicalEvents.MoonTint;

@Mixin(ItemStack.class)
public abstract class VioletMoonDurabilityMixin {
    @Inject(method = "hurt", at = @At("HEAD"), cancellable = true)
    private void astronomy$preserveDurability(int amount, RandomSource random, ServerPlayer player,
                                               CallbackInfoReturnable<Boolean> cir) {
        if (amount <= 0 || player == null || !player.isAlive() || player.isSpectator()
                || !Level.OVERWORLD.equals(player.level().dimension())) return;
        var level = player.serverLevel();
        var settings = AstronomyData.settings(level);
        if (settings.activeEffectColor(level.getDayTime(), level.getMoonPhase()) != MoonTint.VIOLET) return;
        if (settings.preserveDurability(level.getDayTime(), level.getMoonPhase(), random.nextFloat()))
            cir.setReturnValue(false);
    }
}
