package minecraftarmorweapon.procedures;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.InteractionHand;
import net.minecraft.network.chat.Component;

import minecraftarmorweapon.network.MinecraftArmorWeaponModVariables;

import minecraftarmorweapon.init.MinecraftArmorWeaponModItems;

public class KabuserumobugaturudeGongJisaretatokiProcedure {
	public static void execute(Entity entity, Entity sourceentity) {
		if (entity == null || sourceentity == null)
			return;
		if ((entity.getCapability(MinecraftArmorWeaponModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new MinecraftArmorWeaponModVariables.PlayerVariables())).aaa == 1) {
			{
				Entity _entity = entity;
				if (_entity instanceof Player _player) {
					_player.getInventory().armor.set(3, (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY));
					_player.getInventory().setChanged();
				} else if (_entity instanceof LivingEntity _living) {
					_living.setItemSlot(EquipmentSlot.HEAD, (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY));
				}
			}
			(new ItemStack(MinecraftArmorWeaponModItems.KABUSERU.get())).setHoverName(Component.literal("\u304B\u3076\u308C\u3084"));
		}
		if ((entity.getCapability(MinecraftArmorWeaponModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new MinecraftArmorWeaponModVariables.PlayerVariables())).aaa == 2) {
			{
				Entity _entity = entity;
				if (_entity instanceof Player _player) {
					_player.getInventory().armor.set(2, (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY));
					_player.getInventory().setChanged();
				} else if (_entity instanceof LivingEntity _living) {
					_living.setItemSlot(EquipmentSlot.CHEST, (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY));
				}
			}
			(new ItemStack(MinecraftArmorWeaponModItems.KABUSERU.get())).setHoverName(Component.literal("\u80F4\u306B\u3053\u308C\u7740\u308D"));
		}
		if ((entity.getCapability(MinecraftArmorWeaponModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new MinecraftArmorWeaponModVariables.PlayerVariables())).aaa == 3) {
			{
				Entity _entity = entity;
				if (_entity instanceof Player _player) {
					_player.getInventory().armor.set(1, (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY));
					_player.getInventory().setChanged();
				} else if (_entity instanceof LivingEntity _living) {
					_living.setItemSlot(EquipmentSlot.LEGS, (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY));
				}
			}
			(new ItemStack(MinecraftArmorWeaponModItems.KABUSERU.get())).setHoverName(Component.literal("\u811A\u306B\u3053\u308C\u7740\u308D"));
		}
		if ((entity.getCapability(MinecraftArmorWeaponModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new MinecraftArmorWeaponModVariables.PlayerVariables())).aaa == 4) {
			{
				Entity _entity = entity;
				if (_entity instanceof Player _player) {
					_player.getInventory().armor.set(0, (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY));
					_player.getInventory().setChanged();
				} else if (_entity instanceof LivingEntity _living) {
					_living.setItemSlot(EquipmentSlot.FEET, (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY));
				}
			}
			(new ItemStack(MinecraftArmorWeaponModItems.KABUSERU.get())).setHoverName(Component.literal("\u8DB3\u306B\u3053\u308C\u7740\u308D"));
		}
		if ((entity.getCapability(MinecraftArmorWeaponModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new MinecraftArmorWeaponModVariables.PlayerVariables())).aaa == 5) {
			if (entity instanceof LivingEntity _entity) {
				ItemStack _setstack = (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY);
				_setstack.setCount(1);
				_entity.setItemInHand(InteractionHand.MAIN_HAND, _setstack);
				if (_entity instanceof Player _player)
					_player.getInventory().setChanged();
			}
			(new ItemStack(MinecraftArmorWeaponModItems.KABUSERU.get())).setHoverName(Component.literal("\u3053\u308C\u6301\u3066\u5229\u304D\u624B\u306B"));
		}
		if ((entity.getCapability(MinecraftArmorWeaponModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new MinecraftArmorWeaponModVariables.PlayerVariables())).aaa == 6) {
			if (entity instanceof LivingEntity _entity) {
				ItemStack _setstack = (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY);
				_setstack.setCount(1);
				_entity.setItemInHand(InteractionHand.OFF_HAND, _setstack);
				if (_entity instanceof Player _player)
					_player.getInventory().setChanged();
			}
			(new ItemStack(MinecraftArmorWeaponModItems.KABUSERU.get())).setHoverName(Component.literal("\u3053\u308C\u6301\u3066\u5229\u304D\u624B\u3058\u3083\u306A\u3044\u65B9\u306B"));
		}
	}
}
