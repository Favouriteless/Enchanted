package com.favouriteless.enchanted.common.items;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.common.init.EnchantedTags;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class MutandisItem extends Item {

    private final TagKey<Block> validBlocks;

    public MutandisItem(TagKey<Block> validBlocks, Properties properties) {
        super(properties);
        this.validBlocks = validBlocks;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if(ForgeRegistries.BLOCKS.tags().getTag(EnchantedTags.Blocks.MUTANDIS_PLANTS).stream().allMatch(block -> ForgeRegistries.BLOCKS.tags().getTag(EnchantedTags.Blocks.MUTANDIS_BLACKLIST).contains(block)))
            return InteractionResult.FAIL; // If all whitelisted plants are blacklisted

        BlockState state = context.getLevel().getBlockState(context.getClickedPos());
        if(!ForgeRegistries.BLOCKS.tags().getTag(EnchantedTags.Blocks.MUTANDIS_BLACKLIST).contains(state.getBlock()) && ForgeRegistries.BLOCKS.tags().getTag(validBlocks).contains(state.getBlock())) {
            Level world = context.getLevel();
            if(!world.isClientSide) {

                BlockState newState = null;
                while(newState == null) {
                    Block newBlock = ForgeRegistries.BLOCKS.tags().getTag(validBlocks).getRandomElement(Enchanted.RANDOM).get();
                    if(!ForgeRegistries.BLOCKS.tags().getTag(EnchantedTags.Blocks.MUTANDIS_BLACKLIST).contains(newBlock)) {
                        newState = newBlock.defaultBlockState();
                    }
                }

                world.setBlockAndUpdate(context.getClickedPos(), newState);
                world.playSound(null, context.getClickedPos(), SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 1.0F, 1.0F);
                if(!context.getPlayer().isCreative()) context.getItemInHand().shrink(1);
                return InteractionResult.CONSUME;
            }
            else {
                for(int i = 0; i < 10; i++) {
                    double dx = context.getClickedPos().getX() + Math.random();
                    double dy = context.getClickedPos().getY() + Math.random();
                    double dz = context.getClickedPos().getZ() + Math.random();
                    world.addParticle(ParticleTypes.WITCH, dx, dy, dz, 0.0D, 0.0D, 0.0D);
                }
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.FAIL;
    }
}
