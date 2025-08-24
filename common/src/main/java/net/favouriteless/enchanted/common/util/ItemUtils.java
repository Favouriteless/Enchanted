package net.favouriteless.enchanted.common.util;

import net.favouriteless.enchanted.platform.EServices;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;

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
			double dx = Math.floor(x) + RandomUtils.nextDouble() * inverseWidth + radius;
			double dy = Math.floor(y) + RandomUtils.nextDouble() * inverseWidth;
			double dz = Math.floor(z) + RandomUtils.nextDouble() * inverseWidth + radius;

			while(!item.isEmpty()) {
				ItemEntity entity = new ItemEntity(level, dx, dy, dz, item.split(RandomUtils.nextInt(21) + 10));
				entity.setDeltaMovement(RandomUtils.nextGaussian() * 0.05D, RandomUtils.nextGaussian() * 0.05D + 0.2D, RandomUtils.nextGaussian() * 0.05D);
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
		return EServices.PLATFORM.getBurnTime(stack, null) > 0;
	}

	/**
	 * @return true if <b>a</b> is the same item as <b>b</b> AND all components on <b>b</b> are present and equal on
	 * <b>a</b>.
	 */
	public static boolean isSameItemPartial(ItemStack a, ItemStack b) {
		if(a.getItem() != b.getItem())
			return false;

		for(Entry<DataComponentType<?>, Optional<?>> entry : b.getComponentsPatch().entrySet()) {
			Optional<?> optional = a.getComponentsPatch().get(entry.getKey());
            if(optional == null || optional.isEmpty())
                return false;

			Object aComp = optional.orElse(null);
			Object bComp = entry.getValue().orElse(null);

			if(!Objects.equals(aComp, bComp))
				return false;
		}

		return true;
	}

	public static void giveOrDrop(Player player, ItemStack item) {
		if(player.addItem(item))
			return;

		ItemEntity entity = new ItemEntity(player.level(), player.getX(), player.getY(), player.getZ(), item);
		entity.setNoPickUpDelay();
		entity.setThrower(player);
		player.level().addFreshEntity(player);
	}

	public static CompoundTag saveAllItems(CompoundTag tag, List<ItemStack> items, HolderLookup.Provider registries) {
		ListTag list = new ListTag();

        for(ItemStack stack : items) {
            if(!stack.isEmpty()) {
                CompoundTag itemTag = new CompoundTag();
                list.add(stack.save(registries, itemTag));
            }
        }

		tag.put("Items", list);
		return tag;
	}

	public static void loadAllItems(CompoundTag tag, List<ItemStack> items, HolderLookup.Provider registries) {
		ListTag list = tag.getList("Items", CompoundTag.TAG_COMPOUND);

		for(int i = 0; i < list.size(); i++) {
			items.add(ItemStack.parse(registries, list.getCompound(i)).orElse(ItemStack.EMPTY));
		}
	}

}
