package net.gensokyoreimagined.nitori.mixin.network.microopt;

import io.netty.buffer.ByteBuf;
import net.gensokyoreimagined.nitori.common.util.network.VarIntUtil;
import net.minecraft.network.VarInt;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(VarInt.class)
public class VarIntsMixin {
    /**
     * @author Andrew Steinborn
     * @reason optimized version
     */
    @Overwrite
    public static int getByteSize(int v) {
        return VarIntUtil.getVarIntLength(v);
    }

    /**
     * @author Andrew Steinborn
     * @reason optimized version
     */
    @Overwrite
    public static ByteBuf write(ByteBuf buf, int i) {
        // Peel the one and two byte count cases explicitly as they are the most common VarInt sizes
        // that the server will send, to improve inlining.
        if ((i & (0xFFFFFFFF << 7)) == 0) {
            buf.writeByte(i);
        } else if ((i & (0xFFFFFFFF << 14)) == 0) {
            int w = (i & 0x7F | 0x80) << 8 | (i >>> 7);
            buf.writeShort(w);
        } else {
            writeOld(buf, i);
        }
        return buf;
    }

    private static void writeOld(ByteBuf buf, int value) {
        // See https://steinborn.me/posts/performance/how-fast-can-you-write-a-varint/
        if ((value & (0xFFFFFFFF << 7)) == 0) {
            buf.writeByte(value);
        } else if ((value & (0xFFFFFFFF << 14)) == 0) {
            int w = (value & 0x7F | 0x80) << 8 | (value >>> 7);
            buf.writeShort(w);
        } else if ((value & (0xFFFFFFFF << 21)) == 0) {
            int w = (value & 0x7F | 0x80) << 16 | ((value >>> 7) & 0x7F | 0x80) << 8 | (value >>> 14);
            buf.writeMedium(w);
        } else if ((value & (0xFFFFFFFF << 28)) == 0) {
            int w = (value & 0x7F | 0x80) << 24 | (((value >>> 7) & 0x7F | 0x80) << 16)
                    | ((value >>> 14) & 0x7F | 0x80) << 8 | (value >>> 21);
            buf.writeInt(w);
        } else {
            int w = (value & 0x7F | 0x80) << 24 | ((value >>> 7) & 0x7F | 0x80) << 16
                    | ((value >>> 14) & 0x7F | 0x80) << 8 | ((value >>> 21) & 0x7F | 0x80);
            buf.writeInt(w);
            buf.writeByte(value >>> 28);
        }
    }
}