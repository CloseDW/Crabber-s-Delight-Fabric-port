package alabaster.crabbersdelight.common.block;

import alabaster.crabbersdelight.common.Config;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.Waterloggable;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

public class SeashellBlock extends Block implements Waterloggable {
    public static final IntProperty VARIANT = IntProperty.of("variant", 0, 63);
    public static final DirectionProperty FACING = DirectionProperty.of("facing",
            Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST);
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

    private static final VoxelShape SHAPE = createCuboidShape(3, 0, 3, 13, 1, 13);

    public SeashellBlock(AbstractBlock.Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState()
                .with(VARIANT, 0)
                .with(FACING, Direction.NORTH)
                .with(WATERLOGGED, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(VARIANT, FACING, WATERLOGGED);
    }

    private int getVariantCount() {
        return Config.SEASHELL_VARIANT_COUNT.get();
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        int count = getVariantCount();
        int variant = context.getWorld().random.nextInt(count);
        Direction facing = Direction.Type.HORIZONTAL.random(context.getWorld().random);
        boolean waterlogged = context.getWorld().getFluidState(context.getBlockPos()).getFluid() == Fluids.WATER;

        return this.getDefaultState()
                .with(VARIANT, variant)
                .with(FACING, facing)
                .with(WATERLOGGED, waterlogged);
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        super.onBlockAdded(state, world, pos, oldState, notify);
        if (world.getFluidState(pos).getFluid() == Fluids.WATER) {
            world.setBlockState(pos, state.with(WATERLOGGED, true), 2);
            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext ctx) {
        var offset = state.getModelOffset(world, pos);
        return SHAPE.offset(offset.x, offset.y, offset.z);
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        BlockPos below = pos.down();
        BlockState ground = world.getBlockState(below);
        return ground.isSideSolidFullSquare(world, below, Direction.UP);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getDefaultState() : super.getFluidState(state);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction dir, BlockState neighborState,
                                                WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (world.getFluidState(pos).getFluid() == Fluids.WATER) {
            if (!state.get(WATERLOGGED)) {
                state = state.with(WATERLOGGED, true);
            }
            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        } else if (state.get(WATERLOGGED)) {
            state = state.with(WATERLOGGED, false);
        }

        return super.getStateForNeighborUpdate(state, dir, neighborState, world, pos, neighborPos);
    }
}
