package com.favouriteless.enchanted.common.blocks.altar;

import com.favouriteless.enchanted.common.init.registry.EnchantedBlocks;
import com.favouriteless.enchanted.common.init.registry.EnchantedItems;
import com.mojang.math.Vector3f;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ChaliceBlock extends Block {

    private final boolean isFilled;

    public ChaliceBlock(Properties properties, boolean filled) {
        super(properties);
        this.isFilled = filled;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if(state.is(EnchantedBlocks.CHALICE.get())) {

            if(player.getItemInHand(hand).getItem() == EnchantedItems.REDSTONE_SOUP.get()) {
                if (!level.isClientSide) {
                    level.playSound(null, pos, SoundEvents.FISHING_BOBBER_SPLASH, SoundSource.BLOCKS, 0.4F, 1.0F);
                    level.setBlockAndUpdate(pos, EnchantedBlocks.CHALICE_FILLED.get().defaultBlockState());
                    player.getItemInHand(hand).shrink(1);
                    return InteractionResult.CONSUME;
                }
                return InteractionResult.SUCCESS;
            }

            else if(player.getItemInHand(hand).getItem() == Items.MILK_BUCKET) {
                if (!level.isClientSide) {
                    level.playSound(null, pos, SoundEvents.FISHING_BOBBER_SPLASH, SoundSource.BLOCKS, 0.4F, 1.0F);
                    level.setBlockAndUpdate(pos, EnchantedBlocks.CHALICE_FILLED_MILK.get().defaultBlockState());
                    player.setItemInHand(hand, new ItemStack(Items.BUCKET, 1));
                    return InteractionResult.CONSUME;
                }
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.FAIL;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.box(0.34375, 0, 0.34375, 0.65625, 0.4375, 0.65625);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if(isFilled) {
            double x = pos.getX() + 0.4D + random.nextDouble() * 0.2D;
            double y = pos.getY() + 0.5D;
            double z = pos.getZ() + 0.4D + random.nextDouble() * 0.2D;
            level.addParticle(new DustParticleOptions(new Vector3f(3.6F, 0.2F, 0.0F), 0.6F), x, y, z, 0.0D, 0.0D, 0.0D);
        }
    }
    
}
