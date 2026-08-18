package alabaster.crabbersdelight.mixin;

import alabaster.crabbersdelight.common.item.CrabClawItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// 原版服务端对“破坏方块”有 6 格（36.0 平方）的上限校验（ServerPlayNetworkHandler.MAX_BREAK_SQUARED_DISTANCE）。
// 持蟹钳时把上限放宽到 4.5/5.0 + 3 的距离，等价于 Forge 的 BLOCK_REACH 属性。
@Mixin(ServerPlayerInteractionManager.class)
public abstract class ServerPlayerInteractionManagerMixin {
    @Shadow
    @Final
    public ServerPlayerEntity player;

    @Redirect(method = "processBlockBreakingAction",
            at = @At(value = "FIELD",
                    target = "Lnet/minecraft/server/network/ServerPlayNetworkHandler;MAX_BREAK_SQUARED_DISTANCE:D"))
    private double crabbersdelight$extendedBreakDistance() {
        return CrabClawItem.getExtendedBreakSquaredDistance(this.player);
    }

    // 等价 Forge BlockEvent.EntityPlaceEvent：超距“放置”方块（放的是方块物品且成功）时扣耐久
    @Inject(method = "interactBlock", at = @At("RETURN"))
    private void crabbersdelight$consumeOnPlace(ServerPlayerEntity player, World world, ItemStack stack,
                                                Hand hand, BlockHitResult hitResult,
                                                CallbackInfoReturnable<ActionResult> cir) {
        if (cir.getReturnValue().isAccepted() && stack.getItem() instanceof BlockItem) {
            double dist = player.getEyePos().distanceTo(Vec3d.ofCenter(hitResult.getBlockPos()));
            if (dist > CrabClawItem.getBaseBlockReach(player)) {
                CrabClawItem.damageClaws(player);
            }
        }
    }
}
