/*
 *
 *   Copyright (c) 2023. Favouriteless
 *   Enchanted, a minecraft mod.
 *   GNU GPLv3 License
 *
 *       This file is part of Enchanted.
 *
 *       Enchanted is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU General Public License as published by
 *       the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       Enchanted is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU General Public License for more details.
 *
 *       You should have received a copy of the GNU General Public License
 *       along with Enchanted.  If not, see <https://www.gnu.org/licenses/>.
 *
 *
 */

package com.favouriteless.enchanted.common.rites.processing;

import com.favouriteless.enchanted.EnchantedConfig;
import com.favouriteless.enchanted.api.rites.AbstractRite;
import com.favouriteless.enchanted.common.init.EnchantedItems;
import com.favouriteless.enchanted.common.init.EnchantedTags;
import com.favouriteless.enchanted.common.init.registry.EnchantedBlocks;
import com.favouriteless.enchanted.common.init.registry.EnchantedParticles;
import com.favouriteless.enchanted.common.rites.CirclePart;
import com.favouriteless.enchanted.common.rites.RiteType;
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
import net.minecraftforge.registries.ForgeRegistries;

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
                entitiesInside.removeIf(entity -> !ForgeRegistries.ITEMS.tags().getTag(EnchantedTags.Items.RAW_FOODS).contains(((ItemEntity) entity).getItem().getItem())); // Remove if not raw food
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
                    if(Math.random() < EnchantedConfig.BROILING_BURN_CHANCE.get())
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
