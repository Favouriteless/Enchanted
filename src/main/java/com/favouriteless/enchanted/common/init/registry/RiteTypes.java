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

package com.favouriteless.enchanted.common.init.registry;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.api.rites.AbstractRite;
import com.favouriteless.enchanted.common.rites.binding.*;
import com.favouriteless.enchanted.common.rites.curse.*;
import com.favouriteless.enchanted.common.rites.entity.*;
import com.favouriteless.enchanted.common.rites.entity.protection.RiteProtection;
import com.favouriteless.enchanted.common.rites.entity.protection.RiteProtectionLarge;
import com.favouriteless.enchanted.common.rites.entity.protection.RiteProtectionTemporary;
import com.favouriteless.enchanted.common.rites.processing.RiteBroiling;
import com.favouriteless.enchanted.common.rites.processing.RiteBroilingCharged;
import com.favouriteless.enchanted.common.rites.processing.RiteChargingStone;
import com.favouriteless.enchanted.common.rites.processing.RiteInfusionBroom;
import com.favouriteless.enchanted.common.rites.world.*;
import com.favouriteless.enchanted.common.rites.CirclePart;
import com.favouriteless.enchanted.common.rites.RiteType;
import com.favouriteless.enchanted.core.util.RegistryHelper.Generify;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.function.Supplier;

public class RiteTypes {

    public static final DeferredRegister<RiteType<?>> RITE_TYPES = DeferredRegister.create(Enchanted.location("rites"), Enchanted.MOD_ID);
    public static final Supplier<IForgeRegistry<RiteType<?>>> REGISTRY = RITE_TYPES.makeRegistry(Generify.<RiteType<?>>from(RiteType.class), RegistryBuilder::new);

