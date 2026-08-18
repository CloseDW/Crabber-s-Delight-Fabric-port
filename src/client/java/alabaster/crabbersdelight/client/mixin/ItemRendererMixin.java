package alabaster.crabbersdelight.client.mixin;

import alabaster.crabbersdelight.common.registry.CDModItems;
import net.minecraft.client.render.item.ItemModels;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.util.Identifier;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

// 原版三叉戟/望远镜就是这样做的：GUI/地面/固定视角渲染 2D 扁平模型，手持才用 3D 模型。
// 这里在 renderItem 里把“即将用于渲染的 BakedModel”按物品和视角切换模型：
//   - 蟹钳：GUI/地面/固定视角 → 2D 平面模型（手持仍是 3D）
//   - 椰壳头盔：HEAD 视角（戴在头上）→ 3D 椰壳模型（手持/背包仍是 2D 图标）
// 这些额外模型通过 ModelLoadingPlugin.addModels 预烘焙。
@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {
    private static final ModelIdentifier CRAB_CLAW_2D =
            new ModelIdentifier(new Identifier("crabbersdelight", "crab_claw_2d"), "inventory");
    private static final ModelIdentifier COCONUT_HELMET_3D =
            new ModelIdentifier(new Identifier("crabbersdelight", "coconut_helmet_3d"), "inventory");

    @Shadow
    @Final
    private ItemModels models;

    @ModifyVariable(
            method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V",
            at = @At("HEAD"),
            argsOnly = true)
    private BakedModel crabbersdelight$switchPerspective(BakedModel model, ItemStack stack, ModelTransformationMode mode) {
        if ((mode == ModelTransformationMode.GUI
                || mode == ModelTransformationMode.GROUND
                || mode == ModelTransformationMode.FIXED)
                && stack.isOf(CDModItems.CRAB_CLAW)) {
            return this.models.getModelManager().getModel(CRAB_CLAW_2D);
        }
        if (mode == ModelTransformationMode.HEAD && stack.isOf(CDModItems.COCONUT_HELMET)) {
            return this.models.getModelManager().getModel(COCONUT_HELMET_3D);
        }
        return model;
    }
}
