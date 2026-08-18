package alabaster.crabbersdelight.common.registry;

import alabaster.crabbersdelight.CrabbersDelightFabric;
import net.fabricmc.fabric.api.object.builder.v1.block.type.WoodTypeRegistry;
import net.minecraft.block.BlockSetType;
import net.minecraft.block.WoodType;

public class CDWoodTypes {
    // 用 Fabric API 官方注册器：名字会带 mod 命名空间（crabbersdelight:palm），
    // 与 Forge 原版的 CrabbersDelight.MODID + ":palm" 保持一致。
    // 不再需要自写 WoodTypeAccessor @Invoker 了。
    public static final WoodType PALM = WoodTypeRegistry.register(
            CrabbersDelightFabric.id("palm"), BlockSetType.OAK);
}
