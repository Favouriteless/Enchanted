package net.favouriteless.enchanted.common.enchanted.circle_magic;

import com.mojang.serialization.Codec;
import net.favouriteless.enchanted.api.Vec2i;
import net.favouriteless.enchanted.common.init.EBlocks;
import net.favouriteless.enchanted.common.init.EData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public class CircleMagicShape {

    public static final Codec<Holder<CircleMagicShape>> HOLDER_CODEC = Codec.lazyInitialized(() -> RegistryFixedCodec.create(EData.CIRCLE_SHAPE_REGISTRY));
    public static final Codec<CircleMagicShape> CODEC = Codec.STRING.listOf(1, Integer.MAX_VALUE).xmap(CircleMagicShape::new, CircleMagicShape::getStrings);

    private final List<String> input;
    private final List<Vec2i> points = new ArrayList<>();
    private final List<Vec2i> interiorPoints = new ArrayList<>();
    private final int radius;

    public CircleMagicShape(List<String> rows) {
        this.input = rows;
        int width = rows.getFirst().length();
        if(rows.size() != width)
            throw new IllegalArgumentException("Circle magic shapes must be square");

        if(width % 2 == 0)
            throw new IllegalArgumentException("Circle magic shapes must have an odd width.");

        for(String row : rows) {
            if(row.length() != width)
                throw new IllegalArgumentException("All rows in a circle magic shape must be the same width.");
        }

        int radius = width / 2;
        boolean foundPoint = false;
        for(int y = 0; y < width; y++) {
            String rowString = rows.get(y);

            if(!foundPoint && !rowString.isBlank())
                foundPoint = true;

            for(int x = 0; x < width; x++) {
                char charAt = rowString.charAt(x);
                if(charAt == 'X')
                    points.add(new Vec2i(x-radius, y-radius));
                else if(charAt  == 'O')
                    interiorPoints.add(new Vec2i(x-radius, y-radius));
            }
        }

        if(!foundPoint)
            throw new IllegalArgumentException("A circle magic shape cannot have zero points");

        this.radius = (width - 1) / 2;
    }

    public boolean matches(Level level, BlockPos pos, Block block) {
        for(Vec2i point : points) {
            BlockState state = level.getBlockState(pos.offset(point.x(), 0, point.y()));

            if(!state.is(block))
                return false; // Return false if any of the points doesn't match.
        }
        return true;
    }

    public List<String> getStrings() {
        return input;
    }

    public List<Vec2i> getInteriorPoints() {
        return interiorPoints;
    }

    public int getRadius() {
        return radius;
    }

    public boolean canPlace(Level level, BlockPos center) {
        for(Vec2i point : points) {
            BlockPos pos = center.offset(point.x(), 0, point.y());
            if(!level.getBlockState(pos).canBeReplaced() || !EBlocks.RITUAL_CHALK.get().canSurvive(null, level, pos))
                return false;
        }
        return true;
    }

    public void place(Level level, BlockPos center, Block block, UseOnContext context) {
        for(Vec2i point : points)
            level.setBlockAndUpdate(center.offset(point.x(), 0, point.y()), block.getStateForPlacement(new BlockPlaceContext(context)));
    }

    public void remove(Level level, BlockPos center) {
        for(Vec2i point : points)
            level.setBlockAndUpdate(center.offset(point.x(), 0, point.y()), Blocks.AIR.defaultBlockState());
    }

    public Block getBlockAt(Level level, BlockPos center) {
        Vec2i p1 = points.getFirst();
        BlockState first = level.getBlockState(center.offset(p1.x(), 0, p1.y()));
        if(first.isAir())
            return null;

        for(int i = 1; i < points.size(); i++) {
            Vec2i p = points.get(i);
            if(!level.getBlockState(center.offset(p.x(), 0, p.y())).is(first.getBlock()))
                return null;
        }
        return first.getBlock();
    }

}
