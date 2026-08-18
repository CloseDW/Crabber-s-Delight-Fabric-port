package alabaster.crabbersdelight.mixin;

import alabaster.crabbersdelight.common.registry.CDModItems;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// 原版 SheepEntity#interactMob 判断 isOf(Items.SHEARS) 才允许剪毛，
// 这里在方法开头补一个“持蟹钳时走剪毛流程”的分支（成功剪毛消耗 1 耐久，与原版剪刀一致）。
@Mixin(SheepEntity.class)
public abstract class SheepEntityMixin {
    @Inject(method = "interactMob(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;)Lnet/minecraft/util/ActionResult;",
            at = @At("HEAD"), cancellable = true)
    private void crabbersdelight$clawShearsSheep(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        ItemStack stack = player.getStackInHand(hand);
        if (!stack.isOf(CDModItems.CRAB_CLAW)) return;

        SheepEntity sheep = (SheepEntity) (Object) this;
        if (sheep.getWorld().isClient) {
            cir.setReturnValue(ActionResult.CONSUME);
            return;
        }
        if (sheep.isShearable()) {
            sheep.sheared(SoundCategory.PLAYERS);
            sheep.emitGameEvent(GameEvent.SHEAR, player);
            stack.damage(1, player, (p) -> p.sendEquipmentBreakStatus(
                    hand == Hand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND));
            cir.setReturnValue(ActionResult.SUCCESS);
            return;
        }
        cir.setReturnValue(ActionResult.CONSUME);
    }
}
