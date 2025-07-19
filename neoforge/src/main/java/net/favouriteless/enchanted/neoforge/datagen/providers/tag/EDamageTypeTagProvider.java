package net.favouriteless.enchanted.neoforge.datagen.providers.tag;

import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.enchanted.common.init.EDamageTypes;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraft.tags.DamageTypeTags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class EDamageTypeTagProvider extends DamageTypeTagsProvider {

    public EDamageTypeTagProvider(PackOutput output, CompletableFuture<Provider> provider, ExistingFileHelper existingFileHelper) {
        super(output, provider, Enchanted.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(Provider provider) {
        addVanillaTags(provider);
    }

    public void addVanillaTags(Provider provider) {
        tag(DamageTypeTags.BYPASSES_ARMOR)
                .add(EDamageTypes.SACRIFICE, EDamageTypes.SOUND, EDamageTypes.VOODOO, EDamageTypes.INTERNAL_EXPLOSION);
        tag(DamageTypeTags.BYPASSES_EFFECTS)
                .add(EDamageTypes.SACRIFICE, EDamageTypes.SOUND, EDamageTypes.VOODOO, EDamageTypes.INTERNAL_EXPLOSION);
        tag(DamageTypeTags.BYPASSES_COOLDOWN)
                .add(EDamageTypes.SACRIFICE, EDamageTypes.INTERNAL_EXPLOSION);
        tag(DamageTypeTags.BYPASSES_ENCHANTMENTS)
                .add(EDamageTypes.SACRIFICE, EDamageTypes.SOUND, EDamageTypes.VOODOO, EDamageTypes.INTERNAL_EXPLOSION);
        tag(DamageTypeTags.BYPASSES_INVULNERABILITY)
                .add(EDamageTypes.SACRIFICE, EDamageTypes.INTERNAL_EXPLOSION);
        tag(DamageTypeTags.BYPASSES_RESISTANCE)
                .add(EDamageTypes.SACRIFICE, EDamageTypes.SOUND, EDamageTypes.VOODOO, EDamageTypes.INTERNAL_EXPLOSION);
    }

}
