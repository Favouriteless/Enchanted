package net.favouriteless.enchanted.integrations.modopedia.client.page_components;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import net.favouriteless.enchanted.client.EnchantedClient;
import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.enchanted.common.enchanted.circle_magic.CircleMagicShape;
import net.favouriteless.enchanted.common.enchanted.circle_magic.RiteType;
import net.favouriteless.enchanted.common.enchanted.circle_magic.RiteWeatherRequirement;
import net.favouriteless.enchanted.common.init.EData;
import net.favouriteless.enchanted.common.util.LangUtils;
import net.favouriteless.modopedia.api.Lookup;
import net.favouriteless.modopedia.api.book.Book;
import net.favouriteless.modopedia.api.book.BookTexture;
import net.favouriteless.modopedia.api.book.BookTexture.Rectangle;
import net.favouriteless.modopedia.api.book.page_components.BookRenderContext;
import net.favouriteless.modopedia.api.book.page_components.ItemDisplay;
import net.favouriteless.modopedia.api.book.page_components.PageComponent;
import net.favouriteless.modopedia.api.registries.client.BookTextureRegistry;
import net.favouriteless.modopedia.client.page_components.item_displays.RingsItemDisplay;
import net.favouriteless.modopedia.client.page_components.item_displays.SimpleItemDisplay;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;

public class RitePageComponent extends PageComponent {

    public static final ResourceLocation ID = Enchanted.id("rite");

    private RiteType rite;
    private ItemDisplay itemDisplay;
    private List<Requirement> requirements;
    private int requirementsWidth;

    @Override
    public void init(Book book, Lookup lookup, Level level) {
        super.init(book, lookup, level);
        Registry<RiteType> registry = level.registryAccess().registryOrThrow(EData.RITE_TYPES_REGISTRY);

        ResourceLocation id = lookup.get("rite").as(ResourceLocation.class);

        this.rite = registry.get(id);
        if(rite == null)
            throw new IllegalArgumentException(id + " is not a valid rite");

        this.itemDisplay = new RingsItemDisplay(rite.getItems().stream().<ItemDisplay>map(SimpleItemDisplay::new).toList(), 6, 16, 8);
        this.requirements = new ArrayList<>();

        BookTexture tex = BookTextureRegistry.get().getTexture(book.getTexture());
        if(tex == null)
            throw new IllegalStateException("RitePageComponent needs a valid book texture");

        buildRequirements(tex);
    }

    public void buildRequirements(BookTexture tex) {
        requirementsWidth = 0;

        final List<Component> powerLines = new ArrayList<>();
        if(rite.getPower() > 0)
            powerLines.add(Component.translatable(LangUtils.tooltip("rite_requirement.power"), rite.getPower()).withStyle(ChatFormatting.GREEN));
        if(rite.getTickPower() > 0)
            powerLines.add(Component.translatable(LangUtils.tooltip("rite_requirement.power_tick"), rite.getTickPower()).withStyle(ChatFormatting.GREEN));
        if(!powerLines.isEmpty())
            addRequirement(tex, "rite/power", powerLines);

        if(rite.getTimeRange() != null) {
            Pair<Integer, Integer> range = rite.getTimeRange();
            if(range.getFirst() != 0 || range.getSecond() != Level.TICKS_PER_DAY)
                addRequirement(tex, "rite/time", List.of(Component.translatable(LangUtils.tooltip("rite_requirement.time_range")).withStyle(ChatFormatting.GREEN)));
        }

        if(rite.getWeatherRequirement() != RiteWeatherRequirement.NONE) {
            String weather = rite.getWeatherRequirement().getSerializedName();
            addRequirement(tex, "rite/weather/" + weather, List.of(
                    Component.translatable(LangUtils.tooltip("rite_requirement.weather")),
                    Component.translatable(LangUtils.tooltip("rite_requirement.weather." + weather)).withStyle(ChatFormatting.GREEN)
            ));
        }

        if(!rite.getEntities().isEmpty()) {
            final List<Component> sacrificeLines = new ArrayList<>();
            sacrificeLines.add(Component.translatable(LangUtils.tooltip("rite_requirement.sacrifice")));
            for(EntityType<?> type : rite.getEntities())
                sacrificeLines.add(type.getDescription().copy().withStyle(ChatFormatting.GREEN));
            addRequirement(tex, "rite/sacrifices", sacrificeLines);
        }
    }

