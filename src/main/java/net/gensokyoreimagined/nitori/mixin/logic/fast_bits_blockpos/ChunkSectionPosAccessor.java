package net.gensokyoreimagined.nitori.mixin.logic.fast_bits_blockpos;

import net.minecraft.core.SectionPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(SectionPos.class)
public interface ChunkSectionPosAccessor {
    @Invoker("<init>")
    public static SectionPos invokeChunkSectionPos(int i, int j, int k) {
        throw new AssertionError();
    }
}