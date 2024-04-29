package minecraftarmorweapon.procedures;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;

public class TestposiyonXiaoGuogaQieretaShiProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		((LivingEntity) entity).getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_KNOCKBACK).setBaseValue((entity.getPersistentData().getDouble("minecraft_armor_weapon:ninjatou")));
		((LivingEntity) entity).getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE).setBaseValue((entity.getPersistentData().getDouble("minecraft_armor_weapon:ninjatouattack")));
	}
}
