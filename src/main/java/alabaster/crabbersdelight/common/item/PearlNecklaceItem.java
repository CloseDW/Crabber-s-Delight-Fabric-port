package alabaster.crabbersdelight.common.item;

import alabaster.crabbersdelight.common.registry.CDArmorMaterials;
import alabaster.crabbersdelight.common.registry.CDModItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

// 珍珠项链：穿戴在胸甲位时，只要在水中/雨里/气泡里就给海豚恩惠 + 水下呼吸，
// 每 5 秒消耗 1 点耐久。可用珍珠修复。
public class PearlNecklaceItem extends ArmorItem {
    private static final int TICK_INTERVAL = 100; // 5 秒
    private static final int EFFECT_REFRESH_INTERVAL = 20; // 每秒刷新一次
    private static final int EFFECT_DURATION = 320;        // 每次给 16 秒（320 tick）效果

    public PearlNecklaceItem(Item.Settings settings) {
        super(CDArmorMaterials.PEARL_NECKLACE, ArmorItem.Type.CHESTPLATE, settings.maxDamage(128));
    }

    @Override
    public boolean canRepair(ItemStack stack, ItemStack ingredient) {
        return ingredient.isOf(CDModItems.PEARL);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (world.isClient) return;
        if (!(entity instanceof PlayerEntity player)) return;
        if (player.getEquippedStack(EquipmentSlot.CHEST) != stack) return;

        // Forge 的 isInWaterRainOrBubble = 水/雨/气泡柱，Yarn 拆成两个方法
        if (player.isTouchingWaterOrRain() || player.isInsideWaterOrBubbleColumn()) {
            // 每秒刷新一次、每次给 2 秒效果，避免每 tick 都调用 addStatusEffect 造成性能消耗
            if (world.getTime() % EFFECT_REFRESH_INTERVAL == 0) {
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.DOLPHINS_GRACE, EFFECT_DURATION, 0, false, false, true));
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.WATER_BREATHING, EFFECT_DURATION, 0, false, false, true));
            }

            if (world.getTime() % TICK_INTERVAL == 0) {
                stack.damage(1, player, (p) -> p.sendEquipmentBreakStatus(EquipmentSlot.CHEST));
            }
        }
    }
}
