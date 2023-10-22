package com.favouriteless.enchanted.api.curses;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.common.curses.CurseMisfortune;
import com.favouriteless.enchanted.common.curses.CurseType;
import com.favouriteless.enchanted.common.init.registry.CurseTypes;
import com.favouriteless.enchanted.common.init.registry.EnchantedSoundEvents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundSoundEntityPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;
import java.util.function.Supplier;

/**
 * {@link Curse} is the base class used for all curses in Enchanted. A curse is an effect which attaches itself to a
 * {@link Player} and acts upon that player while being ticked. Curses must then be registered using
 * {@link CurseTypes#register(ResourceLocation, Supplier)} so the mod is able to know it exists.
 *
 * <p>See {@link RandomCurse} and {@link CurseMisfortune} for examples of how a curse can be implemented.</p>
 */
public abstract class Curse {

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

    public Curse(CurseType<?> type) {
        this.type = type;
    }

    /**
     * Do not override tick in implementations of {@link Curse}. This is used to handle logic for the whispering sounds
     * and targeting. Override {@link Curse#onTick()} instead to add custom tick effects.
     */
    public void tick(ServerLevel level) { // Ticks with level purely for level access, do not save this anywhere.
        if(targetPlayer == null || targetPlayer.isRemoved())
            targetPlayer = level.getServer().getPlayerList().getPlayer(targetUUID);
        if(targetPlayer != null)
            onTick();

        long timeSinceWhisper = ticks - lastWhisper;
        if(timeSinceWhisper > MAX_WHISPER_TIME*20L)
            whisper();
        else if(timeSinceWhisper > MIN_WHISPER_TIME*20L)
            if(Math.random() < WHISPER_CHANCE)
                whisper();
        ticks++;
    }

    /**
     * Override onTick to add custom effects to ticks for a {@link Curse}.
     */
    protected abstract void onTick();

    private void whisper() {
        if(targetPlayer != null) {
            lastWhisper = ticks;
            targetPlayer.connection.send(new ClientboundSoundEntityPacket(EnchantedSoundEvents.CURSE_WHISPER.get(), SoundSource.AMBIENT, targetPlayer, 0.3F, (float) Math.random() * 0.15F + 0.85F, Enchanted.RANDOM.nextLong()));
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
        nbt.putString("type", type.getId().toString());
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

    public void onRemove(ServerLevel level) {}

}
