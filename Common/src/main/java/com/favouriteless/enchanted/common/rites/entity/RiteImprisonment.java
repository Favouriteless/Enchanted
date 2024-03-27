package com.favouriteless.enchanted.common.rites.entity;

import com.favouriteless.enchanted.api.rites.AbstractRite;
import com.favouriteless.enchanted.client.particles.ImprisonmentCageParticle;
import com.favouriteless.enchanted.common.init.EnchantedTags.EntityTypes;
import com.favouriteless.enchanted.common.init.registry.EnchantedBlocks;
import com.favouriteless.enchanted.common.init.registry.EnchantedParticles;
import com.favouriteless.enchanted.common.rites.CirclePart;
import com.favouriteless.enchanted.common.rites.RiteType;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;

import java.util.*;

public class RiteImprisonment extends AbstractRite {

    public static final double ATTRACT_VELOCITY = 0.03D;
    public static final double TETHER_RANGE = 3.0D;
    public static final double TETHER_BREAK_RANGE = 1.0D;
    private final Set<Entity> tetheredMonsters = new HashSet<>();

    public RiteImprisonment(RiteType<?> type, ServerLevel level, BlockPos pos, UUID caster) {
        super(type, level, pos, caster, 500, 3); // Power, power per tick
        CIRCLES_REQUIRED.put(CirclePart.SMALL, EnchantedBlocks.CHALK_WHITE.get());
        ITEMS_REQUIRED.put(Items.SLIME_BALL, 1);
        ITEMS_REQUIRED.put(Items.REDSTONE, 1);
    }

    @Override
    public void execute() {
        getLevel().playSound(null, getPos(), SoundEvents.ZOMBIE_VILLAGER_CURE, SoundSource.MASTER, 0.5F, 1.0F);
    }

    @Override
    public void onTick() {
        BlockPos pos = getPos();
        List<Entity> currentEntities = CirclePart.SMALL.getEntitiesInside(getLevel(), pos, entity -> entity.getType().is(EntityTypes.MONSTERS));
        if(!currentEntities.isEmpty()) {
            tetheredMonsters.addAll(currentEntities);
        }
        List<Entity> removeList = new ArrayList<>();
        for(Entity monster : tetheredMonsters) {
            Vec3 relativePos = monster.position().subtract(pos.getX()+0.5D, monster.position().y(), pos.getZ()+0.5D);
            double distance = relativePos.length();
            if(distance > TETHER_RANGE + TETHER_BREAK_RANGE) {
                removeList.add(monster);
            }
            else if(distance > TETHER_RANGE) {
                monster.setDeltaMovement(relativePos.normalize().scale(-1 * ATTRACT_VELOCITY));
            }
        }
        removeList.forEach(tetheredMonsters::remove);

        if(this.ticks % (ImprisonmentCageParticle.LIFETIME+15) == 0) { // 15 ticks for the fade time
            getLevel().sendParticles(EnchantedParticles.IMPRISONMENT_CAGE_SEED.get(), pos.getX()+0.5D, pos.getY()+0.2D, pos.getZ()+0.5D, 1, 0.0D, 0.0D, 0.0D, 0.0D);
        }
    }

}
