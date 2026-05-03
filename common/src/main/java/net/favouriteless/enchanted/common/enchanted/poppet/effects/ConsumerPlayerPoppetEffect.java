package net.favouriteless.enchanted.common.enchanted.poppet.effects;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class ConsumerPlayerPoppetEffect extends EffectsPlayerPoppetEffect {

    private final Consumer<Player> consumer;

    public ConsumerPlayerPoppetEffect(Predicate<DamageSource> predicate, Consumer<Player> consumer, List<Supplier<MobEffectInstance>> effects) {
        super(predicate, effects);
        this.consumer = consumer;
    }

    @SafeVarargs
    public static ConsumerPlayerPoppetEffect of(Predicate<DamageSource> predicate, Consumer<Player> consumer, Supplier<MobEffectInstance>... effects) {
        return new ConsumerPlayerPoppetEffect(predicate, consumer, List.of(effects));
    }

    public static ConsumerPlayerPoppetEffect ofDefault(Predicate<DamageSource> predicate, Consumer<Player> consumer) {
        return new ConsumerPlayerPoppetEffect(predicate, consumer, List.of(() -> new MobEffectInstance(MobEffects.REGENERATION, 100, 1)));
    }

    @SafeVarargs
    public static ConsumerPlayerPoppetEffect ofDefaultPlus(Predicate<DamageSource> predicate, Consumer<Player> consumer, Supplier<MobEffectInstance>... effects) {
        List<Supplier<MobEffectInstance>> list = new ArrayList<>(effects.length + 1);
        list.add(() -> new MobEffectInstance(MobEffects.REGENERATION, 100, 1));
        list.addAll(Arrays.asList(effects));
        return new ConsumerPlayerPoppetEffect(predicate, consumer, list);
    }

    @Override
    public void protect(Player player) {
        super.protect(player);
        consumer.accept(player);
    }

}
