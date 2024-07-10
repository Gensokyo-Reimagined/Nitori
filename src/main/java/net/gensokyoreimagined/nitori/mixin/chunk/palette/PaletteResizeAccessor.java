package net.gensokyoreimagined.nitori.mixin.chunk.palette;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(targets = "net.minecraft.world.level.chunk.PaletteResize")
public abstract class PaletteResizeAccessor<T> {
    @Invoker
    public abstract int callOnResize(int newBits, T object);
}
