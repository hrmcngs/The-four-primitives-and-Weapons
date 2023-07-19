
package minecraftarmorweapon.potion;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffect;

import minecraftarmorweapon.procedures.TunderbolteffrctehuekutogaYouXiaoShinoteitukuProcedure;

public class TunderbolteffrctMobEffect extends MobEffect {
	public TunderbolteffrctMobEffect() {
		super(MobEffectCategory.NEUTRAL, -852224);
	}

	@Override
	public String getDescriptionId() {
		return "effect.minecraft_armor_weapon.tunderbolteffrct";
	}

	@Override
	public void applyEffectTick(LivingEntity entity, int amplifier) {
		TunderbolteffrctehuekutogaYouXiaoShinoteitukuProcedure.execute(entity);
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return true;
	}
}
