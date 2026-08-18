package alabaster.crabbersdelight.client.mixin;

import alabaster.crabbersdelight.common.item.CrabClawItem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// 客户端瞄准距离：原版生存 4.5 / 创造 5.0（getReachDistance 同时驱动方块与实体准星）。
// 持蟹钳时 +3，等价于 Forge 的 BLOCK_REACH 属性对客户端的作用。
@Mixin(ClientPlayerInteractionManager.class)
public abstract class ClientPlayerInteractionManagerMixin {
    @Shadow
    @Final
    private MinecraftClient client;

    @Inject(method = "getReachDistance", at = @At("HEAD"), cancellable = true)
    private void crabbersdelight$extendedReach(CallbackInfoReturnable<Float> cir) {
        PlayerEntity player = this.client.player;
        if (player != null && CrabClawItem.isHoldingExactlyOneClaw(player)) {
            cir.setReturnValue(CrabClawItem.getExtendedReachDistance(player));
        }
    }
}
