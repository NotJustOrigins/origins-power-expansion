package me.jarva.origins_power_expansion.actions.factory.forge;

import io.github.apace100.origins.power.factory.action.ActionFactory;
import io.github.apace100.origins.registry.ModRegistriesArchitectury;
import net.minecraft.world.entity.Entity;

public class ActionFactoriesImpl {
    public static void register(ActionFactory<Entity> actionFactory) {
        ModRegistriesArchitectury.ENTITY_ACTION.register(actionFactory.getSerializerId(), () -> actionFactory);
    }
}
