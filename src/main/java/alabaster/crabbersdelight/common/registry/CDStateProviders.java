package alabaster.crabbersdelight.common.registry;

import alabaster.crabbersdelight.CrabbersDelightFabric;
import alabaster.crabbersdelight.common.worldgen.RandomSeashellStateProvider;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.world.gen.stateprovider.BlockStateProviderType;

// 对应 Forge 的 CDStateProviders：注册方块状态提供器 codec，
// 供世界生成 JSON 的 "type": "crabbersdelight:random_seashell" 使用。
public class CDStateProviders {
    public static final BlockStateProviderType<RandomSeashellStateProvider> RANDOM_SEASHELL =
            Registry.register(Registries.BLOCK_STATE_PROVIDER_TYPE,
                    CrabbersDelightFabric.id("random_seashell"),
                    new BlockStateProviderType<>(RandomSeashellStateProvider.CODEC));

    public static void register() {
    }
}
