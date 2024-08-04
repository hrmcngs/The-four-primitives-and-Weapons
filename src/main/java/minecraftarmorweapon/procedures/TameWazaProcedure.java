package minecraftarmorweapon.procedures;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.entity.living.LivingEvent;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffectInstance;

import minecraftarmorweapon.init.MinecraftArmorWeaponModMobEffects;
import minecraftarmorweapon.init.MinecraftArmorWeaponModItems;

import minecraftarmorweapon.MinecraftArmorWeaponMod;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class TameWazaProcedure {
	@SubscribeEvent
	public static void onEntityTick(LivingEvent.LivingTickEvent event) {
		execute(event, event.getEntity().level, event.getEntity());
	}

	public static void execute(LevelAccessor world, Entity entity) {
		execute(null, world, entity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == MinecraftArmorWeaponModItems.TEST_BOW.get()) {
			if (!(entity instanceof LivingEntity _livEnt ? _livEnt.hasEffect(MinecraftArmorWeaponModMobEffects.COOL_DOWN_1.get()) : false)) {
				if (entity.getPersistentData().getDouble("minecraft_armor_weapon:tame") >= 10) {
					if (entity instanceof LivingEntity _entity && !_entity.level.isClientSide())
						_entity.addEffect(new MobEffectInstance(MinecraftArmorWeaponModMobEffects.COOL_DOWN_1.get(), 40, 1, true, false));
					MinecraftArmorWeaponMod.queueServerWork(20, () -> {
						entity.getPersistentData().putDouble("minecraft_armor_weapon:tame", 0);
					});
				}
			}
		} else {
			entity.getPersistentData().putDouble("minecraft_armor_weapon:tame", 0);
		}
	}
}
