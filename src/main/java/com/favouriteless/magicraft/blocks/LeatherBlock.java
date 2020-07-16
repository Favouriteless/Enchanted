package com.favouriteless.magicraft.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class LeatherBlock extends Block {

    public LeatherBlock() {
        super(Block.Properties.create(Material.WOOL)
                .hardnessAndResistance(0.8f, 0.8f)
                .sound(SoundType.CLOTH)
        );
    }

}
