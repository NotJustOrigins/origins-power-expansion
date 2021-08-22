package me.jarva.origins_power_expansion;

import me.jarva.origins_power_expansion.registry.EntitiesRegistry;
import me.jarva.origins_power_expansion.registry.KeybindsRegistry;
import me.shedaniel.architectury.registry.entity.EntityRenderers;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;

public class OriginsPowerExpansionClient {
    @Environment(EnvType.CLIENT)
    public static void register() {
        EntityRenderers.register(EntitiesRegistry.CUSTOM_PROJECTILE_ENTITY, (dispatcher) -> new ThrownItemRenderer<>(dispatcher, Minecraft.getInstance().getItemRenderer()));
        KeybindsRegistry.register();
    }
}
