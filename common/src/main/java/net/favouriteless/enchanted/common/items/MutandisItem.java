package net.favouriteless.enchanted.common.items;

import net.favouriteless.enchanted.common.mutandis.MutagenHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class MutandisItem extends Item {

    private final boolean isExtremis;

    public MutandisItem(boolean isExtremis) {
        super(new Properties());
        this.isExtremis = isExtremis;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();

        if(!level.isClientSide) {
            MutagenHandler.tryMutate((ServerLevel)level, pos);
        }

        return InteractionResult.PASS;
    }

}
