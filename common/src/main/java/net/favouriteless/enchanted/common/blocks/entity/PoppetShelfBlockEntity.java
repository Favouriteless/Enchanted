package net.favouriteless.enchanted.common.blocks.entity;

import net.favouriteless.enchanted.common.enchanted.poppet.shelf.PoppetShelfInventory;
import net.favouriteless.enchanted.common.enchanted.poppet.shelf.PoppetShelfManager;
import net.favouriteless.enchanted.common.menus.PoppetShelfMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class PoppetShelfBlockEntity extends BlockEntity implements MenuProvider {

	public PoppetShelfInventory inventory = null;

	public PoppetShelfBlockEntity(BlockPos pos, BlockState state) {
		super(EBlockEntityTypes.POPPET_SHELF.get(), pos, state);
	}

	@Override
	public Component getDisplayName() {
		return Component.translatable("container.enchanted.poppet_shelf");
	}

	@Override
	public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
		return new PoppetShelfMenu(id, inventory, this);
	}

	public void updateBlock() {
		if(level != null && !level.isClientSide) {
			BlockState state = level.getBlockState(worldPosition);
			level.sendBlockUpdated(worldPosition, state, state, Block.UPDATE_CLIENTS);
		}
	}

	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public void loadAdditional(CompoundTag tag, Provider registries) {
		if(tag.contains("Items"))
			getInventory().load(tag, registries);
	}

	@Override
	public CompoundTag getUpdateTag(Provider registries) {
		return getInventory().save(registries);
	}

	/**
	 * @return The {@link PoppetShelfInventory} instance for this {@link PoppetShelfBlockEntity}.
	 */
	public PoppetShelfInventory getInventory() {
		if(inventory == null) {
            if(level instanceof ServerLevel serverLevel)
                inventory = PoppetShelfManager.get(serverLevel).getOrCreate(serverLevel, worldPosition);
            else
                inventory = PoppetShelfInventory.forClient();
		}
		return inventory;
	}

}
