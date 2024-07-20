package minecraftarmorweapon.procedures;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;

import minecraftarmorweapon.init.MinecraftArmorWeaponModMobEffects;

public class ChuzumeHuskArmorKnockBackposiyonnoXiaoGuogaKaiShiShiYongsaretatokiProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		entity.getPersistentData().putDouble("minecraft_armor_weapon:chuzume_husk_armor_effect_knockback", ((LivingEntity) entity).getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_KNOCKBACK).getBaseValue());
		entity.getPersistentData().putDouble("minecraft_armor_weapon:chuzume_husk_armor_effect_damage", ((LivingEntity) entity).getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE).getBaseValue());
		((LivingEntity) entity).getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_KNOCKBACK)
				.setBaseValue((entity instanceof LivingEntity _livEnt && _livEnt.hasEffect(MinecraftArmorWeaponModMobEffects.CHUZUME_HUSK_ARMOR_KNOCK_BACK.get())
						? _livEnt.getEffect(MinecraftArmorWeaponModMobEffects.CHUZUME_HUSK_ARMOR_KNOCK_BACK.get()).getAmplifier()
						: 0));
		((LivingEntity) entity).getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE)
				.setBaseValue((((LivingEntity) entity).getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE).getBaseValue()
						- (0.1 + 0.1 * (entity instanceof LivingEntity _livEnt && _livEnt.hasEffect(MinecraftArmorWeaponModMobEffects.CHUZUME_HUSK_ARMOR_KNOCK_BACK.get())
								? _livEnt.getEffect(MinecraftArmorWeaponModMobEffects.CHUZUME_HUSK_ARMOR_KNOCK_BACK.get()).getAmplifier()
								: 0))));
	}
}
