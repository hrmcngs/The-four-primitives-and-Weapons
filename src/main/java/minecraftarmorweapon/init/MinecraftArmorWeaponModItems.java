
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package minecraftarmorweapon.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.item.Item;

import minecraftarmorweapon.item.ThunderboltItem;
import minecraftarmorweapon.item.FireballItem;
import minecraftarmorweapon.item.BubbleshotItem;

import minecraftarmorweapon.MinecraftArmorWeaponMod;

public class MinecraftArmorWeaponModItems {
	public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, MinecraftArmorWeaponMod.MODID);
	public static final RegistryObject<Item> BUBBLESHOT = REGISTRY.register("bubbleshot", () -> new BubbleshotItem());
	public static final RegistryObject<Item> FIREBALL = REGISTRY.register("fireball", () -> new FireballItem());
	public static final RegistryObject<Item> THUNDERBOLT = REGISTRY.register("thunderbolt", () -> new ThunderboltItem());
}
