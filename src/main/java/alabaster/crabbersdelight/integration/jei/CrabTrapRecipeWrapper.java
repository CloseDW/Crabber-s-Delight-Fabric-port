package alabaster.crabbersdelight.integration.jei;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;

// 一个“鱼饵 → 可能掉落的战利品”的展示条目
public class CrabTrapRecipeWrapper {
    private final ItemStack input;
    private final Ingredient output;

    public CrabTrapRecipeWrapper(ItemStack input, Ingredient output) {
        this.input = input;
        this.output = output;
    }

    public ItemStack getInput() {
        return input;
    }

    public Ingredient getOutput() {
        return output;
    }
}
