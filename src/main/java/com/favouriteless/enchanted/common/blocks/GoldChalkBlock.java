package com.favouriteless.enchanted.common.blocks;

import com.favouriteless.enchanted.common.tileentity.ChalkGoldTileEntity;
import com.favouriteless.enchanted.core.init.EnchantedTileEntities;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class GoldChalkBlock extends ChalkBlockBase {

    public GoldChalkBlock() {
        super();
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return EnchantedTileEntities.CHALK_GOLD.get().create();
    }

    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
    {
        ( (ChalkGoldTileEntity) world.getBlockEntity(pos)).Execute(state, world, pos, player, hand, hit);
        return ActionResultType.PASS;
    }

}