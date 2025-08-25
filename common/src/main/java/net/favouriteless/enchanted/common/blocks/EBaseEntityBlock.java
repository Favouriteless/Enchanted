package net.favouriteless.enchanted.common.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public abstract class EBaseEntityBlock<B extends EBaseEntityBlock<?>> extends BaseEntityBlock {

    private final MapCodec<B> codec;

    protected EBaseEntityBlock(Function<Properties, B> factory, Properties properties) {
        super(properties);
        this.codec = simpleCodec(factory);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return codec;
    }

    @Nullable
    @SuppressWarnings("unchecked")
    protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createSidedTickerHelper(
            Level level, BlockEntityType<A> serverType, BlockEntityType<E> clientType,
            BlockEntityTicker<? super E> serverTicker, BlockEntityTicker<? super E> clientTicker) {
        return clientType == serverType ? !level.isClientSide ? (BlockEntityTicker<A>)serverTicker : (BlockEntityTicker<A>)clientTicker : null;
    }

}
