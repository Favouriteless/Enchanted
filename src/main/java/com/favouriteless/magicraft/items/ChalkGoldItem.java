package com.favouriteless.magicraft.items;

import com.favouriteless.magicraft.init.MagicraftBlocks;
import com.favouriteless.magicraft.init.MagicraftTabs;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;

public class ChalkGoldItem extends Item {

    public ChalkGoldItem() {
        super(new Item.Properties()
                .group(MagicraftTabs.MAIN)
                .maxStackSize(1)
                .maxDamage(3)
        );
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context)
    {
        Block blockClicked = context.getWorld().getBlockState(context.getPos()).getBlock();
        Block blockAboveClicked = context.getWorld().getBlockState(context.getPos().add(0, 1, 0)).getBlock();

        if(context.getFace() == Direction.UP) {
            if (blockAboveClicked == Blocks.AIR) {
                if (blockClicked.isNormalCube(blockClicked.getDefaultState(), context.getWorld(), context.getPos())) {
                    context.getWorld().setBlockState(context.getPos().add(0, 1, 0), MagicraftBlocks.CHALK_GOLD.get().getDefaultState());
                    context.getWorld().playSound(context.getPlayer(), context.getPos().add(0, 1, 0), SoundEvents.BLOCK_STONE_PLACE, SoundCategory.BLOCKS, 1f, 1f);
                    context.getPlayer().getHeldItem(context.getHand()).damageItem(1, context.getPlayer(), (p_220038_0_) -> { });
                    return ActionResultType.SUCCESS;
                }
            }
        }
        return ActionResultType.FAIL;
    }

}
