package net.gensokyoreimagined.nitori.mixin.math.intrinsic;

import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

// Credit to Gale patch #0018
@Mixin(Mth.class)
public class MathHelperIntrinsicMixin {
    /**
     * @author QPCrummer
     * @reason Use Intrinsic instead
     */
    @Overwrite
    public static int floor(float value) {
        return (int) Math.floor(value);
    }

    /**
     * @author QPCrummer
     * @reason Use Intrinsic instead
     */
    @Overwrite
    public static int floor(double value) {
        return (int) Math.floor(value);
    }

    /**
     * @author QPCrummer
     * @reason Use Intrinsic instead
     */
    @Overwrite
    public static int ceil(float value) {
        return (int) Math.ceil(value);
    }

    /**
     * @author QPCrummer
     * @reason Use Intrinsic instead
     */
    @Overwrite
    public static int ceil(double value) {
        return (int) Math.ceil(value);
    }

    /**
     * @author QPCrummer
     * @reason Use Intrinsic instead
     */
    @Overwrite
    public static double absMax(double a, double b) {
        return Math.max(Math.abs(a), Math.abs(b));
    }
}