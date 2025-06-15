package net.favouriteless.enchanted.common.blocks.entity;

import net.favouriteless.enchanted.client.particles.types.ColourOptions;
import net.favouriteless.enchanted.common.init.EBlocks;
import net.favouriteless.enchanted.common.init.EParticleTypes;
import net.favouriteless.enchanted.common.init.ERecipeTypes;
import net.favouriteless.enchanted.common.recipes.WitchCauldronRecipe;
import net.favouriteless.enchanted.common.recipes.recipe_inputs.ListInput;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;
import net.minecraft.world.level.block.state.BlockState;

public class WitchCauldronBlockEntity extends CauldronBlockEntity<WitchCauldronRecipe> {

    public WitchCauldronBlockEntity(BlockPos pos, BlockState state) {
        super(EBlockEntityTypes.WITCH_CAULDRON.get(), pos, state, 3, 200);
    }

    @Override
    public double getWaterStartY(BlockState state) {
        return 0.0625D;
    }

    @Override
    public double getWaterMaxHeight() {
        return 0.4375D;
    }

    @Override
    public double getWaterWidth() {
        return 0.875D;
    }

    @Override
    public void handleCookParticles(long time) {
        double dx = worldPosition.getX() + 0.5D;
        double dy = worldPosition.getY() + getWaterY(EBlocks.WITCH_CAULDRON.get().defaultBlockState());
        double dz = worldPosition.getZ() + 0.5D;

        level.addParticle(new ColourOptions(EParticleTypes.CAULDRON_COOK.get(), FastColor.ARGB32.color(getRed(time), getGreen(time), getBlue(time))), dx, dy, dz, 0.0D, 0.0D, 0.0D);
    }

    @Override
    protected void matchRecipes() {
        if (level != null)
            setPotentialRecipes(level.getRecipeManager().getRecipesFor(ERecipeTypes.WITCH_CAULDRON.get(), ListInput.of(inventory), level));
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.witch_cauldron");
    }

}
