package alabaster.crabbersdelight.common.registry;

import alabaster.crabbersdelight.CrabbersDelightFabric;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

import java.util.function.Supplier;

public enum CDArmorMaterials implements ArmorMaterial {
    // 珍珠项链：0 护甲值（防护全靠效果），25 耐久系数，珍珠修复
    PEARL_NECKLACE(
            "pearl_necklace",
            25,
            new int[]{0, 0, 0, 0}, // 靴/腿/胸/头 的护甲值
            0,
            SoundEvents.ITEM_ARMOR_EQUIP_GENERIC,
            () -> Ingredient.ofItems(CDModItems.PEARL),
            0.0F,
            0.0F
    );

    private static final int[] HEALTH_PER_SLOT = new int[]{13, 15, 16, 11};

    private final String name;
    private final int durabilityMultiplier;
    private final int[] slotProtections;
    private final int enchantability;
    private final SoundEvent equipSound;
    private final Supplier<Ingredient> repairIngredient;
    private final float toughness;
    private final float knockbackResistance;

    CDArmorMaterials(String name,
                     int durabilityMultiplier,
                     int[] slotProtections,
                     int enchantability,
                     SoundEvent equipSound,
                     Supplier<Ingredient> repairIngredient,
                     float toughness,
                     float knockbackResistance) {
        this.name = name;
        this.durabilityMultiplier = durabilityMultiplier;
        this.slotProtections = slotProtections;
        this.enchantability = enchantability;
        this.equipSound = equipSound;
        this.repairIngredient = repairIngredient;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
    }

    @Override
    public int getDurability(ArmorItem.Type type) {
        return HEALTH_PER_SLOT[type.getEquipmentSlot().getEntitySlotId()] * this.durabilityMultiplier;
    }

    @Override
    public int getProtection(ArmorItem.Type type) {
        return this.slotProtections[type.getEquipmentSlot().getEntitySlotId()];
    }

    @Override
    public int getEnchantability() {
        return this.enchantability;
    }

    @Override
    public SoundEvent getEquipSound() {
        return this.equipSound;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }

    @Override
    public String getName() {
        return CrabbersDelightFabric.MOD_ID + ":" + this.name;
    }

    @Override
    public float getToughness() {
        return this.toughness;
    }

    @Override
    public float getKnockbackResistance() {
        return this.knockbackResistance;
    }
}
