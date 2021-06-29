package com.favouriteless.enchanted.common.tileentity;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.core.init.EnchantedTileEntities;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

public class ChalkGoldTileEntity extends TileEntity implements ITickableTileEntity {

    private boolean executing = false;
    private int ticks = 0;

    public ChalkGoldTileEntity(final TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    public ChalkGoldTileEntity() {
        this(EnchantedTileEntities.CHALK_GOLD.get());
    }

    public void Execute(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if(!executing && !world.isClientSide()) {
            executing = true;

            Enchanted.LOGGER.debug("gold chalk used at " + pos);
        }
    }

    @Override
    public void tick() {
        if(executing) {
            ticks++;
            if(ticks >= 40) {
                ticks = 0;
                executing = false;
            }
        }
    }

}
