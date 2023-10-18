package com.favouriteless.enchanted.common.init.registry;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.common.blockentities.*;

import com.favouriteless.enchanted.common.blockentities.AltarBlockEntity;
import com.favouriteless.enchanted.platform.RegistryHandler;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public class EnchantedBlockEntityTypes {

    public static final Supplier<BlockEntityType<ChalkGoldBlockEntity>> CHALK_GOLD = register("chalk_gold",
            () -> BlockEntityType.Builder.of(ChalkGoldBlockEntity::new, EnchantedBlocks.CHALK_GOLD.get()).build(Enchanted.location("chalk_gold")));

    public static final Supplier<BlockEntityType<WitchOvenBlockEntity>> WITCH_OVEN = register("witch_oven",
            () -> BlockEntityType.Builder.of(WitchOvenBlockEntity::new, EnchantedBlocks.WITCH_OVEN.get()).build(Enchanted.location("witch_oven")));

    public static final Supplier<BlockEntityType<DistilleryBlockEntity>> DISTILLERY = register("distillery",
            () -> BlockEntityType.Builder.of(DistilleryBlockEntity::new, EnchantedBlocks.DISTILLERY.get()).build(Enchanted.location("distillery")));

    public static final Supplier<BlockEntityType<AltarBlockEntity>> ALTAR = register("altar",
            () -> BlockEntityType.Builder.of(AltarBlockEntity::new, EnchantedBlocks.ALTAR.get()).build(Enchanted.location("altar")));

    public static final Supplier<BlockEntityType<BloodPoppyBlockEntity>> BLOOD_POPPY = register("blood_poppy",
            () -> BlockEntityType.Builder.of(BloodPoppyBlockEntity::new, EnchantedBlocks.BLOOD_POPPY.get()).build(Enchanted.location("blood_poppy")));

    public static final Supplier<BlockEntityType<WitchCauldronBlockEntity>> WITCH_CAULDRON = register("witch_cauldron",
            () -> BlockEntityType.Builder.of(WitchCauldronBlockEntity::new, EnchantedBlocks.WITCH_CAULDRON.get()).build(Enchanted.location("witch_cauldron")));

    public static final Supplier<BlockEntityType<KettleBlockEntity>> KETTLE = register("kettle",
            () -> BlockEntityType.Builder.of(KettleBlockEntity::new, EnchantedBlocks.KETTLE.get()).build(Enchanted.location("kettle")));

    public static final Supplier<BlockEntityType<SpinningWheelBlockEntity>> SPINNING_WHEEL = register("spinning_wheel",
            () -> BlockEntityType.Builder.of(SpinningWheelBlockEntity::new, EnchantedBlocks.SPINNING_WHEEL.get()).build(Enchanted.location("spinning_wheel")));

    public static final Supplier<BlockEntityType<PoppetShelfBlockEntity>> POPPET_SHELF = register("poppet_shelf",
            () -> BlockEntityType.Builder.of(PoppetShelfBlockEntity::new, EnchantedBlocks.POPPET_SHELF.get()).build(Enchanted.location("poppet_shelf")));



    private static <T extends EntityType<?>> Supplier<T> register(String name, Supplier<T> entityTypeSupplier) {
        return RegistryHandler.register(Registry.ENTITY_TYPE, name, entityTypeSupplier);
    }

    public static void load() {} // Method which exists purely to load the class.

}
