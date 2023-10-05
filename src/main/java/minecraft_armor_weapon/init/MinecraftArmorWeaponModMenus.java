
/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package minecraft_armor_weapon.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.common.extensions.IForgeMenuType;

import net.minecraft.world.inventory.MenuType;

import minecraftarmorweapon.world.inventory.QuiverinventoryMenu;

import minecraft_armor_weapon.MinecraftArmorWeaponMod;

public class MinecraftArmorWeaponModMenus {
	public static final DeferredRegister<MenuType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.MENU_TYPES, MinecraftArmorWeaponMod.MODID);
	public static final RegistryObject<MenuType<QuiverinventoryMenu>> QUIVERINVENTORY = REGISTRY.register("quiverinventory", () -> IForgeMenuType.create(QuiverinventoryMenu::new));
}
