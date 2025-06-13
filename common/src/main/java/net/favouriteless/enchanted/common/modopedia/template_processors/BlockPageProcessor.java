package net.favouriteless.enchanted.common.modopedia.template_processors;

import net.favouriteless.modopedia.api.Lookup.MutableLookup;
import net.favouriteless.modopedia.api.Variable;
import net.favouriteless.modopedia.api.books.Book;
import net.favouriteless.modopedia.api.books.TemplateProcessor;
import net.favouriteless.modopedia.api.multiblock.Multiblock;
import net.favouriteless.modopedia.multiblock.DenseMultiblock;
import net.favouriteless.modopedia.multiblock.state_matchers.SimpleStateMatcher;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.Map;

public class BlockPageProcessor implements TemplateProcessor {

    @Override
    public void init(Book book, MutableLookup lookup, Level level) {
        BlockState state = lookup.get("block").as(BlockState.class);

        Multiblock multiblock = new DenseMultiblock(
                List.of(List.of("B")),
                Map.of('B', new SimpleStateMatcher(List.of(state)))
        );

        lookup.set("multiblock", Variable.of(multiblock));
    }

}
