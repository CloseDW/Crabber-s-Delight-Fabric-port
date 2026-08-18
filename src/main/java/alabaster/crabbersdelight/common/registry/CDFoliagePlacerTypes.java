package alabaster.crabbersdelight.common.registry;

import alabaster.crabbersdelight.CrabbersDelightFabric;
import alabaster.crabbersdelight.common.worldgen.tree.PalmFoliagePlacer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.world.gen.foliage.FoliagePlacerType;

public class CDFoliagePlacerTypes {
    public static final FoliagePlacerType<PalmFoliagePlacer> PALM_FOLIAGE_PLACER = Registry.register(
            Registries.FOLIAGE_PLACER_TYPE, CrabbersDelightFabric.id("palm_foliage_placer"),
            new FoliagePlacerType<>(PalmFoliagePlacer.CODEC));

    public static void register() {
    }
}
