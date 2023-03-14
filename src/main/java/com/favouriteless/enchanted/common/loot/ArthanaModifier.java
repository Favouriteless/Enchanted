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

package com.favouriteless.enchanted.common.loot;

import com.favouriteless.enchanted.common.init.EnchantedData;
import com.favouriteless.enchanted.common.init.EnchantedItems;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;

import javax.annotation.Nonnull;
import java.util.List;

public class ArthanaModifier extends LootModifier {

    protected ArthanaModifier(LootItemCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Nonnull
    @Override
    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        DamageSource source = context.getParamOrNull(LootContextParams.DAMAGE_SOURCE);
        if(source != null) {
            Entity entity = source.getDirectEntity();
            if(entity != null) {
                if(entity instanceof LivingEntity livingEntity) {
                    ItemStack item = livingEntity.getMainHandItem();

                    if(item.getItem() == EnchantedItems.ARTHANA.get()) {
                        EntityType<?> entityKilled = context.getParamOrNull(LootContextParams.THIS_ENTITY).getType();
                        ItemStack extraLoot = EnchantedData.ARTHANA_LOOT.getLootFor(entityKilled);
                        if(extraLoot != null)
                            generatedLoot.add(extraLoot);
                    }
                }
            }
        }
        return generatedLoot;
    }

    public static class Serializer extends GlobalLootModifierSerializer<ArthanaModifier> {

        @Override
        public ArthanaModifier read(ResourceLocation name, JsonObject object, LootItemCondition[] conditionsIn) {
            return new ArthanaModifier(conditionsIn);
        }

        @Override
        public JsonObject write(ArthanaModifier instance) {
            return makeConditions(instance.conditions);
        }
    }
}