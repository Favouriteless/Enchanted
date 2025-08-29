package net.favouriteless.enchanted.common.enchanted.poppet;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;

public class PoppetUseResult {

    private final Item poppet;
    private final ResultType result;

    private PoppetUseResult(Item poppet, ResultType result) {
        this.poppet = poppet;
        this.result = result;
    }

    public static PoppetUseResult of(Item poppet, boolean isBroken) {
        return isBroken ? successBreak(poppet) : success(poppet);
    }

    public static PoppetUseResult pass() {
        return new PoppetUseResult(null, ResultType.PASS);
    }

    public static PoppetUseResult fail(Item poppet) {
        return new PoppetUseResult(poppet, ResultType.FAIL);
    }

    public static PoppetUseResult success(Item poppet) {
        return new PoppetUseResult(poppet, ResultType.SUCCESS);
    }

    public static PoppetUseResult successBreak(Item poppet) {
        return new PoppetUseResult(poppet, ResultType.SUCCESS_BREAK);
    }

    public Item poppet() {
        return poppet;
    }

    public ResultType type() {
        return result;
    }

    public boolean isSuccess() {
        return result == ResultType.SUCCESS || result == ResultType.SUCCESS_BREAK;
    }

    public enum ResultType {
        SUCCESS,
        SUCCESS_BREAK,
        FAIL,
        PASS;

        public static final StreamCodec<ByteBuf, ResultType> STREAM_CODEC = StreamCodec.of((buf, val) -> buf.writeByte(val.ordinal()), (buf) -> ResultType.values()[buf.readByte()]);
    }
}
