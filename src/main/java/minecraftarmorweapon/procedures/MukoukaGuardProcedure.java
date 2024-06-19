package minecraftarmorweapon.procedures;

import top.theillusivec4.curios.api.CuriosApi;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.entity.living.LivingAttackEvent;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;

import minecraftarmorweapon.init.MinecraftArmorWeaponModMobEffects;
import minecraftarmorweapon.init.MinecraftArmorWeaponModItems;

import minecraftarmorweapon.entity.BlackholeEntity;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class MukoukaGuardProcedure {
	@SubscribeEvent
	public static void onEntityAttacked(LivingAttackEvent event) {
		if (event != null && event.getEntity() != null) {
			execute(event, event.getEntity());
		}
	}

	public static void execute(Entity entity) {
		execute(null, entity);
	}

	private static void execute(@Nullable Event event, Entity entity) {
		if (entity == null)
			return;
		if (entity instanceof LivingEntity _livEnt ? _livEnt.hasEffect(MinecraftArmorWeaponModMobEffects.GUARD.get()) : false) {
			if (event != null && event.isCancelable()) {
				event.setCanceled(true);
			}
		}
		if (entity instanceof BlackholeEntity) {
			if (event != null && event.isCancelable()) {
				event.setCanceled(true);
			}
		}
		if (!(CuriosApi.getCuriosHelper().getCurioTags(MinecraftArmorWeaponModItems.NAIHAZUNO_RING.get())).isEmpty()) {
			if (event != null && event.isCancelable()) {
				event.setCanceled(true);
			}
		}
	}
}
