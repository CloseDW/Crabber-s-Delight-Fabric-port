package alabaster.crabbersdelight.common.item;

import alabaster.crabbersdelight.common.registry.CDModItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShearsItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

// 蟹钳：原版 Forge 版通过 ForgeMod.BLOCK_REACH / ENTITY_REACH 属性 + 4 个事件实现，
// Fabric 没有这些属性/事件，等价方案是：
//   1. 客户端 Mixin 改 ClientPlayerInteractionManager#getReachDistance（瞄准距离）
//   2. 客户端 Mixin 放宽 updateTargetedEntity 里生存模式实体准星的 3 格硬上限
//   3. 服务端 Mixin 放宽方块破坏/交互距离校验（默认 6 格上限）
//   4. 服务端 Mixin 放宽 attack() 里横扫的距离检查
//   5. Fabric 事件（PlayerBlockBreakEvents / AttackEntityCallback）负责超距时扣耐久
public class CrabClawItem extends ShearsItem {
    public static final int MAX_DAMAGE = 128;

    // Forge 属性加成：方块触及 +3，实体触及 +3
    public static final float BLOCK_REACH_BONUS = 3.0F;
    public static final double ENTITY_REACH_BONUS = 3.0D;

    public CrabClawItem(Item.Settings settings) {
        super(settings.maxDamage(MAX_DAMAGE));
    }

    // 主手/副手“恰好”拿一个蟹钳时才生效（Forge 用 main ^ off 判断）
    public static boolean isHoldingExactlyOneClaw(PlayerEntity player) {
        boolean main = player.getMainHandStack().isOf(CDModItems.CRAB_CLAW);
        boolean off = player.getOffHandStack().isOf(CDModItems.CRAB_CLAW);
        return main ^ off;
    }

    // 原版基础方块触及：生存 4.5，创造 5.0（对应 Forge BLOCK_REACH 属性默认值）
    public static float getBaseBlockReach(PlayerEntity player) {
        return player.isCreative() ? 5.0F : 4.5F;
    }

    // 服务端校验用的最大距离（平方值）：拿蟹钳时按 4.5/5.0 + 3 计算。
    // 额外 +1 容差：客户端准星射线判定的是"方块表面"的距离，服务端判定的是
    // "眼睛到方块中心"的距离（相差半个方块 ~0.5-0.87），不加容差会导致
    // 方块表面在距离内、中心在距离外的"破坏后立刻恢复"现象。
    public static double getExtendedBreakSquaredDistance(PlayerEntity player) {
        double reach = getBaseBlockReach(player) + BLOCK_REACH_BONUS + 1.0F;
        return reach * reach;
    }

    // 客户端瞄准距离：生存 7.5 / 创造 8.0
    public static float getExtendedReachDistance(PlayerEntity player) {
        return getBaseBlockReach(player) + BLOCK_REACH_BONUS;
    }

    private static double distanceEyeToAABB(PlayerEntity player, LivingEntity target) {
        Vec3d eye = player.getEyePos();
        var box = target.getBoundingBox();
        double x = Math.max(box.minX, Math.min(eye.x, box.maxX));
        double y = Math.max(box.minY, Math.min(eye.y, box.maxY));
        double z = Math.max(box.minZ, Math.min(eye.z, box.maxZ));
        return eye.distanceTo(new Vec3d(x, y, z));
    }

    // 是否超出原版实体触及（3.0 格，即 Forge ENTITY_REACH 默认值）
    private static boolean beyondBaseEntityReach(PlayerEntity player, LivingEntity target) {
        return distanceEyeToAABB(player, target) > 3.0D;
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (attacker instanceof PlayerEntity player) {
            if (!player.isCreative() && beyondBaseEntityReach(player, target)) {
                stack.damage(1, player, (p) -> p.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
                return true;
            }
        }
        return super.postHit(stack, target, attacker);
    }

    // 对应 Forge AttackEntityEvent：攻击超距目标时消耗耐久（服务端才执行）
    public static void handleAttackEntity(PlayerEntity player, World world, Entity targetEntity) {
        if (world.isClient || !(targetEntity instanceof LivingEntity target)) return;

        ItemStack main = player.getMainHandStack();
        ItemStack off = player.getOffHandStack();
        boolean clawMain = main.isOf(CDModItems.CRAB_CLAW);
        boolean clawOff = off.isOf(CDModItems.CRAB_CLAW);
        if (!clawMain && !clawOff) return;

        if (beyondBaseEntityReach(player, target)) {
            if (clawMain) main.damage(1, player, (p) -> p.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
            if (clawOff) off.damage(1, player, (p) -> p.sendEquipmentBreakStatus(EquipmentSlot.OFFHAND));
        }
    }

    // 对应 Forge BlockEvent.BreakEvent / EntityPlaceEvent：超距破坏/放置时消耗耐久
    public static void damageClaws(PlayerEntity player) {
        if (player == null || player.getWorld().isClient) return;

        ItemStack main = player.getMainHandStack();
        ItemStack off = player.getOffHandStack();
        if (main.isOf(CDModItems.CRAB_CLAW)) {
            main.damage(1, player, (p) -> p.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
        }
        if (off.isOf(CDModItems.CRAB_CLAW)) {
            off.damage(1, player, (p) -> p.sendEquipmentBreakStatus(EquipmentSlot.OFFHAND));
        }
    }

    public static void consumeOnBreak(PlayerEntity player, BlockPos pos) {
        double dist = player.getEyePos().distanceTo(Vec3d.ofCenter(pos));
        if (dist > getBaseBlockReach(player)) {
            damageClaws(player);
        }
    }
}
