package net.favouriteless.enchanted.common.enchanted.curses.curses;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.enchanted.common.init.ETags.Biomes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;

public class CurseOverheating extends AbstractRandomCurse {

    private static final MapCodec<CurseOverheating> CODEC = RecordCodecBuilder.mapCodec(instance -> codecStart(instance).apply(instance, CurseOverheating::new));
    public static final Type<CurseOverheating> TYPE = new Type<>(Enchanted.id("overheating"), () -> new CurseOverheating(0), CODEC);

    private static final RandomSource random = RandomSource.create();

    public CurseOverheating(long next) {
        super(30, 90, next);
    }

    @Override
    protected void execute(ServerPlayer target, int strength, long age) {
        ServerLevel level = target.serverLevel();
        if(level.dimension() != Level.NETHER && !level.getBiome(target.blockPosition()).is(Biomes.OVERHEATING_BIOMES))
            return;

        int duration = 4;
        for(int i = 0; i < strength; i++) {
            if(Math.random() < 0.75D)
                duration += 4;
        }
        target.setRemainingFireTicks(duration * 20);
    }

    @Override
    public Type<?> type() {
        return TYPE;
    }

}
