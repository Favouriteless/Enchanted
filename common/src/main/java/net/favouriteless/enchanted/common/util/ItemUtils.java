package net.favouriteless.enchanted.common.util;

import com.google.gson.JsonObject;
import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.enchanted.platform.CommonServices;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NumericTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Objects;
import java.util.Set;

/**
 * Util class for functions related to {@link ItemStack}.
 */
public class ItemUtils {

    /**
     * Drops copies of a {@link Container}'s items on the floor, without modifying the ones stored in the container.
     */
    public static void dropContentsNoChange(Level level, double x, double y, double z, Container inventory) {
        for(int i = 0; i < inventory.getContainerSize(); ++i) {
            ItemStack item = inventory.getItem(i).copy();

            double width = EntityType.ITEM.getWidth();
            double inverseWidth = 1.0D - width;
            double radius = width / 2.0D;
            double dx = Math.floor(x) + Enchanted.RANDOM.nextDouble() * inverseWidth + radius;
            double dy = Math.floor(y) + Enchanted.RANDOM.nextDouble() * inverseWidth;
            double dz = Math.floor(z) + Enchanted.RANDOM.nextDouble() * inverseWidth + radius;

            while(!item.isEmpty()) {
                ItemEntity entity = new ItemEntity(level, dx, dy, dz, item.split(Enchanted.RANDOM.nextInt(21) + 10));
                entity.setDeltaMovement(Enchanted.RANDOM.nextGaussian() * 0.05D, Enchanted.RANDOM.nextGaussian() * 0.05D + 0.2D, Enchanted.RANDOM.nextGaussian() * 0.05D);
                level.addFreshEntity(entity);
            }
        }
    }

    /**
     * Check if a given {@link ItemStack} is furnace fuel or not.
     *
     * @param stack The {@link ItemStack} to be checked.
     * @return True if stack's burn value > 0.
     */
    public static boolean isFuel(ItemStack stack) {
        return CommonServices.PLATFORM.getBurnTime(stack, null) > 0;
    }

    /**
     * @return true if <b>a</b> is the same item as <b>b</b> AND all tags on <b>b</b> are present and equal on
     * <b>a</b>.
     */
    public static boolean isSameItemPartial(ItemStack a, ItemStack b) {
        if(a.getItem() != b.getItem())
            return false;

        if(b.hasTag()) {
            if(!a.hasTag())
                return false;
        }
        else {
            return true;
        }

        // Check if all tags in b are present and equal in a
        CompoundTag aTags = a.getTag();
        CompoundTag bTags = b.getTag();
        return compareNbt(aTags, bTags);
    }

    private static boolean compareNbt(Tag aTag, Tag bTag) {
        if(aTag == null)
            return bTag == null;

        if(Objects.equals(aTag, bTag))
            return true;

        if(aTag instanceof NumericTag aNumeric && bTag instanceof NumericTag bNumeric) {
            if(aNumeric.getAsFloat() == bNumeric.getAsFloat())
                return true;
        }

        if(aTag instanceof CompoundTag aCompound && bTag instanceof CompoundTag bCompound)
            return bCompound.getAllKeys().stream().allMatch(key -> compareNbt(aCompound.get(key), bCompound.get(key)));

        return false;
    }

    public static void giveOrDrop(Player player, ItemStack item) {
        if(player.addItem(item))
            return;

        ItemEntity entity = new ItemEntity(player.level(), player.getX(), player.getY(), player.getZ(), item);
        entity.setNoPickUpDelay();
        entity.setThrower(player.getUUID());
        player.level().addFreshEntity(player);
    }

    /**
     * Parse a given {@link JsonObject} to get an {@link ItemStack}.
     *
     * @param json The {@link JsonObject} to parse.
     * @param readNbt True if NBT data should be parsed.
     * @return The {@link ItemStack} which was stored in json.
     */
    public static ItemStack fromJson(JsonObject json, boolean readNbt) {
        Item item = BuiltInRegistries.ITEM.get(new ResourceLocation(GsonHelper.getAsString(json, "item")));
        int count = GsonHelper.getAsInt(json, "count", 1); // Get count, default to 1 if there was no count.
        ItemStack out = new ItemStack(item, count);

        if (readNbt && json.has("nbt"))
            out.setTag(JsonHelper.readTag(json.get("nbt")));

        return out;
    }

    /**
     * Serialize a given {@link ItemStack} to a {@link JsonObject}.
     *
     * @param item The {@link ItemStack} to serialize.
     * @param saveNbt True if item's {@link CompoundTag} should be serialized.
     * @return A {@link JsonObject} containing item.
     */
    public static JsonObject asJson(ItemStack item, boolean saveNbt) {
        return asJson(item.getItem(), item.getCount(), saveNbt && item.hasTag() ? item.getTag() : null);
    }

    /**
     * Serialize a given {@link Item}, count and {@link CompoundTag} to a {@link JsonObject} in the same format as an
     * {@link ItemStack}.
     *
     * @param item The {@link Item} of the {@link ItemStack}.
     * @param count The count of the {@link ItemStack}.
     * @return A {@link JsonObject} containing item.
     */
    public static JsonObject asJson(Item item, int count, CompoundTag nbt) {
        JsonObject obj = new JsonObject();

        obj.addProperty("item", BuiltInRegistries.ITEM.getKey(item).toString());
        if(count > 1)
            obj.addProperty("count", count);

        if(nbt != null) {
            CompoundTag noDamage = nbt.copy();
            noDamage.remove("Damage");
            if(!noDamage.isEmpty())
                obj.addProperty("nbt", noDamage.getAsString());
        }
        return obj;
    }

}