package me.jarva.origins_power_expansion.registry.forge;

import io.github.apace100.origins.registry.ModRegistriesArchitectury;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;

public class EntitiesRegistryImpl {
    public static void register(EntityType<?> entityType, ResourceLocation resource) {
        ModRegistriesArchitectury.ENTITY_TYPES.register(resource, () -> entityType);
    }
}
