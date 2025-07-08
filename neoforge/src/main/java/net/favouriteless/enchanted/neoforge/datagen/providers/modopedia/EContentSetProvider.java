package net.favouriteless.enchanted.neoforge.datagen.providers.modopedia;

import net.favouriteless.enchanted.api.datagen.builders.modopedia.templates.page.ByproductPageBuilder;
import net.favouriteless.enchanted.api.datagen.builders.modopedia.templates.page.DistilleryPageBuilder;
import net.favouriteless.enchanted.api.datagen.builders.modopedia.templates.page.DoubleByproductPageBuilder;
import net.favouriteless.enchanted.api.datagen.builders.modopedia.templates.recipe.KettleRecipeBuilder;
import net.favouriteless.enchanted.api.datagen.builders.modopedia.templates.recipe.WitchCauldronRecipeBuilder;
import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.enchanted.common.blocks.DistilleryBlock;
import net.favouriteless.enchanted.common.blocks.FumeFunnelBlock;
import net.favouriteless.enchanted.common.blocks.WitchOvenBlock;
import net.favouriteless.enchanted.common.blocks.crops.CropBlockAgeFive;
import net.favouriteless.enchanted.common.init.EBlocks;
import net.favouriteless.enchanted.common.init.EItems;
import net.favouriteless.enchanted.common.init.ETags;
import net.favouriteless.modopedia.Modopedia;
import net.favouriteless.modopedia.api.datagen.BookContentOutput;
import net.favouriteless.modopedia.api.datagen.builders.CategoryBuilder;
import net.favouriteless.modopedia.api.datagen.builders.EntryBuilder;
import net.favouriteless.modopedia.api.datagen.builders.PageComponentBuilder;
import net.favouriteless.modopedia.api.datagen.builders.page_components.components.GalleryBuilder;
import net.favouriteless.modopedia.api.datagen.builders.page_components.components.HeaderBuilder;
import net.favouriteless.modopedia.api.datagen.builders.page_components.components.MultiblockBuilder;
import net.favouriteless.modopedia.api.datagen.builders.page_components.components.SeparatorBuilder;
import net.favouriteless.modopedia.api.datagen.builders.templates.page.*;
import net.favouriteless.modopedia.api.datagen.builders.templates.recipes.CookingRecipeBuilder;
import net.favouriteless.modopedia.api.datagen.builders.templates.recipes.CraftingRecipeBuilder;
import net.favouriteless.modopedia.api.datagen.providers.ContentSetProvider;
import net.favouriteless.modopedia.client.multiblock.DenseMultiblock;
import net.favouriteless.modopedia.client.multiblock.state_matchers.SimpleStateMatcher;
import net.favouriteless.modopedia.client.multiblock.state_matchers.TagStateMatcher;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class EContentSetProvider extends ContentSetProvider {

    public EContentSetProvider(CompletableFuture<Provider> registries, PackOutput output) {
        super(Enchanted.MOD_ID, "art_of_witchcraft", "en_us", registries, output);
    }

    @Override
    public void buildCategories(Provider provider, BookContentOutput output) {
        CategoryBuilder.of("Getting Started")
                .landingText("""
                        To get started in witchcraft, first you must know about the basic tools and equipment used by witches.
                        
                        This chapter tells you everything you need to know about getting started as a Witch.""")
                .icon(EItems.ARTHANA.get().getDefaultInstance())
                .children("getting_started/altars")
                .entries(
                        itemPaths(EItems.ARTHANA.get(), EItems.EARMUFFS.get(), EItems.BROOM.get(), EItems.TAGLOCK.get(),
                                EItems.BONE_NEEDLE.get(), EItems.ATTUNED_STONE.get())
                )
                .build("getting_started", output);

        CategoryBuilder.of("Altars")
                .landingText("""
                        An altar acts as a source of magical energy for chalk circles and most of a witch's tools.
                        
                        The amount of natural energy around an altar will determine how effective it is. Generally, a variety of plants is best.""")
                .icon(EItems.ALTAR.get().getDefaultInstance())
                .displayOnFrontPage(false)
                .entries(blockPath(EBlocks.ALTAR.get()), gettingStartedPath("torch_upgrades"),
                        gettingStartedPath("skull_upgrades"), gettingStartedPath("chalice_upgrades"))
                .build("getting_started/altars", output);

        CategoryBuilder.of("Material Processing")
                .landingText("""
                        Many resources used in witchcraft can only be obtained by extracting them from other materials.
                        
                        This involves two processes; fume collection and distillation. Both of these are detailed in this chapter.""")
                .icon(EItems.WITCH_OVEN.get().getDefaultInstance())
                .entries(merge(
                        blockPaths(EBlocks.WITCH_OVEN.get(), EBlocks.DISTILLERY.get()),
                        itemPaths(EItems.CLAY_JAR.get(), EItems.BREATH_OF_THE_GODDESS.get(), EItems.DEMONIC_BLOOD.get(),
                                EItems.DIAMOND_VAPOUR.get(), EItems.ENDER_DEW.get(), EItems.EXHALE_OF_THE_HORNED_ONE.get(),
                                EItems.FOUL_FUME.get(), EItems.GYPSUM.get(), EItems.HINT_OF_REBIRTH.get(),
                                EItems.ODOUR_OF_PURITY.get(), EItems.OIL_OF_VITRIOL.get(), EItems.REEK_OF_MISFORTUNE.get(),
                                EItems.REFINED_EVIL.get(), EItems.TEAR_OF_THE_GODDESS.get(), EItems.WHIFF_OF_MAGIC.get())
                ))
                .build("extraction", output);

        CategoryBuilder.of("Herbology")
                .landingText("""
                        Witchcraft often requires using various plants, some of which are common while others require mutations.
                        
                        This chapter aims to tell you how to obtain these plants and what they do.""")
                .icon(EItems.WOLFSBANE_FLOWER.get().getDefaultInstance())
                .children("herbology/mutated_plants")
                .entries(
                        blockPaths(EBlocks.BELLADONNA.get(), EBlocks.WATER_ARTICHOKE.get(), EBlocks.MANDRAKE.get(),
                                EBlocks.SNOWBELL.get(), EBlocks.WOLFSBANE.get(), EBlocks.GARLIC.get())
                )
                .build("herbology", output);

        CategoryBuilder.of("Mutated Plants")
                .landingText("""
                        Some plants require mutations to be obtained, either by using Mutandis or Mutandis Extremis.
                        
                        Plants of this nature are covered in this chapter.""")
                .icon(EItems.MUTANDIS.get().getDefaultInstance())
                .displayOnFrontPage(false)
                .entries(merge(
                        itemPaths(EItems.MUTANDIS.get(), EItems.MUTANDIS_EXTREMIS.get()),
                        blockPaths(EBlocks.ROWAN_SAPLING.get(), EBlocks.HAWTHORN_SAPLING.get(), EBlocks.ALDER_SAPLING.get(),
                                EBlocks.SPANISH_MOSS.get(), EBlocks.GLINT_WEED.get(), EBlocks.EMBER_MOSS.get())
                ))
                .build("herbology/mutated_plants", output);

        CategoryBuilder.of("Brewing")
                .landingText("""
                        One of the most essential skills a witch can possess is the abilities and knowledge to create brews, potions and decoctions.
                        
                        The methods of brewing and various common recipes can be found in this chapter.""")
                .icon(EItems.REDSTONE_SOUP.get().getDefaultInstance())
                .entries(merge(
                        "brewing/brewing",
                        itemPaths(EItems.MUTANDIS.get(), EItems.MUTANDIS_EXTREMIS.get(), EItems.GOLDEN_CHALK.get(),
                                EItems.NETHER_CHALK.get(), EItems.OTHERWHERE_CHALK.get(), EItems.DROP_OF_LUCK.get(),
                                EItems.REDSTONE_SOUP.get(), EItems.FLYING_OINTMENT.get(), EItems.HAPPENSTANCE_OIL.get(),
                                EItems.MYSTIC_UNGUENT.get(), EItems.SPIRIT_OF_OTHERWHERE.get(), EItems.SOUL_OF_THE_WORLD.get(),
                                EItems.BREW_OF_LOVE.get(), EItems.BREW_OF_SPROUTING.get(), EItems.BREW_OF_THE_DEPTHS.get(),
                                EItems.BREW_OF_THE_GROTESQUE.get())
                ))
                .build("brewing", output);

        CategoryBuilder.of("Circle Magic")
                .icon(EItems.CIRCLE_TALISMAN.get().getDefaultInstance())
                .landingText("""
                        Circle magic is the practice of using chalk circles or other materials in combination with foci items to collect magical energy from the environment and achieve the intended affect.
                        
                        The methods of circle magic and known rites are detailed in this chapter.
                        """)
                .children("circle_magic/tutorial", "circle_magic/binding", "circle_magic/charging",
                        "circle_magic/creature", "circle_magic/curses", "circle_magic/infusion",
                        "circle_magic/transposition", "circle_magic/world")
                .build("circle_magic", output);

        CategoryBuilder.of("Performing Rites")
                .icon(EItems.CIRCLE_TALISMAN.get().getDefaultInstance())
                .displayOnFrontPage(false)
                .landingText("""
                        Performing a circle magic rite can be a complex and difficult process, requiring a mixture of chalk circles, items, and sometimes even a sacrifice.""")
                .entries("circle_magic/tutorial/performing_rites", "golden_chalk", "ritual_chalk", "nether_chalk", "otherwhere_chalk")
                .build("circle_magic/tutorial", output);
    }

    @Override
    public void buildEntries(Provider provider, BookContentOutput output) {
        buildItemEntries(output);
        buildBlockEntries(output);

        buildGettingStartedEntries(output);
        buildBrewingEntries(output);
        buildCircleMagicEntries(output);
    }

    public void buildItemEntries(BookContentOutput output) {
        buildBrewItemEntries(output);
        buildExtractionItemEntries(output);

        craftingEntry(output, "Ritual Chalk", formatItems("""
                Ritual chalk is the most basic of the four types of chalk, used for drawing basic circles in rites.
                
                There are no known special effects of ritual chalk, it is merely chalk infused with $(b)$(el:%s)Tear of the Goddess$().""", EItems.TEAR_OF_THE_GODDESS.get()),
                EItems.RITUAL_CHALK.get());

        cauldronEntry(output, "Golden Chalk", EItems.GOLDEN_CHALK.get(), """
                Chalk is vital for performing circle magic and among chalks golden chalk is the most important.
                
                The $(b)$(el:circle_magic/tutorial/performing_rites)heart glyph$() for $(b)$(cl:circle_magic)circle magic$() is drawn using golden chalk.""");

        cauldronEntry(output, "Infernal Chalk", EItems.NETHER_CHALK.get(), """
                Infusing chalk with blaze powder binds it to the nether and enables it to better conduct heat.
                
                Infernal chalk is used for many rites involving the nether, demonic beings or fire.""");

        cauldronEntry(output, "Otherwhere chalk", EItems.OTHERWHERE_CHALK.get(), """
                Materials from the end can be infused into chalk to create a rich, purple chalk with special properties.
                
                Typically, otherwhere chalk is used in circle magic rites involving teleportation, transposition, relocation or the end.""");

        craftingEntry(output, "Brooms", """
                                A broom can be used to sweep chalk away quickly, without having to spend time erasing it.
                                
                                Conveniently, a broom can also make for an excellent method of transportation with some preparation.""",
                EItems.BROOM.get());

        craftingEntry(output, "Earmuffs", """
                                When dealing with Mandrakes, Banshees and other loud creatures, protection for your ears is essential.
                                
                                $(b)Earmuffs$() can dampen deafening sounds, rendering them bearable.""",
                EItems.EARMUFFS.get());

        craftingEntry(output, "Taglocks", """
                                Sometimes, you may need to represent another person to perform magic. This can be achieved using a $(b)taglock kit$().
                                
                                By using a taglock kit on a Player or their bed, you can collect a sample to use in magic.""",
                EItems.TAGLOCK.get(), EItems.TAGLOCK_FILLED.get(), EItems.TAGLOCK.get());

        EntryBuilder.of("Arthana")
                .icon(EItems.ARTHANA.get().getDefaultInstance())
                .assignedItems(EItems.ARTHANA.get(), EItems.TONGUE_OF_DOG.get(), EItems.WOOL_OF_BAT.get(), EItems.CREEPER_HEART.get())
                .page(
                        HeaderedTextBuilder.of("Arthana", "The $(b)arthana$() is a ritual knife used for sacrifice. When used to kill certain mobs, they can drop rare materials."),
                        CraftingRecipeBuilder.of(itemId(EItems.ARTHANA.get())).y(75)
                )
                .page(
                        GalleryBuilder.of(
                                EntityPageBuilder.of(EntityType.BAT, "$(b)Drops:$()\nWool of Bat").scale(0.75F),
                                EntityPageBuilder.of(EntityType.WOLF, "$(b)Drops:$()\nTongue of Dog").scale(0.65F),
                                EntityPageBuilder.of(EntityType.CREEPER, "$(b)Drops:$()\nCreeper Heart"),
                                EntityPageBuilder.of(EntityType.SKELETON, "$(b)Drops:$()\nSkeleton Skull")
                        )
                )
                .build(itemPath(EItems.ARTHANA.get()), output);

        craftingEntry(output, "Attuned Stones", """
                                An attuned stone is a diamond which has been infused with magical energy.
                                
                                It can be charged to be used as a portable container for $(b)$(el:getting_started/altars/altar_construction)altar power$(), allowing the user to cast circle magic without an altar being present, and is also used in the creation of a variety of magical tools.""",
                EItems.ATTUNED_STONE.get());

        craftingEntry(output, "Bone Needles", formatItems("""
                                Needles are an important part of many tools used for Witchcraft, most notably $(el:%s)$(b)taglock kits$() and $(b)poppets$().
                                
                                A simple needle can be fashioned by whittling a bone.""", EItems.TAGLOCK.get()),
                EItems.BONE_NEEDLE.get());

        cauldronEntry(output, "Mutandis", EItems.MUTANDIS.get(), """
                                Mutandis is used to mutate plants into other species you could not normally obtain.
                                
                                Using this substance on small plants such as grass, saplings, and flowers will mutate them into a different plant."""
        );

        cauldronEntry(output, "Mutandis Extremis", EItems.MUTANDIS_EXTREMIS.get(), formatItems("""
                                Mutandis Extremis, an enhanced form of $(el:%s)$(b)mutandis$(), is able to mutate multi-stage plants such as cactus, sugar cane and wheat as well as everything mutandis can.
                                
                                It can also be used to create $(b)Blood Poppies$().""", EItems.MUTANDIS.get())
        );

        EntryBuilder.of("Clay Jars")
                .icon(EItems.CLAY_JAR.get().getDefaultInstance())
                .assignedItems(EItems.CLAY_JAR.get(), EItems.SOFT_CLAY_JAR.get())
                .page(
                        HeaderedTextBuilder.of("Clay Jars", """
                                A container to hold materials collected during fume extraction or distillation is essential for witches. A simple clay jar works well for this purpose.""")
                )
                .page(
                        HeaderBuilder.of(Modopedia.translation("template", "recipe")),
                        SeparatorBuilder.of().y(10),
                        CookingRecipeBuilder.of(itemId(EItems.CLAY_JAR.get())).y(30),
                        CraftingRecipeBuilder.of(itemId(EItems.SOFT_CLAY_JAR.get())).y(70)
                )
                .build(itemPath(EItems.CLAY_JAR.get()), output);


    }

    public void buildBrewItemEntries(BookContentOutput output) {
        kettleEntry(output, "Brew of Love", EItems.BREW_OF_LOVE.get(), "Charming or love potions are one of the most widely sought types of brew.\n\nWhen thrown, the vapour produced will cause nearby animals to become infatuated.");
        kettleEntry(output, "Brew of Sprouting", EItems.BREW_OF_SPROUTING.get(), "Being able to slow your opponents in combat can be the difference between life and death.\n\nThe brew of sprouting can be used to ensnare mobs and people with roots.\n\n$(b)This feature is coming soon.");
        kettleEntry(output, "Brew of the Depths", EItems.BREW_OF_THE_DEPTHS.get(), "The ability to breathe underwater has fascinated people for millennia. Fortunately, witches have a solution for this.\n\nThe brew of depths grants the ability to breathe underwater for an extended time when ingested.");
        kettleEntry(output, "Brew of the Grotesque", EItems.BREW_OF_THE_GROTESQUE.get(), "This brew makes the drinker unrecognisably grotesque, tricking nearby mobs into believing the drinker is one of their own.\n\nAdditionally, brew of the grotesque is used as a base in most curses.");
        kettleEntry(output, "Flying Ointment", EItems.FLYING_OINTMENT.get(), "Flying ointment is a brew which seems to defy gravity; anything imbued with it will gain the property of flight, including people who ingest it.\n\nMost notably, it can be used to $(b)infuse broomsticks$()");
        kettleEntry(output, "Happenstance Oil", EItems.HAPPENSTANCE_OIL.get(), "Clairvoyance is an indispensable tool for a witch in need of information.\n\nHappenstance oil is a key component of crystal balls, and while consuming it is not recommended, it can improve your vision.");
        kettleEntry(output, "Mystic Unguent", EItems.MYSTIC_UNGUENT.get(), "Mystic Unguent is a curious concoction which allows a witch to give physical form to thoughts.\n\nIt's main use is in the production of mystic branches, consuming this brew is not recommended.");
        kettleEntry(output, "Redstone Soup", EItems.REDSTONE_SOUP.get(), "Redstone Soup primarily acts as a base for other infusions and brews, it has very few reported effects outside of this.\n\nIngesting redstone soup can increase your health for a short time.");
        kettleEntry(output, "Soul of the World", EItems.SOUL_OF_THE_WORLD.get(), formatItems("Soul of the World is a derivation of $(b)$(el:%s)redstone soup$() imbued with natural energy. It can be used to infuse energy into a person to grant them special abilities.\n\nThis brew is extremely toxic and should not be ingested.", EItems.REDSTONE_SOUP.get()));
        kettleEntry(output, "Spirit of Otherwhere", EItems.SPIRIT_OF_OTHERWHERE.get(), formatItems("Similar to $(b)$(el:%s)soul of the world$(), spirit of otherwhere is a derivation of $(b)$(el:%s)redstone soup$() imbued with the same properties as the End and its inhabitants.\n\nThis infusion is extremely toxic and should not be ingested.", EItems.SOUL_OF_THE_WORLD.get(), EItems.REDSTONE_SOUP.get()));

        cauldronEntry(output, "Drop of Luck", EItems.DROP_OF_LUCK.get(), formatItems("Liquid luck, or drop of luck, is a potion which enhances the luck of anybody who drinks it and can be used in rites or the creation of magical items.\n\nFamously, it's a core ingredient in $(b)$(el:%s)Redstone Soup$().", EItems.REDSTONE_SOUP.get()));
    }

    public void buildExtractionItemEntries(BookContentOutput output) {
        galleryEntry(output, "Breath of the Goddess", EItems.BREATH_OF_THE_GODDESS.get(), """
                Due to it's silvery appearance, birch is sometimes called "White Lady Of The Woods" and is associated with the goddess Brigid.
                
                The smoke produced by burning birch has healing properties.""",
                byproductComponents("byproduct/breath_of_the_goddess_birch_sapling")
        );

        galleryEntry(output, "Demonic Blood", EItems.DEMONIC_BLOOD.get(), """
                        The blood of a demon has many uses in witchcraft. While usually obtained from demons, it can also be refined from several other materials.
                        
                        It's main uses are in infusions and curses.""",
                distillingComponents("distilling/diamond_vapour_blaze_rod")
        );

        galleryEntry(output, "Diamond Vapour", EItems.DIAMOND_VAPOUR.get(), """
                        Diamonds, by using oil of vitriol, can be dissolved and then evaporated to form a powerful refining agent.
                        
                        Diamond vapour is required to distill some other materials.""",
                distillingComponents("distilling/diamond_oil_of_vitriol")
        );

        galleryEntry(output, "Ender Dew", EItems.ENDER_DEW.get(), """
                        Ender pearls, which allow people to teleport, can be distilled into a purer form called Ender Dew.
                        
                        This substance contains the relocation properties of ender pearls in a liquid form, making it useful for brewing.""",
                distillingComponents("distilling/ender_pearl")
        );

        galleryEntry(output, "Horned One's Exhale", EItems.EXHALE_OF_THE_HORNED_ONE.get(), """
                        The Oak King, one of the aspects of the Horned God, symbolises nature, hunting and the cycle of life.
                        
                        The fumes produced by oaks are said to be The Horned God's exhale, carrying some of the properties associated with him.""",
                byproductComponents("byproduct/exhale_of_the_horned_one_oak_sapling")
        );

        galleryEntry(output, "Foul Fume", EItems.FOUL_FUME.get(), """
                        A foul smelling smoke containing sulfur, often smelled when encountering demonic beings or the nether.
                        
                        There are many plants, foods and other sources containing this gas.""",
                merge(
                        byproductComponents("byproduct/foul_fume_jungle_sapling", "byproduct/foul_fume_logs_that_burn", "byproduct/foul_fume_raw_foods"),
                        distillingComponents("distilling/breath_of_the_goddess_lapis_lazuli", "distilling/diamond_vapour_ghast_tear")
                )
        );

        galleryEntry(output, "Gypsum", EItems.GYPSUM.get(), """
                        A soft, translucent mineral salt produced by oxidising sulfides in the presence of quicklime.
                        
                        In addition to being an effective fertiliser, it is commonly used as a base for ritual chalks.""",
                distillingComponents("distilling/foul_fume_quicklime")
        );

        galleryEntry(output, "Hint of Rebirth", EItems.HINT_OF_REBIRTH.get(), """
                        Spruce trees are associated with the birth of the divine child. As such, they are a symbol of rebirth, protection, resilience and endurance.
                        
                        Regardless, they make good firewood.""",
                byproductComponents("byproduct/hint_of_rebirth_spruce_sapling")
        );

        galleryEntry(output, "Odour of Purity", EItems.ODOUR_OF_PURITY.get(), """
                        Hawthorn can be used to collect a powerful purifying agent called Odour of Purity, which is used in the creation of most pure substances.
                        
                        This tree is sacred to the Goddesses Aine and Brigid.""",
                merge(
                        byproductComponents("byproduct/odour_of_purity_hawthorn_sapling"),
                        distillingComponents("distilling/diamond_oil_of_vitriol", "distilling/diamond_vapour_ghast_tear")
                )
        );

        galleryEntry(output, "Oil of Vitriol", EItems.OIL_OF_VITRIOL.get(), """
                        A clear, slightly yellowed liquid which seems to react to living matter with vitriol, leaving them burned and blackened.
                        
                        Oil of vitriol dissolves most substances it comes into contact with.""",
                distillingComponents("distilling/foul_fume_quicklime")
        );

        galleryEntry(output, "Reek of Misfortune", EItems.REEK_OF_MISFORTUNE.get(), """
                        The sacred alder tree appears to bleed when cut, bringing misfortune to all. It is thought that the tree contains the souls of our ancestors.
                        
                        The fumes produced from burning it can be collected, keeping these effects.""",
                merge(
                        byproductComponents("byproduct/reek_of_misfortune_alder_sapling"),
                        distillingComponents("distilling/diamond_vapour_ghast_tear", "distilling/ender_pearl")
                )
        );

        galleryEntry(output, "Refined Evil", EItems.REFINED_EVIL.get(), """
                        Pure, condensed evil refined using diamond vapour.
                        
                        Refined Evil is primarily used for brewing, but also has some uses in demonology.""",
                distillingComponents("distilling/diamond_vapour_ghast_tear")
        );

        galleryEntry(output, "Tear of the Goddess", EItems.TEAR_OF_THE_GODDESS.get(), """
                        Lapis Lazuli is a gemstone which brings wisdom, truth, loyalty and honour.
                        
                        Combining Lapis with the White Goddess's power amplifies it's effects.""",
                distillingComponents("distilling/breath_of_the_goddess_lapis_lazuli")
        );

        galleryEntry(output, "Whiff of Magic", EItems.WHIFF_OF_MAGIC.get(), """
                Due to it's silvery appearance, birch is sometimes called "White Lady Of The Woods" and is associated with the goddess Brigid.
                
                The smoke produced by burning birch has healing properties.""",
                merge(
                        byproductComponents("byproduct/whiff_of_magic_rowan_sapling"),
                        distillingComponents("distilling/ender_pearl", "distilling/breath_of_the_goddess_lapis_lazuli")
                )
        );
    }

    public void buildBlockEntries(BookContentOutput output) {
        buildHerbologyBlockEntries(output);

        EntryBuilder.of("Creating an Altar")
                .icon(EItems.ALTAR.get().getDefaultInstance())
                .assignedItems(EItems.ALTAR.get())
                .page(
                        HeaderedTextBuilder.of("Creating an Altar", "To construct an altar, a witch must place $(b)six altar blocks$() to form a rectangular platform."),
                        CraftingRecipeBuilder.of(Enchanted.id("altar")).y(65)
                )
                .page(
                        HeaderedTextBuilder.of("Upgrades", """
                                Certain blocks may be placed on top of the altar to multiply its effects.
                                
                                Upgrades have a $(b)type$(), only the best upgrade within a type will be counted."""
                        ),
                        MultiblockBuilder.of()
                                .y(70).height(70)
                                .multiblockId(Enchanted.id("altar"))
                )
                .build(blockPath(EBlocks.ALTAR.get()), output);

        EntryBuilder.of("Distillation")
                .icon(EItems.DISTILLERY.get().getDefaultInstance())
                .assignedItems(EItems.DISTILLERY.get())
                .page(
                        HeaderedTextBuilder.of("Distillation", formatBlocks("""
                                The distillery allows witches to separate materials into their components by utilising differences in boiling and condensing points.
                                
                                Processing consumes $(b)$(el:%s)altar power$(), so the distillery must be placed accordingly.""", EBlocks.ALTAR.get())
                        )
                )
                .page(
                        HeaderBuilder.of(Modopedia.translation("template", "crafting_recipe")),
                        SeparatorBuilder.of().y(10),
                        MultiblockBuilder.of().multiblock(new DenseMultiblock(List.of(List.of("D")), Map.of('D', new SimpleStateMatcher(List.of(EBlocks.DISTILLERY.get().defaultBlockState().setValue(DistilleryBlock.LIT, true))))))
                                .y(10).height(50),
                        CraftingRecipeBuilder.of(itemId(EItems.DISTILLERY.get())).y(70)
                )
                .build(blockPath(EBlocks.DISTILLERY.get()), output);

        EntryBuilder.of("Fume Collection")
                .icon(EItems.WITCH_OVEN.get().getDefaultInstance())
                .assignedItems(EItems.WITCH_OVEN.get(), EItems.FUME_FUNNEL.get(), EItems.FUME_FUNNEL_FILTERED.get(), EItems.FUME_FILTER.get())
                .page(
                        HeaderedTextBuilder.of("Fume Collection", formatItems("""
                                The witch's oven is a modified furnace used to collect byproducts and fumes made from burning materials.
                                
                                Unlike a furnace, this oven cannot process ores. Place a $(b)$(el:%s)clay jar$() into the middle slot to collect fumes.""", EItems.CLAY_JAR.get())
                        )
                )
                .page(
                        HeaderBuilder.of(Modopedia.translation("template", "crafting_recipe")),
                        SeparatorBuilder.of().y(10),
                        MultiblockBuilder.of().multiblock(new DenseMultiblock(List.of(List.of("W")), Map.of('W', new SimpleStateMatcher(List.of(EBlocks.WITCH_OVEN.get().defaultBlockState().setValue(WitchOvenBlock.LIT, true))))))
                                .y(10).height(50),
                        CraftingRecipeBuilder.of(itemId(EItems.WITCH_OVEN.get())).y(70)
                )
                .page(
                        HeaderedTextBuilder.of("Fume Funnels", "Adding fume funnels to the left and right of your witch's oven can drastically improve it's yield."),
                        CraftingRecipeBuilder.of(itemId(EItems.FUME_FUNNEL.get())).y(68)
                )
                .page(
                        MultiblockPageBuilder.of("A third fume funnel can be placed on top of the witch oven as a decorative chimney.")
                                .multiblockId(Enchanted.id("witch_oven"))
                                .offsetY(-10)
                )
                .page(
                        HeaderedTextBuilder.of("Fume Filters", "A fume filter can be added to fume funnels to further increase yield. With two filters, success is guaranteed."),
                        CraftingRecipeBuilder.of(itemId(EItems.FUME_FILTER.get())).y(70)
                )
                .page(BlockPageBuilder.of("$(b)Procurement:$()\nUse a fume filter on a fume funnel.", EBlocks.FUME_FUNNEL_FILTERED.get().defaultBlockState().setValue(FumeFunnelBlock.LIT, true)))
                .build(blockPath(EBlocks.WITCH_OVEN.get()), output);

        EntryBuilder.of("The Kettle")
                .icon(EItems.KETTLE.get().getDefaultInstance())
                .assignedItems(EItems.KETTLE.get())
                .page(
                        HeaderedTextBuilder.of("Kettle", "The kettle, used for creating infusions, is a crucial utensil for any witch."),
                        CraftingRecipeBuilder.of(itemId(EItems.KETTLE.get())).y(65)
                )
                .page(
                        MultiblockBuilder.of()
                                .multiblockId(Enchanted.id("kettle"))
                                .y(20)
                )
                .build(blockPath(EBlocks.KETTLE.get()), output);

        EntryBuilder.of("Witch's Cauldron")
                .icon(EItems.WITCH_CAULDRON.get().getDefaultInstance())
                .assignedItems(EItems.WITCH_CAULDRON.get(), EItems.ANOINTING_PASTE.get())
                .page(
                        HeaderedTextBuilder.of("Witch's Cauldron", "The witch's cauldron is the most important brewing tool at a witch's disposal, enabling you to brew complex potions."),
                        CraftingRecipeBuilder.of(itemId(EItems.ANOINTING_PASTE.get())).y(70)
                )
                .page(
                        BlockPageBuilder.of("$(b)Procurement:$()\nUse anointing paste on a cauldron.", EBlocks.WITCH_CAULDRON.get().defaultBlockState())
                )
                .build(blockPath(EBlocks.WITCH_CAULDRON.get()), output);
    }

    public void buildHerbologyBlockEntries(BookContentOutput output) {
        blockEntry(output, "Belladonna", """
                Atropa bella-donna, commonly known as deadly nightshade, is a poisonous member of the Solanaceae family.
                
                The berries and foliage of this plant are extremely toxic and should be handled with care.""",
                EBlocks.BELLADONNA.get(), procureGrass(), EItems.BELLADONNA_FLOWER.get(), EItems.BELLADONNA_SEEDS.get());

        blockEntry(output, "Garlic", """
                Garlic, scientific name allium sativum, is a dietary staple in many households.
                
                Vampires are said to dislike garlic but its effectiveness as a repellent is questionable.""",
                EBlocks.GARLIC.get(), procureGrass(), EItems.GARLIC.get()
        );

        blockEntry(output, "Mandrakes", formatItems("""
                Mandrakes are a group of. perennial herbaceous plants with long, parsnip shaped roots.
                
                If agitated, mandrakes will scream causing injury or even death. Wearing $(el:%s)$(b)earmuffs$() can protect you, and they sleep at night.""", EItems.EARMUFFS.get()),
                EBlocks.MANDRAKE.get(), procureGrass(), EItems.MANDRAKE_ROOT.get(), EItems.MANDRAKE_SEEDS.get()
        );

        blockEntry(output, "Snowbell", """
                Styrax japonicus, more commonly referred to as Snowbell, is a shrub from the Styracaceae family.
                
                Despite the name, they are native to warm climates in Asia. Its resin is used for purification, dispelling anger or soothing tension.""",
                EBlocks.SNOWBELL.get(), procureGrass(), EItems.ICY_NEEDLE.get(), EItems.SNOWBELL_SEEDS.get()
        );

        blockEntry(output, "Water Artichoke", """
                This subspecies of the common Artichoke, or cynara cardunculus, only grows on still water.
                
                Unlike it's green cousin, it is not considered edible. Consumption of this plant will satiate hunger but empty your stomach.""",
                EBlocks.WATER_ARTICHOKE.get(), procureGrass(), EItems.WATER_ARTICHOKE.get(), EItems.WATER_ARTICHOKE_SEEDS.get()
        );

        blockEntry(output, "Wolfsbane", """
                Aconitum, common name of Wolfsbane, is a perennial flower of the Ranunculaceae family.
                
                Its roots contain aconitine, a potent neurotoxin and cardiotoxin. Contrary to popular belief, the name is just a translation from greek.""",
                EBlocks.WOLFSBANE.get(), procureGrass(), EItems.WOLFSBANE_FLOWER.get(), EItems.WOLFSBANE_SEEDS.get()
        );

        blockEntry(output, "Glint Weed", """
                This magical weed emits a glow around it, acting like a torch. Nobody knows what type of plant it actually is.
                
                While it can survive on nearly any surface, if placed on grass, dirt or sand it will spread.""",
                EBlocks.GLINT_WEED.get(), procureMutandis(), EItems.GLINT_WEED.get()
        );

        blockEntry(output, "Ember Moss", """
                Ember moss is a non-vascular plant with a very peculiar and unique defense mechanism where it bursts into flames at the slightest touch or disturbance.""",
                EBlocks.EMBER_MOSS.get(), procureMutandis() + " Harvest with shears.", EItems.EMBER_MOSS.get()
        );

        blockEntry(output, "Spanish Moss", """
                An epiphytic flowering plant, similar to a moss or lichen, found growing on trees in tropical or subtropical climates.
                
                Spanish Moss is often used in the creation of Poppets. Should be harvested with shears to be kept intact.""",
                EBlocks.SPANISH_MOSS.get(), procureMutandis() + " Harvest with shears.", EItems.SPANISH_MOSS.get()
        );

        EntryBuilder.of("Alder Trees")
                .icon(EItems.ALDER_SAPLING.get().getDefaultInstance())
                .assignedItems(EItems.ALDER_SAPLING.get(), EItems.ALDER_LOG.get(), EItems.STRIPPED_ALDER_LOG.get(),
                        EItems.ALDER_PLANKS.get(), EItems.ALDER_STAIRS.get(), EItems.ALDER_SLAB.get(), EItems.ALDER_FENCE.get(),
                        EItems.ALDER_FENCE_GATE.get(), EItems.ALDER_BUTTON.get(), EItems.ALDER_PRESSURE_PLATE.get())
                .page(
                        HeaderedTextBuilder.of("Alder Trees", """
                                Alder trees, of the Betulaceae family, are deciduous trees thought to bleed when cut due to their red sap.
                                
                                Alders are thought to be both a bringer of misfortune and a repellent of negativity."""
                        )
                )
                .page(
                        GalleryBuilder.of(
                                MultiblockPageBuilder.of(procureMutandis())
                                        .multiblockId(Enchanted.id("alder_tree")),
                                BlockPageBuilder.of(procureMutandis(), EBlocks.ALDER_SAPLING.get().defaultBlockState())
                        )
                )
                .build(blockPath(EBlocks.ALDER_SAPLING.get()), output);

        EntryBuilder.of("Hawthorn Trees")
                .icon(EItems.HAWTHORN_SAPLING.get().getDefaultInstance())
                .assignedItems(EItems.HAWTHORN_SAPLING.get(), EItems.HAWTHORN_LOG.get(), EItems.STRIPPED_HAWTHORN_LOG.get(),
                        EItems.HAWTHORN_PLANKS.get(), EItems.HAWTHORN_STAIRS.get(), EItems.HAWTHORN_SLAB.get(), EItems.HAWTHORN_FENCE.get(),
                        EItems.HAWTHORN_FENCE_GATE.get(), EItems.HAWTHORN_BUTTON.get(), EItems.HAWTHORN_PRESSURE_PLATE.get())
                .page(
                        HeaderedTextBuilder.of("Hawthorn Trees", """
                                This species of crataegus, commonly referred to as Hawthorn, is native to northern Europe and known as a tree of purity.
                                
                                In addition to herbal uses, hawthorn is known to be an effective material for dispatching vampires."""
                        )
                )
                .page(
                        GalleryBuilder.of(
                                MultiblockPageBuilder.of(procureMutandis()).multiblockId(Enchanted.id("hawthorn_tree")),
                                BlockPageBuilder.of(procureMutandis(), EBlocks.HAWTHORN_SAPLING.get().defaultBlockState())
                        )
                )
                .build(blockPath(EBlocks.HAWTHORN_SAPLING.get()), output);

        EntryBuilder.of("Rowan Trees")
                .icon(EItems.ROWAN_BERRIES.get().getDefaultInstance())
                .assignedItems(EItems.ROWAN_BERRIES.get(), EItems.ROWAN_SAPLING.get(), EItems.ROWAN_LOG.get(),
                        EItems.STRIPPED_ROWAN_LOG.get(), EItems.ROWAN_PLANKS.get(), EItems.ROWAN_STAIRS.get(),
                        EItems.ROWAN_SLAB.get(), EItems.ROWAN_FENCE.get(), EItems.ROWAN_FENCE_GATE.get(),
                        EItems.ROWAN_BUTTON.get(), EItems.ROWAN_PRESSURE_PLATE.get())
                .page(
                        HeaderedTextBuilder.of("Rowan Trees", """
                                The rowan, or mountain-ash, a small deciduous tree native to Europe, has many uses in witchcraft.
                                
                                The magical affinity of the Rowan is seldom matched by other trees, and is thought to give protection against malevolent beings."""
                        )
                )
                .page(
                        GalleryBuilder.of(
                                MultiblockPageBuilder.of(procureMutandis()).multiblockId(Enchanted.id("rowan_tree")),
                                BlockPageBuilder.of(procureMutandis(), EBlocks.ROWAN_SAPLING.get().defaultBlockState())
                        )
                )
                .build(blockPath(EBlocks.ROWAN_SAPLING.get()), output);
    }

    public void buildGettingStartedEntries(BookContentOutput output) {
        EntryBuilder.of("Chalice Upgrades")
                .icon(EItems.CHALICE_FILLED.get().getDefaultInstance())
                .assignedItems(EItems.CHALICE_FILLED.get(), EItems.CHALICE.get())
                .page(
                        HeaderedTextBuilder.of("Chalices", formatItems("A chalice may be placed on top of an altar to increase its power capacity. The chalice can be filled by using $(b)$(el:%s)Redstone Soup$() on it.", EItems.REDSTONE_SOUP.get())
                        ),
                        CraftingRecipeBuilder.of(Enchanted.id("chalice")).y(70)
                )
                .page(
                        GalleryBuilder.of(
                                BlockPageBuilder.of("$(b)Chalice:$()\n+1x Power Capacity", EBlocks.CHALICE.get().defaultBlockState())
                                        .scale(2.0F).offsetY(-20),
                                BlockPageBuilder.of("$(b)Chalice (Filled):$()\n+2x Power Capacity", EBlocks.CHALICE_FILLED.get().defaultBlockState())
                                        .scale(2.0F).offsetY(-20)
                        )
                )
                .build(gettingStartedPath("chalice_upgrades"), output);

        EntryBuilder.of("Skull Upgrades")
                .icon(Items.SKELETON_SKULL.getDefaultInstance())
                .page(
                        HeaderedTextBuilder.of("Skulls", """
                                Skulls can increase both the capacity and recharge rate of an altar, with varying effectiveness.
                                
                                Human skulls are particularly effective at channeling energy."""
                        )
                )
                .page(
                        GalleryBuilder.of(
                                BlockPageBuilder.of("$(b)Skeleton Skull:$()\n+1x Power Capacity\n+1x Recharge Rate", Blocks.SKELETON_SKULL.defaultBlockState())
                                        .scale(1.6F).offsetY(-20).textOffset(-25),
                                BlockPageBuilder.of("$(b)Wither Skeleton Skull:$()\n+2x Power Capacity\n+2x Recharge Rate", Blocks.WITHER_SKELETON_SKULL.defaultBlockState())
                                        .scale(1.6F).offsetY(-20).textOffset(-25),
                                BlockPageBuilder.of("$(b)Player Skull:$()\n+2.5x Power Capacity\n+3x Recharge Rate", Blocks.PLAYER_HEAD.defaultBlockState())
                                        .scale(1.6F).offsetY(-20).textOffset(-25)
                        )
                )
                .build(gettingStartedPath("skull_upgrades"), output);

        EntryBuilder.of("Torch Upgrades")
                .icon(EItems.CANDELABRA.get().getDefaultInstance())
                .assignedItems(EItems.CANDELABRA.get())
                .page(
                        HeaderedTextBuilder.of("Torches", "A torch, candelabra or candle can be an effective means of increasing your altar's recharge rate."
                        ),
                        CraftingRecipeBuilder.of(Enchanted.id("candelabra")).y(70)
                )
                .page(
                        GalleryBuilder.of(
                                BlockPageBuilder.of("$(b)Torch:$()\n+0.5x Recharge Rate", Blocks.TORCH.defaultBlockState())
                                        .scale(1.8F).offsetY(-15).textOffset(-30),
                                BlockPageBuilder.of("$(b)Candle:$()\n+1x Recharge Rate", Blocks.LIGHT_GRAY_CANDLE.defaultBlockState())
                                        .scale(2.0F).offsetY(-20).textOffset(-30),
                                BlockPageBuilder.of("$(b)Candelabra:$()\n+2x Recharge Rate", EBlocks.CANDELABRA.get().defaultBlockState())
                                        .scale(1.75F).offsetY(-15).textOffset(-30)
                        )
                )
                .build(gettingStartedPath("torch_upgrades"), output);
    }

    public void buildBrewingEntries(BookContentOutput output) {
        EntryBuilder.of("Creating Brews")
                .icon(EItems.WITCH_CAULDRON.get().getDefaultInstance())
                .page(
                        HeaderedTextBuilder.of("Brews and Potions", formatBlocks("""
                                Creating a brew, infusion or potion is a simple process, requiring either a $(b)$(c:#582C69)$(el:%1$s)witch's cauldron$() or $(b)$(c:#582C69)$(el:%2$s)kettle$().
                                
                                First, you must fill your $(b)$(el:%1$s)cauldron$() or $(b)$(el:%2$s)kettle$() with water from a bucket. You will know it is full if no more water can fit.""",
                                EBlocks.WITCH_CAULDRON.get(), EBlocks.KETTLE.get())
                        )
                )
                .page(
                        MultiblockBuilder.of()
                                .y(70).height(60)
                                .multiblock(new DenseMultiblock(
                                        List.of(
                                                List.of("H"),
                                                List.of("C")
                                        ),
                                        Map.of(
                                                'C', new SimpleStateMatcher(List.of(EBlocks.WITCH_CAULDRON.get().defaultBlockState())),
                                                'H', new TagStateMatcher(ETags.Blocks.HEAT_SOURCES)
                                        )
                                )),
                        HeaderedTextBuilder.of("Heat Sources", "Next, place a heat source underneath your vessel such as fire, lava or a campfire. After a few seconds, the water should begin to boil.")
                )
                .page(
                        HeaderedTextBuilder.of("Adding Ingredients", """
                                The final step, after filling the vessel and waiting for it to boil, is to drop your ingredients into the vessel, $(b)in order$(), one by one.
                                
                                The water should start to change colour. Once added, the brew needs to boil for a few seconds to finish."""
                        )
                )
                .page(
                        HeaderedTextBuilder.of("Spoiled Brews", """
                                If you make a mistake during brewing, such as adding the wrong ingredient or removing the heat source, your brew will $(b)spoil$(/b).
                                
                                When spoiled, the brew will turn brown and need to be removed using an $(b)empty bucket$(/b)."""
                        )
                )
                .build(brewingPath("brewing"), output);
    }

    public void buildCircleMagicEntries(BookContentOutput output) {
        EntryBuilder.of("Performing Rites")
                .icon(EItems.CIRCLE_TALISMAN.get().getDefaultInstance())
                .page(
                      HeaderedTextBuilder.of("Performing Rites", """
                              To perform circle magic, there are three basic steps a witch must follow. First, drawing the correct chalk circles. Second, placing the correct sacrifices into the circle abnd lastly, activating the rite.""")
                )
                .page(
                        HeaderedTextBuilder.of("Chalk Circles", "")
                )
                .build("circle_magic/tutorial/performing_rites", output);
    }



    private void galleryEntry(BookContentOutput output, String title, Item item, String description, PageComponentBuilder... galleryComponents) {
        headeredTextEntry(title, description, item)
                .page(GalleryBuilder.of(galleryComponents))
                .build(itemPath(item), output);
    }

    private void craftingEntry(BookContentOutput output, String title, String description, Item item) {
        craftingEntry(output, title, description, item, item);
    }

    /**
     * The first item should be the one the recipe is for. The others are just linked to the page.
     */
    private void craftingEntry(BookContentOutput output, String title, String description, Item item, Item... items) {
        headeredTextEntry(title, description, items)
                .page(CraftingPageBuilder.of(itemId(item)))
                .build(itemPath(item), output);
    }

    private void blockEntry(BookContentOutput output, String title, String description, Block block, String procurement, Item... items) {
        blockEntry(title, description, block.defaultBlockState(), procurement, items).build(blockPath(block), output);
    }

    private void blockEntry(BookContentOutput output, String title, String description, CropBlockAgeFive block, String procurement, Item... items) {
        blockEntry(title, description, block.defaultBlockState().setValue(CropBlockAgeFive.AGE_FIVE, 4), procurement, items).build(blockPath(block), output);
    }

    private void cauldronEntry(BookContentOutput output, String title, Item item, String description) {
        headeredTextEntry(title, description, item)
                .page(WitchCauldronRecipeBuilder.of(Enchanted.id("witch_cauldron/" + itemId(item).getPath())))
                .build(itemPath(item), output);
    }

    private void kettleEntry(BookContentOutput output, String title, Item item, String description) {
        headeredTextEntry(title, description, item)
                .page(KettleRecipeBuilder.of(Enchanted.id("kettle/" + itemId(item).getPath())))
                .build(itemPath(item), output);
    }

    private EntryBuilder blockEntry(String title, String description, BlockState state, String blockDescription, Item... items) {
        return headeredTextEntry(title, description, items).page(BlockPageBuilder.of(blockDescription, state));
    }

    private EntryBuilder headeredTextEntry(String title, String description, Item... items) {
        return EntryBuilder.of(title)
                .icon(items[0].getDefaultInstance())
                .assignedItems(items)
                .page(HeaderedTextBuilder.of(title, description));
    }

    private static <T> T[] merge(T a, T[] b) {
        return ArrayUtils.insert(0, b, a);
    }

    private static <T> T[] merge(T[] a, T b) {
        return ArrayUtils.insert(0, a, b);
    }


    private static <T> T[] merge(T[] a, T[] b) {
        return ArrayUtils.addAll(a, b);
    }

    private PageComponentBuilder[] byproductComponents(String... recipes) {
        List<PageComponentBuilder> pages = new ArrayList<>();

        for(int i = 0; i < recipes.length; i++) {
            if(++i < recipes.length)
                pages.add(DoubleByproductPageBuilder.of(Enchanted.id(recipes[i-1]), Enchanted.id(recipes[i])));
            else
                pages.add(ByproductPageBuilder.of(Enchanted.id(recipes[i-1])));
        }

        return pages.toArray(PageComponentBuilder[]::new);
    }

    private PageComponentBuilder[] distillingComponents(String... recipes) {
        return Arrays.stream(recipes).map(Enchanted::id).map(DistilleryPageBuilder::of).toArray(PageComponentBuilder[]::new);
    }

    private String procureMutandis() {
        return formatItems("$(b)Procurement:$()\nUse $(el:%s)$(b)mutandis$() on a small plant.", EItems.MUTANDIS.get());
    }

    private String procureGrass() {
        return "$(b)Procurement:$()\nDropped by tall and short grass";
    }



    private ResourceLocation itemId(Item item) {
        return BuiltInRegistries.ITEM.getKey(item);
    }

    private String gettingStartedPath(String path) {
        return "getting_started/" + path;
    }

    private String brewingPath(String path) {
        return "brewing/" + path;
    }

    private String itemPath(Item item) {
        return "items/" + BuiltInRegistries.ITEM.getKey(item).getPath();
    }

    private String blockPath(Block block) {
        return "blocks/" + BuiltInRegistries.BLOCK.getKey(block).getPath();
    }

    private String[] itemPaths(Item... items) {
        return Arrays.stream(items).map(this::itemPath).toArray(String[]::new);
    }

    private String[] blockPaths(Block... items) {
        return Arrays.stream(items).map(this::blockPath).toArray(String[]::new);
    }

    private String format(String string, Object... vars) {
        return String.format(string, vars);
    }

    private String formatItems(String string, Item... items) {
        return format(string, (Object[])itemPaths(items));
    }

    private String formatBlocks(String string, Block... blocks) {
        return format(string, (Object[])blockPaths(blocks));
    }

}