package net.favouriteless.enchanted.common.blocks.entity;

import net.favouriteless.enchanted.api.power.IPowerConsumer;
import net.favouriteless.enchanted.api.power.IPowerProvider;
import net.favouriteless.enchanted.api.power.PowerHelper;
import net.favouriteless.enchanted.common.altar.SimplePowerPosHolder;
import net.favouriteless.enchanted.common.menus.SpinningWheelMenu;
import net.favouriteless.enchanted.common.init.ERecipeTypes;
import net.favouriteless.enchanted.common.recipes.SpinningRecipe;
import net.favouriteless.enchanted.common.recipes.recipe_inputs.ListInput;
import net.favouriteless.enchanted.common.util.ItemUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeManager.CachedCheck;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SpinningWheelBlockEntity extends ContainerBlockEntityBase implements IPowerConsumer, MenuProvider, WorldlyContainer {

	private static final int[] INPUT_SLOTS = new int[] { 0, 1, 2 };
	private static final int[] BOTTOM_SLOTS = new int[] { 3 };

	private final SimplePowerPosHolder posHolder;
	private final CachedCheck<ListInput, SpinningRecipe> spinCheck;

	private int spinDuration = 300;
	private int spinProgress = 0;

	private boolean isSpinning = false; // This is only used clientside. We do this and change spinProgress in client tick to avoid snapping animations.

	public SpinningWheelBlockEntity(BlockPos pos, BlockState state) {
		super(EBlockEntityTypes.SPINNING_WHEEL.get(), pos, state, NonNullList.withSize(4, ItemStack.EMPTY));
		this.posHolder = new SimplePowerPosHolder(pos);
		this.spinCheck = RecipeManager.createCheck(ERecipeTypes.SPINNING.get());
	}

	public static void serverTick(Level level, BlockPos pos, BlockState state, SpinningWheelBlockEntity be) {
		RecipeHolder<SpinningRecipe> recipe = be.spinCheck.getRecipeFor(ListInput.of(be.inventory.subList(0, 3)), level).orElse(null);
		boolean wasSpinning = be.spinProgress > 0;

		IPowerProvider provider = PowerHelper.tryGetPowerProvider(level, be.posHolder);
		// Can fit result and has enough power to spin
		if(recipe != null && be.canSpin(recipe) && (recipe.value().getPower() == 0 ||
				(provider != null && provider.tryConsume((double)recipe.value().getPower() / be.spinDuration)))) {
			if(++be.spinProgress == be.spinDuration) {
				be.spinProgress = 0;
				be.spinDuration = recipe.value().getDuration();
				be.spin(recipe);
			}
		}
		else {
			be.spinProgress = 0;
		}

		if(wasSpinning != be.isSpinning())
			be.updateBlock();
	}

	public static void clientTick(Level level, BlockPos pos, BlockState state, SpinningWheelBlockEntity be) {
		if(be.isSpinning)
			be.spinProgress++;
		else
			be.spinProgress = 0;
	}

	/**
	 * Attempt to spin the items in this {@link SpinningWheelBlockEntity}'s input slots.
	 * @param recipe The {@link SpinningRecipe} to use.
	 */
	protected void spin(@NotNull RecipeHolder<SpinningRecipe> recipe) {
		for(ItemStack recipeStack : recipe.value().getInputs()) {
			for(int i = 0; i < 3; i++) {
				ItemStack input = inventory.get(i);
				if(ItemUtils.isSameItemPartial(input, recipeStack)) {
					input.shrink(recipeStack.getCount());
					break;
				}
			}
		}

		ItemStack result = recipe.value().assemble(null, null);
		ItemStack output = inventory.get(3);
		if(output.isEmpty())
			inventory.set(3, result);
		else
			output.grow(result.getCount());
	}

	/**
	 * Check if this {@link SpinningWheelBlockEntity} has enough space for the output of a given recipe.
	 */
	private boolean canSpin(@NotNull RecipeHolder<SpinningRecipe> recipe) {
		ItemStack result = recipe.value().assemble(null, null);
		if(result.isEmpty())
			return false;

		ItemStack output = inventory.get(3);

		if(output.isEmpty())
			return true;
		if(ItemStack.isSameItemSameComponents(result, output))
			return output.getCount() + result.getCount() <= output.getMaxStackSize();

		return false;
	}

	@Override
	public Component getDefaultName() {
		return Component.translatable("container.enchanted.spinning_wheel");
	}

	@Override
	public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
		return new SpinningWheelMenu(id, inventory, this, access);
	}

	@Override
	public void saveAdditional(@NotNull CompoundTag tag, @NotNull Provider registries) {
		super.saveAdditional(tag, registries);
		tag.put("posHolder", posHolder.serialize());
		tag.putInt("spinProgress", spinProgress);
		tag.putInt("spinDuration", spinDuration);
	}

	@Override
	public void loadAdditional(@NotNull CompoundTag tag, @NotNull Provider registries) {
		super.loadAdditional(tag, registries);
		if(tag.contains("posHolder"))
			posHolder.deserialize(tag.getCompound("posHolder"));
		spinProgress = tag.getInt("spinProgress"); // Don't need to check if exists, client wants this to reset anyway.
		isSpinning = tag.getBoolean("isSpinning");
	}

	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public @NotNull CompoundTag getUpdateTag(@NotNull Provider registries) {
		CompoundTag nbt = super.getUpdateTag(registries);
		nbt.putBoolean("isSpinning", spinProgress > 0);
		return nbt;
	}

	@Override
	public @NotNull IPowerPosHolder getPosHolder() {
		return posHolder;
	}

	public int getSpinProgress() {
		return spinProgress;
	}

	public boolean isSpinning() {
		return spinProgress > 0;
	}

	@Override
	public int[] getSlotsForFace(@NotNull Direction face) {
		if(face == Direction.DOWN)
			return BOTTOM_SLOTS;
		else
			return INPUT_SLOTS;
	}

	@Override
	public boolean canPlaceItemThroughFace(int index, @NotNull ItemStack stack, @Nullable Direction face) {
		return index != 3;
	}

	@Override
	public boolean canTakeItemThroughFace(int index, @NotNull ItemStack stack, @Nullable Direction face) {
		return index == 3;
	}

	@Override
	public void setItem(int index, @NotNull ItemStack stack) {
		boolean changed = stack.isEmpty() || !ItemStack.isSameItemSameComponents(inventory.get(index), stack);
		inventory.set(index, stack);

		if(index != 4 && changed) {
			spinDuration = getTotalSpinTime();
			spinProgress = 0;
			updateBlock();
			setChanged();
		}
	}

	private int getTotalSpinTime() {
		return spinCheck.getRecipeFor(ListInput.of(inventory.subList(0, 3)), level)
				.map(holder -> holder.value().getDuration()).orElse(300);
	}

	public ContainerData access = new ContainerData() {

		@Override
		public int get(int index) {
            return switch(index) {
                case 0 -> spinProgress;
                case 1 -> spinDuration;
                default -> 0;
            };
        }

		@Override
		public void set(int index, int value) {
			if(index == 0)
				spinProgress = value;
			else if(index == 2)
				spinDuration = value;
		}

		@Override
		public int getCount() {
			return 2;
		}

	};

}
