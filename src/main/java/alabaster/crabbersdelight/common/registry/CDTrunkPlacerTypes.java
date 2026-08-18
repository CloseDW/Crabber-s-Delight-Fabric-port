package alabaster.crabbersdelight.common.registry;

import alabaster.crabbersdelight.CrabbersDelightFabric;
import alabaster.crabbersdelight.common.worldgen.tree.PalmTrunkPlacer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.world.gen.trunk.TrunkPlacerType;

public class CDTrunkPlacerTypes {
    public static final TrunkPlacerType<PalmTrunkPlacer> PALM = Registry.register(
            Registries.TRUNK_PLACER_TYPE, CrabbersDelightFabric.id("palm_trunk_placer"),
            new TrunkPlacerType<>(PalmTrunkPlacer.CODEC));

    public static void register() {
    }
}
