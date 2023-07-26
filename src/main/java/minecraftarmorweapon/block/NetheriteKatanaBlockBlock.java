
package minecraftarmorweapon.block;

import org.checkerframework.checker.units.qual.s;

import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;

import minecraftarmorweapon.init.MinecraftArmorWeaponModItems;

import java.util.List;
import java.util.Collections;

public class NetheriteKatanaBlockBlock extends Block {
	public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.AXIS;

	public NetheriteKatanaBlockBlock() {
		super(BlockBehaviour.Properties.of(Material.METAL).sound(SoundType.METAL).strength(0f, 10f).lightLevel(s -> 5).noOcclusion().isRedstoneConductor((bs, br, bp) -> false));
		this.registerDefaultState(this.stateDefinition.any().setValue(AXIS, Direction.Axis.Y));
	}

	@Override
	public float[] getBeaconColorMultiplier(BlockState state, LevelReader world, BlockPos pos, BlockPos beaconPos) {
		return new float[]{0f, 0f, 0f};
	}

	@Override
	public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
		return true;
	}

	@Override
	public int getLightBlock(BlockState state, BlockGetter worldIn, BlockPos pos) {
		return 0;
	}

	@Override
	public VoxelShape getVisualShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return Shapes.empty();
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return switch (state.getValue(AXIS)) {
			case X -> Shapes.or(box(10.75, -1.5, 7.5, 11.75, -0.5, 8.5), box(6.75, -0.5, 7.5, 12.75, 0.5, 8.5), box(5.75, -1.5, 7.5, 9.75, -0.5, 8.5), box(5.75, -2.5, 7.5, 7.75, -1.5, 8.5), box(9.75, 0.5, 7.5, 11.75, 1.5, 8.5),
					box(7.75, 0.5, 7.5, 9.75, 1.5, 8.5), box(6.75, 1.5, 7.5, 12.75, 2.5, 8.5), box(11.75, 2.5, 7.5, 12.75, 5.5, 8.5), box(12.75, 3.5, 7.5, 13.75, 5.5, 8.5), box(10.75, 2.5, 7.5, 11.75, 4.5, 8.5), box(5.75, 2.5, 7.5, 10.75, 3.5, 8.5),
					box(4.75, 3.5, 7.5, 9.75, 4.5, 8.5), box(3.75, 4.5, 7.5, 8.75, 5.5, 8.5), box(2.75, 5.5, 7.5, 7.75, 6.5, 8.5), box(1.75, 6.5, 7.5, 6.75, 7.5, 8.5), box(0.75, 7.5, 7.5, 5.75, 8.5, 8.5), box(-0.25, 8.5, 7.5, 4.75, 9.5, 8.5),
					box(-1.25, 9.5, 7.5, 3.75, 10.5, 8.5), box(-1.25, 10.5, 7.5, 2.75, 11.5, 8.5), box(11.75, -2.5, 7.5, 12.75, -1.5, 8.5), box(11.75, -1.5, 7.5, 13.75, -0.5, 8.5), box(12.75, -4.5, 7.5, 15.75, -1.5, 8.5));
			case Y -> Shapes.or(box(7.5, 10.75, -1.5, 8.5, 11.75, -0.5), box(7.5, 6.75, -0.5, 8.5, 12.75, 0.5), box(7.5, 5.75, -1.5, 8.5, 9.75, -0.5), box(7.5, 5.75, -2.5, 8.5, 7.75, -1.5), box(7.5, 9.75, 0.5, 8.5, 11.75, 1.5),
					box(7.5, 7.75, 0.5, 8.5, 9.75, 1.5), box(7.5, 6.75, 1.5, 8.5, 12.75, 2.5), box(7.5, 11.75, 2.5, 8.5, 12.75, 5.5), box(7.5, 12.75, 3.5, 8.5, 13.75, 5.5), box(7.5, 10.75, 2.5, 8.5, 11.75, 4.5), box(7.5, 5.75, 2.5, 8.5, 10.75, 3.5),
					box(7.5, 4.75, 3.5, 8.5, 9.75, 4.5), box(7.5, 3.75, 4.5, 8.5, 8.75, 5.5), box(7.5, 2.75, 5.5, 8.5, 7.75, 6.5), box(7.5, 1.75, 6.5, 8.5, 6.75, 7.5), box(7.5, 0.75, 7.5, 8.5, 5.75, 8.5), box(7.5, -0.25, 8.5, 8.5, 4.75, 9.5),
					box(7.5, -1.25, 9.5, 8.5, 3.75, 10.5), box(7.5, -1.25, 10.5, 8.5, 2.75, 11.5), box(7.5, 11.75, -2.5, 8.5, 12.75, -1.5), box(7.5, 11.75, -1.5, 8.5, 13.75, -0.5), box(7.5, 12.75, -4.5, 8.5, 15.75, -1.5));
			case Z -> Shapes.or(box(7.5, -1.5, 4.25, 8.5, -0.5, 5.25), box(7.5, -0.5, 3.25, 8.5, 0.5, 9.25), box(7.5, -1.5, 6.25, 8.5, -0.5, 10.25), box(7.5, -2.5, 8.25, 8.5, -1.5, 10.25), box(7.5, 0.5, 4.25, 8.5, 1.5, 6.25),
					box(7.5, 0.5, 6.25, 8.5, 1.5, 8.25), box(7.5, 1.5, 3.25, 8.5, 2.5, 9.25), box(7.5, 2.5, 3.25, 8.5, 5.5, 4.25), box(7.5, 3.5, 2.25, 8.5, 5.5, 3.25), box(7.5, 2.5, 4.25, 8.5, 4.5, 5.25), box(7.5, 2.5, 5.25, 8.5, 3.5, 10.25),
					box(7.5, 3.5, 6.25, 8.5, 4.5, 11.25), box(7.5, 4.5, 7.25, 8.5, 5.5, 12.25), box(7.5, 5.5, 8.25, 8.5, 6.5, 13.25), box(7.5, 6.5, 9.25, 8.5, 7.5, 14.25), box(7.5, 7.5, 10.25, 8.5, 8.5, 15.25), box(7.5, 8.5, 11.25, 8.5, 9.5, 16.25),
					box(7.5, 9.5, 12.25, 8.5, 10.5, 17.25), box(7.5, 10.5, 13.25, 8.5, 11.5, 17.25), box(7.5, -2.5, 3.25, 8.5, -1.5, 4.25), box(7.5, -1.5, 2.25, 8.5, -0.5, 4.25), box(7.5, -4.5, 0.25, 8.5, -1.5, 3.25));
		};
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(AXIS);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState().setValue(AXIS, context.getClickedFace().getAxis());
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rot) {
		if (rot == Rotation.CLOCKWISE_90 || rot == Rotation.COUNTERCLOCKWISE_90) {
			if (state.getValue(AXIS) == Direction.Axis.X) {
				return state.setValue(AXIS, Direction.Axis.Z);
			} else if (state.getValue(AXIS) == Direction.Axis.Z) {
				return state.setValue(AXIS, Direction.Axis.X);
			}
		}
		return state;
	}

	@Override
	public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter world, BlockPos pos, Player player) {
		return new ItemStack(MinecraftArmorWeaponModItems.NETHERITE_KATANA.get());
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		List<ItemStack> dropsOriginal = super.getDrops(state, builder);
		if (!dropsOriginal.isEmpty())
			return dropsOriginal;
		return Collections.singletonList(new ItemStack(MinecraftArmorWeaponModItems.NETHERITE_KATANA.get()));
	}
}
