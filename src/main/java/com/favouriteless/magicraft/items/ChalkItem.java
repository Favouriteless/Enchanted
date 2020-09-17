package com.favouriteless.magicraft.items;

import com.favouriteless.magicraft.blocks.ChalkBase;
import com.favouriteless.magicraft.init.MagicraftTabs;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

import java.util.Objects;

public class ChalkItem extends Item {

    private final ChalkBase chalkBlock;

    public ChalkItem(Item.Properties builder, Block block) {
        super(builder);
        if(block instanceof ChalkBase) {
            this.chalkBlock = (ChalkBase) block;
        } else {
            this.chalkBlock = null;
        }
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context)
    {
        Block blockClicked = context.getWorld().getBlockState(context.getPos()).getBlock();
        Block blockAboveClicked = context.getWorld().getBlockState(context.getPos().add(0, 1, 0)).getBlock();

        if(context.getFace() == Direction.UP) {
            if (blockAboveClicked == Blocks.AIR) {
                if (this.chalkBlock.isValidPosition(blockClicked.getDefaultState(), context.getWorld(), context.getPos())) {

                    if(!context.getWorld().isRemote) {
                        context.getWorld().setBlockState(context.getPos().add(0, 1, 0), Objects.requireNonNull(chalkBlock.getStateForPlacement(new BlockItemUseContext(context))));
                    }
                    context.getWorld().playSound(context.getPlayer(), context.getPos().add(0, 1, 0), SoundEvents.BLOCK_STONE_PLACE, SoundCategory.BLOCKS, 1f, 1f);
                    Objects.requireNonNull(context.getPlayer()).getHeldItem(context.getHand()).damageItem(1, context.getPlayer(), (p_220038_0_) -> { });

                    return ActionResultType.SUCCESS;
                }
            }
        }
        return ActionResultType.FAIL;
    }

}
