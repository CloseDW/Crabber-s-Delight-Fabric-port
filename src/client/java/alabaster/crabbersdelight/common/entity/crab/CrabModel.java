package alabaster.crabbersdelight.common.entity.crab;

import alabaster.crabbersdelight.CrabbersDelightFabric;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class CrabModel<T extends CrabEntity> extends SinglePartEntityModel<T> {

	public static final EntityModelLayer LAYER_LOCATION =
			new EntityModelLayer(new Identifier(CrabbersDelightFabric.MOD_ID, "crab"), "main");

	private final ModelPart crab;
	private final ModelPart fullbody;
	private final ModelPart eye;
	private final ModelPart bigclaw;
	private final ModelPart bigclawbottom;
	private final ModelPart bigclawtop;
	private final ModelPart smallclaw;
	private final ModelPart antenna;
	private final ModelPart rantenna;
	private final ModelPart rantenna2;
	private final ModelPart bodybase;
	private final ModelPart legs;
	private final ModelPart rlegs;
	private final ModelPart rleg1;
	private final ModelPart rleg2;
	private final ModelPart rleg3;
	private final ModelPart llegs;
	private final ModelPart lleg1;
	private final ModelPart lleg2;
	private final ModelPart lleg3;

	public CrabModel(ModelPart root) {
		this.crab = root.getChild("crab");
		this.fullbody = this.crab.getChild("fullbody");
		this.eye = this.fullbody.getChild("eye");
		this.bigclaw = this.fullbody.getChild("bigclaw");
		this.bigclawbottom = this.bigclaw.getChild("bigclawbottom");
		this.bigclawtop = this.bigclaw.getChild("bigclawtop");
		this.smallclaw = this.fullbody.getChild("smallclaw");
		this.antenna = this.fullbody.getChild("antenna");
		this.rantenna = this.antenna.getChild("rantenna");
		this.rantenna2 = this.antenna.getChild("rantenna2");
		this.bodybase = this.fullbody.getChild("bodybase");
		this.legs = this.crab.getChild("legs");
		this.rlegs = this.legs.getChild("rlegs");
		this.rleg1 = this.rlegs.getChild("rleg1");
		this.rleg2 = this.rlegs.getChild("rleg2");
		this.rleg3 = this.rlegs.getChild("rleg3");
		this.llegs = this.legs.getChild("llegs");
		this.lleg1 = this.llegs.getChild("lleg1");
		this.lleg2 = this.llegs.getChild("lleg2");
		this.lleg3 = this.llegs.getChild("lleg3");
	}

	public static TexturedModelData createBodyLayer() {
		ModelData meshdefinition = new ModelData();
		ModelPartData partdefinition = meshdefinition.getRoot();

		ModelPartData crab = partdefinition.addChild("crab", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 21.0F, 0.5F));

		ModelPartData fullbody = crab.addChild("fullbody", ModelPartBuilder.create(), ModelTransform.pivot(-3.0F, -1.5F, -3.5F));

		ModelPartData eye = fullbody.addChild("eye", ModelPartBuilder.create().uv(5, 4).cuboid(5.5F, -1.5F, 0.0F, 1.0F, 1.0F, 0.01F, new Dilation(0.0F))
				.uv(5, 5).cuboid(-0.5F, -1.5F, 0.0F, 1.0F, 1.0F, 0.01F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData bigclaw = fullbody.addChild("bigclaw", ModelPartBuilder.create(), ModelTransform.pivot(8.5165F, 0.0121F, 4.7325F));

		ModelPartData bigclawbottom = bigclaw.addChild("bigclawbottom", ModelPartBuilder.create(), ModelTransform.pivot(0.2405F, -0.0456F, -0.4621F));

		bigclawbottom.addChild("bigclawbottom_r1", ModelPartBuilder.create().uv(16, 15).cuboid(-1.9052F, 0.1072F, -6.8775F, 4.0F, 3.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, -0.1835F, -0.5658F, 0.0179F));

		ModelPartData bigclawtop = bigclaw.addChild("bigclawtop", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		bigclawtop.addChild("bigclawtop_r1", ModelPartBuilder.create().uv(0, 22).cuboid(-1.9506F, -2.8472F, -7.3965F, 4.0F, 3.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, -0.1835F, -0.5658F, 0.0179F));

		ModelPartData smallclaw = fullbody.addChild("smallclaw", ModelPartBuilder.create(), ModelTransform.pivot(-2.5396F, 1.2184F, 4.2473F));

		smallclaw.addChild("smallclaw_r1", ModelPartBuilder.create().uv(0, 12).cuboid(-1.2717F, -1.1118F, -4.1902F, 3.0F, 3.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, -0.1835F, 0.5658F, -0.0179F));

		ModelPartData antenna = fullbody.addChild("antenna", ModelPartBuilder.create(), ModelTransform.pivot(4.0F, -0.0231F, -0.1503F));

		ModelPartData rantenna = antenna.addChild("rantenna", ModelPartBuilder.create(), ModelTransform.pivot(-2.0F, 0.0F, 0.0F));

		rantenna.addChild("rantenna_r1", ModelPartBuilder.create().uv(4, 2).cuboid(-0.5F, -1.0F, -0.2F, 1.0F, 2.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0231F, 0.1503F, -0.3054F, 0.0F, 0.0F));

		ModelPartData rantenna2 = antenna.addChild("rantenna2", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		rantenna2.addChild("rantenna2_r1", ModelPartBuilder.create().uv(4, 2).cuboid(-0.5F, -1.0F, -0.2F, 1.0F, 2.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0231F, 0.1503F, -0.3054F, 0.0F, 0.0F));

		ModelPartData bodybase = fullbody.addChild("bodybase", ModelPartBuilder.create().uv(0, 1).mirrored().cuboid(-4.5F, -2.0F, -3.5F, 9.0F, 4.0F, 7.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(3.0F, 1.5F, 3.5F));

		ModelPartData legs = crab.addChild("legs", ModelPartBuilder.create(), ModelTransform.pivot(-4.9759F, 2.5095F, 0.0F));

		ModelPartData rlegs = legs.addChild("rlegs", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData rleg1 = rlegs.addChild("rleg1", ModelPartBuilder.create(), ModelTransform.pivot(1.0F, -1.0F, -2.0F));

		rleg1.addChild("rleg1_r1", ModelPartBuilder.create().uv(-1, 0).cuboid(-2.4129F, -0.0617F, -0.5F, 2.0F, 0.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.829F));

		ModelPartData rleg2 = rlegs.addChild("rleg2", ModelPartBuilder.create(), ModelTransform.pivot(1.0F, -1.0F, 0.0F));

		rleg2.addChild("rleg2_r1", ModelPartBuilder.create().uv(-1, 1).cuboid(-2.4129F, -0.0617F, -0.5F, 2.0F, 0.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.829F));

		ModelPartData rleg3 = rlegs.addChild("rleg3", ModelPartBuilder.create(), ModelTransform.pivot(1.0F, -1.0F, 2.0F));

		rleg3.addChild("rleg3_r1", ModelPartBuilder.create().uv(-1, 0).cuboid(-2.4129F, -0.0617F, -0.5F, 2.0F, 0.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.829F));

		ModelPartData llegs = legs.addChild("llegs", ModelPartBuilder.create(), ModelTransform.pivot(9.9518F, 0.0F, 0.0F));

		ModelPartData lleg1 = llegs.addChild("lleg1", ModelPartBuilder.create(), ModelTransform.pivot(-1.0F, -1.0F, -2.0F));

		lleg1.addChild("lleg1_r1", ModelPartBuilder.create().uv(-1, 0).cuboid(0.4129F, -0.0617F, -0.5F, 2.0F, 0.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.829F));

		ModelPartData lleg2 = llegs.addChild("lleg2", ModelPartBuilder.create(), ModelTransform.pivot(-1.0F, -1.0F, 0.0F));

		lleg2.addChild("lleg2_r1", ModelPartBuilder.create().uv(-1, 1).cuboid(0.4129F, -0.0617F, -0.5F, 2.0F, 0.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.829F));

		ModelPartData lleg3 = llegs.addChild("lleg3", ModelPartBuilder.create(), ModelTransform.pivot(-1.0F, -1.0F, 2.0F));

		lleg3.addChild("lleg3_r1", ModelPartBuilder.create().uv(-1, 0).cuboid(0.4129F, -0.0617F, -0.5F, 2.0F, 0.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.829F));

		return TexturedModelData.of(meshdefinition, 64, 64);
	}

	@Override
	public void setAngles(CrabEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.getPart().traverse().forEach(ModelPart::resetTransform);
		this.applyHeadRotation(netHeadYaw, headPitch);
		this.animateMovement(CrabAnimations.walk, limbSwing, limbSwingAmount, 2f, 5f);
		this.updateAnimation(entity.idleAnimationState, CrabAnimations.idle, ageInTicks, 1f);
	}

	private void applyHeadRotation(float headYaw, float headPitch) {
		headYaw = MathHelper.clamp(headYaw, -30f, 30f);
		headPitch = MathHelper.clamp(headPitch, -25f, 45);

		this.crab.yaw = headYaw * ((float)Math.PI / 180f);
		this.crab.pitch = headPitch *  ((float)Math.PI / 180f);
	}

	@Override
	public void render(MatrixStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		crab.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public ModelPart getPart() {
		return crab;
	}
}
