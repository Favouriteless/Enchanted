package com.favouriteless.enchanted.datagen.providers.tag;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.common.init.EnchantedTags.EntityTypes;
import com.favouriteless.enchanted.common.init.registry.EnchantedEntityTypes;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class EntityTypeTagProvider extends EntityTypeTagsProvider {

    public EntityTypeTagProvider(DataGenerator generator, @Nullable ExistingFileHelper fileHelper) {
        super(generator, Enchanted.MOD_ID, fileHelper);
    }

    @Override
    protected void addTags() {
        addEnchantedTags();
        addVanillaTags();
    }

    public void addEnchantedTags() {
        tag(EntityTypes.MONSTERS)
                .add(EntityType.BLAZE, EntityType.CAVE_SPIDER, EntityType.CREEPER, EntityType.DROWNED,
                        EntityType.ELDER_GUARDIAN, EntityType.ENDERMITE, EntityType.GHAST, EntityType.GIANT,
                        EntityType.GUARDIAN, EntityType.HOGLIN, EntityType.HUSK, EntityType.ILLUSIONER,
                        EntityType.MAGMA_CUBE, EntityType.PHANTOM, EntityType.PIGLIN, EntityType.PIGLIN_BRUTE,
                        EntityType.PILLAGER, EntityType.RAVAGER, EntityType.SHULKER, EntityType.SILVERFISH,
                        EntityType.SKELETON, EntityType.SLIME, EntityType.SPIDER, EntityType.STRAY, EntityType.VEX,
                        EntityType.WITCH, EntityType.WITHER_SKELETON, EntityType.ZOMBIE, EntityType.ZOMBIE_VILLAGER,
                        EntityType.ZOMBIFIED_PIGLIN, EntityType.ZOGLIN);
        tag(EntityTypes.TAGLOCK_BLACKLIST)
                .add(EntityType.ENDER_DRAGON, EntityType.WITHER, EnchantedEntityTypes.ENT.get(),
                        EnchantedEntityTypes.FAMILIAR_CAT.get());
    }

    public void addVanillaTags() {

    }

}
