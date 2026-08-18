package alabaster.crabbersdelight.client.gui;

import alabaster.crabbersdelight.CrabbersDelightFabric;
import alabaster.crabbersdelight.common.block.container.CrabTrapMenu;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class CrabTrapGUI extends HandledScreen<CrabTrapMenu> {
    private static final Identifier CRAB_TRAP_GUI = CrabbersDelightFabric.id("textures/gui/crab_trap.png");

    public CrabTrapGUI(CrabTrapMenu menu, PlayerInventory playerInv, Text title) {
        super(menu, playerInv, title);
        this.backgroundWidth = 176;
        this.backgroundHeight = 147;
        this.playerInventoryTitleY = 54;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(context, mouseX, mouseY);
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, CRAB_TRAP_GUI);
        int i = (this.width - this.backgroundWidth) / 2;
        int j = (this.height - this.backgroundHeight) / 2;
        context.drawTexture(CRAB_TRAP_GUI, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight);
    }
}
