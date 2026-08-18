package alabaster.crabbersdelight.mixin;

import alabaster.crabbersdelight.common.item.CrabClawItem;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

// PlayerEntity#attack 的横扫（sweep）只作用于 3 格（9.0 平方）内的实体。
// 持蟹钳时把 9.0 改成 36.0（6 格），横扫判定自然放宽。
// 注：参考库 reach-entity-attributes 处理同一检查用的也是 ModifyConstant 改 9.0，
//     比 @Redirect 定位 squaredDistanceTo 调用更可靠。
@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {
    @ModifyConstant(method = "attack(Lnet/minecraft/entity/Entity;)V",
            constant = @Constant(doubleValue = 9.0D))
    private double crabbersdelight$extendedSweepReach(double original) {
        if (CrabClawItem.isHoldingExactlyOneClaw((PlayerEntity) (Object) this)) {
            return 36.0D;
        }
        return original;
    }
}
