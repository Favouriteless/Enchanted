package net.favouriteless.enchanted.common;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class LootExtensions {

    private static final Map<ResourceLocation, LootExtension> extensions = new HashMap<>();

    static {
        register(ResourceLocation.withDefaultNamespace("blocks/short_grass"), new LootExtension(Enchanted.id("extensions/grass_seeds"), c -> !ServerConfig.INSTANCE.hoeOnlySeeds.get() || c.getParam(LootContextParams.TOOL).getItem() instanceof HoeItem));
        register(ResourceLocation.withDefaultNamespace("entities/bat"), new LootExtension(Enchanted.id("extensions/arthana/bat"), c -> true));
        register(ResourceLocation.withDefaultNamespace("entities/creeper"), new LootExtension(Enchanted.id("extensions/arthana/creeper"), c -> true));
        register(ResourceLocation.withDefaultNamespace("entities/wolf"), new LootExtension(Enchanted.id("extensions/arthana/wolf"), c -> true));
    }

    public static void register(ResourceLocation id, LootExtension extension) {
        extensions.put(id, extension);
    }

    // ----------------------------------------- IMPLEMENTATION DETAILS BELOW -----------------------------------------

    public static void tryRoll(LootTable table, LootContext context, Consumer<ItemStack> output) {
        Registry<LootTable> registry = context.getLevel().getServer().reloadableRegistries().get().registryOrThrow(Registries.LOOT_TABLE);
        LootExtension extension = extensions.get(registry.getKey(table));
        if(extension == null)
            return;

        if(!extension.predicate().test(context))
            return;

        LootTable extended = registry.get(extension.table());
        if(extended == null)
            return;

        extended.getRandomItemsRaw(context, output);
    }

    public record LootExtension(ResourceLocation table, Predicate<LootContext> predicate) {}

}
