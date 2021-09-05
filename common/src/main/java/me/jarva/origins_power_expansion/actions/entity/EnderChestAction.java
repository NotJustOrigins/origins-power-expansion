package me.jarva.origins_power_expansion.actions.entity;

import io.github.apace100.origins.power.factory.action.ActionFactory;
import io.github.apace100.origins.util.SerializableData;
import me.jarva.origins_power_expansion.OriginsPowerExpansion;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.stats.Stats;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.PlayerEnderChestContainer;

@SuppressWarnings({"unchecked", "UnstableApiUsage", "deprecation"})
public class EnderChestAction {
    public static void action(SerializableData.Instance data, Entity entity) {
        if (!(entity instanceof Player)) return;

        Player player = (Player) entity;
        PlayerEnderChestContainer enderChestContainer = player.getEnderChestInventory();

        player.openMenu(new SimpleMenuProvider((i, inventory, playerx) ->
                ChestMenu.threeRows(i, inventory, enderChestContainer), new TranslatableComponent("container.enderchest")));

        player.awardStat(Stats.OPEN_ENDERCHEST);
    }

    public static ActionFactory<Entity> getFactory() {
        return new ActionFactory<Entity>(OriginsPowerExpansion.identifier("ender_chest"),
            new SerializableData(),
            EnderChestAction::action
        );
    }
}
