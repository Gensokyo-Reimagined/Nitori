package net.gensokyoreimagined.nitori.mixin.world.biome_access;

import net.minecraft.core.Holder;
import net.minecraft.util.Mth;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.util.LinearCongruentialGenerator;
import org.spongepowered.asm.mixin.*;

/**
 * Credit to FXMorin (Unmerged PR for Lithium)
 */
@Mixin(BiomeManager.class)
public class BiomeAccessMixin {
    @Shadow
    @Final
    private BiomeManager.NoiseBiomeSource noiseBiomeSource;

    @Shadow
    @Final
    private long biomeZoomSeed;

    @Shadow
    private static double getFiddle(long l) {return 0;}

    @Unique
    private static final double nitori$maxOffset = 0.4500000001D;

    /**
     * @author FX - PR0CESS
     * @reason I wanted to make it faster
     */
    @Overwrite
    public Holder<Biome> getBiome(BlockPos pos) {
        int xMinus2 = pos.getX() - 2;
        int yMinus2 = pos.getY() - 2;
        int zMinus2 = pos.getZ() - 2;
        int x = xMinus2 >> 2;
        int y = yMinus2 >> 2;
        int z = zMinus2 >> 2;
        double quartX = (double)(xMinus2 & 3) / 4.0D;
        double quartY = (double)(yMinus2 & 3) / 4.0D;
        double quartZ = (double)(zMinus2 & 3) / 4.0D;
        int smallestX = 0;
        double smallestDist = Double.POSITIVE_INFINITY;
        for(int biomeX = 0; biomeX < 8; ++biomeX) {
            boolean everyOtherQuad = (biomeX & 4) == 0;
            boolean everyOtherPair = (biomeX & 2) == 0;
            boolean everyOther =     (biomeX & 1) == 0;
            double quartXX = everyOtherQuad ? quartX : quartX - 1.0D;
            double quartYY = everyOtherPair ? quartY : quartY - 1.0D;
            double quartZZ = everyOther     ? quartZ : quartZ - 1.0D;

            double maxQuartYY = 0.0D,maxQuartZZ = 0.0D;
            if (biomeX != 0) {
                maxQuartYY = Mth.square(Math.max(quartYY + nitori$maxOffset, Math.abs(quartYY - nitori$maxOffset)));
                maxQuartZZ = Mth.square(Math.max(quartZZ + nitori$maxOffset, Math.abs(quartZZ - nitori$maxOffset)));
                double maxQuartXX = Mth.square(Math.max(quartXX + nitori$maxOffset, Math.abs(quartXX - nitori$maxOffset)));
                if (smallestDist < maxQuartXX + maxQuartYY + maxQuartZZ) {
                    continue;
                }
            }

            int xx = everyOtherQuad ? x : x + 1;
            int yy = everyOtherPair ? y : y + 1;
            int zz = everyOther ? z : z + 1;

            long seed = LinearCongruentialGenerator.next(this.biomeZoomSeed, xx);
            seed = LinearCongruentialGenerator.next(seed, yy);
            seed = LinearCongruentialGenerator.next(seed, zz);
            seed = LinearCongruentialGenerator.next(seed, xx);
            seed = LinearCongruentialGenerator.next(seed, yy);
            seed = LinearCongruentialGenerator.next(seed, zz);
            double offsetX = getFiddle(seed);
            double sqrX = Mth.square(quartXX + offsetX);
            if (biomeX != 0 && smallestDist < sqrX + maxQuartYY + maxQuartZZ) {
                continue;
            }
            seed = LinearCongruentialGenerator.next(seed, this.biomeZoomSeed);
            double offsetY = getFiddle(seed);
            double sqrY = Mth.square(quartYY + offsetY);
            if (biomeX != 0 && smallestDist < sqrX + sqrY + maxQuartZZ) {
                continue;
            }
            seed = LinearCongruentialGenerator.next(seed, this.biomeZoomSeed);
            double offsetZ = getFiddle(seed);
            double biomeDist = sqrX + sqrY + Mth.square(quartZZ + offsetZ);

            if (smallestDist > biomeDist) {
                smallestX = biomeX;
                smallestDist = biomeDist;
            }
        }

        int biomeX = (smallestX & 4) == 0 ? x : x + 1;
        int biomeY = (smallestX & 2) == 0 ? y : y + 1;
        int biomeZ = (smallestX & 1) == 0 ? z : z + 1;
        return this.noiseBiomeSource.getNoiseBiome(biomeX, biomeY, biomeZ);
    }
}