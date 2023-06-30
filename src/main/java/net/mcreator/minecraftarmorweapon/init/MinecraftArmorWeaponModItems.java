
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.minecraftarmorweapon.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.item.Item;

import net.mcreator.minecraftarmorweapon.item.NetheriteKatanaItem;
import net.mcreator.minecraftarmorweapon.item.FireballItem;
import net.mcreator.minecraftarmorweapon.MinecraftArmorWeaponMod;

public class MinecraftArmorWeaponModItems {
	public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, MinecraftArmorWeaponMod.MODID);
	public static final RegistryObject<Item> NETHERITE_KATANA = REGISTRY.register("netherite_katana", () -> new NetheriteKatanaItem());
	public static final RegistryObject<Item> FIREBALL = REGISTRY.register("fireball", () -> new FireballItem());
}
