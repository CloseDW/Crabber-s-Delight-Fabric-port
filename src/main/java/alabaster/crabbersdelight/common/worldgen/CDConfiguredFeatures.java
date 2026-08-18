package alabaster.crabbersdelight.common.worldgen;

import alabaster.crabbersdelight.CrabbersDelightFabric;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.gen.feature.ConfiguredFeature;

public class CDConfiguredFeatures {
    // 树苗生长时引用的特征键；实际特征由数据包 JSON 定义
    // (data/crabbersdelight/worldgen/configured_feature/palm.json)
    public static final RegistryKey<ConfiguredFeature<?, ?>> PALM_KEY =
            RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, CrabbersDelightFabric.id("palm"));
}
