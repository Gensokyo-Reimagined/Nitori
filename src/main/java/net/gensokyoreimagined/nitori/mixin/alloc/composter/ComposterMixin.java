package net.gensokyoreimagined.nitori.mixin.alloc.composter;

//import net.gensokyoreimagined.nitori.common.util.ArrayConstants;
//import net.minecraft.world.WorldlyContainer;
//import net.minecraft.core.Direction;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.Overwrite;
//
//public class ComposterMixin {
//
//    @Mixin(targets = "net.minecraft.block.ComposterBlock$ComposterInventory")
//    static abstract class ComposterBlockComposterInventoryMixin implements WorldlyContainer {
//        /**
//         * @author 2No2Name
//         * @reason avoid allocation
//         */
//        @Overwrite
//        public int[] getSlotsForFace(Direction side) {
//            return side == Direction.UP ? ArrayConstants.ZERO : ArrayConstants.EMPTY;
//        }
//    }
//
//    @Mixin(targets = "net.minecraft.block.ComposterBlock$DummyInventory")
//    static abstract class ComposterBlockDummyInventoryMixin implements WorldlyContainer {
//        /**
//         * @author 2No2Name
//         * @reason avoid allocation
//         */
//        @Overwrite
//        public int[] getSlotsForFace(Direction side) {
//            return ArrayConstants.EMPTY;
//        }
//    }
//
//    @Mixin(targets = "net.minecraft.block.ComposterBlock$FullComposterInventory")
//    static abstract class ComposterBlockFullComposterInventoryMixin implements WorldlyContainer {
//        /**
//         * @author 2No2Name
//         * @reason avoid allocation
//         */
//        @Overwrite
//        public int[] getSlotsForFace(Direction side) {
//            return side == Direction.DOWN ? ArrayConstants.ZERO : ArrayConstants.EMPTY;
//        }
//    }
//}

//TODO: Mixins are not getting dettected for some reason even if they are in the mixins.core???