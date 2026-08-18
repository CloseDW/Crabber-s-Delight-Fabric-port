package alabaster.crabbersdelight.common.block;

import alabaster.crabbersdelight.common.block.entity.CDHangingSignBlockEntity;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.HangingSignBlock;
import net.minecraft.block.WoodType;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class CDHangingSignBlock extends HangingSignBlock {
    public CDHangingSignBlock(AbstractBlock.Settings settings, WoodType type) {
        super(settings, type);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new CDHangingSignBlockEntity(pos, state);
    }
}
