package com.favouriteless.enchanted.common.curses;

import com.favouriteless.enchanted.api.curses.RandomCurse;
import com.favouriteless.enchanted.common.init.EnchantedTags.MobEffects;
import com.favouriteless.enchanted.common.init.registry.CurseTypes;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;

public class CurseMisfortune extends RandomCurse<CurseMisfortune> {

	private static final RandomSource random = RandomSource.create();

	public CurseMisfortune() {
		super(CurseTypes.MISFORTUNE, 120, 300); // Executes once every 2-5 minutes
	}

	@Override
	protected void execute() {
		Holder<MobEffect> effect = Registry.MOB_EFFECT.getOrCreateTag(MobEffects.MISFORTUNE_EFFECTS).getRandomElement(random).orElse(null);
		if(effect != null) {
			int effectLevel = 0;
			int effectDuration = 30;
			for(int i = 0; i < level; i++) {
				if(Math.random() < 0.25D)
					effectLevel++; // Every additional curse level has a 25% chance to increase the effect level
				if(Math.random() < 0.25D)
					effectDuration += 15; // Every additional curse level has a 25% chance to increase duration by 15 seconds
			}
			targetPlayer.addEffect(new MobEffectInstance(effect.value(), effectDuration*20, effectLevel));
		}
	}

}
