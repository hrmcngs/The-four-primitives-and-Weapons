
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
import minecraftarmorweapon.item.SwordOfNightItem;
import minecraftarmorweapon.item.StrayouterarmorhatItem;
import minecraftarmorweapon.item.StrayOuterArmorItem;
import minecraftarmorweapon.item.StoneKatanaItem;
import minecraftarmorweapon.item.RiversOfBloodItem;
import minecraftarmorweapon.item.ResetMaxItem;
import minecraftarmorweapon.item.Reset1Item;
import minecraftarmorweapon.item.QuiverItemItem;
import minecraftarmorweapon.item.PillagerArmorItem;
import minecraftarmorweapon.item.NetheriteQuiverItemItem;
import minecraftarmorweapon.item.NetheriteKatanaItem;
import minecraftarmorweapon.item.MyTestIronKatanaItem;
import minecraftarmorweapon.item.MagischesFeenKatanaItem;
import minecraftarmorweapon.item.MagicalKatanaItem;
import minecraftarmorweapon.item.MagicWandItem;
import minecraftarmorweapon.item.MagicMcrystalItem;
import minecraftarmorweapon.item.LunaItem;
import minecraftarmorweapon.item.KatanaTobuItem;
import minecraftarmorweapon.item.ItemWeaponswordItem;
import minecraftarmorweapon.item.IronKatanaItem;
import minecraftarmorweapon.item.IllusionerArmorItem;
import minecraftarmorweapon.item.HarvestMoon2023929Item;
import minecraftarmorweapon.item.HammerItem;
import minecraftarmorweapon.item.Halloween20231031SickleItem;
import minecraftarmorweapon.item.GomanorikenItem;
import minecraftarmorweapon.item.FireballItem;
import minecraftarmorweapon.item.EnderQuiverItemItem;
import minecraftarmorweapon.item.EnderPearlNetheriteQuiverItemItem;
import minecraftarmorweapon.item.DragonRedArmorItem;
import minecraftarmorweapon.item.DragonGreenArmorItem;
import minecraftarmorweapon.item.DragonBlueArmorItem;
import minecraftarmorweapon.item.DragonBlackArmorItem;
import minecraftarmorweapon.item.DragonArmorItem;
import minecraftarmorweapon.item.BubbleshotItem;
import minecraftarmorweapon.item.BloodBottleItem;
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
	public static final RegistryObject<Item> MAGIC_WAND = REGISTRY.register("magic_wand", () -> new MagicWandItem());
	public static final RegistryObject<Item> STRAYOUTERARMORHAT_HELMET = REGISTRY.register("strayouterarmorhat_helmet", () -> new StrayouterarmorhatItem.Helmet());
	public static final RegistryObject<Item> MAGIC_MCRYSTAL = REGISTRY.register("magic_mcrystal", () -> new MagicMcrystalItem());
	public static final RegistryObject<Item> MAGICAL_KATANA = REGISTRY.register("magical_katana", () -> new MagicalKatanaItem());
	public static final RegistryObject<Item> STONE_BRICKS_TRAP_DOOR = block(MinecraftArmorWeaponModBlocks.STONE_BRICKS_TRAP_DOOR, MinecraftArmorWeaponModTabs.TAB_YOPKEINAMONO);
	public static final RegistryObject<Item> ROSE_FLOWER_POT = block(MinecraftArmorWeaponModBlocks.ROSE_FLOWER_POT, MinecraftArmorWeaponModTabs.TAB_MAGIC_BOOKS);
	public static final RegistryObject<Item> HRMCNGS_SPAWN_EGG = REGISTRY.register("hrmcngs_spawn_egg", () -> new ForgeSpawnEggItem(MinecraftArmorWeaponModEntities.HRMCNGS, -406791, -1, new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
	public static final RegistryObject<Item> RUAMI_284_SPAWN_EGG = REGISTRY.register("ruami_284_spawn_egg",
			() -> new ForgeSpawnEggItem(MinecraftArmorWeaponModEntities.RUAMI_284, -14277082, -4210753, new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
	public static final RegistryObject<Item> HALLOWEEN_2023_10_31_SICKLE = REGISTRY.register("halloween_2023_10_31_sickle", () -> new Halloween20231031SickleItem());
	public static final RegistryObject<Item> KILLOTIRU_SPAWN_EGG = REGISTRY.register("killotiru_spawn_egg", () -> new ForgeSpawnEggItem(MinecraftArmorWeaponModEntities.KILLOTIRU, -1, -1, new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
	public static final RegistryObject<Item> KATANA_TOBU = REGISTRY.register("katana_tobu", () -> new KatanaTobuItem());
	public static final RegistryObject<Item> DRAGON_ARMOR_HELMET = REGISTRY.register("dragon_armor_helmet", () -> new DragonArmorItem.Helmet());
	public static final RegistryObject<Item> DRAGON_ARMOR_CHESTPLATE = REGISTRY.register("dragon_armor_chestplate", () -> new DragonArmorItem.Chestplate());
	public static final RegistryObject<Item> DRAGON_ARMOR_LEGGINGS = REGISTRY.register("dragon_armor_leggings", () -> new DragonArmorItem.Leggings());
	public static final RegistryObject<Item> DRAGON_ARMOR_BOOTS = REGISTRY.register("dragon_armor_boots", () -> new DragonArmorItem.Boots());
	public static final RegistryObject<Item> DRAGON_GREEN_ARMOR_HELMET = REGISTRY.register("dragon_green_armor_helmet", () -> new DragonGreenArmorItem.Helmet());
	public static final RegistryObject<Item> DRAGON_GREEN_ARMOR_CHESTPLATE = REGISTRY.register("dragon_green_armor_chestplate", () -> new DragonGreenArmorItem.Chestplate());
	public static final RegistryObject<Item> DRAGON_GREEN_ARMOR_LEGGINGS = REGISTRY.register("dragon_green_armor_leggings", () -> new DragonGreenArmorItem.Leggings());
	public static final RegistryObject<Item> DRAGON_GREEN_ARMOR_BOOTS = REGISTRY.register("dragon_green_armor_boots", () -> new DragonGreenArmorItem.Boots());
	public static final RegistryObject<Item> DRAGON_BLACK_ARMOR_HELMET = REGISTRY.register("dragon_black_armor_helmet", () -> new DragonBlackArmorItem.Helmet());
	public static final RegistryObject<Item> DRAGON_BLACK_ARMOR_CHESTPLATE = REGISTRY.register("dragon_black_armor_chestplate", () -> new DragonBlackArmorItem.Chestplate());
	public static final RegistryObject<Item> DRAGON_BLACK_ARMOR_LEGGINGS = REGISTRY.register("dragon_black_armor_leggings", () -> new DragonBlackArmorItem.Leggings());
	public static final RegistryObject<Item> DRAGON_BLACK_ARMOR_BOOTS = REGISTRY.register("dragon_black_armor_boots", () -> new DragonBlackArmorItem.Boots());
	public static final RegistryObject<Item> DRAGON_RED_ARMOR_HELMET = REGISTRY.register("dragon_red_armor_helmet", () -> new DragonRedArmorItem.Helmet());
	public static final RegistryObject<Item> DRAGON_RED_ARMOR_CHESTPLATE = REGISTRY.register("dragon_red_armor_chestplate", () -> new DragonRedArmorItem.Chestplate());
	public static final RegistryObject<Item> DRAGON_RED_ARMOR_LEGGINGS = REGISTRY.register("dragon_red_armor_leggings", () -> new DragonRedArmorItem.Leggings());
	public static final RegistryObject<Item> DRAGON_RED_ARMOR_BOOTS = REGISTRY.register("dragon_red_armor_boots", () -> new DragonRedArmorItem.Boots());
	public static final RegistryObject<Item> DRAGON_BLUE_ARMOR_HELMET = REGISTRY.register("dragon_blue_armor_helmet", () -> new DragonBlueArmorItem.Helmet());
	public static final RegistryObject<Item> DRAGON_BLUE_ARMOR_CHESTPLATE = REGISTRY.register("dragon_blue_armor_chestplate", () -> new DragonBlueArmorItem.Chestplate());
	public static final RegistryObject<Item> DRAGON_BLUE_ARMOR_LEGGINGS = REGISTRY.register("dragon_blue_armor_leggings", () -> new DragonBlueArmorItem.Leggings());
	public static final RegistryObject<Item> DRAGON_BLUE_ARMOR_BOOTS = REGISTRY.register("dragon_blue_armor_boots", () -> new DragonBlueArmorItem.Boots());
	public static final RegistryObject<Item> MAGISCHES_FEEN_KATANA = REGISTRY.register("magisches_feen_katana", () -> new MagischesFeenKatanaItem());
	public static final RegistryObject<Item> BLOOD_BOTTLE = REGISTRY.register("blood_bottle", () -> new BloodBottleItem());
	public static final RegistryObject<Item> RIVERS_OF_BLOOD = REGISTRY.register("rivers_of_blood", () -> new RiversOfBloodItem());
	public static final RegistryObject<Item> RESET_MAX = REGISTRY.register("reset_max", () -> new ResetMaxItem());
	public static final RegistryObject<Item> RESET_1 = REGISTRY.register("reset_1", () -> new Reset1Item());
	public static final RegistryObject<Item> SWORD_OF_NIGHT = REGISTRY.register("sword_of_night", () -> new SwordOfNightItem());

	private static RegistryObject<Item> block(RegistryObject<Block> block, CreativeModeTab tab) {
		return REGISTRY.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties().tab(tab)));
	}
}
