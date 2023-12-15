package moe.wolfgirl.visualjs.forge;

import dev.architectury.platform.forge.EventBuses;
import moe.wolfgirl.visualjs.VisualJS;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(VisualJS.MOD_ID)
public class VisualJSForge {
    public VisualJSForge() {
		// Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(VisualJS.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
            VisualJS.init();
    }
}