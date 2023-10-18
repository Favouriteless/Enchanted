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

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.common.CommonConfig;
import com.favouriteless.enchanted.common.init.EnchantedTags.Items;
import com.google.gson.JsonObject;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.List;

public class GrassSeedModifier extends LootModifier {

    protected GrassSeedModifier(LootItemCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Nonnull
    @Override
    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        if(AutoConfig.getConfigHolder(CommonConfig.class).getConfig().hoeOnlySeeds && !(context.getParam(LootContextParams.TOOL).getItem() instanceof HoeItem))
            return generatedLoot;

        ForgeRegistries.ITEMS.tags().getTag(Items.SEEDS_DROPS).getRandomElement(Enchanted.RANDOM).ifPresent(item -> {
            generatedLoot.add(new ItemStack(item, 1));
        });

        return generatedLoot;
    }

    public static class Serializer extends GlobalLootModifierSerializer<GrassSeedModifier> {

        @Override
        public GrassSeedModifier read(ResourceLocation name, JsonObject object, LootItemCondition[] conditionsIn) {
            return new GrassSeedModifier(conditionsIn);
        }

        @Override
        public JsonObject write(GrassSeedModifier instance) {
            return makeConditions(instance.conditions);
        }
    }
}
