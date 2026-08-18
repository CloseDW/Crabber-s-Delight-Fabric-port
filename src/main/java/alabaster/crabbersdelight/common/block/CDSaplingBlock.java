package alabaster.crabbersdelight.common.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SaplingBlock;
import net.minecraft.block.sapling.SaplingGenerator;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

import java.util.function.Supplier;

public class CDSaplingBlock extends SaplingBlock {
    private final Supplier<Block> blockToSurviveOn;

    public CDSaplingBlock(SaplingGenerator generator, AbstractBlock.Settings settings, Supplier<Block> block) {
        super(generator, settings);
        this.blockToSurviveOn = block;
    }

    @Override
    protected boolean canPlantOnTop(BlockState state, BlockView world, BlockPos pos) {
        Block block = state.getBlock();
        return blockToSurviveOn.get() == block
                || state.isIn(BlockTags.DIRT)
                || state.isIn(BlockTags.SAND);
    }
}
