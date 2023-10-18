package com.favouriteless.enchanted.common.blockentities;

import com.favouriteless.enchanted.common.init.registry.EnchantedBlockEntityTypes;
import com.favouriteless.enchanted.common.menus.PoppetShelfMenu;
import com.favouriteless.enchanted.common.poppet.PoppetShelfInventory;
import com.favouriteless.enchanted.common.poppet.PoppetShelfManager;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class PoppetShelfBlockEntity extends BlockEntity implements MenuProvider {

	public PoppetShelfInventory inventory = null;

	public PoppetShelfBlockEntity(BlockPos pos, BlockState state) {
		super(EnchantedBlockEntityTypes.POPPET_SHELF.get(), pos, state);
	}

	@Override
	public Component getDisplayName() {
		return Component.translatable("container.enchanted.poppet_shelf");
	}

	@Nullable
	@Override
	public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player playerEntity) {
		return new PoppetShelfMenu(id, playerInventory, this);
	}

	public void updateBlock() {
		if(level != null && !level.isClientSide) {
			BlockState state = level.getBlockState(worldPosition);
			level.sendBlockUpdated(worldPosition, state, state, 2);
		}
	}

	@Nullable
	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
		getInventory().load(pkt.getTag());
	}

	@Override
	public CompoundTag getUpdateTag() {
		CompoundTag nbt = super.getUpdateTag();
		getInventory().save(nbt);
		return nbt;
	}

	@Override
	public void handleUpdateTag( CompoundTag tag) {
		super.handleUpdateTag(tag);
		getInventory().load(tag);
	}

	public PoppetShelfInventory getInventory() {
		if(inventory == null) {
			if(level.isClientSide)
				inventory = new PoppetShelfInventory(level, worldPosition);
			else
				inventory = PoppetShelfManager.getInventoryFor(this);
		}
		return inventory;
	}

}
