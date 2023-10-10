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
import com.favouriteless.enchanted.common.entities.*;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityType.Builder;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EnchantedEntityTypes {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, Enchanted.MOD_ID);

    public static final RegistryObject<EntityType<Mandrake>> MANDRAKE = ENTITY_TYPES.register("mandrake", () -> Builder.of(Mandrake::new, MobCategory.MONSTER)
            .sized(0.4F, 0.7F).build(Enchanted.location("mandrake").toString()));

    public static final RegistryObject<EntityType<Ent>> ENT = ENTITY_TYPES.register("ent", () -> Builder.of(Ent::new, MobCategory.MONSTER)
            .sized(2F, 3.0F).build(Enchanted.location("ent").toString()));

    public static final RegistryObject<EntityType<Broomstick>> BROOMSTICK = ENTITY_TYPES.register("broomstick", () -> Builder.of(Broomstick::new, MobCategory.MISC)
            .sized(1.0F, 1.0F).clientTrackingRange(10).build(Enchanted.location("broomstick").toString()));

    public static final RegistryObject<EntityType<ThrowableBrew>> THROWABLE_BREW = ENTITY_TYPES.register("throwable_brew", () -> Builder.of(ThrowableBrew::new, MobCategory.MISC)
            .sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10).build(Enchanted.location("throwable_brew").toString()));

    public static final RegistryObject<EntityType<FamiliarCat>> FAMILIAR_CAT = ENTITY_TYPES.register("familiar_cat", () -> Builder.of(FamiliarCat::new, MobCategory.CREATURE)
            .sized(0.6F, 0.7F).clientTrackingRange(8).build(Enchanted.location("familiar_cat").toString()));

}
