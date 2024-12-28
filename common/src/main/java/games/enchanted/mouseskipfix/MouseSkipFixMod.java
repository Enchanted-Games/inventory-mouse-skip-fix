package games.enchanted.mouseskipfix;

import games.enchanted.mouseskipfix.config.ConfigFileHandler;
import games.enchanted.mouseskipfix.platform.Services;

public class MouseSkipFixMod {
    public static void init() {
        Constants.LOG.info("[Mouse Skip Fix] Mod loaded in a {} environment!", Services.PLATFORM.getPlatformName());
        ConfigFileHandler.loadConfig();
        ConfigFileHandler.saveConfig();
    }
}