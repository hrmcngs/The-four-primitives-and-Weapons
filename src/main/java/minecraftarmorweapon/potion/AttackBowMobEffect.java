
package minecraftarmorweapon.potion;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffect;

import minecraftarmorweapon.procedures.AttackBowposiyonnoXiaoGuogaKaiShiShiYongsaretatokiProcedure;

public class AttackBowMobEffect extends MobEffect {
	public AttackBowMobEffect() {
		super(MobEffectCategory.NEUTRAL, -1);
	}

	@Override
	public String getDescriptionId() {
		return "effect.minecraft_armor_weapon.attack_bow";
	}

	@Override
	public boolean isInstantenous() {
		return true;
	}

	@Override
	public void applyInstantenousEffect(Entity source, Entity indirectSource, LivingEntity entity, int amplifier, double health) {
		AttackBowposiyonnoXiaoGuogaKaiShiShiYongsaretatokiProcedure.execute(entity);
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return true;
	}
}
