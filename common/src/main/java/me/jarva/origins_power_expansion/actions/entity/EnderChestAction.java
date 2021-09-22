package me.jarva.origins_power_expansion.actions.entity;

import io.github.apace100.origins.power.factory.action.ActionFactory;
import io.github.apace100.origins.util.SerializableData;
import me.jarva.origins_power_expansion.OriginsPowerExpansion;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EnderChestInventory;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.stat.Stats;
import net.minecraft.text.TranslatableText;

@SuppressWarnings({"unchecked", "UnstableApiUsage", "deprecation"})
public class EnderChestAction {
    public static void action(SerializableData.Instance data, Entity entity) {
        if (!(entity instanceof PlayerEntity)) return;

        PlayerEntity player = (PlayerEntity) entity;
        EnderChestInventory enderChestContainer = player.getEnderChestInventory();

        player.openHandledScreen(new SimpleNamedScreenHandlerFactory((i, inventory, playerx) ->
                GenericContainerScreenHandler.createGeneric9x3(i, inventory, enderChestContainer), new TranslatableText("container.enderchest")));

        player.incrementStat(Stats.OPEN_ENDERCHEST);
    }

    public static ActionFactory<Entity> getFactory() {
        return new ActionFactory<Entity>(OriginsPowerExpansion.identifier("ender_chest"),
            new SerializableData(),
            EnderChestAction::action
        );
    }
}
