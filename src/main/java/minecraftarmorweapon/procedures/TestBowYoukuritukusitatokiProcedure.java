package minecraftarmorweapon.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import minecraftarmorweapon.MinecraftArmorWeaponMod;

public class TestBowYoukuritukusitatokiProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		entity.getPersistentData().putBoolean("minecraft_armor_weapon:armor_stand_tobasu_kill_off", true);
		{
			Entity _ent = entity;
			if (!_ent.level.isClientSide() && _ent.getServer() != null) {
				_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level instanceof ServerLevel ? (ServerLevel) _ent.level : null, 4,
						_ent.getName().getString(), _ent.getDisplayName(), _ent.level.getServer(), _ent), "function minecraft_armor_weapon:tyokusen_arrow_start");
			}
		}
		MinecraftArmorWeaponMod.queueServerWork(200, () -> {
			entity.getPersistentData().putBoolean("minecraft_armor_weapon:armor_stand_tobasu_kill_off", false);
		});
	}
}