    private void addRequirement(BookTexture tex, String widget, List<Component> tooltipLines) {
        Rectangle rec = tex.widgets().get(widget);
        if(rec == null)
            throw new IllegalStateException("RitePageComponent needs a \"" + widget + "\" widget.");
        requirementsWidth += rec.width();
        requirements.add(new Requirement(rec, tooltipLines));
    }

    @Override
    public void render(GuiGraphics graphics, BookRenderContext context, int mouseX, int mouseY, float partialTicks) {
        renderShapes(graphics);
        renderItems(graphics, context, mouseX, mouseY);
        renderShapeTooltip(graphics, context, mouseX, mouseY);
        renderRequirementWidgets(graphics, context, mouseX, mouseY);
    }

    public void renderShapes(GuiGraphics graphics) {
        ResourceLocation heart = Enchanted.id("textures/gui/gold_glyph.png");
        graphics.blit(heart, x - 50, y, 0, 0, 100, 100, 100, 100);

        for(Entry<Holder<CircleMagicShape>, Block> entry : rite.getShapes().entrySet()) {
            Optional<ResourceKey<CircleMagicShape>> optional = entry.getKey().unwrapKey();
            if(optional.isEmpty())
                continue;

            ResourceLocation tex = EnchantedClient.getShapeGuiTexture(optional.get(), entry.getValue());
            if(tex != null)
                graphics.blit(tex, x - 50, y, 0, 0, 100, 100, 100, 100);
        }
    }

    public void renderItems(GuiGraphics graphics, BookRenderContext context, int mouseX, int mouseY) {
        PoseStack pose = graphics.pose();

        int xShift = x;
        int yShift = y + 50;

        pose.pushPose();
        pose.translate(x, y + 50, 0);
        itemDisplay.render(graphics, context, mouseX - xShift, mouseY - yShift, entryId);
        pose.popPose();
    }

    public void renderShapeTooltip(GuiGraphics graphics, BookRenderContext context, int mouseX, int mouseY) {
        Minecraft mc = Minecraft.getInstance();
        Font font = mc.font;

        List<Component> shapeTooltip = new ArrayList<>();
        shapeTooltip.add(Component.translatable(LangUtils.tooltip("rite_requirement.shapes")));
        rite.getShapes().forEach((rite, block) -> rite.unwrapKey()
                .map(ResourceKey::location)
                .ifPresent(loc -> shapeTooltip.add(
                        Component.translatable(LangUtils.key(loc.getNamespace(), "circle_magic.shape", loc.getPath())).withStyle(ChatFormatting.GREEN)
                                .append(Component.literal(" - ").withStyle(ChatFormatting.WHITE))
                                .append(block.getName().withStyle(ChatFormatting.GREEN)))
                )
        );

        if(context.isHovered(mouseX, mouseY, x - 5, y + 45, 10, 10))
            graphics.renderComponentTooltip(font, shapeTooltip, mouseX, mouseY);
    }

    public void renderRequirementWidgets(GuiGraphics graphics, BookRenderContext context, int mouseX, int mouseY) {
        Font font = Minecraft.getInstance().font;
        BookTexture tex = context.getBookTexture();
        ResourceLocation atlas = tex.location();
        int x = this.x - requirementsWidth/2;
        int y = this.y + 101;

        for(Requirement req : requirements) {
            Rectangle r = req.rectangle();
            graphics.blit(atlas, x, y, r.u(), r.v(), r.width(), r.height(), tex.texWidth(), tex.texHeight());

            if(context.isHovered(mouseX, mouseY, x, y, r.width(), r.height()))
                graphics.renderComponentTooltip(font, req.tooltip, mouseX, mouseY);

            x += r.width();
        }
    }

    private record Requirement(Rectangle rectangle, List<Component> tooltip) {}

}