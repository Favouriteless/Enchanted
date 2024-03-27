package com.favouriteless.enchanted.datagen.providers.tag;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.common.init.EnchantedTags;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class MobEffectTagProvider extends TagsProvider<MobEffect> {

    public MobEffectTagProvider(DataGenerator generator, @Nullable ExistingFileHelper fileHelper) {
        super(generator, Registry.MOB_EFFECT, Enchanted.MOD_ID, fileHelper);
    }

    @Override
    protected void addTags() {
        addEnchantedTags();
        addVanillaTags();
    }

    public void addEnchantedTags() {
        tag(EnchantedTags.MobEffects.BLIGHT_EFFECTS)
                .add(MobEffects.BLINDNESS,  MobEffects.CONFUSION, MobEffects.HUNGER, MobEffects.POISON,
                        MobEffects.WEAKNESS, MobEffects.WITHER);
        tag(EnchantedTags.MobEffects.FERTILITY_CURE_EFFECTS)
                .add(MobEffects.BLINDNESS,  MobEffects.CONFUSION, MobEffects.HUNGER, MobEffects.POISON,
                        MobEffects.WEAKNESS, MobEffects.WITHER);
        tag(EnchantedTags.MobEffects.MISFORTUNE_EFFECTS)
                .add(MobEffects.BLINDNESS,  MobEffects.CONFUSION, MobEffects.DIG_SLOWDOWN, MobEffects.HUNGER,
                        MobEffects.MOVEMENT_SLOWDOWN, MobEffects.UNLUCK ,MobEffects.WEAKNESS);
    }

    public void addVanillaTags() {

    }

}
