/*
 *
 *   Copyright (c) 2022. Favouriteless
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
import com.favouriteless.enchanted.EnchantedConfig;
import com.favouriteless.enchanted.common.init.EnchantedItems;
import com.favouriteless.enchanted.common.init.EnchantedTags;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GrassSeedModifier extends LootModifier {

    private static final List<Item> seeds = Arrays.asList(
            EnchantedItems.ARTICHOKE_SEEDS.get(),
            EnchantedItems.BELLADONNA_SEEDS.get(),
            EnchantedItems.MANDRAKE_SEEDS.get(),
            EnchantedItems.SNOWBELL_SEEDS.get(),
            EnchantedItems.WOLFSBANE_SEEDS.get(),
            EnchantedItems.GARLIC.get()
    );

    protected GrassSeedModifier(LootItemCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Nonnull
    @Override
    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        if(EnchantedConfig.HOE_ONLY_SEEDS.get() && !ForgeRegistries.ITEMS.tags().getTag(EnchantedTags.Items.HOES).contains(context.getParam(LootContextParams.TOOL).getItem()))
            return generatedLoot;

        List<ItemStack> outputItems = new ArrayList<>();

        if(!generatedLoot.isEmpty()) {
            for(ItemStack item : generatedLoot) {
                if(item.getItem() == Items.WHEAT_SEEDS) {
                    outputItems.add(new ItemStack(
                            seeds.get(Enchanted.RANDOM.nextInt(seeds.size())),
                            item.getCount()));
                }
                else {
                    outputItems.add(item);
                }
            }
        }

        return outputItems;
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
