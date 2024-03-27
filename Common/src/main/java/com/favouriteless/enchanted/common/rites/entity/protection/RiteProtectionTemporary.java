package com.favouriteless.enchanted.common.rites.entity.protection;

import com.favouriteless.enchanted.client.particles.types.DoubleParticleType.DoubleParticleData;
import com.favouriteless.enchanted.common.init.registry.EnchantedBlocks;
import com.favouriteless.enchanted.common.init.registry.EnchantedItems;
import com.favouriteless.enchanted.common.init.registry.EnchantedParticles;
import com.favouriteless.enchanted.common.rites.CirclePart;
import com.favouriteless.enchanted.common.rites.RiteType;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Items;

import java.util.UUID;

public class RiteProtectionTemporary extends RiteProtection {

    public static final int DURATION = 60 * 20; // 60 seconds duration

    public RiteProtectionTemporary(RiteType<?> type, ServerLevel level, BlockPos pos, UUID caster) {
        super(type, level, pos, caster, 0, 0, 4, EnchantedBlocks.PROTECTION_BARRIER_TEMPORARY.get()); // Power, power per tick, radius
        CIRCLES_REQUIRED.put(CirclePart.SMALL, EnchantedBlocks.CHALK_WHITE.get());
        ITEMS_REQUIRED.put(Items.OBSIDIAN, 1);
        ITEMS_REQUIRED.put(EnchantedItems.ATTUNED_STONE_CHARGED.get(), 1);
    }

    @Override
    protected void onTick() {
        if(getLevel().isLoaded(targetPos)) {
            if(ticks % 20 == 0) {
                generateSphere(block);
                getLevel().sendParticles(new DoubleParticleData(EnchantedParticles.PROTECTION_SEED.get(), radius), targetPos.getX() + 0.5D, targetPos.getY() + 0.6D, targetPos.getZ() + 0.5D, 1, 0.0D, 0.0D, 0.0D, 0.0D);
            }
        }
        if(ticks > DURATION)
            stopExecuting();
    }

}
