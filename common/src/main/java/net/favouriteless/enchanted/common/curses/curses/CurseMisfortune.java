package net.favouriteless.enchanted.common.curses.curses;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.enchanted.common.init.ETags.MobEffects;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;

public class CurseMisfortune extends AbstractRandomCurse {

    private static final MapCodec<CurseMisfortune> CODEC = RecordCodecBuilder.mapCodec(instance -> codecStart(instance).apply(instance, CurseMisfortune::new));
    public static final Type<CurseMisfortune> TYPE = new Type<>(Enchanted.id("misfortune"), () -> new CurseMisfortune(0), CODEC);

    private static final RandomSource random = RandomSource.create();

    public CurseMisfortune(long next) {
        super(120, 300, next);
    }

    @Override
    protected void execute(ServerPlayer target, int strength, long age) {
        Holder<MobEffect> effect = BuiltInRegistries.MOB_EFFECT.getOrCreateTag(MobEffects.MISFORTUNE_EFFECTS).getRandomElement(random).orElse(null);
        if(effect == null)
            return;

        int effectLevel = 0;
        int duration = 30;
        for(int i = 0; i < strength; i++) {
            if(Math.random() < 0.25D)
                effectLevel++; // Every additional curse level has a 25% weight to increase the effect level
            if(Math.random() < 0.25D)
                duration += 15; // Every additional curse level has a 25% weight to increase duration by 15 seconds
        }
        target.addEffect(new MobEffectInstance(effect, duration*20, effectLevel));
    }

    @Override
    public Type<?> type() {
        return TYPE;
    }

}
