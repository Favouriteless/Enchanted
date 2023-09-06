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
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;

/**
 * Simple AbstractRite implementation for creating items with JEI compat
 */
public abstract class AbstractCreateItemRite extends AbstractRite {

    private final SoundEvent createItemSound;
    private final ItemStack[] resultItems;

    public AbstractCreateItemRite(RiteType<?> type, int power, SoundEvent createItemSound, ItemStack... resultItems) {
        super(type, power, 0);
        this.createItemSound = createItemSound;
        this.resultItems = resultItems;
    }

    @Override
    public void execute() {
        if(level != null && !level.isClientSide) {
            for(ItemStack stack : getResultItems()) {
                ItemEntity itemEntity = new ItemEntity(level, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, stack.copy());
                level.addFreshEntity(itemEntity);
            }
            level.playSound(null, pos, createItemSound, SoundSource.MASTER, 0.5F, 1.0F);
            spawnMagicParticles();
        }
        stopExecuting();
    }

    /**
     * Use a copy of these itemstacks if making any changes.
     * @return
     */
    public ItemStack[] getResultItems() {
        for(int i = 0; i < resultItems.length; i++) {
            setupItemNbt(i, resultItems[i]);
        }
        return resultItems;
    }

    /**
     * For any custom nbt which needs to be added to the itemstacks at time of execution.
     * @param index
     * @param stack
     */
    public void setupItemNbt(int index, ItemStack stack) {}

    /**
     * Return true if JEI entry should display additional requirements message, including for entities.
     * @return
     */
    public boolean hasAdditionalRequirements() {
        return false;
    }

}
