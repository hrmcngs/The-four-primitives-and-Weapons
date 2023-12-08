package minecraftarmorweapon.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.core.BlockPos;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

public class NagiharaiehuekutogaYouXiaoShinoteitukuProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		entity.getPersistentData().putDouble("Xpos", (entity.getPersistentData().getDouble("X") + Math.sin(Math.toRadians(entity.getPersistentData().getDouble("yaw") + 180)) * entity.getPersistentData().getDouble("distance")));
		entity.getPersistentData().putDouble("Zpos", (entity.getPersistentData().getDouble("Z") + Math.cos(Math.toRadians(entity.getPersistentData().getDouble("yaw"))) * entity.getPersistentData().getDouble("distance")));
		for (int index0 = 0; index0 < 1; index0++) {
			if (world.getBlockState(new BlockPos(entity.getPersistentData().getDouble("Xpos"), entity.getPersistentData().getDouble("Ypos"), entity.getPersistentData().getDouble("Zpos"))).canOcclude()) {
				entity.getPersistentData().putDouble("Ypos", (entity.getPersistentData().getDouble("Ypos")));
			} else {
				break;
			}
		}
		for (int index1 = 0; index1 < 1; index1++) {
			if (world.getBlockState(new BlockPos(entity.getPersistentData().getDouble("Xpos"), entity.getPersistentData().getDouble("Ypos"), entity.getPersistentData().getDouble("Zpos"))).canOcclude()) {
				entity.getPersistentData().putDouble("Ypos", (entity.getPersistentData().getDouble("Ypos")));
				break;
			}
			entity.getPersistentData().putDouble("Ypos", (entity.getPersistentData().getDouble("Ypos")));
		}
		for (int index2 = 0; index2 < 2; index2++) {
			if (world instanceof ServerLevel _level)
				_level.getServer().getCommands()
						.performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3((entity.getPersistentData().getDouble("Xpos")), (entity.getPersistentData().getDouble("Ypos")), (entity.getPersistentData().getDouble("Zpos"))),
								Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(), "playsound minecraft:entity.drowned.shoot voice @p ^ ^ ^ 1 1.3");
			if (world instanceof ServerLevel _level)
				_level.getServer().getCommands()
						.performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3((entity.getPersistentData().getDouble("Xpos")), (entity.getPersistentData().getDouble("Ypos")), (entity.getPersistentData().getDouble("Zpos"))),
								Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(), "particle minecraft:sweep_attack ^ ^1 ^ 2 0.5 2 3 30 force @p");
		}
		if (world instanceof ServerLevel _level)
			_level.getServer().getCommands()
					.performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3((entity.getPersistentData().getDouble("Xpos")), (entity.getPersistentData().getDouble("Ypos")), (entity.getPersistentData().getDouble("Zpos"))),
							Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(), "particle minecraft:item netherite_ingot ^ ^1 ^ 3 3 3 .0 20");
		if (world instanceof ServerLevel _level)
			_level.getServer().getCommands()
					.performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3((entity.getPersistentData().getDouble("Xpos")), (entity.getPersistentData().getDouble("Ypos")), (entity.getPersistentData().getDouble("Zpos"))),
							Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(), "particle minecraft:item iron_ingot ^ ^1 ^ 3 3 3 .0 20 force @p");
	}
}
