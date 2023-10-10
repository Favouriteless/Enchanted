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

package com.favouriteless.enchanted.common.poppet;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.common.blockentities.PoppetShelfBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.*;

public class PoppetShelfSavedData extends SavedData {

	private static final String NAME = "enchanted_poppets";
	public final Map<UUID, List<PoppetEntry>> PLAYER_POPPETS = new HashMap<>();
	public final Map<String, PoppetShelfInventory> SHELF_STORAGE = new HashMap<>();
	public final ServerLevel level;

	public PoppetShelfSavedData(ServerLevel world) {
		super();
		this.level = world;
	}

	public static PoppetShelfSavedData get(Level world) {
		if (world instanceof ServerLevel) {
			ServerLevel overworld = world.getServer().getLevel(Level.OVERWORLD);

			DimensionDataStorage storage = overworld.getDataStorage();
			return storage.computeIfAbsent((nbt) -> PoppetShelfSavedData.load(overworld, nbt),() -> new PoppetShelfSavedData(overworld), NAME);
		}
		else {
			throw new RuntimeException("Game attempted to load server-side poppet shelf data from a client-side world.");
		}
	}

	public static PoppetShelfSavedData load(ServerLevel level, CompoundTag nbt) {
		PoppetShelfSavedData data = new PoppetShelfSavedData(level);

		for(String identifier : nbt.getAllKeys()) {
			ServerLevel world = data.getLevelFromShelfIdentifier(identifier);
			BlockPos pos = data.getBlockPosFromShelfIdentifier(identifier);
			PoppetShelfInventory inventory = new PoppetShelfInventory(world, pos);

			inventory.load((CompoundTag)nbt.get(identifier));
			data.SHELF_STORAGE.put(identifier, inventory);
			data.setupPoppetUUIDs(identifier, inventory);
		}
		Enchanted.LOGGER.info("Loaded poppet shelves successfully");
		return data;
	}

	@Override
	public CompoundTag save(CompoundTag nbt) {
		for(String identifier : SHELF_STORAGE.keySet()) {
			CompoundTag tag = new CompoundTag();
			PoppetShelfInventory inventory = SHELF_STORAGE.get(identifier);
			inventory.save(tag);
			nbt.put(identifier, tag);
		}
		Enchanted.LOGGER.info("Saved poppet shelves successfully");
		return nbt;
	}

	public void updateShelf(String identifier) {
		Level level = getLevelFromShelfIdentifier(identifier);
		BlockPos pos = getBlockPosFromShelfIdentifier(identifier);
		BlockEntity blockEntity = level.getBlockEntity(pos);
		if(blockEntity instanceof PoppetShelfBlockEntity)
			((PoppetShelfBlockEntity)blockEntity).updateBlock();
	}

	public void setupPoppetUUIDs(String identifier, PoppetShelfInventory inventory) {
		for(ItemStack item : inventory) {
			setupPoppetUUID(identifier, item);
		}
	}

	public void removePoppetUUIDs(String identifier, PoppetShelfInventory inventory) {
		for(ItemStack item : inventory) {
			removePoppetUUID(identifier, item);
		}
	}

	public void setupPoppetUUID(String identifier, ItemStack itemStack) {
		if(PoppetHelper.isBound(itemStack)) {
			UUID uuid = itemStack.getTag().getUUID("boundPlayer");
			PLAYER_POPPETS.putIfAbsent(uuid, new ArrayList<>());
			PLAYER_POPPETS.get(uuid).add(new PoppetEntry(itemStack, identifier));
		}
	}

	public void removePoppetUUID(String identifier, ItemStack itemStack) {
		if(PoppetHelper.isBound(itemStack)) {
			UUID uuid = itemStack.getTag().getUUID("boundPlayer");
			PLAYER_POPPETS.get(uuid).removeIf(entry -> entry.matches(itemStack, identifier));
		}
	}

	public static String getShelfIdentifier(BlockEntity blockEntity) {
		BlockPos pos = blockEntity.getBlockPos();
		return String.format("%s+%s+%s+%s", blockEntity.getLevel().dimension().location(), pos.getX(), pos.getY(), pos.getZ());
	}

	public static String getShelfIdentifier(Level level, BlockPos pos) {
		return String.format("%s+%s+%s+%s", level.dimension().location(), pos.getX(), pos.getY(), pos.getZ());
	}

	public ServerLevel getLevelFromShelfIdentifier(String shelfIdentifier) {
		String levelString = shelfIdentifier.substring(0, shelfIdentifier.indexOf("+"));
		return level.getServer().getLevel(ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(levelString)));
	}

	public BlockPos getBlockPosFromShelfIdentifier(String shelfIdentifier) {
		String[] strings = shelfIdentifier.substring(shelfIdentifier.indexOf("+")+1).split("\\+");
		return new BlockPos(Integer.parseInt(strings[0]), Integer.parseInt(strings[1]), Integer.parseInt(strings[2]));
	}

	public record PoppetEntry(ItemStack item, String shelfIdentifier) {

		public boolean matches(ItemStack stack, String shelfIdentifier) {
			return stack.equals(item) && shelfIdentifier.equals(this.shelfIdentifier);
		}

	}

}
