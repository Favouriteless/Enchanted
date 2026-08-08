package net.favouriteless.enchanted.common.enchanted.poppet.shelf;

import net.favouriteless.enchanted.common.blocks.entity.PoppetShelfBlockEntity;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * Inventory containing the items in a {@link PoppetShelfBlockEntity}. This is separated from the BE so it can be loaded
 * independently of the chunk the BE is in.
 */
public class PoppetShelfInventory implements Container {

    private final NonNullList<ItemStack> items = NonNullList.withSize(4, ItemStack.EMPTY);

    private PoppetShelfInventory() {
    }

    public static PoppetShelfInventory forServer(PoppetShelfIdentifier id, PoppetShelfManager data) {
        return new ServerPoppetShelfInventory(id, data);
    }

    public static PoppetShelfInventory forClient() {
        return new PoppetShelfInventory();
    }

    public CompoundTag save(Provider registries) {
        CompoundTag out = new CompoundTag();
        ContainerHelper.saveAllItems(out, items, registries);
        return out;
    }

    public void load(CompoundTag tag, Provider registries) {
        ContainerHelper.loadAllItems(tag, items, registries);
    }

    @Override
    public int getContainerSize() {
        return items.size();
    }

    @Override
    public boolean isEmpty() {
        return items.isEmpty();
    }

    @Override
    public ItemStack getItem(int slot) {
        return items.get(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        ItemStack out = ContainerHelper.removeItem(items, slot, amount);
        if (!out.isEmpty()) {
            setChanged();
        }
        return out;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return ContainerHelper.takeItem(items, slot);
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        if (!getItem(slot).equals(stack)) {
            setChanged();
        }
        items.set(slot, stack);
    }

    @Override
    public void setChanged() {
    }

    @Override
    public boolean stillValid(Player player) {
        return false;
    }

    @Override
    public void clearContent() {
        if (!items.isEmpty()) {
            items.clear();
            setChanged();
        }
    }


    /**
     * Inventories on the server need to notify the SavedData of reference changes and if it needs to be marked as dirty.
     */
    private static class ServerPoppetShelfInventory extends PoppetShelfInventory {

        private final PoppetShelfIdentifier id;
        private final PoppetShelfManager data;

        private ServerPoppetShelfInventory(PoppetShelfIdentifier id, PoppetShelfManager data) {
            super();
            this.id = id;
            this.data = data;
        }

        @Override
        public void setChanged() {
            data.setDirty();
        }

        @Override
        public void setItem(int slot, ItemStack stack) {
            ItemStack old = getItem(slot);
            if (old != stack) { // If item changes we want to make sure the UUID cache is updated.
                data.dereference(old);
                data.reference(id, slot, stack);
            }
            super.setItem(slot, stack);
        }

        @Override
        public ItemStack removeItem(int slot, int amount) {
            ItemStack old = getItem(slot);
            if (old.getCount() - amount <= 0) {
                data.dereference(old); // Dereference before because DataComponents get nulled after
            }
            return super.removeItem(slot, amount);
        }

        @Override
        public ItemStack removeItemNoUpdate(int slot) {
            ItemStack old = getItem(slot);
            if (!old.isEmpty()) {
                data.dereference(old);
            }
            return super.removeItemNoUpdate(slot);
        }

    }

}
