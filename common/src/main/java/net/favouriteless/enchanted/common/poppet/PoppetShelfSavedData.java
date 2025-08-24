package net.favouriteless.enchanted.common.poppet;

import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.enchanted.common.blocks.entity.PoppetShelfBlockEntity;
import net.favouriteless.enchanted.common.items.component.EDataComponents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
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

	public static PoppetShelfSavedData get(Level level) {
		if (level instanceof ServerLevel) {
			ServerLevel overworld = level.getServer().overworld();
			return overworld.getDataStorage().computeIfAbsent(new Factory<>(
					() -> new PoppetShelfSavedData(overworld),
					(nbt, reg) -> PoppetShelfSavedData.load(overworld, nbt, reg),
					null), NAME);
		}
		else {
			throw new RuntimeException("Game attempted to load server-side poppet shelf data from a client-side world.");
		}
	}

	public static PoppetShelfSavedData load(ServerLevel level, CompoundTag nbt, Provider registries) {
		PoppetShelfSavedData data = new PoppetShelfSavedData(level);

		for(String identifier : nbt.getAllKeys()) {
			ServerLevel world = data.getLevelFromShelfIdentifier(identifier);
			BlockPos pos = data.getBlockPosFromShelfIdentifier(identifier);
			PoppetShelfInventory inventory = new PoppetShelfInventory(world, pos);

			inventory.load((CompoundTag)nbt.get(identifier), registries);
			data.SHELF_STORAGE.put(identifier, inventory);
			data.setupPoppetUUIDs(identifier, inventory);
		}
		Enchanted.LOG.info("Loaded poppet shelves successfully");
		return data;
	}

	@Override
	public CompoundTag save(CompoundTag nbt, Provider registries) {
		for(String identifier : SHELF_STORAGE.keySet()) {
			CompoundTag tag = new CompoundTag();
			PoppetShelfInventory inventory = SHELF_STORAGE.get(identifier);
			inventory.save(tag, registries);
			nbt.put(identifier, tag);
		}
		Enchanted.LOG.info("Saved poppet shelves successfully");
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

	public void setupPoppetUUID(String identifier, ItemStack stack) {
		if(PoppetUtils.isBound(stack)) {
			UUID uuid = stack.get(EDataComponents.ENTITY_REF.get()).uuid();
			PLAYER_POPPETS.putIfAbsent(uuid, new ArrayList<>());
			PLAYER_POPPETS.get(uuid).add(new PoppetEntry(stack, identifier));
		}
	}

	public void removePoppetUUID(String identifier, ItemStack stack) {
		if(PoppetUtils.isBound(stack)) {
			PLAYER_POPPETS.get(stack.get(EDataComponents.ENTITY_REF.get()).uuid()).removeIf(entry -> entry.matches(stack, identifier));
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
		return level.getServer().getLevel(ResourceKey.create(Registries.DIMENSION, ResourceLocation.parse(levelString)));
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
