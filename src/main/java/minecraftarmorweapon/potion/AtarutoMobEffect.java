
package minecraftarmorweapon.potion;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffect;

import minecraftarmorweapon.procedures.AtarutoehuekutogaYouXiaoShinoteitukuProcedure;

public class AtarutoMobEffect extends MobEffect {
	public AtarutoMobEffect() {
		super(MobEffectCategory.NEUTRAL, -1);
	}

	@Override
	public String getDescriptionId() {
		return "effect.minecraft_armor_weapon.ataruto";
	}

	@Override
	public void applyEffectTick(LivingEntity entity, int amplifier) {
		AtarutoehuekutogaYouXiaoShinoteitukuProcedure.execute(entity);
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return true;
	}
}
