package net.favouriteless.enchanted.common.enchanted.poppet.shelf;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.enchanted.common.blocks.entity.PoppetShelfBlockEntity;
import net.favouriteless.enchanted.common.items.component.EDataComponents;
import net.favouriteless.enchanted.common.items.component.EntityRefData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * {@link PoppetShelfBlockEntity PoppetShelfBlockEntities} store their inventories detached using SavedData to allow
 * loading their inventory without loading the chunk they are in.
 *
 * @see PoppetShelfInventory
 */
public class PoppetShelfManager extends SavedData {

    private static final String NAME = Enchanted.savedDataName("poppet_shelves");

    private final Map<PoppetShelfIdentifier, PoppetShelfInventory> shelves = new HashMap<>();
    private final Multimap<UUID, PoppetReference> poppets = HashMultimap.create(); // Optimisation for searching for poppets belonging to a player.

    public static PoppetShelfManager get(ServerLevel level) {
        return level.getServer().overworld().getDataStorage().computeIfAbsent(new Factory<>(PoppetShelfManager::new, PoppetShelfManager::load, null), NAME);
    }

    public PoppetShelfInventory getOrCreate(ServerLevel level, BlockPos pos) {
        PoppetShelfIdentifier id = new PoppetShelfIdentifier(level.dimension(), pos);
        return shelves.computeIfAbsent(id, k -> PoppetShelfInventory.forServer(id, this));
    }

    @Nullable
    public PoppetShelfInventory get(PoppetShelfIdentifier id) {
        return shelves.get(id);
    }

    public void remove(ServerLevel level, BlockPos pos) {
        shelves.remove(new PoppetShelfIdentifier(level.dimension(), pos));
    }

    /**
     * Get a reference to all poppets belonging to a player.
     *
     * @param player {@link Player} the poppets should be bound to.
     *
     * @return References to all poppets belonging to player.
     */
    public Collection<PoppetReference> getPoppets(Player player) {
        return getPoppets(player.getUUID());
    }

    /**
     * Get a reference to all poppets belonging to a UUID.
     *
     * @param uuid {@link UUID} the poppets should be bound to.
     *
     * @return References to all poppets bound to uuid.
     */
    public Collection<PoppetReference> getPoppets(UUID uuid) {
        return poppets.get(uuid);
    }

    public void reference(PoppetShelfIdentifier id, int slot, ItemStack stack) {
        EntityRefData ref = stack.get(EDataComponents.ENTITY_REF.get());
        if(ref == null) return;
        poppets.put(ref.uuid(), new PoppetReference(id, slot, stack));
    }

    public void dereference(ItemStack stack) {
        EntityRefData ref = stack.get(EDataComponents.ENTITY_REF.get());
        if(ref == null) return;
        poppets.get(ref.uuid()).removeIf(r -> r.references(stack));
    }

    public void init() {
        shelves.forEach((id, inventory) -> {
            for(int i = 0; i < inventory.getContainerSize(); i++) {
                reference(id, i, inventory.getItem(i));
            }
        });
    }

    @Override
    public CompoundTag save(CompoundTag tag, Provider registries) {
        ListTag list = new ListTag();
        shelves.forEach((id, inventory) -> {
            CompoundTag shelfTag = new CompoundTag();
            shelfTag.put("id", PoppetShelfIdentifier.CODEC.encodeStart(NbtOps.INSTANCE, id).getOrThrow());
            shelfTag.put("inventory", inventory.save(registries));
            list.add(shelfTag);
        });

        tag.put("shelves", list);
        return tag;
    }

    public static PoppetShelfManager load(CompoundTag tag, Provider registries) {
        PoppetShelfManager data = new PoppetShelfManager();

        ListTag list = tag.getList("shelves", Tag.TAG_COMPOUND);
        list.forEach(t -> {
            CompoundTag shelfTag = (CompoundTag)t;

            PoppetShelfIdentifier id = PoppetShelfIdentifier.CODEC.parse(NbtOps.INSTANCE, shelfTag.get("id")).getOrThrow();
            PoppetShelfInventory inventory = PoppetShelfInventory.forServer(id, data);
            inventory.load(shelfTag.getCompound("inventory"), registries);

            data.shelves.put(id, inventory);
        });

        data.init();
        return data;
    }

}
