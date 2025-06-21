package net.favouriteless.enchanted.common.circle_magic;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.favouriteless.enchanted.api.Vec2i;
import net.favouriteless.enchanted.api.circle_magic.RiteFactory;
import net.favouriteless.enchanted.common.init.EData;
import net.favouriteless.enchanted.common.circle_magic.rites.Rite;
import net.favouriteless.enchanted.common.circle_magic.rites.Rite.BaseRiteParams;
import net.favouriteless.enchanted.common.circle_magic.rites.Rite.RiteParams;
import net.favouriteless.enchanted.common.util.ItemUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class RiteType implements Comparable<RiteType> {

    public static final Codec<RiteType> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ItemStack.CODEC.listOf().fieldOf("items").forGetter(r -> r.items),
            Codec.unboundedMap(CircleMagicShape.HOLDER_CODEC, BuiltInRegistries.BLOCK.byNameCodec()).optionalFieldOf("shapes", Map.of()).forGetter(r -> r.shapes),
            BuiltInRegistries.ENTITY_TYPE.byNameCodec().listOf().optionalFieldOf("entities", List.of()).forGetter(r -> r.entities),
            Codec.INT.optionalFieldOf("power", 0).forGetter(r -> r.power),
            Codec.INT.optionalFieldOf("tick_power", 0).forGetter(r -> r.tickPower),
            RiteWeatherRequirement.CODEC.optionalFieldOf("weather", RiteWeatherRequirement.NONE).forGetter(r -> r.weather),
            Codec.INT.listOf(2, 2).optionalFieldOf("time", List.of(0, Level.TICKS_PER_DAY)).forGetter(r -> r.timeRange),
            RiteFactoryRegistry.CODEC.fieldOf("factory").forGetter(r -> r.factory)
    ).apply(instance, RiteType::new));

    private final List<ItemStack> items;
    private final Map<Holder<CircleMagicShape>, Block> shapes;
    private final List<EntityType<?>> entities;
    private final int power;
    private final int tickPower;
    private final RiteFactory factory;
    private final RiteWeatherRequirement weather;
    private final List<Integer> timeRange;

    private final List<Vec2i> interiorPoints = new ArrayList<>();
    private int radius = 1;

    public RiteType(List<ItemStack> items, Map<Holder<CircleMagicShape>, Block> shapes, List<EntityType<?>> entities,
                    int power, int tickPower, RiteWeatherRequirement weather, List<Integer> times, RiteFactory factory) {
        this.items = items;
        this.shapes = shapes;
        this.entities = entities;
        this.power = power;
        this.tickPower = tickPower;
        this.weather = weather;
        this.timeRange = times;
        this.factory = factory;

        shapes.keySet().stream().map(Holder::value).forEach(shape -> {
            if(shape.getRadius() > radius)
                radius = shape.getRadius();
            interiorPoints.addAll(shape.getInteriorPoints());
        });
    }


    public boolean matches(Level level, BlockPos pos, List<Entity> inputs) {
        if(!weather.check(level))
            return false;

        long time = level.getDayTime() % Level.TICKS_PER_DAY;
        if(time < timeRange.getFirst())
            return false;
        if(time > timeRange.getLast())
            return false;

        for(Entry<Holder<CircleMagicShape>, Block> entry : shapes.entrySet()) {
            if(!entry.getKey().value().matches(level, pos, entry.getValue()))
                return false;
        }

        List<EntityType<?>> entities = getEntities();
        inputs.forEach(entity -> entities.remove(entity.getType()));
        if(!entities.isEmpty())
            return false;

        List<ItemStack> items = getItems();
        for(Entity input : inputs) {
            if(input instanceof ItemEntity itemEntity) {
                ItemStack item = itemEntity.getItem();
                if(item.isEmpty())
                    break;

                for(ItemStack required : items) {
                    if(ItemUtils.isSameItemPartial(item, required)) {
                        required.shrink(item.getCount());
                    }
                }
            }
        }
        items.removeIf(ItemStack::isEmpty);

        return items.isEmpty();
    }

    /**
     * @return A copy of this RiteRequirements' item list.
     */
    public List<ItemStack> getItems() {
        return items.stream().map(ItemStack::copy).collect(Collectors.toList());
    }

    /**
     * @return A copy of this RiteRequirements' entity list.
     */
    public List<EntityType<?>> getEntities() {
        return new ArrayList<>(entities);
    }

    /**
     * @return This RiteRequirements' shapes map.
     */
    public Map<Holder<CircleMagicShape>, Block> getShapes() {
        return shapes;
    }

    public int getPower() {
        return power;
    }

    public int getRadius() {
        return radius;
    }

    public AABB getBounds(BlockPos pos) {
        Vec3 center = pos.getBottomCenter();
        return new AABB(center.subtract(radius, 0, radius), center.add(radius, 1, radius));
    }

    public List<Vec2i> getInteriorPoints() {
        return interiorPoints;
    }

    public @Nullable List<ItemStack> getOutputs() {
        return factory.getOutputs();
    }

    public Rite create(ServerLevel level, BlockPos pos, UUID caster, List<ItemStack> consumedItems) {
        return factory.create(new BaseRiteParams(this, level, pos, tickPower), RiteParams.of(caster, consumedItems));
    }

    public Rite create(ServerLevel level, BlockPos pos) {
        return factory.create(new BaseRiteParams(this, level, pos, tickPower), RiteParams.empty());
    }

    public static RiteType getFirstMatching(Level level, BlockPos pos) {
        Registry<RiteType> reg = level.registryAccess().registryOrThrow(EData.RITE_TYPES_REGISTRY);

        List<Entity> entities = level.getEntities(null, new AABB(
                pos.getX()-3, pos.getY(), pos.getZ()-3,
                pos.getX()+4, pos.getY()+1, pos.getZ()+4
                ));

        for(Iterator<RiteType> it = reg.stream().sorted().iterator(); it.hasNext(); ) {
            RiteType type = it.next();
            if(type.matches(level, pos, entities))
                return type;
        }

        return null;
    }

    @Override
    public int compareTo(@NotNull RiteType o) {
        if(shapes.size() == o.shapes.size() && items.size() == o.items.size() && entities.size() == o.entities.size())
            return 0;

        if(shapes.size() > o.shapes.size())
            return -1;
        else if(shapes.size() < o.shapes.size())
            return 1;

        if(items.size() > o.items.size())
            return -1;
        else if(items.size() < o.items.size())
            return 1;

        if(entities.size() > o.entities.size())
            return -1;
        return 1;
    }

}
