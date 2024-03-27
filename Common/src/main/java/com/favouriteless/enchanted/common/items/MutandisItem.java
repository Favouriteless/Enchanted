package com.favouriteless.enchanted.common.items;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.common.init.EnchantedTags.Blocks;
import net.minecraft.core.HolderSet.Named;
import net.minecraft.core.Registry;
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
        BlockState state = context.getLevel().getBlockState(context.getClickedPos());

        Named<Block> blacklistTag = Registry.BLOCK.getOrCreateTag(Blocks.MUTANDIS_BLACKLIST);
        Named<Block> validTag = Registry.BLOCK.getOrCreateTag(validBlocks);

        if(validTag.size() == 0 || validTag.stream().allMatch(blacklistTag::contains)) { // This check prevents the while loop below from becoming infinite.
            Enchanted.LOG.error("Mutandis tag is invalid! This means the tag is empty, or every item in it is blacklisted.");
            return InteractionResult.FAIL;
        }

        if(!state.is(blacklistTag) && state.is(validTag)) {
            Level level = context.getLevel();
            if(!level.isClientSide) {

                BlockState newState = null;
                while(newState == null) {
                    // This CAN throw an NPE, but it shouldn't as the above check ensures that validTag does have values it can use.
                    BlockState _state = validTag.getRandomElement(Enchanted.RANDOMSOURCE).orElse(null).value().defaultBlockState();
                    if(!_state.is(Blocks.MUTANDIS_BLACKLIST))
                        newState = _state;
                }

                level.setBlockAndUpdate(context.getClickedPos(), newState);
                level.playSound(null, context.getClickedPos(), SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 1.0F, 1.0F);
                if(!context.getPlayer().isCreative()) context.getItemInHand().shrink(1);
                return InteractionResult.CONSUME;
            }
            else {
                for(int i = 0; i < 10; i++) {
                    double dx = context.getClickedPos().getX() + Math.random();
                    double dy = context.getClickedPos().getY() + Math.random();
                    double dz = context.getClickedPos().getZ() + Math.random();
                    level.addParticle(ParticleTypes.WITCH, dx, dy, dz, 0.0D, 0.0D, 0.0D);
                }
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.FAIL;
    }
}
