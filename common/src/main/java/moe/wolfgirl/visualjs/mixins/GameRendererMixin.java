package moe.wolfgirl.visualjs.mixins;

import moe.wolfgirl.visualjs.VisualManager;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GameRenderer.class)
public abstract class GameRendererMixin {

    @Inject(method = "togglePostEffect", at = @At("HEAD"), cancellable = true)
    public void togglePostEffect(CallbackInfo ci) {
        if (VisualManager.INSTANCE.getForceOverride()) {
            // We do not accept toggle if forceOverride is true
            ci.cancel();
        }
    }

    @Inject(method = "checkEntityPostEffect", at = @At("HEAD"), cancellable = true)
    public void checkEntityPostEffect(@Nullable Entity entity, CallbackInfo ci) {
        if (VisualManager.INSTANCE.getForceOverride()) {
            ci.cancel();
        }
    }
}
