package net.favouriteless.enchanted.common.items;

import net.favouriteless.enchanted.common.init.EBlocks;
import net.favouriteless.enchanted.common.util.RandomUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

public class AnointingPasteItem extends Item {

    public AnointingPasteItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();

        if(!level.getBlockState(pos).is(Blocks.CAULDRON))
            return InteractionResult.PASS;

        if(!level.isClientSide) {
            level.setBlockAndUpdate(pos, EBlocks.WITCH_CAULDRON.get().defaultBlockState());
            context.getItemInHand().shrink(1);
        }
        else {
            level.playSound(context.getPlayer(), pos, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
            spawnParticles(level, pos);
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    public static void spawnParticles(Level level, BlockPos pos) {
        for(int i = 0; i < 20; i++) {
            double x = RandomUtils.nextDouble() * 2;
            double y = RandomUtils.nextDouble() * 1.5D;
            double z = RandomUtils.nextDouble() * 2;

            level.addParticle(ParticleTypes.WITCH, pos.getX()-0.5D + x, pos.getY() + y, pos.getZ()-0.5D + z, 0.0D, 0.0D, 0.0D);
        }
    }

}
