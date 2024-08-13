package net.gensokyoreimagined.nitori.mixin.removed;

//import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
//import net.minecraft.world.entity.Entity;
//import net.minecraft.world.level.entity.EntityTickList;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.Unique;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//
//import java.util.function.Consumer;
//
//@Mixin(EntityTickList.class)
//public class MixinEntityTickList {
//    @Unique
//    private Int2ObjectLinkedOpenHashMap<Entity> nitori$entities = new Int2ObjectLinkedOpenHashMap<>();
//    @Unique
//    private Int2ObjectLinkedOpenHashMap<Entity> nitori$iteratorPointer = null;
//
//    // I've decided I hate Paper's design.
//    // All of these are effectively Overwrites.
//
//    @Unique
//    private void nitori$ensureActiveIsNotIterated() {
//        if (nitori$iteratorPointer == nitori$entities) {
//            // Avoid a ConcurrentModificationException by cloning the map before modifying it.
//            // Side effect is that it allocates more memory to avoid blocking the main thread, but it's all pointers anyway.
//            nitori$entities = nitori$entities.clone();
//        }
//    }
//
//    @Inject(method = "add", at = @At("HEAD"), cancellable = true)
//    public void add(Entity entity, CallbackInfo ci) {
//        nitori$ensureActiveIsNotIterated();
//        nitori$entities.put(entity.getId(), entity);
//        ci.cancel();
//    }
//
//    @Inject(method = "remove", at = @At("HEAD"), cancellable = true)
//    public void remove(Entity entity, CallbackInfo ci) {
//        nitori$ensureActiveIsNotIterated();
//        nitori$entities.remove(entity.getId());
//        ci.cancel();
//    }
//
//    @Inject(method = "contains", at = @At("HEAD"), cancellable = true)
//    public void contains(Entity entity, CallbackInfoReturnable<Boolean> ci) {
//        ci.setReturnValue(nitori$entities.containsKey(entity.getId()));
//        ci.cancel();
//    }
//
//    @Inject(method = "forEach", at = @At("HEAD"), cancellable = true)
//    public void forEach(Consumer<Entity> action, CallbackInfo ci) {
//        if (nitori$iteratorPointer == nitori$entities) {
//            nitori$entities = nitori$entities.clone(); // Avoid a ConcurrentModificationException by cloning the map before iterating over it.
//        }
//
//        nitori$iteratorPointer = nitori$entities; // Mark the map as being iterated.
//
//        try {
//            nitori$iteratorPointer.values().forEach(action); // Iterate over the map.
//        } finally {
//            nitori$iteratorPointer = null; // Mark the map as no longer being iterated.
//        }
//
//        ci.cancel();
//    }
//}
