package minecraftarmorweapon.procedures;

import net.minecraft.world.item.ItemStack;

public class KatanaNiguHumerusturugainbentoriniaruJiannoteitukuProcedure {
	public static void execute(ItemStack itemstack) {
		itemstack.getOrCreateTag().putDouble("HideFlags", 2);
	}
}
