
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package minecraftarmorweapon.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.level.block.Block;

import minecraftarmorweapon.block.StoneKatanaBlockBlock;
import minecraftarmorweapon.block.StoneKatanaBlock1Block;
import minecraftarmorweapon.block.StoneBricksTrapDoorBlock;
import minecraftarmorweapon.block.RoseFlowerPotBlock;
import minecraftarmorweapon.block.RoseBlock;
import minecraftarmorweapon.block.NetheriteKatanaBlockBlock;
import minecraftarmorweapon.block.KurikarakenBlockBlock;
import minecraftarmorweapon.block.CrossBlock;

import minecraftarmorweapon.MinecraftArmorWeaponMod;

public class MinecraftArmorWeaponModBlocks {
	public static final DeferredRegister<Block> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, MinecraftArmorWeaponMod.MODID);
	public static final RegistryObject<Block> ROSE = REGISTRY.register("rose", () -> new RoseBlock());
	public static final RegistryObject<Block> NETHERITE_KATANA_BLOCK = REGISTRY.register("netherite_katana_block", () -> new NetheriteKatanaBlockBlock());
	public static final RegistryObject<Block> CROSS = REGISTRY.register("cross", () -> new CrossBlock());
	public static final RegistryObject<Block> STONE_BRICKS_TRAP_DOOR = REGISTRY.register("stone_bricks_trap_door", () -> new StoneBricksTrapDoorBlock());
	public static final RegistryObject<Block> ROSE_FLOWER_POT = REGISTRY.register("rose_flower_pot", () -> new RoseFlowerPotBlock());
	public static final RegistryObject<Block> KURIKARAKEN_BLOCK = REGISTRY.register("kurikaraken_block", () -> new KurikarakenBlockBlock());
	public static final RegistryObject<Block> STONE_KATANA_BLOCK = REGISTRY.register("stone_katana_block", () -> new StoneKatanaBlockBlock());
	public static final RegistryObject<Block> STONE_KATANA_BLOCK_1 = REGISTRY.register("stone_katana_block_1", () -> new StoneKatanaBlock1Block());
}
