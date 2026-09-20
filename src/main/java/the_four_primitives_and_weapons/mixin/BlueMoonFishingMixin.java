package the_four_primitives_and_weapons.mixin;

import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import the_four_primitives_and_weapons.world.AstronomyData;

/** Add to the original loot context, preserving enchantments, loot tables and open-water checks. */
@Mixin(FishingHook.class)
public abstract class BlueMoonFishingMixin {
    @ModifyArg(method = "retrieve", at = @At(value = "INVOKE",
        target = "Lnet/minecraft/world/level/storage/loot/LootParams$Builder;withLuck(F)Lnet/minecraft/world/level/storage/loot/LootParams$Builder;"), index = 0, require = 1)
    private float astronomy$fishingLuck(float original) {
        Level level = ((FishingHook)(Object)this).level();
        if (level.isClientSide || !Level.OVERWORLD.equals(level.dimension())) return original;
        return original + AstronomyData.settings(level).fishingLuckBonus(level.getDayTime(), level.getMoonPhase());
    }
}
