package alabaster.crabbersdelight.common.item;

import net.minecraft.item.Item;

// 鱼饵桶：有 48 点耐久，用于捕蟹笼时每次成功收获消耗 1 点，
// 耐久耗尽变成空桶（见 CrabTrapItemHandler 里的逻辑）。
public class ChumItem extends Item {
    public ChumItem(Item.Settings settings) {
        super(settings.maxDamage(48));
    }
}
