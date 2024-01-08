package minecraftarmorweapon.procedures;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.BlockPos;

public class AaaYoukuritukusitatokiProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		world.setBlock(new BlockPos(x, y - 1, z), Blocks.END_STONE.defaultBlockState(), 3);
		world.setBlock(new BlockPos(x, y - 2, z), Blocks.END_STONE.defaultBlockState(), 3);
		world.setBlock(new BlockPos(x, y - 3, z), Blocks.END_STONE.defaultBlockState(), 3);
		world.setBlock(new BlockPos(x, y - 4, z), Blocks.END_STONE.defaultBlockState(), 3);
	}
}
