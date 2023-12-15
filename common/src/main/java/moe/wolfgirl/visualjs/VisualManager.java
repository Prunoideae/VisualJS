package moe.wolfgirl.visualjs;

import com.google.common.base.Suppliers;
import com.google.gson.JsonSyntaxException;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.shaders.Uniform;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

public class VisualManager {
    public static final VisualManager INSTANCE = new VisualManager();

    private boolean forceOverride = false;
    private PostChain currentEffect = null;
    private ResourceLocation currentEffectLoc = null;
    private final HashMap<Pair<Integer, String>, List<Double>> uniforms = new HashMap<>();

    private final Supplier<GameRenderer> rendererGetter;

    private VisualManager() {
        this.rendererGetter = Suppliers.memoize(() -> Minecraft.getInstance().gameRenderer);
    }

    public void reapplyEffect() {
        applyEffect(currentEffectLoc, forceOverride);
        uniforms.forEach((k, v) -> setUniform(k.getFirst(), k.getSecond(), v));
    }

    public void applyEffect(ResourceLocation resourceLocation, boolean force) {
        if (this.currentEffect != null) {
            clearEffect();
        }
        if (resourceLocation == null) {
            return;
        }
        currentEffectLoc = resourceLocation;
        resourceLocation = new ResourceLocation(resourceLocation.getNamespace(), "shaders/post/%s.json".formatted(resourceLocation.getPath()));
        setForceOverride(force);
        try {
            this.currentEffect = new PostChain(Minecraft.getInstance().getTextureManager(), Minecraft.getInstance().getResourceManager(), Minecraft.getInstance().getMainRenderTarget(), resourceLocation);
            Window gameWindow = Minecraft.getInstance().getWindow();
            this.currentEffect.resize(gameWindow.getWidth(), gameWindow.getHeight());
            getRenderer().postEffect = this.currentEffect;
            getRenderer().effectActive = true;
        } catch (IOException e) {
            VisualJS.LOGGER.error("Failed to load shader: %s".formatted(resourceLocation));
            getRenderer().effectActive = false;
        } catch (JsonSyntaxException e) {
            VisualJS.LOGGER.error("Failed to parse shader: %s".formatted(resourceLocation));
            getRenderer().effectActive = false;
        }
    }

    public void clearEffect() {
        if (currentEffect != null) {
            currentEffect.close();
            currentEffect = null;
            uniforms.clear();
        }
        getRenderer().postEffect = null;
        currentEffectLoc = null;
        setForceOverride(false);
    }

    @Nullable
    private Uniform getUniform(int pass, String name) {
        return currentEffect.passes.get(pass).getEffect().getUniform(name);
    }

    public void setUniform(int pass, String name, List<Double> list) {
        if (currentEffect == null) return;
        Uniform uniform = getUniform(pass, name);
        if (uniform == null) {
            VisualJS.LOGGER.error("Uniform %s does not exist on pass %s!".formatted(name, pass));
            return;
        }

        uniforms.put(new Pair<>(pass, name), list);

        List<Float> fs = list.stream().map(Double::floatValue).toList();

        switch (fs.size()) {
            case 1 -> uniform.set(fs.get(0));
            case 2 -> uniform.set(fs.get(0), fs.get(1));
            case 3 -> uniform.set(fs.get(0), fs.get(1), fs.get(2));
            case 4 -> uniform.set(fs.get(0), fs.get(1), fs.get(2), fs.get(3));
        }
    }

    public void setForceOverride(boolean override) {
        this.forceOverride = override;
    }

    public boolean getForceOverride() {
        return this.forceOverride;
    }

    private GameRenderer getRenderer() {
        return rendererGetter.get();
    }
}