    public static final RegistryObject<RiteType<?>> BINDING_TALISMAN = RITE_TYPES.register("binding_talisman", () -> new RiteType<>(RiteBindingTalisman::new));
    public static final RegistryObject<RiteType<?>> BINDING_TALISMAN_CHARGED = RITE_TYPES.register("binding_talisman_charged", () -> new RiteType<>(RiteBindingTalismanCharged::new));
    public static final RegistryObject<RiteType<?>> BINDING_WAYSTONE = RITE_TYPES.register("binding_waystone", () -> new RiteType<>(RiteBindingWaystone::new));
    public static final RegistryObject<RiteType<?>> BINDING_WAYSTONE_CHARGED = RITE_TYPES.register("binding_waystone_charged", () -> new RiteType<>(RiteBindingWaystoneCharged::new));
    public static final RegistryObject<RiteType<?>> BINDING_WAYSTONE_DUPLICATE = RITE_TYPES.register("binding_waystone_duplicate", () -> new RiteType<>(RiteBindingWaystoneDuplicate::new));
    public static final RegistryObject<RiteType<?>> BINDING_WAYSTONE_DUPLICATE_CHARGED = RITE_TYPES.register("binding_waystone_duplicate_charged", () -> new RiteType<>(RiteBindingWaystoneDuplicateCharged::new));
    public static final RegistryObject<RiteType<?>> BINDING_WAYSTONE_PLAYER = RITE_TYPES.register("binding_waystone_player", () -> new RiteType<>(RiteBindingWaystonePlayer::new));
    public static final RegistryObject<RiteType<?>> BINDING_WAYSTONE_PLAYER_CHARGED = RITE_TYPES.register("binding_waystone_player_charged", () -> new RiteType<>(RiteBindingWaystonePlayerCharged::new));
    public static final RegistryObject<RiteType<?>> BINDING_FAMILIAR = RITE_TYPES.register("binding_familiar", () -> new RiteType<>(RiteBindingFamiliar::new));
    public static final RegistryObject<RiteType<?>> BROILING = RITE_TYPES.register("broiling", () -> new RiteType<>(RiteBroiling::new));
    public static final RegistryObject<RiteType<?>> BROILING_CHARGED = RITE_TYPES.register("broiling_charged", () -> new RiteType<>(RiteBroilingCharged::new));
    public static final RegistryObject<RiteType<?>> CHARGING_STONE = RITE_TYPES.register("charging_stone", () -> new RiteType<>(RiteChargingStone::new));
    public static final RegistryObject<RiteType<?>> CURSE_OF_BLIGHT = RITE_TYPES.register("curse_of_blight", () -> new RiteType<>(RiteCurseOfBlight::new));
    public static final RegistryObject<RiteType<?>> CURSE_OF_MISFORTUNE = RITE_TYPES.register("curse_of_misfortune", () -> new RiteType<>(RiteCurseMisfortune::new));
    public static final RegistryObject<RiteType<?>> CURSE_OF_OVERHEATING = RITE_TYPES.register("curse_of_overheating", () -> new RiteType<>(RiteCurseOverheating::new));
    public static final RegistryObject<RiteType<?>> CURSE_OF_SINKING = RITE_TYPES.register("curse_of_sinking", () -> new RiteType<>(RiteCurseSinking::new));
    public static final RegistryObject<RiteType<?>> FERTILITY = RITE_TYPES.register("fertility", () -> new RiteType<>(RiteFertility::new));
    public static final RegistryObject<RiteType<?>> FERTILITY_CHARGED = RITE_TYPES.register("fertility_charged", () -> new RiteType<>(RiteFertilityCharged::new));
    public static final RegistryObject<RiteType<?>> FOREST = RITE_TYPES.register("forest", () -> new RiteType<>(RiteForest::new));
    public static final RegistryObject<RiteType<?>> IMPRISONMENT = RITE_TYPES.register("imprisonment", () -> new RiteType<>(RiteImprisonment::new));
    public static final RegistryObject<RiteType<?>> INFUSION_BROOM = RITE_TYPES.register("infusion_broom", () -> new RiteType<>(RiteInfusionBroom::new));
    public static final RegistryObject<RiteType<?>> PROTECTION = RITE_TYPES.register("protection", () -> new RiteType<>(RiteProtection::new));
    public static final RegistryObject<RiteType<?>> PROTECTION_LARGE = RITE_TYPES.register("protection_large", () -> new RiteType<>(RiteProtectionLarge::new));
    public static final RegistryObject<RiteType<?>> PROTECTION_TEMPORARY = RITE_TYPES.register("protection_temporary", () -> new RiteType<>(RiteProtectionTemporary::new));
    public static final RegistryObject<RiteType<?>> REMOVE_MISFORTUNE = RITE_TYPES.register("remove_misfortune", () -> new RiteType<>(RiteRemoveMisfortune::new));
    public static final RegistryObject<RiteType<?>> REMOVE_OVERHEATING = RITE_TYPES.register("remove_overheating", () -> new RiteType<>(RiteRemoveOverheating::new));
    public static final RegistryObject<RiteType<?>> REMOVE_SINKING = RITE_TYPES.register("remove_sinking", () -> new RiteType<>(RiteRemoveSinking::new));
    public static final RegistryObject<RiteType<?>> SANCTITY = RITE_TYPES.register("sanctity", () -> new RiteType<>(RiteSanctity::new));
    public static final RegistryObject<RiteType<?>> SKY_WRATH = RITE_TYPES.register("sky_wrath", () -> new RiteType<>(RiteSkyWrath::new));
    public static final RegistryObject<RiteType<?>> SKY_WRATH_CHARGED = RITE_TYPES.register("sky_wrath_charged", () -> new RiteType<>(RiteSkyWrathCharged::new));
    public static final RegistryObject<RiteType<?>> SUMMONING_ENTITY = RITE_TYPES.register("summoning_entity", () -> new RiteType<>(RiteSummonEntity::new));
    public static final RegistryObject<RiteType<?>> SUMMONING_FAMILIAR = RITE_TYPES.register("summoning_familiar", () -> new RiteType<>(RiteSummonFamiliar::new));
    public static final RegistryObject<RiteType<?>> TOTAL_ECLIPSE = RITE_TYPES.register("total_eclipse", () -> new RiteType<>(RiteTotalEclipse::new));
    public static final RegistryObject<RiteType<?>> TOTAL_ECLIPSE_CHARGED = RITE_TYPES.register("total_eclipse_charged", () -> new RiteType<>(RiteTotalEclipseCharged::new));
    public static final RegistryObject<RiteType<?>> TRANSPOSITION_IRON = RITE_TYPES.register("transposition_iron", () -> new RiteType<>(RiteTranspositionIron::new));
    public static final RegistryObject<RiteType<?>> TRANSPOSITION_PLAYER = RITE_TYPES.register("transposition_player", () -> new RiteType<>(RiteTranspositionPlayer::new));
    public static final RegistryObject<RiteType<?>> TRANSPOSITION_PLAYER_BLOODED = RITE_TYPES.register("transposition_player_blooded", () -> new RiteType<>(RiteTranspositionPlayerBlooded::new));



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

    public static AbstractRite riteAvailableAt(Level world, BlockPos pos) {
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

}
