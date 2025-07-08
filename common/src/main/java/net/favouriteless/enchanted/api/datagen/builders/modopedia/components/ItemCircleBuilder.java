package net.favouriteless.enchanted.api.datagen.builders.modopedia.components;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Either;
import net.favouriteless.enchanted.integrations.modopedia.client.page_components.ItemCirclePageComponent;
import net.favouriteless.modopedia.api.datagen.builders.PageComponentBuilder;
import net.minecraft.resources.RegistryOps;
import net.minecraft.world.item.ItemStack;

import java.util.Arrays;
import java.util.List;

public class ItemCircleBuilder extends PageComponentBuilder {

    private final Either<List<ItemStack>, String> items;
    private Either<Integer, String> ringMax;
    private Either<Integer, String> radius;

    private ItemCircleBuilder(List<ItemStack> items) {
        super(ItemCirclePageComponent.ID);
        this.items = Either.left(items);
    }

    private ItemCircleBuilder(String items) {
        super(ItemCirclePageComponent.ID);
        this.items = Either.right(items);
    }

    public static ItemCircleBuilder of(ItemStack... items) {
        return new ItemCircleBuilder(Arrays.asList(items));
    }

    public static ItemCircleBuilder of(List<ItemStack> items) {
        return new ItemCircleBuilder(items);
    }

    public static ItemCircleBuilder of(String items) {
        return new ItemCircleBuilder(items);
    }

    @Override
    public ItemCircleBuilder x(int x) {
        return (ItemCircleBuilder)super.x(x);
    }

    @Override
    public ItemCircleBuilder x(String x) {
        return (ItemCircleBuilder)super.x(x);
    }

    @Override
    public ItemCircleBuilder y(int y) {
        return (ItemCircleBuilder)super.y(y);
    }

    @Override
    public ItemCircleBuilder y(String y) {
        return (ItemCircleBuilder)super.y(y);
    }

    public ItemCircleBuilder ringMax(int rowMax) {
        this.ringMax = Either.left(rowMax);
        return this;
    }

    public ItemCircleBuilder ringMax(String rowMax) {
        this.ringMax = Either.right(rowMax);
        return this;
    }

    public ItemCircleBuilder radius(int radius) {
        this.radius = Either.left(radius);
        return this;
    }

    public ItemCircleBuilder radius(String radius) {
        this.radius = Either.right(radius);
        return this;
    }

    @Override
    protected void build(JsonObject json, RegistryOps<JsonElement> ops) {
        json.add("items", resolve(items, l -> ItemStack.CODEC.listOf().encodeStart(ops, l).getOrThrow()));
        if(ringMax != null)
            json.add("ring_max", resolveNum(ringMax));
        if(radius != null)
            json.add("radius", resolveNum(radius));
    }

}
