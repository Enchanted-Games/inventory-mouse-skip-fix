package games.enchanted.mouseskipfix;

import net.fabricmc.api.ModInitializer;

public class MouseSkipFixFabricEntrypoint implements ModInitializer {
    @Override
    public void onInitialize() {
        MouseSkipFixMod.init();
    }
}
