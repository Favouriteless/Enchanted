package net.favouriteless.enchanted.neoforge.datagen.providers;

import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.enchanted.common.init.EBlocks;
import net.favouriteless.enchanted.common.init.EEntityTypes;
import net.favouriteless.enchanted.common.init.EItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.LanguageProvider;

import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Supplier;

public class ELanguageProvider extends LanguageProvider {

    public static final Set<String> lowerCaseWords = Set.of("of", "the", "and");

    private final Set<String> usedKeys = new HashSet<>();

    public ELanguageProvider(PackOutput output) {
        super(output, Enchanted.MOD_ID, "en_us");
    }


    @Override
    protected void addTranslations() {
        add(Enchanted.translationKey("tab", "main"), "Enchanted");

        add(Enchanted.translationKey("item", "bound_waystone.not_bound"), "Not bound");

        add("death.attack.enchanted.sacrifice", "%1$s was sacrificed.");
        add("death.attack.enchanted.sound", "%1$s had their eardrums pierced.");
        add("death.attack.enchanted.voodoo", "%1$s died mysteriously.");

        addBlock(EBlocks.CHALICE_FILLED, "Chalice (Filled)");
        addBlock(EBlocks.FUME_FUNNEL_FILTERED, "Fume Funnel (Filtered)");
        addBlock(EBlocks.WITCH_CAULDRON, "Witch's Cauldron");
        addBlock(EBlocks.WITCH_OVEN, "Witch's Oven");

        addItem(EItems.EXHALE_OF_THE_HORNED_ONE, "Horned One's Exhale");
        addItem(EItems.ATTUNED_STONE_CHARGED, "Attuned Stone (Charged)");

        addEntityType(EEntityTypes.FAMILIAR_CAT, "Cat (Familiar)");

        addJei(EItems.CHALICE_FILLED, "Right click on a chalice using redstone soup");
        addJei(EItems.WITCH_CAULDRON, "Right click on a cauldron using anointing Paste");
        addJei(EItems.FUME_FUNNEL_FILTERED, "Right click on a fume funnel using a fume filter");
        addJeiCategory("circle_magic", "Circle Magic");
        addJeiCategory("kettle", "Kettle");
        addJeiCategory("witch_cauldron", "Witch's Cauldron");
        addJeiCategory("mutandis", "Mutandis");
        addJeiCategory("mutandis_extremis", "Mutandis Extremis");
        addJei("mutandis.description", "Use mutandis to transmute plants.");
        addJei("arthana.bat", "Obtained by killing a bat with an arthana. Looting increases the odds.");
        addJei("arthana.wolf", "Obtained by killing a wolf with an arthana. Looting increases the odds.");
        addJei("arthana.creeper", "Obtained by killing a creeper with an arthana. Looting increases the odds.");

        addContainer(EBlocks.ALTAR, "Altar");
        addContainer(EBlocks.DISTILLERY, "Distillery");
        addContainer(EBlocks.KETTLE, "Kettle");
        addContainer(EBlocks.WITCH_CAULDRON, "Witch's Cauldron");
        addContainer(EBlocks.POPPET_SHELF, "Poppet Shelf");
        addContainer(EBlocks.SPINNING_WHEEL, "Spinning Wheel");
        addContainer(EBlocks.WITCH_OVEN, "Witch's Oven");

        addRite("bind_familiar", "Rite of Binding");
        addRite("bind_talisman", "Rite of Binding");
        addRite("bind_talisman_charged", "Rite of Binding");
        addRite("blight", "Curse of Blight");
        addRite("broiling", "Rite of Broiling");
        addRite("broiling_charged", "Rite of Broiling");
        addRite("charging_stone", "Rite of Charging");
        addRite("curse_misfortune", "Curse of Misfortune");
        addRite("curse_overheating", "Curse of Overheating");
        addRite("curse_sinking", "Curse of Sinking");
        addRite("duplicate_waystone", "Rite of Binding");
        addRite("duplicate_waystone_charged", "Rite of Binding");
        addRite("fertility", "Rite of Fertility");
        addRite("fertility_charged", "Rite of Fertility");
        addRite("forest", "Rite of Forest");
        addRite("imprisonment", "Rite of Imprisonment");
        addRite("infusion_broom", "Rite of Infusion");
        addRite("protection", "Rite of Protection");
        addRite("protection_waystone", "Rite of Protection");
        addRite("protection_large", "Rite of Protection");
        addRite("protection_large_waystone", "Rite of Protection");
        addRite("protection_temporary", "Rite of Protection");
        addRite("protection_temporary_blooded", "Rite of Protection");
        addRite("protection_temporary_waystone", "Rite of Protection");
        addRite("remove_misfortune", "Rite of Curse Removal");
        addRite("remove_overheating", "Rite of Curse Removal");
        addRite("remove_sinking", "Rite of Curse Removal");
        addRite("sanctity", "Rite of Sanctity");
        addRite("sky_wrath", "Rite of Sky's Wrath");
        addRite("sky_wrath_charged", "Rite of Sky's Wrath");
        addRite("sky_wrath_blooded", "Rite of Sky's Wrath");
        addRite("sky_wrath_blooded_charged", "Rite of Sky's Wrath");
        addRite("sky_wrath_waystone", "Rite of Sky's Wrath");
        addRite("sky_wrath_waystone_charged", "Rite of Sky's Wrath");
        addRite("summon_entity", "Rite of Summoning");
        addRite("summon_familiar", "Rite of Summoning");
        addRite("total_eclipse", "Rite of Total Eclipse");
        addRite("total_eclipse_charged", "Rite of Total Eclipse");
        addRite("transpose_caster_blooded", "Rite of Transposition");
        addRite("transpose_caster_waystone", "Rite of Transposition");
        addRite("transpose_iron", "Rite of Transposition");
        addRite("waystone", "Rite of Binding");
        addRite("waystone_charged", "Rite of Binding");
        addRite("waystone_blooded", "Rite of Binding");
        addRite("waystone_blooded_charged", "Rite of Binding");

        addBookTitle("art_of_witchcraft", "Art of Witchcraft");
        addBookSubtitle("art_of_witchcraft", "A guide to magic");
        addBookHeader("fume_extraction", "Fume Extraction");
        addBookHeader("distillation", "Distillation");
        addBookHeader("witch_cauldron_recipe", "Cauldron Recipe");
        addBookHeader("kettle_recipe", "Kettle Recipe");

        addTooltip("byproduct_recipe", "Byproduct Recipe");
        addTooltip("altar_power", "%1$s Altar Power");
        addTooltip("disabled_totems", "Disabled (Enchanted: Witchcraft)");

        autoGenerateAll(); // All keys which weren't included are attempted to be automatically generated.
    }

