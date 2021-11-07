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
import com.favouriteless.enchanted.common.entities.mandrake.MandrakeEntity;
import com.favouriteless.enchanted.common.entities.mandrake.MandrakeRenderer;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EnchantedEntityTypes {

    public static void registerEntityRenderers() {
        RenderingRegistry.registerEntityRenderingHandler(EnchantedEntityTypes.MANDRAKE.get(), MandrakeRenderer::new);
    }

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, Enchanted.MOD_ID);

    public static final RegistryObject<EntityType<MandrakeEntity>> MANDRAKE = ENTITY_TYPES.register("mandrake", () -> EntityType.Builder.<MandrakeEntity>of(MandrakeEntity::new, EntityClassification.MONSTER)
            .sized(0.4F, 0.7F)
            .build(new ResourceLocation(Enchanted.MOD_ID, "mandrake").toString()));

}
