package minecraftarmorweapon.procedures;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.client.gui.screens.Screen;

import minecraftarmorweapon.init.MinecraftArmorWeaponModMobEffects;

public class GoldenKatanaYoukuritukusitatokiProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		if (Screen.hasShiftDown()) {
			if (entity instanceof LivingEntity _entity && !_entity.level.isClientSide())
				_entity.addEffect(new MobEffectInstance(MinecraftArmorWeaponModMobEffects.GUARD.get(), 8, 1, true, false));
		}
	}
}
