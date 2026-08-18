package alabaster.crabbersdelight.common.block;

import alabaster.crabbersdelight.common.block.entity.CDSignBlockEntity;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.SignBlock;
import net.minecraft.block.WoodType;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class CDSignBlock extends SignBlock {
    public CDSignBlock(AbstractBlock.Settings settings, WoodType type) {
        super(settings, type);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new CDSignBlockEntity(pos, state);
    }
}
