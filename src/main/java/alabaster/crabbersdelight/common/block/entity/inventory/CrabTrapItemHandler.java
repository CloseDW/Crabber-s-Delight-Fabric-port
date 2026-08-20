package alabaster.crabbersdelight.common.block.entity.inventory;

import alabaster.crabbersdelight.common.tags.CDModTags;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import java.util.List;

public class CrabTrapItemHandler extends SimpleInventory {
    public CrabTrapItemHandler() {
        super(10);
    }

    // 把战利品插入槽位并消耗鱼饵
    public void addItemsAndShrinkBait(World world, BlockPos pos, List<ItemStack> lootList,
                                      ItemStack baitItem, Random random) {
        boolean insertedAny = false;

        for (ItemStack lootStack : lootList) {
            if (lootStack.isEmpty()) continue;

            for (int slot = 1; slot < this.size(); slot++) {
                ItemStack current = this.getStack(slot);
                if (!current.isEmpty() && !ItemStack.areItemsEqual(current, lootStack)) continue;

                int limit = this.getMaxCountPerStack();
                int space = limit - current.getCount();
                if (space <= 0) continue;

                int amount = Math.min(space, lootStack.getCount());
                if (current.isEmpty()) {
                    this.setStack(slot, lootStack.copyWithCount(amount));
                } else {
                    current.increment(amount);
                }
                lootStack.decrement(amount);
                insertedAny = true;
                if (lootStack.isEmpty()) break;
            }

            if (!lootStack.isEmpty()) continue;

            // 成功放入战利品后消耗鱼饵
            if (baitItem.isIn(CDModTags.CRAB_TRAP_BAIT) && !baitItem.isIn(CDModTags.CREATURE_CHUMS)) {
                baitItem.decrement(1);
            } else if (baitItem.isIn(CDModTags.CREATURE_CHUMS)) {
                baitItem.damage(1, random, null);
                if (baitItem.getDamage() == 48) {
                    baitItem.decrement(1);
                    this.setStack(0, new ItemStack(Items.BUCKET));
                }
            }
        }

        if (insertedAny) {
            world.playSound(null,
                    pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D,
                    SoundEvents.ENTITY_FISH_SWIM, SoundCategory.BLOCKS,
                    0.5F, 1.0F);
        }
    }

    @Override
    public boolean isValid(int slot, ItemStack stack) {
        if (slot == 0) {
            return stack.isIn(CDModTags.CRAB_TRAP_BAIT);
        }
        return true;
    }

    public DefaultedList<ItemStack> getItems() {
        return this.stacks;
    }

    /*
     * 覆写序列化：SimpleInventory的toNbtList丢失槽位信息
     */
    @Override
    public NbtList toNbtList() {
        NbtList nbtList = new NbtList();
        for (int i = 0; i < this.size(); i++) {
            ItemStack stack = this.getStack(i);
            if (!stack.isEmpty()) {
                NbtCompound nbt = new NbtCompound();
                nbt.putInt("Slot", i);
                stack.writeNbt(nbt);
                nbtList.add(nbt);
            }
        }
        return nbtList;
    }

    /*
     * 覆写反序列化：按保存的槽位索引逐格恢复
     */
    @Override
    public void readNbtList(NbtList nbtList) {
        this.clear();
        for (int i = 0; i < nbtList.size(); i++) {
            NbtCompound nbt = nbtList.getCompound(i);
            int slot = nbt.getInt("Slot");
            if (slot >= 0 && slot < this.size()) {
                this.setStack(slot, ItemStack.fromNbt(nbt));
            }
        }
    }
}
