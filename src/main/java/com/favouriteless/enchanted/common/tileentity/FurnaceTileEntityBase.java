package com.favouriteless.enchanted.common.tileentity;

import com.google.common.collect.Maps;
import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.InvWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

public abstract class FurnaceTileEntityBase extends LockableLootTileEntity implements ITickableTileEntity {

    protected NonNullList<ItemStack> inventoryContents;
    protected IItemHandlerModifiable items = createHandler();
    protected LazyOptional<IItemHandlerModifiable> itemHandler = LazyOptional.of(() -> items);

    protected int numPlayersUsing;

    protected int burnTime;
    protected int recipesUsed;
    protected int cookTime;
    protected int cookTimeTotal;
    protected final IIntArray furnaceData = new IIntArray() {
        public int get(int index) {
            switch (index) {
                case 0:
                    return FurnaceTileEntityBase.this.burnTime;
                case 1:
                    return FurnaceTileEntityBase.this.recipesUsed;
                case 2:
                    return FurnaceTileEntityBase.this.cookTime;
                case 3:
                    return FurnaceTileEntityBase.this.cookTimeTotal;
                default:
                    return 0;
            }
        }

        public void set(int index, int value) {
            switch (index) {
                case 0:
                    FurnaceTileEntityBase.this.burnTime = value;
                    break;
                case 1:
                    FurnaceTileEntityBase.this.recipesUsed = value;
                    break;
                case 2:
                    FurnaceTileEntityBase.this.cookTime = value;
                    break;
                case 3:
                    FurnaceTileEntityBase.this.cookTimeTotal = value;
            }

        }

        @Override
        public int getCount() {
            return 4;
        }

        public int size() {
            return 4;
        }
    };
    protected final Map<ResourceLocation, Integer> field_214022_n = Maps.newHashMap();

    public FurnaceTileEntityBase(TileEntityType<?> typeIn, NonNullList<ItemStack> inventoryContents) {
        super(typeIn);
        this.inventoryContents = inventoryContents;
    }

    public IIntArray getFurnaceData() {
        return this.furnaceData;
    }

    @Override
    public int getContainerSize() {
        return inventoryContents.size();
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return this.inventoryContents;
    }

    @Override
    public void setItems(NonNullList<ItemStack> itemsIn) {
        this.inventoryContents = itemsIn;
    }

    @Override
    protected abstract ITextComponent getDefaultName();

    @Override
    public void load(BlockState state, CompoundNBT nbt) {
        super.load(state, nbt);
        this.inventoryContents = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(nbt, this.inventoryContents);
        this.burnTime = nbt.getInt("BurnTime");
        this.cookTime = nbt.getInt("CookTime");
        this.cookTimeTotal = nbt.getInt("CookTimeTotal");
        this.recipesUsed = this.getBurnTime(this.inventoryContents.get(1));
        int i = nbt.getShort("RecipesUsedSize");

        for(int j = 0; j < i; ++j) {
            ResourceLocation resourcelocation = new ResourceLocation(nbt.getString("RecipeLocation" + j));
            int k = nbt.getInt("RecipeAmount" + j);
            this.field_214022_n.put(resourcelocation, k);
        }

        this.loadAdditional(nbt);
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        super.save(nbt);
        nbt.putInt("BurnTime", this.burnTime);
        nbt.putInt("CookTime", this.cookTime);
        nbt.putInt("CookTimeTotal", this.cookTimeTotal);
        ItemStackHelper.saveAllItems(nbt, this.inventoryContents);
        nbt.putShort("RecipesUsedSize", (short)this.field_214022_n.size());
        int i = 0;

        for(Map.Entry<ResourceLocation, Integer> entry : this.field_214022_n.entrySet()) {
            nbt.putString("RecipeLocation" + i, entry.getKey().toString());
            nbt.putInt("RecipeAmount" + i, entry.getValue());
            ++i;
        }

        this.saveAdditional(nbt);
        return nbt;
    }

    protected void loadAdditional(CompoundNBT nbt) {}

