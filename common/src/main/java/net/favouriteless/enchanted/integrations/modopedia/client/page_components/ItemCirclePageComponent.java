package net.favouriteless.enchanted.integrations.modopedia.client.page_components;

import com.google.common.reflect.TypeToken;
import com.mojang.blaze3d.platform.InputConstants;
import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.modopedia.api.Lookup;
import net.favouriteless.modopedia.api.book.Book;
import net.favouriteless.modopedia.api.book.page_components.BookRenderContext;
import net.favouriteless.modopedia.api.book.page_components.PageComponent;
import net.favouriteless.modopedia.book.StudyManager;
import net.favouriteless.modopedia.book.registries.client.ItemAssociationRegistry.EntryAssociation;
import net.favouriteless.modopedia.client.BookOpenHandler;
import net.favouriteless.modopedia.client.init.MKeyMappings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ItemCirclePageComponent extends PageComponent {

    public static final ResourceLocation ID = Enchanted.id("item_circle");

    private final List<List<ItemStack>> itemRings = new ArrayList<>();
    private int ringMax;
    private int radius;

    @Override
    public void init(Book book, Lookup lookup, Level level) {
        super.init(book, lookup, level);
        Queue<ItemStack> full = new LinkedList<>(lookup.get("items").as(new TypeToken<List<ItemStack>>() {}));
        if(full.isEmpty())
            throw new IllegalArgumentException("Item circle gallery cannot have zero items in it");

        ringMax = lookup.getOrDefault("ring_max", Integer.MAX_VALUE).asInt();
        radius = lookup.getOrDefault("radius", 16).asInt();

        int rings = 1;
        while(!full.isEmpty()) { // Initialise the item and position lists to make things easier later
            List<ItemStack> ring = new ArrayList<>();

            int count = Math.min(ringMax * rings, full.size());
            for(int i = 0; i < count; i++)
                ring.add(full.poll());

            itemRings.add(ring);
            rings++;
        }
    }

    @Override
    public void render(GuiGraphics graphics, BookRenderContext context, int mouseX, int mouseY, float partialTicks) {
        Font font = Minecraft.getInstance().font;

        for(int r = 0; r < itemRings.size(); r++) {
            List<ItemStack> items = itemRings.get(r);

            float a = 360.0F / items.size() * Mth.DEG_TO_RAD;
            float sin = Mth.sin(a);
            float cos = Mth.cos(a);

            float x = 0;
            float y = -radius * (r+1);

            for(ItemStack stack : items) {
                int x1 = Math.round(x);
                int y1 = Math.round(y);

                graphics.renderItem(stack, x1, y1);
                graphics.renderItemDecorations(font, stack, x1, y1);

                if(context.isHovered(mouseX, mouseY, x1, y1, 16, 16)) {
                    graphics.renderTooltip(font, stack, mouseX, mouseY);

                    String langCode = Minecraft.getInstance().options.languageCode;
                    EntryAssociation association = StudyManager.getAssociation(langCode, stack.getItem());
                    if(association != null && InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), MKeyMappings.KEY_STUDY.key.getValue())) {
                        if(association.entryId().equals(entryId))
                            return;
                        BookOpenHandler.tryOpenEntry(association.book(), association.entryId());
                    }
                }
                // Rotate point around (0, 0). Use a copy of x as it gets modified.
                float xc = x;
                x = xc * cos - y * sin;
                y = xc * sin + y * cos;
            }
        }
    }

}