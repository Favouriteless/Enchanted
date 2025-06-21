package net.favouriteless.enchanted.common;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class CommonConfig {

    public static final CommonConfig INSTANCE;
    public static final ModConfigSpec SPEC;

    private CommonConfig(ModConfigSpec.Builder builder) {
    }

    static {
        Pair<CommonConfig, ModConfigSpec> pair = new ModConfigSpec.Builder().configure(CommonConfig::new);
        INSTANCE = pair.getLeft();
        SPEC = pair.getRight();
    }

}