package alabaster.crabbersdelight.common.worldgen.tree;

import alabaster.crabbersdelight.common.block.CoconutBlock;
import alabaster.crabbersdelight.common.registry.CDModBlocks;
import alabaster.crabbersdelight.common.registry.CDTrunkPlacerTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.TestableWorld;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.foliage.FoliagePlacer;
import net.minecraft.world.gen.trunk.TrunkPlacer;
import net.minecraft.world.gen.trunk.TrunkPlacerType;

import java.util.List;
import java.util.function.BiConsumer;

public class PalmTrunkPlacer extends TrunkPlacer {
    public static final Codec<PalmTrunkPlacer> CODEC = RecordCodecBuilder.create(instance ->
            fillTrunkPlacerFields(instance)
                    .and(IntProvider.VALUE_CODEC.fieldOf("bend_length").forGetter(p -> p.bendLength))
                    .apply(instance, PalmTrunkPlacer::new));

    private final IntProvider bendLength;

    public PalmTrunkPlacer(int baseHeight, int firstRandomHeight, int secondRandomHeight, IntProvider bendLength) {
        super(baseHeight, firstRandomHeight, secondRandomHeight);
        this.bendLength = bendLength;
    }

    @Override
    protected TrunkPlacerType<?> getType() {
        return CDTrunkPlacerTypes.PALM;
    }

    @Override
    public List<FoliagePlacer.TreeNode> generate(TestableWorld world, BiConsumer<BlockPos, BlockState> replacer,
                                                 Random random, int height, BlockPos pos, TreeFeatureConfig config) {
        int dx = random.nextBoolean() ? 1 : -1;
        int dz = random.nextBoolean() ? 1 : -1;
        int bend = bendLength.get(random);

        BlockPos.Mutable cursor = pos.mutableCopy();
        for (int y = 0; y < height; y++) {
            if (y > height / 3 && bend > 0) {
                if (random.nextInt(3) == 0) {
                    cursor.move(dx, 0, dz);
                    bend--;
                }
            }
            getAndSetState(world, replacer, random, cursor, config);
            cursor.move(0, 1, 0);
        }

        // 低概率在树根周围生成椰子
        final int CHANCE = 20;
        final int RADIUS = 3;
        final int MAX_COCONUTS = 3;
        final int MAX_ATTEMPTS = 10;
        final int MAX_DESCEND = 6;

        if (random.nextInt(CHANCE) == 0) {
            int coconutCount = 1 + random.nextInt(MAX_COCONUTS);
            for (int i = 0; i < coconutCount; i++) {
                for (int attempt = 0; attempt < MAX_ATTEMPTS; attempt++) {
                    int rx = pos.getX() + random.nextInt(RADIUS * 2 + 1) - RADIUS;
                    int rz = pos.getZ() + random.nextInt(RADIUS * 2 + 1) - RADIUS;
                    BlockPos candidate = new BlockPos(rx, pos.getY(), rz);
                    boolean placed = false;
                    for (int dy = 0; dy >= -MAX_DESCEND; dy--) {
                        BlockPos target = candidate.add(0, dy, 0);
                        if (world.testBlockState(target, BlockState::isAir)
                                && !world.testBlockState(target.down(), BlockState::isAir)) {
                            replacer.accept(target, CDModBlocks.COCONUT.getDefaultState()
                                    .with(CoconutBlock.HANGING, false));
                            placed = true;
                            break;
                        }
                    }
                    if (placed) break;
                }
            }
        }

        return List.of(new FoliagePlacer.TreeNode(cursor, 0, false));
    }
}
