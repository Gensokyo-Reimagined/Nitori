package net.gensokyoreimagined.nitori.mixin.removed.chunk.palette;

// import net.gensokyoreimagined.nitori.common.world.chunk.LithiumHashPalette;
// import net.minecraft.core.IdMap;
// import net.minecraft.world.level.chunk.PalettedContainer;
// import org.jetbrains.annotations.NotNull;
// import org.spongepowered.asm.mixin.*;
// import net.minecraft.world.level.chunk.Palette;
//
// import static net.minecraft.world.level.chunk.PalettedContainer.Strategy.LINEAR_PALETTE_FACTORY;
// import static net.minecraft.world.level.chunk.PalettedContainer.Strategy.SINGLE_VALUE_PALETTE_FACTORY;
//
// @Mixin(PalettedContainer.Strategy.class)
// public abstract class PalettedContainerMixin {
//     @Mutable
//     @Shadow
//     @Final
//     public static PalettedContainer.Strategy SECTION_STATES;
//
//     @Unique
//     private static final PalettedContainerConfigurationMixin<?>[] BLOCKSTATE_DATA_PROVIDERS;
//     @Unique
//     private static final PalettedContainerConfigurationMixin<?>[] BIOME_DATA_PROVIDERS;
//
//
//     @Unique
//     private static final Palette.Factory HASH = LithiumHashPalette::create;
//     @Mutable
//     @Shadow
//     @Final
//     public static PalettedContainer.Strategy SECTION_BIOMES;
//     @Shadow
//     @Final
//     static Palette.Factory GLOBAL_PALETTE_FACTORY;
//
//     /*
//      * @reason Replace the hash palette from vanilla with our own and change the threshold for usage to only 3 bits,
//      * as our implementation performs better at smaller key ranges.
//      * @author JellySquid, 2No2Name (avoid Configuration duplication, use hash palette for 3 bit biomes)
//      */
//     static {
//         Palette.Factory idListFactory = GLOBAL_PALETTE_FACTORY;
//
//         PalettedContainerConfigurationMixin<?> arrayConfiguration4bit = PalettedContainerConfigurationMixin.create(LINEAR_PALETTE_FACTORY, 4);
//         PalettedContainerConfigurationMixin<?> hashConfiguration4bit = PalettedContainerConfigurationMixin.create(HASH, 4);
//         BLOCKSTATE_DATA_PROVIDERS = new PalettedContainerConfigurationMixin<?>[]{
//                 PalettedContainerConfigurationMixin.create(SINGLE_VALUE_PALETTE_FACTORY, 0),
//                 // Bits 1-4 must all pass 4 bits as parameter, otherwise chunk sections will corrupt.
//                 arrayConfiguration4bit,
//                 arrayConfiguration4bit,
//                 hashConfiguration4bit,
//                 hashConfiguration4bit,
//                 PalettedContainerConfigurationMixin.create(HASH, 5),
//                 PalettedContainerConfigurationMixin.create(HASH, 6),
//                 PalettedContainerConfigurationMixin.create(HASH, 7),
//                 PalettedContainerConfigurationMixin.create(HASH, 8)
//         };
//
//         SECTION_STATES = new PalettedContainer.Strategy(4) {
//             @Override
//             public <A> @NotNull PalettedContainerConfigurationMixin<A> getConfiguration(@NotNull IdMap<A> idList, int bits) {
//                 if (bits >= 0 && bits < BLOCKSTATE_DATA_PROVIDERS.length) {
//                     //noinspection unchecked
//                     return (PalettedContainerConfigurationMixin<A>) BLOCKSTATE_DATA_PROVIDERS[bits];
//                 }
//                 return PalettedContainerConfigurationMixin.create(idListFactory, MathHelper.ceilLog2(idList.size()));
//             }
//         };
//
//         BIOME_DATA_PROVIDERS = new PalettedContainerConfigurationMixin<?>[]{
//                 PalettedContainerConfigurationMixin.create(SINGLE_VALUE_PALETTE_FACTORY, 0),
//                 PalettedContainerConfigurationMixin.create(LINEAR_PALETTE_FACTORY, 1),
//                 PalettedContainerConfigurationMixin.create(LINEAR_PALETTE_FACTORY, 2),
//                 PalettedContainerConfigurationMixin.create(HASH, 3)
//         };
//
//
//         SECTION_BIOMES = new PalettedContainer.Strategy(2) {
//             @Override
//             public <A> @NotNull PalettedContainerConfigurationMixin<A> getConfiguration(@NotNull IdMap<A> idList, int bits) {
//                 if (bits >= 0 && bits < BIOME_DATA_PROVIDERS.length) {
//                     //noinspection unchecked
//                     return (PalettedContainerConfigurationMixin<A>) BIOME_DATA_PROVIDERS[bits];
//                 }
//                 return PalettedContainerConfigurationMixin.create(idListFactory, MathHelper.ceilLog2(idList.size()));
//             }
//         };
//     }
// }