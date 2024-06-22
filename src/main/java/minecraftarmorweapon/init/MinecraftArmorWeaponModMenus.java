
/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package minecraftarmorweapon.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.common.extensions.IForgeMenuType;

import net.minecraft.world.inventory.MenuType;

import minecraftarmorweapon.world.inventory.RpgBookGuiMenu;
import minecraftarmorweapon.world.inventory.RpgBookGui2Menu;

import minecraftarmorweapon.MinecraftArmorWeaponMod;

public class MinecraftArmorWeaponModMenus {
	public static final DeferredRegister<MenuType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.MENU_TYPES, MinecraftArmorWeaponMod.MODID);
	public static final RegistryObject<MenuType<RpgBookGuiMenu>> RPG_BOOK_GUI = REGISTRY.register("rpg_book_gui", () -> IForgeMenuType.create(RpgBookGuiMenu::new));
	public static final RegistryObject<MenuType<RpgBookGui2Menu>> RPG_BOOK_GUI_2 = REGISTRY.register("rpg_book_gui_2", () -> IForgeMenuType.create(RpgBookGui2Menu::new));
}
