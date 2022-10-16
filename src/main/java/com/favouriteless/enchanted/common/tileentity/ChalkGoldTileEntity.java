/*
 * Copyright (c) 2022. Favouriteless
 * Enchanted, a minecraft mod.
 * GNU GPLv3 License
 *
 *     This file is part of Enchanted.
 *
 *     Enchanted is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Enchanted is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Enchanted.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.favouriteless.enchanted.common.tileentity;

import com.favouriteless.enchanted.api.altar.AltarPowerHelper;
import com.favouriteless.enchanted.api.altar.IAltarPowerConsumer;
import com.favouriteless.enchanted.common.init.EnchantedRiteTypes;
import com.favouriteless.enchanted.common.init.EnchantedTileEntityTypes;
import com.favouriteless.enchanted.api.rites.AbstractRite;
import com.favouriteless.enchanted.common.util.rite.RiteManager;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.InteractionHand;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;

import java.util.ArrayList;
import java.util.List;

public class ChalkGoldTileEntity extends BlockEntity implements TickableBlockEntity, IAltarPowerConsumer {

    private final List<BlockPos> potentialAltars = new ArrayList<>();
    private AbstractRite currentRite = null;

    public ChalkGoldTileEntity(final BlockEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    public ChalkGoldTileEntity() {
        this(EnchantedTileEntityTypes.CHALK_GOLD.get());
    }

    public void execute(BlockState state, Level world, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if(!world.isClientSide) {
            if (currentRite == null) {

                AbstractRite rite = EnchantedRiteTypes.riteAvailableAt(world, pos);

                if (rite != null) {
                    currentRite = rite;
                    currentRite.setWorld((ServerLevel)world);
                    currentRite.setPos(pos);
                    currentRite.setCaster(player);
                    currentRite.setChalk(this);
                    RiteManager.addRite(currentRite);
                    currentRite.start();
                } else {
                    world.playSound(null, pos.getX(), pos.getY() + 1, pos.getZ(), SoundEvents.NOTE_BLOCK_SNARE, SoundSource.MASTER, 2f, 1f);
                }
            }
            else {
                if(!currentRite.getStarting()) {
                    currentRite.stopExecuting();
                }
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
    public void tick() {
        if(level != null && currentRite != null) {
            double dx = worldPosition.getX() + Math.random();
            double dy = worldPosition.getY() + Math.random() * 0.3D;
            double dz = worldPosition.getZ() + Math.random();
            ((ServerLevel)level).sendParticles(new DustParticleOptions(254/255F,94/255F,94/255F, 1.0F), dx, dy, dz, 1, 0.0D, 0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    public CompoundTag save(CompoundTag nbt) {
        AltarPowerHelper.savePosTag(potentialAltars, nbt);
        return super.save(nbt);
    }

    @Override
    public void load(BlockState state, CompoundTag nbt) {
        super.load(state, nbt);
        AltarPowerHelper.loadPosTag(potentialAltars, nbt);
    }

    @Override
    public List<BlockPos> getAltarPositions() {
        return potentialAltars;
    }

    @Override
    public void removeAltar(BlockPos altarPos) {
        potentialAltars.remove(altarPos);
        this.setChanged();
    }

    @Override
    public void addAltar(BlockPos altarPos) {
        AltarPowerHelper.addAltarByClosest(potentialAltars, level, worldPosition, altarPos);
        this.setChanged();
    }
}
