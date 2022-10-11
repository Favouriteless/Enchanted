/*
 * Copyright (c) 2022. Favouriteless
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
import com.favouriteless.enchanted.common.util.CirclePart;
import com.favouriteless.enchanted.common.util.RiteType;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.HashMap;
import java.util.function.Supplier;

public class EnchantedRiteTypes {

    public static final DeferredRegister<RiteType<?>> RITE_TYPES = DeferredRegister.create(Generify.<RiteType<?>>from(RiteType.class), Enchanted.MOD_ID);
    public static final Supplier<IForgeRegistry<RiteType<?>>> REGISTRY = RITE_TYPES.makeRegistry("rite_types", RegistryBuilder::new);

    public static final RegistryObject<RiteType<?>> CHARGING_STONE = RITE_TYPES.register("charging_stone", () -> new RiteType<>(RiteOfChargingStone::new));

    public static final RegistryObject<RiteType<?>> TOTAL_ECLIPSE = RITE_TYPES.register("total_eclipse", () -> new RiteType<>(RiteOfTotalEclipse::new));
    public static final RegistryObject<RiteType<?>> TOTAL_ECLIPSE_CHARGED = RITE_TYPES.register("total_eclipse_charged", () -> new RiteType<>(RiteOfTotalEclipseCharged::new));

    public static final RegistryObject<RiteType<?>> BINDING_TALISMAN = RITE_TYPES.register("binding_talisman", () -> new RiteType<>(RiteOfBindingTalisman::new));
    public static final RegistryObject<RiteType<?>> BINDING_TALISMAN_CHARGED = RITE_TYPES.register("binding_talisman_charged", () -> new RiteType<>(RiteOfBindingTalismanCharged::new));
    public static final RegistryObject<RiteType<?>> BINDING_WAYSTONE = RITE_TYPES.register("binding_waystone", () -> new RiteType<>(RiteOfBindingWaystone::new));
    public static final RegistryObject<RiteType<?>> BINDING_WAYSTONE_CHARGED = RITE_TYPES.register("binding_waystone_charged", () -> new RiteType<>(RiteOfBindingWaystoneCharged::new));
    public static final RegistryObject<RiteType<?>> BINDING_WAYSTONE_DUPLICATE = RITE_TYPES.register("binding_waystone_duplicate", () -> new RiteType<>(RiteOfBindingWaystoneDuplicate::new));
    public static final RegistryObject<RiteType<?>> BINDING_WAYSTONE_DUPLICATE_CHARGED = RITE_TYPES.register("binding_waystone_duplicate_charged", () -> new RiteType<>(RiteOfBindingWaystoneDuplicateCharged::new));
    public static final RegistryObject<RiteType<?>> BINDING_WAYSTONE_ENTITY = RITE_TYPES.register("binding_waystone_entity", () -> new RiteType<>(RiteOfBindingWaystoneEntity::new));
    public static final RegistryObject<RiteType<?>> BINDING_WAYSTONE_ENTITY_CHARGED = RITE_TYPES.register("binding_waystone_entity_charged", () -> new RiteType<>(RiteOfBindingWaystoneEntityCharged::new));

    public static final RegistryObject<RiteType<?>> INFUSION_BROOM = RITE_TYPES.register("infusion_broom", () -> new RiteType<>(RiteOfInfusionBroom::new));

    public static final RegistryObject<RiteType<?>> SANCTITY = RITE_TYPES.register("sanctity", () -> new RiteType<>(RiteOfSanctity::new));

    public static final RegistryObject<RiteType<?>> TRANSPOSITION_PLAYER = RITE_TYPES.register("transposition_player", () -> new RiteType<>(RiteOfTranspositionPlayer::new));
    public static final RegistryObject<RiteType<?>> SUMMONING_ENTITY = RITE_TYPES.register("summoning_entity", () -> new RiteType<>(RiteOfSummoningEntity::new));


    public static AbstractRite getFromRequirements(HashMap<CirclePart, Block> circles, HashMap<EntityType<?>, Integer> entities, HashMap<Item, Integer> items) {
        for(RegistryObject<RiteType<?>> registryObject : RITE_TYPES.getEntries()) {
            RiteType<?> type = registryObject.get();
            AbstractRite rite = type.create();
            if(rite.is(circles, entities, items)) {
                return rite;
            }
        }
        return null;
    }

    public static AbstractRite riteAvailableAt(World world, BlockPos pos) {
        AbstractRite currentRite = null;
        int currentDiff = Integer.MAX_VALUE;

        for(RegistryObject<RiteType<?>> registryObject : RITE_TYPES.getEntries()) {
            RiteType<?> type = registryObject.get();
            AbstractRite rite = type.create();
            int diff = rite.differenceAt(world, pos);
            if(diff != -1 && diff < currentDiff) {
                currentRite = rite;
                currentDiff = diff;
            }
        }
        return currentRite;
    }

    public static AbstractRite getByName(ResourceLocation loc) {
        for(RegistryObject<RiteType<?>> registryObject : RITE_TYPES.getEntries()) {
            if(registryObject.getId().equals(loc))
                return registryObject.get().create();
        }
        return null;
    }

    private static class Generify {

        @SuppressWarnings("unchecked")
        public static <T extends IForgeRegistryEntry<T>> Class<T> from(Class<? super T> cls)
        {
            return (Class<T>) cls;
        }

    }

}
