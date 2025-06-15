package net.favouriteless.enchanted.common.items;

import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.enchanted.api.taglock.BedTaglockSavedData;
import net.favouriteless.enchanted.api.taglock.IBedTaglock;
import net.favouriteless.enchanted.common.blocks.entity.BloodPoppyBlockEntity;
import net.favouriteless.enchanted.common.blocks.crops.BloodPoppyBlock;
import net.favouriteless.enchanted.common.init.EItems;
import net.favouriteless.enchanted.common.init.ETags.EntityTypes;
import net.favouriteless.enchanted.common.init.EBlocks;
import net.favouriteless.enchanted.common.items.component.EDataComponents;
import net.favouriteless.enchanted.common.items.component.EntityRefData;
import net.favouriteless.enchanted.common.util.ItemUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.entity.BedBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public class EmptyTaglockItem extends Item {

    public EmptyTaglockItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
        if(target instanceof Player) {
            double failChance = 0.2D;
            if (!player.isCrouching())
                failChance += 0.2D;
            if(!facingAway(player, target))
                failChance += 0.4D;

            if(Math.random() >= failChance) {
                if(!player.level().isClientSide) {
                    player.displayClientMessage(Component.literal("Taglock attempt failed").withStyle(ChatFormatting.RED), false);
                    ((ServerPlayer)target).displayClientMessage(Component.literal(player.getDisplayName().getString() + " tried to taglock you").withStyle(ChatFormatting.RED), false);
                }
                return InteractionResult.FAIL;
            }
        }
        if(!target.getType().is(EntityTypes.TAGLOCK_BLACKLIST))
            fillTaglockEntity(player, stack, target);

        return InteractionResult.SUCCESS;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockState state = level.getBlockState(context.getClickedPos());
        BlockPos pos = context.getClickedPos();

        if(state.getBlock() instanceof BedBlock) {
            if(!level.isClientSide) {
                BlockEntity be = state.getValue(BedBlock.PART) == BedPart.HEAD ?
                        level.getBlockEntity(pos) :
                        level.getBlockEntity(pos.relative(BedBlock.getConnectedDirection(state)));

                if(be instanceof BedBlockEntity bed) {
                    BedTaglockSavedData data = BedTaglockSavedData.get(level);
                    IBedTaglock bedEntry = data.getEntry(bed);

                    if(bedEntry.getData() != null) {
                        fillTaglock(context.getPlayer(), context.getItemInHand(), bedEntry.getData());
                        bedEntry.setData(null);
                        data.setDirty();
                    }
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        if(state.getBlock() == EBlocks.BLOOD_POPPY.get()) {
            if(!level.isClientSide) {
                if(level.getBlockEntity(pos) instanceof BloodPoppyBlockEntity poppy) {
                    fillTaglock(context.getPlayer(), context.getItemInHand(), poppy.getTaglockData());
                    BloodPoppyBlock.reset(level, pos);
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        return InteractionResult.PASS;
    }

    protected void fillTaglockEntity(Player player, ItemStack stack, LivingEntity entity) {
        fillTaglock(player, stack, EntityRefData.of(entity.getUUID(), entity.getDisplayName().getString()));
    }

    protected void fillTaglock(Player player, ItemStack stack, EntityRefData data) {
        if(player instanceof ServerPlayer serverPlayer) {
            ItemStack item = new ItemStack(EItems.TAGLOCK_FILLED.get(), 1);
            item.set(EDataComponents.ENTITY_REF.get(), data);

            ItemUtils.giveOrDrop(player, item);

            // Send sound packet to player
            serverPlayer.connection.send(new ClientboundSoundPacket(BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.EXPERIENCE_ORB_PICKUP), SoundSource.MASTER, serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ(), 1.0F, 1.0F, Enchanted.RANDOM.nextLong()));
            stack.shrink(1);
        }
    }

    protected boolean facingAway(Player source, Entity target){
        Vec3 sourceLook = source.getLookAngle().normalize();
        Vec3 targetLook = target.getLookAngle().normalize();

        Vec2 v1 = new Vec2((float)sourceLook.x, (float)sourceLook.z);
        Vec2 v2 = new Vec2((float)targetLook.x, (float)targetLook.z);

        return !(Math.acos((v1.x * v2.x + v1.y *v2.y) / (Mth.sqrt(v1.x * v1.x + v1.y * v1.y) * Mth.sqrt(v2.x * v2.x + v2.y * v2.y))) > Mth.PI/2);
    }


}
