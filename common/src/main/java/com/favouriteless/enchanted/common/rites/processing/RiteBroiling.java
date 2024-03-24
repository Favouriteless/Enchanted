package com.favouriteless.enchanted.common.rites.processing;

import com.favouriteless.enchanted.api.rites.AbstractRite;
import com.favouriteless.enchanted.common.CommonConfig;
import com.favouriteless.enchanted.common.init.registry.EnchantedBlocks;
import com.favouriteless.enchanted.common.init.registry.EnchantedItems;
import com.favouriteless.enchanted.common.init.registry.EnchantedParticles;
import com.favouriteless.enchanted.common.rites.CirclePart;
import com.favouriteless.enchanted.common.rites.RiteType;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;

import java.util.List;
import java.util.UUID;

public class RiteBroiling extends AbstractRite {

    public static final double CIRCLE_RADIUS = 3.0D;

    public RiteBroiling(RiteType<?> type, ServerLevel level, BlockPos pos, UUID caster, int power) {
        super(type, level, pos, caster, power, 0);
    }

    public RiteBroiling(RiteType<?> type, ServerLevel level, BlockPos pos, UUID caster) {
        this(type, level, pos, caster, 1000); // Power, power per tick
        CIRCLES_REQUIRED.put(CirclePart.SMALL, EnchantedBlocks.CHALK_RED.get());
        ITEMS_REQUIRED.put(Items.COAL, 1);
        ITEMS_REQUIRED.put(Items.BLAZE_ROD, 1);
        ITEMS_REQUIRED.put(EnchantedItems.WOOD_ASH.get(), 1);
    }

    @Override
    public void execute() {
        detatchFromChalk();
    }

    @Override
    public void onTick() {
        if(ticks % 5 == 0) {
            ServerLevel level = getLevel();
            BlockPos pos = getPos();

            if(level != null) {
                List<Entity> entitiesInside = CirclePart.SMALL.getEntitiesInside(level, pos, entity -> entity instanceof ItemEntity); // Get all ItemEntity
                entitiesInside.removeIf(e -> ((ItemEntity)e).getItem().getItem().getFoodProperties() == null);
                entitiesInside.sort((a, b) -> a.distanceToSqr(pos.getX() + 0.5D, pos.getY(), pos.getX() + 0.5D) > b.distanceToSqr(pos.getX() + 0.5D, pos.getY(), pos.getX() + 0.5D) ? 1 : 0); // Sort by distance.

                if(entitiesInside.isEmpty()) { // If no food left to cook
                    stopExecuting();
                    return;
                }

                ItemEntity itemEntity = (ItemEntity) entitiesInside.get(0);
                SmeltingRecipe recipe = getRecipeFor(itemEntity);
                if(recipe == null) {
                    entitiesInside.remove(0); // No recipe was found
                    return;
                }

                int totalCount = itemEntity.getItem().getCount();
                int burnedCount = 0;
                for(int i = 0; i < totalCount; i++)
                    if(Math.random() < AutoConfig.getConfigHolder(CommonConfig.class).getConfig().broilingBurnChance)
                        burnedCount++;

                level.playSound(null, itemEntity, SoundEvents.BLAZE_SHOOT, SoundSource.MASTER, 1.0F, 1.0F);
                replaceItem(itemEntity, new ItemStack(recipe.getResultItem().getItem(), recipe.getResultItem().getCount() * (totalCount - burnedCount)), new ItemStack(Items.CHARCOAL, burnedCount));

                level.sendParticles(ParticleTypes.SMALL_FLAME, itemEntity.position().x(), itemEntity.position().y(), itemEntity.position().z, 25, 0.2D, 0.2D, 0.2D, 0.0D);
                level.sendParticles(EnchantedParticles.BROILING_SEED.get(), pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, 1, 0.0D, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    private SmeltingRecipe getRecipeFor(ItemEntity item) {
        return getLevel().getRecipeManager().getAllRecipesFor(RecipeType.SMELTING)
                .stream()
                .filter(recipe -> recipe.getIngredients().get(0).test(item.getItem()))
                .findFirst()
                .orElse(null);
    }

}
