package alabaster.crabbersdelight.common.block.container;

import alabaster.crabbersdelight.CrabbersDelightFabric;
import alabaster.crabbersdelight.common.block.entity.inventory.CrabTrapItemHandler;
import alabaster.crabbersdelight.common.registry.CDModMenus;
import alabaster.crabbersdelight.common.tags.CDModTags;
import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Identifier;

public class CrabTrapMenu extends ScreenHandler {
    public final CrabTrapItemHandler inventory;
    public static final Identifier BAIT_SLOT = CrabbersDelightFabric.id("gui/bait_slot");

    public CrabTrapMenu(int syncId, PlayerInventory playerInv, CrabTrapItemHandler inventory) {
        super(CDModMenus.CRAB_TRAP_MENU, syncId);
        this.inventory = inventory;

        int startX = 8;
        int startY = 13;
        int borderSlotSize = 18;

        // 鱼饵槽（0 号）
        this.addSlot(new Slot(inventory, 0, 80, 13) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.isIn(CDModTags.CRAB_TRAP_BAIT);
            }

            // 空槽时显示鱼饵图标（来自方块图集，配合 assets/minecraft/atlases/blocks.json）
            @Override
            public Pair<Identifier, Identifier> getBackgroundSprite() {
                return Pair.of(new Identifier("textures/atlas/blocks.png"), BAIT_SLOT);
            }
        });

        // 战利品第一行：每格最多 8 个
        for (int column = 0; column < 9; ++column) {
            final int slotIndex = column + 1;
            this.addSlot(new Slot(inventory, slotIndex, 8 + column * 18, 34) {
                @Override
                public int getMaxItemCount() {
                    return 8;
                }

                @Override
                public int getMaxItemCount(ItemStack stack) {
                    return 8;
                }
            });
        }

        // 玩家背包
        int startPlayerInvY = 65;
        for (int row = 0; row < 3; ++row) {
            for (int column = 0; column < 9; ++column) {
                this.addSlot(new Slot(playerInv, 9 + (row * 9) + column, startX + (column * borderSlotSize),
                        startPlayerInvY + (row * borderSlotSize)));
            }
        }

        // 快捷栏
        for (int column = 0; column < 9; ++column) {
            this.addSlot(new Slot(playerInv, column, startX + (column * borderSlotSize), 123));
        }
    }

    public CrabTrapMenu(int syncId, PlayerInventory playerInventory, PacketByteBuf data) {
        this(syncId, playerInventory, new CrabTrapItemHandler());
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasStack()) {
            ItemStack itemStack1 = slot.getStack();
            itemStack = itemStack1.copy();
            if (index < this.inventory.size()) {
                if (!this.insertItem(itemStack1, this.inventory.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(itemStack1, 0, this.inventory.size(), false)) {
                return ItemStack.EMPTY;
            }

            if (itemStack1.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }
        return itemStack;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }
}
