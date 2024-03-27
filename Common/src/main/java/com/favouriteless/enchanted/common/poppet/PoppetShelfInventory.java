package com.favouriteless.enchanted.common.poppet;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.core.NonNullList;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.AbstractList;

public class PoppetShelfInventory extends AbstractList<ItemStack> implements Container {

	private final Level level;
	private final BlockPos pos;
	private final String identifier;
	public NonNullList<ItemStack> inventoryContents = NonNullList.withSize(4, ItemStack.EMPTY);

	public PoppetShelfInventory(Level level, BlockPos pos) {
		this.level = level;
		this.pos = pos;
		identifier = PoppetShelfSavedData.getShelfIdentifier(level, pos);
	}

	@Override
	public int getContainerSize() {
		return size();
	}

	@Override
	public boolean isEmpty() {
		return isEmpty();
	}

	@Override
	public ItemStack getItem(int index) {
		return get(index);
	}

	@Override
	public ItemStack removeItem(int index, int count) {
		return ContainerHelper.removeItem(this, index, count);
	}

	@Override
	public ItemStack removeItemNoUpdate(int index) {;
		return ContainerHelper.takeItem(this, index);
	}

	@Override
	public void setItem(int index, ItemStack itemStack) {
		set(index, itemStack);
	}

	@Override
	public void setChanged() {
		if(!level.isClientSide)
			PoppetShelfSavedData.get(level).setDirty();
	}

	@Override
	public boolean stillValid(Player player) {
		return false;
	}

	@Override
	public void clearContent() {
		inventoryContents.clear();
	}

	public void save(CompoundTag nbt) {
		ListTag list = new ListTag();
		for(ItemStack itemStack : inventoryContents) {
			CompoundTag itemTag = new CompoundTag();
			itemStack.save(itemTag);
			list.add(itemTag);
		}
		nbt.put("items", list);
	}

	public void load(CompoundTag nbt) {
		ListTag list = nbt.getList("items", 10);
		for(int i = 0; i < list.size(); i++) {
			CompoundTag tag = (CompoundTag)list.get(i);
			inventoryContents.set(i, ItemStack.of(tag));
		}
	}

	public Level getLevel() {
		return level;
	}

	public BlockPos getPos() {
		return pos;
	}

	@Override
	public ItemStack get(int index) {
		return inventoryContents.get(index);
	}

	@Override
	public ItemStack set(int index, ItemStack value) {
		if(!level.isClientSide) {
			if(get(index).isEmpty()) {
				get(index).setCount(1);
			}
			PoppetShelfSavedData data = PoppetShelfSavedData.get(level);
			data.removePoppetUUID(identifier, get(index));
			data.setupPoppetUUID(identifier, value);
		}
		setChanged();
		return inventoryContents.set(index, value);
	}

	@Override
	public int size() {
		return inventoryContents.size();
	}
}
