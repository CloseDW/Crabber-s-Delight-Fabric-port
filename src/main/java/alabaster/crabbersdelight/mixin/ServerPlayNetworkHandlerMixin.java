package alabaster.crabbersdelight.mixin;

import alabaster.crabbersdelight.common.item.CrabClawItem;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

// 原版服务端对方块“交互/放置”也有同样的 6 格上限校验，
// 持蟹钳时一并放宽，否则 4.5~7.5 格放置方块会被服务端拒绝。
@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin {
    @Shadow
    @Final
    public ServerPlayerEntity player;

    @Redirect(method = "onPlayerInteractBlock",
            at = @At(value = "FIELD",
                    target = "Lnet/minecraft/server/network/ServerPlayNetworkHandler;MAX_BREAK_SQUARED_DISTANCE:D"))
    private double crabbersdelight$extendedInteractDistance() {
        return CrabClawItem.getExtendedBreakSquaredDistance(this.player);
    }
}
