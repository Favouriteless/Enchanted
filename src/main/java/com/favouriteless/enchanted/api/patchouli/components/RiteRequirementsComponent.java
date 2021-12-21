/*
 * Copyright (c) 2021. Favouriteless
 * Enchanted, a minecraft mod.
 * GNU GPLv3 License
 *
 *     This file is part of Enchanted.
 *
 *     Enchanted is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Enchanted is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Enchanted.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.favouriteless.enchanted.api.patchouli.components;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.api.rites.AbstractRite;
import com.favouriteless.enchanted.common.init.EnchantedBlocks;
import com.favouriteless.enchanted.common.init.EnchantedRiteTypes;
import com.favouriteless.enchanted.common.rites.util.CirclePart;
import com.favouriteless.enchanted.core.util.Vector2i;
import com.google.gson.annotations.SerializedName;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.StringTextComponent;
import vazkii.patchouli.api.IComponentRenderContext;
import vazkii.patchouli.api.ICustomComponent;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.client.RenderHelper;
import vazkii.patchouli.client.book.gui.BookTextRenderer;
import vazkii.patchouli.client.book.gui.GuiBook;
import vazkii.patchouli.client.book.gui.GuiBookEntry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.UnaryOperator;

public class RiteRequirementsComponent implements ICustomComponent {

	public transient static final String PATH = "textures/patchouli/";
	public transient static final String FILE_END = ".png";
	public transient static final int IMAGE_SIZE = 110;
	public transient static final int TEXTURE_SIZE = 128;
	public transient static final float IMAGE_OFFSET = 110/2F;
	public transient static final HashMap<Block, String> BLOCK_IMAGES = new HashMap<>();

	static {
		BLOCK_IMAGES.put(EnchantedBlocks.CHALK_WHITE.get(), PATH + "white");
		BLOCK_IMAGES.put(EnchantedBlocks.CHALK_RED.get(), PATH + "red");
		BLOCK_IMAGES.put(EnchantedBlocks.CHALK_PURPLE.get(), PATH + "purple");
	}

	public int startRadius;
	public float scale;
	public int itemsFirstCircle;
	@SerializedName("rite") public IVariable riteName;

	private transient BookTextRenderer powerTextRenderer;
	private transient AbstractRite rite;
	private transient final List<ItemRing> itemRings = new ArrayList<>();
	private transient final List<ResourceLocation> circleImages = new ArrayList<>();
	private transient int x;
	private transient int y;

	@Override
	public void build(int componentX, int componentY, int pageNum) {
		this.x = componentX;
		this.y = componentY;

		repopulateCircleResources();
		repopulateRings();
	}

	@Override
	public void render(MatrixStack matrix, IComponentRenderContext context, float partialticks, int mouseX, int mouseY) {
		if(rite != null && context.getGui() instanceof GuiBook) {
			GuiBook gui = (GuiBook)context.getGui();

			for(ResourceLocation resourceLocation : circleImages) {
				renderCircle(matrix, resourceLocation);
			}
			for(ItemRing ring : itemRings) {
				ring.render(matrix, gui, mouseX, mouseY);
			}

			powerTextRenderer.render(matrix, mouseX, mouseY);
		}
	}

	@Override
	public void onDisplayed(IComponentRenderContext context) {
		Screen gui = context.getGui();
		if(gui instanceof GuiBookEntry) {
			GuiBookEntry entry = (GuiBookEntry)gui;
			StringTextComponent text = new StringTextComponent(rite.POWER + " Power Required");
			this.powerTextRenderer = new BookTextRenderer(entry, new StringTextComponent(rite.POWER + " Power Required"), 2, 140, 116, 9, 0x000000);
		}
	}

	private void repopulateCircleResources() {
		circleImages.clear();
		if(rite != null) {
			circleImages.add(new ResourceLocation(Enchanted.MOD_ID, PATH + "gold" + FILE_END));

			ResourceLocation small = getImageResource(CirclePart.SMALL, "small");
			ResourceLocation medium = getImageResource(CirclePart.MEDIUM, "medium");
			ResourceLocation large = getImageResource(CirclePart.LARGE, "large");
			if(small != null) circleImages.add(small);
			if(medium != null) circleImages.add(medium);
			if(large != null) circleImages.add(large);
		}
	}

	private ResourceLocation getImageResource(CirclePart part, String suffix) {
		for(Block block : BLOCK_IMAGES.keySet()) {
			if(rite.hasCircle(part, block))
				return new ResourceLocation(Enchanted.MOD_ID, BLOCK_IMAGES.get(block) + "_" + suffix + FILE_END);
		}
		return null;
	}

	private void renderCircle(MatrixStack matrix, ResourceLocation resourceLocation) {
		Minecraft.getInstance().getTextureManager().bind(resourceLocation);
		matrix.pushPose();
		matrix.translate(x-IMAGE_OFFSET, y-IMAGE_OFFSET, 0);
		RenderSystem.color4f(1F, 1F, 1F, 1F);
		RenderSystem.enableBlend();
		AbstractGui.blit(matrix, 0, 0, 0, 0, IMAGE_SIZE, IMAGE_SIZE, TEXTURE_SIZE, TEXTURE_SIZE);
		matrix.popPose();
	}

	@Override
	public void onVariablesAvailable(UnaryOperator<IVariable> lookup) {
		AbstractRite rite = EnchantedRiteTypes.getByName(new ResourceLocation(lookup.apply(riteName).asString()));
		if(rite == null)
			throw new IllegalStateException();
		this.rite = rite;
	}

	public void repopulateRings() {
		if(rite != null) {
			this.itemRings.clear();
			List<ItemStack> items = getItems();

			int index = 0;
			int currentItems = itemsFirstCircle;
			int currentRadius = startRadius;

			while(true) {
				List<ItemStack> subList = items.subList(index, Math.min(items.size(), index + currentItems));
				itemRings.add(new ItemRing(subList, x, y, currentRadius, scale));

				index += currentItems;
				if(index >= items.size()) break;
				currentItems *= 2;
				currentRadius += 17;
			}
		}
	}

	private List<ItemStack> getItems() {
		ItemStack[] items = new ItemStack[rite.ITEMS_REQUIRED.keySet().size()];
		int i = 0;
		for(Item item : rite.ITEMS_REQUIRED.keySet()) {
			items[i] = new ItemStack(item, rite.ITEMS_REQUIRED.get(item));
			i++;
		}
		return Arrays.asList(items);
	}

	private static class ItemRing {

		private final List<ItemStack> items;
		private final float scale;
		private final Vector2i[] scaledPositions;
		private final Vector2i[] absPositions;
		private final int itemSize;

		private ItemRing(List<ItemStack> items, int x, int y, float radius, float scale) {
			this.items = items;
			this.itemSize = Math.round(16*scale);
			this.scaledPositions = new Vector2i[items.size()];
			this.absPositions = new Vector2i[items.size()];
			this.scale = scale;
			recalculatePositions(x, y, radius);
		}

		private void recalculatePositions(int x, int y, float radius) {
			double xLocal = 0;
			double yLocal = -radius/scale;

			double angle = Math.toRadians(360D/items.size());
			double sin = Math.sin(angle);
			double cos = Math.cos(angle);

			for(int i = 0; i < scaledPositions.length; i++) {
				scaledPositions[i] = new Vector2i(
						(int)Math.round((xLocal + x/scale) - 8),
						(int)Math.round((yLocal + y/scale) - 8));
				absPositions[i] = new Vector2i(
						(int)Math.round(xLocal*scale + x - (8*scale)),
						(int)Math.round(yLocal*scale + y - (8*scale)));

				Vector3d newLocalPos = new Vector3d(
						cos * xLocal - sin * yLocal,
						sin * xLocal + cos * yLocal, 0);
				xLocal = newLocalPos.x;
				yLocal = newLocalPos.y;
			}
		}

		private void render(MatrixStack matrix, GuiBook gui, int mouseX, int mouseY) {
			matrix.pushPose();
			matrix.scale(scale, scale, scale);
			for(int i = 0; i < items.size(); i++) {
				ItemStack item = items.get(i);
				Vector2i scaledPos = scaledPositions[i];
				Vector2i absPos = absPositions[i];

				Minecraft mc = Minecraft.getInstance();

				RenderHelper.transferMsToGl(matrix, () -> {
					mc.getItemRenderer().renderAndDecorateItem(item, scaledPos.x, scaledPos.y);
					mc.getItemRenderer().renderGuiItemDecorations(mc.font, item, scaledPos.x, scaledPos.y);
				});

				if(gui.isMouseInRelativeRange(mouseX, mouseY, absPos.x, absPos.y, itemSize, itemSize)) {
					gui.setTooltipStack(item);
				}
			}
			matrix.popPose();
		}
	}

}
