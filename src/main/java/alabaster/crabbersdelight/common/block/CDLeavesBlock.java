package alabaster.crabbersdelight.common.block;

import alabaster.crabbersdelight.common.registry.CDModBlocks;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class CDLeavesBlock extends LeavesBlock {
    public static final BooleanProperty GENERATED = BooleanProperty.of("generated");

    public CDLeavesBlock(AbstractBlock.Settings settings) {
        super(settings);
        this.setDefaultState(this.getDefaultState().with(GENERATED, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(GENERATED);
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        // 玩家放置的树叶正常枯萎
        if (!state.get(GENERATED) && !state.get(LeavesBlock.PERSISTENT)) {
            super.randomTick(state, world, pos, random);
            return;
        }

        // 生成的树叶：4 格内没有棕榈原木就消失
        if (state.get(GENERATED)) {
            boolean connected = hasPalmLogNearby(world, pos, 4);
            if (!connected) {
                world.breakBlock(pos, true);
            }
        }
    }

    private boolean hasPalmLogNearby(World world, BlockPos pos, int range) {
        for (int dx = -range; dx <= range; dx++) {
            for (int dy = -range; dy <= range; dy++) {
                for (int dz = -range; dz <= range; dz++) {
                    if (dx == 0 && dy == 0 && dz == 0) continue;
                    BlockPos checkPos = pos.add(dx, dy, dz);
                    if (world.getBlockState(checkPos).isOf(CDModBlocks.PALM_LOG)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean hasRandomTicks(BlockState state) {
        return !state.get(LeavesBlock.PERSISTENT) || state.get(GENERATED);
    }
}
