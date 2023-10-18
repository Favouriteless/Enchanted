package com.favouriteless.enchanted.common.blocks.crops;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.common.entities.Mandrake;
import com.favouriteless.enchanted.common.init.registry.EnchantedBlocks;
import com.favouriteless.enchanted.common.init.registry.EnchantedEntityTypes;
import com.favouriteless.enchanted.common.init.registry.EnchantedItems;
import net.minecraft.core.BlockPos;
import net.minecraft.stats.Stats;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class MandrakeBlock extends CropsBlockAgeFive {

    public MandrakeBlock(Properties properties) {
        super(properties);
    }

    protected ItemLike getBaseSeedId() {
        return EnchantedItems.MANDRAKE_SEEDS.get();
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack stack) {
        player.awardStat(Stats.BLOCK_MINED.get(this));
        player.causeFoodExhaustion(0.005F);

        if(!level.isClientSide() && level.getDifficulty() != Difficulty.PEACEFUL) {
            if (state.is(EnchantedBlocks.MANDRAKE.get()) && state.getValue(AGE_FIVE) == 4) {
                if (level.isDay()) {
                    spawnMandrake(level, pos);
                    return;
                } else { // 1/5 Chance to "wake up" mandrake at night
                    if (Enchanted.RANDOM.nextInt(5) == 0) {
                        spawnMandrake(level, pos);
                        return;
                    }
                }
            }
        }
        dropResources(state, level, pos, blockEntity, player, stack);
    }

    public static void spawnMandrake(Level level, BlockPos pos) {
        Mandrake entity = EnchantedEntityTypes.MANDRAKE.get().create(level);
        entity.moveTo(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, 0.0F, 0.0F);
        level.addFreshEntity(entity);
    }

}
