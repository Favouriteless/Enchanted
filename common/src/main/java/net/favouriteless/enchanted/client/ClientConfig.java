package net.favouriteless.enchanted.client;

import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec.BooleanValue;
import org.apache.commons.lang3.tuple.Pair;

public class ClientConfig {

    public static final ClientConfig INSTANCE;
    public static final ModConfigSpec SPEC;

    public final BooleanValue useOriginalCatType;

    private ClientConfig(ModConfigSpec.Builder builder) {
        useOriginalCatType = builder.comment("Render cat familiars with their original fur colour rather than all black").define("original_cat_type", false);
    }

    static {
        Pair<ClientConfig, ModConfigSpec> pair = new ModConfigSpec.Builder().configure(ClientConfig::new);
        INSTANCE = pair.getLeft();
        SPEC = pair.getRight();
    }

}