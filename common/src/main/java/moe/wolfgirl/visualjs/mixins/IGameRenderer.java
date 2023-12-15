package moe.wolfgirl.visualjs.mixins;

import net.minecraft.client.renderer.PostChain;

public interface IGameRenderer {
    void setForceOverride(boolean forceOverride);

    void setPostEffect(PostChain effect);

    PostChain getPostEffect();

    void setEffectActive(boolean active);
}
