
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package minecraftarmorweapon.init;

import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.CreativeModeTab;

public class MinecraftArmorWeaponModTabs {
	public static CreativeModeTab TAB_WEAPON;
	public static CreativeModeTab TAB_MAGIC_BOOKS;
	public static CreativeModeTab TAB_ARMOR;
	public static CreativeModeTab TAB_YOPKEINAMONO;
	public static CreativeModeTab TAB_EVENT;
	public static CreativeModeTab TAB_DRAGON_ARMOR_TAB;
	public static CreativeModeTab TAB_NIGU;
	public static CreativeModeTab TAB_CHUZUME_TAB;

	public static void load() {
		TAB_WEAPON = new CreativeModeTab("tabweapon") {
			@Override
			public ItemStack makeIcon() {
				return new ItemStack(MinecraftArmorWeaponModItems.IRON_KATANA.get());
			}

			@Override
			public boolean hasSearchBar() {
				return false;
			}
		};
		TAB_MAGIC_BOOKS = new CreativeModeTab("tabmagic_books") {
			@Override
			public ItemStack makeIcon() {
				return new ItemStack(Items.WRITABLE_BOOK);
			}

			@Override
			public boolean hasSearchBar() {
				return false;
			}
		};
		TAB_ARMOR = new CreativeModeTab("tabarmor") {
			@Override
			public ItemStack makeIcon() {
				return new ItemStack(Items.TURTLE_HELMET);
			}

			@Override
			public boolean hasSearchBar() {
				return false;
			}
		};
		TAB_YOPKEINAMONO = new CreativeModeTab("tabyopkeinamono") {
			@Override
			public ItemStack makeIcon() {
				return new ItemStack(MinecraftArmorWeaponModBlocks.CROSS.get());
			}

			@Override
			public boolean hasSearchBar() {
				return false;
			}
		};
		TAB_EVENT = new CreativeModeTab("tabevent") {
			@Override
			public ItemStack makeIcon() {
				return new ItemStack(MinecraftArmorWeaponModItems.HARVEST_MOON_2023929.get());
			}

			@Override
			public boolean hasSearchBar() {
				return false;
			}
		};
		TAB_DRAGON_ARMOR_TAB = new CreativeModeTab("tabdragon_armor_tab") {
			@Override
			public ItemStack makeIcon() {
				return new ItemStack(MinecraftArmorWeaponModItems.DRAGON_ARMOR_HELMET.get());
			}

			@Override
			public boolean hasSearchBar() {
				return false;
			}
		};
		TAB_NIGU = new CreativeModeTab("tabnigu") {
			@Override
			public ItemStack makeIcon() {
				return new ItemStack(MinecraftArmorWeaponModItems.KATANA_NIGU_HUMERUS.get());
			}

			@Override
			public boolean hasSearchBar() {
				return false;
			}
		};
		TAB_CHUZUME_TAB = new CreativeModeTab("tabchuzume_tab") {
			@Override
			public ItemStack makeIcon() {
				return new ItemStack(MinecraftArmorWeaponModItems.SWORD_OF_NIGHT.get());
			}

			@Override
			public boolean hasSearchBar() {
				return false;
			}
		};
	}
}
