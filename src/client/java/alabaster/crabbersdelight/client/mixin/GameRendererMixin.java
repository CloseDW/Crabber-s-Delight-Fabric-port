package alabaster.crabbersdelight.client.mixin;

import alabaster.crabbersdelight.common.item.CrabClawItem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

// 生存模式下实体准星有 3 格（9.0 平方）硬上限：超过就视为 miss。
// 持蟹钳时放宽到 6 格（36.0），与 Forge 的 ENTITY_REACH（3+3）一致。
@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
    @Shadow
    @Final
    private MinecraftClient client;

    @ModifyConstant(method = "updateTargetedEntity", constant = @Constant(doubleValue = 9.0D))
    private double crabbersdelight$extendedEntityTargetRange(double original) {
        PlayerEntity player = this.client.player;
        if (player != null && CrabClawItem.isHoldingExactlyOneClaw(player)) {
            return 36.0D;
        }
        return original;
    }
}
