package net.favouriteless.enchanted.neoforge.datagen.providers.modopedia;

import com.google.gson.JsonElement;
import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.enchanted.common.init.EItems;
import net.favouriteless.modopedia.api.book.Category;
import net.favouriteless.modopedia.api.datagen.builders.CategoryBuilder;
import net.favouriteless.modopedia.api.datagen.providers.ContentSetProvider;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class EContentSetProvider extends ContentSetProvider {

    public EContentSetProvider(CompletableFuture<Provider> registries, PackOutput output) {
        super(Enchanted.MOD_ID, "art_of_witchcraft", "en_us", registries, output);
    }

    @Override
    public void buildEntries(BiConsumer<String, JsonElement> output) {

    }

    public void buildGettingStartedEntries(BiConsumer<String, JsonElement> output) {

    }

    @Override
    public void buildCategories(BiConsumer<String, Category> output) {
        CategoryBuilder.of("getting_started", "Getting Started")
                .landingText("""
                        To get started in witchcraft, first you must know about the basic tools and equipment used by witches.
                        
                        This chapter tells you everything you need to know about getting started as a Witch."""
                )
                .icon(EItems.ARTHANA.get().getDefaultInstance())
                .displayOnFrontPage(true)
                .children("getting_started/altars")
                .entries(
                        "getting_started/arthana", "getting_started/earmuffs", "getting_started/broom",
                        "getting_started/taglocks", "getting_started/bone_needle", "getting_started/attuned_stone"
                )
                .build(output);

        CategoryBuilder.of("getting_started/altars", "Altars")
                .landingText("""
                        An altar acts as a source of magical energy for chalk circles and most of a witch's tools.
                        
                        The amount of natural energy around an altar will determine how effective it is. Generally, a variety of plants is best."""
                )
                .icon(EItems.ALTAR.get().getDefaultInstance())
                .displayOnFrontPage(false)
                .entries(
                        "getting_started/altars/altar_construction", "getting_started/altars/torch_upgrades",
                        "getting_started/altars/skull_upgrades", "getting_started/altars/chalice_upgrades"
                )
                .build(output);

        CategoryBuilder.of("extraction", "Material Processing")
                .landingText("""
                        Many resources used in witchcraft can only be obtained by extracting them from other materials.
                        
                        This involves two processes; fume collection and distillation. Both of these are detailed in this chapter."""
                )
                .icon(EItems.WITCH_OVEN.get().getDefaultInstance())
                .displayOnFrontPage(true)
                .entries(
                        "extraction/witch_oven", "extraction/distillery", "extraction/clay_jar",
                        "extraction/breath_of_the_goddess", "extraction/demonic_blood", "extraction/diamond_vapour",
                        "extraction/ender_dew", "extraction/exhale_of_the_horned_one", "extraction/foul_fume",
                        "extraction/glowstone_dust", "extraction/gypsum", "extraction/hint_of_rebirth",
                        "extraction/odour_of_purity", "extraction/oil_of_vitriol", "extraction/reek_of_misfortune",
                        "extraction/refined_evil", "extraction/slime_ball", "extraction/tear_of_the_goddess",
                        "extraction/whiff_of_magic"
                )
                .build(output);

        CategoryBuilder.of("herbology", "Herbology")
                .landingText("""
                        Witchcraft often requires using various plants, some of which are common while others require mutations.
                        
                        This chapter aims to tell you how to obtain these plants and what they do."""
                )
                .icon(EItems.WOLFSBANE_FLOWER.get().getDefaultInstance())
                .displayOnFrontPage(true)
                .children("herbology/mutated_plants")
                .entries(
                        "herbology/belladonna", "herbology/water_artichoke", "herbology/mandrake", "herbology/snowbell",
                        "herbology/wolfsbane", "herbology/garlic"
                )
                .build(output);

        CategoryBuilder.of("herbology/mutated_plants", "Mutated Plants")
                .landingText("""
                        Some plants require mutations to be obtained, either by using Mutandis or Mutandis Extremis.
                        
                        Plants of this nature are covered in this chapter."""
                )
                .icon(EItems.MUTANDIS.get().getDefaultInstance())
                .displayOnFrontPage(false)
                .entries(
                        "herbology/mutandis/mutandis", "herbology/mutandis/mutandis_extremis", "herbology/mutandis/rowan",
                        "herbology/mutandis/hawthorn", "herbology/mutandis/alder", "herbology/mutandis/spanish_moss",
                        "herbology/mutandis/glint_weed", "herbology/mutandis/ember_moss"
                )
                .build(output);

        CategoryBuilder.of("brewing", "Brewing")
                .landingText("""
                        One of the most essential skills a witch can possess is the abilities and knowledge to create brews, potions and decoctions.
                        
                        The methods of brewing and various common recipes can be found in this chapter."""
                )
                .icon(EItems.REDSTONE_SOUP.get().getDefaultInstance())
                .displayOnFrontPage(true)
                .entries(
                        "brewing/brewing", "herbology/mutandis", "herbology/mutandis_extremis", "brewing/golden_chalk",
                        "brewing/nether_chalk", "brewing/otherwhere_chalk", "brewing/drop_of_luck",
                        "brewing/redstone_soup", "brewing/flying_ointment", "brewing/happenstance_oil",
                        "brewing/mystic_unguent", "brewing/spirit_of_otherwhere", "brewing/soul_of_the_world",
                        "brewing/brew_of_love", "brewing/brew_of_sprouting", "brewing/brew_of_the_depths",
                        "brewing/brew_of_the_grotesque"
                )
                .build(output);
    }

}
