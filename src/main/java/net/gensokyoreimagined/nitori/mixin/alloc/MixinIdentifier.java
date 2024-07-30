package net.gensokyoreimagined.nitori.mixin.alloc;

import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ResourceLocation.class)
public class MixinIdentifier {

    @Shadow @Final
    private String namespace;

    @Shadow @Final
    private String path;

    @Unique
    private String nitori$cachedString = null;

    /**
     * @author ishland
     * @reason cache toString
     */
    @Overwrite
    public String toString() {
        if (this.nitori$cachedString != null) return this.nitori$cachedString;
        final String s = this.namespace + ":" + this.path;
        this.nitori$cachedString = s;
        return s;
    }

}