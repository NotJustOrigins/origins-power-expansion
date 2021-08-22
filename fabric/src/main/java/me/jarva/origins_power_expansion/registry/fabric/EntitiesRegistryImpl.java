package me.jarva.origins_power_expansion.registry.fabric;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;

public class EntitiesRegistryImpl {
    public static void register(EntityType<?> entityType, ResourceLocation resource) {
        Registry.register(Registry.ENTITY_TYPE, resource, entityType);
    }
}
