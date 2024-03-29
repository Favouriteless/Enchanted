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

package com.favouriteless.enchanted.common.util;

import com.favouriteless.enchanted.common.init.EnchantedItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.UUID;

public class WaystoneHelper {

	public static BlockPos getPos(ItemStack stack) {
		if(stack.getItem() == EnchantedItems.BOUND_WAYSTONE.get()) {
			if(stack.hasTag()) {
				CompoundTag nbt = stack.getTag();
				if(nbt.contains("xPos") && nbt.contains("yPos") && nbt.contains("zPos"))
					return new BlockPos(nbt.getInt("xPos"), nbt.getInt("yPos"), nbt.getInt("zPos"));
			}
		}
		return null;
	}

	public static Level getLevel(Level level, ItemStack stack) { // Requires a level to grab server from
		if(stack.getItem() == EnchantedItems.BOUND_WAYSTONE.get()) {
			if(stack.hasTag()) {
				CompoundTag nbt = stack.getTag();
				if(nbt.contains("dimension"))
					return level.getServer().getLevel(ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(nbt.getString("dimension"))));
			}
		}
		return null;
	}

	public static Entity getEntity(Level level, ItemStack stack) { // Requires a level to grab server from
		if(stack.getItem() == EnchantedItems.BLOODED_WAYSTONE.get()) {
			if(stack.hasTag()) {
				CompoundTag nbt = stack.getTag();
				if(nbt.contains("uuid")) {
					UUID uuid = nbt.getUUID("uuid");

					ServerPlayer player = level.getServer().getPlayerList().getPlayer(uuid);
					if(player != null)
						return player;

					for(ServerLevel serverLevel : level.getServer().getAllLevels()) {
						Entity entity = serverLevel.getEntity(uuid);
						if(entity != null)
							return entity;
					}
				}
			}
		}
		return null;
	}

	public static void bind(ItemStack stack, Level level, BlockPos pos) {
		if(stack.getItem() == EnchantedItems.BOUND_WAYSTONE.get()) {
			CompoundTag nbt = stack.getOrCreateTag();
			nbt.putString("dimension", level.dimension().location().toString());
			nbt.putInt("xPos", pos.getX());
			nbt.putInt("yPos", pos.getY());
			nbt.putInt("zPos", pos.getZ());
		}
	}

	public static void bind(ItemStack stack, UUID uuid, @Nullable String name) {
		if(stack.getItem() == EnchantedItems.BLOODED_WAYSTONE.get()) {
			CompoundTag nbt = stack.getOrCreateTag();
			nbt.putUUID("uuid", uuid);
			if(name != null)
				nbt.putString("displayName", name);
		}
	}

	public static void bind(ItemStack stack, Entity entity) {
		bind(stack, entity.getUUID(), entity.getDisplayName().getString());
	}

	public static ItemStack create(Level level, BlockPos pos) {
		ItemStack stack = new ItemStack(EnchantedItems.BOUND_WAYSTONE.get(), 1);
		bind(stack, level, pos);
		return stack;
	}

	public static ItemStack create(Entity entity) {
		ItemStack stack = new ItemStack(EnchantedItems.BLOODED_WAYSTONE.get(), 1);
		bind(stack, entity);
		return stack;
	}

	public static ItemStack create(UUID uuid, @Nullable String name) {
		ItemStack stack = new ItemStack(EnchantedItems.BLOODED_WAYSTONE.get(), 1);
		bind(stack, uuid, name);
		return stack;
	}

}
