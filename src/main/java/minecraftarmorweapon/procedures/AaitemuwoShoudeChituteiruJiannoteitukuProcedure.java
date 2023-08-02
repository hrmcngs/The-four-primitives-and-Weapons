package minecraftarmorweapon.procedures;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.BlockPos;

public class AaitemuwoShoudeChituteiruJiannoteitukuProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		world.setBlock(new BlockPos(x, y - 1, z), Blocks.BLACKSTONE.defaultBlockState(), 3);
		world.setBlock(new BlockPos(x - 1, y - 1, z), Blocks.STONE.defaultBlockState(), 3);
		world.setBlock(new BlockPos(x, y - 1, z), Blocks.BLACKSTONE.defaultBlockState(), 3);
		world.setBlock(new BlockPos(x + 1, y - 1, z), Blocks.STONE.defaultBlockState(), 3);
		world.setBlock(new BlockPos(x, y - 1, z + 1), Blocks.BLACKSTONE.defaultBlockState(), 3);
		world.setBlock(new BlockPos(x, y - 1, z - 1), Blocks.STONE.defaultBlockState(), 3);
		world.setBlock(new BlockPos(x - 1, y - 1, z - 1), Blocks.BLACKSTONE.defaultBlockState(), 3);
		world.setBlock(new BlockPos(x + 1, y - 1, z - 1), Blocks.STONE.defaultBlockState(), 3);
		world.setBlock(new BlockPos(x + 1, y - 1, z + 1), Blocks.BLACKSTONE.defaultBlockState(), 3);
	}
}
