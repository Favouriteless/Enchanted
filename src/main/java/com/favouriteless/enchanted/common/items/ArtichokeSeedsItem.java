package com.favouriteless.enchanted.common.items;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.world.World;

public class ArtichokeSeedsItem extends BlockNamedItem {
    
    public ArtichokeSeedsItem(Block block, Item.Properties properties) {
        super(block, properties);
    }

    public ActionResultType useOn(ItemUseContext pContext) {
        return ActionResultType.PASS;
    }

    public ActionResult<ItemStack> use(World pWorldIn, PlayerEntity pPlayerIn, Hand pHandIn) {
        BlockRayTraceResult blockraytraceresult = getPlayerPOVHitResult(pWorldIn, pPlayerIn, RayTraceContext.FluidMode.SOURCE_ONLY);
        BlockRayTraceResult blockraytraceresult1 = blockraytraceresult.withPosition(blockraytraceresult.getBlockPos().above());
        ActionResultType actionresulttype = super.useOn(new ItemUseContext(pPlayerIn, pHandIn, blockraytraceresult1));
        return new ActionResult<>(actionresulttype, pPlayerIn.getItemInHand(pHandIn));
    }
}