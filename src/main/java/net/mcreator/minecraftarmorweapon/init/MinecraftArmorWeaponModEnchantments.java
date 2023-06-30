
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.minecraftarmorweapon.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.item.enchantment.Enchantment;

import net.mcreator.minecraftarmorweapon.enchantment.KillEnchantment;
import net.mcreator.minecraftarmorweapon.enchantment.DemonizedEnchantment;
import net.mcreator.minecraftarmorweapon.MinecraftArmorWeaponMod;

public class MinecraftArmorWeaponModEnchantments {
	public static final DeferredRegister<Enchantment> REGISTRY = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, MinecraftArmorWeaponMod.MODID);
	public static final RegistryObject<Enchantment> DEMONIZED = REGISTRY.register("demonized", () -> new DemonizedEnchantment());
	public static final RegistryObject<Enchantment> KILL = REGISTRY.register("kill", () -> new KillEnchantment());
}
