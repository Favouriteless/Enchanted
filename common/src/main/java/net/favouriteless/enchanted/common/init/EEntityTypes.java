package net.favouriteless.enchanted.common.init;

import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.enchanted.common.entities.*;
import net.favouriteless.enchanted.platform.EServices;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityType.Builder;
import net.minecraft.world.entity.MobCategory;

import java.util.function.Supplier;

public class EEntityTypes {

    public static final Supplier<EntityType<Broomstick>> BROOMSTICK = register("broomstick", () -> Builder.of(Broomstick::new, MobCategory.MISC)
            .sized(1.0F, 1.0F).clientTrackingRange(10).build(Enchanted.id("broomstick").toString()));

//    public static final Supplier<EntityType<Ent>> ENT = register("ent", () -> Builder.of(Ent::new, MobCategory.MONSTER)
//            .sized(2F, 3.0F).build(Enchanted.location("ent").toString()));

    public static final Supplier<EntityType<FamiliarCat>> FAMILIAR_CAT = register("familiar_cat", () -> Builder.of(FamiliarCat::new, MobCategory.CREATURE)
            .sized(0.6F, 0.7F).clientTrackingRange(8).build(Enchanted.id("familiar_cat").toString()));

    public static final Supplier<EntityType<Mandrake>> MANDRAKE = register("mandrake", () -> Builder.of(Mandrake::new, MobCategory.MONSTER)
            .sized(0.4F, 0.7F).build(Enchanted.id("mandrake").toString()));

    public static final Supplier<EntityType<ThrowableBrew>> THROWABLE_BREW = register("throwable_brew", () -> Builder.of(ThrowableBrew::new, MobCategory.MISC)
            .sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10).build(Enchanted.id("throwable_brew").toString()));

    public static final Supplier<EntityType<VoodooItemEntity>> VOODOO_ITEM = register("voodoo_item", () -> Builder.of(VoodooItemEntity::new, MobCategory.MISC)
            .sized(0.25F, 0.25F).clientTrackingRange(6).updateInterval(20).build(Enchanted.id("voodoo_item").toString()));



    private static <T extends EntityType<?>> Supplier<T> register(String name, Supplier<T> entityTypeSupplier) {
        return EServices.REGISTRY.register(BuiltInRegistries.ENTITY_TYPE, name, entityTypeSupplier);
    }

    public static void load() {} // Method which exists purely to load the class.

}