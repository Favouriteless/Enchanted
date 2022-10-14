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

package com.favouriteless.enchanted.common.util.poppet;

import com.favouriteless.enchanted.Enchanted;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraft.world.storage.WorldSavedData;

import java.util.*;

public class PoppetShelfWorldSavedData extends WorldSavedData {

	private static final String NAME = "enchanted_poppets";
	public final Map<UUID, List<PoppetEntry>> PLAYER_POPPETS = new HashMap<>();
	public final Map<String, PoppetShelfInventory> SHELF_STORAGE = new HashMap<>();
	public final ServerWorld level;

	public PoppetShelfWorldSavedData(ServerWorld world) {
		super(NAME);
		this.level = world;
	}

	public static PoppetShelfWorldSavedData get(World world) {
		if (world instanceof ServerWorld) {
			ServerWorld overworld = world.getServer().getLevel(World.OVERWORLD);

			DimensionSavedDataManager storage = overworld.getDataStorage();
			return storage.computeIfAbsent(() -> new PoppetShelfWorldSavedData(overworld), NAME);
		}
		else {
			throw new RuntimeException("Game attempted to load server-side poppet shelf data from a client-side world.");
		}
	}

	@Override
	public void load(CompoundNBT nbt) {
		for(String identifier : nbt.getAllKeys()) {
			ServerWorld world = getLevelFromShelfIdentifier(identifier);
			BlockPos pos = getBlockPosFromShelfIdentifier(identifier);
			PoppetShelfInventory inventory = new PoppetShelfInventory(world, pos);

			inventory.load((CompoundNBT)nbt.get(identifier));
			SHELF_STORAGE.put(identifier, inventory);
			setupPoppetUUIDs(identifier, inventory);
		}
		Enchanted.LOGGER.info("Loaded poppet shelves successfully");
	}

	@Override
	public CompoundNBT save(CompoundNBT nbt) {
		for(String identifier : SHELF_STORAGE.keySet()) {
			CompoundNBT tag = new CompoundNBT();
			PoppetShelfInventory inventory = SHELF_STORAGE.get(identifier);
			inventory.save(tag);
			nbt.put(identifier, tag);
		}
		Enchanted.LOGGER.info("Saved poppet shelves successfully");
		return nbt;
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

	public static String getShelfIdentifier(TileEntity tileEntity) {
		BlockPos pos = tileEntity.getBlockPos();
		return String.format("%s+%s+%s+%s", tileEntity.getLevel().dimension().location(), pos.getX(), pos.getY(), pos.getZ());
	}

	public static String getShelfIdentifier(World level, BlockPos pos) {
		return String.format("%s+%s+%s+%s", level.dimension().location(), pos.getX(), pos.getY(), pos.getZ());
	}

	public ServerWorld getLevelFromShelfIdentifier(String shelfIdentifier) {
		String levelString = shelfIdentifier.substring(0, shelfIdentifier.indexOf("+"));
		return level.getServer().getLevel(RegistryKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(levelString)));
	}

	public BlockPos getBlockPosFromShelfIdentifier(String shelfIdentifier) {
		String[] strings = shelfIdentifier.substring(shelfIdentifier.indexOf("+")+1).split("\\+");
		return new BlockPos(Integer.parseInt(strings[0]), Integer.parseInt(strings[1]), Integer.parseInt(strings[2]));
	}

	public static class PoppetEntry {

		private final ItemStack item;
		private final String shelfIdentifier;

		public PoppetEntry(ItemStack item, String shelfIdentifier) {
			this.item = item;
			this.shelfIdentifier = shelfIdentifier;
		}

		public ItemStack getItem() {
			return item;
		}

		public String getShelfIdentifier() {
			return shelfIdentifier;
		}

		public boolean matches(ItemStack stack, String shelfIdentifier) {
			return stack.equals(item) && shelfIdentifier.equals(this.shelfIdentifier);
		}

	}

}
