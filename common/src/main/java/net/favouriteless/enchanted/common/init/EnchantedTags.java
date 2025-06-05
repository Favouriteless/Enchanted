package net.favouriteless.enchanted.common.init;

import net.favouriteless.enchanted.common.Enchanted;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;

public class EnchantedTags {

    public static class Blocks {
        public static final TagKey<Block> ALDER_LOGS = createBlockTag(Enchanted.id("alder_logs"));
        public static final TagKey<Block> BLIGHT_DECAYABLE_BLOCKS = createBlockTag(Enchanted.id("blight_decayable_blocks"));
        public static final TagKey<Block> BLIGHT_DECAYABLE_PLANTS = createBlockTag(Enchanted.id("blight_decayable_plants"));
        public static final TagKey<Block> BLIGHT_DECAY_BLOCKS = createBlockTag(Enchanted.id("blight_decay_blocks"));
        public static final TagKey<Block> BROOM_SWEEPABLE = createBlockTag(Enchanted.id("broom_sweepable"));
        public static final TagKey<Block> CHALICES = createBlockTag(Enchanted.id("chalices"));
        public static final TagKey<Block> CHALKS = createBlockTag(Enchanted.id("chalks"));
        public static final TagKey<Block> CROPS = createBlockTag(Enchanted.id("crops"));
        public static final TagKey<Block> EMBER_MOSS_SPREADS_ON = createBlockTag(Enchanted.id("ember_moss_spreads_on"));
        public static final TagKey<Block> FENCES = createBlockTag(Enchanted.id("fences"));
        public static final TagKey<Block> FENCE_GATES = createBlockTag(Enchanted.id("fence_gates"));
        public static final TagKey<Block> GLINT_WEED_SPREADS_ON = createBlockTag(Enchanted.id("glint_weed_spreads_on"));
        public static final TagKey<Block> HAWTHORN_LOGS = createBlockTag(Enchanted.id("hawthorn_logs"));
        public static final TagKey<Block> LEAVES = createBlockTag(Enchanted.id("leaves"));
        public static final TagKey<Block> LOGS = createBlockTag(Enchanted.id("logs"));
        public static final TagKey<Block> MUTANDIS_BLACKLIST = createBlockTag(Enchanted.id("mutandis_blacklist"));
        public static final TagKey<Block> MUTANDIS_EXTREMIS_BLACKLIST = createBlockTag(Enchanted.id("mutandis_extremis_blacklist"));
        public static final TagKey<Block> MUTANDIS_EXTREMIS = createBlockTag(Enchanted.id("mutandis_extremis"));
        public static final TagKey<Block> MUTANDIS = createBlockTag(Enchanted.id("mutandis"));
        public static final TagKey<Block> PLANKS = createBlockTag(Enchanted.id("planks"));
        public static final TagKey<Block> RITE_FOREST_REPLACEABLE = createBlockTag(Enchanted.id("rite_forest_replaceable"));
        public static final TagKey<Block> ROWAN_LOGS = createBlockTag(Enchanted.id("rowan_logs"));
        public static final TagKey<Block> SAPLINGS = createBlockTag(Enchanted.id("saplings"));
        public static final TagKey<Block> SLABS = createBlockTag(Enchanted.id("slabs"));
        public static final TagKey<Block> STAIRS = createBlockTag(Enchanted.id("stairs"));
        public static final TagKey<Block> WOODEN_FENCES = createBlockTag(Enchanted.id("wooden_fences"));
        public static final TagKey<Block> WOODEN_SLABS = createBlockTag(Enchanted.id("wooden_slabs"));
        public static final TagKey<Block> WOODEN_STAIRS = createBlockTag(Enchanted.id("wooden_stairs"));
    }

