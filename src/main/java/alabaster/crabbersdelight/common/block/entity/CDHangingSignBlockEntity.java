package alabaster.crabbersdelight.common.block.entity;

import alabaster.crabbersdelight.common.registry.CDModBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.HangingSignBlockEntity;
import net.minecraft.util.math.BlockPos;

public class CDHangingSignBlockEntity extends HangingSignBlockEntity {
    public CDHangingSignBlockEntity(BlockPos pos, BlockState blockState) {
        super(pos, blockState);
    }

    @Override
    public BlockEntityType<?> getType() {
        return CDModBlockEntity.HANGING_PALM_SIGN;
    }
}
