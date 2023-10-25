package com.favouriteless.enchanted.common.rites.entity;

import com.favouriteless.enchanted.api.familiars.FamiliarSavedData;
import com.favouriteless.enchanted.api.familiars.IFamiliarCapability.IFamiliarEntry;
import com.favouriteless.enchanted.api.rites.AbstractRite;
import com.favouriteless.enchanted.common.init.registry.EnchantedBlocks;
import com.favouriteless.enchanted.common.init.registry.EnchantedItems;
import com.favouriteless.enchanted.common.rites.CirclePart;
import com.favouriteless.enchanted.common.rites.RiteType;
import com.favouriteless.enchanted.common.rites.SummonForcer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;

public class RiteSummonFamiliar extends AbstractRite {

    public RiteSummonFamiliar(RiteType<?> type, ServerLevel level, BlockPos pos, UUID caster) {
        super(type, level, pos, caster, 1000, 0); // Power, power per tick
        CIRCLES_REQUIRED.put(CirclePart.MEDIUM, EnchantedBlocks.CHALK_WHITE.get());
        ITEMS_REQUIRED.put(EnchantedItems.BREATH_OF_THE_GODDESS.get(), 1);
        ITEMS_REQUIRED.put(EnchantedItems.HINT_OF_REBIRTH.get(), 1);
        ITEMS_REQUIRED.put(EnchantedItems.WHIFF_OF_MAGIC.get(), 1);
    }

    @Override
    public void execute() {
        IFamiliarEntry entry = FamiliarSavedData.get(getLevel()).getEntry(getCasterUUID());

        if(entry != null) {
            ServerLevel level = getLevel();
            BlockPos pos = getPos();
            Vec3 vec3 = Vec3.atCenterOf(pos);

            setTargetUUID(entry.getUUID());

            Entity targetEntity = tryFindTargetEntity();

            if(targetEntity == null) {
                targetEntity = entry.getType().getTypeOut().create(level);
                targetEntity.load(entry.getNbt());
                targetEntity.setPos(vec3);
                level.addFreshEntity(targetEntity);
                ((TamableAnimal)targetEntity).setPersistenceRequired();

                entry.setUUID(targetEntity.getUUID()); // Update the UUID entry
            }

            if(targetEntity.getLevel() != level)
                targetEntity.changeDimension(level, new SummonForcer(vec3));
            else
                targetEntity.teleportTo(vec3.x, vec3.y, vec3.z);

            entry.setDismissed(false);
            level.playSound(null, vec3.x, vec3.y, vec3.z, SoundEvents.ENDERMAN_TELEPORT, SoundSource.NEUTRAL, 1.0F, 1.0F);
            teleportParticles(level, vec3.x, vec3.y, vec3.z);
        }
        else {
            cancel();
        }
        stopExecuting();
    }

    @Override
    protected boolean checkAdditional() {
        return true;
    }

    protected void teleportParticles(ServerLevel world, double x, double y, double z) {
        for(int i = 0; i < 25; i++) {
            double dx = x - 0.5D + (Math.random() * 1.5D);
            double dy = y + (Math.random() * 2.0D);
            double dz = z - 0.5D + (Math.random() * 1.5D);
            world.sendParticles(ParticleTypes.PORTAL, dx, dy, dz, 1, 0.0D, 0.0D, 0.0D, 0.0D);
        }
    }

}
