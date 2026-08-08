package net.favouriteless.enchanted.common.init;

import net.favouriteless.enchanted.common.init.EFeatures.Trees;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

import java.util.Optional;

public class ETreeGrowers {

    public static final TreeGrower ALDER = create("alder", null, Trees.ALDER, null);
    public static final TreeGrower HAWTHORN = create("hawthorn", null, Trees.HAWTHORN, null);
    public static final TreeGrower ROWAN = create("rowan", null, Trees.ROWAN, null);

    public static TreeGrower create(String name,
                                    ResourceKey<ConfiguredFeature<?, ?>> mega,
                                    ResourceKey<ConfiguredFeature<?, ?>> normal,
                                    ResourceKey<ConfiguredFeature<?, ?>> flowers) {
        return new TreeGrower(
                name,
                mega != null ? Optional.of(mega) : Optional.empty(),
                normal != null ? Optional.of(normal) : Optional.empty(),
                flowers != null ? Optional.of(flowers) : Optional.empty()
        );
    }
}
