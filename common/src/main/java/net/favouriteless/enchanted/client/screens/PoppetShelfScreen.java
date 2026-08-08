package net.favouriteless.enchanted.client.screens;

import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.enchanted.common.menus.PoppetShelfMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class PoppetShelfScreen extends AbstractContainerScreen<PoppetShelfMenu> {

    private static final ResourceLocation TEXTURE = Enchanted.id("textures/gui/menus/poppet_shelf.png");

    public PoppetShelfScreen(PoppetShelfMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);

        this.imageWidth = 176;
        this.imageHeight = 131;
        this.inventoryLabelY = imageHeight - 94; // This is set in super constructor
    }

    @Override
    public void render(GuiGraphics gui, int xMouse, int yMouse, float partialTicks) {
        this.renderBackground(gui, xMouse, yMouse, partialTicks);
        super.render(gui, xMouse, yMouse, partialTicks);
        this.renderTooltip(gui, xMouse, yMouse);
    }

    @Override
    protected void renderBg(GuiGraphics gui, float partialTicks, int x, int y) {
        int edgeSpacingX = (this.width - this.imageWidth) / 2;
        int edgeSpacingY = (this.height - this.imageHeight) / 2;
        gui.blit(TEXTURE, edgeSpacingX, edgeSpacingY, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    protected void renderLabels(GuiGraphics gui, int x, int y) {
        gui.drawString(font, title, (imageWidth / 2 - font.width(title) / 2), titleLabelY, 0x404040, false);
    }

}
