package alabaster.crabbersdelight.common.block.entity;

import alabaster.crabbersdelight.CrabbersDelightFabric;
import alabaster.crabbersdelight.common.Config;
import alabaster.crabbersdelight.common.block.container.CrabTrapMenu;
import alabaster.crabbersdelight.common.block.entity.inventory.CrabTrapItemHandler;
import alabaster.crabbersdelight.common.registry.CDModBlockEntity;
import alabaster.crabbersdelight.common.tags.CDModTags;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootDataKey;
import net.minecraft.loot.LootDataType;
import net.minecraft.loot.LootManager;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.registry.Registries;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CrabTrapBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory, SidedInventory {
    public static final Text CRAB_TRAP_NAME = Text.translatable("block.crabbersdelight.crab_trap");

    private final CrabTrapItemHandler inventory = new CrabTrapItemHandler() {
        @Override
        public void markDirty() {
            super.markDirty();
            CrabTrapBlockEntity.this.markDirty();
        }
    };

    private int tickCounter = 0;

    public CrabTrapBlockEntity(BlockPos pos, BlockState state) {
        super(CDModBlockEntity.CRAB_TRAP, pos, state);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.put("handler", this.inventory.toNbtList());
        nbt.putInt("tickCounter", tickCounter);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.inventory.readNbtList(nbt.getList("handler", 10));
        this.tickCounter = nbt.getInt("tickCounter");
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return this.createNbt();
    }

    public static void serverTick(World world, BlockPos pos, BlockState state, CrabTrapBlockEntity blockEntity) {
        Random random = world.getRandom();
        int min = Config.MIN_TICKS.get();
        int max = Config.MAX_TICKS.get();
        if (max > min) {
            if (blockEntity.tickCounter >= random.nextBetween(min, max)) {
                blockEntity.tickCounter = 0;
                if (isSurroundedByWater(world, pos)) {
                    if (isValidFishingLocation(world, pos)) {
                        LootContextParameterSet lootParams = new LootContextParameterSet.Builder((ServerWorld) world)
                                .add(LootContextParameters.ORIGIN, new Vec3d(pos.getX(), pos.getY(), pos.getZ()))
                                .add(LootContextParameters.TOOL, ItemStack.EMPTY)
                                .build(LootContextTypes.FISHING);

                        ItemStack itemInBaitSlot = blockEntity.inventory.getStack(0);
                        if (itemInBaitSlot.isIn(CDModTags.CRAB_TRAP_BAIT)) {
                            Identifier registryName = Registries.ITEM.getId(itemInBaitSlot.getItem());
                            Identifier lootTableLocation = CrabbersDelightFabric.id(
                                    "gameplay/crab_trap_loot/" + registryName.getNamespace() + "/" + registryName.getPath());
                            LootDataKey<LootTable> key = new LootDataKey<>(LootDataType.LOOT_TABLES, lootTableLocation);
                            LootTable lootTable = world.getServer().getLootManager().getElement(key);
                            if (lootTable != null) {
                                List<ItemStack> list = lootTable.generateLoot(lootParams);
                                blockEntity.inventory.addItemsAndShrinkBait(world, pos, list, itemInBaitSlot, random);
                            }
                        }
                    }
                }
            } else {
                if (isWaterBiome(world, pos)) {
                    blockEntity.tickCounter++;
                }
                blockEntity.tickCounter++;
            }
        } else {
            CrabbersDelightFabric.LOGGER.error("Error: Minimum value is higher than maximum value!");
        }
    }

    private static boolean isValidFishingLocation(World world, BlockPos pos) {
        for (Direction direction : Direction.values()) {
            if (world.getBlockState(pos).getFluidState().isIn(FluidTags.WATER)) {
                if (world.getFluidState(pos.offset(direction)).isIn(FluidTags.WATER)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isSurroundedByWater(World world, BlockPos pos) {
        if (Config.REQUIRE_SURROUNDING_WATER.get()) {
            for (BlockPos nearbyPos : BlockPos.stream(pos.add(-1, 0, -1), pos.add(1, 0, 1)).toList()) {
                if (!world.getFluidState(nearbyPos).isIn(FluidTags.WATER)) {
                    return false;
                }
            }
            return true;
        }
        return true;
    }

    private static boolean isWaterBiome(World world, BlockPos pos) {
        return world.getBiome(pos).isIn(BiomeTags.IS_OCEAN);
    }

    public CrabTrapItemHandler getInventory() {
        return this.inventory;
    }

    // ===== SidedInventory（漏斗适配） =====
    // 槽位：0 = 诱饵，1-9 = 捕获物。
    // 漏斗从上方/侧面输入诱饵（槽 0），从下方抽取捕获物（槽 1-9），与 Forge 的 input/output RangedWrapper 一致。

    @Override
    public int[] getAvailableSlots(Direction side) {
        if (side == Direction.DOWN) {
            // 下方漏斗：抽取捕获物槽
            return new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9};
        }
        // 上方/侧面漏斗：只输入诱饵槽
        return new int[]{0};
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
        return dir != Direction.DOWN && slot == 0 && this.inventory.isValid(slot, stack);
    }

    @Override
    public boolean isValid(int slot, ItemStack stack) {
        return this.inventory.isValid(slot, stack);
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return dir == Direction.DOWN && slot != 0;
    }

    @Override
    public int size() {
        return this.inventory.size();
    }

    @Override
    public boolean isEmpty() {
        return this.inventory.isEmpty();
    }

    @Override
    public ItemStack getStack(int slot) {
        return this.inventory.getStack(slot);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        return this.inventory.removeStack(slot, amount);
    }

    @Override
    public ItemStack removeStack(int slot) {
        return this.inventory.removeStack(slot);
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        this.inventory.setStack(slot, stack);
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        if (this.world == null || this.world.getBlockEntity(this.pos) != this) {
            return false;
        }
        return player.squaredDistanceTo(this.pos.getX() + 0.5, this.pos.getY() + 0.5, this.pos.getZ() + 0.5) <= 64.0;
    }

    @Override
    public void clear() {
        this.inventory.clear();
    }

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new CrabTrapMenu(syncId, playerInventory, this.inventory);
    }

    @Override
    public Text getDisplayName() {
        return CRAB_TRAP_NAME;
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        // 客户端会创建新的空物品栏，不需要传输数据
    }
}
