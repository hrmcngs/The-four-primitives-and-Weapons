
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.minecraftarmorweapon.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.item.Item;

import net.mcreator.minecraftarmorweapon.item.WaterIronIngotItem;
import net.mcreator.minecraftarmorweapon.item.NetheriteKatanaItem;
import net.mcreator.minecraftarmorweapon.item.FireballItem;
import net.mcreator.minecraftarmorweapon.item.FireIronIngotItem;
import net.mcreator.minecraftarmorweapon.item.CompressedWaterItem;
import net.mcreator.minecraftarmorweapon.item.BubbleshotItem;
import net.mcreator.minecraftarmorweapon.MinecraftArmorWeaponMod;

public class MinecraftArmorWeaponModItems {
	public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, MinecraftArmorWeaponMod.MODID);
	public static final RegistryObject<Item> NETHERITE_KATANA = REGISTRY.register("netherite_katana", () -> new NetheriteKatanaItem());
	public static final RegistryObject<Item> FIREBALL = REGISTRY.register("fireball", () -> new FireballItem());
	public static final RegistryObject<Item> BUBBLESHOT = REGISTRY.register("bubbleshot", () -> new BubbleshotItem());
	public static final RegistryObject<Item> WATER_IRON_INGOT = REGISTRY.register("water_iron_ingot", () -> new WaterIronIngotItem());
	public static final RegistryObject<Item> FIRE_IRON_INGOT = REGISTRY.register("fire_iron_ingot", () -> new FireIronIngotItem());
	public static final RegistryObject<Item> COMPRESSED_WATER = REGISTRY.register("compressed_water", () -> new CompressedWaterItem());
}
