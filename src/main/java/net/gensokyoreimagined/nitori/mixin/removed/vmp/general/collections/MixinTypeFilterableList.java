package net.gensokyoreimagined.nitori.mixin.removed.vmp.general.collections;

//import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
//import it.unimi.dsi.fastutil.objects.ObjectArrayList;
//import net.gensokyoreimagined.nitori.access.ITypeFilterableList;
//import net.minecraft.util.ClassInstanceMultiMap;
//import org.objectweb.asm.Opcodes;
//import org.spongepowered.asm.mixin.*;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Redirect;
//
//import java.util.*;
//
//@Mixin(value = ClassInstanceMultiMap.class, priority = 1005) // priority compatibility hack for lithium
//public abstract class MixinTypeFilterableList<T> extends AbstractCollection<T> implements ITypeFilterableList {
//
//    @Mutable
//    @Shadow @Final private Map<Class<?>, List<T>> byClass;
//
//    @Mutable
//    @Shadow @Final private List<T> allInstances;
//
//    @Shadow @Final private Class<T> baseClass;
//
//    @Redirect(method = "<init>", at = @At(value = "FIELD", target = "Lnet/minecraft/util/ClassInstanceMultiMap;byClass:Ljava/util/Map;", opcode = Opcodes.PUTFIELD))
//    private void redirectSetElementsByType(ClassInstanceMultiMap<T> instance, Map<Class<?>, List<T>> value) {
//        this.byClass = new Object2ObjectLinkedOpenHashMap<>();
//    }
//
//    @Redirect(method = "<init>", at = @At(value = "FIELD", target = "Lnet/minecraft/util/ClassInstanceMultiMap;allInstances:Ljava/util/List;", opcode = Opcodes.PUTFIELD))
//    private void redirectSetAllElements(ClassInstanceMultiMap<T> instance, List<T> value) {
//        this.allInstances = new ObjectArrayList<>();
//    }
//
//    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lcom/google/common/collect/Maps;newHashMap()Ljava/util/HashMap;", remap = false))
//    private HashMap<?, ?> redirectNewHashMap() {
//        return null; // avoid unnecessary alloc
//    }
//
//    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lcom/google/common/collect/Lists;newArrayList()Ljava/util/ArrayList;", remap = false))
//    private ArrayList<?> redirectNewArrayList() {
//        return null;
//    }
//
//    @Override
//    public Object[] getBackingArray() {
//        return ((ObjectArrayList<T>) this.allInstances).elements();
//    }
//
//    /**
//     * @author ishland
//     * @reason use fastutil array list for faster iteration & use array for filtering iteration
//     */
//    @Overwrite
//    public <S> Collection<S> find(Class<S> type) {
//        List<T> cached = this.byClass.get(type);
//        if (cached != null) return (Collection<S>) cached;
//
//        if (!this.baseClass.isAssignableFrom(type)) {
//            throw new IllegalArgumentException("Don't know how to search for " + type);
//        } else {
//            List<? extends T> list = this.byClass.computeIfAbsent(type,
//                    typeClass -> {
//                        ObjectArrayList<T> ts = new ObjectArrayList<>(this.allInstances.size());
//                        for (Object _allElement : ((ObjectArrayList<T>) this.allInstances).elements()) {
//                            if (typeClass.isInstance(_allElement)) {
//                                ts.add((T) _allElement);
//                            }
//                        }
//                        return ts;
//                    }
//            );
//            return (Collection<S>) list;
//        }
//    }
//}