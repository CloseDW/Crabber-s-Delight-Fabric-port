package alabaster.crabbersdelight.integration.jei;

import alabaster.crabbersdelight.CrabbersDelightFabric;
import alabaster.crabbersdelight.common.registry.CDModItems;
import alabaster.crabbersdelight.common.tags.CDModTags;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

// JEI 集成：给捕蟹笼做一个“鱼饵 → 战利品”配方类别。
// 每个属于 crab_trap_bait 标签的鱼饵，如果在 jei_display_results/<ns>/<path> 标签里有掉落表，就生成一条展示。
// Fabric 通过 fabric.mod.json 的 "jei_mod_plugin" 入口点发现本插件。
@JeiPlugin
public class JEIPlugin implements IModPlugin {
    private static final Identifier PLUGIN_ID = CrabbersDelightFabric.id("jei_plugin");
    public static final RecipeType<CrabTrapRecipeWrapper> CRAB_TRAP_RECIPE =
            RecipeType.create(CrabbersDelightFabric.MOD_ID, "crab_trap_loot", CrabTrapRecipeWrapper.class);

    @Override
    public Identifier getPluginUid() {
        return PLUGIN_ID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new CrabTrapCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(CRAB_TRAP_RECIPE, addWrappers());
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(CDModItems.CRAB_TRAP), CRAB_TRAP_RECIPE);
    }

    public List<CrabTrapRecipeWrapper> addWrappers() {
        List<CrabTrapRecipeWrapper> list = new ArrayList<>();
        for (Item item : Registries.ITEM) {
            ItemStack stack = new ItemStack(item);
            if (stack.isIn(CDModTags.CRAB_TRAP_BAIT)) {
                Identifier registryName = Registries.ITEM.getId(item);
                TagKey<Item> outputTag = TagKey.of(RegistryKeys.ITEM,
                        CrabbersDelightFabric.id("jei_display_results/"
                                + registryName.getNamespace() + "/" + registryName.getPath()));
                if (Registries.ITEM.getEntryList(outputTag).isPresent()) {
                    list.add(new CrabTrapRecipeWrapper(stack, Ingredient.fromTag(outputTag)));
                }
            }
        }
        return list;
    }
}
