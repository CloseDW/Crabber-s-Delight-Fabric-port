package alabaster.crabbersdelight.mixin;

import alabaster.crabbersdelight.common.registry.CDModItems;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.condition.MatchToolLootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.predicate.item.ItemPredicate;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// 原版树叶/藤蔓/蜘蛛网等“用剪刀才能掉落自身”是靠战利品表里的 match_tool 条件
// 写死 minecraft:shears 实现的，自定义剪刀子类（蟹钳）不满足。
// 这里在所有 match_tool 条件前插一手：只要手持蟹钳、且这个条件对“原版剪刀”成立，
// 就直接放行——蟹钳在掉落层面完全等价于剪刀（树叶掉自身、藤蔓掉落等）。
@Mixin(MatchToolLootCondition.class)
public abstract class MatchToolLootConditionMixin {
    private static final Logger LOGGER = LoggerFactory.getLogger("crabbersdelight");

    @Shadow
    @Final
    private ItemPredicate predicate;

    @Inject(method = "test(Lnet/minecraft/loot/context/LootContext;)Z",
            at = @At("HEAD"), cancellable = true)
    private void crabbersdelight$clawCountsAsShears(LootContext context, CallbackInfoReturnable<Boolean> cir) {
        ItemStack tool = context.get(LootContextParameters.TOOL);
        if (tool != null && tool.isOf(CDModItems.CRAB_CLAW)
                && this.predicate.test(new ItemStack(Items.SHEARS))) {
            LOGGER.info("[crab_claw] 蟹钳通过了剪刀战利品条件（match_tool）");
            cir.setReturnValue(true);
        }
    }
}
