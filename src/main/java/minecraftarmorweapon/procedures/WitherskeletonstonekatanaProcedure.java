package minecraftarmorweapon.procedures;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.InteractionHand;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;

import minecraftarmorweapon.init.MinecraftArmorWeaponModItems;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class WitherskeletonstonekatanaProcedure {
	@SubscribeEvent
	public static void onEntitySpawned(EntityJoinLevelEvent event) {
		execute(event, event.getEntity());
	}

	public static void execute(Entity entity) {
		execute(null, entity);
	}

	private static void execute(@Nullable Event event, Entity entity) {
		if (entity == null)
			return;
		if (entity instanceof WitherSkeleton) {
			if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == MinecraftArmorWeaponModItems.STONE_KATANA.get() && Mth.nextInt(RandomSource.create(), 0, 5) == 1) {
				if (entity instanceof LivingEntity _entity) {
					ItemStack _setstack = new ItemStack(MinecraftArmorWeaponModItems.MOTO_WITHER_KATANA.get());
					_setstack.setCount(1);
					_entity.setItemInHand(InteractionHand.MAIN_HAND, _setstack);
					if (_entity instanceof Player _player)
						_player.getInventory().setChanged();
				}
			} else if (Mth.nextInt(RandomSource.create(), 0, 5) == 1) {
				if (entity instanceof LivingEntity _entity) {
					ItemStack _setstack = new ItemStack(MinecraftArmorWeaponModItems.STONE_KATANA.get());
					_setstack.setCount(1);
					_entity.setItemInHand(InteractionHand.MAIN_HAND, _setstack);
					if (_entity instanceof Player _player)
						_player.getInventory().setChanged();
				}
			}
		}
	}
}
