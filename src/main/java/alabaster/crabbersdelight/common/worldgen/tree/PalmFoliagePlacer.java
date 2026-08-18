package alabaster.crabbersdelight.common.worldgen.tree;

import alabaster.crabbersdelight.common.block.CDLeavesBlock;
import alabaster.crabbersdelight.common.block.CoconutBlock;
import alabaster.crabbersdelight.common.registry.CDFoliagePlacerTypes;
import alabaster.crabbersdelight.common.registry.CDModBlocks;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.TestableWorld;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.foliage.FoliagePlacer;
import net.minecraft.world.gen.foliage.FoliagePlacerType;

public class PalmFoliagePlacer extends FoliagePlacer {
    public static final Codec<PalmFoliagePlacer> CODEC =
            RecordCodecBuilder.create(instance ->
                    fillFoliagePlacerFields(instance).apply(instance, PalmFoliagePlacer::new));

    public PalmFoliagePlacer(IntProvider radius, IntProvider offset) {
        super(radius, offset);
    }

    @Override
    protected FoliagePlacerType<?> getType() {
        return CDFoliagePlacerTypes.PALM_FOLIAGE_PLACER;
    }

    @Override
    protected void generate(TestableWorld world, BlockPlacer placer, Random random, TreeFeatureConfig config,
                            int trunkHeight, TreeNode treeNode, int foliageHeight, int radius, int offset) {
        BlockPos center = treeNode.getCenter();

        // 顶部一簇
        BlockPos tuftCenter = treeNode.getCenter();
        placeLeafWithCoconut(world, placer, random, config, tuftCenter);
        placeLeafWithCoconut(world, placer, random, config, tuftCenter.north());
        placeLeafWithCoconut(world, placer, random, config, tuftCenter.south());
        placeLeafWithCoconut(world, placer, random, config, tuftCenter.east());
        placeLeafWithCoconut(world, placer, random, config, tuftCenter.west());

        // 四个主方向
        createFrond(center, world, placer, random, config, 1, 0);
        createFrond(center, world, placer, random, config, -1, 0);
        createFrond(center, world, placer, random, config, 0, 1);
        createFrond(center, world, placer, random, config, 0, -1);

        // 四个对角
        createFrond(center, world, placer, random, config, 1, 1);
        createFrond(center, world, placer, random, config, 1, -1);
        createFrond(center, world, placer, random, config, -1, 1);
        createFrond(center, world, placer, random, config, -1, -1);
    }

    @Override
    public int getRandomHeight(Random random, int height, TreeFeatureConfig config) {
        return 0;
    }

    @Override
    protected boolean isInvalidForLeaves(Random random, int localX, int localY, int localZ, int range, boolean large) {
        return false;
    }

    private static void createFrond(BlockPos start, TestableWorld world, BlockPlacer placer, Random random,
                                    TreeFeatureConfig config, int dx, int dz) {
        BlockPos.Mutable pos = start.mutableCopy();

        // 第一步：向下 1 格
        pos.set(start.getX() + dx, start.getY() - 1, start.getZ() + dz);
        placeLeafWithCoconut(world, placer, random, config, pos);

        // 第二步：水平延伸 2 格
        for (int i = 2; i <= 3; i++) {
            pos.set(start.getX() + dx * i, start.getY() - 1, start.getZ() + dz * i);
            placeLeafWithCoconut(world, placer, random, config, pos);
        }

        // 第三步：尖端再低 1 格
        pos.set(start.getX() + dx * 4, start.getY() - 2, start.getZ() + dz * 4);
        placeLeafWithCoconut(world, placer, random, config, pos);
    }

    private static void placeLeafWithCoconut(TestableWorld world, BlockPlacer placer, Random random,
                                             TreeFeatureConfig config, BlockPos pos) {
        BlockState leaf = config.foliageProvider.get(random, pos);
        if (leaf.contains(CDLeavesBlock.GENERATED)) {
            leaf = leaf.with(CDLeavesBlock.GENERATED, true);
        }
        placer.placeBlock(pos, leaf);

        // 20% 概率在树叶下挂椰子
        if (random.nextInt(5) == 0) {
            BlockPos below = pos.down();
            if (world.testBlockState(below, BlockState::isAir)) {
                placer.placeBlock(below, CDModBlocks.COCONUT.getDefaultState().with(CoconutBlock.HANGING, true));
            }
        }
    }
}
