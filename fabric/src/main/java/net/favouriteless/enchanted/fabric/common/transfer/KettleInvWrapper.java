package net.favouriteless.enchanted.fabric.common.transfer;

import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.StoragePreconditions;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant;
import net.favouriteless.enchanted.common.blocks.entity.KettleBlockEntity;
import net.favouriteless.enchanted.fabric.common.transfer.KettleInvWrapper.Snapshot;
import net.minecraft.world.item.ItemStack;

public class KettleInvWrapper extends SnapshotParticipant<Snapshot> implements SingleSlotStorage<ItemVariant> {

    private final KettleBlockEntity kettle;

    public KettleInvWrapper(KettleBlockEntity kettle) {
        this.kettle = kettle;
    }

    @Override
    public long insert(ItemVariant variant, long maxAmount, TransactionContext transaction) {
        return 0;
    }

    @Override
    public long extract(ItemVariant variant, long maxAmount, TransactionContext transaction) {
        StoragePreconditions.notBlank(variant);

        if(!variant.matches(kettle.getResult()))
            return 0;

        updateSnapshots(transaction);
        return kettle.takeItemNoUpdate(false).getCount();
    }

    @Override
    protected void onFinalCommit() {
        kettle.checkResultEmpty();
    }

    @Override
    public boolean isResourceBlank() {
        return kettle.getResult().isEmpty();
    }

    @Override
    public ItemVariant getResource() {
        return ItemVariant.of(kettle.getResult());
    }

    @Override
    public long getAmount() {
        return kettle.getResult().getCount();
    }

    @Override
    public long getCapacity() {
        return kettle.getResult().getMaxStackSize();
    }

    @Override
    protected Snapshot createSnapshot() {
        return new Snapshot(kettle.getResult().copy(), kettle.getFluidAmount());
    }

    @Override
    protected void readSnapshot(Snapshot snapshot) {
        kettle.setResult(snapshot.result());
        kettle.setFluidAmount(snapshot.water());
    }

    public record Snapshot(ItemStack result, int water) {}

}