    protected CompoundNBT saveAdditional(CompoundNBT nbt) {
        return nbt;
    }

    @Override
    public boolean triggerEvent(int id, int type) {
        if(id == 1) {
            this.numPlayersUsing = type;
            return true;
        } else {
            return super.triggerEvent(id, type);
        }
    }

    @Override
    public void startOpen(PlayerEntity player) {
        if(!player.isSpectator()) {
            if(numPlayersUsing < 0 ) {
                numPlayersUsing = 0;
            }
            this.numPlayersUsing++;
            this.onOpenOrClose();
        }
    }

    @Override
    public void stopOpen(PlayerEntity player) {
        if(!player.isSpectator()) {
            this.numPlayersUsing--;
            this.onOpenOrClose();
        }
    }

    protected void onOpenOrClose() {
        Block block = this.getBlockState().getBlock();
        this.level.blockEvent(this.worldPosition, block, 1, this.numPlayersUsing);
        this.level.updateNeighborsAt(this.worldPosition, block);
    }

    public static int getNumPlayersUsing(IBlockReader reader, BlockPos pos) {
        BlockState state = reader.getBlockState(pos);
        if(state.hasTileEntity()) {
            TileEntity tileEntity = reader.getBlockEntity(pos);
            if(tileEntity instanceof FurnaceTileEntityBase) {
                return ((FurnaceTileEntityBase)tileEntity).numPlayersUsing;
            }
        }
        return 0;
    }

    public static void swapContents(FurnaceTileEntityBase tileEntity, FurnaceTileEntityBase otherTileEntity) {
        NonNullList<ItemStack> list = tileEntity.getItems();
        tileEntity.setItems(otherTileEntity.getItems());
        otherTileEntity.setItems(list);
    }

