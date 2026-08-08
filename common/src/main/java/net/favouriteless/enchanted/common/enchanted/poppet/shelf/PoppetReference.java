package net.favouriteless.enchanted.common.enchanted.poppet.shelf;

import net.favouriteless.enchanted.common.blocks.entity.PoppetShelfBlockEntity;
import net.favouriteless.enchanted.common.enchanted.poppet.PoppetHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

/**
 * Wraps a poppet item and the shelf it is in on the server. Use this in place of directly interacting with the poppet
 * item as it will also notify relevant clients about any inventory changes.
 */
public class PoppetReference {

    private final PoppetShelfIdentifier shelf;
    private final int slot;
    private final ItemStack stack;

    public PoppetReference(PoppetShelfIdentifier shelf, int slot, ItemStack stack) {
        this.shelf = shelf;
        this.slot = slot;
        this.stack = stack;
    }

    /**
     * Attempt to consume the referenced poppet and notify clients about the changes.
     *
     * @param player    Player the poppet is activating for.
     * @param predicate Predicate to be run with the referenced poppet. If the item should be consumed/updated, return
     *                  true. The provided itemstack is a copy, treat it like a transaction.
     *
     * @return true if the referenced poppet was acted upon.
     */
    public boolean tryConsume(ServerPlayer player, Predicate<ItemStack> predicate) {
        ItemStack copy = stack.copy();

        if (predicate.test(copy)) {
            copy.setDamageValue(copy.getDamageValue() + 1);
            if (copy.getDamageValue() >= copy.getMaxDamage()) {
                copy.shrink(1);
            }

            ServerLevel level = player.server.getLevel(shelf.dimension());
            PoppetShelfInventory inv = PoppetShelfManager.get(level).get(shelf);

            if (inv != null) {
                inv.setItem(slot, copy);
            }
            if (level.getBlockEntity(shelf.pos()) instanceof PoppetShelfBlockEntity be) {
                be.updateBlock();
            }

            PoppetHelper.doTriggerAnimation(player, stack.getItem());
            return true;
        }

        return false;
    }

    /**
     * @return true if stack is referenced by this PoppetReference.
     */
    public boolean references(ItemStack stack) {
        return this.stack == stack;
    }

}