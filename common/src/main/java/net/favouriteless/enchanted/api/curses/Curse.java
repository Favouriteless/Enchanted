package net.favouriteless.enchanted.api.curses;

import net.favouriteless.enchanted.common.CommonConfig;
import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.enchanted.common.circle_magic.rites.Rite;
import net.favouriteless.enchanted.common.curses.CurseType;
import net.favouriteless.enchanted.common.curses.CurseTypes;
import net.favouriteless.enchanted.common.init.ESoundEvents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundSoundEntityPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;

import java.lang.ref.WeakReference;
import java.util.UUID;
import java.util.function.Supplier;

/**
 * <p>
 *     A {@link Curse} is a server-side object which attaches itself to a target player and ticks. Not to be confused
 *     with {@link Rite}.
 * </p>
 * <p>
 *     <b>IMPORTANT:</b> Curses must be registered via {@link CurseTypes#register(ResourceLocation, Supplier)} to work.
 * </p>
 */
public abstract class Curse {

    public final CurseType<?> type;
    public int strength;
    private UUID targetUUID;

    private WeakReference<ServerPlayer> target;
    private long ticks = 0;
    private long lastWhisper = 0;

    public Curse(CurseType<?> type) {
        this.type = type;
    }

    protected void onTick(final ServerPlayer target, long ticks) {};

    protected void saveAdditional(CompoundTag nbt) {}

    protected void loadAdditional(CompoundTag nbt) {}

    public void onRemove(ServerPlayer target) {}

    public ServerPlayer getTarget() {
        return target != null ? target.get() : null;
    }

    public UUID getTargetUUID() {
        return targetUUID;
    }

    public void setTargetUUID(UUID targetUUID) {
        this.targetUUID = targetUUID;
        this.target = null;
    }

    // ----------------------------------- NON-API IMPLEMENTATIONS BELOW THIS POINT -----------------------------------


    public final void tick(ServerLevel level) { // Ticks with level purely for level access, do not save this anywhere.
        if(getTarget() == null)
            target = new WeakReference<>(level.getServer().getPlayerList().getPlayer(targetUUID));

        ServerPlayer target = getTarget();
        if(target != null)
            onTick(target, ticks);


        long since = ticks - lastWhisper;
        int min = CommonConfig.INSTANCE.curseWhisperMin.get();
        int max = CommonConfig.INSTANCE.curseWhisperMax.get();

        if(since > max * 20L)
            whisper();
        else if(since > min * 20L) {
            if(Math.random() < 1.0D / ((max - min) * 20))
                whisper();
        }
        ticks++;
    }

    public final void remove(ServerLevel level) {
        if(getTarget() == null)
            target = new WeakReference<>(level.getServer().getPlayerList().getPlayer(targetUUID));

        ServerPlayer target = getTarget();
        if(target != null)
            onRemove(target);
    }

    private void whisper() {
        ServerPlayer target = getTarget();
        if(target == null)
            return;

        lastWhisper = ticks;
        target.connection.send(new ClientboundSoundEntityPacket(ESoundEvents.CURSE_WHISPER, SoundSource.AMBIENT, target,
                0.1F, (float)Math.random() * 0.15F + 0.85F, Enchanted.RANDOM.nextLong()));
    }

    public final void save(CompoundTag nbt) {
        nbt.putString("type", type.getId().toString());
        nbt.putUUID("target", targetUUID);
        nbt.putLong("ticks", ticks);
        nbt.putInt("strength", strength);
        saveAdditional(nbt);
    }

    public final void load(CompoundTag nbt) {
        target = null;
        targetUUID = nbt.getUUID("target");
        ticks = nbt.getLong("ticks");
        strength = nbt.getInt("strength");
        loadAdditional(nbt);
    }

}
