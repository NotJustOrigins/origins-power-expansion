package me.jarva.origins_power_expansion.registry.actions;

import dev.architectury.injectables.annotations.ExpectPlatform;
import io.github.apace100.origins.power.factory.action.ActionFactory;
import me.jarva.origins_power_expansion.actions.item.DamageAction;
import net.minecraft.item.ItemStack;

public class ItemActionRegistry {
    public static void register() {
        register(DamageAction.getFactory());
    }

    @ExpectPlatform
    private static void register(ActionFactory<ItemStack> actionFactory) {
        throw new AssertionError();
    }
}
