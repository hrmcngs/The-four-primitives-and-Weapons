
package minecraftarmorweapon.potion;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffect;

import minecraftarmorweapon.procedures.AttackBubbleshotehuekutogaYouXiaoShinoteitukuProcedure;

public class AttackBubbleshotMobEffect extends MobEffect {
	public AttackBubbleshotMobEffect() {
		super(MobEffectCategory.NEUTRAL, -15662849);
	}

	@Override
	public String getDescriptionId() {
		return "effect.minecraft_armor_weapon.attack_bubbleshot";
	}

	@Override
	public void applyEffectTick(LivingEntity entity, int amplifier) {
		AttackBubbleshotehuekutogaYouXiaoShinoteitukuProcedure.execute(entity);
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return true;
	}
}
