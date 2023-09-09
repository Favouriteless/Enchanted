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

package com.favouriteless.enchanted.api.rites;

import com.favouriteless.enchanted.common.rites.RiteType;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

/**
 * Simple implementation of {@link AbstractRite} for creating and spawning a list of {@link ItemStack}s on execution.
 *
 * <p>Rites implementing this will automatically have compatibility for JEI.</p>
 */
public abstract class AbstractCreateItemRite extends AbstractRite {

    private final SoundEvent createItemSound;
    private final ItemStack[] resultItems;

    public AbstractCreateItemRite(RiteType<?> type, ServerLevel level, BlockPos pos, UUID caster, int power, SoundEvent createItemSound, ItemStack... resultItems) {
        super(type, level, pos, caster, power, 0);
        this.createItemSound = createItemSound;
        this.resultItems = resultItems;
    }

    @Override
    public void execute() {
        if(getLevel() != null && !getLevel().isClientSide) {
            BlockPos pos = getPos();
            for(ItemStack stack : getResultItems()) {
                ItemEntity itemEntity = new ItemEntity(getLevel(), pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, stack.copy());
                getLevel().addFreshEntity(itemEntity);
            }
            getLevel().playSound(null, pos, createItemSound, SoundSource.MASTER, 0.5F, 1.0F);
            spawnMagicParticles();
        }
        stopExecuting();
    }

    /**
     * Use a copy of these itemstacks if making any changes.
     * @return An array containing the {@link ItemStack}s created by this rite.
     */
    public ItemStack[] getResultItems() {
        for(int i = 0; i < resultItems.length; i++) {
            setupItemNbt(i, resultItems[i]);
        }
        return resultItems;
    }

    /**
     * For any custom nbt which needs to be added to the {@link ItemStack}s at time of execution.
     * @param index The index of the {@link ItemStack} within the result items array.
     * @param stack The {@link ItemStack} being modified.
     */
    public void setupItemNbt(int index, ItemStack stack) {}

    /**
     * @return True if JEI entry should display additional requirements message, this includes the list of required
     * entities.
     */
    public boolean hasAdditionalRequirements() {
        return false;
    }

}
