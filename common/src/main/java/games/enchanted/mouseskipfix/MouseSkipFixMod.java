package games.enchanted.mouseskipfix;

import games.enchanted.mouseskipfix.config.ConfigFileHandler;
import games.enchanted.mouseskipfix.platform.Services;

public class MouseSkipFixMod {
    public static void init() {
        ConfigFileHandler.loadConfig();
        ConfigFileHandler.saveConfig();
        Constants.LOG.info("[Inventory Mouse Skipping Fix] Mod loading in a {} environment!", Services.PLATFORM.getPlatformName());
    }
}