package me.jarva.origins_power_expansion.actions.factory.fabric;

import io.github.apace100.origins.power.factory.action.ActionFactory;
import io.github.apace100.origins.registry.ModRegistries;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.Entity;

public class ActionFactoriesImpl {
    public static void register(ActionFactory<Entity> actionFactory) {
        Registry.register(ModRegistries.ENTITY_ACTION, actionFactory.getSerializerId(), actionFactory);
    }
}
