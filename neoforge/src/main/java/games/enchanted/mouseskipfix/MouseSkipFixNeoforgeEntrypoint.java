package games.enchanted.mouseskipfix;


import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(Constants.MOD_ID)
public class MouseSkipFixNeoforgeEntrypoint {
    public MouseSkipFixNeoforgeEntrypoint(IEventBus eventBus) {
        MouseSkipFixMod.init();
    }
}