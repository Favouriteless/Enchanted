package net.favouriteless.enchanted.neoforge.datagen.providers.tag;

import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.enchanted.common.init.EEntityTypes;
import net.favouriteless.enchanted.common.init.ETags.EntityTypes;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class EEntityTypeTagProvider extends IntrinsicHolderTagsProvider<EntityType<?>> {

    public EEntityTypeTagProvider(PackOutput output, CompletableFuture<Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, Registries.ENTITY_TYPE, lookupProvider, entityType -> entityType.builtInRegistryHolder().key(), Enchanted.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(Provider provider) {
        addEnchantedTags();
    }

    public void addEnchantedTags() {
        tag(EntityTypes.GROTESQUE_IMMUNE)
                .add(EntityType.ENDER_DRAGON, EntityType.ELDER_GUARDIAN, EntityType.PILLAGER, EntityType.EVOKER,
                        EntityType.VINDICATOR, EntityType.RAVAGER, EntityType.IRON_GOLEM, EntityType.WARDEN,
                        EntityType.VEX, EntityType.WITCH, EntityType.PIGLIN_BRUTE);
        tag(EntityTypes.MONSTERS)
                .add(EntityType.BLAZE, EntityType.CAVE_SPIDER, EntityType.CREEPER, EntityType.DROWNED,
                        EntityType.ELDER_GUARDIAN, EntityType.ENDERMITE, EntityType.GHAST, EntityType.GIANT,
                        EntityType.GUARDIAN, EntityType.HOGLIN, EntityType.HUSK, EntityType.ILLUSIONER,
                        EntityType.MAGMA_CUBE, EntityType.PHANTOM, EntityType.PIGLIN, EntityType.PIGLIN_BRUTE,
                        EntityType.PILLAGER, EntityType.RAVAGER, EntityType.SHULKER, EntityType.SILVERFISH,
                        EntityType.SKELETON, EntityType.SLIME, EntityType.SPIDER, EntityType.STRAY, EntityType.VEX,
                        EntityType.WITCH, EntityType.WITHER_SKELETON, EntityType.ZOMBIE, EntityType.ZOMBIE_VILLAGER,
                        EntityType.ZOMBIFIED_PIGLIN, EntityType.ZOGLIN, EntityType.WARDEN);
        tag(EntityTypes.TAGLOCK_BLACKLIST)
                .add(EntityType.ENDER_DRAGON, EntityType.WITHER, EntityType.WARDEN, EEntityTypes.FAMILIAR_CAT.get());
    }

}
