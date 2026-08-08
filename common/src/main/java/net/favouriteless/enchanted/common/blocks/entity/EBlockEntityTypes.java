package net.favouriteless.enchanted.common.blocks.entity;

import net.favouriteless.enchanted.common.init.EBlocks;
import net.favouriteless.enchanted.platform.EServices;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BlockEntityType.Builder;

import java.util.function.Supplier;

public class EBlockEntityTypes {

    public static final Supplier<BlockEntityType<AltarBlockEntity>> ALTAR = register(
            "altar",
            () -> Builder.of(AltarBlockEntity::new, EBlocks.ALTAR.get())
    );
    public static final Supplier<BlockEntityType<BloodPoppyBlockEntity>> BLOOD_POPPY = register(
            "blood_poppy",
            () -> Builder.of(BloodPoppyBlockEntity::new, EBlocks.BLOOD_POPPY.get())
    );
    public static final Supplier<BlockEntityType<DistilleryBlockEntity>> DISTILLERY = register(
            "distillery",
            () -> Builder.of(DistilleryBlockEntity::new, EBlocks.DISTILLERY.get())
    );
    public static final Supplier<BlockEntityType<GoldChalkBlockEntity>> GOLD_CHALK = register(
            "gold_chalk",
            () -> Builder.of(GoldChalkBlockEntity::new, EBlocks.GOLDEN_CHALK.get())
    );
    public static final Supplier<BlockEntityType<KettleBlockEntity>> KETTLE = register(
            "kettle",
            () -> Builder.of(KettleBlockEntity::new, EBlocks.KETTLE.get())
    );
    public static final Supplier<BlockEntityType<MortarBlockEntity>> MORTAR = register(
            "mortar",
            () -> Builder.of(MortarBlockEntity::new, EBlocks.MORTAR.get())
    );
    public static final Supplier<BlockEntityType<PoppetShelfBlockEntity>> POPPET_SHELF = register(
            "poppet_shelf",
            () -> Builder.of(PoppetShelfBlockEntity::new, EBlocks.POPPET_SHELF.get())
    );
    public static final Supplier<BlockEntityType<SpinningWheelBlockEntity>> SPINNING_WHEEL = register(
            "spinning_wheel",
            () -> Builder.of(SpinningWheelBlockEntity::new, EBlocks.SPINNING_WHEEL.get())
    );
    public static final Supplier<BlockEntityType<WitchCauldronBlockEntity>> WITCH_CAULDRON = register(
            "witch_cauldron",
            () -> Builder.of(WitchCauldronBlockEntity::new, EBlocks.WITCH_CAULDRON.get())
    );
    public static final Supplier<BlockEntityType<WitchOvenBlockEntity>> WITCH_OVEN = register(
            "witch_oven",
            () -> Builder.of(WitchOvenBlockEntity::new, EBlocks.WITCH_OVEN.get())
    );


    private static <T extends BlockEntity> Supplier<BlockEntityType<T>> register(String name, Supplier<BlockEntityType.Builder<T>> type) {
        return EServices.REGISTRY.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, name, () -> type.get().build(null));
    }

    public static void load() {
    } // Method which exists purely to load the class.

}
