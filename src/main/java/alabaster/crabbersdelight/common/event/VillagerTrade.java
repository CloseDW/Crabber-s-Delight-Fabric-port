package alabaster.crabbersdelight.common.event;

import alabaster.crabbersdelight.common.Config;
import alabaster.crabbersdelight.common.registry.CDModItems;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.VillagerProfession;

public class VillagerTrade {
    public static void register() {
        // 渔民收购海鲜，付给绿宝石（对应 Forge VillagerTradesEvent + emeraldForItemsTrade）
        if (Config.FISHERMAN_BUY_SEAFOOD.get()) {
            TradeOfferHelper.registerVillagerOffers(VillagerProfession.FISHERMAN, 1, factories -> {
                factories.add(emeraldForItemsTrade(CDModItems.CRAB, 6, 16, 2));
                factories.add(emeraldForItemsTrade(CDModItems.RAW_SHRIMP, 8, 16, 2));
            });
            TradeOfferHelper.registerVillagerOffers(VillagerProfession.FISHERMAN, 2, factories -> {
                factories.add(emeraldForItemsTrade(CDModItems.RAW_CLAWSTER, 4, 16, 5));
                factories.add(emeraldForItemsTrade(CDModItems.CLAM, 3, 16, 5));
            });
            TradeOfferHelper.registerVillagerOffers(VillagerProfession.FISHERMAN, 4, factories -> {
                factories.add(emeraldForItemsTrade(CDModItems.PEARL, 1, 16, 5));
            });
        }

        // 流浪商人用珍珠换宝藏（对应 Forge WandererTradesEvent）
        if (Config.WANDERING_TRADER_PEARLS.get()) {
            TradeOfferHelper.registerWanderingTraderOffers(1, factories -> {
                factories.add(heartForPearlsTrade(CDModItems.PEARL, 32, 4, 12));
                factories.add(spongeForPearlsTrade(CDModItems.PEARL, 8, 4, 12));
                factories.add(tridentForPearlsTrade(CDModItems.PEARL, 64, 1, 12));
            });
        }
    }

    // 渔民：支付 count 个海鲜，换取 1 个绿宝石
    private static TradeOffers.Factory emeraldForItemsTrade(Item item, int count, int maxTrades, int xp) {
        return (entity, random) -> new TradeOffer(
                new ItemStack(item, count), ItemStack.EMPTY, new ItemStack(Items.EMERALD),
                maxTrades, xp, 0.05F);
    }

    // 流浪商人：支付 count 个珍珠，换取海洋之心
    private static TradeOffers.Factory heartForPearlsTrade(Item item, int count, int maxTrades, int xp) {
        return (entity, random) -> new TradeOffer(
                new ItemStack(item, count), ItemStack.EMPTY, new ItemStack(Items.HEART_OF_THE_SEA),
                maxTrades, xp, 0.05F);
    }

    // 流浪商人：支付 count 个珍珠，换取海绵
    private static TradeOffers.Factory spongeForPearlsTrade(Item item, int count, int maxTrades, int xp) {
        return (entity, random) -> new TradeOffer(
                new ItemStack(item, count), ItemStack.EMPTY, new ItemStack(Items.SPONGE),
                maxTrades, xp, 0.05F);
    }

    // 流浪商人：支付 count 个珍珠，换取三叉戟
    private static TradeOffers.Factory tridentForPearlsTrade(Item item, int count, int maxTrades, int xp) {
        return (entity, random) -> new TradeOffer(
                new ItemStack(item, count), ItemStack.EMPTY, new ItemStack(Items.TRIDENT),
                maxTrades, xp, 0.05F);
    }
}
