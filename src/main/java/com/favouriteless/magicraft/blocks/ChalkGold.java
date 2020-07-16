package com.favouriteless.magicraft.blocks;

import com.favouriteless.magicraft.init.MagicraftMaterials;
import com.favouriteless.magicraft.init.MagicraftTileEntities;
import com.favouriteless.magicraft.tileentity.ChalkGoldTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import java.util.Objects;

public class ChalkGold extends ChalkBase {

    public ChalkGold() {
        super(MagicraftMaterials.GOLDCHALK);
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return MagicraftTileEntities.CHALK_GOLD.get().create();
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
    {
        ( (ChalkGoldTileEntity) Objects.requireNonNull(world.getTileEntity(pos))).Execute(state, world, pos, player, hand, hit);
        return ActionResultType.PASS;
    }

}