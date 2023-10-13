
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package minecraftarmorweapon.init;

import org.checkerframework.checker.units.qual.A;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.common.ForgeSpawnEggItem;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.BlockItem;

import minecraftarmorweapon.item.WindStepItem;
import minecraftarmorweapon.item.WaterKatanaItem;
import minecraftarmorweapon.item.WarabitetouItem;
import minecraftarmorweapon.item.ThunderboltItem;
import minecraftarmorweapon.item.StrayOuterArmorItem;
import minecraftarmorweapon.item.StoneKatanaItem;
import minecraftarmorweapon.item.QuiverItemItem;
import minecraftarmorweapon.item.PillagerArmorItem;
import minecraftarmorweapon.item.NetheriteQuiverItemItem;
import minecraftarmorweapon.item.NetheriteKatanaItem;
import minecraftarmorweapon.item.MyTestIronKatanaItem;
import minecraftarmorweapon.item.LunaItem;
import minecraftarmorweapon.item.ItemWeaponswordItem;
import minecraftarmorweapon.item.IronKatanaItem;
import minecraftarmorweapon.item.IllusionerArmorItem;
import minecraftarmorweapon.item.HarvestMoon2023929Item;
import minecraftarmorweapon.item.HammerItem;
import minecraftarmorweapon.item.GomanorikenItem;
import minecraftarmorweapon.item.FireballItem;
import minecraftarmorweapon.item.EnderQuiverItemItem;
import minecraftarmorweapon.item.EnderPearlNetheriteQuiverItemItem;
import minecraftarmorweapon.item.BubbleshotItem;
import minecraftarmorweapon.item.AchromaticShieldItem;
import minecraftarmorweapon.item.AItem;
import minecraftarmorweapon.item.A2Item;

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
	public static final RegistryObject<Item> ILLUSIONER_ARMOR_HELMET = REGISTRY.register("illusioner_armor_helmet", () -> new IllusionerArmorItem.Helmet());
	public static final RegistryObject<Item> ILLUSIONER_ARMOR_CHESTPLATE = REGISTRY.register("illusioner_armor_chestplate", () -> new IllusionerArmorItem.Chestplate());
	public static final RegistryObject<Item> ILLUSIONER_ARMOR_LEGGINGS = REGISTRY.register("illusioner_armor_leggings", () -> new IllusionerArmorItem.Leggings());
	public static final RegistryObject<Item> ILLUSIONER_ARMOR_BOOTS = REGISTRY.register("illusioner_armor_boots", () -> new IllusionerArmorItem.Boots());
	public static final RegistryObject<Item> NETHERITE_KATANA_BLOCK = block(MinecraftArmorWeaponModBlocks.NETHERITE_KATANA_BLOCK, MinecraftArmorWeaponModTabs.TAB_YOPKEINAMONO);
	public static final RegistryObject<Item> STONE_KATANA_BLOCK = block(MinecraftArmorWeaponModBlocks.STONE_KATANA_BLOCK, MinecraftArmorWeaponModTabs.TAB_YOPKEINAMONO);
	public static final RegistryObject<Item> STONE_KATANA = REGISTRY.register("stone_katana", () -> new StoneKatanaItem());
	public static final RegistryObject<Item> A = REGISTRY.register("a", () -> new AItem());
	public static final RegistryObject<Item> STONE_KATANA_BLOCK_2 = block(MinecraftArmorWeaponModBlocks.STONE_KATANA_BLOCK_2, CreativeModeTab.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Item> HAMMER = REGISTRY.register("hammer", () -> new HammerItem());
	public static final RegistryObject<Item> IRON_KATANA = REGISTRY.register("iron_katana", () -> new IronKatanaItem());
	public static final RegistryObject<Item> CROSS = block(MinecraftArmorWeaponModBlocks.CROSS, MinecraftArmorWeaponModTabs.TAB_YOPKEINAMONO);
	public static final RegistryObject<Item> WATER_KATANA = REGISTRY.register("water_katana", () -> new WaterKatanaItem());
	public static final RegistryObject<Item> MY_TEST_IRON_KATANA = REGISTRY.register("my_test_iron_katana", () -> new MyTestIronKatanaItem());
	public static final RegistryObject<Item> LUNA = REGISTRY.register("luna", () -> new LunaItem());
	public static final RegistryObject<Item> QUIVER_ITEM = REGISTRY.register("quiver_item", () -> new QuiverItemItem());
	public static final RegistryObject<Item> SKELTON_MOB_SPAWN_EGG = REGISTRY.register("skelton_mob_spawn_egg", () -> new ForgeSpawnEggItem(MinecraftArmorWeaponModEntities.SKELTON_MOB, -1, -1, new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
	public static final RegistryObject<Item> GOMANORIKEN = REGISTRY.register("gomanoriken", () -> new GomanorikenItem());
	public static final RegistryObject<Item> ACHROMATIC_SHIELD = REGISTRY.register("achromatic_shield", () -> new AchromaticShieldItem());
	public static final RegistryObject<Item> GOMANORIKEN_BLOCK = block(MinecraftArmorWeaponModBlocks.GOMANORIKEN_BLOCK, MinecraftArmorWeaponModTabs.TAB_YOPKEINAMONO);
	public static final RegistryObject<Item> HARVEST_MOON_2023929 = REGISTRY.register("harvest_moon_2023929", () -> new HarvestMoon2023929Item());
	public static final RegistryObject<Item> ENDER_QUIVER_ITEM = REGISTRY.register("ender_quiver_item", () -> new EnderQuiverItemItem());
	public static final RegistryObject<Item> NETHERITE_QUIVER_ITEM = REGISTRY.register("netherite_quiver_item", () -> new NetheriteQuiverItemItem());
	public static final RegistryObject<Item> ENDER_PEARL_NETHERITE_QUIVER_ITEM = REGISTRY.register("ender_pearl_netherite_quiver_item", () -> new EnderPearlNetheriteQuiverItemItem());
	public static final RegistryObject<Item> WARABITETOU = REGISTRY.register("warabitetou", () -> new WarabitetouItem());
	public static final RegistryObject<Item> A_2 = REGISTRY.register("a_2", () -> new A2Item());
	public static final RegistryObject<Item> OTIRUYO_SPAWN_EGG = REGISTRY.register("otiruyo_spawn_egg", () -> new ForgeSpawnEggItem(MinecraftArmorWeaponModEntities.OTIRUYO, -1, -1, new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
	public static final RegistryObject<Item> WIND_STEP = REGISTRY.register("wind_step", () -> new WindStepItem());
	public static final RegistryObject<Item> STRAY_OUTER_ARMOR_HELMET = REGISTRY.register("stray_outer_armor_helmet", () -> new StrayOuterArmorItem.Helmet());
	public static final RegistryObject<Item> STRAY_OUTER_ARMOR_CHESTPLATE = REGISTRY.register("stray_outer_armor_chestplate", () -> new StrayOuterArmorItem.Chestplate());
	public static final RegistryObject<Item> STRAY_OUTER_ARMOR_LEGGINGS = REGISTRY.register("stray_outer_armor_leggings", () -> new StrayOuterArmorItem.Leggings());

	private static RegistryObject<Item> block(RegistryObject<Block> block, CreativeModeTab tab) {
		return REGISTRY.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties().tab(tab)));
	}
}
