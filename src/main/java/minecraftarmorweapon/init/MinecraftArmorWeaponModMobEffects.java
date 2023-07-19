
/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package minecraftarmorweapon.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.effect.MobEffect;

import minecraftarmorweapon.potion.TunderbolteffrctMobEffect;
import minecraftarmorweapon.potion.FireballeffectMobEffect;
import minecraftarmorweapon.potion.AtarutoMobEffect;
import minecraftarmorweapon.potion.Arrow1MobEffect;

import minecraftarmorweapon.MinecraftArmorWeaponMod;

public class MinecraftArmorWeaponModMobEffects {
	public static final DeferredRegister<MobEffect> REGISTRY = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, MinecraftArmorWeaponMod.MODID);
	public static final RegistryObject<MobEffect> FIREBALLEFFECT = REGISTRY.register("fireballeffect", () -> new FireballeffectMobEffect());
	public static final RegistryObject<MobEffect> ARROW_1 = REGISTRY.register("arrow_1", () -> new Arrow1MobEffect());
	public static final RegistryObject<MobEffect> TUNDERBOLTEFFRCT = REGISTRY.register("tunderbolteffrct", () -> new TunderbolteffrctMobEffect());
	public static final RegistryObject<MobEffect> ATARUTO = REGISTRY.register("ataruto", () -> new AtarutoMobEffect());
}
