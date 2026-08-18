package alabaster.crabbersdelight.common.entity.crab;

import alabaster.crabbersdelight.CrabbersDelightFabric;
import com.google.common.collect.Maps;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

import java.util.Map;

public class CrabRenderer extends MobEntityRenderer<CrabEntity, CrabModel<CrabEntity>> {
    private static final Map<CrabVariant, Identifier> TEXTURE_BY_VARIANT =
            Maps.newEnumMap(CrabVariant.class);

    static {
        TEXTURE_BY_VARIANT.put(CrabVariant.BLACK, new Identifier(CrabbersDelightFabric.MOD_ID, "textures/entity/black_crab.png"));
        TEXTURE_BY_VARIANT.put(CrabVariant.BLUE, new Identifier(CrabbersDelightFabric.MOD_ID, "textures/entity/blue_crab.png"));
        TEXTURE_BY_VARIANT.put(CrabVariant.BROWN, new Identifier(CrabbersDelightFabric.MOD_ID, "textures/entity/brown_crab.png"));
        TEXTURE_BY_VARIANT.put(CrabVariant.CYAN, new Identifier(CrabbersDelightFabric.MOD_ID, "textures/entity/cyan_crab.png"));
        TEXTURE_BY_VARIANT.put(CrabVariant.GRAY, new Identifier(CrabbersDelightFabric.MOD_ID, "textures/entity/gray_crab.png"));
        TEXTURE_BY_VARIANT.put(CrabVariant.GREEN, new Identifier(CrabbersDelightFabric.MOD_ID, "textures/entity/green_crab.png"));
        TEXTURE_BY_VARIANT.put(CrabVariant.LIGHT_BLUE, new Identifier(CrabbersDelightFabric.MOD_ID, "textures/entity/light_blue_crab.png"));
        TEXTURE_BY_VARIANT.put(CrabVariant.LIGHT_GRAY, new Identifier(CrabbersDelightFabric.MOD_ID, "textures/entity/light_gray_crab.png"));
        TEXTURE_BY_VARIANT.put(CrabVariant.LIME, new Identifier(CrabbersDelightFabric.MOD_ID, "textures/entity/lime_crab.png"));
        TEXTURE_BY_VARIANT.put(CrabVariant.MAGENTA, new Identifier(CrabbersDelightFabric.MOD_ID, "textures/entity/magenta_crab.png"));
        TEXTURE_BY_VARIANT.put(CrabVariant.ORANGE, new Identifier(CrabbersDelightFabric.MOD_ID, "textures/entity/orange_crab.png"));
        TEXTURE_BY_VARIANT.put(CrabVariant.PINK, new Identifier(CrabbersDelightFabric.MOD_ID, "textures/entity/pink_crab.png"));
        TEXTURE_BY_VARIANT.put(CrabVariant.PURPLE, new Identifier(CrabbersDelightFabric.MOD_ID, "textures/entity/purple_crab.png"));
        TEXTURE_BY_VARIANT.put(CrabVariant.RED, new Identifier(CrabbersDelightFabric.MOD_ID, "textures/entity/red_crab.png"));
        TEXTURE_BY_VARIANT.put(CrabVariant.WHITE, new Identifier(CrabbersDelightFabric.MOD_ID, "textures/entity/white_crab.png"));
        TEXTURE_BY_VARIANT.put(CrabVariant.YELLOW, new Identifier(CrabbersDelightFabric.MOD_ID, "textures/entity/yellow_crab.png"));
    }

    public CrabRenderer(EntityRendererFactory.Context context) {
        super(context, new CrabModel<>(context.getPart(CrabModel.LAYER_LOCATION)), 0.3f);
    }

    @Override
    public Identifier getTexture(CrabEntity entity) {
        return TEXTURE_BY_VARIANT.get(entity.getVariant());
    }

    @Override
    public void render(CrabEntity entity, float entityYaw, float partialTicks, MatrixStack poseStack,
                       VertexConsumerProvider buffer, int packedLight) {
        if (entity.isBaby()) {
            poseStack.scale(0.5f, 0.5f, 0.5f);
        } else {
            poseStack.scale(1f, 1f, 1f);
        }

        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
}