    public static class Items {
        public static final TagKey<Item> ALDER_LOGS = createItemTag(Enchanted.id("alder_logs"));
        public static final TagKey<Item> ARMORS = createItemTag(Enchanted.id("armors"));
        public static final TagKey<Item> ARMOR_POPPET_BLACKLIST = createItemTag(Enchanted.id("armor_poppet_blacklist"));
        public static final TagKey<Item> ARMOR_POPPET_WHITELIST = createItemTag(Enchanted.id("armor_poppet_whitelist"));
        public static final TagKey<Item> CHALICES = createItemTag(Enchanted.id("chalices"));
        public static final TagKey<Item> CHALKS = createItemTag(Enchanted.id("chalks"));
        public static final TagKey<Item> HAWTHORN_LOGS = createItemTag(Enchanted.id("hawthorn_logs"));
        public static final TagKey<Item> LEAVES = createItemTag(Enchanted.id("leaves"));
        public static final TagKey<Item> LOGS = createItemTag(Enchanted.id("logs"));
        public static final TagKey<Item> PLANKS = createItemTag(Enchanted.id("planks"));
        public static final TagKey<Item> RAW_FOODS = createItemTag(Enchanted.id("raw_foods"));
        public static final TagKey<Item> ROWAN_LOGS = createItemTag(Enchanted.id("rowan_logs"));
        public static final TagKey<Item> SAPLINGS = createItemTag(Enchanted.id("saplings"));
        public static final TagKey<Item> SLABS = createItemTag(Enchanted.id("slabs"));
        public static final TagKey<Item> STAIRS = createItemTag(Enchanted.id("stairs"));
        public static final TagKey<Item> SWORDS = createItemTag(Enchanted.id("swords"));
        public static final TagKey<Item> TOOLS = createItemTag(Enchanted.id("tools"));
        public static final TagKey<Item> TOOL_POPPET_BLACKLIST = createItemTag(Enchanted.id("tool_poppet_blacklist"));
        public static final TagKey<Item> TOOL_POPPET_WHITELIST = createItemTag(Enchanted.id("tool_poppet_whitelist"));
        public static final TagKey<Item> WITCH_OVEN_BLACKLIST = createItemTag(Enchanted.id("witch_oven_blacklist"));
        public static final TagKey<Item> WOODEN_SLABS = createItemTag(Enchanted.id("wooden_slabs"));
        public static final TagKey<Item> WOODEN_STAIRS = createItemTag(Enchanted.id("wooden_stairs"));
        public static final TagKey<Item> VOODOO_POPPETS = createItemTag(Enchanted.id("voodoo_poppets"));
    }

    public static class MobEffects {
        public static final TagKey<MobEffect> MISFORTUNE_EFFECTS = createEffectTag(Enchanted.id("misfortune_effects"));
        public static final TagKey<MobEffect> BLIGHT_EFFECTS = createEffectTag(Enchanted.id("blight_effects"));
        public static final TagKey<MobEffect> FERTILITY_CURE_EFFECTS = createEffectTag(Enchanted.id("fertility_cure_effects"));
    }

    public static class Biomes {
        public static final TagKey<Biome> OVERHEATING_BIOMES = createBiomeTag(Enchanted.id("overheating_biomes"));
    }

    public static class EntityTypes {
        public static final TagKey<EntityType<?>> MONSTERS = createEntityTag(Enchanted.id("monsters"));
        public static final TagKey<EntityType<?>> TAGLOCK_BLACKLIST = createEntityTag(Enchanted.id("taglock_blacklist"));
    }



    private static <T> TagKey<T> createTag(ResourceKey<? extends Registry<T>> registry, ResourceLocation name) {
        return TagKey.create(registry, name);
    }

    public static TagKey<Item> createItemTag(ResourceLocation name) {
        return createTag(Registries.ITEM, name);
    }

    public static TagKey<Block> createBlockTag(ResourceLocation name) {
        return createTag(Registries.BLOCK, name);
    }

    public static TagKey<EntityType<?>> createEntityTag(ResourceLocation name) {
        return createTag(Registries.ENTITY_TYPE, name);
    }

    public static TagKey<Biome> createBiomeTag(ResourceLocation name) {
        return createTag(Registries.BIOME, name);
    }

    public static TagKey<MobEffect> createEffectTag(ResourceLocation name) {
        return createTag(Registries.MOB_EFFECT, name);
    }

}
