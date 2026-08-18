package alabaster.crabbersdelight.common.worldgen;

import alabaster.crabbersdelight.common.Config;
import alabaster.crabbersdelight.common.block.SeashellBlock;
import alabaster.crabbersdelight.common.registry.CDModBlocks;
import alabaster.crabbersdelight.common.registry.CDStateProviders;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.stateprovider.BlockStateProviderType;

// 对应 Forge 的 RandomSeashellStateProvider：随机选择贝壳变体和朝向，
// 用于沙滩/水下生成（configured_feature JSON 里通过 codec 反序列化）。
public class RandomSeashellStateProvider extends BlockStateProvider {
    private final boolean waterlogged;

    public RandomSeashellStateProvider(boolean waterlogged) {
        this.waterlogged = waterlogged;
    }

    public static final Codec<RandomSeashellStateProvider> CODEC =
            RecordCodecBuilder.create(instance ->
                    instance.group(Codec.BOOL.optionalFieldOf("waterlogged", false)
                            .forGetter(p -> p.waterlogged)).apply(instance, RandomSeashellStateProvider::new));

    @Override
    protected BlockStateProviderType<?> getType() {
        return CDStateProviders.RANDOM_SEASHELL;
    }

    @Override
    public BlockState get(Random random, BlockPos pos) {
        int variant = random.nextInt(Config.SEASHELL_VARIANT_COUNT.get());
        Direction facing = Direction.Type.HORIZONTAL.random(random);

        return CDModBlocks.SEASHELLS.getDefaultState()
                .with(SeashellBlock.VARIANT, variant)
                .with(SeashellBlock.FACING, facing)
                .with(SeashellBlock.WATERLOGGED, waterlogged);
    }
}
