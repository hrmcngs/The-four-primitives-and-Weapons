package minecraftarmorweapon.procedures;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;

import minecraftarmorweapon.init.MinecraftArmorWeaponModItems;

public class KatanaNiguHumerusturugainbentoriniaruJiannoteitukuProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		if (entity instanceof Player _playerHasItem ? _playerHasItem.getInventory().contains(new ItemStack(MinecraftArmorWeaponModItems.KATANA_NIGU_HUMERUS.get())) : false) {
			new ItemStack(MinecraftArmorWeaponModItems.KATANA_NIGU_HUMERUS.get()).getOrCreateTag().putDouble("HideFlags", 2);
		}
	}
}
