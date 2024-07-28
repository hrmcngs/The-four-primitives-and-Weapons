
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package minecraftarmorweapon.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.effect.MobEffectInstance;

import minecraftarmorweapon.MinecraftArmorWeaponMod;

public class MinecraftArmorWeaponModPotions {
	public static final DeferredRegister<Potion> REGISTRY = DeferredRegister.create(ForgeRegistries.POTIONS, MinecraftArmorWeaponMod.MODID);
	public static final RegistryObject<Potion> HARD_ENTITY_EFFECT_ITEM = REGISTRY.register("hard_entity_effect_item", () -> new Potion(new MobEffectInstance(MinecraftArmorWeaponModMobEffects.HARDENTITY.get(), 72000, 3, false, true)));
}
