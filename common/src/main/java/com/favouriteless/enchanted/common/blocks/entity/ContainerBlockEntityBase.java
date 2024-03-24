package com.favouriteless.enchanted.common.blocks.entity;

import com.favouriteless.enchanted.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public abstract class ContainerBlockEntityBase extends BlockEntity implements Container, Nameable {

    protected NonNullList<ItemStack> inventory;
    private Component name;

    public ContainerBlockEntityBase(BlockEntityType<?> type, BlockPos pos, BlockState state, NonNullList<ItemStack> inventory) {
        super(type, pos, state);
        this.inventory = inventory;
    }

    protected int getBurnTime(ItemStack fuel, @Nullable RecipeType<?> type) {
        return fuel.isEmpty() ? 0 : Services.PLATFORM.getBurnTime(fuel, type);
    }

    protected void updateBlock() {
        if(level != null && !level.isClientSide) {
            BlockState state = level.getBlockState(worldPosition);
            level.sendBlockUpdated(worldPosition, state, state, 2);
        }
    }

    public abstract NonNullList<ItemStack> getDroppableInventory();

    public void setCustomName(Component name) {
        this.name = name;
    }

    @Override
    @NotNull
    public Component getName() {
        return name != null ? name : getDefaultName();
    }

    @Override
    @NotNull
    public Component getDisplayName() {
        return getName();
    }

    @Nullable
    @Override
    public Component getCustomName() {
        return name;
    }

    protected abstract Component getDefaultName();

    @Override
    public void load(@NotNull CompoundTag nbt) {
        super.load(nbt);

        if(nbt.contains("CustomName", 8))
            name = Component.Serializer.fromJson(nbt.getString("CustomName"));
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag nbt) {
        super.saveAdditional(nbt);

        if(name != null)
            nbt.putString("CustomName", Component.Serializer.toJson(name));
    }

    @Override
    public void clearContent() {
        inventory.clear();
    }

    @Override
    public int getContainerSize() {
        return inventory.size();
    }

    @Override
    public boolean isEmpty() {
        for(ItemStack stack : inventory) {
            if(!stack.isEmpty())
                return false;
        }
        return true;
    }

    @Override
    @NotNull
    public ItemStack getItem(int index) {
        return inventory.get(index);
    }

    @Override
    @NotNull
    public ItemStack removeItem(int index, int amount) {
        return ContainerHelper.removeItem(inventory, index, amount);
    }

    @Override
    @NotNull
    public ItemStack removeItemNoUpdate(int index) {
        return ContainerHelper.takeItem(inventory, index);
    }

    @Override
    public void setItem(int index, @NotNull ItemStack stack) {
        inventory.set(index, stack);
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        if (level.getBlockEntity(worldPosition) != this)
            return false;
        return player.distanceToSqr(worldPosition.getX() + 0.5D, worldPosition.getY() + 0.5D, worldPosition.getZ() + 0.5D) <= 64.0D;
    }

}