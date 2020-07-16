package com.favouriteless.magicraft.items;

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

    private final String chalkColor;
    private final Block chalkBlock;

    public ChalkItem(Block chalkBlock, String chalkColor) {
        super(new Item.Properties()
                .group(MagicraftTabs.MAIN)
                .maxStackSize(1)
                .maxDamage(40)
        );
        this.chalkBlock = chalkBlock;
        this.chalkColor = chalkColor;
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context)
    {
        Block blockClicked = context.getWorld().getBlockState(context.getPos()).getBlock();
        Block blockAboveClicked = context.getWorld().getBlockState(context.getPos().add(0, 1, 0)).getBlock();
        World world = context.getWorld();

        if(context.getFace() == Direction.UP) {
            if (blockAboveClicked == Blocks.AIR) {
                if (blockClicked.isNormalCube(blockClicked.getDefaultState(), context.getWorld(), context.getPos())) {

                    if(!world.isRemote) {
                        world.setBlockState(context.getPos().add(0, 1, 0), Objects.requireNonNull(chalkBlock.getStateForPlacement(new BlockItemUseContext(context))));
                    }
                    world.playSound(context.getPlayer(), context.getPos().add(0, 1, 0), SoundEvents.BLOCK_STONE_PLACE, SoundCategory.BLOCKS, 1f, 1f);
                    Objects.requireNonNull(context.getPlayer()).getHeldItem(context.getHand()).damageItem(1, context.getPlayer(), (p_220038_0_) -> { });

                    return ActionResultType.SUCCESS;
                }
            }
        }
        return ActionResultType.FAIL;
    }

}
