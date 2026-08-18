package alabaster.crabbersdelight.common.block;

import alabaster.crabbersdelight.common.block.entity.CrabTrapBlockEntity;
import alabaster.crabbersdelight.common.registry.CDModBlockEntity;
import alabaster.crabbersdelight.common.utils.CDTextUtils;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.Waterloggable;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import static alabaster.crabbersdelight.common.block.entity.CrabTrapBlockEntity.isSurroundedByWater;

public class CrabTrapBlock extends BlockWithEntity implements Waterloggable {
    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;
    public static final BooleanProperty HANGING = Properties.HANGING;
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

    public CrabTrapBlock(AbstractBlock.Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState()
                .with(FACING, Direction.NORTH).with(HANGING, false).with(WATERLOGGED, false));
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.isOf(newState.getBlock())) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof CrabTrapBlockEntity crabTrapBlockEntity) {
                ItemScatterer.spawn(world, pos, crabTrapBlockEntity.getInventory());
                world.updateComparators(pos, this);
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player,
                             Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof CrabTrapBlockEntity crabTrapBlockEntity) {
                if (state.get(WATERLOGGED) == Boolean.TRUE || state.get(HANGING) == Boolean.TRUE) {
                    if (isSurroundedByWater(world, pos) == Boolean.TRUE) {
                        ((ServerPlayerEntity) player).openHandledScreen(crabTrapBlockEntity);
                    } else {
                        player.sendMessage(CDTextUtils.getTranslation("block.crab_trap.insufficient_surrounding_water"), true);
                    }
                } else {
                    player.sendMessage(CDTextUtils.getTranslation("block.crab_trap.not_waterlogged"), true);
                }
            }
        }
        return ActionResult.success(world.isClient);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Direction direction = state.get(FACING);
        if (direction == Direction.NORTH || direction == Direction.SOUTH) {
            return createCuboidShape(0.0D, 0.0D, 1.0D, 16.0D, 10.0D, 15.0D);
        }
        return createCuboidShape(1.0D, 0.0D, 0.0D, 15.0D, 10.0D, 16.0D);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        BlockState stateAbove = context.getWorld().getBlockState(context.getBlockPos().up());
        Direction direction = context.getHorizontalPlayerFacing().getOpposite();
        FluidState fluidState = context.getWorld().getFluidState(context.getBlockPos());
        boolean hangingFlag = !stateAbove.isAir() && !(stateAbove.isOf(Blocks.WATER) || stateAbove.isOf(Blocks.LAVA));

        return this.getDefaultState().with(FACING, direction)
                .with(WATERLOGGED, fluidState.isOf(Fluids.WATER))
                .with(HANGING, hangingFlag);
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState,
                                                WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        BlockState stateAbove = world.getBlockState(pos.up());
        if (state.get(WATERLOGGED)) {
            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }
        if (!stateAbove.isAir() && !(stateAbove.isOf(Blocks.WATER) || stateAbove.isOf(Blocks.LAVA))) {
            return state.with(HANGING, true);
        }

        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos)
                .with(HANGING, false);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getDefaultState() : super.getFluidState(state);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, HANGING, WATERLOGGED);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new CrabTrapBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state,
                                                                  BlockEntityType<T> type) {
        return world.isClient ? null
                : checkType(type, CDModBlockEntity.CRAB_TRAP, CrabTrapBlockEntity::serverTick);
    }
}
