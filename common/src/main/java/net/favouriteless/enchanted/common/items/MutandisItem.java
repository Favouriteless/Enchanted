package net.favouriteless.enchanted.common.items;

import net.favouriteless.enchanted.common.Enchanted;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet.Named;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
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

    private final TagKey<Block> validTag;
    private final TagKey<Block> invalidTag;

    public MutandisItem(TagKey<Block> validTag, TagKey<Block> invalidTag) {
        super(new Properties());
        this.validTag = validTag;
        this.invalidTag = invalidTag;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);

        if(state.is(invalidTag) || !state.is(validTag))
            return InteractionResult.PASS;

        Named<Block> validBlocks = BuiltInRegistries.BLOCK.getOrCreateTag(validTag);
        Named<Block> invalidBlocks = BuiltInRegistries.BLOCK.getOrCreateTag(invalidTag);

        Block[] options = validBlocks.stream()
                .filter(b -> !invalidBlocks.contains(b))
                .map(Holder::value)
                .toArray(Block[]::new);

        if(options.length == 0)
            return InteractionResult.PASS;

        if(!level.isClientSide) {
            level.setBlockAndUpdate(pos, options[Enchanted.RANDOM.nextInt(options.length)].defaultBlockState());
            level.playSound(null, pos, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 1.0F, 1.0F);

            if(!context.getPlayer().isCreative())
                context.getItemInHand().shrink(1);
        }
        else {
            for(int i = 0; i < 10; i++) {
                double dx = pos.getX() + Math.random();
                double dy = pos.getY() + Math.random();
                double dz = pos.getZ() + Math.random();
                level.addParticle(ParticleTypes.WITCH, dx, dy, dz, 0, 0, 0);
            }
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}
