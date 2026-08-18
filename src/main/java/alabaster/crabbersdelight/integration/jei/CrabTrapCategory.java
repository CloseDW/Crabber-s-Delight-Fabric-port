package alabaster.crabbersdelight.integration.jei;

import alabaster.crabbersdelight.CrabbersDelightFabric;
import alabaster.crabbersdelight.common.Config;
import alabaster.crabbersdelight.common.registry.CDModItems;
import com.google.common.collect.ImmutableList;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CrabTrapCategory implements IRecipeCategory<CrabTrapRecipeWrapper> {
    private static final Identifier CRAB_TRAP_LOCATION =
            CrabbersDelightFabric.id("textures/gui/jei_crab_trap.png");
    private final IDrawable background;
    private final IDrawable icon;
    private final Text title;

    public CrabTrapCategory(IGuiHelper helper) {
        this.title = Text.translatable("block.crabbersdelight.crab_trap");
        this.background = helper.createDrawable(CRAB_TRAP_LOCATION, 0, 0, 79, 39);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(CDModItems.CRAB_TRAP));
    }

    @Override
    public RecipeType<CrabTrapRecipeWrapper> getRecipeType() {
        return JEIPlugin.CRAB_TRAP_RECIPE;
    }

    @Override
    public Text getTitle() {
        return this.title;
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    private static boolean iconPosition(double mouseX, double mouseY) {
        int iconPosX = 32;
        int iconPosY = 2;
        int iconHeight = 10;
        int iconWidth = 15;
        return iconPosX <= mouseX && mouseX < iconPosX + iconWidth
                && iconPosY <= mouseY && mouseY < iconPosY + iconHeight;
    }

    @Override
    public List<Text> getTooltipStrings(CrabTrapRecipeWrapper recipe, IRecipeSlotsView recipeSlotsView,
                                        double mouseX, double mouseY) {
        if (iconPosition(mouseX, mouseY)) {
            int min = Config.MIN_TICKS.get();
            int max = Config.MAX_TICKS.get();
            if (max > min) {
                return ImmutableList.of(Text.literal("Collects every " + min + "-" + max + " ticks"));
            }
            return ImmutableList.of(Text.literal("Error: Minimum value is higher than maximum value!")
                    .formatted(Formatting.RED));
        }
        return Collections.emptyList();
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, CrabTrapRecipeWrapper recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 7, 16).addItemStack(recipe.getInput());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 57, 16)
                .addItemStacks(Arrays.stream(recipe.getOutput().getMatchingStacks()).toList());
    }
}