    protected void addContainer(Supplier<? extends Block> block, String value) {
        add(Enchanted.translationKey("container", BuiltInRegistries.BLOCK.getKey(block.get()).getPath()), value);
    }

    protected void addKey(String key, String value) {
        add(Enchanted.translationKey("key", key), value);
    }

    protected void addRite(String key, String value) {
        add(Enchanted.translationKey("rite", key), value);
    }

    protected void addBookTitle(String key, String value) {
        add(Enchanted.translationKey("book.title", key), value);
    }

    protected void addBookSubtitle(String key, String value) {
        add(Enchanted.translationKey("book.subtitle", key), value);
    }

    protected void addBookHeader(String key, String value) {
        add(Enchanted.translationKey("book", "header." + key), value);
    }

    protected void addJeiCategory(String key, String value) {
        add(Enchanted.translationKey("jei.category", key), value);
    }

    protected void addJei(String key, String value) {
        add(Enchanted.translationKey("jei", key), value);
    }

    protected void addJei(Supplier<? extends Item> item, String value) {
        add(Enchanted.translationKey("jei", BuiltInRegistries.ITEM.getKey(item.get()).getPath()), value);
    }

    protected void addTooltip(String suffix, String value) {
        add(Enchanted.translationKey("tooltip", suffix), value);
    }

    protected void autoGenerateAll() {
        autoGenerateBlocks();
        autoGenerateItems();
        autoGenerateEntities();
        autoGenerateMobEffects();
    }

    protected void autoGenerateBlocks() {
        for(Entry<ResourceKey<Block>, Block> entry : BuiltInRegistries.BLOCK.entrySet()) {
            if(entry.getKey().location().getNamespace().equals(Enchanted.MOD_ID)) {
                String id = entry.getValue().getDescriptionId();
                if(!usedKeys.contains(id))
                    add(id, getAutoName(entry.getKey().location().getPath()));
            }
        }
    }

    protected void autoGenerateItems() {
        for(Entry<ResourceKey<Item>, Item> entry : BuiltInRegistries.ITEM.entrySet()) {
            if(entry.getKey().location().getNamespace().equals(Enchanted.MOD_ID)) {
                String id = entry.getValue().getDescriptionId();
                if(!usedKeys.contains(id))
                    add(id, getAutoName(entry.getKey().location().getPath()));
            }
        }
    }

    protected void autoGenerateMobEffects() {
        for(Entry<ResourceKey<MobEffect>, MobEffect> entry : BuiltInRegistries.MOB_EFFECT.entrySet()) {
            if(entry.getKey().location().getNamespace().equals(Enchanted.MOD_ID)) {
                String id = entry.getValue().getDescriptionId();
                if(!usedKeys.contains(id))
                    add(id, getAutoName(entry.getKey().location().getPath()));
            }
        }
    }

    protected void autoGenerateEntities() {
        for(Entry<ResourceKey<EntityType<?>>, EntityType<?>> entry : BuiltInRegistries.ENTITY_TYPE.entrySet()) {
            if(entry.getKey().location().getNamespace().equals(Enchanted.MOD_ID)) {
                String id = entry.getValue().getDescriptionId();
                if(!usedKeys.contains(id))
                    add(id, getAutoName(entry.getKey().location().getPath()));
            }
        }
    }

    public String getAutoName(String path) {
        String[] words = path.split("_");

        StringBuilder builder = new StringBuilder();
        for(String word : words) {
            if(!builder.isEmpty())
                builder.append(" ");
            if(!lowerCaseWords.contains(word))
                builder.append(word.substring(0, 1).toUpperCase()).append(word.substring(1));
            else
                builder.append(word);
        }

        return builder.toString();
    }

    @Override
    public void add(String key, String value) {
        usedKeys.add(key);
        super.add(key, value);
    }

}
