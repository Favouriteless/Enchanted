package net.favouriteless.enchanted.neoforge.datagen.providers.tag;

import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.enchanted.common.init.ETags;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;

import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class EMobEffectTagProvider extends IntrinsicHolderTagsProvider<MobEffect> {

    public EMobEffectTagProvider(PackOutput output, CompletableFuture<Provider> provider, ExistingFileHelper existingFileHelper) {
        super(output, Registries.MOB_EFFECT, provider, mobEffect -> BuiltInRegistries.MOB_EFFECT.getResourceKey(mobEffect).orElse(null), Enchanted.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(Provider provider) {
        addEnchantedTags(provider);
    }

    public void addEnchantedTags(Provider provider) {
        tag(ETags.MobEffects.BLIGHT_EFFECTS)
                .add(
                        MobEffects.BLINDNESS.getKey(), MobEffects.CONFUSION.getKey(), MobEffects.HUNGER.getKey(), MobEffects.POISON.getKey(),
                        MobEffects.WEAKNESS.getKey(), MobEffects.WITHER.getKey()
                );
        tag(ETags.MobEffects.FERTILITY_CURE_EFFECTS)
                .add(
                        MobEffects.BLINDNESS.getKey(), MobEffects.CONFUSION.getKey(), MobEffects.HUNGER.getKey(), MobEffects.POISON.getKey(),
                        MobEffects.WEAKNESS.getKey(), MobEffects.WITHER.getKey()
                );
        tag(ETags.MobEffects.MISFORTUNE_EFFECTS)
                .add(
                        MobEffects.BLINDNESS.getKey(), MobEffects.CONFUSION.getKey(), MobEffects.DIG_SLOWDOWN.getKey(), MobEffects.HUNGER.getKey(),
                        MobEffects.MOVEMENT_SLOWDOWN.getKey(), MobEffects.UNLUCK.getKey(), MobEffects.WEAKNESS.getKey()
                );
    }

}
