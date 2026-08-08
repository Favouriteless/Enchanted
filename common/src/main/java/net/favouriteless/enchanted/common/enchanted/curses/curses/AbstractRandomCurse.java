package net.favouriteless.enchanted.common.enchanted.curses.curses;

import com.mojang.datafixers.Products;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.favouriteless.enchanted.api.curses.Curse;
import net.favouriteless.enchanted.common.util.RandomUtils;
import net.minecraft.server.level.ServerPlayer;

public abstract class AbstractRandomCurse implements Curse {

    private final int min;
    private final int max;

    protected long next;

    public AbstractRandomCurse(int min, int max, long next) {
        this.min = min;
        this.max = max;
        this.next = next;
    }

    protected abstract void execute(ServerPlayer target, int strength, long age);

    @Override
    public void tick(ServerPlayer target, int strength, long age) {
        if (next <= age) {
            execute(target, strength, age);
            next = age + RandomUtils.nextLong(min * 20L, max * 20L);
        }
    }

    protected static <T extends AbstractRandomCurse> Products.P1<RecordCodecBuilder.Mu<T>, Long> codecStart(RecordCodecBuilder.Instance<T> instance) {
        return instance.group(Codec.LONG.fieldOf("next").forGetter(c -> c.next));
    }

}
