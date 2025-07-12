package net.favouriteless.enchanted.integrations.modopedia.client.page_components;

import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.modopedia.api.Lookup;
import net.favouriteless.modopedia.api.book.Book;
import net.favouriteless.modopedia.api.book.page_components.BookRenderContext;
import net.favouriteless.modopedia.api.book.page_components.PageComponent;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

public class MutagenInfoPageComponent extends PageComponent {

    public static final ResourceLocation ID = Enchanted.id("mutagen_info");

//    private Map<MutagenSet, BookItemRing> rings;

    @Override
    public void init(Book book, Lookup lookup, Level level) {
        super.init(book, lookup, level);
//
//        Block block = BuiltInRegistries.BLOCK.get(lookup.get("block").as(ResourceLocation.class));
//        int radius = lookup.getOrDefault("radius", 16).asInt();
//        int ringMax = lookup.getOrDefault("ring_max", Integer.MAX_VALUE).asInt();
//
//        List<MutagenInfo> infos = MutagenManager.get().getMutagensFor(level, block);
//        if(infos.isEmpty())
//            throw new IllegalArgumentException(block + " does not have any valid MutagenInfos");
//
//        for(MutagenInfo info : infos) { // Filter out individual sets which produce the result.
//            for(MutagenSet set : info.sets()) {
//                if(block != set.result())
//                    continue;
//
//                List<ItemStack> items = set.mutagens().stream().map(b -> b.asItem().getDefaultInstance()).toList();
//                rings.put(set, new BookItemRing(items, radius, ringMax));
//            }
//        }
    }

    @Override
    public void render(GuiGraphics graphics, BookRenderContext context, int mouseX, int mouseY, float partialTicks) {
//        rings.render(graphics, context, mouseX, mouseY, entryId);
    }

}