
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package minecraftarmorweapon.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.item.Item;

import minecraftarmorweapon.item.WaterIronIngotItem;
import minecraftarmorweapon.item.ThunderboltItem;
import minecraftarmorweapon.item.ThunderIronIngotItem;
import minecraftarmorweapon.item.NetheritekatanaItem;
import minecraftarmorweapon.item.FireballItem;
import minecraftarmorweapon.item.FireIronIngotItem;
import minecraftarmorweapon.item.CompressedWaterItem;
import minecraftarmorweapon.item.CompressedMagmaItem;
import minecraftarmorweapon.item.CompressedElectricityItem;

import minecraftarmorweapon.MinecraftArmorWeaponMod;

public class MinecraftArmorWeaponModItems {
	public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, MinecraftArmorWeaponMod.MODID);
	public static final RegistryObject<Item> COMPRESSED_MAGMA = REGISTRY.register("compressed_magma", () -> new CompressedMagmaItem());
	public static final RegistryObject<Item> NETHERITEKATANA = REGISTRY.register("netheritekatana", () -> new NetheritekatanaItem());
	public static final RegistryObject<Item> COMPRESSED_WATER = REGISTRY.register("compressed_water", () -> new CompressedWaterItem());
	public static final RegistryObject<Item> FIRE_IRON_INGOT = REGISTRY.register("fire_iron_ingot", () -> new FireIronIngotItem());
	public static final RegistryObject<Item> WATER_IRON_INGOT = REGISTRY.register("water_iron_ingot", () -> new WaterIronIngotItem());
	public static final RegistryObject<Item> THUNDER_IRON_INGOT = REGISTRY.register("thunder_iron_ingot", () -> new ThunderIronIngotItem());
	public static final RegistryObject<Item> COMPRESSED_ELECTRICITY = REGISTRY.register("compressed_electricity", () -> new CompressedElectricityItem());
	public static final RegistryObject<Item> FIREBALL = REGISTRY.register("fireball", () -> new FireballItem());
	public static final RegistryObject<Item> THUNDERBOLT = REGISTRY.register("thunderbolt", () -> new ThunderboltItem());
}
