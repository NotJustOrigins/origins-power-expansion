package me.jarva.origins_power_expansion;

import me.jarva.origins_power_expansion.registry.KeybindRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class OriginsPowerExpansionClient {
    @Environment(EnvType.CLIENT)
    public static void register() {
        KeybindRegistry.register();
    }
}
