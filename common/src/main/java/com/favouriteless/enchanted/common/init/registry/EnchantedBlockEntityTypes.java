package com.favouriteless.enchanted.common.init.registry;

import com.favouriteless.enchanted.common.blockentities.*;
import com.favouriteless.enchanted.platform.Services;
import net.minecraft.core.Registry;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public class EnchantedBlockEntityTypes {

    public static final Supplier<BlockEntityType<ChalkGoldBlockEntity>> CHALK_GOLD = register("chalk_gold",
            () -> BlockEntityType.Builder.of(ChalkGoldBlockEntity::new, EnchantedBlocks.CHALK_GOLD.get()).build(null));

    public static final Supplier<BlockEntityType<WitchOvenBlockEntity>> WITCH_OVEN = register("witch_oven",
            () -> BlockEntityType.Builder.of(WitchOvenBlockEntity::new, EnchantedBlocks.WITCH_OVEN.get()).build(null));

    public static final Supplier<BlockEntityType<DistilleryBlockEntity>> DISTILLERY = register("distillery",
            () -> BlockEntityType.Builder.of(DistilleryBlockEntity::new, EnchantedBlocks.DISTILLERY.get()).build(null));

    public static final Supplier<BlockEntityType<AltarBlockEntity>> ALTAR = register("altar",
            () -> BlockEntityType.Builder.of(AltarBlockEntity::new, EnchantedBlocks.ALTAR.get()).build(null));

    public static final Supplier<BlockEntityType<BloodPoppyBlockEntity>> BLOOD_POPPY = register("blood_poppy",
            () -> BlockEntityType.Builder.of(BloodPoppyBlockEntity::new, EnchantedBlocks.BLOOD_POPPY.get()).build(null));

    public static final Supplier<BlockEntityType<WitchCauldronBlockEntity>> WITCH_CAULDRON = register("witch_cauldron",
            () -> BlockEntityType.Builder.of(WitchCauldronBlockEntity::new, EnchantedBlocks.WITCH_CAULDRON.get()).build(null));

    public static final Supplier<BlockEntityType<KettleBlockEntity>> KETTLE = register("kettle",
            () -> BlockEntityType.Builder.of(KettleBlockEntity::new, EnchantedBlocks.KETTLE.get()).build(null));

    public static final Supplier<BlockEntityType<SpinningWheelBlockEntity>> SPINNING_WHEEL = register("spinning_wheel",
            () -> BlockEntityType.Builder.of(SpinningWheelBlockEntity::new, EnchantedBlocks.SPINNING_WHEEL.get()).build(null));

    public static final Supplier<BlockEntityType<PoppetShelfBlockEntity>> POPPET_SHELF = register("poppet_shelf",
            () -> BlockEntityType.Builder.of(PoppetShelfBlockEntity::new, EnchantedBlocks.POPPET_SHELF.get()).build(null));



    private static <T extends BlockEntity> Supplier<BlockEntityType<T>> register(String name, Supplier<BlockEntityType<T>> type) {
        return Services.COMMON_REGISTRY.register(Registry.BLOCK_ENTITY_TYPE, name, type);
    }

    public static void load() {} // Method which exists purely to load the class.

}
