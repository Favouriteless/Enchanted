/*
 * Copyright (c) 2021. Favouriteless
 * Enchanted, a minecraft mod.
 * GNU GPLv3 License
 *
 *     This file is part of Enchanted.
 *
 *     Enchanted is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Enchanted is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Enchanted.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.favouriteless.enchanted.api.rites;

import com.favouriteless.enchanted.common.init.EnchantedBlocks;
import com.favouriteless.enchanted.common.rites.util.CircleSize;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Items;

public class RiteTemplate extends AbstractRite {

    public RiteTemplate() {
        super(3000, 0); // Power, power per tick
        CIRCLES_REQUIRED.put(CircleSize.SMALL, EnchantedBlocks.CHALK_WHITE.get()); // Add required circles
        ITEMS_REQUIRED.put(Items.DIAMOND, 5); // Add item, count required for ritual
        ENTITIES_REQUIRED.put(EntityType.PIG, 1);  // Add entity, count required for ritual
    }

    @Override
    public void execute() {
        // Do ritual effects here
    }

    @Override
    public void onTick() {
        // Do tick based ritual stuff here
    }

    @Override
    public RiteTemplate create() {
        return new RiteTemplate();
    }

}
