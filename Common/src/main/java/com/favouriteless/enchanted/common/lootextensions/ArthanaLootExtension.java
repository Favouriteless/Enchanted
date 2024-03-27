package com.favouriteless.enchanted.common.lootextensions;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.api.LootExtension;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.storage.loot.LootContext;

public class ArthanaLootExtension extends LootExtension {

    public ArthanaLootExtension(EntityType<?> type) {
        super(Enchanted.location("extensions/entities/" + Registry.ENTITY_TYPE.getKey(type).getPath()));
        addType(type);
    }

    @Override
    public boolean test(LootContext context) {
        return true; // The arthana conditions are actually just attached to the loot table.
    }

}
