package com.favouriteless.magicraft.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class Altar extends Block {

    public Altar() {
        super(Block.Properties.create(Material.ROCK)
                .hardnessAndResistance(5.0F, 6.0F)
                .sound(SoundType.STONE));
    }


}
