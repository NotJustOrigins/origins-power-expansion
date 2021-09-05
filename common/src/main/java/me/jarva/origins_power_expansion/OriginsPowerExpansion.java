package me.jarva.origins_power_expansion;

import me.jarva.origins_power_expansion.registry.ActionRegistry;
import me.jarva.origins_power_expansion.registry.PowerRegistry;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class OriginsPowerExpansion {
    public static final String MOD_ID = "origins_power_expansion";
    public static final String MOD_NAME = "OriginsPowerExpansion";
    public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);
    
    public static void init() {
        PowerRegistry.register();
        ActionRegistry.register();
    }

    public static ResourceLocation identifier(String path) {
        return new ResourceLocation("ope", path);
    }
}
