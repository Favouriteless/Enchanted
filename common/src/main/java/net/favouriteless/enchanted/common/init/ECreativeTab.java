package net.favouriteless.enchanted.common.init;

import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.enchanted.common.items.component.EDataComponents;
import net.favouriteless.enchanted.platform.EServices;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTab.DisplayItemsGenerator;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class ECreativeTab {

    public static final ResourceLocation[] SHAPE_IDS = new ResourceLocation[] {
            Enchanted.id("small_circle"),
            Enchanted.id("medium_circle"),
            Enchanted.id("large_circle")
    };

    public static final List<Supplier<? extends Block>> SHAPE_BLOCKS = List.of(
            EBlocks.RITUAL_CHALK,
            EBlocks.NETHER_CHALK,
            EBlocks.OTHERWHERE_CHALK
    );

    public static final Supplier<CreativeModeTab> TAB = register(
            "main",
            () -> EItems.ENCHANTED_BROOMSTICK.get().getDefaultInstance(),
            (params, out) -> {
                out.accept(EItems.ALDER_LOG.get());
                out.accept(EItems.STRIPPED_ALDER_LOG.get());
                out.accept(EItems.ALDER_PLANKS.get());
                out.accept(EItems.ALDER_STAIRS.get());
                out.accept(EItems.ALDER_SLAB.get());
                out.accept(EItems.ALDER_FENCE.get());
                out.accept(EItems.ALDER_FENCE_GATE.get());
                out.accept(EItems.ALDER_PRESSURE_PLATE.get());
                out.accept(EItems.ALDER_BUTTON.get());
                out.accept(EItems.HAWTHORN_LOG.get());
                out.accept(EItems.STRIPPED_HAWTHORN_LOG.get());
                out.accept(EItems.HAWTHORN_PLANKS.get());
                out.accept(EItems.HAWTHORN_STAIRS.get());
                out.accept(EItems.HAWTHORN_SLAB.get());
                out.accept(EItems.HAWTHORN_FENCE.get());
                out.accept(EItems.HAWTHORN_FENCE_GATE.get());
                out.accept(EItems.HAWTHORN_PRESSURE_PLATE.get());
                out.accept(EItems.HAWTHORN_BUTTON.get());
                out.accept(EItems.ROWAN_LOG.get());
                out.accept(EItems.STRIPPED_ROWAN_LOG.get());
                out.accept(EItems.ROWAN_PLANKS.get());
                out.accept(EItems.ROWAN_STAIRS.get());
                out.accept(EItems.ROWAN_SLAB.get());
                out.accept(EItems.ROWAN_FENCE.get());
                out.accept(EItems.ROWAN_FENCE_GATE.get());
                out.accept(EItems.ROWAN_PRESSURE_PLATE.get());
                out.accept(EItems.ROWAN_BUTTON.get());
                out.accept(EItems.ALDER_LEAVES.get());
                out.accept(EItems.HAWTHORN_LEAVES.get());
                out.accept(EItems.ROWAN_LEAVES.get());

                out.accept(EItems.ALTAR.get());
                out.accept(EItems.WITCH_OVEN.get());
                out.accept(EItems.FUME_FUNNEL.get());
                out.accept(EItems.FUME_FUNNEL_FILTERED.get());
                out.accept(EItems.DISTILLERY.get());
                out.accept(EItems.WITCH_CAULDRON.get());
                out.accept(EItems.KETTLE.get());
                out.accept(EItems.SPINNING_WHEEL.get());
                out.accept(EItems.POPPET_SHELF.get());

                out.accept(EItems.CHALICE.get());
                out.accept(EItems.CHALICE_FILLED.get());
                out.accept(EItems.CANDELABRA.get());
                out.accept(EItems.INFINITY_EGG.get());

                out.accept(EItems.WICKER_BUNDLE.get());

                out.accept(EItems.ALDER_SAPLING.get());
                out.accept(EItems.HAWTHORN_SAPLING.get());
                out.accept(EItems.ROWAN_SAPLING.get());
                out.accept(EItems.WATER_ARTICHOKE_SEEDS.get());
                out.accept(EItems.BELLADONNA_SEEDS.get());
                out.accept(EItems.MANDRAKE_SEEDS.get());
                out.accept(EItems.SNOWBELL_SEEDS.get());
                out.accept(EItems.WOLFSBANE_SEEDS.get());
                out.accept(EItems.GARLIC.get());
                out.accept(EItems.ROWAN_BERRIES.get());
                out.accept(EItems.WATER_ARTICHOKE.get());
                out.accept(EItems.BELLADONNA_FLOWER.get());
                out.accept(EItems.MANDRAKE_ROOT.get());
                out.accept(EItems.ICY_NEEDLE.get());
                out.accept(EItems.WOLFSBANE_FLOWER.get());

                out.accept(EItems.GLINT_WEED.get());
                out.accept(EItems.EMBER_MOSS.get());
                out.accept(EItems.SPANISH_MOSS.get());
                out.accept(EItems.BLOOD_POPPY.get());

                out.accept(EItems.ANOINTING_PASTE.get());
                out.accept(EItems.MUTANDIS.get());
                out.accept(EItems.MUTANDIS_EXTREMIS.get());
                out.accept(EItems.TAGLOCK.get());
                out.accept(EItems.GOLDEN_CHALK.get());
                out.accept(EItems.RITUAL_CHALK.get());
                out.accept(EItems.NETHER_CHALK.get());
                out.accept(EItems.OTHERWHERE_CHALK.get());

                out.accept(EItems.ARTHANA.get());
                out.accept(EItems.EARMUFFS.get());
                out.accept(EItems.BROOM.get());
                out.accept(EItems.ENCHANTED_BROOMSTICK.get());

                out.accept(EItems.SOFT_CLAY_JAR.get());
                out.accept(EItems.CLAY_JAR.get());
                out.accept(EItems.BREATH_OF_THE_GODDESS.get());
                out.accept(EItems.EXHALE_OF_THE_HORNED_ONE.get());
                out.accept(EItems.FOUL_FUME.get());
                out.accept(EItems.HINT_OF_REBIRTH.get());
                out.accept(EItems.TEAR_OF_THE_GODDESS.get());
                out.accept(EItems.WHIFF_OF_MAGIC.get());
                out.accept(EItems.CONDENSED_FEAR.get());
                out.accept(EItems.DIAMOND_VAPOUR.get());
                out.accept(EItems.DROP_OF_LUCK.get());
                out.accept(EItems.ENDER_DEW.get());
                out.accept(EItems.FOCUSED_WILL.get());
                out.accept(EItems.DEMONIC_BLOOD.get());
                out.accept(EItems.MELLIFLUOUS_HUNGER.get());
                out.accept(EItems.ODOUR_OF_PURITY.get());
                out.accept(EItems.OIL_OF_VITRIOL.get());
                out.accept(EItems.PURIFIED_MILK.get());
                out.accept(EItems.REEK_OF_MISFORTUNE.get());

                out.accept(EItems.ATTUNED_STONE.get());
                out.accept(EItems.ATTUNED_STONE_CHARGED.get());
                out.accept(EItems.WAYSTONE.get());
                out.accept(EItems.GYPSUM.get());
                out.accept(EItems.QUICKLIME.get());
                out.accept(EItems.REFINED_EVIL.get());
                out.accept(EItems.WOOD_ASH.get());
                out.accept(EItems.BONE_NEEDLE.get());
                out.accept(EItems.WOOL_OF_BAT.get());
                out.accept(EItems.TONGUE_OF_DOG.get());
                out.accept(EItems.CREEPER_HEART.get());
                out.accept(EItems.ENT_TWIG.get());

                out.accept(EItems.REDSTONE_SOUP.get());
                out.accept(EItems.FLYING_OINTMENT.get());
                out.accept(EItems.MYSTIC_UNGUENT.get());
                out.accept(EItems.GHOST_OF_THE_LIGHT.get());
                out.accept(EItems.SOUL_OF_THE_WORLD.get());
                out.accept(EItems.SPIRIT_OF_OTHERWHERE.get());
                out.accept(EItems.INFERNAL_ANIMUS.get());
                out.accept(EItems.HAPPENSTANCE_OIL.get());
                out.accept(EItems.BREW_OF_THE_GROTESQUE.get());
                out.accept(EItems.BREW_OF_SPROUTING.get());
                out.accept(EItems.BREW_OF_LOVE.get());
                out.accept(EItems.BREW_OF_THE_DEPTHS.get());

                out.accept(EItems.POPPET.get());
                out.accept(EItems.POPPET_INFUSED.get());
                out.accept(EItems.POPPET_STURDY.get());
                out.accept(EItems.ARMOUR_POPPET.get());
                out.accept(EItems.ARMOUR_POPPET_INFUSED.get());
                out.accept(EItems.ARMOUR_POPPET_STURDY.get());
                out.accept(EItems.EARTH_POPPET.get());
                out.accept(EItems.EARTH_POPPET_INFUSED.get());
                out.accept(EItems.EARTH_POPPET_STURDY.get());
                out.accept(EItems.FIRE_POPPET.get());
                out.accept(EItems.FIRE_POPPET_INFUSED.get());
                out.accept(EItems.FIRE_POPPET_STURDY.get());
                out.accept(EItems.HUNGER_POPPET.get());
                out.accept(EItems.HUNGER_POPPET_INFUSED.get());
                out.accept(EItems.HUNGER_POPPET_STURDY.get());
                out.accept(EItems.MAGIC_POPPET.get());
                out.accept(EItems.MAGIC_POPPET_INFUSED.get());
                out.accept(EItems.MAGIC_POPPET_STURDY.get());
                out.accept(EItems.WATER_POPPET.get());
                out.accept(EItems.WATER_POPPET_INFUSED.get());
                out.accept(EItems.WATER_POPPET_STURDY.get());
                out.accept(EItems.VOID_POPPET.get());
                out.accept(EItems.VOID_POPPET_INFUSED.get());
                out.accept(EItems.VOID_POPPET_STURDY.get());
                out.accept(EItems.VOODOO_POPPET.get());
                out.accept(EItems.VOODOO_PROTECTION_POPPET.get());
                out.accept(EItems.VOODOO_PROTECTION_POPPET_INFUSED.get());
                out.accept(EItems.VOODOO_PROTECTION_POPPET_STURDY.get());
                out.accept(EItems.TOOL_POPPET.get());
                out.accept(EItems.TOOL_POPPET_INFUSED.get());
                out.accept(EItems.TOOL_POPPET_STURDY.get());


                out.accept(EItems.CIRCLE_TALISMAN.get());
                for (Supplier<? extends Block> block : SHAPE_BLOCKS) {
                    for (ResourceLocation id : SHAPE_IDS) {
                        ItemStack stack = new ItemStack(EItems.CIRCLE_TALISMAN.get());
                        stack.set(EDataComponents.CIRCLE_MAGIC_SHAPE_MAP.get(), new HashMap<>(Map.of(id, block.get())));
                        out.accept(stack);
                    }
                }
            }
    );


    public static Supplier<CreativeModeTab> register(String name, Supplier<ItemStack> iconSupplier, DisplayItemsGenerator itemsGenerator) {
        return EServices.REGISTRY.registerCreativeTab(name, iconSupplier, itemsGenerator);
    }

    public static void load() {
    } // Method which exists purely to load the class.

}
