
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package minecraftarmorweapon.init;

import net.minecraftforge.fml.common.Mod;

import net.minecraft.world.level.GameRules;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class MinecraftArmorWeaponModGameRules {
	public static final GameRules.Key<GameRules.BooleanValue> RPG_BOOK_GIVE = GameRules.register("rpgBookGive", GameRules.Category.PLAYER, GameRules.BooleanValue.create(true));
}