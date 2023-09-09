/*
 *
 *   Copyright (c) 2023. Favouriteless
 *   Enchanted, a minecraft mod.
 *   GNU GPLv3 License
 *
 *       This file is part of Enchanted.
 *
 *       Enchanted is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU General Public License as published by
 *       the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       Enchanted is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU General Public License for more details.
 *
 *       You should have received a copy of the GNU General Public License
 *       along with Enchanted.  If not, see <https://www.gnu.org/licenses/>.
 *
 *
 */

package com.favouriteless.enchanted.common.blockentities;

import com.favouriteless.enchanted.api.power.IPowerConsumer;
import com.favouriteless.enchanted.api.rites.AbstractRite;
import com.favouriteless.enchanted.common.altar.SimplePowerPosHolder;
import com.favouriteless.enchanted.common.init.registry.EnchantedBlockEntityTypes;
import com.favouriteless.enchanted.common.init.registry.RiteTypes;
import com.favouriteless.enchanted.common.rites.RiteManager;
import com.mojang.math.Vector3f;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

public class ChalkGoldBlockEntity extends BlockEntity implements IPowerConsumer {

    private final SimplePowerPosHolder altarPosHolder;
    private AbstractRite currentRite = null;

    public ChalkGoldBlockEntity(BlockPos pos, BlockState state) {
        super(EnchantedBlockEntityTypes.CHALK_GOLD.get(), pos, state);
        this.altarPosHolder = new SimplePowerPosHolder(pos);
    }

    public void execute(BlockState state, Level _level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if(!level.isClientSide && _level instanceof ServerLevel level) {
            if (currentRite == null) {

                AbstractRite rite = RiteTypes.getRiteAt(level, pos, player.getUUID());

                if (rite != null) {
                    currentRite = rite;
                    RiteManager.addRite(currentRite);
                    currentRite.start();
                } else {
                    level.playSound(null, pos.getX(), pos.getY() + 1, pos.getZ(), SoundEvents.NOTE_BLOCK_SNARE, SoundSource.MASTER, 2f, 1f);
                }
            }
            else {
                if(!currentRite.isStarting()) {
                    currentRite.stopExecuting();
                }
            }
        }
    }

    public static <T extends BlockEntity> void tick(Level level, BlockPos blockPos, BlockState blockState, T t) {
        if(t instanceof ChalkGoldBlockEntity blockEntity) {
            if(level != null && blockEntity.currentRite != null) {
                double dx = blockEntity.worldPosition.getX() + Math.random();
                double dy = blockEntity.worldPosition.getY() + Math.random() * 0.3D;
                double dz = blockEntity.worldPosition.getZ() + Math.random();
                ((ServerLevel) level).sendParticles(new DustParticleOptions(new Vector3f(254 / 255F, 94 / 255F, 94 / 255F), 1.0F), dx, dy, dz, 1, 0.0D, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    public void setRite(AbstractRite rite) {
        currentRite = rite;;
    }

    public void clearRite() {
        currentRite = null;
    }

    public void stopRite() {
        currentRite.stopExecuting();
        clearRite();
    }

    public AbstractRite getRite() {
        return this.currentRite;
    }

    @Override
    public void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.put("altarPos", altarPosHolder.serializeNBT());
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        altarPosHolder.deserializeNBT(nbt.getList("altarPos", 10));
    }

    @Override
    public @NotNull IPowerConsumer.IPowerPosHolder getPosHolder() {
        return altarPosHolder;
    }
}
