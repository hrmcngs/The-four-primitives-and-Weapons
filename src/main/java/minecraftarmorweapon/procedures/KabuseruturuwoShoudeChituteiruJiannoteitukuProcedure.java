package minecraftarmorweapon.procedures;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.chat.Component;

import minecraftarmorweapon.network.MinecraftArmorWeaponModVariables;

import minecraftarmorweapon.init.MinecraftArmorWeaponModItems;

public class KabuseruturuwoShoudeChituteiruJiannoteitukuProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		if (MinecraftArmorWeaponModItems.KABUSERU.get() == (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem()) {
			if ((entity.getCapability(MinecraftArmorWeaponModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new MinecraftArmorWeaponModVariables.PlayerVariables())).aaa == 1) {
				(new ItemStack(MinecraftArmorWeaponModItems.KABUSERU.get())).setHoverName(Component.literal("\u304B\u3076\u308C\u3084"));
			}
			if ((entity.getCapability(MinecraftArmorWeaponModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new MinecraftArmorWeaponModVariables.PlayerVariables())).aaa == 2) {
				(new ItemStack(MinecraftArmorWeaponModItems.KABUSERU.get())).setHoverName(Component.literal("\u80F4\u306B\u3053\u308C\u7740\u308D"));
			}
			if ((entity.getCapability(MinecraftArmorWeaponModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new MinecraftArmorWeaponModVariables.PlayerVariables())).aaa == 3) {
				(new ItemStack(MinecraftArmorWeaponModItems.KABUSERU.get())).setHoverName(Component.literal("\u811A\u306B\u3053\u308C\u7740\u308D"));
			}
			if ((entity.getCapability(MinecraftArmorWeaponModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new MinecraftArmorWeaponModVariables.PlayerVariables())).aaa == 4) {
				(new ItemStack(MinecraftArmorWeaponModItems.KABUSERU.get())).setHoverName(Component.literal("\u8DB3\u306B\u3053\u308C\u7740\u308D"));
			}
			if ((entity.getCapability(MinecraftArmorWeaponModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new MinecraftArmorWeaponModVariables.PlayerVariables())).aaa == 5) {
				(new ItemStack(MinecraftArmorWeaponModItems.KABUSERU.get())).setHoverName(Component.literal("\u3053\u308C\u6301\u3066\u5229\u304D\u624B\u306B"));
			}
			if ((entity.getCapability(MinecraftArmorWeaponModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new MinecraftArmorWeaponModVariables.PlayerVariables())).aaa == 6) {
				(new ItemStack(MinecraftArmorWeaponModItems.KABUSERU.get())).setHoverName(Component.literal("\u3053\u308C\u6301\u3066\u5229\u304D\u624B\u3058\u3083\u306A\u3044\u65B9\u306B"));
			}
		}
	}
}
