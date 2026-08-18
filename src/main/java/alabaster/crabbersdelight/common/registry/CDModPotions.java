package alabaster.crabbersdelight.common.registry;

import alabaster.crabbersdelight.CrabbersDelightFabric;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.potion.Potion;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class CDModPotions {
    // 墨盲药水：15 秒失明（对应 Forge 的 CDModPotions.INKY_POTION）
    public static final Potion INKY_POTION = Registry.register(
            Registries.POTION, CrabbersDelightFabric.id("inky_potion"),
            new Potion(new StatusEffectInstance(StatusEffects.BLINDNESS, 300, 0)));

    // 空方法：让 onInitialize 显式调用一次，保证类加载完成注册
    public static void register() {
    }
}
