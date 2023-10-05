
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package minecraft_armor_weapon.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.level.block.Block;

import minecraftarmorweapon.block.StoneKatanaBlockBlock;
import minecraftarmorweapon.block.StoneKatanaBlock2Block;
import minecraftarmorweapon.block.RoseBlock;
import minecraftarmorweapon.block.NetheriteKatanaBlockBlock;
import minecraftarmorweapon.block.GomanorikenBlockBlock;
import minecraftarmorweapon.block.CrossBlock;

import minecraft_armor_weapon.MinecraftArmorWeaponMod;

public class MinecraftArmorWeaponModBlocks {
	public static final DeferredRegister<Block> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, MinecraftArmorWeaponMod.MODID);
	public static final RegistryObject<Block> ROSE = REGISTRY.register("rose", () -> new RoseBlock());
	public static final RegistryObject<Block> NETHERITE_KATANA_BLOCK = REGISTRY.register("netherite_katana_block", () -> new NetheriteKatanaBlockBlock());
	public static final RegistryObject<Block> STONE_KATANA_BLOCK = REGISTRY.register("stone_katana_block", () -> new StoneKatanaBlockBlock());
	public static final RegistryObject<Block> STONE_KATANA_BLOCK_2 = REGISTRY.register("stone_katana_block_2", () -> new StoneKatanaBlock2Block());
	public static final RegistryObject<Block> CROSS = REGISTRY.register("cross", () -> new CrossBlock());
	public static final RegistryObject<Block> GOMANORIKEN_BLOCK = REGISTRY.register("gomanoriken_block", () -> new GomanorikenBlockBlock());
}
