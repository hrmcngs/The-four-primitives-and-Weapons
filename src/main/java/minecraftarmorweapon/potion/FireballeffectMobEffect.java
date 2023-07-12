
package minecraftarmorweapon.potion;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffect;

import minecraftarmorweapon.procedures.FireballeffectehuekutogaYouXiaoShinoteitukuProcedure;

public class FireballeffectMobEffect extends MobEffect {
	public FireballeffectMobEffect() {
		super(MobEffectCategory.NEUTRAL, -5832704);
	}

	@Override
	public String getDescriptionId() {
		return "effect.minecraft_armor_weapon.fireballeffect";
	}

	@Override
	public void applyEffectTick(LivingEntity entity, int amplifier) {
		FireballeffectehuekutogaYouXiaoShinoteitukuProcedure.execute(entity);
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return true;
	}
}
