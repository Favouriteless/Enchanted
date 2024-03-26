package com.favouriteless.enchanted.api;

import com.favouriteless.enchanted.common.init.LootExtensions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * {@link LootExtension} represents an extra loot table for a {@link BlockState} or {@link EntityType} when it is
 * broken/killed.
 * <strong>IMPORTANT:</strong> All {@link LootExtension} must be registered using their respective method in
 * {@link LootExtensions}.
 */
public abstract class LootExtension {

    private final ResourceLocation tableLocation;
    private final List<Supplier<Object>> typeSuppliers = new ArrayList<>();

    public LootExtension(ResourceLocation tableLocation) {
        this.tableLocation = tableLocation;
    }

    /**
     * Test if this {@link LootExtension} should be rolled for a given {@link LootContext}.
     * @return True if table should be rolled, otherwise false.
     */
    public abstract boolean test(LootContext context);

    /**
     * @return True if object is one of this {@link LootExtension}'s types.
     */
    public boolean canApply(Object object) {
        for(Supplier<Object> supplier : typeSuppliers) {
            if(supplier.get() == object)
                return true;
        }
        return false;
    }

    public ResourceLocation getTable() {
        return tableLocation;
    }

    protected void addType(Supplier<Object> type) {
        typeSuppliers.add(type);
    }

    protected void addType(Object type) {
        typeSuppliers.add(() -> type);
    }

}