    @Override
    public void clearCache() {
        super.clearCache();
        if(this.itemHandler != null) {
            this.itemHandler.invalidate();
            this.itemHandler = null;
        }
    }

    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nonnull Direction side) {
        if(cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return itemHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    private IItemHandlerModifiable createHandler() {
        return new InvWrapper(this);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        if(itemHandler != null) {
            itemHandler.invalidate();
        }
    }

    @Override
    public void tick() {
        boolean flag = this.isBurning();
        boolean flag1 = false;
        if (this.isBurning()) {
            --this.burnTime;
        }

        if (!this.level.isClientSide) {
            ItemStack itemstack = this.inventoryContents.get(1);
            if (this.isBurning() || !itemstack.isEmpty() && !this.inventoryContents.get(0).isEmpty()) {
                IRecipe<?> irecipe = this.level.getRecipeManager().getRecipeFor(IRecipeType.SMELTING, this, this.level).orElse(null);
                if (!this.isBurning() && this.canSmelt(irecipe)) {
                    this.burnTime = this.getBurnTime(itemstack);
                    this.recipesUsed = this.burnTime;
                    if (this.isBurning()) {
                        flag1 = true;
                        if (itemstack.hasContainerItem())
                            this.inventoryContents.set(1, itemstack.getContainerItem());
                        else
                        if (!itemstack.isEmpty()) {
                            itemstack.shrink(1);
                            if (itemstack.isEmpty()) {
                                this.inventoryContents.set(1, itemstack.getContainerItem());
                            }
                        }
                    }
                }

                if (this.isBurning() && this.canSmelt(irecipe)) {
                    ++this.cookTime;
                    if (this.cookTime == this.cookTimeTotal) {
                        this.cookTime = 0;
                        this.cookTimeTotal = this.getCookTime();
                        this.smelt(irecipe);
                        flag1 = true;
                    }
                } else {
                    this.cookTime = 0;
                }
            } else if (!this.isBurning() && this.cookTime > 0) {
                this.cookTime = MathHelper.clamp(this.cookTime - 2, 0, this.cookTimeTotal);
            }

            if (flag != this.isBurning()) {
                flag1 = true;
                this.level.setBlock(this.worldPosition, this.level.getBlockState(this.worldPosition).setValue(AbstractFurnaceBlock.LIT, Boolean.valueOf(this.isBurning())), 3);
            }
        }

        if (flag1) {
            this.setChanged();
        }

    }

    protected boolean isBurning() {
        return this.burnTime > 0;
    }

    public static boolean isFuel(ItemStack stack) {
        return net.minecraftforge.common.ForgeHooks.getBurnTime(stack) > 0;
    }

    protected boolean canSmelt(@Nullable IRecipe<?> recipeIn) {
        if (!this.inventoryContents.get(0).isEmpty() && recipeIn != null) { // If item in input & recipe not null

            ItemStack resultStack = recipeIn.getResultItem();

            if (resultStack.isEmpty()) { // If recipe makes nothing
                return false;
            } else {
                ItemStack outputStack = this.inventoryContents.get(2);
                if (outputStack.isEmpty()) { // If output is empty
                    return true;
                } else if (!outputStack.sameItem(resultStack)) { // If output is a different item
                    return false;
                } else if (outputStack.getCount() + resultStack.getCount() <= this.getMaxStackSize() && outputStack.getCount() + resultStack.getCount() <= outputStack.getMaxStackSize()) { // Forge fix: make furnace respect stack sizes in furnace recipes
                    return true;
                } else {
                    return outputStack.getCount() + resultStack.getCount() <= resultStack.getMaxStackSize(); // Forge fix: make furnace respect stack sizes in furnace recipes
                }
            }
        } else {
            return false;
        }
    }

    protected void smelt(@Nullable IRecipe<?> recipe) {
        if (recipe != null && this.canSmelt(recipe)) { // If valid recipe

            ItemStack inputStack = this.inventoryContents.get(0);
            ItemStack resultStack = recipe.getResultItem();
            ItemStack outputStack = this.inventoryContents.get(2);

            if (outputStack.isEmpty()) { // If output is empty
                this.inventoryContents.set(2, resultStack.copy());
            } else if (outputStack.getItem() == resultStack.getItem()) { // Else if result = output
                outputStack.grow(resultStack.getCount());
            }

            if (!this.level.isClientSide) {
                this.setRecipeUsed(recipe);
            }

            if (inputStack.getItem() == Blocks.WET_SPONGE.asItem() && !this.inventoryContents.get(1).isEmpty() && this.inventoryContents.get(1).getItem() == Items.BUCKET) {
                this.inventoryContents.set(1, new ItemStack(Items.WATER_BUCKET));
            }

            inputStack.shrink(1);
        }
    }

    protected int getBurnTime(ItemStack fuel) {
        if (fuel.isEmpty()) {
            return 0;
        } else {
            return net.minecraftforge.common.ForgeHooks.getBurnTime(fuel);
        }
    }

    protected int getCookTime() {
        return this.level.getRecipeManager().getRecipeFor(IRecipeType.SMELTING, this, this.level).map(AbstractCookingRecipe::getCookingTime).orElse(200);
    }

    /**
     * Zero clue what this entire method does but I needed it
     * @param recipe
     */
    protected void setRecipeUsed(@Nullable IRecipe<?> recipe) {
        if (recipe != null) {
            this.field_214022_n.compute(recipe.getId(), (p_214004_0_, p_214004_1_) -> 1 + (p_214004_1_ == null ? 0 : p_214004_1_));
        }
    }

    @Nullable
    protected IRecipe<?> getRecipeUsed() {
        return null;
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        ItemStack itemstack = this.inventoryContents.get(index);
        boolean flag = !stack.isEmpty() && stack.sameItem(itemstack) && net.minecraft.item.ItemStack.tagMatches(stack, itemstack);
        this.inventoryContents.set(index, stack);
        if (stack.getCount() > this.getMaxStackSize()) {
            stack.setCount(this.getMaxStackSize());
        }

        if (index == 0 && !flag) {
            this.cookTimeTotal = this.getCookTime();
            this.cookTime = 0;
            this.setChanged();
        }

    }
}