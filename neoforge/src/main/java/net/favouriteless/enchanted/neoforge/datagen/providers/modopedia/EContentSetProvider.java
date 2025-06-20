package net.favouriteless.enchanted.neoforge.datagen.providers.modopedia;

import com.google.gson.JsonElement;
import net.favouriteless.enchanted.api.datagen.builders.modopedia.templates.page.ByproductPageBuilder;
import net.favouriteless.enchanted.api.datagen.builders.modopedia.templates.page.DistilleryPageBuilder;
import net.favouriteless.enchanted.api.datagen.builders.modopedia.templates.page.DoubleByproductPageBuilder;
import net.favouriteless.enchanted.api.datagen.builders.modopedia.templates.recipe.KettleRecipeBuilder;
import net.favouriteless.enchanted.api.datagen.builders.modopedia.templates.recipe.WitchCauldronRecipeBuilder;
import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.enchanted.common.blocks.DistilleryBlock;
import net.favouriteless.enchanted.common.blocks.FumeFunnelBlock;
import net.favouriteless.enchanted.common.blocks.WitchOvenBlock;
import net.favouriteless.enchanted.common.init.EBlocks;
import net.favouriteless.enchanted.common.init.EItems;
import net.favouriteless.enchanted.common.init.ETags;
import net.favouriteless.modopedia.Modopedia;
import net.favouriteless.modopedia.api.book.Category;
import net.favouriteless.modopedia.api.datagen.builders.CategoryBuilder;
import net.favouriteless.modopedia.api.datagen.builders.EntryBuilder;
import net.favouriteless.modopedia.api.datagen.builders.PageComponentBuilder;
import net.favouriteless.modopedia.api.datagen.builders.page_components.components.GalleryBuilder;
import net.favouriteless.modopedia.api.datagen.builders.page_components.components.HeaderBuilder;
import net.favouriteless.modopedia.api.datagen.builders.page_components.components.MultiblockBuilder;
import net.favouriteless.modopedia.api.datagen.builders.page_components.components.SeparatorBuilder;
import net.favouriteless.modopedia.api.datagen.builders.templates.page.*;
import net.favouriteless.modopedia.api.datagen.builders.templates.recipes.CraftingRecipeBuilder;
import net.favouriteless.modopedia.api.datagen.providers.ContentSetProvider;
import net.favouriteless.modopedia.client.multiblock.DenseMultiblock;
import net.favouriteless.modopedia.client.multiblock.state_matchers.SimpleStateMatcher;
import net.favouriteless.modopedia.client.multiblock.state_matchers.TagStateMatcher;
import net.favouriteless.modopedia.datagen.builders.SingleRecipeTemplateBuilder;
import net.favouriteless.modopedia.datagen.builders.TemplateComponentBuilder;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class EContentSetProvider extends ContentSetProvider {

    public EContentSetProvider(CompletableFuture<Provider> registries, PackOutput output) {
        super(Enchanted.MOD_ID, "art_of_witchcraft", "en_us", registries, output);
    }

    @Override
    public void buildEntries(Provider provider, BiConsumer<String, JsonElement> output) {
        buildGettingStartedEntries(provider, output);
        buildHerbologyEntries(provider, output);
        buildExtractionEntries(provider, output);
        buildBrewingEntries(provider, output);
    }

    public void buildGettingStartedEntries(Provider provider, BiConsumer<String, JsonElement> output) {
        simpleCraftedItem("getting_started/attuned_stone", "Attuned Stones", """
                                An attuned stone is a diamond which has been infused with magical energy.
                                
                                It can be charged to be used as a portable container for $(b)$(el:getting_started/altars/altar_construction)altar power$(), allowing the user to cast circle magic without an altar being present, and is also used in the creation of a variety of magical tools.""",
                Enchanted.id("attuned_stone"), EItems.ATTUNED_STONE.get(), EItems.ATTUNED_STONE_CHARGED.get()).build(output);

        simpleCraftedItem("getting_started/bone_needle", "Bone Needles", """
                                Needles are an important part of many tools used for Witchcraft, most notably $(el:getting_started/taglocks)$(b)taglock kits$() and $(b)poppets$().
                                
                                A simple needle can be fashioned by whittling a bone.""",
                Enchanted.id("bone_needle"), EItems.BONE_NEEDLE.get()).build(output);

        simpleCraftedItem("getting_started/broom", "Brooms", """
                                A broom can be used to sweep chalk away quickly, without having to spend time erasing it.
                                
                                Conveniently, a broom can also make for an excellent method of transportation with some preparation.""",
                Enchanted.id("broom"), EItems.BROOM.get()).build(output);

        simpleCraftedItem("getting_started/earmuffs", "Earmuffs", """
                                When dealing with Mandrakes, Banshees and other loud creatures, protection for your ears is essential.
                                
                                $(b)Earmuffs$() can dampen deafening sounds, rendering them bearable.""",
                Enchanted.id("earmuffs"), EItems.EARMUFFS.get()).build(output);

        simpleCraftedItem("getting_started/taglocks", "Taglocks", """
                                Sometimes, you may need to represent another person to perform magic. This can be achieved using a $(b)taglock kit$().
                                
                                By using a taglock kit on a Player or their bed, you can collect a sample to use in magic.""",
                Enchanted.id("taglock_kit"), EItems.TAGLOCK_FILLED.get(), EItems.TAGLOCK.get()).build(output);

        EntryBuilder.of("getting_started/altars/altar_construction", "Creating an Altar")
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
                .build(output);

        EntryBuilder.of("getting_started/altars/chalice_upgrades", "Chalice Upgrades")
                .icon(EItems.CHALICE_FILLED.get().getDefaultInstance())
                .assignedItems(EItems.CHALICE_FILLED.get(), EItems.CHALICE.get())
                .page(
                        HeaderedTextBuilder.of("Chalices", """
                                A chalice may be placed on top of an altar to increase its power capacity. The chalice can be filled by using $(b)$(el:brewing/redstone_soup)Redstone Soup$() on it."""
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
                .build(output);

        EntryBuilder.of("getting_started/altars/skull_upgrades", "Skull Upgrades")
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
                                        .scale(1.75F).offsetY(-20).textOffset(-20),
                                BlockPageBuilder.of("$(b)Wither Skeleton Skull:$()\n+2x Power Capacity\n+2x Recharge Rate", Blocks.WITHER_SKELETON_SKULL.defaultBlockState())
                                        .scale(1.75F).offsetY(-20).textOffset(-20),
                                BlockPageBuilder.of("$(b)Player Skull:$()\n+2.5x Power Capacity\n+3x Recharge Rate", Blocks.PLAYER_HEAD.defaultBlockState())
                                        .scale(1.75F).offsetY(-20).textOffset(-20)
                        )
                )
                .build(output);

        EntryBuilder.of("getting_started/altars/torch_upgrades", "Torch Upgrades")
                .icon(EItems.CANDELABRA.get().getDefaultInstance())
                .assignedItems(EItems.CANDELABRA.get())
                .page(
                        HeaderedTextBuilder.of("Torches", """
                                A torch, candelabra or candle can be an effective means of increasing your altar's recharge rate."""
                        ),
                        CraftingRecipeBuilder.of(Enchanted.id("candelabra")).y(70)
                )
                .page(
                        GalleryBuilder.of(
                                BlockPageBuilder.of("$(b)Torch:$()\n+0.5x Recharge Rate", Blocks.TORCH.defaultBlockState())
                                        .scale(2.0F).offsetY(-20).textOffset(-30),
                                BlockPageBuilder.of("$(b)Candle:$()\n+1x Recharge Rate", Blocks.LIGHT_GRAY_CANDLE.defaultBlockState())
                                        .scale(2.0F).offsetY(-20).textOffset(-30),
                                BlockPageBuilder.of("$(b)Candelabra:$()\n+2x Recharge Rate", EBlocks.CANDELABRA.get().defaultBlockState())
                                        .scale(2.0F).offsetY(-20).textOffset(-30)
                        )
                )
                .build(output);

        EntryBuilder.of("getting_started/arthana", "Arthana")
                .icon(EItems.ARTHANA.get().getDefaultInstance())
                .assignedItems(EItems.ARTHANA.get(), EItems.TONGUE_OF_DOG.get(), EItems.WOOL_OF_BAT.get(), EItems.CREEPER_HEART.get())
                .page(
                        HeaderedTextBuilder.of("Arthana", """
                                The $(b)arthana$() is a ritual knife used for sacrifice. When used to kill certain mobs, they can drop rare magical materials you otherwise couldn't obtain."""
                        ),
                        CraftingRecipeBuilder.of(Enchanted.id("arthana")).y(70)
                )
                .page(
                        GalleryBuilder.of(
                                EntityPageBuilder.of(EntityType.BAT, "$(b)Drops:$()\nWool of Bat"),
                                EntityPageBuilder.of(EntityType.WOLF, "$(b)Drops:$()\nTongue of Dog"),
                                EntityPageBuilder.of(EntityType.CREEPER, "$(b)Drops:$()\nCreeper Heart")
                        )
                )
                .build(output);
    }

    public void buildHerbologyEntries(Provider provider, BiConsumer<String, JsonElement> output) {

    }

    public void buildExtractionEntries(Provider provider, BiConsumer<String, JsonElement> output) {
        simpleExtractionPage(EItems.BREATH_OF_THE_GODDESS.get(), "Breath of the Goddess", """
                Due to it's silvery appearance, birch is sometimes called "White Lady Of The Woods" and is associated with the goddess Brigid.
                
                The smoke produced by burning birch has healing properties.""",
                byproductPages("byproduct/breath_of_the_goddess_birch_sapling")
        ).build(output);

        simpleExtractionPage(EItems.DEMONIC_BLOOD.get(), "Demonic Blood", """
                        The blood of a demon has many uses in witchcraft. While usually obtained from demons, it can also be refined from several other materials.
                        
                        It's main uses are in infusions and curses.""",
                distillingPages("distilling/diamond_vapour_blaze_rod")
        ).build(output);

        simpleExtractionPage(EItems.DIAMOND_VAPOUR.get(), "Diamond Vapour", """
                        Diamonds, by using oil of vitriol, can be dissolved and then evaporated to form a powerful refining agent.
                        
                        Diamond vapour is required to distill some other materials.""",
                distillingPages("distilling/diamond_oil_of_vitriol")
        ).build(output);

        simpleExtractionPage(EItems.ENDER_DEW.get(), "Ender Dew", """
                        Ender pearls, which allow people to teleport, can be distilled into a purer form called Ender Dew.
                        
                        This substance contains the relocation properties of ender pearls in a liquid form, making it useful for brewing.""",
                distillingPages("distilling/ender_pearl")
        ).build(output);

        simpleExtractionPage(EItems.EXHALE_OF_THE_HORNED_ONE.get(), "Horned One's Exhale", """
                        The Oak King, one of the aspects of the Horned God, symbolises nature, hunting and the cycle of life.
                        
                        The fumes produced by oaks are said to be The Horned God's exhale, carrying some of the properties associated with him.""",
                byproductPages("byproduct/exhale_of_the_horned_one_oak_sapling")
        ).build(output);

        simpleExtractionPage(EItems.FOUL_FUME.get(), "Foul Fume", """
                        A foul smelling smoke containing sulfur, often smelled when encountering demonic beings or the nether.
                        
                        There are many plants, foods and other sources containing this gas.""",
                combine(
                        byproductPages("byproduct/foul_fume_jungle_sapling", "byproduct/foul_fume_logs_that_burn",
                                "byproduct/foul_fume_raw_foods"),
                        distillingPages("distilling/breath_of_the_goddess_lapis_lazuli",
                                "distilling/diamond_vapour_ghast_tear")
                )
        ).build(output);

        simpleExtractionPage(EItems.GYPSUM.get(), "Gypsum", """
                        A soft, translucent mineral salt produced by oxidising sulfides in the presence of quicklime.
                        
                        In addition to being an effective fertiliser, it is commonly used as a base for ritual chalks.""",
                distillingPages("distilling/foul_fume_quicklime")
        ).build(output);

        simpleExtractionPage(EItems.HINT_OF_REBIRTH.get(), "Hint of Rebirth", """
                        Spruce trees are associated with the birth of the divine child. As such, they are a symbol of rebirth, protection, resilience and endurance.
                        
                        Regardless, they make good firewood.""",
                byproductPages("byproduct/hint_of_rebirth_spruce_sapling")
        ).build(output);

        simpleExtractionPage(EItems.ODOUR_OF_PURITY.get(), "Odour of Purity", """
                        Hawthorn can be used to collect a powerful purifying agent called Odour of Purity, which is used in the creation of most pure substances.
                        
                        This tree is sacred to the Goddesses Aine and Brigid.""",
                combine(
                        byproductPages("byproduct/odour_of_purity_hawthorn_sapling"),
                        distillingPages("distilling/diamond_oil_of_vitriol", "distilling/diamond_vapour_ghast_tear")
                )
        ).build(output);

        simpleExtractionPage(EItems.OIL_OF_VITRIOL.get(), "Oil of Vitriol", """
                        A clear, slightly yellowed liquid which seems to react to living matter with vitriol, leaving them burned and blackened.
                        
                        Oil of vitriol dissolves most substances it comes into contact with.""",
                distillingPages("distilling/foul_fume_quicklime")
        ).build(output);

        simpleExtractionPage(EItems.REEK_OF_MISFORTUNE.get(), "Reek of Misfortune", """
                        The sacred alder tree appears to bleed when cut, bringing misfortune to all. It is thought that the tree contains the souls of our ancestors.
                        
                        The fumes produced from burning it can be collected, keeping these effects.""",
                combine(
                        byproductPages("byproduct/reek_of_misfortune_alder_sapling"),
                        distillingPages("distilling/diamond_vapour_ghast_tear", "distilling/ender_pearl")
                )
        ).build(output);

        simpleExtractionPage(EItems.REFINED_EVIL.get(), "Refined Evil", """
                        Pure, condensed evil refined using diamond vapour.
                        
                        Refined Evil is primarily used for brewing, but also has some uses in demonology.""",
                distillingPages("distilling/diamond_vapour_ghast_tear")
        ).build(output);

        simpleExtractionPage(EItems.TEAR_OF_THE_GODDESS.get(), "Tear of the Goddess", """
                        Lapis Lazuli is a gemstone which brings wisdom, truth, loyalty and honour.
                        
                        Combining Lapis with the White Goddess's power amplifies it's effects.""",
                distillingPages("distilling/breath_of_the_goddess_lapis_lazuli")
        ).build(output);

        simpleExtractionPage(EItems.WHIFF_OF_MAGIC.get(), "Whiff of Magic", """
                Due to it's silvery appearance, birch is sometimes called "White Lady Of The Woods" and is associated with the goddess Brigid.
                
                The smoke produced by burning birch has healing properties.""",
                combine(byproductPages("byproduct/whiff_of_magic_rowan_sapling"),
                        distillingPages("distilling/ender_pearl", "distilling/breath_of_the_goddess_lapis_lazuli")
                )
        ).build(output);



        EntryBuilder.of("extraction/distillery", "Distillation")
                .icon(EItems.DISTILLERY.get().getDefaultInstance())
                .assignedItems(EItems.DISTILLERY.get())
                .page(
                        HeaderedTextBuilder.of("Distillation", """
                                The distillery allows witches to separate materials into their components by utilising differences in boiling and condensing points.
                                
                                Processing consumes $(b)$(el:getting_started/altars/altar_construction)altar power$(), so the distillery must be placed accordingly."""
                        )
                )
                .page(
                        HeaderBuilder.of(Modopedia.translation("template", "crafting_recipe")),
                        SeparatorBuilder.of().y(10),
                        MultiblockBuilder.of().multiblock(new DenseMultiblock(List.of(List.of("D")), Map.of('D', new SimpleStateMatcher(List.of(EBlocks.DISTILLERY.get().defaultBlockState().setValue(DistilleryBlock.LIT, true))))))
                                .y(10).height(50),
                        CraftingRecipeBuilder.of(Enchanted.id("distillery")).y(70)
                )
                .build(output);

        EntryBuilder.of("extraction/witch_oven", "Fume Collection")
                .icon(EItems.WITCH_OVEN.get().getDefaultInstance())
                .assignedItems(EItems.WITCH_OVEN.get(), EItems.FUME_FUNNEL.get(), EItems.FUME_FUNNEL_FILTERED.get(), EItems.FUME_FILTER.get())
                .page(
                        HeaderedTextBuilder.of("Fume Collection", """
                                The witch's oven is a modified furnace used to collect byproducts and fumes made from burning materials.
                                
                                Unlike a furnace, this oven cannot process ores. Place a $(b)$(el:extraction/clay_jar)clay jar$() into the middle slot to collect fumes."""
                        )
                )
                .page(
                        HeaderBuilder.of(Modopedia.translation("template", "crafting_recipe")),
                        SeparatorBuilder.of().y(10),
                        MultiblockBuilder.of().multiblock(new DenseMultiblock(List.of(List.of("W")), Map.of('W', new SimpleStateMatcher(List.of(EBlocks.WITCH_OVEN.get().defaultBlockState().setValue(WitchOvenBlock.LIT, true))))))
                                .y(10).height(50),
                        CraftingRecipeBuilder.of(Enchanted.id("witch_oven")).y(70)
                )
                .page(
                        HeaderedTextBuilder.of("Fume Funnels", "Adding fume funnels to the left and right of your witch's oven can drastically improve it's yield."),
                        CraftingRecipeBuilder.of(Enchanted.id("fume_funnel")).y(68)
                )
                .page(
                        MultiblockPageBuilder.of("A third fume funnel can be placed on top of the witch oven as a decorative chimney.")
                                .multiblockId(Enchanted.id("witch_oven"))
                                .offsetY(-10)
                )
                .page(
                        HeaderedTextBuilder.of("Fume Filters", "A fume filter can be added to fume funnels to further increase yield. With two filters, success is guaranteed."),
                        CraftingRecipeBuilder.of(Enchanted.id("fume_filter")).y(70)
                )
                .page(
                        BlockPageBuilder.of("$(b)Procurement:$()\nUse a fume filter on a fume funnel.", EBlocks.FUME_FUNNEL_FILTERED.get().defaultBlockState().setValue(FumeFunnelBlock.LIT, true))
                )
                .build(output);
    }

    public void buildBrewingEntries(Provider provider, BiConsumer<String, JsonElement> output) {
        simpleKettlePage(EItems.BREW_OF_LOVE.get(), "Brew of Love", "Charming or love potions are one of the most widely sought types of brew.\n\nWhen thrown, the vapour produced will cause nearby animals to become infatuated.").build(output);
        simpleKettlePage(EItems.BREW_OF_SPROUTING.get(), "Brew of Sprouting", "Being able to slow your opponents in combat can be the difference between life and death.\n\nThe brew of sprouting can be used to ensnare mobs and people with roots.\n\n$(b)This feature is coming soon.").build(output);
        simpleKettlePage(EItems.BREW_OF_THE_DEPTHS.get(), "Brew of the Depths", "The ability to breathe underwater has fascinated people for millennia. Fortunately, witches have a solution for this.\n\nThe brew of depths grants the ability to breathe underwater for an extended time when ingested.").build(output);
        simpleKettlePage(EItems.BREW_OF_THE_GROTESQUE.get(), "Brew of the Grotesque", "This brew makes the drinker unrecognisably grotesque, tricking nearby mobs into believing the drinker is one of their own.\n\nAdditionally, brew of the grotesque is used as a base in most curses.").build(output);
        simpleKettlePage(EItems.FLYING_OINTMENT.get(), "Flying Ointment", "Flying ointment is a brew which seems to defy gravity; anything imbued with it will gain the property of flight, including people who ingest it.\n\nMost notably, it can be used to $(b)infuse broomsticks$()").build(output);
        simpleKettlePage(EItems.HAPPENSTANCE_OIL.get(), "Happenstance Oil", "Clairvoyance is an indispensable tool for a witch in need of information.\n\nHappenstance oil is a key component of crystal balls, and while consuming it is not recommended, it can improve your vision.").build(output);
        simpleKettlePage(EItems.MYSTIC_UNGUENT.get(), "Mystic Unguent", "Mystic Unguent is a curious concoction which allows a witch to give physical form to thoughts.\n\nIt's main use is in the production of mystic branches, consuming this brew is not recommended.").build(output);
        simpleKettlePage(EItems.REDSTONE_SOUP.get(), "Redstone Soup", "Redstone Soup primarily acts as a base for other infusions and brews, it has very few reported effects outside of this.\n\nIngesting redstone soup can increase your health for a short time.").build(output);
        simpleKettlePage(EItems.SOUL_OF_THE_WORLD.get(), "Soul of the World", "Soul of the World is a derivation of $(b)$(el:brewing/redstone_soup)redstone soup$() imbued with natural energy. It can be used to infuse energy into a person to grant them special abilities.\n\nThis brew is extremely toxic and should not be ingested.").build(output);
        simpleKettlePage(EItems.SPIRIT_OF_OTHERWHERE.get(), "Spirit of Otherwhere", "Similar to $(b)$(el:brewing/soul_of_the_world)soul of the world$(), spirit of otherwhere is a derivation of $(b)$(el:brewing/redstone_soup)redstone soup$() imbued with the same properties as the End and its inhabitants.\n\nThis infusion is extremely toxic and should not be ingested.").build(output);

        simpleCauldronPage(EItems.DROP_OF_LUCK.get(), "Drop of Luck", "Liquid luck, or drop of luck, is a potion which enhances the luck of anybody who drinks it and can be used in rites or the creation of magical items.\n\nFamously, it's a core ingredient in $(b)$(el:brewing/redstone_soup)Redstone Soup$().").build(output);
        simpleCauldronPage(EItems.GOLDEN_CHALK.get(), "Golden Chalk", "Chalk is vital for performing circle magic and among chalks golden chalk is the most important.\n\nThe $(b)heart glyph$() for $(b)circle magic$() is drawn using golden chalk.").build(output);
        simpleCauldronPage(EItems.NETHER_CHALK.get(), "Infernal Chalk", "Infusing chalk with blaze powder binds it to the nether and enables it to better conduct heat.\n\nInfernal chalk is used for many rites involving the nether, demonic beings or fire.").build(output);
        simpleCauldronPage(EItems.OTHERWHERE_CHALK.get(), "Otherwhere chalk", "Materials from the end can be infused into chalk to create a rich, purple chalk with special properties.\n\nTypically, otherwhere chalk is used in circle magic rites involving teleportation, transposition, relocation or the end.").build(output);

        EntryBuilder.of("brewing/kettle", "The Kettle")
                .icon(EItems.KETTLE.get().getDefaultInstance())
                .assignedItems(EItems.KETTLE.get())
                .page(
                        HeaderedTextBuilder.of("Kettle", "The kettle, used for creating infusions, is a crucial utensil for any witch."),
                        CraftingRecipeBuilder.of(Enchanted.id("kettle")).y(65)
                )
                .page(
                        MultiblockBuilder.of()
                                .multiblockId(Enchanted.id("kettle"))
                                .y(20)
                )
                .build(output);

        EntryBuilder.of("brewing/witch_cauldron", "Witch's Cauldron")
                .icon(EItems.WITCH_CAULDRON.get().getDefaultInstance())
                .assignedItems(EItems.WITCH_CAULDRON.get())
                .page(
                        HeaderedTextBuilder.of("Witch's Cauldron", "The witch's cauldron is the most important brewing tool at a witch's disposal, enabling you to brew complex potions."),
                        CraftingRecipeBuilder.of(Enchanted.id("kettle")).y(70)
                )
                .page(
                        BlockPageBuilder.of("$(b)Procurement:$()\nUse anointing paste on a cauldron.", EBlocks.WITCH_CAULDRON.get().defaultBlockState())
                )
                .build(output);
        EntryBuilder.of("brewing/brewing", "Creating Brews")
                .icon(EItems.WITCH_CAULDRON.get().getDefaultInstance())
                .page(
                        HeaderedTextBuilder.of("Brews and Potions", """
                                Creating a brew, infusion or potion is a simple process, requiring either a $(b)$(c:#582C69)$(el:brewing/witch_cauldron)witch's cauldron$() or $(b)$(c:#582C69)$(el:brewing/kettle)kettle$().
                                
                                First, you must fill your $(b)$(el:brewing/witch_cauldron)cauldron$() or $(b)$(el:brewing/kettle)kettle$() with water from a bucket. You will know it is full if no more water can fit."""
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
                                The final step, after filling the vessel and waiting for it to boil, is to drop your ingredients into the vessel, in order, one by one.
                                
                                The water should start to change colour. Once added, the brew needs to boil for a few seconds to finish."""
                        )
                )
                .page(
                        HeaderedTextBuilder.of("Spoiled Brews", """
                                If you make a mistake during brewing, such as adding the wrong ingredient or removing the heat source, your brew will $(b)spoil$(/b).
                                
                                When spoiled, the brew will turn brown and need to be removed using an $(b)empty bucket$(/b)."""
                        )
                )
                .build(output);
    }

    @Override
    public void buildCategories(Provider provider, BiConsumer<String, Category> output) {
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

    public EntryBuilder simpleCraftedItem(String id, String title, String description, ResourceLocation recipe, Item... items) {
        return EntryBuilder.of(id, title)
                .icon(items[0].getDefaultInstance())
                .assignedItems(items)
                .page(HeaderedTextBuilder.of(title, description))
                .page(CraftingPageBuilder.of(recipe));
    }

    public EntryBuilder simpleCauldronPage(Item item, String title, String description) {
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(item);
        return EntryBuilder.of("brewing/" + id.getPath(), title)
                .icon(item.getDefaultInstance())
                .assignedItems(item)
                .page(HeaderedTextBuilder.of(title, description))
                .page(WitchCauldronRecipeBuilder.of(Enchanted.id("witch_cauldron/" + id.getPath())));
    }

    public EntryBuilder simpleKettlePage(Item item, String title, String description) {
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(item);
        return EntryBuilder.of("brewing/" + id.getPath(), title)
                .icon(item.getDefaultInstance())
                .assignedItems(item)
                .page(HeaderedTextBuilder.of(title, description))
                .page(KettleRecipeBuilder.of(Enchanted.id("kettle/" + id.getPath())));
    }

    public EntryBuilder simpleExtractionPage(Item item, String title, String description, PageComponentBuilder... recipeTemplates) {
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(item);
        return EntryBuilder.of("extraction/" + id.getPath(), title)
                .icon(item.getDefaultInstance())
                .assignedItems(item)
                .page(HeaderedTextBuilder.of(title, description))
                .page(GalleryBuilder.of(recipeTemplates).height(140));
    }

    public static PageComponentBuilder[] combine(PageComponentBuilder[] a, PageComponentBuilder[] b) {
        return ArrayUtils.addAll(a, b);
    }

    public PageComponentBuilder[] byproductPages(String... recipes) {
        List<PageComponentBuilder> pages = new ArrayList<>();

        for(int i = 0; i < recipes.length; i++) {
            if(++i < recipes.length)
                pages.add(DoubleByproductPageBuilder.of(Enchanted.id(recipes[i-1]), Enchanted.id(recipes[i])));
            else
                pages.add(ByproductPageBuilder.of(Enchanted.id(recipes[i-1])));
        }

        return pages.toArray(PageComponentBuilder[]::new);
    }

    public PageComponentBuilder[] distillingPages(String... recipes) {
        return Arrays.stream(recipes).map(Enchanted::id).map(DistilleryPageBuilder::of).toArray(PageComponentBuilder[]::new);
    }

}
