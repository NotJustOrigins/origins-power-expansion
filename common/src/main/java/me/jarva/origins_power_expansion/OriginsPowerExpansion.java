package me.jarva.origins_power_expansion;

import me.jarva.origins_power_expansion.registry.actions.BlockActionRegistry;
import me.jarva.origins_power_expansion.registry.actions.EntityActionRegistry;
import me.jarva.origins_power_expansion.registry.PowerRegistry;
import me.jarva.origins_power_expansion.registry.actions.ItemActionRegistry;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class OriginsPowerExpansion {
    public static final String MOD_ID = "origins_power_expansion";
    public static final String MOD_NAME = "OriginsPowerExpansion";
    public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);

    public static void init() {
        PowerRegistry.register();
        EntityActionRegistry.register();
        BlockActionRegistry.register();
        ItemActionRegistry.register();
    }

    public static Identifier identifier(String path) {
        return new Identifier("ope", path);
    }
}
