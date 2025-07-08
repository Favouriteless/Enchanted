package net.favouriteless.enchanted.api.datagen.builders.modopedia.components;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.favouriteless.enchanted.integrations.modopedia.client.init.EPageComponents;
import net.favouriteless.modopedia.api.datagen.builders.PageComponentBuilder;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;

public class MutagenBuilder extends PageComponentBuilder {

    public static final ResourceLocation ID = EPageComponents.ID_MUTAGEN;

    protected MutagenBuilder() {
        super(ID);
    }

    public static MutagenBuilder of() {
        return new MutagenBuilder();
    }

    @Override
    protected void build(JsonObject jsonObject, RegistryOps<JsonElement> registryOps) {}

}
