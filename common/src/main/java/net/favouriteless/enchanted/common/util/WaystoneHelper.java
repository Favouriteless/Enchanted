package net.favouriteless.enchanted.common.util;

import net.favouriteless.enchanted.common.init.registry.EItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.UUID;

public class WaystoneHelper {

	public static final String X = "xPos";
	public static final String Y = "yPos";
	public static final String Z = "zPos";
	public static final String DIMENSION = "dimension";
	public static final String UUID = "uuid";
	public static final String NAME = "displayName";

	public static BlockPos getPos(ItemStack stack) {
		if(stack.getItem() != EItems.BOUND_WAYSTONE.get())
			return null;

		if(!stack.hasTag())
			return null;

		CompoundTag nbt = stack.getTag();
		if(!nbt.contains(X) || !nbt.contains(Y) || !nbt.contains(Z))
			return null;

		return new BlockPos(nbt.getInt(X), nbt.getInt(Y), nbt.getInt(Z));
	}

	public static Level getLevel(Level level, ItemStack stack) { // Requires a level to grab server from
		if(stack.getItem() != EItems.BOUND_WAYSTONE.get())
			return null;

		if(!stack.hasTag())
			return null;

		CompoundTag nbt = stack.getTag();
		if(!nbt.contains(DIMENSION))
			return null;

		return level.getServer().getLevel(ResourceKey.create(Registries.DIMENSION, new ResourceLocation(nbt.getString(DIMENSION))));
	}

	public static Entity getEntity(Level level, ItemStack stack) { // Requires a level to grab server from
		if(stack.getItem() == EItems.BLOODED_WAYSTONE.get()) {
			if(stack.hasTag()) {
				CompoundTag nbt = stack.getTag();
				if(nbt.contains(UUID)) {
					UUID uuid = nbt.getUUID(UUID);

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
		if(stack.getItem() == EItems.BOUND_WAYSTONE.get()) {
			CompoundTag nbt = stack.getOrCreateTag();
			nbt.putString(DIMENSION, level.dimension().location().toString());
			nbt.putInt(X, pos.getX());
			nbt.putInt(Y, pos.getY());
			nbt.putInt(Z, pos.getZ());
		}
	}

	public static void bind(ItemStack stack, UUID uuid, @Nullable String name) {
		if(stack.getItem() == EItems.BLOODED_WAYSTONE.get()) {
			CompoundTag nbt = stack.getOrCreateTag();
			nbt.putUUID(UUID, uuid);
			if(name != null)
				nbt.putString(NAME, name);
		}
	}

	public static void bind(ItemStack stack, Entity entity) {
		bind(stack, entity.getUUID(), entity.getDisplayName().getString());
	}

	public static ItemStack create(Level level, BlockPos pos) {
		ItemStack stack = new ItemStack(EItems.BOUND_WAYSTONE.get(), 1);
		bind(stack, level, pos);
		return stack;
	}

	public static ItemStack create(Entity entity) {
		ItemStack stack = new ItemStack(EItems.BLOODED_WAYSTONE.get(), 1);
		bind(stack, entity);
		return stack;
	}

	public static ItemStack create(UUID uuid, @Nullable String name) {
		ItemStack stack = new ItemStack(EItems.BLOODED_WAYSTONE.get(), 1);
		bind(stack, uuid, name);
		return stack;
	}

}
