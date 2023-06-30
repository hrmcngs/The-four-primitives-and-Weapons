
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.minecraftarmorweapon.init;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.CreativeModeTab;

public class MinecraftArmorWeaponModTabs {
	public static CreativeModeTab TAB_MAGIC_BOOK;
	public static CreativeModeTab TAB_BUKI;

	public static void load() {
		TAB_MAGIC_BOOK = new CreativeModeTab("tabmagic_book") {
			@Override
			public ItemStack makeIcon() {
				return new ItemStack(Items.WRITABLE_BOOK);
			}

			@Override
			public boolean hasSearchBar() {
				return false;
			}
		};
		TAB_BUKI = new CreativeModeTab("tabbuki") {
			@Override
			public ItemStack makeIcon() {
				return new ItemStack(Blocks.CRAFTING_TABLE);
			}

			@Override
			public boolean hasSearchBar() {
				return false;
			}
		};
	}
}
