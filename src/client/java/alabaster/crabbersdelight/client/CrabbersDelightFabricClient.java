package alabaster.crabbersdelight.client;

import alabaster.crabbersdelight.CrabbersDelightFabric;
import alabaster.crabbersdelight.common.registry.CDModBlocks;
import alabaster.crabbersdelight.common.registry.CDModEntities;
import alabaster.crabbersdelight.common.entity.crab.CrabModel;
import alabaster.crabbersdelight.common.entity.crab.CrabRenderer;
import alabaster.crabbersdelight.client.gui.CrabTrapGUI;
import alabaster.crabbersdelight.common.entity.boat.CDBoatModelLayers;
import alabaster.crabbersdelight.common.entity.boat.CDBoatRenderer;
import alabaster.crabbersdelight.common.block.entity.CDSignBlockEntity;
import alabaster.crabbersdelight.common.block.entity.CDHangingSignBlockEntity;
import alabaster.crabbersdelight.common.registry.CDModMenus;
import alabaster.crabbersdelight.common.registry.CDModBlockEntity;
import alabaster.crabbersdelight.common.registry.CDModEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.util.Identifier;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.block.entity.SignBlockEntityRenderer;
import net.minecraft.client.render.block.entity.HangingSignBlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.block.entity.HangingSignBlockEntity;
import net.minecraft.client.render.entity.model.BoatEntityModel;
import net.minecraft.client.render.entity.model.ChestBoatEntityModel;

public class CrabbersDelightFabricClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// 蟹钳 GUI 用 2D 模型（手持用 3D），先把 2D 模型注册进烘焙队列
		ModelLoadingPlugin.register(plugin ->
				plugin.addModels(new ModelIdentifier(
						new Identifier("crabbersdelight", "crab_claw_2d"), "inventory")));
		// 椰壳头盔戴在头上（HEAD 视角）时用 3D 模型，先注册进烘焙队列
		ModelLoadingPlugin.register(plugin ->
				plugin.addModels(new ModelIdentifier(
						new Identifier("crabbersdelight", "coconut_helmet_3d"), "inventory")));

		// 贝壳模型有透明镂空，必须注册 Cutout 渲染层
		// （1.20.1 的模型 JSON 不认识 1.21 才有的 render_type 字段）
		BlockRenderLayerMap.INSTANCE.putBlock(CDModBlocks.SEASHELLS, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(CDModBlocks.PALM_SAPLING, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(CDModBlocks.CRAB_TRAP, RenderLayer.getCutout());

		// 螃蟹：渲染器 + 模型层
		EntityRendererRegistry.register(CDModEntities.CRAB, CrabRenderer::new);
		EntityModelLayerRegistry.registerModelLayer(CrabModel.LAYER_LOCATION, CrabModel::createBodyLayer);

		// 捕蟹笼 GUI
		HandledScreens.register(CDModMenus.CRAB_TRAP_MENU, CrabTrapGUI::new);

		// 船：渲染器 + 模型层
		EntityRendererRegistry.register(CDModEntities.MOD_BOAT, ctx -> new CDBoatRenderer(ctx, false));
		EntityRendererRegistry.register(CDModEntities.MOD_CHEST_BOAT, ctx -> new CDBoatRenderer(ctx, true));
		EntityModelLayerRegistry.registerModelLayer(CDBoatModelLayers.PALM_BOAT_LAYER, BoatEntityModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(CDBoatModelLayers.PALM_CHEST_BOAT_LAYER, ChestBoatEntityModel::getTexturedModelData);

		// 告示牌渲染器
		BlockEntityRendererRegistry.register(CDModBlockEntity.PALM_SIGN,
				(BlockEntityRendererFactory<SignBlockEntity>) SignBlockEntityRenderer::new);
		BlockEntityRendererRegistry.register(CDModBlockEntity.HANGING_PALM_SIGN,
				(BlockEntityRendererFactory<SignBlockEntity>) HangingSignBlockEntityRenderer::new);
	}
}
