package net.favouriteless.enchanted.neoforge.datagen.providers.modopedia;

import net.favouriteless.enchanted.api.datagen.builders.modopedia.templates.FramedImageBuilder;
import net.favouriteless.enchanted.api.datagen.builders.modopedia.templates.page.*;
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
import net.favouriteless.modopedia.api.datagen.builders.page_components.components.*;
import net.favouriteless.modopedia.api.datagen.builders.templates.page.*;
import net.favouriteless.modopedia.api.datagen.builders.templates.recipes.CookingRecipeBuilder;
import net.favouriteless.modopedia.api.datagen.builders.templates.recipes.CraftingRecipeBuilder;
import net.favouriteless.modopedia.api.datagen.providers.ContentSetProvider;
import net.favouriteless.modopedia.api.text.FormattedStringBuilder;
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
                .landingText(FormattedStringBuilder.begin()
                        .then("To get started in witchcraft, first you must know about the basic tools and equipment used by witches.")
                        .paragraph("This chapter tells you everything you need to know about getting started as a Witch.").toString())
                .icon(EItems.ARTHANA.get().getDefaultInstance())
                .children("getting_started/altars")
                .entries(itemPaths(EItems.ARTHANA.get(), EItems.EARMUFFS.get(), EItems.BROOM.get(), EItems.TAGLOCK.get(),
                        EItems.BONE_NEEDLE.get(), EItems.ATTUNED_STONE.get(), EItems.WAYSTONE.get(), EItems.WOOD_ASH.get(),
                        EItems.QUICKLIME.get(), EItems.CIRCLE_TALISMAN.get()))
                .sortNum(0)
                .build("getting_started", output);

        CategoryBuilder.of("Altars")
                .landingText(FormattedStringBuilder.begin()
                        .then("An altar acts as a source of magical energy for chalk circles and most of a witch's tools.")
                        .paragraph("The amount of natural energy around an altar will determine how effective it is. Generally, a variety of plants is best.").toString())
                .icon(EItems.ALTAR.get().getDefaultInstance())
                .displayOnFrontPage(false)
                .entries(blockPath(EBlocks.ALTAR.get()), gettingStartedPath("torch_upgrades"),
                        gettingStartedPath("skull_upgrades"), gettingStartedPath("chalice_upgrades"))
                .build("getting_started/altars", output);

        CategoryBuilder.of("Material Processing")
                .landingText(FormattedStringBuilder.begin()
                        .then("Many resources used in witchcraft can only be obtained by extracting them from other materials.")
                        .paragraph("This involves two processes; fume collection and distillation. Both of these are detailed in this chapter.").toString())
                .icon(EItems.WITCH_OVEN.get().getDefaultInstance())
                .entries(blockPaths(EBlocks.WITCH_OVEN.get(), EBlocks.DISTILLERY.get()))
                .entries(itemPaths(EItems.CLAY_JAR.get(), EItems.BREATH_OF_THE_GODDESS.get(),
                        EItems.DEMONIC_BLOOD.get(), EItems.DIAMOND_VAPOUR.get(), EItems.ENDER_DEW.get(),
                        EItems.EXHALE_OF_THE_HORNED_ONE.get(), EItems.FOUL_FUME.get(), EItems.GYPSUM.get(),
                        EItems.HINT_OF_REBIRTH.get(), EItems.ODOUR_OF_PURITY.get(), EItems.OIL_OF_VITRIOL.get(),
                        EItems.QUICKLIME.get(), EItems.REEK_OF_MISFORTUNE.get(), EItems.REFINED_EVIL.get(),
                        EItems.TEAR_OF_THE_GODDESS.get(), EItems.WHIFF_OF_MAGIC.get(), EItems.WOOD_ASH.get()))
                .sortNum(1)
                .build("extraction", output);

        CategoryBuilder.of("Herbology")
                .landingText(FormattedStringBuilder.begin()
                        .then("Witchcraft often requires using various plants, some of which are common while others require mutations.")
                        .paragraph("This chapter aims to tell you how to obtain these plants and what they do.").toString())
                .icon(EItems.WOLFSBANE_FLOWER.get().getDefaultInstance())
                .children("herbology/mutated_plants")
                .entries(blockPaths(EBlocks.BELLADONNA.get(), EBlocks.WATER_ARTICHOKE.get(), EBlocks.MANDRAKE.get(),
                        EBlocks.SNOWBELL.get(), EBlocks.WOLFSBANE.get(), EBlocks.GARLIC.get()))
                .sortNum(2)
                .build("herbology", output);

        CategoryBuilder.of("Mutated Plants")
                .landingText(FormattedStringBuilder.begin()
                        .then("Some plants require mutations to be obtained, either by using Mutandis or Mutandis Extremis.")
                        .paragraph("Plants of this nature are covered in this chapter.").toString())
                .icon(EItems.MUTANDIS.get().getDefaultInstance())
                .displayOnFrontPage(false)
                .entries(herbologyPath("mutations"))
                .entries(itemPaths(EItems.MUTANDIS.get(), EItems.MUTANDIS_EXTREMIS.get()))
                .entries(blockPaths(EBlocks.ROWAN_SAPLING.get(), EBlocks.HAWTHORN_SAPLING.get(), EBlocks.ALDER_SAPLING.get(),
                        EBlocks.SPANISH_MOSS.get(), EBlocks.GLINT_WEED.get(), EBlocks.EMBER_MOSS.get(), EBlocks.BLOOD_POPPY.get()))
                .build("herbology/mutated_plants", output);

        CategoryBuilder.of("Brewing")
                .landingText(FormattedStringBuilder.begin()
                        .then("One of the most essential skills a witch can possess is the abilities and knowledge to create brews, potions and decoctions.")
                        .paragraph("The methods of brewing and various common recipes can be found in this chapter.").toString())
                .icon(EItems.REDSTONE_SOUP.get().getDefaultInstance())
                .entries("brewing/brewing")
                .entries(itemPaths(EItems.MUTANDIS.get(), EItems.MUTANDIS_EXTREMIS.get(), EItems.GOLDEN_CHALK.get(),
                        EItems.NETHER_CHALK.get(), EItems.OTHERWHERE_CHALK.get(), EItems.DROP_OF_LUCK.get(),
                        EItems.REDSTONE_SOUP.get(), EItems.FLYING_OINTMENT.get(), EItems.HAPPENSTANCE_OIL.get(),
                        EItems.MYSTIC_UNGUENT.get(), EItems.SPIRIT_OF_OTHERWHERE.get(), EItems.SOUL_OF_THE_WORLD.get(),
                        EItems.BREW_OF_LOVE.get(), EItems.BREW_OF_SPROUTING.get(), EItems.BREW_OF_THE_DEPTHS.get(),
                        EItems.BREW_OF_THE_GROTESQUE.get()))
                .sortNum(3)
                .build("brewing", output);

        CategoryBuilder.of("Circle Magic")
                .icon(EItems.CIRCLE_TALISMAN.get().getDefaultInstance())
                .landingText(FormattedStringBuilder.begin()
                        .then("Circle magic is the practice of using chalk circles and foci to manifest complex magical phenomenon.")
                        .paragraph("The fundamentals of circle magic and known rites are detailed in this chapter.").toString())
                .children("circle_magic/tutorial", "circle_magic/binding", "circle_magic/creature", "circle_magic/curses",
                        "circle_magic/transposition", "circle_magic/world")
                .sortNum(5)
                .build("circle_magic", output);

        CategoryBuilder.of("Fundamental Theory")
                .icon(EItems.CIRCLE_TALISMAN.get().getDefaultInstance())
                .displayOnFrontPage(false)
                .landingText(FormattedStringBuilder.begin()
                        .then("Performing a circle magic rite can be a complex and difficult process, requiring a mixture of chalk circles and foci.")
                        .paragraph("This chapter explains the fundamentals and some important items.").toString())
                .entries("circle_magic/tutorial/performing_rites")
                .entries(itemPaths(EItems.GOLDEN_CHALK.get(), EItems.RITUAL_CHALK.get(), EItems.NETHER_CHALK.get(),
                        EItems.OTHERWHERE_CHALK.get(), EItems.BROOM.get(), EItems.CIRCLE_TALISMAN.get()))
                .build("circle_magic/tutorial", output);

        CategoryBuilder.of("Binding")
                .icon(Items.CHAIN.getDefaultInstance())
                .displayOnFrontPage(false)
                .landingText(FormattedStringBuilder.begin()
                        .then("Binding is the act of linking a being or object to a magical effect or property, for example imbuing a broomstick with the power of flight.")
                        .paragraph("Rites of this manner are explained in this chapter.").toString())
                .entries(ritePaths("charging_stone", "bind_familiar", "bind_talisman", "waystone", "waystone_blooded", "infuse_broom"))
                .build("circle_magic/binding", output);

        CategoryBuilder.of("Creature")
                .icon(Items.CREEPER_HEAD.getDefaultInstance())
                .displayOnFrontPage(false)
                .landingText(FormattedStringBuilder.begin()
                        .then("Creature rites are used to change or limit the behaviour of mobs and other creatures.")
                        .paragraph("Rites of this type are often used for protection, warding and imprisonment.").toString())
                .entries(ritePaths("imprisonment", "protection", "protection_temporary", "sanctity"))
                .build("circle_magic/creature", output);

        CategoryBuilder.of("Curses")
                .icon(Items.WITHER_ROSE.getDefaultInstance())
                .displayOnFrontPage(false)
                .landingText(FormattedStringBuilder.begin()
                        .then("A curse is a long-lasting, malicious form of magic intended to harm it's target.")
                        .paragraph("There are several types of curses, each of which are explained in detail here.").toString())
                .entries("circle_magic/curses/casting_curses")
                .entries(ritePaths("blight", "curse_misfortune", "curse_overheating", "curse_sinking", "remove_misfortune",
                        "remove_overheating", "remove_sinking"))
                .build("circle_magic/curses", output);

        CategoryBuilder.of("Transposition")
                .icon(Items.ENDER_PEARL.getDefaultInstance())
                .displayOnFrontPage(false)
                .landingText(FormattedStringBuilder.begin()
                        .then("Transposition rites are used to change the position of an object or being, for example, teleportation.")
                        .paragraph("Summoning also falls under transposition as the summoned being is being moved to the circle.").toString())
                .entries(ritePaths("summon_entity", "summon_familiar", "transpose_caster", "transpose_iron"))
                .build("circle_magic/transposition", output);

        CategoryBuilder.of("World")
                .icon(Items.GRASS_BLOCK.getDefaultInstance())
                .displayOnFrontPage(false)
                .landingText(FormattedStringBuilder.begin()
                        .then("World rites are a powerful form of circle magic in which a witch attempts to alter natural phenomenon.")
                        .paragraph("This can prove dangerous if used improperly.").toString())
                .entries(ritePaths("broiling", "fertility", "forest", "sky_wrath", "total_eclipse", "transpose_iron"))
                .build("circle_magic/world", output);

        CategoryBuilder.of("Poppetry")
                .icon(EItems.SPINNING_WHEEL.get().getDefaultInstance())
                .landingText(FormattedStringBuilder.begin()
                        .then("Poppets are a type of sympathetic magic for transferring effects on a person to a doll or vice versa.")
                        .paragraph("There are beneficial and harmful poppets, both of which are detailed in this chapter.").toString())
                .entries(itemPath(EItems.POPPET.get()))
                .entries(blockPath(EBlocks.POPPET_SHELF.get()))
                .entries(itemPaths(EItems.POPPET_INFUSED.get(), EItems.POPPET_STURDY.get(), EItems.ARMOUR_POPPET.get(),
                        EItems.EARTH_POPPET.get(), EItems.FIRE_POPPET.get(), EItems.HUNGER_POPPET.get(), EItems.MAGIC_POPPET.get(),
                        EItems.TOOL_POPPET.get(), EItems.VOID_POPPET.get(), EItems.VOODOO_POPPET.get(), EItems.VOODOO_PROTECTION_POPPET.get(),
                        EItems.WATER_POPPET.get()))
                .sortNum(4)
                .build("poppetry", output);
    }

    @Override
    public void buildEntries(Provider provider, BookContentOutput output) {
        buildItemEntries(output);
        buildBlockEntries(output);

        buildGettingStartedEntries(output);
        buildHerbologyEntries(output);
        buildBrewingEntries(output);
        buildCircleMagicEntries(output);
        buildPoppetryEntries(output);
    }

    public void buildItemEntries(BookContentOutput output) {
        buildExtractionItemEntries(output);
        buildBrewItemEntries(output);

        craftingEntry(output, "Ritual Chalk", FormattedStringBuilder.begin()
                        .then("Ritual chalk is the most basic of the four types of chalk, used for drawing basic circles in rites.")
                        .paragraph("There are no known special effects of ritual chalk, it is merely chalk infused with ")
                        .boldEntryLink(itemPath(EItems.TEAR_OF_THE_GODDESS.get()), "Tear of the Goddess").then(".").toString(),
                EItems.RITUAL_CHALK.get());

        cauldronEntry(output, "Golden Chalk", EItems.GOLDEN_CHALK.get(), FormattedStringBuilder.begin()
                .then("Chalk is vital for performing circle magic and among chalks golden chalk is the most important.")
                .paragraph("The ").boldEntryLink("circle_magic/tutorial/performing_rites", "heart glyph").then(" for ")
                .boldCategoryLink("circle_magic", "circle magic").then(" is drawn using golden chalk.").toString());

        cauldronEntry(output, "Infernal Chalk", EItems.NETHER_CHALK.get(), FormattedStringBuilder.begin()
                        .then("Infusing chalk with blaze powder binds it to the nether and enables it to better conduct heat.")
                        .paragraph("Infernal chalk is used for many rites involving the nether, demonic beings or fire.").toString());

        cauldronEntry(output, "Otherwhere Chalk", EItems.OTHERWHERE_CHALK.get(), FormattedStringBuilder.begin()
                        .then("Materials from the end can be infused into chalk to create a rich, purple chalk with special properties.")
                        .paragraph("Typically, otherwhere chalk is used in circle magic rites involving teleportation, transposition, relocation or the end.").toString());

        craftingEntry(output, "Brooms", FormattedStringBuilder.begin()
                        .then("A broom can be used to sweep chalk away quickly, without having to spend time erasing it.")
                        .paragraph("Conveniently, a broom can also make for an excellent method of transportation with some preparation.").toString(),
                EItems.BROOM.get());

        craftingEntry(output, "Earmuffs", FormattedStringBuilder.begin()
                        .then("When dealing with Mandrakes, Banshees and other loud creatures, protection for your ears is essential.")
                        .paragraph().bold("Earmuffs").then(" can dampen deafening sounds, rendering them bearable.").toString(),
                EItems.EARMUFFS.get());

        craftingEntry(output, "Taglocks", FormattedStringBuilder.begin()
                        .then("Sometimes, you may need to represent another person to perform magic. This can be achieved using a ").bold("taglock kit")
                        .paragraph("By using a taglock kit on a Player or their bed, you can collect a sample to use in magic.").toString(),
                EItems.TAGLOCK.get(), EItems.TAGLOCK_FILLED.get(), EItems.TAGLOCK.get());

        EntryBuilder.of("Arthana")
                .icon(EItems.ARTHANA.get().getDefaultInstance())
                .assignedItems(EItems.ARTHANA.get(), EItems.TONGUE_OF_DOG.get(), EItems.WOOL_OF_BAT.get(), EItems.CREEPER_HEART.get())
                .page(
                        HeaderedTextBuilder.of("Arthana", FormattedStringBuilder.begin().then("The ").bold("arthana").then("  is a ritual knife used for sacrifice. When used to kill certain mobs, they can drop rare materials.").toString()),
                        CraftingRecipeBuilder.of(itemId(EItems.ARTHANA.get())).y(75)
                )
                .page(GalleryBuilder.of(
                        EntityPageBuilder.of(EntityType.BAT, drops("Wool of Bat")).scale(0.75F),
                        EntityPageBuilder.of(EntityType.WOLF, drops("Tongue of Dog")).scale(0.65F),
                        EntityPageBuilder.of(EntityType.CREEPER, drops("Creeper Heart")),
                        EntityPageBuilder.of(EntityType.SKELETON, drops("Skeleton Skull")))
                )
                .build(itemPath(EItems.ARTHANA.get()), output);

        craftingEntry(output, "Attuned Stones", FormattedStringBuilder.begin()
                .then("An attuned stone is a diamond which has been infused with magical energy.")
                .paragraph("It can be charged to be used as a portable container for ").boldEntryLink(blockPath(EBlocks.ALTAR.get()), "altar power")
                .then(", allowing the user to cast circle magic without an altar being present, and is also used in the creation of a variety of magical tools.").toString(),
                EItems.ATTUNED_STONE.get());

        craftingEntry(output, "Bone Needles", FormattedStringBuilder.begin()
                        .then("Needles are an important part of many tools used for Witchcraft, most notably ").boldEntryLink(itemPath(EItems.TAGLOCK.get()), "taglock kits")
                        .then(" and ").bold("poppets").then(".")
                        .paragraph("A simple needle can be fashioned by whittling a bone.").toString(),
                EItems.BONE_NEEDLE.get());

        craftingEntry(output, "Waystone", FormattedStringBuilder.begin()
                        .then("Waystones are a type of stone which can be bound to the location of a block or entity and used to represent it in circle magic.")
                        .paragraph("This can be accomplished via a ").boldCategoryLink("circle_magic/binding", "rite of binding").then(".").toString(),
                EItems.WAYSTONE.get());

        craftingEntry(output, "Circle Talisman", FormattedStringBuilder.begin()
                        .then("Used for picking up and holding chalk glyphs via a ").boldEntryLink(ritePath("bind_talisman"), "rite of binding")
                        .then(", talismans are an indispensable tool for circle magic.")
                        .paragraph("The ornate and complex symbols engraved on the talisman can also indicate wealth.").toString(),
                EItems.CIRCLE_TALISMAN.get());

        cauldronEntry(output, "Mutandis", EItems.MUTANDIS.get(), FormattedStringBuilder.begin()
                .then("Mutandis is used to mutate plants into other species you could not normally obtain.")
                .paragraph("Using this substance on a block can cause it to ").boldEntryLink(herbologyPath("mutations"), "mutate")
                .then(" into a different block under the right conditions.").toString()
        );

        cauldronEntry(output, "Mutandis Extremis", EItems.MUTANDIS_EXTREMIS.get(), FormattedStringBuilder.begin()
                .then("Mutandis Extremis, an enhanced form of ").boldEntryLink(itemPath(EItems.MUTANDIS.get()), "mutandis").then(", performs a very similar function.")
                .paragraph("Extremis is able to create more advanced ").boldEntryLink(herbologyPath("mutations"), "mutations")
                .then(" such as ").boldEntryLink(blockPath(EBlocks.BLOOD_POPPY.get()), "Blood Poppies").then(".").toString()
        );

        EntryBuilder.of("Clay Jars")
                .icon(EItems.CLAY_JAR.get().getDefaultInstance())
                .assignedItems(EItems.CLAY_JAR.get(), EItems.SOFT_CLAY_JAR.get())
                .page(HeaderedTextBuilder.of("Clay Jars", "A container to hold materials collected during fume extraction or distillation is essential for witches. A simple clay jar works well for this purpose."))
                .page(
                        HeaderBuilder.of(Modopedia.translation("template", "recipe")),
                        SeparatorBuilder.of().y(10),
                        CookingRecipeBuilder.of(itemId(EItems.CLAY_JAR.get())).y(30),
                        CraftingRecipeBuilder.of(itemId(EItems.SOFT_CLAY_JAR.get())).y(70)
                )
                .build(itemPath(EItems.CLAY_JAR.get()), output);
    }

    public void buildBrewItemEntries(BookContentOutput output) {
        kettleEntry(output, "Brew of Love", EItems.BREW_OF_LOVE.get(), FormattedStringBuilder.begin()
                .then("Charming or love potions are one of the most widely sought types of brew.")
                .paragraph("When thrown, the vapour produced will cause nearby animals to become infatuated.").toString());

        kettleEntry(output, "Brew of Sprouting", EItems.BREW_OF_SPROUTING.get(), FormattedStringBuilder.begin()
                .then("Being able to slow your opponents in combat can be the difference between life and death.")
                .paragraph("The brew of sprouting can be used to ensnare mobs and people with roots.")
                .paragraph().bold("This feature is coming soon.").toString());

        kettleEntry(output, "Brew of the Depths", EItems.BREW_OF_THE_DEPTHS.get(), FormattedStringBuilder.begin()
                .then("The ability to breathe underwater has fascinated people for millennia. Fortunately, witches have a solution for this.")
                .paragraph("The brew of depths grants the ability to breathe underwater for an extended time when ingested.").toString());

        kettleEntry(output, "Brew of the Grotesque", EItems.BREW_OF_THE_GROTESQUE.get(), FormattedStringBuilder.begin()
                .then("This brew makes the drinker unrecognisably grotesque, tricking nearby mobs into believing the drinker is one of their own.")
                .paragraph("Additionally, brew of the grotesque is used as a base in most ").boldEntryLink("circle_magic/curses", "curses").then(".").toString());

        kettleEntry(output, "Flying Ointment", EItems.FLYING_OINTMENT.get(), FormattedStringBuilder.begin()
                .then("Flying ointment is a brew which seems to defy gravity; anything imbued with it will gain the property of flight, including people who ingest it.")
                .paragraph("Most notably, it can be used to ").boldEntryLink(ritePath("infuse_broomstick"), "infuse broomsticks").toString());

        kettleEntry(output, "Happenstance Oil", EItems.HAPPENSTANCE_OIL.get(), FormattedStringBuilder.begin()
                .then("Clairvoyance is an indispensable tool for a witch in need of information.")
                .paragraph("Happenstance oil is a key component of crystal balls, and while consuming it is not recommended, it can improve your vision.").toString());

        kettleEntry(output, "Mystic Unguent", EItems.MYSTIC_UNGUENT.get(), FormattedStringBuilder.begin()
                .then("Mystic Unguent is a curious concoction which allows a witch to give physical form to thoughts.")
                .paragraph("It's main use is in the production of mystic branches, consuming this brew is not recommended.").toString());

        kettleEntry(output, "Redstone Soup", EItems.REDSTONE_SOUP.get(), FormattedStringBuilder.begin()
                .then("Redstone Soup primarily acts as a base for other infusions and brews, it has very few reported effects outside of this.")
                .paragraph("Ingesting redstone soup can increase your health for a short time.").toString());

        kettleEntry(output, "Soul of the World", EItems.SOUL_OF_THE_WORLD.get(), FormattedStringBuilder.begin()
                .then("Soul of the World is a derivation of ").boldEntryLink(itemPath(EItems.REDSTONE_SOUP.get()), "redstone soup").then(" imbued with natural energy. It can be used to infuse energy into a person to grant them special abilities.")
                .paragraph("This brew is extremely toxic and should not be ingested.").toString());

        kettleEntry(output, "Spirit of Otherwhere", EItems.SPIRIT_OF_OTHERWHERE.get(), FormattedStringBuilder.begin()
                .then("Similar to ").boldEntryLink(itemPath(EItems.SOUL_OF_THE_WORLD.get()), "soul of the world")
                .then(", spirit of otherwhere is a derivation of ").boldEntryLink(itemPath(EItems.REDSTONE_SOUP.get()), "redstone soup").then(" imbued with the same properties as the End and its inhabitants.")
                .paragraph("This infusion is extremely toxic and should not be ingested.").toString());

        cauldronEntry(output, "Drop of Luck", EItems.DROP_OF_LUCK.get(), formatItems("Liquid luck, or drop of luck, is a potion which enhances the luck of anybody who drinks it and can be used in rites or the creation of magical items.\n\nFamously, it's a core ingredient in $(b)$(el:%s)Redstone Soup$().", EItems.REDSTONE_SOUP.get()));
    }

    public void buildExtractionItemEntries(BookContentOutput output) {
        cookingEntry(output, "Wood Ash", FormattedStringBuilder.begin()
                        .then("Simple ash has many uses, including making ").boldEntryLink(itemPath(EItems.RITUAL_CHALK.get()), "ritual chalk")
                        .then(" and bone meal among other things.")
                        .paragraph("As it has no magical properties, burning any sapling will work.").toString(),
                EItems.WOOD_ASH.get());

        cookingEntry(output, "Quicklime", FormattedStringBuilder.begin()
                        .then("Burnt lime, or quicklime, one of the primary ingredients for ")
                        .boldEntryLink(itemPath(EItems.GYPSUM.get()), "gypsum").then(", can be made using calcite.")
                        .paragraph("Quicklime can be thrown at players to temporarily blind them.").toString(),
                EItems.QUICKLIME.get());

        galleryEntry(output, "Breath of the Goddess", EItems.BREATH_OF_THE_GODDESS.get(), FormattedStringBuilder.begin()
                        .then("Due to it's silvery appearance, birch is sometimes called \"White Lady Of The Woods\" and is associated with the goddess Brigid.")
                        .paragraph("The smoke produced by burning birch has healing properties.").toString(),
                byproductComponents("byproduct/breath_of_the_goddess_birch_sapling")
        );

        galleryEntry(output, "Demonic Blood", EItems.DEMONIC_BLOOD.get(), FormattedStringBuilder.begin()
                        .then("The blood of a demon has many uses in witchcraft. While usually obtained from demons, it can also be refined from several other materials.")
                        .paragraph("It's main uses are in infusions and curses.").toString(),
                distillingComponents("distilling/diamond_vapour_blaze_rod")
        );

        galleryEntry(output, "Diamond Vapour", EItems.DIAMOND_VAPOUR.get(), FormattedStringBuilder.begin()
                        .then("Diamonds, by using oil of vitriol, can be dissolved and then evaporated to form a powerful refining agent.")
                        .paragraph("Diamond vapour is required to distill some other materials.").toString(),
                distillingComponents("distilling/diamond_oil_of_vitriol")
        );

        galleryEntry(output, "Ender Dew", EItems.ENDER_DEW.get(), FormattedStringBuilder.begin()
                        .then("Ender pearls, which allow people to teleport, can be distilled into a purer form called Ender Dew.")
                        .paragraph("This substance contains the relocation properties of ender pearls in a liquid form, making it useful for brewing.").toString(),
                distillingComponents("distilling/ender_pearl")
        );

        galleryEntry(output, "Horned One's Exhale", EItems.EXHALE_OF_THE_HORNED_ONE.get(), FormattedStringBuilder.begin()
                        .then("The Oak King, one of the aspects of the Horned God, symbolises nature, hunting and the cycle of life.")
                        .paragraph("The fumes produced by oaks are said to be The Horned God's exhale, carrying some of the properties associated with him.").toString(),
                byproductComponents("byproduct/exhale_of_the_horned_one_oak_sapling")
        );

        galleryEntry(output, "Foul Fume", EItems.FOUL_FUME.get(),  FormattedStringBuilder.begin()
                        .then("A foul smelling smoke containing sulfur, often smelled when encountering demonic beings or the nether.")
                        .paragraph("There are many plants, foods and other sources containing this gas.").toString(),
                merge(
                        byproductComponents("byproduct/foul_fume_jungle_sapling", "byproduct/foul_fume_logs_that_burn", "byproduct/foul_fume_raw_foods"),
                        distillingComponents("distilling/breath_of_the_goddess_lapis_lazuli", "distilling/diamond_vapour_ghast_tear")
                )
        );

        galleryEntry(output, "Gypsum", EItems.GYPSUM.get(), FormattedStringBuilder.begin()
                        .then("A soft, translucent mineral salt produced by oxidising sulfides in the presence of quicklime.")
                        .paragraph("In addition to being an effective fertiliser, it is commonly used as a base for ritual chalks.").toString(),
                distillingComponents("distilling/foul_fume_quicklime")
        );

        galleryEntry(output, "Hint of Rebirth", EItems.HINT_OF_REBIRTH.get(), FormattedStringBuilder.begin()
                        .then("Spruce trees are associated with the birth of the divine child. As such, they are a symbol of rebirth, protection, resilience and endurance.")
                        .paragraph("Regardless, they make good firewood.").toString(),
                byproductComponents("byproduct/hint_of_rebirth_spruce_sapling")
        );

        galleryEntry(output, "Odour of Purity", EItems.ODOUR_OF_PURITY.get(), FormattedStringBuilder.begin()
                        .then("Hawthorn can be used to collect a powerful purifying agent called Odour of Purity, which is used in the creation of most pure substances.")
                        .paragraph("This tree is sacred to the Goddesses Aine and Brigid.").toString(),
                merge(
                        byproductComponents("byproduct/odour_of_purity_hawthorn_sapling"),
                        distillingComponents("distilling/diamond_oil_of_vitriol", "distilling/diamond_vapour_ghast_tear")
                )
        );

        galleryEntry(output, "Oil of Vitriol", EItems.OIL_OF_VITRIOL.get(), FormattedStringBuilder.begin()
                        .then("A clear, slightly yellowed liquid which seems to react to living matter with vitriol, leaving them burned and blackened.")
                        .paragraph("Oil of vitriol dissolves most substances it comes into contact with.").toString(),
                distillingComponents("distilling/foul_fume_quicklime")
        );

        galleryEntry(output, "Reek of Misfortune", EItems.REEK_OF_MISFORTUNE.get(), FormattedStringBuilder.begin()
                        .then("The sacred alder tree appears to bleed when cut, bringing misfortune to all. It is thought that the tree contains the souls of our ancestors.")
                        .paragraph("The fumes produced from burning it can be collected, keeping these effects.").toString(),
                merge(
                        byproductComponents("byproduct/reek_of_misfortune_alder_sapling"),
                        distillingComponents("distilling/diamond_vapour_ghast_tear", "distilling/ender_pearl")
                )
        );

        galleryEntry(output, "Refined Evil", EItems.REFINED_EVIL.get(), FormattedStringBuilder.begin()
                        .then("Pure, condensed evil refined using diamond vapour.")
                        .paragraph("Refined Evil is primarily used for brewing, but also has some uses in demonology.").toString(),
                distillingComponents("distilling/diamond_vapour_ghast_tear")
        );

        galleryEntry(output, "Tear of the Goddess", EItems.TEAR_OF_THE_GODDESS.get(), FormattedStringBuilder.begin()
                        .then("Lapis Lazuli is a gemstone which brings wisdom, truth, loyalty and honour.")
                        .paragraph("Combining Lapis with the White Goddess's power amplifies it's effects.").toString(),
                distillingComponents("distilling/breath_of_the_goddess_lapis_lazuli")
        );

        galleryEntry(output, "Whiff of Magic", EItems.WHIFF_OF_MAGIC.get(), FormattedStringBuilder.begin()
                        .then("Rowan wood is famous for it's unparalleled affinity for magic")
                        .paragraph("The smoke it produces can be condensed into pure magical energy thought to provide protection against malevolent beings.").toString(),
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
                        HeaderedTextBuilder.of("Creating an Altar", FormattedStringBuilder.begin()
                                .then("To construct an altar, a witch must place ").bold("six altar blocks").then(" to form a rectangular platform.").toString()),
                        CraftingRecipeBuilder.of(Enchanted.id("altar")).y(65)
                )
                .page(
                        HeaderedTextBuilder.of("Altar Power", "Altars draw their power from nature, specifically from plants in the surrounding area. A variety of plants works better than having only a few."),
                        FramedImageBuilder.of(Enchanted.id("textures/gui/modopedia/altar.png")).x(10).y(80)

                )
                .page(
                        HeaderedTextBuilder.of("Upgrades", FormattedStringBuilder.begin()
                                .then("Certain blocks may be placed on top of the altar to multiply its effects.")
                                .paragraph("Upgrades have a ").bold("type").then(", only the best upgrade within a type will be counted.").toString()),
                        MultiblockBuilder.of()
                                .y(70).height(70)
                                .multiblockId(Enchanted.id("altar"))
                )
                .build(blockPath(EBlocks.ALTAR.get()), output);

        EntryBuilder.of("Distillation")
                .icon(EItems.DISTILLERY.get().getDefaultInstance())
                .assignedItems(EItems.DISTILLERY.get())
                .page(HeaderedTextBuilder.of("Distillation", FormattedStringBuilder.begin()
                        .then("The distillery allows witches to separate materials into their components by utilising differences in boiling and condensing points.")
                        .paragraph("Processing consumes ").boldEntryLink(blockPath(EBlocks.ALTAR.get()), "altar power").then(", so the distillery must be placed accordingly.").toString())
                )
                .page(
                        HeaderBuilder.of(Modopedia.translation("template", "crafting_recipe")),
                        SeparatorBuilder.of().y(10),
                        MultiblockBuilder.of().multiblock(new DenseMultiblock(List.of(List.of("D")), Map.of('D', new SimpleStateMatcher(List.of(EBlocks.DISTILLERY.get().defaultBlockState().setValue(DistilleryBlock.LIT, true))))))
                                .y(10).height(50),
                        CraftingRecipeBuilder.of(itemId(EItems.DISTILLERY.get())).y(70)
                )
                .build(blockPath(EBlocks.DISTILLERY.get()), output);

        EntryBuilder.of("Spinning")
                .icon(EItems.SPINNING_WHEEL.get().getDefaultInstance())
                .assignedItems(EItems.SPINNING_WHEEL.get())
                .page(HeaderedTextBuilder.of("Spinning", FormattedStringBuilder.begin()
                        .then("The spinning wheel is used by witches to weave materials together, this is especially prevalent in ")
                        .boldCategoryLink("poppetry", "poppetry").then(".")
                        .paragraph("Weaving consumes ").boldEntryLink(blockPath(EBlocks.ALTAR.get()), "altar power").then(", so the spinning wheel must be placed accordingly.").toString()))
                .page(
                        HeaderBuilder.of(Modopedia.translation("template", "crafting_recipe")),
                        SeparatorBuilder.of().y(10),
                        MultiblockBuilder.of().multiblock(new DenseMultiblock(List.of(List.of("D")), Map.of('D', new SimpleStateMatcher(List.of(EBlocks.SPINNING_WHEEL.get().defaultBlockState())))))
                                .y(10).height(50),
                        CraftingRecipeBuilder.of(itemId(EItems.SPINNING_WHEEL.get())).y(70)
                )
                .build(blockPath(EBlocks.SPINNING_WHEEL.get()), output);

        EntryBuilder.of("Poppet Shelf")
                .icon(EItems.POPPET_SHELF.get().getDefaultInstance())
                .assignedItems(EItems.POPPET_SHELF.get())
                .page(HeaderedTextBuilder.of("Poppet Shelf", FormattedStringBuilder.begin()
                        .then("A poppet shelf allows the ").boldCategoryLink("poppetry", "poppets")
                        .then(" held within it to be activated without being in a player's inventory.")
                        .paragraph("A single shelf can hold up to four poppets, belonging to any player.").toString()))
                .page(
                        HeaderBuilder.of(Modopedia.translation("template", "crafting_recipe")),
                        SeparatorBuilder.of().y(10),
                        MultiblockBuilder.of().multiblock(new DenseMultiblock(List.of(List.of("D")), Map.of('D', new SimpleStateMatcher(List.of(EBlocks.POPPET_SHELF.get().defaultBlockState())))))
                                .y(10).height(50),
                        CraftingRecipeBuilder.of(itemId(EItems.POPPET_SHELF.get())).y(70)
                )
                .build(blockPath(EBlocks.POPPET_SHELF.get()), output);

        EntryBuilder.of("Fume Collection")
                .icon(EItems.WITCH_OVEN.get().getDefaultInstance())
                .assignedItems(EItems.WITCH_OVEN.get(), EItems.FUME_FUNNEL.get(), EItems.FUME_FUNNEL_FILTERED.get(), EItems.FUME_FILTER.get())
                .page(HeaderedTextBuilder.of("Fume Collection", FormattedStringBuilder.begin()
                        .then("The witch's oven is a modified furnace used to collect byproducts and fumes made from burning materials.")
                        .paragraph("Unlike a furnace, this oven cannot process ores. Place a ").boldEntryLink(itemPath(EItems.CLAY_JAR.get()), "clay jar").then(" into the middle slot to collect fumes.").toString())
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
                .page(MultiblockPageBuilder.of("A third fume funnel can be placed on top of the witch oven as a decorative chimney.")
                        .multiblockId(Enchanted.id("witch_oven"))
                        .offsetY(-10)
                )
                .page(
                        HeaderedTextBuilder.of("Fume Filters", "A fume filter can be added to fume funnels to further increase yield. With two filters, success is guaranteed."),
                        CraftingRecipeBuilder.of(itemId(EItems.FUME_FILTER.get())).y(70)
                )
                .page(BlockPageBuilder.of(FormattedStringBuilder.begin()
                        .bold("Procurement:").linebreak("Use a fume filter on a fume funnel.").toString(),
                        EBlocks.FUME_FUNNEL_FILTERED.get().defaultBlockState().setValue(FumeFunnelBlock.LIT, true))
                )
                .build(blockPath(EBlocks.WITCH_OVEN.get()), output);

        EntryBuilder.of("The Kettle")
                .icon(EItems.KETTLE.get().getDefaultInstance())
                .assignedItems(EItems.KETTLE.get())
                .page(
                        HeaderedTextBuilder.of("Kettle", "The kettle, used for creating infusions, is a crucial utensil for any witch."),
                        CraftingRecipeBuilder.of(itemId(EItems.KETTLE.get())).y(65)
                )
                .page(MultiblockBuilder.of()
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
                .page(BlockPageBuilder.of(FormattedStringBuilder.begin()
                        .bold("Procurement:").linebreak("Use a anointing paste on a cauldron").toString(),
                        EBlocks.WITCH_CAULDRON.get().defaultBlockState())
                )
                .build(blockPath(EBlocks.WITCH_CAULDRON.get()), output);
    }

    public void buildHerbologyBlockEntries(BookContentOutput output) {
        blockEntry(output, "Belladonna", FormattedStringBuilder.begin()
                        .then("Atropa bella-donna, commonly known as deadly nightshade, is a poisonous member of the Solanaceae family.")
                        .paragraph("The berries and foliage of this plant are extremely toxic and should be handled with care.").toString(),
                EBlocks.BELLADONNA.get(), procureGrass(), EItems.BELLADONNA_FLOWER.get(), EItems.BELLADONNA_SEEDS.get());

        blockEntry(output, "Garlic", FormattedStringBuilder.begin()
                .then("Garlic, scientific name allium sativum, is a dietary staple in many households.")
                .paragraph("Vampires are said to dislike garlic but its effectiveness as a repellent is questionable.").toString(),
                EBlocks.GARLIC.get(), procureGrass(), EItems.GARLIC.get());


        blockEntry(output, "Mandrakes", FormattedStringBuilder.begin()
                        .then("Mandrakes are a group of. perennial herbaceous plants with long, parsnip shaped roots.")
                        .paragraph("If agitated, mandrakes will scream causing injury or even death. Wearing ")
                        .boldEntryLink(itemPath(EItems.EARMUFFS.get()), "earmuffs").then(" can protect you, and they sleep at night.").toString(),
                EBlocks.MANDRAKE.get(), procureGrass(), EItems.MANDRAKE_ROOT.get(), EItems.MANDRAKE_SEEDS.get()
        );

        blockEntry(output, "Snowbell", FormattedStringBuilder.begin()
                        .then("Styrax japonicus, more commonly referred to as Snowbell, is a shrub from the Styracaceae family.")
                        .paragraph("Despite the name, they are native to warm climates in Asia. Its resin is used for purification, dispelling anger or soothing tension.").toString(),
                EBlocks.SNOWBELL.get(), procureGrass(), EItems.ICY_NEEDLE.get(), EItems.SNOWBELL_SEEDS.get()
        );

        blockEntry(output, "Water Artichoke", FormattedStringBuilder.begin()
                        .then("This subspecies of the common Artichoke, or cynara cardunculus, only grows on still water.")
                        .paragraph("Unlike it's green cousin, it is not considered edible. Consumption of this plant will satiate hunger but empty your stomach.").toString(),
                EBlocks.WATER_ARTICHOKE.get(), procureGrass(), EItems.WATER_ARTICHOKE.get(), EItems.WATER_ARTICHOKE_SEEDS.get()
        );

        blockEntry(output, "Wolfsbane", FormattedStringBuilder.begin()
                        .then("Aconitum, common name of Wolfsbane, is a perennial flower of the Ranunculaceae family.")
                        .paragraph("Its roots contain aconitine, a potent neurotoxin and cardiotoxin. Contrary to popular belief, the name is just a translation from greek.").toString(),
                EBlocks.WOLFSBANE.get(), procureGrass(), EItems.WOLFSBANE_FLOWER.get(), EItems.WOLFSBANE_SEEDS.get()
        );

        mutatedEntry(output, "Glint Weed", FormattedStringBuilder.begin()
                        .then("This magical weed emits a glow around it, acting like a torch. ")
                        .then("While it can survive on nearly any surface, if placed on grass, dirt or sand it will spread.").toString(),
                EBlocks.GLINT_WEED.get(), EItems.GLINT_WEED.get()
        );

        mutatedEntry(output, "Ember Moss", FormattedStringBuilder.begin()
                        .then("Ember moss is a plant with a very unique defense mechanism; it bursts into flames at the slightest touch or disturbance. ")
                        .then("Harvest with shears to be keep intact.").toString(),
                EBlocks.EMBER_MOSS.get(), EItems.EMBER_MOSS.get()
        );

        mutatedEntry(output, "Spanish Moss", FormattedStringBuilder.begin()
                        .then("An epiphytic flowering plant, similar to a moss or lichen, found growing on trees in tropical or subtropical climates. ")
                        .then("Harvest with shears to be keep intact.").toString(),
                EBlocks.SPANISH_MOSS.get(), EItems.SPANISH_MOSS.get()
        );

        mutatedEntry(output, "Blood Poppy", FormattedStringBuilder.begin()
                        .then("Poppies famously thrive in ground stained with blood, with a mutation they can also draw blood from those who touch them. ")
                        .then("Right click with a ").boldEntryLink(itemPath(EItems.TAGLOCK.get()), "taglock kit").then(" to collect blood.").toString(),
                EBlocks.BLOOD_POPPY.get(), EItems.BLOOD_POPPY.get()
        );

        EntryBuilder.of("Alder Trees")
                .icon(EItems.ALDER_SAPLING.get().getDefaultInstance())
                .assignedItems(EItems.ALDER_SAPLING.get(), EItems.ALDER_LOG.get(), EItems.STRIPPED_ALDER_LOG.get(),
                        EItems.ALDER_PLANKS.get(), EItems.ALDER_STAIRS.get(), EItems.ALDER_SLAB.get(), EItems.ALDER_FENCE.get(),
                        EItems.ALDER_FENCE_GATE.get(), EItems.ALDER_BUTTON.get(), EItems.ALDER_PRESSURE_PLATE.get())
                .page(
                        HeaderedTextBuilder.of("Alder Trees", FormattedStringBuilder.begin()
                                .then("Alder trees, of the Betulaceae family, are deciduous trees thought to bleed when cut due to their red sap.").toString()),
                        GalleryBuilder.of(
                                MultiblockBuilder.of()
                                        .multiblockId(Enchanted.id("alder_tree"))
                                        .height(80),
                                MultiblockBuilder.of()
                                        .multiblock(new DenseMultiblock(
                                                List.of(List.of("S")),
                                                Map.of('S', new SimpleStateMatcher(List.of(EBlocks.ALDER_SAPLING.get().defaultBlockState())))
                                        ))
                                        .noOffsets(true)
                        ).y(54).height(80)
                )
                .page(MutagenPageBuilder.of(EBlocks.ALDER_SAPLING.get()))
                .build(blockPath(EBlocks.ALDER_SAPLING.get()), output);

        EntryBuilder.of("Hawthorn Trees")
                .icon(EItems.HAWTHORN_SAPLING.get().getDefaultInstance())
                .assignedItems(EItems.HAWTHORN_SAPLING.get(), EItems.HAWTHORN_LOG.get(), EItems.STRIPPED_HAWTHORN_LOG.get(),
                        EItems.HAWTHORN_PLANKS.get(), EItems.HAWTHORN_STAIRS.get(), EItems.HAWTHORN_SLAB.get(), EItems.HAWTHORN_FENCE.get(),
                        EItems.HAWTHORN_FENCE_GATE.get(), EItems.HAWTHORN_BUTTON.get(), EItems.HAWTHORN_PRESSURE_PLATE.get())
                .page(
                        HeaderedTextBuilder.of("Hawthorn Trees", FormattedStringBuilder.begin()
                                .then("This species of crataegus, commonly referred to as Hawthorn, has herbal uses and is known to be effective at dispatching vampires.").toString()),
                        GalleryBuilder.of(
                                MultiblockBuilder.of()
                                        .multiblockId(Enchanted.id("hawthorn_tree"))
                                        .height(80),
                                MultiblockBuilder.of()
                                        .multiblock(new DenseMultiblock(
                                                List.of(List.of("S")),
                                                Map.of('S', new SimpleStateMatcher(List.of(EBlocks.HAWTHORN_SAPLING.get().defaultBlockState())))
                                        ))
                                        .noOffsets(true)
                        ).y(54).height(80)
                )
                .page(MutagenPageBuilder.of(EBlocks.HAWTHORN_SAPLING.get()))
                .build(blockPath(EBlocks.HAWTHORN_SAPLING.get()), output);

        EntryBuilder.of("Rowan Trees")
                .icon(EItems.ROWAN_SAPLING.get().getDefaultInstance())
                .assignedItems(EItems.ROWAN_BERRIES.get(), EItems.ROWAN_SAPLING.get(), EItems.ROWAN_LOG.get(),
                        EItems.STRIPPED_ROWAN_LOG.get(), EItems.ROWAN_PLANKS.get(), EItems.ROWAN_STAIRS.get(),
                        EItems.ROWAN_SLAB.get(), EItems.ROWAN_FENCE.get(), EItems.ROWAN_FENCE_GATE.get(),
                        EItems.ROWAN_BUTTON.get(), EItems.ROWAN_PRESSURE_PLATE.get())
                .page(
                        HeaderedTextBuilder.of("Rowan Trees", FormattedStringBuilder.begin()
                                .then("The rowan, or mountain-ash, a small deciduous tree native to Europe, has many uses in witchcraft.").toString()),
                        GalleryBuilder.of(
                                MultiblockBuilder.of()
                                        .multiblockId(Enchanted.id("rowan_tree"))
                                        .height(80),
                                MultiblockBuilder.of()
                                        .multiblock(new DenseMultiblock(
                                                List.of(List.of("S")),
                                                Map.of('S', new SimpleStateMatcher(List.of(EBlocks.ROWAN_SAPLING.get().defaultBlockState())))
                                        ))
                                        .noOffsets(true)
                        ).y(54).height(80)
                )
                .page(MutagenPageBuilder.of(EBlocks.ROWAN_SAPLING.get()))
                .build(blockPath(EBlocks.ROWAN_SAPLING.get()), output);
    }

    public void buildGettingStartedEntries(BookContentOutput output) {
        EntryBuilder.of("Chalice Upgrades")
                .icon(EItems.CHALICE_FILLED.get().getDefaultInstance())
                .assignedItems(EItems.CHALICE_FILLED.get(), EItems.CHALICE.get())
                .page(
                        HeaderedTextBuilder.of("Chalices", FormattedStringBuilder.begin()
                                .then("A chalice may be placed on top of an altar to increase its power capacity. The chalice can be filled by using ")
                                .boldEntryLink(itemPath(EItems.REDSTONE_SOUP.get()), "Redstone Soup").then(" on it.").toString()),
                        CraftingRecipeBuilder.of(Enchanted.id("chalice")).y(70)
                )
                .page(GalleryBuilder.of(
                        BlockPageBuilder.of("$(b)Chalice:$()\n+1x Power Capacity", EBlocks.CHALICE.get().defaultBlockState())
                                .scale(2.0F).offsetY(-20),
                        BlockPageBuilder.of("$(b)Chalice (Filled):$()\n+2x Power Capacity", EBlocks.CHALICE_FILLED.get().defaultBlockState())
                                .scale(2.0F).offsetY(-20))
                )
                .build(gettingStartedPath("chalice_upgrades"), output);

        EntryBuilder.of("Skull Upgrades")
                .icon(Items.SKELETON_SKULL.getDefaultInstance())
                .page(HeaderedTextBuilder.of("Skulls", FormattedStringBuilder.begin()
                        .then("Skulls can increase both the capacity and recharge rate of an altar, with varying effectiveness.")
                        .paragraph("Human skulls are particularly effective at channeling energy.").toString())
                )
                .page(GalleryBuilder.of(
                        BlockPageBuilder.of("$(b)Skeleton Skull:$()\n+1x Power Capacity\n+1x Recharge Rate", Blocks.SKELETON_SKULL.defaultBlockState())
                                .scale(1.6F).offsetY(-20).textOffset(-25),
                        BlockPageBuilder.of("$(b)Wither Skeleton Skull:$()\n+2x Power Capacity\n+2x Recharge Rate", Blocks.WITHER_SKELETON_SKULL.defaultBlockState())
                                .scale(1.6F).offsetY(-20).textOffset(-25),
                        BlockPageBuilder.of("$(b)Player Skull:$()\n+2.5x Power Capacity\n+3x Recharge Rate", Blocks.PLAYER_HEAD.defaultBlockState())
                                .scale(1.6F).offsetY(-20).textOffset(-25))
                )
                .build(gettingStartedPath("skull_upgrades"), output);

        EntryBuilder.of("Torch Upgrades")
                .icon(EItems.CANDELABRA.get().getDefaultInstance())
                .assignedItems(EItems.CANDELABRA.get())
                .page(
                        HeaderedTextBuilder.of("Torches", "A torch, candelabra or candle can be an effective means of increasing your altar's recharge rate."),
                        CraftingRecipeBuilder.of(Enchanted.id("candelabra")).y(70)
                )
                .page(GalleryBuilder.of(
                        BlockPageBuilder.of("$(b)Torch:$()\n+0.5x Recharge Rate", Blocks.TORCH.defaultBlockState())
                                .scale(1.8F).offsetY(-15).textOffset(-30),
                        BlockPageBuilder.of("$(b)Candle:$()\n+1x Recharge Rate", Blocks.LIGHT_GRAY_CANDLE.defaultBlockState())
                                .scale(2.0F).offsetY(-20).textOffset(-30),
                        BlockPageBuilder.of("$(b)Candelabra:$()\n+2x Recharge Rate", EBlocks.CANDELABRA.get().defaultBlockState())
                                .scale(1.75F).offsetY(-15).textOffset(-30))
                )
                .build(gettingStartedPath("torch_upgrades"), output);
    }

    public void buildBrewingEntries(BookContentOutput output) {
        EntryBuilder.of("Creating Brews")
                .icon(EItems.WITCH_CAULDRON.get().getDefaultInstance())
                .page(HeaderedTextBuilder.of("Brews and Potions", FormattedStringBuilder.begin()
                        .then("Creating a brew, infusion or potion is a simple process, requiring either a ")
                        .boldEntryLink(blockPath(EBlocks.WITCH_CAULDRON.get()), "witch's cauldron", 0x582C69).then(" or ").boldEntryLink(blockPath(EBlocks.KETTLE.get()), "kettle", 0x582C69).then(".")
                        .paragraph("First, you must fill your ")
                        .boldEntryLink(blockPath(EBlocks.WITCH_CAULDRON.get()), "cauldron").then(" or ").boldEntryLink(blockPath(EBlocks.KETTLE.get()), "kettle")
                        .then(" with water from a bucket. You will know it is full if no more water can fit.").toString())
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
                .page(HeaderedTextBuilder.of("Adding Ingredients", FormattedStringBuilder.begin()
                        .then("The final step, after filling the vessel and waiting for it to boil, is to drop your ingredients into the vessel, ").bold("in order").then(", one by one")
                        .paragraph("The water should start to change colour. Once added, the brew needs to boil for a few seconds to finish.").toString())
                )
                .page(HeaderedTextBuilder.of("Spoiled Brews", FormattedStringBuilder.begin()
                        .then("If you make a mistake during brewing, such as adding the wrong ingredient or removing the heat source, your brew will ").bold("spoil").then(".")
                        .paragraph("When spoiled, the brew will turn brown and need to be removed using an ").bold("empty bucket").then(".").toString())
                )
                .build(brewingPath("brewing"), output);
    }

    public void buildHerbologyEntries(BookContentOutput output) {
        EntryBuilder.of("Mutations")
                .icon(Items.CHORUS_FRUIT.getDefaultInstance())
                .page(HeaderedTextBuilder.of("Mutations", FormattedStringBuilder.begin()
                        .then("While many plants are naturally occurring, herbology often requires witches to mutate existing plants to suit their needs.")
                        .paragraph("This is done using ").boldEntryLink(itemPath(EItems.MUTANDIS.get()), "mutandis", 0x582C69).then(" or ")
                        .boldEntryLink(itemPath(EItems.MUTANDIS_EXTREMIS.get()), "mutandis extremis", 0x582C69).then(".").toString()))
                .page(
                        HeaderedTextBuilder.of("Mutandis", FormattedStringBuilder.begin()
                                .then("Using mutandis on a block will make it start ").bold("mutating")
                                .then(", giving it a chance to turn into a different block based on nearby ")
                                .bold("mutagens").then(".").toString()),
                        FramedImageBuilder.of(Enchanted.id("textures/gui/modopedia/mutation.png"))
                                .x(10).y(75)
                )
                .page(HeaderedTextBuilder.of("Mutagens", FormattedStringBuilder.begin()
                        .bold("Mutagens").then(" are the blocks a witch needs to place in the area surrounding a mutating plant. The more mutagens you place, the more likely it is for the plant to mutate.")
                        .paragraph("The block in the center is the plant being mutated, while the surrounding blocks are its mutagens.").toString()))
                .page(MutagenPageBuilder.of(EBlocks.ROWAN_SAPLING.get()))
                .build(herbologyPath("mutations"), output);
    }

    public void buildCircleMagicEntries(BookContentOutput output) {
        EntryBuilder.of("Performing Rites")
                .icon(EItems.CIRCLE_TALISMAN.get().getDefaultInstance())
                .page(
                        HeaderedTextBuilder.of("Performing Rites", FormattedStringBuilder.begin()
                                .then("To perform circle magic, there are three basic steps a witch must follow; drawing the correct glyphs, adding foci and lastly, activating the rite.").toString()),
                        FramedImageBuilder.of(Enchanted.id("textures/gui/modopedia/circle_magic.png")).x(10).y(80)
                )
                .page(
                        HeaderedTextBuilder.of("Heart Glyphs", FormattedStringBuilder.begin()
                                .then("Every rite starts with a heart glyph, a symbol drawn with ").boldEntryLink(itemPath(EItems.GOLDEN_CHALK.get()), "golden chalk").then(".")
                                .paragraph("This is the center of the rite, acting as a conduit for power and being used to activate or deactivate the magic.").toString()),
                        MultiblockBuilder.of()
                                .y(80)
                                .height(50)
                                .multiblock(new DenseMultiblock(
                                        List.of(List.of("G")),
                                        Map.of('G', new SimpleStateMatcher(List.of(EBlocks.GOLDEN_CHALK.get().defaultBlockState())))
                                ))
                )
                .page(HeaderedTextBuilder.of("Glyphs and Foci", FormattedStringBuilder.begin()
                        .then("After a heart glyph, rites need chalk circles, drawn using ")
                        .boldEntryLink(itemPath(EItems.RITUAL_CHALK.get()), "ritual").then(", ")
                        .boldEntryLink(itemPath(EItems.NETHER_CHALK.get()), "infernal").then(" or ")
                        .boldEntryLink(itemPath(EItems.OTHERWHERE_CHALK.get()), "otherwhere").then(" chalk. ")
                        .paragraph("Foci, such as items living sacrifices, should be placed within the circles.")
                        .paragraph("When all requirements are met, the heart glyph can be activated.").toString())
                )
                .page(RitePageBuilder.of(Enchanted.id("charging_stone")))
                .build("circle_magic/tutorial/performing_rites", output);

        riteEntry(output, "Charging Stones", EItems.ATTUNED_STONE_CHARGED.get(), EItems.ATTUNED_STONE_CHARGED.get(), FormattedStringBuilder.begin()
                        .then("Gather power to imbue an attuned stone with magical energy.")
                        .paragraph("This stone can be used for various purposes such as substituting an altar during circle magic rites.").toString()
                , "charging_stone");

        riteEntry(output, "Broomsticks", EItems.ENCHANTED_BROOMSTICK.get(), EItems.ENCHANTED_BROOMSTICK.get(), FormattedStringBuilder.begin()
                        .then("The humble ").boldEntryLink(itemPath(EItems.BROOM.get()), "broomstick").then(" can be imbued with the power of flight using ")
                        .boldEntryLink(itemPath(EItems.FLYING_OINTMENT.get()), "flying ointment").then(", creating a classic and effective means of transportation.")
                        .paragraph("The witches association recommends practicing at low altitudes.").toString(),
                "infuse_broom");

        riteEntry(output, "Familiars", EItems.ARTHANA.get(), FormattedStringBuilder.begin()
                        .then("Bind a familiar to yourself. This companion provides a variety of benefits depending on what type of creature it is. The tamed animal must be within the circle.")
                        .paragraph("The ").boldCategoryLink("familiars", "familiars").then(" chapter discusses this in greater detail.").toString(),
                "bind_familiar");

        riteEntry(output, "Talisman", EItems.CIRCLE_TALISMAN.get(), FormattedStringBuilder.begin()
                        .boldEntryLink(itemPath(EItems.CIRCLE_TALISMAN.get()), "Circle talismans").then(" can be used to easily pick up and move glyphs, reducing wasted chalk.")
                        .paragraph("The bound talisman can be used on the ground to place the glyphs it holds.")
                        .paragraph("Works with any and all glyphs.").toString(),
                "bind_talisman", "bind_talisman_charged");

        riteEntry(output, "Waystone", EItems.BOUND_WAYSTONE.get(), EItems.BOUND_WAYSTONE.get(), FormattedStringBuilder.begin()
                        .then("A ").boldEntryLink(itemPath(EItems.WAYSTONE.get()), "waystone").then(" can be bound to a location to represent it in other rites, most notably in ")
                        .boldCategoryLink("circle_magic/translocation", "translocation rites.")
                        .paragraph("The waystone will be bound to the location the rite is cast at, or duplicated from an existing waystone.").toString(),
                "waystone", "waystone_charged", "duplicate_waystone", "duplicate_waystone_charged");

        riteEntry(output, "Blooded Waystone", EItems.BLOODED_WAYSTONE.get(), EItems.BLOODED_WAYSTONE.get(), FormattedStringBuilder.begin()
                        .then("A blooded ").boldEntryLink(itemPath(EItems.WAYSTONE.get()), "waystone")
                        .then(", similar to a regular waystone, is bound to a location. The difference is that it is bound to the location of an entity.")
                        .paragraph("Unlike regular waystones, blooded waystones cannot be duplicated.").toString(),
                "waystone_blooded", "waystone_blooded_charged");

        EntryBuilder.of("Casting Curses")
                .icon(Items.WITHER_ROSE.getDefaultInstance())
                .page(HeaderedTextBuilder.of("Casting Curses", FormattedStringBuilder.begin()
                        .then("Curses at their core are the same as any other circle magic, however the level of the curse is increased by the skill of the caster.")
                        .paragraph("Witches with a cat familiar or those who perform the rite with a coven will be more skilled at cursing others.").toString()))
                .page(HeaderedTextBuilder.of("Removing Curses", FormattedStringBuilder.begin()
                        .then("To remove a curse, a witch must perform the appropriate cleansing rite.")
                        .paragraph("Cleanses have a level raised in the same way as curses, making them more likely to succeed or fail. Failed removals will only make a curse stronger.").toString()))
                .build("circle_magic/curses/casting_curses", output);

        riteEntry(output, "Blight", Items.SPIDER_EYE, FormattedStringBuilder.begin()
                    .then("Cause plants within a large area to wither, grass and dirt to decay, make animals sick and turn villagers into zombies.").toString(),
                "blight");

        riteEntry(output, "Misfortune", Items.ENDER_EYE, FormattedStringBuilder.begin()
                    .then("Curse the afflicted to become prone to occasionally gaining an unfortunate effect such as weakness or mining fatigue.")
                    .paragraph("The strength and duration of effects increases with the level of the curse.").toString(),
                "curse_misfortune");

        riteEntry(output, "Overheating", Items.BLAZE_POWDER, FormattedStringBuilder.begin()
                        .then("Curse the afflicted to periodically overheat and catch fire while in hot biomes.")
                        .paragraph("The duration of the flames increases with the level of the curse.").toString(),
                "curse_overheating");

        riteEntry(output, "Sinking", Items.WATER_BUCKET, FormattedStringBuilder.begin()
                        .then("Curse the afflicted to become heavier in both water and air, making it difficult to swim or fly.")
                        .paragraph("The weight of the target will increase with the level of the curse.").toString(),
                "curse_sinking");

        riteEntry(output, "Cleanse Misfortune", Items.ENDER_EYE, FormattedStringBuilder.begin()
                        .then("Cleanse the targeted being of instances of misfortune.")
                        .paragraph("If removal fails, it will bring even greater misfortune on the target.").toString(),
                "remove_misfortune");

        riteEntry(output, "Cleanse Overheating", Items.BLAZE_POWDER, FormattedStringBuilder.begin()
                        .then("Cleanse a being of their tendency to overheat in certain biomes")
                        .paragraph("If the cleansing fails, the target will grow even hotter.").toString(),
                "remove_overheating");

        riteEntry(output, "Cleanse Sinking", Items.WATER_BUCKET, FormattedStringBuilder.begin()
                        .then("Assist the afflicted in losing weight, allowing them to swim and fly as normal.")
                        .paragraph("Failing this rite will add more weight to the afflicted's shoulders.").toString(),
                "remove_sinking");

        riteEntry(output, "Broiling", Items.BEEF, FormattedStringBuilder.begin()
                        .then("Summon a ring of flames to rapidly cook all raw foods dropped within the circle.")
                        .paragraph("The heat is so intense that some food may be incinerated.").toString(),
                "broiling");

        riteEntry(output, "Fertility", Items.EMERALD, FormattedStringBuilder.begin()
                        .then("A large scale rejuvenation rite which causes plants to grow, heals sick animals and cures zombified villagers.")
                        .paragraph("The witches association recommends against liberal use of the rite of fertility following the overgrowth it leaves behind.").toString(),
                "fertility", "fertility_charged");

        riteEntry(output, "Forest", Items.OAK_SAPLING, FormattedStringBuilder.begin()
                        .then("Grow a large sea of trees around the circle, sprouting a forest even in the most desolate conditions.")
                        .paragraph("A sapling must be dropped within the circle to determine which type of tree to grow.").toString(),
                "forest");

        riteEntry(output, "Sky's Wrath", Items.LIGHTNING_ROD, FormattedStringBuilder.begin()
                        .then("Summon a concentrated thunderstorm around the edges of the circle, causing lighting to strike.")
                        .paragraph("Adding a ").boldEntryLink(ritePath("waystone"), "bound").then(" or ")
                        .boldEntryLink(ritePath("waystone_blooded"), "blooded")
                        .then(" waystone allows the caster to direct the lightning to that location.").toString(),
                "sky_wrath", "sky_wrath_charged", "sky_wrath_waystone", "sky_wrath_waystone_charged", "sky_wrath_blooded",
                "sky_wrath_blooded_charged");

        riteEntry(output, "Total Eclipse", Items.CLOCK, FormattedStringBuilder.begin()
                        .then("Summon the moon to cover the sky with darkness and blot out the sun.")
                        .paragraph("This rite can be useful for witches who need to perform a different magic at night.").toString(),
                "total_eclipse", "total_eclipse_charged");

        riteEntry(output, "Transpose Iron", Items.RAW_IRON, FormattedStringBuilder.begin()
                        .then("Transpose all useful iron from underneath the circle to the surface, leaving the stone behind.")
                        .paragraph("The rite will consume all ores where it is placed and must be moved after each use.").toString(),
                "transpose_iron");

        riteEntry(output, "Imprisonment", Items.IRON_BARS, FormattedStringBuilder.begin()
                        .then("Create a cage of fire, trapping monsters within the circle.")
                        .paragraph("While powerful, this barrier is certainly not impenetrable. Projectiles and teleportation can still break through it.").toString(),
                "imprisonment");

        riteEntry(output, "Protection", Items.SHIELD, FormattedStringBuilder.begin()
                        .then("Summon an impenetrable dome which can only be passed through by sneaking players.")
                        .paragraph("Adding a ").boldEntryLink(ritePath("waystone"), "bound waystone")
                        .then(" allows the caster to summon the dome at a different location.").toString(),
                "protection", "protection_large", "protection_waystone", "protection_large_waystone");

        riteEntry(output, "Temporary Protection", Items.SHULKER_SHELL, FormattedStringBuilder.begin()
                        .then("Summon an impenetrable dome which cannot be passed through by anything for one minute.")
                        .paragraph("Adding a ").boldEntryLink(ritePath("waystone"), "bound").then(" or ")
                        .boldEntryLink(ritePath("waystone_blooded"), "blooded")
                        .then(" waystone allows the caster to summon the dome at a different location.").toString(),
                "protection_temporary", "protection_temporary_waystone", "protection_temporary_blooded");

        riteEntry(output, "Sanctity", Items.FEATHER, FormattedStringBuilder.begin()
                        .then("Create a place of refuge within the circle, pushing mobs away and preventing them from entering.")
                        .paragraph("While strong, this barrier is not impenetrable. Projectiles and teleportation can still break through it.").toString(),
                "sanctity");

        riteEntry(output, "Summon Entity", Items.CHORUS_FRUIT, FormattedStringBuilder.begin()
                        .then("Summon the taglocked being, causing them to teleport to the casting circle. Those wearing witch hunter clothing may be unaffected.")
                        .paragraph("Unloaded or despawned entities cannot be summoned.").toString(),
                "summon_entity");

        riteEntry(output, "Summon Familiar", EItems.ARTHANA.get(), FormattedStringBuilder.begin()
                        .then("Summon the familiar bound to the caster of the rite, causing them to teleport to the casting circle.")
                        .paragraph("This rite is capable of summoning dismissed or deceased familiars.").toString(),
                "summon_familiar");

        riteEntry(output, "Transpose Caster", Items.ENDER_PEARL, FormattedStringBuilder.begin()
                        .then("Teleports the caster of the rite to the location contained within a ")
                        .boldEntryLink(ritePath("circle_magic/binding/waystone"), "bound").then(" or ")
                        .boldEntryLink(ritePath("circle_magic/binding/waystone_blooded"), "blooded").then(" waystone.")
                        .paragraph("The witches association has reported occasional instances of parts being left behind.").toString(),
                "transpose_caster", "transpose_caster_blooded");
    }

    public void buildPoppetryEntries(BookContentOutput output) {
        EntryBuilder.of("Poppet Theory")
                .icon(EItems.SPINNING_WHEEL.get().getDefaultInstance())
                .assignedItems(EItems.POPPET.get())
                .page(HeaderedTextBuilder.of("Poppetry", FormattedStringBuilder.begin()
                        .then("Poppetry is the art of creating dolls, weaving magical materials into them using a ")
                        .boldEntryLink(blockPath(EBlocks.SPINNING_WHEEL.get()), "spinning wheel", 0x582C69)
                        .then(" and binding them to a living being to protect or harm them.")
                        .paragraph("For a poppet to work, it needs to be in the target being's inventory or in a")
                        .boldEntryLink(blockPath(EBlocks.POPPET_SHELF.get()), "poppet shelf").then(".").toString()))
                .page(
                        HeaderedTextBuilder.of("Creating Poppets", FormattedStringBuilder.begin()
                                .then("Poppets need to be weaved with other materials using a ").boldEntryLink(blockPath(EBlocks.SPINNING_WHEEL.get()), "spinning wheel")
                                .then(" after they have been crafted to gain an effect.").toString()),
                        CraftingRecipeBuilder.of(itemId(EItems.POPPET.get())).y(69)
                )
                .build(itemPath(EItems.POPPET.get()), output);

        spinningEntry(output, "Sturdy Poppets", FormattedStringBuilder.begin()
                        .then("A ").bold("sturdy poppet").then(" is an upgraded ").boldEntryLink(itemPath(EItems.POPPET.get()), "poppet")
                        .then(" which provides additional reinforcement.")
                        .paragraph("Sturdy poppets will be able to withstand being used twice, rather than breaking after a single use.").toString(),
                EItems.POPPET_STURDY.get());

        spinningEntry(output, "Infused Poppets", FormattedStringBuilder.begin()
                        .then("An ").bold("infused poppet").then(" is an upgraded ").boldEntryLink(itemPath(EItems.POPPET.get()), "poppet")
                        .then(" capable of amplifying the effects of the materials woven into it.")
                        .paragraph("Infused poppets will have an additional effect applied to them when triggered.").toString(),
                EItems.POPPET_INFUSED.get());

        spinningEntry(output, "Armour", FormattedStringBuilder.begin()
                        .then("Protects a piece of wearable equipment belonging to the beneficiary when it breaks, restoring some of its durability.")
                        .paragraph("If infused, the item is instead restored to full durability.").toString(),
                EItems.ARMOUR_POPPET.get(), EItems.ARMOUR_POPPET_INFUSED.get(), EItems.ARMOUR_POPPET_STURDY.get());

        spinningEntry(output, "Earth", FormattedStringBuilder.begin()
                        .then("If the beneficiary were to take enough fall damage to die, they will instead live on one health.")
                        .paragraph("When infused, the beneficiary will also gain ").bold().tooltip("Gives immunity to fall damage", "fall resistance").stopBold()
                        .then(" for ten seconds.").toString(),
                EItems.EARTH_POPPET.get(), EItems.EARTH_POPPET_INFUSED.get(), EItems.EARTH_POPPET_STURDY.get());

        spinningEntry(output, "Fire", FormattedStringBuilder.begin()
                        .then("When the beneficiary burns to death, they will instead remain at one health and the fire will be extinguished.")
                        .paragraph("If infused, fire poppets also grant ten seconds of fire resistance.").toString(),
                EItems.FIRE_POPPET.get(), EItems.FIRE_POPPET_INFUSED.get(), EItems.FIRE_POPPET_STURDY.get());

        spinningEntry(output, "Hunger", FormattedStringBuilder.begin()
                        .then("Protects the beneficiary from starving to death. Dying of starvation will instead fill the beneficiary's stomach.")
                        .paragraph("Infused hunger poppets grant two minutes of saturation five.").toString(),
                EItems.HUNGER_POPPET.get(), EItems.HUNGER_POPPET_INFUSED.get(), EItems.HUNGER_POPPET_STURDY.get());

        spinningEntry(output, "Magic", FormattedStringBuilder.begin()
                        .then("Taking fatal magic damage will instead cause the beneficiary to survive with one health.")
                        .paragraph("When infused, it provides ").bold().tooltip("Gives immunity to magic damage", "magic resistance").stopBold()
                        .then(" for ten seconds.").toString(),
                EItems.MAGIC_POPPET.get(), EItems.MAGIC_POPPET_INFUSED.get(), EItems.MAGIC_POPPET_STURDY.get());

        spinningEntry(output, "Tool", FormattedStringBuilder.begin()
                        .then("Protects a piece of handheld equipment from destruction when it breaks, restoring some of its durability.")
                        .paragraph("When infused, tool poppets will restore the tool to full durability.").toString(),
                EItems.TOOL_POPPET.get(), EItems.TOOL_POPPET_INFUSED.get(), EItems.TOOL_POPPET_STURDY.get());

        spinningEntry(output, "Void", FormattedStringBuilder.begin()
                        .then("If the beneficiary falls into the void, a void poppet will transport them into the sky instead.")
                        .paragraph("Infused void poppets will also grant ten seconds of ").bold().tooltip("Gives immunity to fall damage", "fall resistance").toString(),
                EItems.VOID_POPPET.get(), EItems.VOID_POPPET_INFUSED.get(), EItems.VOID_POPPET_STURDY.get());

        spinningEntry(output, "Voodoo", FormattedStringBuilder.begin()
                        .then("Unlike other poppets, voodoo poppets are an effigy used to transfer damage to their target.")
                        .paragraph("Burning a voodoo poppet in fire or lava, drowning it or poking needles into it will harm the target instead.").toString(),
                EItems.VOODOO_POPPET.get());

        spinningEntry(output, "Voodoo Protection", FormattedStringBuilder.begin()
                        .then("Protects the beneficiary from voodoo poppets. When affected by a voodoo poppet, the poppet will break instead.")
                        .paragraph("If infused, the holder of the poppet will also be struck by lightning.").toString(),
                EItems.VOODOO_PROTECTION_POPPET.get(), EItems.VOODOO_PROTECTION_POPPET_INFUSED.get(), EItems.VOODOO_PROTECTION_POPPET_STURDY.get());

        spinningEntry(output, "Water", FormattedStringBuilder.begin()
                        .then("Prevents the beneficiary from drowning to death, instead leaving them at one health and granting five seconds of ")
                        .bold().tooltip("Gives immunity to drowning damage", "drown resistance").stopBold()
                        .paragraph("Infused water poppets will provide ten seconds of water breathing instead of resistance.").toString(),
                EItems.WATER_POPPET.get(), EItems.WATER_POPPET_INFUSED.get(), EItems.WATER_POPPET_STURDY.get());
    }


    private void galleryEntry(BookContentOutput output, String title, Item item, String description, PageComponentBuilder... galleryComponents) {
        headeredTextEntry(title, description, item)
                .page(GalleryBuilder.of(galleryComponents))
                .build(itemPath(item), output);
    }

    private void craftingEntry(BookContentOutput output, String title, String description, Item item) {
        craftingEntry(output, title, description, item, item);
    }

    private void cookingEntry(BookContentOutput output, String title, String description, Item item) {
        cookingEntry(output, title, description, item, item);
    }

    /**
     * The first item should be the one the recipe is for. The others are just linked to the page.
     */
    private void craftingEntry(BookContentOutput output, String title, String description, Item item, Item... items) {
        headeredTextEntry(title, description, items)
                .page(CraftingPageBuilder.of(itemId(item)))
                .build(itemPath(item), output);
    }

    private void cookingEntry(BookContentOutput output, String title, String description, Item item, Item... items) {
        headeredTextEntry(title, description, items)
                .page(CookingPageBuilder.of(itemId(item)))
                .build(itemPath(item), output);
    }

    private void blockEntry(BookContentOutput output, String title, String description, Block block, String procurement, Item... items) {
        blockEntry(title, description, block.defaultBlockState(), procurement, items).build(blockPath(block), output);
    }

    private void mutatedEntry(BookContentOutput output, String title, String description, Block result, Item... items) {
        mutatedEntry(title, description, result, items).build(blockPath(result), output);
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

    private void spinningEntry(BookContentOutput output, String title, String description, Item... items) {
        EntryBuilder builder = EntryBuilder.of(title)
                .icon(items[0].getDefaultInstance())
                .assignedItems(items)
                .page(HeaderedTextBuilder.of(title, description));

        if(items.length > 1)
            builder.page(GalleryBuilder.of(Arrays.stream(items).map(i -> SpinningPageBuilder.of(spinningRecipe(i))).toArray(PageComponentBuilder[]::new)));
        else
            builder.page(SpinningPageBuilder.of(spinningRecipe(items[0])));

        builder.build(itemPath(items[0]), output);
    }

    private void riteEntry(BookContentOutput output, String title, Item icon, String description, String... rites) {
        riteEntry(title, icon, description, rites).build(ritePath(rites[0]), output);
    }

    private void riteEntry(BookContentOutput output, String title, Item icon, Item assigned, String description, String... rites) {
        riteEntry(title, icon, description, rites).assignedItems(assigned).build(ritePath(rites[0]), output);
    }

    private EntryBuilder riteEntry(String title, Item icon, String description, String... rites) {
        EntryBuilder builder = EntryBuilder.of(title)
                .icon(icon.getDefaultInstance())
                .page(HeaderedTextBuilder.of(title, description));

        if(rites.length > 1)
            builder.page(GalleryBuilder.of(Arrays.stream(rites).map(s -> RitePageBuilder.of(Enchanted.id(s))).toArray(PageComponentBuilder[]::new)));
        else
            builder.page(RitePageBuilder.of(Enchanted.id(rites[0])));

        return builder;
    }

    private EntryBuilder blockEntry(String title, String description, BlockState state, String blockDescription, Item... items) {
        return headeredTextEntry(title, description, items).page(BlockPageBuilder.of(blockDescription, state));
    }

    private EntryBuilder mutatedEntry(String title, String description, Block result, Item... items) {
        return EntryBuilder.of(title)
                .icon(items[0].getDefaultInstance())
                .assignedItems(items)
                .page(
                        HeaderedTextBuilder.of(title, description),
                        MultiblockBuilder.of()
                                .y(70)
                                .height(60)
                                .multiblock(new DenseMultiblock(
                                        List.of(List.of("S")),
                                        Map.of('S', new SimpleStateMatcher(List.of(result.defaultBlockState())))
                                ))
                                .noOffsets(true)
                )
                .page(MutagenPageBuilder.of(result));
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

    private String procureGrass() {
        return FormattedStringBuilder.begin().bold("Procurement:").linebreak("Dropped by tall and short grass").toString();
    }

    private String drops(String string) {
        return FormattedStringBuilder.begin().bold("Drops:").linebreak(string).toString();
    }



    private ResourceLocation itemId(Item item) {
        return BuiltInRegistries.ITEM.getKey(item);
    }

    private ResourceLocation spinningRecipe(Item item) {
        return Enchanted.id("spinning/" + itemId(item).getPath());
    }

    private String gettingStartedPath(String path) {
        return "getting_started/" + path;
    }

    private String brewingPath(String path) {
        return "brewing/" + path;
    }

    private String herbologyPath(String path) {
        return "herbology/" + path;
    }

    private String itemPath(Item item) {
        return "items/" + BuiltInRegistries.ITEM.getKey(item).getPath();
    }

    private String blockPath(Block block) {
        return "blocks/" + BuiltInRegistries.BLOCK.getKey(block).getPath();
    }

    private String ritePath(ResourceLocation rite) {
        return ritePath(rite.getPath());
    }

    private String ritePath(String rite) {
        return "circle_magic/rites/" + rite;
    }

    private String[] itemPaths(Item... items) {
        return Arrays.stream(items).map(this::itemPath).toArray(String[]::new);
    }

    private String[] blockPaths(Block... items) {
        return Arrays.stream(items).map(this::blockPath).toArray(String[]::new);
    }

    private String[] ritePaths(ResourceLocation... rites) {
        return Arrays.stream(rites).map(this::ritePath).toArray(String[]::new);
    }

    private String[] ritePaths(String... rites) {
        return Arrays.stream(rites).map(this::ritePath).toArray(String[]::new);
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