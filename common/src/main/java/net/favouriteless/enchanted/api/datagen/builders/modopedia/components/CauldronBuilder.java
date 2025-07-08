package net.favouriteless.enchanted.api.datagen.builders.modopedia.components;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.favouriteless.enchanted.integrations.modopedia.client.init.EPageComponents;
import net.favouriteless.modopedia.api.datagen.builders.PageComponentBuilder;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;

public class CauldronBuilder extends PageComponentBuilder {

    public static final ResourceLocation ID = EPageComponents.ID_CAULDRON;

    protected CauldronBuilder() {
        super(ID);
    }

    public static CauldronBuilder of() {
        return new CauldronBuilder();
    }

    @Override
    protected void build(JsonObject jsonObject, RegistryOps<JsonElement> registryOps) {}

}
