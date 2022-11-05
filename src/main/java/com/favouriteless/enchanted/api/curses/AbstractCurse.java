/*
 *
 *   Copyright (c) 2022. Favouriteless
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

package com.favouriteless.enchanted.api.curses;

import com.favouriteless.enchanted.common.init.EnchantedSoundEvents;
import com.favouriteless.enchanted.common.util.curse.CurseType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundSoundEntityPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;

import java.util.UUID;

/**
 * Curses base class
 */
public abstract class AbstractCurse {

    public static final int MIN_WHISPER_TIME = 120;
    public static final int MAX_WHISPER_TIME = 240;
    public static final double WHISPER_CHANCE = 1.0D / ((MAX_WHISPER_TIME - MIN_WHISPER_TIME)*20);

    public final CurseType<?> type;
    protected UUID targetUUID;
    protected UUID casterUUID;
    protected int level;

    protected ServerPlayer targetPlayer;

    protected long ticks = 0;
    private long lastWhisper = 0;

    public AbstractCurse(CurseType<?> type) {
        this.type = type;
    }

    public void tick(ServerLevel level) { // Ticks with level purely for level access, do not save this anywhere.
        if(targetPlayer == null || targetPlayer.isRemoved()) {
            targetPlayer = level.getServer().getPlayerList().getPlayer(targetUUID);
        }
        if(targetPlayer != null) {
            onTick();
        }

        long timeSinceWhisper = ticks - lastWhisper;
        if(timeSinceWhisper > MAX_WHISPER_TIME*20L)
            whisper();
        else if(timeSinceWhisper > MIN_WHISPER_TIME*20L)
            if(Math.random() < WHISPER_CHANCE)
                whisper();

        ticks++;
    }

    protected abstract void onTick();

    private void whisper() {
        if(targetPlayer != null) {
            lastWhisper = ticks;
            targetPlayer.connection.send(new ClientboundSoundEntityPacket(EnchantedSoundEvents.CURSE_WHISPER.get(), SoundSource.AMBIENT, targetPlayer, 0.2F, (float) Math.random() * 0.15F + 0.85F));
        }
    }

    public CurseType<?> getType() {
        return type;
    }

    public UUID getTargetUUID() {
        return targetUUID;
    }

    public UUID getCasterUUID() {
        return casterUUID;
    }

    public void setTarget(UUID targetUUID) {
        this.targetUUID = targetUUID;
    }

    public void setTarget(ServerPlayer player) {
        this.targetUUID = player.getUUID();
        this.targetPlayer = player;
    }

    public void setCaster(UUID casterUUID) {
        this.casterUUID = casterUUID;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public void save(CompoundTag nbt) {
        nbt.putString("type", type.getRegistryName().toString());
        nbt.putUUID("target", targetUUID);
        nbt.putUUID("caster", casterUUID);
        nbt.putLong("ticks", ticks);
        nbt.putInt("level", level);
        saveAdditional(nbt);
    }

    public void load(CompoundTag nbt) {
        targetUUID = nbt.getUUID("target");
        casterUUID = nbt.getUUID("caster");
        ticks = nbt.getLong("ticks");
        level = nbt.getInt("level");
        loadAdditional(nbt);
    }

    protected void saveAdditional(CompoundTag nbt) {}

    protected void loadAdditional(CompoundTag nbt) {}

}
