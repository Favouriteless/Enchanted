package net.favouriteless.enchanted.common.items.brews;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.level.Level;

import java.util.function.Supplier;

public class SimpleEffectBrewItem extends ConsumableBrewItem {

	private final Supplier<Holder<MobEffect>> effect;
	private final int duration;
	private final int amplifier;

	public SimpleEffectBrewItem(Supplier<Holder<MobEffect>> effect, int duration, int amplifier, Properties properties) {
		super(properties);
		this.effect = effect;
		this.duration = duration;
		this.amplifier = amplifier;
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity entity) {
		stack.shrink(1);
		entity.addEffect(new MobEffectInstance(effect.get(), duration, amplifier));
		return super.finishUsingItem(stack, world, entity);
	}

}
