package net.favouriteless.enchanted.common.curses;

import net.favouriteless.enchanted.api.curses.RandomCurse;
import net.favouriteless.enchanted.common.init.ETags.MobEffects;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;

public class CurseMisfortune extends RandomCurse {

	private static final RandomSource random = RandomSource.create();

	public CurseMisfortune() {
		super(CurseTypes.MISFORTUNE, 120, 300); // Executes once every 2-5 minutes
	}

	@Override
	protected void execute(ServerPlayer target) {
		Holder<MobEffect> effect = BuiltInRegistries.MOB_EFFECT.getOrCreateTag(MobEffects.MISFORTUNE_EFFECTS).getRandomElement(random).orElse(null);
		if(effect == null)
			return;

        int level = 0;
        int duration = 30;
        for(int i = 0; i < strength; i++) {
            if(Math.random() < 0.25D)
                level++; // Every additional curse level has a 25% weight to increase the effect level
            if(Math.random() < 0.25D)
                duration += 15; // Every additional curse level has a 25% weight to increase duration by 15 seconds
        }
        target.addEffect(new MobEffectInstance(effect, duration*20, level));
    }

}
