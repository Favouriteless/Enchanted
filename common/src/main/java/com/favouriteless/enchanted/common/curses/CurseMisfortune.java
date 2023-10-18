package com.favouriteless.enchanted.common.curses;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.api.curses.AbstractRandomCurse;
import com.favouriteless.enchanted.common.init.registry.CurseTypes;
import com.favouriteless.enchanted.common.init.EnchantedTags;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;

public class CurseMisfortune extends AbstractRandomCurse {

	public CurseMisfortune() {
		super(CurseTypes.MISFORTUNE, 120, 300); // Executes once every 2-5 minutes
	}

	@Override
	protected void execute() {
		MobEffect effect = ForgeRegistries.MOB_EFFECTS.tags().getTag(EnchantedTags.MobEffects.MISFORTUNE_EFFECTS).getRandomElement(Enchanted.RANDOM).orElse(null);
		if(effect != null) {
			int effectLevel = 0;
			int effectDuration = 30;
			for(int i = 0; i < level; i++) {
				if(Math.random() < 0.25D)
					effectLevel++; // Every additional curse level has a 25% chance to increase the effect level
				if(Math.random() < 0.25D)
					effectDuration += 15; // Every additional curse level has a 25% chance to increase duration by 15 seconds
			}
			targetPlayer.addEffect(new MobEffectInstance(effect, effectDuration*20, effectLevel));
		}
	}

}
