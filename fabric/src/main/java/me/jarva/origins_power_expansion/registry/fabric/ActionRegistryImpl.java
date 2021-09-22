package me.jarva.origins_power_expansion.registry.fabric;

import io.github.apace100.origins.power.factory.action.ActionFactory;
import io.github.apace100.origins.registry.ModRegistries;
import net.minecraft.entity.Entity;
import net.minecraft.util.registry.Registry;

public class ActionRegistryImpl {
    public static void register(ActionFactory<Entity> actionFactory) {
        Registry.register(ModRegistries.ENTITY_ACTION, actionFactory.getSerializerId(), actionFactory);
    }
}
