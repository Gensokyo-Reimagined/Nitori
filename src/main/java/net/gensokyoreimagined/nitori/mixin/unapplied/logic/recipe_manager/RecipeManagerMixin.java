package net.gensokyoreimagined.nitori.mixin.unapplied.logic.recipe_manager;

//import net.minecraft.world.Container;
//import net.minecraft.world.item.crafting.Recipe;
//import net.minecraft.world.item.crafting.RecipeHolder;
//import net.minecraft.world.item.crafting.RecipeManager;
//import net.minecraft.world.item.crafting.RecipeType;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.Overwrite;
//import org.spongepowered.asm.mixin.Shadow;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.List;
//import java.util.Map;
//@Mixin(RecipeManager.class)
//public abstract class RecipeManagerMixin {
//
//    @Shadow protected abstract <C extends Container, T extends Recipe<C>> Collection<RecipeHolder<T>> byType(RecipeType<T> type);
//
//    /**
//     * @author QPCrummer & Leaf Patch #0023
//     * @reason Optimize RecipeManager List Creation
//     */
//    @Overwrite
//    public <C extends Container, T extends Recipe<C>> List<RecipeHolder<T>> getAllRecipesFor(RecipeType<T> type) {
//        return new ArrayList<>(this.byType(type));
//    }
//}