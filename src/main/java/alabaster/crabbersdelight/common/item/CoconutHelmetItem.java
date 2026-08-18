package alabaster.crabbersdelight.common.item;

import alabaster.crabbersdelight.common.registry.CDModItems;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Equipment;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.UUID;

// 椰子头盔保持普通 Item：1.20.1 原版 HeadFeatureRenderer 会把“非盔甲类”的头部物品
// 当作物品模型渲染（HEAD 视角），配合 coconut_helmet.json 里 head→coconut_helmet_3d 的
// 视角切换（由 ItemRendererMixin 实现），穿戴时就能显示 3D 椰壳模型。
// 如果改成 ArmorItem 反而会被 HeadFeatureRenderer 跳过，只显示原版盔甲形状。
// 实现 Equipment 接口后，1.20.1 的 LivingEntity.getPreferredEquipmentSlot 会把它识别为
// 头部装备，背包里可以直接拖到头盔栏（原版 ArmorSlot 只接受首选槽位为 HEAD 的物品）。
public class CoconutHelmetItem extends Item implements Equipment {
    private static final UUID ARMOR_MODIFIER_UUID = UUID.fromString("1e6e6f80-bdf7-4d57-b010-22a9cb3b4f2c");
    private static final Identifier ARMOR_MODIFIER_ID =
            new Identifier("crabbersdelight", "coconut_helmet_armor");
    private static final double ARMOR_VALUE = 3.0;
    private static final int DAMAGE_INTERVAL_TICKS = 600; // 30 秒
    private static final int EFFECT_REFRESH_INTERVAL = 20; // 每秒刷新一次
    private static final int EFFECT_DURATION = 320;         // 每次给 16 秒（320 tick）效果

    private final Multimap<EntityAttribute, EntityAttributeModifier> defaultModifiers;

    public CoconutHelmetItem(Item.Settings settings) {
        super(settings.maxDamage(64));

        ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(EntityAttributes.GENERIC_ARMOR, new EntityAttributeModifier(
                ARMOR_MODIFIER_UUID, "Coconut armor", ARMOR_VALUE, EntityAttributeModifier.Operation.ADDITION));
        this.defaultModifiers = builder.build();
    }

    @Override
    public EquipmentSlot getSlotType() {
        return EquipmentSlot.HEAD;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        ItemStack head = player.getEquippedStack(EquipmentSlot.HEAD);

        if (head.isEmpty()) {
            player.equipStack(EquipmentSlot.HEAD, stack.copy());
            if (!player.isCreative()) stack.decrement(1);
            world.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.ITEM_ARMOR_EQUIP_TURTLE, player.getSoundCategory(), 1.0F, 1.0F);
            return TypedActionResult.success(stack, world.isClient);
        }
        return TypedActionResult.fail(stack);
    }

    @Override
    public boolean canRepair(ItemStack stack, ItemStack ingredient) {
        return ingredient.isOf(CDModItems.COCONUT_HALVE);
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
        return slot == EquipmentSlot.HEAD ? this.defaultModifiers : super.getAttributeModifiers(slot);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (world.isClient) return;
        if (!(entity instanceof PlayerEntity player)) return;
        if (player.getEquippedStack(EquipmentSlot.HEAD) != stack) return;

        // 耐久已耗尽（Damage >= maxDamage）：立即销毁，防止耐久继续扣到负数且永不爆掉
        if (stack.getDamage() >= stack.getMaxDamage()) {
            stack.decrement(1);
            return;
        }

        // 每秒刷新一次、每次给 2 秒效果，避免每 tick 都调用 addStatusEffect 造成性能消耗
        if (world.getTime() % EFFECT_REFRESH_INTERVAL == 0) {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, EFFECT_DURATION, 0, false, false, true));
        }

        // 每 30 秒扣 1 耐久。用带 Consumer 的 damage 重载：耐久耗尽时自动销毁物品（等价于 Forge 的 hurtAndBreak）。
        // 注意不能用 damage(int, Random, ServerPlayerEntity) 那个重载，它只返回是否损坏、不会销毁物品。
        if (world.getTime() % DAMAGE_INTERVAL_TICKS == 0 && player instanceof ServerPlayerEntity serverPlayer) {
            stack.damage(1, serverPlayer, p -> {});
        }
    }
}
