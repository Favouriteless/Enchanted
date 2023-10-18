package com.favouriteless.enchanted.common.rites.entity;

import com.favouriteless.enchanted.api.rites.AbstractRite;
import com.favouriteless.enchanted.common.init.registry.EnchantedBlocks;
import com.favouriteless.enchanted.common.init.registry.EnchantedItems;
import com.favouriteless.enchanted.common.rites.CirclePart;
import com.favouriteless.enchanted.common.rites.RiteType;
import com.favouriteless.enchanted.common.util.WaystoneHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public class RiteTranspositionPlayerBlooded extends AbstractRite {

    public RiteTranspositionPlayerBlooded(RiteType<?> type, ServerLevel level, BlockPos pos, UUID caster) {
        super(type, level, pos, caster, 0, 0); // Power, power per tick
        CIRCLES_REQUIRED.put(CirclePart.SMALL, EnchantedBlocks.CHALK_PURPLE.get());
        ITEMS_REQUIRED.put(EnchantedItems.BLOODED_WAYSTONE.get(), 1);
    }

    @Override
    public void execute() {
        ItemStack stack = itemsConsumed.get(0);
        ServerLevel level = getLevel();
        Player caster = level.getServer().getPlayerList().getPlayer(getCasterUUID());

        if(caster != null) {
            setTargetEntity(WaystoneHelper.getEntity(level, stack));
            Entity targetEntity = getTargetEntity();

            if(targetEntity != null) {
                spawnParticles((ServerLevel)caster.level, caster.getX(), caster.getY(), caster.getZ());

                level.playSound(null, caster.position().x, caster.position().y, caster.position().z, SoundEvents.ENDERMAN_TELEPORT, SoundSource.MASTER, 1.0F, 1.0F);
                if(caster.level != targetEntity.level) {
                    caster.changeDimension((ServerLevel)targetEntity.level);
                }
                caster.teleportTo(targetEntity.getX(), targetEntity.getY(), targetEntity.getZ());
                level.playSound(null, caster.position().x, caster.position().y, caster.position().z, SoundEvents.ENDERMAN_TELEPORT, SoundSource.MASTER, 1.0F, 1.0F);
                spawnParticles((ServerLevel)targetEntity.level, targetEntity.getX(), targetEntity.getX(), targetEntity.getZ());
            }
            else {
                cancel();
            }
        }
        else {
            cancel();
        }

        stopExecuting();
    }

    protected void spawnParticles(ServerLevel world, double x, double y, double z) {
        for(int i = 0; i < 25; i++) {
            double dx = x - 0.5D + (Math.random() * 1.5D);
            double dy = y + (Math.random() * 2.0D);
            double dz = z - 0.5D + (Math.random() * 1.5D);
            world.sendParticles(ParticleTypes.PORTAL, dx, dy, dz, 1, 0.0D, 0.0D, 0.0D, 0.0D);
        }
    }

}
