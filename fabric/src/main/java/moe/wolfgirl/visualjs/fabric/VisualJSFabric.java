package moe.wolfgirl.visualjs.fabric;

import moe.wolfgirl.visualjs.VisualJS;
import net.fabricmc.api.ModInitializer;

public class VisualJSFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        VisualJS.init();
    }
}