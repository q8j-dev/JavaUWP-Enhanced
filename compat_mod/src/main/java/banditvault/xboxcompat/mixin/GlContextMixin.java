package banditvault.xboxcompat.mixin;

import banditvault.xboxcompat.XboxCompatLog;
import net.minecraft.class_1041;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_1041.class)
public abstract class GlContextMixin {

    @Inject(method = "<init>", at = @At("TAIL"))
    private void banditvault$onWindowCreated(CallbackInfo ci) {
        try {
            // Enable parallel shader compilation via reflection to avoid a compile-time
            // dependency on LWJGL (which is not on the compat mod's build classpath).
            // Mesa supports GL_ARB/KHR_parallel_shader_compile; with 4 threads it compiles
            // new GLSL->DXIL variants in the background instead of blocking the render thread.
            Class<?> glClass = Class.forName("org.lwjgl.opengl.GL43C");
            Class<?> capsClass = Class.forName("org.lwjgl.opengl.GL");
            Class<?> glCapsClass = Class.forName("org.lwjgl.opengl.GLCapabilities");

            Object caps = capsClass.getMethod("getCapabilities").invoke(null);
            boolean hasArb = (boolean) glCapsClass.getField("GL_ARB_parallel_shader_compile").get(caps);
            boolean hasKhr = (boolean) glCapsClass.getField("GL_KHR_parallel_shader_compile").get(caps);

            if (hasArb || hasKhr) {
                glClass.getMethod("glMaxShaderCompilerThreadsKHR", int.class).invoke(null, 4);
                XboxCompatLog.log("Mesa parallel shader compile: 4 threads");
            } else {
                XboxCompatLog.log("Mesa parallel shader compile: extension not available");
            }
        } catch (Throwable t) {
            XboxCompatLog.log("GlContextMixin parallel shader setup skipped: " + t);
        }
    }
}
