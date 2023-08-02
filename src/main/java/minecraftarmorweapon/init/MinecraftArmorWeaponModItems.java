
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package minecraftarmorweapon.init;

import org.checkerframework.checker.units.qual.A;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.BlockItem;

import minecraftarmorweapon.item.ThunderboltItem;
import minecraftarmorweapon.item.StoneKatanaItem;
import minecraftarmorweapon.item.PillagerArmorItem;
import minecraftarmorweapon.item.NetheriteKatanaItem;
import minecraftarmorweapon.item.ItemWeaponswordItem;
import minecraftarmorweapon.item.IllusionerArmorItem;
import minecraftarmorweapon.item.FireballItem;
import minecraftarmorweapon.item.CustomElytraTestItem;
import minecraftarmorweapon.item.BubbleshotItem;
import minecraftarmorweapon.item.AItem;

import minecraftarmorweapon.MinecraftArmorWeaponMod;

public class MinecraftArmorWeaponModItems {
	public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, MinecraftArmorWeaponMod.MODID);
	public static final RegistryObject<Item> BUBBLESHOT = REGISTRY.register("bubbleshot", () -> new BubbleshotItem());
	public static final RegistryObject<Item> FIREBALL = REGISTRY.register("fireball", () -> new FireballItem());
	public static final RegistryObject<Item> THUNDERBOLT = REGISTRY.register("thunderbolt", () -> new ThunderboltItem());
	public static final RegistryObject<Item> ITEM_WEAPONSWORD = REGISTRY.register("item_weaponsword", () -> new ItemWeaponswordItem());
	public static final RegistryObject<Item> NETHERITE_KATANA = REGISTRY.register("netherite_katana", () -> new NetheriteKatanaItem());
	public static final RegistryObject<Item> ROSE = block(MinecraftArmorWeaponModBlocks.ROSE, CreativeModeTab.TAB_DECORATIONS);
	public static final RegistryObject<Item> PILLAGER_ARMOR_HELMET = REGISTRY.register("pillager_armor_helmet", () -> new PillagerArmorItem.Helmet());
	public static final RegistryObject<Item> PILLAGER_ARMOR_CHESTPLATE = REGISTRY.register("pillager_armor_chestplate", () -> new PillagerArmorItem.Chestplate());
	public static final RegistryObject<Item> PILLAGER_ARMOR_LEGGINGS = REGISTRY.register("pillager_armor_leggings", () -> new PillagerArmorItem.Leggings());
	public static final RegistryObject<Item> PILLAGER_ARMOR_BOOTS = REGISTRY.register("pillager_armor_boots", () -> new PillagerArmorItem.Boots());
	public static final RegistryObject<Item> CUSTOM_ELYTRA_TEST_CHESTPLATE = REGISTRY.register("custom_elytra_test_chestplate", () -> new CustomElytraTestItem.Chestplate());
	public static final RegistryObject<Item> ILLUSIONER_ARMOR_HELMET = REGISTRY.register("illusioner_armor_helmet", () -> new IllusionerArmorItem.Helmet());
	public static final RegistryObject<Item> ILLUSIONER_ARMOR_CHESTPLATE = REGISTRY.register("illusioner_armor_chestplate", () -> new IllusionerArmorItem.Chestplate());
	public static final RegistryObject<Item> ILLUSIONER_ARMOR_LEGGINGS = REGISTRY.register("illusioner_armor_leggings", () -> new IllusionerArmorItem.Leggings());
	public static final RegistryObject<Item> ILLUSIONER_ARMOR_BOOTS = REGISTRY.register("illusioner_armor_boots", () -> new IllusionerArmorItem.Boots());
	public static final RegistryObject<Item> NETHERITE_KATANA_BLOCK = block(MinecraftArmorWeaponModBlocks.NETHERITE_KATANA_BLOCK, CreativeModeTab.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Item> STONE_KATANA_BLOCK = block(MinecraftArmorWeaponModBlocks.STONE_KATANA_BLOCK, CreativeModeTab.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Item> STONE_KATANA = REGISTRY.register("stone_katana", () -> new StoneKatanaItem());
	public static final RegistryObject<Item> A = REGISTRY.register("a", () -> new AItem());
	public static final RegistryObject<Item> STONE_KATANA_BLOCK_2 = block(MinecraftArmorWeaponModBlocks.STONE_KATANA_BLOCK_2, CreativeModeTab.TAB_BUILDING_BLOCKS);

	private static RegistryObject<Item> block(RegistryObject<Block> block, CreativeModeTab tab) {
		return REGISTRY.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties().tab(tab)));
	}
}
