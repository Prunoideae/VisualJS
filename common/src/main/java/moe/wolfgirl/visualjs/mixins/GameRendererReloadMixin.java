package moe.wolfgirl.visualjs.mixins;

import moe.wolfgirl.visualjs.VisualManager;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.client.renderer.GameRenderer$1")
public abstract class GameRendererReloadMixin {

    @Inject(method = "apply(Lnet/minecraft/client/renderer/GameRenderer$ResourceCache;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)V", at = @At("RETURN"))
    public void apply(GameRenderer.ResourceCache resourceCache, ResourceManager resourceManager, ProfilerFiller profilerFiller, CallbackInfo ci) {
        VisualManager.INSTANCE.reapplyEffect();
    }
}
