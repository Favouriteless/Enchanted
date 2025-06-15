package net.favouriteless.enchanted.common.loot_extensions;

import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.enchanted.api.LootExtension;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.storage.loot.LootContext;

public class ArthanaLootExtension extends LootExtension {

    public ArthanaLootExtension(EntityType<?> type) {
        super(Enchanted.id("extensions/entities/" + BuiltInRegistries.ENTITY_TYPE.getKey(type).getPath()));
        addType(type);
    }

    @Override
    public boolean test(LootContext context) {
        return true; // The arthana conditions are actually just attached to the loot table.
    }

}
