package banditvault.xboxcompat.mixin;

import net.minecraft.class_315;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_315.class)
public abstract class GameOptionsMixin {
    @Shadow
    public int field_1872;

    @Shadow
    public int field_1885;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void banditvault$clearResolutionOverride(CallbackInfo ci) {
        this.field_1872 = 0;
        this.field_1885 = 0;
    }
}
