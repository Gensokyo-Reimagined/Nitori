package net.gensokyoreimagined.nitori.mixin.removed.collections.mob_spawning;

//import com.google.common.collect.ImmutableList;
//import net.gensokyoreimagined.nitori.common.util.collections.HashedReferenceList;
//import net.minecraft.util.random.WeightedRandomList;
//import net.minecraft.util.random.WeightedEntry;
//import org.spongepowered.asm.mixin.*;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//
//import java.util.Collections;
//import java.util.List;
//
//@Mixin(WeightedRandomList.class)
//public class PoolMixin<E extends WeightedEntry> {
//
//    @Mutable
//    @Shadow
//    @Final
//    private ImmutableList<E> items;
//    //Need a separate variable due to entries being type ImmutableList
//    @Unique
//    private List<E> entryHashList;
//
//    @Inject(method = "<init>", at = @At("RETURN"))
//    private void init(List<? extends E> entries, CallbackInfo ci) {
//        //We are using reference equality here, because all vanilla implementations of Weighted use reference equality
//        this.entryHashList = this.items.size() > 4 ? Collections.unmodifiableList(new HashedReferenceList<>(this.items)) : this.items;
//    }
//
//    /**
//     * @author 2No2Name
//     * @reason return a collection with a faster contains() call
//     */
//    @Overwrite
//    public List<E> unwrap() {
//        return this.entryHashList;
//    }
//}