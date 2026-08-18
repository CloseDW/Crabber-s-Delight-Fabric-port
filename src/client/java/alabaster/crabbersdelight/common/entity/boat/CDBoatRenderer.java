package alabaster.crabbersdelight.common.entity.boat;

import alabaster.crabbersdelight.CrabbersDelightFabric;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.BoatEntityModel;
import net.minecraft.client.render.entity.model.ChestBoatEntityModel;
import net.minecraft.client.render.entity.model.CompositeEntityModel;
import net.minecraft.client.render.entity.model.ModelWithWaterPatch;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.joml.Quaternionf;

public class CDBoatRenderer extends EntityRenderer<BoatEntity> {
    private final CompositeEntityModel<BoatEntity> model;
    private final Identifier texture;

    public CDBoatRenderer(EntityRendererFactory.Context context, boolean chestBoat) {
        super(context);
        this.shadowRadius = 0.8f;
        this.model = chestBoat
                ? new ChestBoatEntityModel(context.getPart(CDBoatModelLayers.PALM_CHEST_BOAT_LAYER))
                : new BoatEntityModel(context.getPart(CDBoatModelLayers.PALM_BOAT_LAYER));
        this.texture = new Identifier(CrabbersDelightFabric.MOD_ID,
                "textures/entity/" + (chestBoat ? "chest_boat/" : "boat/") + "palm.png");
    }

    @Override
    public void render(BoatEntity boatEntity, float f, float g, MatrixStack matrixStack,
                       VertexConsumerProvider vertexConsumerProvider, int i) {
        float k;
        matrixStack.push();
        matrixStack.translate(0.0f, 0.375f, 0.0f);
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0f - f));
        float h = (float) boatEntity.getDamageWobbleTicks() - g;
        float j = boatEntity.getDamageWobbleStrength() - g;
        if (j < 0.0f) {
            j = 0.0f;
        }
        if (h > 0.0f) {
            matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(
                    MathHelper.sin(h) * h * j / 10.0f * (float) boatEntity.getDamageWobbleSide()));
        }
        if (!MathHelper.approximatelyEquals(k = boatEntity.interpolateBubbleWobble(g), 0.0f)) {
            matrixStack.multiply(new Quaternionf().setAngleAxis(
                    boatEntity.interpolateBubbleWobble(g) * ((float) Math.PI / 180), 1.0f, 0.0f, 1.0f));
        }
        matrixStack.scale(-1.0f, -1.0f, 1.0f);
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(90.0f));
        this.model.setAngles(boatEntity, g, 0.0f, -0.1f, 0.0f, 0.0f);
        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(this.model.getLayer(this.texture));
        this.model.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, 1.0f, 1.0f, 1.0f, 1.0f);
        if (!boatEntity.isSubmergedInWater()) {
            VertexConsumer vertexConsumer2 = vertexConsumerProvider.getBuffer(RenderLayer.getWaterMask());
            if (this.model instanceof ModelWithWaterPatch modelWithWaterPatch) {
                modelWithWaterPatch.getWaterPatch().render(matrixStack, vertexConsumer2, i, OverlayTexture.DEFAULT_UV);
            }
        }
        matrixStack.pop();
        super.render(boatEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    @Override
    public Identifier getTexture(BoatEntity boatEntity) {
        return this.texture;
    }
}
