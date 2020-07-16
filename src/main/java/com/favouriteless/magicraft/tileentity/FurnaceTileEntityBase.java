package com.favouriteless.magicraft.tileentity;

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
    public int getSizeInventory() {
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
    public void read(CompoundNBT compound) {
        super.read(compound);
        this.inventoryContents = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(compound, this.inventoryContents);
        this.burnTime = compound.getInt("BurnTime");
        this.cookTime = compound.getInt("CookTime");
        this.cookTimeTotal = compound.getInt("CookTimeTotal");
        this.recipesUsed = this.getBurnTime(this.inventoryContents.get(1));
        int i = compound.getShort("RecipesUsedSize");

        for(int j = 0; j < i; ++j) {
            ResourceLocation resourcelocation = new ResourceLocation(compound.getString("RecipeLocation" + j));
            int k = compound.getInt("RecipeAmount" + j);
            this.field_214022_n.put(resourcelocation, k);
        }

        this.readAdditional(compound);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        compound.putInt("BurnTime", this.burnTime);
        compound.putInt("CookTime", this.cookTime);
        compound.putInt("CookTimeTotal", this.cookTimeTotal);
        ItemStackHelper.saveAllItems(compound, this.inventoryContents);
        compound.putShort("RecipesUsedSize", (short)this.field_214022_n.size());
        int i = 0;

        for(Map.Entry<ResourceLocation, Integer> entry : this.field_214022_n.entrySet()) {
            compound.putString("RecipeLocation" + i, entry.getKey().toString());
            compound.putInt("RecipeAmount" + i, entry.getValue());
            ++i;
        }

        this.writeAdditional(compound);
        return compound;
    }

    protected void readAdditional(CompoundNBT nbt) {}

    protected CompoundNBT writeAdditional(CompoundNBT nbt) {
        return nbt;
    }

    @Override
    public boolean receiveClientEvent(int id, int type) {
        if(id == 1) {
            this.numPlayersUsing = type;
            return true;
        } else {
            return super.receiveClientEvent(id, type);
        }
    }

    @Override
    public void openInventory(PlayerEntity player) {
        if(!player.isSpectator()) {
            if(numPlayersUsing < 0 ) {
                numPlayersUsing = 0;
            }
            this.numPlayersUsing++;
            this.onOpenOrClose();
        }
    }

    @Override
    public void closeInventory(PlayerEntity player) {
        if(!player.isSpectator()) {
            this.numPlayersUsing--;
            this.onOpenOrClose();
        }
    }

    protected void onOpenOrClose() {
        Block block = this.getBlockState().getBlock();
        this.world.addBlockEvent(this.pos, block, 1, this.numPlayersUsing);
        this.world.notifyNeighborsOfStateChange(this.pos, block);
    }

    public static int getNumPlayersUsing(IBlockReader reader, BlockPos pos) {
        BlockState state = reader.getBlockState(pos);
        if(state.hasTileEntity()) {
            TileEntity tileEntity = reader.getTileEntity(pos);
            if(tileEntity instanceof FurnaceTileEntityBase) {
                return ((FurnaceTileEntityBase)tileEntity).numPlayersUsing;
            }
        }
        return 0;
    }

    public static void swapContents(FurnaceTileEntityBase te, FurnaceTileEntityBase otherTe) {
        NonNullList<ItemStack> list = te.getItems();
        te.setItems(otherTe.getItems());
        otherTe.setItems(list);
    }

    @Override
    public void updateContainingBlockInfo() {
        super.updateContainingBlockInfo();
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
    public void remove() {
        super.remove();
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

        if (!this.world.isRemote) {
            ItemStack itemstack = this.inventoryContents.get(1);
            if (this.isBurning() || !itemstack.isEmpty() && !this.inventoryContents.get(0).isEmpty()) {
                IRecipe<?> irecipe = this.world.getRecipeManager().getRecipe(IRecipeType.SMELTING, this, this.world).orElse(null);
                if (!this.isBurning() && this.canSmelt(irecipe)) {
                    this.burnTime = this.getBurnTime(itemstack);
                    this.recipesUsed = this.burnTime;
                    if (this.isBurning()) {
                        flag1 = true;
                        if (itemstack.hasContainerItem())
                            this.inventoryContents.set(1, itemstack.getContainerItem());
                        else
                        if (!itemstack.isEmpty()) {
                            Item item = itemstack.getItem();
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
                this.world.setBlockState(this.pos, this.world.getBlockState(this.pos).with(AbstractFurnaceBlock.LIT, Boolean.valueOf(this.isBurning())), 3);
            }
        }

        if (flag1) {
            this.markDirty();
        }

    }

    protected boolean isBurning() {
        return this.burnTime > 0;
    }

    public static boolean isFuel(ItemStack stack) {
        return net.minecraftforge.common.ForgeHooks.getBurnTime(stack) > 0;
    }

    protected boolean canSmelt(@Nullable IRecipe<?> recipeIn) {
        if (!this.inventoryContents.get(0).isEmpty() && recipeIn != null) {
            ItemStack itemstack = recipeIn.getRecipeOutput();
            if (itemstack.isEmpty()) {
                return false;
            } else {
                ItemStack itemstack1 = this.inventoryContents.get(2);
                if (itemstack1.isEmpty()) {
                    return true;
                } else if (!itemstack1.isItemEqual(itemstack)) {
                    return false;
                } else if (itemstack1.getCount() + itemstack.getCount() <= this.getInventoryStackLimit() && itemstack1.getCount() + itemstack.getCount() <= itemstack1.getMaxStackSize()) { // Forge fix: make furnace respect stack sizes in furnace recipes
                    return true;
                } else {
                    return itemstack1.getCount() + itemstack.getCount() <= itemstack.getMaxStackSize(); // Forge fix: make furnace respect stack sizes in furnace recipes
                }
            }
        } else {
            return false;
        }
    }

    protected void smelt(@Nullable IRecipe<?> recipe) {
        if (recipe != null && this.canSmelt(recipe)) {
            ItemStack itemstack = this.inventoryContents.get(0);
            ItemStack itemstack1 = recipe.getRecipeOutput();
            ItemStack itemstack2 = this.inventoryContents.get(2);
            if (itemstack2.isEmpty()) {
                this.inventoryContents.set(2, itemstack1.copy());
            } else if (itemstack2.getItem() == itemstack1.getItem()) {
                itemstack2.grow(itemstack1.getCount());
            }

            if (!this.world.isRemote) {
                this.setRecipeUsed(recipe);
            }

            if (itemstack.getItem() == Blocks.WET_SPONGE.asItem() && !this.inventoryContents.get(1).isEmpty() && this.inventoryContents.get(1).getItem() == Items.BUCKET) {
                this.inventoryContents.set(1, new ItemStack(Items.WATER_BUCKET));
            }

            itemstack.shrink(1);
        }
    }

    protected int getBurnTime(ItemStack fuel) {
        if (fuel.isEmpty()) {
            return 0;
        } else {
            Item item = fuel.getItem();
            return net.minecraftforge.common.ForgeHooks.getBurnTime(fuel);
        }
    }

    protected int getCookTime() {
        return this.world.getRecipeManager().getRecipe(IRecipeType.SMELTING, this, this.world).map(AbstractCookingRecipe::getCookTime).orElse(200);
    }

    protected void setRecipeUsed(@Nullable IRecipe<?> recipe) {
        if (recipe != null) {
            this.field_214022_n.compute(recipe.getId(), (p_214004_0_, p_214004_1_) -> {
                return 1 + (p_214004_1_ == null ? 0 : p_214004_1_);
            });
        }

    }

    @Nullable
    protected IRecipe<?> getRecipeUsed() {
        return null;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        ItemStack itemstack = this.inventoryContents.get(index);
        boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack) && net.minecraft.item.ItemStack.areItemStackTagsEqual(stack, itemstack);
        this.inventoryContents.set(index, stack);
        if (stack.getCount() > this.getInventoryStackLimit()) {
            stack.setCount(this.getInventoryStackLimit());
        }

        if (index == 0 && !flag) {
            this.cookTimeTotal = this.getCookTime();
            this.cookTime = 0;
            this.markDirty();
        }

    }
}
