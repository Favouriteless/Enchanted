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
import net.minecraft.block.Block;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;

public class EnchantedTags {

    public static final Tags.IOptionalNamedTag<Block> POWER_CONSUMERS = BlockTags.createOptional(new ResourceLocation(Enchanted.MOD_ID, "power_consumers"));
    public static final Tags.IOptionalNamedTag<Block> MUTANDIS_PLANTS = BlockTags.createOptional(new ResourceLocation(Enchanted.MOD_ID, "mutandis_plants"));
    public static final Tags.IOptionalNamedTag<Block> MUTANDIS_EXTREMIS_PLANTS = BlockTags.createOptional(new ResourceLocation(Enchanted.MOD_ID, "mutandis_extremis_plants"));
    public static final Tags.IOptionalNamedTag<Block> MUTANDIS_BLACKLIST = BlockTags.createOptional(new ResourceLocation(Enchanted.MOD_ID, "mutandis_blacklist_plants"));
    public static final Tags.IOptionalNamedTag<Block> CHALKS = BlockTags.createOptional(new ResourceLocation(Enchanted.MOD_ID, "chalks"));

}
