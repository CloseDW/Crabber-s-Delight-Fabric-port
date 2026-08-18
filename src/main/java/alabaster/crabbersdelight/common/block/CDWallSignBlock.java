package alabaster.crabbersdelight.common.block;

import alabaster.crabbersdelight.common.block.entity.CDSignBlockEntity;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.WallSignBlock;
import net.minecraft.block.WoodType;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class CDWallSignBlock extends WallSignBlock {
    public CDWallSignBlock(AbstractBlock.Settings settings, WoodType type) {
        super(settings, type);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new CDSignBlockEntity(pos, state);
    }
}
