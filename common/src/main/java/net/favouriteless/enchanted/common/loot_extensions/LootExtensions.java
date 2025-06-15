package net.favouriteless.enchanted.common.loot_extensions;

import net.favouriteless.enchanted.api.LootExtension;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import java.util.ArrayList;
import java.util.List;

public class LootExtensions {

    private static final List<LootExtension> blockLootExtensions = new ArrayList<>();
    private static final List<LootExtension> entityLootExtensions = new ArrayList<>();

    static {
        registerBlock(new GrassLootExtension());
        registerEntity(new ArthanaLootExtension(EntityType.BAT));
        registerEntity(new ArthanaLootExtension(EntityType.CREEPER));
        registerEntity(new ArthanaLootExtension(EntityType.WOLF));
    }

    public static void registerBlock(LootExtension extension) {
        if(blockLootExtensions.contains(extension))
            throw new IllegalArgumentException("Tried to register a duplicate Block loot extension.");
        blockLootExtensions.add(extension);
    }

    public static void registerEntity(LootExtension extension) {
        if(entityLootExtensions.contains(extension))
            throw new IllegalArgumentException("Tried to register a duplicate Entity loot extension.");
        entityLootExtensions.add(extension);
    }

    // ----------------------------------------- IMPLEMENTATION DETAILS BELOW -----------------------------------------

    public static ObjectArrayList<ItemStack> tryRollBlock(BlockState state, LootParams.Builder builder) {
        ObjectArrayList<ItemStack> out = new ObjectArrayList<>();
        for(LootExtension extension : blockLootExtensions) {
            if(extension.canApply(state.getBlock())) {
                LootParams params = builder.withParameter(LootContextParams.BLOCK_STATE, state).create(LootContextParamSets.BLOCK);
                ServerLevel level = params.getLevel();
                out.addAll(level.getServer().reloadableRegistries().getLootTable(ResourceKey.create(Registries.LOOT_TABLE, extension.getTable())).getRandomItems(params));
            }
        }
        return out;
    }

    public static void tryRollEntity(LivingEntity entity, LootParams.Builder builder) {
        for(LootExtension extension : LootExtensions.entityLootExtensions) {
            if(extension.canApply(entity.getType())) {
                LootParams params = builder.create(LootContextParamSets.ENTITY);
                params.getLevel().getServer().reloadableRegistries().getLootTable(ResourceKey.create(Registries.LOOT_TABLE, extension.getTable())).getRandomItems(params, entity::spawnAtLocation);
            }
        }
    }

}
