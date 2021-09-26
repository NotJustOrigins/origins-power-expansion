package me.jarva.origins_power_expansion.registry;

import dev.architectury.injectables.annotations.ExpectPlatform;
import io.github.apace100.origins.power.factory.action.ActionFactory;
import me.jarva.origins_power_expansion.actions.entity.*;
import net.minecraft.entity.Entity;

@SuppressWarnings({"unchecked", "UnstableApiUsage", "deprecation"})
public class ActionRegistry {
    public static void register() {
        register(AreaOfEffectAction.getFactory());
        register(EnderChestAction.getFactory());
        register(CraftingTableAction.getFactory());
        register(FireProjectileAction.getFactory());
        register(RaycastAction.getFactory());
        register(SetResourceAction.getFactory());
    }

    @ExpectPlatform
    private static void register(ActionFactory<Entity> actionFactory) {
        throw new AssertionError();
    }
}
