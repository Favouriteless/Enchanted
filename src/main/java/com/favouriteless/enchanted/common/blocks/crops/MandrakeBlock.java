package com.favouriteless.enchanted.common.blocks.crops;

import com.favouriteless.enchanted.common.entities.mandrake.MandrakeEntity;
import com.favouriteless.enchanted.core.init.EnchantedBlocks;
import com.favouriteless.enchanted.core.init.EnchantedEntityTypes;
import com.favouriteless.enchanted.core.init.EnchantedItems;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class MandrakeBlock extends CropsBlockAgeFive {

    public MandrakeBlock(Properties properties) {
        super(properties);
    }

    protected IItemProvider getBaseSeedId() {
        return EnchantedItems.MANDRAKE_SEEDS.get();
    }

    @Override
    public void playerDestroy(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable TileEntity tileEntity, ItemStack stack) {
        player.awardStat(Stats.BLOCK_MINED.get(this));
        player.causeFoodExhaustion(0.005F);

        if(!world.isClientSide() && world.getDifficulty() != Difficulty.PEACEFUL) {
            if (state.is(EnchantedBlocks.MANDRAKE.get()) && state.getValue(AGE_FIVE) == 4) {
                if (world.isDay()) {
                    spawnMandrake(world, pos);
                    return;
                } else { // 1/5 Chance to "wake up" mandrake at night
                    if (RANDOM.nextInt(5) == 0) {
                        spawnMandrake(world, pos);
                        return;
                    }
                }
            }
        }
        dropResources(state, world, pos, tileEntity, player, stack);
    }

    public static void spawnMandrake(World world, BlockPos pos) {
        MandrakeEntity entity = EnchantedEntityTypes.MANDRAKE.get().create(world);
        entity.moveTo(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, 0.0F, 0.0F);
        world.addFreshEntity(entity);
    }

}
