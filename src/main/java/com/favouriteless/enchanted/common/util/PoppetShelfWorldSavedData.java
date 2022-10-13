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

package com.favouriteless.enchanted.common.util;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.common.items.poppets.AbstractPoppetItem;
import com.favouriteless.enchanted.common.tileentity.PoppetShelfTileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;

public class PoppetShelfWorldSavedData extends WorldSavedData {

	private static final String NAME = "enchanted_poppets";
	public final Map<UUID, List<PoppetEntry>> PLAYER_POPPETS = new HashMap<>();
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
		ListNBT playerEntries = nbt.getList("playerEntries", 10);

		for(INBT inbt : playerEntries) {
			if(inbt instanceof CompoundNBT) {
				CompoundNBT tag = (CompoundNBT)inbt;
				if(tag.contains("uuid") && tag.contains("poppetEntries")) {
					List<PoppetEntry> poppetEntries = new ArrayList<>();
					UUID uuid = tag.getUUID("uuid");

					for(INBT poppetInbt : tag.getList("poppetEntries", 10)) {
						if(poppetInbt instanceof CompoundNBT) {
							CompoundNBT poppetTag = (CompoundNBT)poppetInbt;
							BlockPos pos = new BlockPos(poppetTag.getInt("xPos"), poppetTag.getInt("yPos"), poppetTag.getInt("zPos"));
							ServerWorld level = this.level.getServer().getLevel(RegistryKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(poppetTag.getString("dimension"))));
							Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(poppetTag.getString("item")));
							if(level != null && item instanceof AbstractPoppetItem)
								poppetEntries.add(new PoppetEntry((AbstractPoppetItem)item, uuid, level, pos));
						}
					}
					if(!poppetEntries.isEmpty())
						PLAYER_POPPETS.put(uuid, poppetEntries);
				}
			}
		}

		Enchanted.LOGGER.info("Loaded poppet shelves successfully");
	}

	@Override
	public CompoundNBT save(CompoundNBT nbt) {
		ListNBT playerEntries = new ListNBT();
		for(UUID uuid : PLAYER_POPPETS.keySet()) {
			CompoundNBT entry = new CompoundNBT();

			List<PoppetEntry> poppetEntryList = PLAYER_POPPETS.get(uuid);
			ListNBT poppetsNbt = new ListNBT();
			for(PoppetEntry poppetEntry : poppetEntryList) {
				CompoundNBT itemNbt = new CompoundNBT();
				itemNbt.putString("item", poppetEntry.getItem().getRegistryName().toString());
				itemNbt.putString("dimension", poppetEntry.getLevel().dimension().location().toString());
				itemNbt.putInt("xPos", poppetEntry.getPos().getX());
				itemNbt.putInt("yPos", poppetEntry.getPos().getY());
				itemNbt.putInt("zPos", poppetEntry.getPos().getZ());
				poppetsNbt.add(itemNbt);
			}

			entry.putUUID("uuid", uuid);
			entry.put("poppetEntries", poppetsNbt);
			playerEntries.add(entry);
		}

		nbt.put("playerEntries", playerEntries);
		Enchanted.LOGGER.info("Saved poppet shelves successfully");
		return nbt;
	}

	public static class PoppetEntry {

		private final AbstractPoppetItem item;
		private final UUID uuid;
		private final ServerWorld level;
		private final BlockPos shelfPos;

		public PoppetEntry(AbstractPoppetItem item, UUID uuid, ServerWorld level, BlockPos shelfPos) {
			this.item = item;
			this.uuid = uuid;
			this.shelfPos = shelfPos;
			this.level = level;
		}

		public AbstractPoppetItem getItem() {
			return item;
		}

		public BlockPos getPos() {
			return shelfPos;
		}

		public ServerWorld getLevel() {
			return level;
		}

		public UUID getUUID() {
			return uuid;
		}

		public boolean matches(ItemStack stack, PoppetShelfTileEntity tileEntity) {
			if(stack.getItem() == item)
				if(tileEntity.getLevel().dimension() == level.dimension())
					if(PoppetHelper.belongsTo(stack, uuid))
						return tileEntity.getBlockPos().equals(shelfPos);
			return false;
		}

		public boolean matches(ItemStack stack) {
			return stack.getItem() == item && PoppetHelper.belongsTo(stack, uuid);
		}

		public ItemStack getItemStack() {
			TileEntity tileEntity = level.getBlockEntity(shelfPos);
			if(tileEntity instanceof PoppetShelfTileEntity) {
				PoppetShelfTileEntity shelf = (PoppetShelfTileEntity)tileEntity;
				for(ItemStack itemStack : shelf.getItems()) {
					if(matches(itemStack))
						return itemStack;
				}
			}
			return ItemStack.EMPTY;
		}

	}

}
