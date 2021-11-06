package me.jarva.origins_power_expansion.registry.actions.forge;

import io.github.apace100.origins.power.factory.action.ActionFactory;
import io.github.apace100.origins.registry.ModRegistriesArchitectury;
import net.minecraft.item.ItemStack;

public class ItemActionRegistryImpl {
    public static void register(ActionFactory<ItemStack> actionFactory) {
        ModRegistriesArchitectury.ITEM_ACTION.register(actionFactory.getSerializerId(), () -> actionFactory);
    }
}
