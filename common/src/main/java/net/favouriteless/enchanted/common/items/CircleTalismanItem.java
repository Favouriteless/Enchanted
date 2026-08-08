package net.favouriteless.enchanted.common.items;

import net.favouriteless.enchanted.common.enchanted.circle_magic.CircleMagicShape;
import net.favouriteless.enchanted.common.init.EBlocks;
import net.favouriteless.enchanted.common.init.EData;
import net.favouriteless.enchanted.common.items.component.EDataComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import java.util.HashMap;
import java.util.Map;

public class CircleTalismanItem extends Item {

    public CircleTalismanItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();

        if (!level.isClientSide) {
            ItemStack stack = context.getItemInHand();

            if (!(stack.getItem() instanceof CircleTalismanItem)) {
                return InteractionResult.CONSUME;
            }

            Registry<CircleMagicShape> registry = level.registryAccess().registryOrThrow(EData.CIRCLE_SHAPE_REGISTRY);

            BlockPos clicked = context.getClickedPos();
            BlockPos pos = level.getBlockState(clicked).canBeReplaced() ? clicked : clicked.above();

            Map<ResourceLocation, Block> shapes = stack.get(EDataComponents.CIRCLE_MAGIC_SHAPE_MAP.get());
            if (shapes.isEmpty()) {
                return InteractionResult.CONSUME;
            }

            boolean valid = level.getBlockState(pos).canBeReplaced() && EBlocks.GOLDEN_CHALK.get().canSurvive(null, level, pos);
            if (!valid) {
                return sendFail(context.getPlayer());
            }

            Map<CircleMagicShape, Block> toPlace = new HashMap<>();
            for (ResourceLocation location : shapes.keySet()) {
                CircleMagicShape shape = registry.get(location);
                if (shape == null) {
                    continue;
                }

                if (!shape.canPlace(level, pos)) {
                    return sendFail(context.getPlayer());
                } else {
                    toPlace.put(shape, shapes.get(location));
                }
            }
            toPlace.forEach((shape, block) -> shape.place(level, pos, block, context));
            level.setBlockAndUpdate(pos, EBlocks.GOLDEN_CHALK.get().getRandomState());
            stack.set(EDataComponents.CIRCLE_MAGIC_SHAPE_MAP.get(), new HashMap<>());
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    protected InteractionResult sendFail(Player player) {
        if (player != null) {
            player.displayClientMessage(Component.literal("All blocks must be valid spots.").withStyle(ChatFormatting.RED), true);
        }
        return InteractionResult.CONSUME;
    }

}
