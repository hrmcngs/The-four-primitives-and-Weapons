package minecraftarmorweapon.procedures;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;

import minecraftarmorweapon.init.MinecraftArmorWeaponModMobEffects;

public class KaitennposiyonnoXiaoGuogaKaiShiShiYongsaretatokiProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		if (entity instanceof LivingEntity _entity && !_entity.level.isClientSide())
			_entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN,
					(int) ((entity instanceof LivingEntity _livEnt && _livEnt.hasEffect(MinecraftArmorWeaponModMobEffects.KAITEN.get()) ? _livEnt.getEffect(MinecraftArmorWeaponModMobEffects.KAITEN.get()).getDuration() : 0) + 20), 1, true, false));
	}
}
