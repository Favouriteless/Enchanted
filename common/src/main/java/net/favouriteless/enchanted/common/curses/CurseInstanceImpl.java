package net.favouriteless.enchanted.common.curses;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.favouriteless.enchanted.api.curses.Curse;
import net.favouriteless.enchanted.api.curses.CurseInstance;
import net.favouriteless.enchanted.common.ServerConfig;
import net.favouriteless.enchanted.common.init.ESoundEvents;
import net.favouriteless.enchanted.common.util.RandomUtils;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.protocol.game.ClientboundSoundEntityPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;

import java.lang.ref.WeakReference;
import java.util.UUID;

public class CurseInstanceImpl implements CurseInstance {

    public static final Codec<CurseInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Curse.codec().fieldOf("curse").forGetter(CurseInstance::getCurse),
            UUIDUtil.CODEC.fieldOf("target").forGetter(CurseInstance::getTargetUuid),
            Codec.INT.fieldOf("level").forGetter(CurseInstance::getLevel),
            Codec.LONG.fieldOf("age").forGetter(CurseInstance::getAge)
    ).apply(instance, CurseInstanceImpl::new));

    private final Curse curse;
    private final UUID target;
    private int level;
    private long age;

    private WeakReference<ServerPlayer> targetCache;
    private long nextWhisper = 0;
    private boolean isRemoved = false;

    public CurseInstanceImpl(Curse curse, UUID target, int level, long age) {
        this.curse = curse;
        this.target = target;
    }

    /**
     * Tick this instance.
     */
    public void tick(ServerLevel level) {
        ServerPlayer target = getTarget(level);
        if(target == null)
            return;

        curse.tick(target, this.level, ++age);

        if(nextWhisper <= age) {
            target.connection.send(new ClientboundSoundEntityPacket(ESoundEvents.CURSE_WHISPER, SoundSource.AMBIENT, target, 0.1F, (float)Math.random() * 0.15F + 0.85F, RandomUtils.nextLong()));
            int min = ServerConfig.INSTANCE.curseWhisperMin.get();
            int max = ServerConfig.INSTANCE.curseWhisperMax.get();
            nextWhisper = age + RandomUtils.nextLong(min * 20L, max * 20L);
        }
        age++;
    }

    @Override
    public Curse getCurse() {
        return curse;
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public void setLevel(int level) {
        this.level = Mth.clamp(level, 0, curse.getMaxStrength());
    }

    @Override
    public long getAge() {
        return age;
    }

    @Override
    public UUID getTargetUuid() {
        return target;
    }

    @Override
    public ServerPlayer getTarget(ServerLevel level) {
        ServerPlayer player = targetCache != null ? targetCache.get() : null;
        if(player != null)
            return player;

        targetCache = new WeakReference<>(level.getServer().getPlayerList().getPlayer(target));
        return targetCache.get();
    }

    @Override
    public boolean isRemoved() {
        return this.isRemoved;
    }

    public void setRemoved() {
        this.isRemoved = true;
    }

}
