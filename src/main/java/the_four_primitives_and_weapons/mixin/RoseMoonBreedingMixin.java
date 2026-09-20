package the_four_primitives_and_weapons.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import the_four_primitives_and_weapons.world.AstronomyData;

@Mixin(Animal.class)
public abstract class RoseMoonBreedingMixin {
    @Inject(method = "finalizeSpawnChildFromBreeding", at = @At("RETURN"))
    private void astronomy$breedingCooldown(ServerLevel level, Animal partner, AgeableMob child, CallbackInfo ci) {
        if (!Level.OVERWORLD.equals(level.dimension())) return;
        var settings = AstronomyData.settings(level);
        Animal self = (Animal)(Object)this;
        self.setAge(settings.breedingCooldown(level.getDayTime(), level.getMoonPhase(), self.getAge()));
        partner.setAge(settings.breedingCooldown(level.getDayTime(), level.getMoonPhase(), partner.getAge()));
    }
}
