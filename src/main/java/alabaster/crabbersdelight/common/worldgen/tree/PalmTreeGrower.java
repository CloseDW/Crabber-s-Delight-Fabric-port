package alabaster.crabbersdelight.common.worldgen.tree;

import alabaster.crabbersdelight.common.worldgen.CDConfiguredFeatures;
import net.minecraft.block.sapling.SaplingGenerator;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.gen.feature.ConfiguredFeature;

public class PalmTreeGrower extends SaplingGenerator {
    @Override
    protected RegistryKey<ConfiguredFeature<?, ?>> getTreeFeature(Random random, boolean bees) {
        return CDConfiguredFeatures.PALM_KEY;
    }
}
