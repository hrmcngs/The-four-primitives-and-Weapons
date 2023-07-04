
/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.minecraftarmorweapon.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.effect.MobEffect;

import net.mcreator.minecraftarmorweapon.potion.Arrow1MobEffect;
import net.mcreator.minecraftarmorweapon.MinecraftArmorWeaponMod;

public class MinecraftArmorWeaponModMobEffects {
	public static final DeferredRegister<MobEffect> REGISTRY = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, MinecraftArmorWeaponMod.MODID);
	public static final RegistryObject<MobEffect> ARROW_1 = REGISTRY.register("arrow_1", () -> new Arrow1MobEffect());
}
