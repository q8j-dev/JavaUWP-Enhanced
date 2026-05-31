package banditvault.xboxcompat.mixin;

import banditvault.xboxcompat.XboxCompatLog;
import net.minecraft.class_1041;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL43C;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_1041.class)
public abstract class GlContextMixin {

    @Inject(method = "<init>", at = @At("TAIL"))
    private void banditvault$onWindowCreated(CallbackInfo ci) {
        try {
            // Enable parallel shader compilation on Mesa (GL_ARB/KHR_parallel_shader_compile).
            // Mesa compiles new GLSL->DXIL shader variants on background threads instead of
            // blocking the render thread. Eliminates hitching on first use of new shader combos.
            org.lwjgl.opengl.GLCapabilities caps = GL.getCapabilities();
            if (caps.GL_ARB_parallel_shader_compile || caps.GL_KHR_parallel_shader_compile) {
                GL43C.glMaxShaderCompilerThreadsKHR(4);
                XboxCompatLog.log("Mesa parallel shader compile: 4 threads");
            } else {
                XboxCompatLog.log("Mesa parallel shader compile: extension not available");
            }
        } catch (Throwable t) {
            XboxCompatLog.log("GlContextMixin parallel shader setup: " + t);
        }
    }
}
