package alabaster.crabbersdelight.common.registry;

import alabaster.crabbersdelight.CrabbersDelightFabric;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class CDDamageSources {
    public static final RegistryKey<DamageType> FALLING_COCONUT =
            RegistryKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier(CrabbersDelightFabric.MOD_ID, "falling_coconut"));

    public static DamageSource getSimpleDamageSource(World world, RegistryKey<DamageType> type) {
        return new DamageSource(world.getRegistryManager().get(RegistryKeys.DAMAGE_TYPE).getEntry(type).orElseThrow());
    }
}
