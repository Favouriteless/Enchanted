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

package com.favouriteless.enchanted.common.init;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.api.rites.AbstractRite;
import com.favouriteless.enchanted.common.rites.*;
import com.favouriteless.enchanted.common.rites.util.CircleSize;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

import java.util.HashMap;

public class EnchantedRiteTypes {

    public static final DeferredRegister<AbstractRite> RITES = DeferredRegister.create(AbstractRite.class, Enchanted.MOD_ID);

    public static final RegistryObject<AbstractRite> CHARGING_STONE = RITES.register("charging_stone", RiteOfChargingStone::new);
    public static final RegistryObject<AbstractRite> TOTAL_ECLIPSE = RITES.register("total_eclipse", RiteOfTotalEclipse::new);
    public static final RegistryObject<AbstractRite> TOTAL_ECLIPSE_CHARGED = RITES.register("total_eclipse_charged", RiteOfTotalEclipseCharged::new);



    public static AbstractRite get(HashMap<CircleSize, Block> circles, HashMap<EntityType<?>, Integer> entities, HashMap<Item, Integer> items) {
        for(RegistryObject<AbstractRite> registryObject : RITES.getEntries()) {
            AbstractRite rite = registryObject.get();
            if(rite.is(circles, entities, items)) {
                return rite;
            }
        }
        return null;
    }

    public static AbstractRite riteFor(World world, BlockPos pos) {
        AbstractRite currentRite = null;
        int currentDiff = Integer.MAX_VALUE;

        for(RegistryObject<AbstractRite> registryObject : RITES.getEntries()) {
            AbstractRite rite = registryObject.get();
            int diff = rite.differenceAt(world, pos);
            if(diff != -1 && diff < currentDiff) {
                currentRite = rite;
                currentDiff = diff;
            }
        }
        return currentRite;
    }
}
