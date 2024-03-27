package com.favouriteless.enchanted.common.init;

import com.favouriteless.enchanted.api.LootExtension;
import com.favouriteless.enchanted.common.lootextensions.ArthanaLootExtension;
import com.favouriteless.enchanted.common.lootextensions.GrassLootExtension;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import java.util.ArrayList;
import java.util.List;

public class LootExtensions {

    private static final List<LootExtension> entity = new ArrayList<>();
    private static final List<LootExtension> entityLootExtensions = new ArrayList<>();

    static {
        registerBlock(new GrassLootExtension());
        registerEntity(new ArthanaLootExtension(EntityType.BAT));
        registerEntity(new ArthanaLootExtension(EntityType.CREEPER));
        registerEntity(new ArthanaLootExtension(EntityType.WOLF));
    }

    public static void registerBlock(LootExtension extension) {
        if(entity.contains(extension))
            throw new IllegalArgumentException("Tried to register a duplicate Block loot extension.");
        entity.add(extension);
    }

    public static void registerEntity(LootExtension extension) {
        if(entityLootExtensions.contains(extension))
            throw new IllegalArgumentException("Tried to register a duplicate Entity loot extension.");
        entityLootExtensions.add(extension);
    }

    // ----------------------------------------- IMPLEMENTATION DETAILS BELOW -----------------------------------------

    public static ObjectArrayList<ItemStack> tryRollBlock(BlockState state, LootContext.Builder builder) {
        ObjectArrayList<ItemStack> out = new ObjectArrayList<>();
        for(LootExtension extension : entity) {
            if(extension.canApply(state.getBlock())) {
                LootContext context = builder.withParameter(LootContextParams.BLOCK_STATE, state).create(LootContextParamSets.BLOCK);
                ServerLevel level = context.getLevel();
                out.addAll(level.getServer().getLootTables().get(extension.getTable()).getRandomItems(context));
            }
        }
        return out;
    }

    public static void tryRollEntity(LivingEntity entity, LootContext.Builder builder) {
        for(LootExtension extension : LootExtensions.entity) {
            if(extension.canApply(entity.getType())) {
                LootContext context = builder.create(LootContextParamSets.ENTITY);
                context.getLevel().getServer().getLootTables().get(extension.getTable()).getRandomItems(context, entity::spawnAtLocation);
            }
        }
    }

}
