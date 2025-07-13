package net.favouriteless.enchanted.integrations.modopedia.client.page_components;

import com.mojang.blaze3d.vertex.PoseStack;
import net.favouriteless.enchanted.api.MutagenManager;
import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.enchanted.common.mutandis.MutagenInfo.MutagenSet;
import net.favouriteless.modopedia.api.Lookup;
import net.favouriteless.modopedia.api.book.Book;
import net.favouriteless.modopedia.api.book.BookTexture;
import net.favouriteless.modopedia.api.book.BookTexture.FixedRectangle;
import net.favouriteless.modopedia.api.book.BookTexture.Rectangle;
import net.favouriteless.modopedia.api.book.page_components.BookRenderContext;
import net.favouriteless.modopedia.api.book.page_components.ItemDisplay;
import net.favouriteless.modopedia.api.book.page_components.PageComponent;
import net.favouriteless.modopedia.api.book.page_components.PageWidgetHolder;
import net.favouriteless.modopedia.api.registries.client.BookTextureRegistry;
import net.favouriteless.modopedia.client.page_components.item_displays.RingsItemDisplay;
import net.favouriteless.modopedia.client.page_components.item_displays.SimpleItemDisplay;
import net.favouriteless.modopedia.client.page_widgets.PageImageButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;

public class MutagenDisplayPageComponent extends PageComponent {

    public static final ResourceLocation ID = Enchanted.id("mutagen_display");

    private List<MutagenDisplay> displays;

    protected PageImageButton leftButton;
    protected PageImageButton rightButton;
    protected int selected = 0;

    @Override
    public void init(Book book, Lookup lookup, Level level) {
        super.init(book, lookup, level);

        BookTexture texture = BookTextureRegistry.get().getTexture(book.getTexture());
        if(texture == null || !texture.widgets().containsKey("mutagen"))
            throw new IllegalStateException("MutagenInfoPageComponent has been created on a book texture with no mutagen widget");

        displays = new ArrayList<>(); // Re-initialise every time.

        Block result = BuiltInRegistries.BLOCK.get(lookup.get("result").as(ResourceLocation.class));

        for(Entry<Block, List<MutagenSet>> entry : MutagenManager.get().getMutagensFor(level, result).entrySet()) {
            ItemStack mutee = entry.getKey().asItem().getDefaultInstance();

            for(MutagenSet set : entry.getValue()) {
                displays.add(new MutagenDisplay(
                        new SimpleItemDisplay(mutee),
                        new RingsItemDisplay(set.mutagens().stream().<ItemDisplay>map(b -> new SimpleItemDisplay(b.asItem().getDefaultInstance())).toList(), 8, 22, 8),
                        set.weight(),
                        set.extremis()
                ));
            }
        }
        displays.sort((o1, o2) -> o2.weight - o1.weight);
    }

    @Override
    public void render(GuiGraphics graphics, BookRenderContext context, int mouseX, int mouseY, float partialTicks) {
        BookTexture tex = context.getBookTexture();
        Rectangle mutagen = tex.widgets().get("mutagen");

        int height = mutagen.height();
        int xo = mutagen.width() / 2;
        int yo = y + height / 2;

        graphics.blit(tex.location(), x - xo, y, mutagen.width(), mutagen.height(),
                mutagen.u(), mutagen.v(), mutagen.width(), mutagen.height(), tex.texWidth(), tex.texHeight());

        MutagenDisplay display = displays.get(selected);

        PoseStack pose = graphics.pose();

        pose.pushPose();
        pose.translate(x, yo, 0);

        pose.pushPose();
        pose.translate(-8, -8, 0);
        display.mutee.render(graphics, context, mouseX - (x-8), mouseY - (yo-8), entryId);
        pose.popPose();

        display.mutagens.render(graphics, context, mouseX - x, mouseY - yo, entryId);

        pose.popPose();

        Font font = Minecraft.getInstance().font;
        Component weightTitle = Component.translatable(Enchanted.translationKey("tooltip", "mutagen_weight")).withStyle(context.getStyle().withBold(true));
        Component weight = Component.literal(String.valueOf(display.weight)).withStyle(context.getStyle());
        Component extremis = Component.translatable(Enchanted.translationKey("tooltip", "mutagen_extremis")).withStyle(context.getStyle().withBold(true).withColor(0x732D2D));

        int yw = y + height + 2;

        graphics.drawString(font, weightTitle, x - font.width(weightTitle) / 2, yw, 0, false);
        graphics.drawString(font, weight, x - font.width(weight) / 2, yw + font.lineHeight, 0, false);

        if(display.extremis)
            graphics.drawString(font, extremis, x - font.width(extremis) / 2, y - 7, 0, false);
    }

    @Override
    public void initWidgets(PageWidgetHolder holder, BookRenderContext context) {
        if(displays.size() < 2)
            return;

        BookTexture bookTex = context.getBookTexture();
        ResourceLocation tex = bookTex.location();
        FixedRectangle left = bookTex.left();
        FixedRectangle right = bookTex.right();
        Rectangle mutagen = bookTex.widgets().get("mutagen");

        int xo = mutagen.width() / 2;

        leftButton = holder.addRenderableWidget(
                new PageImageButton(tex, x - xo, y + mutagen.height() + 11, left.width(), left.height(),
                        left.u(), left.v(), bookTex.texWidth(), bookTex.texHeight(), b -> changeImage(-1))
        );
        rightButton = holder.addRenderableWidget(
                new PageImageButton(tex, x + xo - right.width(), y + mutagen.height() + 11, right.width(), right.height(),
                        right.u(), right.v(), bookTex.texWidth(), bookTex.texHeight(), b -> changeImage(1))
        );

        updateWidgetVisibility();
    }

    protected void changeImage(int by) {
        selected = Mth.clamp(selected + by, 0, displays.size() - 1);
        updateWidgetVisibility();
    }

    protected void updateWidgetVisibility() {
        if(selected <= 0) {
            selected = 0;
            leftButton.active = false;
        }
        else {
            leftButton.active = true;
        }

        if(selected >= displays.size() - 1) {
            selected = displays.size() - 1;
            rightButton.active = false;
        }
        else {
            rightButton.active = true;
        }
    }



    private record MutagenDisplay(ItemDisplay mutee, ItemDisplay mutagens, int weight, boolean extremis) {}

}