package net.favouriteless.enchanted.datagen.providers;

import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.enchanted.common.init.registry.EBlocks;
import net.favouriteless.enchanted.common.init.registry.EEntityTypes;
import net.favouriteless.enchanted.common.init.registry.EItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Supplier;

public class LanguageProvider extends net.minecraftforge.common.data.LanguageProvider {

    public static final Set<String> lowerCaseWords = Set.of("of", "the", "and");

    private final Set<String> usedKeys = new HashSet<>();

    public LanguageProvider(PackOutput output) {
        super(output, Enchanted.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        add(Enchanted.translationKey("tab", "main"), "Enchanted");

        add("death.attack.enchanted.sacrifice", "%1$s was sacrificed.");
        add("death.attack.enchanted.sound", "%1$s had their eardrums pierced.");
        add("death.attack.enchanted.voodoo", "%1$s died mysteriously.");

        addBlock(EBlocks.CHALICE_FILLED, "Chalice (Filled)");
        addBlock(EBlocks.FUME_FUNNEL_FILTERED, "Fume Funnel (Filtered)");
        addBlock(EBlocks.WITCH_CAULDRON, "Witch's Cauldron");
        addBlock(EBlocks.WITCH_OVEN, "Witch's Oven");

        addEntityType(EEntityTypes.FAMILIAR_CAT, "Cat (Familiar)");

        addJei(EItems.CHALICE_FILLED, "Right click on a Chalice using redstone soup.");
        addJei("circle_magic", "Circle Magic");
        addJei("kettle", "Kettle");
        addJei("witch_cauldron", "Witch's Cauldron");
        addJei("mutandis", "Mutandis");
        addJei("mutandis_extremis", "Mutandis Extremis");
        addJei("mutandis.description", "Use mutandis to transmute plants.");

        addKey("categories.broomstick", "Broomsticks");
        addKey("broom_aim_down", "Point Down");
        addKey("broom_aim_up", "Point Up");
        addKey("broom_forward", "Fly Forwards");
        addKey("broom_backward", "Fly Backwards");
        addKey("broom_turn_left", "Turn Left");
        addKey("broom_turn_right", "Turn Right");
        addKey("broom_up", "Fly Up");
        addKey("broom_down", "Fly Down");

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

        add("item.enchanted.bound_waystone.not_bound", "Not bound");

        autoGenerateAll(); // All keys which weren't included are attempted to be automatically generated.
    }

    protected void addContainer(Supplier<? extends Block> block, String value) {
        add(Enchanted.translationKey("container", ForgeRegistries.BLOCKS.getKey(block.get()).getPath()), value);
    }

    protected void addKey(String key, String value) {
        add(Enchanted.translationKey("key", key), value);
    }

    protected void addJei(String key, String value) {
        add(Enchanted.translationKey("jei", key), value);
    }

    protected void addJei(Supplier<? extends Item> item, String value) {
        add(Enchanted.translationKey("jei", ForgeRegistries.ITEMS.getKey(item.get()).getPath()), value);
    }

    protected void addRite(String key, String value) {
        add(Enchanted.translationKey("rite", key), value);
    }

    protected void autoGenerateAll() {
        autoGenerateBlocks();
        autoGenerateItems();
        autoGenerateEntities();
        autoGenerateMobEffects();
    }

    protected void autoGenerateBlocks() {
        for(Entry<ResourceKey<Block>, Block> entry : ForgeRegistries.BLOCKS.getEntries()) {
            if(entry.getKey().location().getNamespace().equals(Enchanted.MOD_ID)) {
                String id = entry.getValue().getDescriptionId();
                if(!usedKeys.contains(id))
                    add(id, getAutoName(entry.getKey().location().getPath()));
            }
        }
    }

    protected void autoGenerateItems() {
        for(Entry<ResourceKey<Item>, Item> entry : ForgeRegistries.ITEMS.getEntries()) {
            if(entry.getKey().location().getNamespace().equals(Enchanted.MOD_ID)) {
                String id = entry.getValue().getDescriptionId();
                if(!usedKeys.contains(id))
                    add(id, getAutoName(entry.getKey().location().getPath()));
            }
        }
    }

    protected void autoGenerateMobEffects() {
        for(Entry<ResourceKey<MobEffect>, MobEffect> entry : ForgeRegistries.MOB_EFFECTS.getEntries()) {
            if(entry.getKey().location().getNamespace().equals(Enchanted.MOD_ID)) {
                String id = entry.getValue().getDescriptionId();
                if(!usedKeys.contains(id))
                    add(id, getAutoName(entry.getKey().location().getPath()));
            }
        }
    }

    protected void autoGenerateEntities() {
        for(Entry<ResourceKey<EntityType<?>>, EntityType<?>> entry : ForgeRegistries.ENTITY_TYPES.getEntries()) {
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
