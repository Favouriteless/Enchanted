package net.favouriteless.enchanted.common.enchanted.poppet.effects;

import net.favouriteless.enchanted.api.poppets.PlayerPoppetEffect;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class EffectsPlayerPoppetEffect implements PlayerPoppetEffect {

    private final Predicate<DamageSource> predicate;
    private final List<Supplier<MobEffectInstance>> effects;

    public EffectsPlayerPoppetEffect(Predicate<DamageSource> predicate, List<Supplier<MobEffectInstance>> effects) {
        this.predicate = predicate;
        this.effects = effects;
    }

    @SafeVarargs
    public static EffectsPlayerPoppetEffect of(Predicate<DamageSource> predicate, Supplier<MobEffectInstance>... effects) {
        return new EffectsPlayerPoppetEffect(predicate, List.of(effects));
    }

    public static EffectsPlayerPoppetEffect ofDefault(Predicate<DamageSource> predicate) {
        return new EffectsPlayerPoppetEffect(predicate, List.of(() -> new MobEffectInstance(MobEffects.REGENERATION, 100, 1)));
    }

    @SafeVarargs
    public static EffectsPlayerPoppetEffect ofDefaultPlus(Predicate<DamageSource> predicate, Supplier<MobEffectInstance>... effects) {
        List<Supplier<MobEffectInstance>> list = new ArrayList<>(effects.length + 1);
        list.add(() -> new MobEffectInstance(MobEffects.REGENERATION, 100, 1));
        list.addAll(Arrays.asList(effects));
        return new EffectsPlayerPoppetEffect(predicate, list);
    }


    @Override
    public boolean protectsAgainst(DamageSource damageSource) {
        return predicate.test(damageSource);
    }

    @Override
    public void protect(Player player) {
        player.setHealth(1);
        for (Supplier<MobEffectInstance> supplier : effects) {
            player.addEffect(supplier.get());
        }
    }

}
