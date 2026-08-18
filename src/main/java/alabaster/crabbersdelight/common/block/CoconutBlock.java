package alabaster.crabbersdelight.common.block;

import alabaster.crabbersdelight.common.registry.CDDamageSources;
import alabaster.crabbersdelight.common.registry.CDModItems;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FallingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.List;

public class CoconutBlock extends FallingBlock {
    public static final BooleanProperty HANGING = BooleanProperty.of("hanging");
    private static final VoxelShape SHAPE = createCuboidShape(3.0D, 0.0D, 3.0D, 13.0D, 10.0D, 13.0D);

    public CoconutBlock(AbstractBlock.Settings settings) {
        super(settings);
        this.setDefaultState(this.getDefaultState().with(HANGING, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(HANGING);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if (state.get(HANGING)) {
            return createCuboidShape(3.0D, 5.0D, 3.0D, 13.0D, 15.0D, 13.0D);
        }
        return SHAPE;
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction facing, BlockState neighborState,
                                                WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (facing == Direction.UP) {
            boolean hanging = !neighborState.isAir();
            if (!hanging) {
                world.scheduleBlockTick(pos, this, 1);
            }
            return state.with(HANGING, hanging);
        }
        return super.getStateForNeighborUpdate(state, facing, neighborState, world, pos, neighborPos);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        World world = context.getWorld();
        BlockPos above = context.getBlockPos().up();
        BlockState aboveState = world.getBlockState(above);
        boolean hanging = !aboveState.isAir();
        return this.getDefaultState().with(HANGING, hanging);
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        boolean hanging = state.get(HANGING);
        if (!hanging) {
            super.scheduledTick(state, world, pos, random);
        }
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        // 不产生粒子
    }

    @Override
    public void onLanding(World world, BlockPos pos, BlockState state, BlockState replaceState,
                          FallingBlockEntity fallingEntity) {
        super.onLanding(world, pos, state, replaceState, fallingEntity);

        if (!world.isClient && fallingEntity != null) {
            boolean hitPlayer = false;
            List<Entity> entities = world.getOtherEntities(null, SHAPE.getBoundingBox().offset(pos));

            for (Entity entity : entities) {
                if (entity instanceof LivingEntity living) {
                    living.damage(CDDamageSources.getSimpleDamageSource(world, CDDamageSources.FALLING_COCONUT), 2.0F);

                    if (entity instanceof PlayerEntity player) {
                        world.playSound(null, entity.getX(), entity.getY(), entity.getZ(),
                                SoundEvents.ENTITY_GOAT_HORN_BREAK, SoundCategory.PLAYERS, 0.7F, 0.5F);

                        player.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 200, 0));
                        player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 200, 2));

                        ItemStack headSlot = player.getEquippedStack(EquipmentSlot.HEAD);
                        if (headSlot.isEmpty()) {
                            ItemStack coconutHelmet = new ItemStack(CDModItems.COCONUT_HELMET);
                            player.equipStack(EquipmentSlot.HEAD, coconutHelmet);
                            world.playSound(null, player.getX(), player.getY(), player.getZ(),
                                    SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, SoundCategory.PLAYERS, 1.0F, 1.0F);
                            world.breakBlock(pos, false);
                        }

                        hitPlayer = true;
                    }
                }
            }

            if (hitPlayer) {
                world.breakBlock(pos, true);
            }
        }
    }

    @Override
    public void onProjectileHit(World world, BlockState state, BlockHitResult hit, ProjectileEntity projectile) {
        super.onProjectileHit(world, state, hit, projectile);

        if (!world.isClient) {
            BlockPos pos = hit.getBlockPos();
            if (state.get(HANGING)) {
                world.setBlockState(pos, state.with(HANGING, false), Block.NOTIFY_ALL);
                world.scheduleBlockTick(pos, this, 1);
            }
        }
    }
}
