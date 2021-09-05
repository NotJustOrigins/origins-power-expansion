package me.jarva.origins_power_expansion.registry;

import dev.architectury.injectables.annotations.ExpectPlatform;
import io.github.apace100.origins.power.factory.action.ActionFactory;
import me.jarva.origins_power_expansion.actions.entity.AreaOfEffectAction;
import me.jarva.origins_power_expansion.actions.entity.EnderChestAction;
import net.minecraft.world.entity.Entity;

@SuppressWarnings({"unchecked", "UnstableApiUsage", "deprecation"})
public class ActionRegistry {
    public static void register() {
        register(AreaOfEffectAction.getFactory());
        register(EnderChestAction.getFactory());
    }

    @ExpectPlatform
    private static void register(ActionFactory<Entity> actionFactory) {
        throw new AssertionError();
    }
}
