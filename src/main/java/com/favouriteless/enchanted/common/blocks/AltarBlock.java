package com.favouriteless.enchanted.common.blocks;

import com.favouriteless.enchanted.common.multiblock.altar.AltarMultiBlock;
import com.favouriteless.enchanted.common.multiblock.altar.AltarPartIndex;
import com.favouriteless.enchanted.common.tileentity.DistilleryTileEntity;
import com.favouriteless.enchanted.core.init.EnchantedBlocks;
import com.favouriteless.enchanted.core.util.MultiBlockTools;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class AltarBlock extends Block {

    public static final EnumProperty<AltarPartIndex> FORMED = EnumProperty.<AltarPartIndex>create("formed", AltarPartIndex.class);
    public static final BooleanProperty FACING_X = BooleanProperty.create("facing_x");

    public AltarBlock() {
        super(AbstractBlock.Properties.copy(Blocks.STONE)
                .requiresCorrectToolForDrops()
                .strength(1.5F, 6.0F)
        );

        this.registerDefaultState(defaultBlockState().setValue(FORMED, AltarPartIndex.UNFORMED).setValue(FACING_X, true));
    }

    @Override
    public void setPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if(!world.isClientSide()) {
            MultiBlockTools.formMultiblock(AltarMultiBlock.INSTANCE, world, pos);
        }
    }

    @Override
    public void onRemove(BlockState state, World world, BlockPos pos, BlockState newState, boolean pIsMoving) {
        if(!world.isClientSide()) {
            if(state != newState && !newState.is(EnchantedBlocks.ALTAR.get())) {
                MultiBlockTools.breakMultiblock(AltarMultiBlock.INSTANCE, world, pos, state);
            }
        }
        super.onRemove(state, world, pos, newState, pIsMoving);
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FORMED);
        builder.add(FACING_X);
    }

    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        return ActionResultType.FAIL;
    }

}
