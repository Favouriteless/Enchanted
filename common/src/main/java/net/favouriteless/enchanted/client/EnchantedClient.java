package net.favouriteless.enchanted.client;

import net.favouriteless.enchanted.common.circle_magic.CircleMagicShape;
import net.favouriteless.enchanted.common.init.EKeybinds;
import net.favouriteless.enchanted.integrations.modopedia.EModopedia;
import net.favouriteless.enchanted.integrations.modopedia.client.init.EBookScreenFactories;
import net.favouriteless.enchanted.integrations.modopedia.client.init.EPageComponents;
import net.favouriteless.enchanted.integrations.modopedia.client.init.ETemplateProcessors;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

public class EnchantedClient {

    public static void init() {
        EKeybinds.load();
        EModopedia.initClient();
    }

    @Nullable
    public static ResourceLocation getShapeGuiTexture(ResourceKey<CircleMagicShape> shape, Block block) {
        if(shape == null || block == null)
            return null;

        ResourceLocation id = shape.location();
        ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(block);

        ResourceLocation tex = ResourceLocation.fromNamespaceAndPath(
                id.getNamespace(),
                String.format("textures/enchanted/circle_magic/shapes/%s/%s/%s.png", id.getPath(), blockId.getNamespace(), blockId.getPath())
        );

        return Minecraft.getInstance().getResourceManager().getResource(tex).isPresent() ? tex : null;
    }

}
