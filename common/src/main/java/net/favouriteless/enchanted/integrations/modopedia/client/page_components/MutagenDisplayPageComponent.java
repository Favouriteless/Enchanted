package net.favouriteless.enchanted.integrations.modopedia.client.page_components;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.favouriteless.enchanted.api.MutagenManager;
import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.enchanted.common.blocks.crops.CropBlockAgeFive;
import net.favouriteless.enchanted.common.enchanted.mutandis.MutagenInfo.MutagenSet;
import net.favouriteless.enchanted.common.util.LangUtils;
import net.favouriteless.modopedia.api.Lookup;
import net.favouriteless.modopedia.api.book.Book;
import net.favouriteless.modopedia.api.book.BookTexture;
import net.favouriteless.modopedia.api.book.BookTexture.FixedRectangle;
import net.favouriteless.modopedia.api.book.BookTexture.Rectangle;
import net.favouriteless.modopedia.api.book.page_components.BookRenderContext;
import net.favouriteless.modopedia.api.book.page_components.PageComponent;
import net.favouriteless.modopedia.api.book.page_components.PageWidgetHolder;
import net.favouriteless.modopedia.api.registries.client.BookTextureRegistry;
import net.favouriteless.modopedia.client.multiblock.DenseMultiblock;
import net.favouriteless.modopedia.client.multiblock.PlacedMultiblock;
import net.favouriteless.modopedia.client.multiblock.state_matchers.SimpleStateMatcher;
import net.favouriteless.modopedia.client.page_widgets.PageImageButton;
import net.favouriteless.modopedia.platform.ClientServices;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class MutagenDisplayPageComponent extends PageComponent {

    public static final ResourceLocation ID = Enchanted.id("mutagen_display");
    private static final RandomSource RANDOM = RandomSource.createNewThreadLocalInstance();

    private List<MutagenDisplay> displays;

    protected PageImageButton leftButton;
    protected PageImageButton rightButton;
    protected int selected = 0;


    @Override
    public void init(Book book, Lookup lookup, Level level) {
        super.init(book, lookup, level);

        BookTexture texture = BookTextureRegistry.get().getTexture(book.getTexture());
        if(texture == null || !texture.widgets().containsKey("mutagen"))
            throw new IllegalStateException("MutagenInfoPageComponent has been created on a book texture with no mutagen widget");

        displays = new ArrayList<>(); // Re-initialise every time.

        Block result = BuiltInRegistries.BLOCK.get(lookup.get("result").as(ResourceLocation.class));

        for(Entry<Block, List<MutagenSet>> entry : MutagenManager.get().getMutagensFor(level, result).entrySet()) {
            BlockState mutee = getState(entry.getKey());

            for(MutagenSet set : entry.getValue()) {
                displays.add(new MutagenDisplay( // This looks stupid (it is), but ModelData grabbing is dependent on having a BlockAndTintGetter, so we use modopedia multiblocks as a substitute.
                        new PlacedMultiblock(
                                new DenseMultiblock(
                                        List.of(List.of("m")),
                                        Map.of('m', new SimpleStateMatcher(List.of(mutee)))
                                ), level
                        ),
                        set.mutagens().stream().map(b -> new PlacedMultiblock(
                                new DenseMultiblock(
                                        List.of(List.of("m")),
                                        Map.of('m', new SimpleStateMatcher(List.of(getState(b))))
                                ), level)
                        ).toList(),
                        set.weight(),
                        set.extremis()
                ));
            }
        }
        displays.sort((o1, o2) -> o2.weight - o1.weight);
    }

    @Override
    public void render(GuiGraphics graphics, BookRenderContext context, int mouseX, int mouseY, float partialTicks) {
        BookTexture tex = context.getBookTexture();
        Rectangle mutagen = tex.widgets().get("mutagen");

        int width = mutagen.width();
        int height = mutagen.height();
        int xo = mutagen.width() / 2;
        int yo = y + height / 2; // -1 offset due to rotating the blocks making them look lower than they are

        graphics.blit(tex.location(), x - xo, y, width, height, mutagen.u(), mutagen.v(), width, height, tex.texWidth(), tex.texHeight());

        Minecraft mc = Minecraft.getInstance();
        MultiBufferSource bufferSource = mc.renderBuffers().bufferSource();
        PoseStack pose = graphics.pose();

        pose.pushPose();
        pose.translate(x, yo, 100);

        MutagenDisplay display = displays.get(selected);

        render(context, display.mutee, pose, bufferSource, 16, partialTicks);

        float a = 360.0F / display.mutagens.size() * Mth.DEG_TO_RAD;
        float sin = Mth.sin(a);
        float cos = Mth.cos(a);

        float aStart = (context.getTicks() + partialTicks) / 8.0F * -Mth.DEG_TO_RAD;
        float x1 = 23 * Mth.sin(aStart); // 23 is the radius of the circle.
        float y1 = 23 * Mth.cos(aStart);

        for(PlacedMultiblock multiblock : display.mutagens) {
            pose.pushPose();
            pose.translate(x1, y1, 0);
            render(context, multiblock, pose, bufferSource, 10, partialTicks);
            pose.popPose();

            float xc = x1; // Rotate around (0, 0)
            x1 = xc * cos - y1 * sin;
            y1 = xc * sin + y1 * cos;
        }

        pose.popPose();

        Font font = Minecraft.getInstance().font;
        Component weightTitle = Component.translatable(LangUtils.tooltip("mutagen_weight")).withStyle(context.getStyle().withBold(true));
        Component weight = Component.literal(String.valueOf(display.weight)).withStyle(context.getStyle());
        Component extremis = Component.translatable(LangUtils.tooltip("mutagen_extremis")).withStyle(context.getStyle().withBold(true).withColor(0x732D2D));

        int yw = y + height + 2;

        graphics.drawString(font, weightTitle, x - font.width(weightTitle) / 2, yw, 0, false);
        graphics.drawString(font, weight, x - font.width(weight) / 2, yw + font.lineHeight, 0, false);

        if(display.extremis)
            graphics.drawString(font, extremis, x - font.width(extremis) / 2, y - 7, 0, false);


        if(context.isHovered(mouseX, mouseY, x - xo, y, width, height)) {
            List<Component> lines = new ArrayList<>();

            lines.add(Component.translatable(LangUtils.tooltip("mutee")));
            lines.add(display.mutee.getBlockState(BlockPos.ZERO).getBlock().getName().withStyle(ChatFormatting.GREEN));
            lines.add(Component.literal("")); // An empty line as a break.
            lines.add(Component.translatable(LangUtils.tooltip("mutagens")));
            display.mutagens.stream().<Component>map(m -> m.getBlockState(BlockPos.ZERO).getBlock().getName().withStyle(ChatFormatting.GREEN)).forEach(lines::add);

            graphics.renderComponentTooltip(font, lines, mouseX, mouseY);
        }
    }

    @Override
    public void initWidgets(PageWidgetHolder holder, BookRenderContext context) {
        if(displays.size() < 2)
            return;

        BookTexture bookTex = context.getBookTexture();
        ResourceLocation tex = bookTex.location();
        FixedRectangle left = bookTex.left();
        FixedRectangle right = bookTex.right();
        Rectangle mutagen = bookTex.widgets().get("mutagen");

        int xo = mutagen.width() / 2;

        leftButton = holder.addRenderableWidget(
                new PageImageButton(tex, x - xo, y + mutagen.height() + 11, left.width(), left.height(),
                        left.u(), left.v(), bookTex.texWidth(), bookTex.texHeight(), b -> changeImage(-1))
        );
        rightButton = holder.addRenderableWidget(
                new PageImageButton(tex, x + xo - right.width(), y + mutagen.height() + 11, right.width(), right.height(),
                        right.u(), right.v(), bookTex.texWidth(), bookTex.texHeight(), b -> changeImage(1))
        );

        updateWidgetVisibility();
    }

    @Override
    public void tick(BookRenderContext context) {
        displays.forEach(d -> {
            d.mutee.tick();
            d.mutagens.forEach(PlacedMultiblock::tick);
        });
    }

    protected BlockState getState(Block block) {
        return block.defaultBlockState() // There's a bunch of properties for which the display would look far better with defaults.
                .trySetValue(CropBlock.AGE, CropBlock.MAX_AGE)
                .trySetValue(CropBlockAgeFive.AGE_FIVE, 4)
                .trySetValue(BlockStateProperties.LIT, true)
                .trySetValue(BlockStateProperties.BERRIES, true);
    }

    protected void render(BookRenderContext context, PlacedMultiblock multiblock, PoseStack pose, MultiBufferSource bufferSource, float scale, float partialTicks) {
        float off = 0.5F * scale;
        pose.pushPose();
        pose.translate(off, off, off);
        pose.scale(-scale, -scale, -scale);

        pose.translate(0.5F, 0.5F, 0.5F); // 0.5 offset is to center the block, so it can be rotated around center.
        pose.mulPose(Axis.XN.rotationDegrees(30));
        pose.mulPose(Axis.YP.rotationDegrees((context.getTicks() + partialTicks) / 2.0F));
        pose.translate(-0.5F, -0.5F, -0.5F);

        renderBlock(multiblock, pose, bufferSource, partialTicks);
        renderBlockEntity(multiblock, pose, bufferSource, partialTicks);

        pose.popPose();
    }

    protected void renderBlock(PlacedMultiblock multiblock, PoseStack pose, MultiBufferSource bufferSource, float partialTicks) {
        BlockRenderDispatcher dispatcher = Minecraft.getInstance().getBlockRenderer();
        BlockState state = multiblock.getBlockState(BlockPos.ZERO);
        if(state.getRenderShape() != RenderShape.MODEL)
            return;

        pose.pushPose();
        for(RenderType type : ClientServices.PLATFORM.getRenderTypes(multiblock, BlockPos.ZERO, state)) {
            VertexConsumer buffer = bufferSource.getBuffer(type);

            Vec3 offset = state.getOffset(multiblock, BlockPos.ZERO);
            pose.translate(-offset.x, -offset.y, -offset.z);

            dispatcher.renderBatched(state, BlockPos.ZERO, multiblock, pose, buffer, false, RANDOM);
        }
        pose.popPose();
    }

    protected void renderBlockEntity(PlacedMultiblock multiblock, PoseStack pose, MultiBufferSource bufferSource, float partialTicks) {
        Minecraft mc = Minecraft.getInstance();

        BlockEntity be = multiblock.getBlockEntity(BlockPos.ZERO);
        if(be == null)
            return;

        be.setLevel(mc.level);

        try {
            BlockEntityRenderer<BlockEntity> renderer = mc.getBlockEntityRenderDispatcher().getRenderer(be);
            if(renderer != null)
                renderer.render(be, partialTicks, pose, bufferSource, 0xF000F0, OverlayTexture.NO_OVERLAY);
        }
        catch(Exception ignored) {}
    }

    protected void changeImage(int by) {
        selected = Mth.clamp(selected + by, 0, displays.size() - 1);
        updateWidgetVisibility();
    }

    protected void updateWidgetVisibility() {
        if(selected <= 0) {
            selected = 0;
            leftButton.active = false;
        }
        else {
            leftButton.active = true;
        }

        if(selected >= displays.size() - 1) {
            selected = displays.size() - 1;
            rightButton.active = false;
        }
        else {
            rightButton.active = true;
        }
    }



    private record MutagenDisplay(PlacedMultiblock mutee, List<PlacedMultiblock> mutagens, int weight, boolean extremis) {}

}