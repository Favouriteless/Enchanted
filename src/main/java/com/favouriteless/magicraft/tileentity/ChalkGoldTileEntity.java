package com.favouriteless.magicraft.tileentity;

import com.favouriteless.magicraft.init.MagicraftRituals;
import com.favouriteless.magicraft.init.MagicraftTileEntities;
import com.favouriteless.magicraft.rituals.Ritual;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber
public class ChalkGoldTileEntity extends TileEntity implements ITickableTileEntity {

    private boolean executing = false;
    private int ticks = 0;

    public final String[] ritualGlyphs = new String[225];
    public List<Entity> ritualEntities = new ArrayList<Entity>();

    public ChalkGoldTileEntity(final TileEntityType<?> tileEntityTypeIn) { super(tileEntityTypeIn); }

    public ChalkGoldTileEntity()
    {
        this(MagicraftTileEntities.CHALK_GOLD.get());
    }

    public void Execute(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if(!executing && !world.isRemote()) {
            this.executing = true;

            BlockPos corner = pos.add(-7, 0, -7);
            for (int z = 0; z < 15; z++) {
                for (int x = 0; x < 15; x++) {
                    Material blockMaterial = world.getBlockState(corner.add(x, 0, z)).getMaterial();
                    String type = "XX";

                    for(Material material : MagicraftRituals.CHARACTER_MAP.keySet())
                    {
                        if (material == blockMaterial) {
                            type = MagicraftRituals.CHARACTER_MAP.get(material);
                            break;
                        }
                    }

                    ritualGlyphs[(x + (z*15))] = type;

                }
            }
            this.ritualEntities = world.getEntitiesWithinAABBExcludingEntity(player, new AxisAlignedBB(corner, corner.add(15, 2, 15)));
            List<Ritual> currentRituals = MagicraftRituals.GetRituals(this.ritualGlyphs, this.ritualEntities);

            if(!currentRituals.isEmpty()) {
                for(Ritual ritual : currentRituals) {
                    ritual.StartRitual(state, (ServerWorld)world, pos, player);
                }
            } else {
                this.world.playSound(null, pos.getX(), pos.getY() + 1, pos.getZ(), SoundEvents.BLOCK_NOTE_BLOCK_SNARE, SoundCategory.MASTER, 2f, 1f);
            }
        }
    }

    @Override
    public void tick() {
        if(executing) {
            ticks++;
            if(ticks >= 40) {
                ticks = 0;
                executing = false;
            }
        }
    }

}
