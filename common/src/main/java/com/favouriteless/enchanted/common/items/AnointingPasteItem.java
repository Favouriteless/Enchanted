package com.favouriteless.enchanted.common.items;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.common.init.registry.EnchantedBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class AnointingPasteItem extends Item {

    public AnointingPasteItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        BlockPos pos = context.getClickedPos();
        BlockState state = context.getLevel().getBlockState(pos);
        if(state.is(Blocks.CAULDRON)) {
            context.getLevel().setBlockAndUpdate(pos, EnchantedBlocks.WITCH_CAULDRON.get().defaultBlockState());
            context.getItemInHand().shrink(1);

            context.getLevel().playSound(context.getPlayer(), pos, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
            spawnParticles(context.getLevel(), pos);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.FAIL;
    }

    public static void spawnParticles(Level level, BlockPos pos) {
        for(int i = 0; i < 20; i++) {
            double x = Enchanted.RANDOM.nextDouble() * 2;
            double y = Enchanted.RANDOM.nextDouble() * 1.5D;
            double z = Enchanted.RANDOM.nextDouble() * 2;

            level.addParticle(ParticleTypes.WITCH, pos.getX()-0.5D + x, pos.getY() + y, pos.getZ()-0.5D + z, 0.0D, 0.0D, 0.0D);
        }
    }

}
