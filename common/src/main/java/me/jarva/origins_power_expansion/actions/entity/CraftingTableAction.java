package me.jarva.origins_power_expansion.actions.entity;

import io.github.apace100.origins.power.factory.action.ActionFactory;
import io.github.apace100.origins.util.SerializableData;
import me.jarva.origins_power_expansion.OriginsPowerExpansion;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

/** md
---
title: Crafting Table (Entity Action)
date: 2021-10-08
---

# Crafting Table

[Entity Action](../)

Opens a crafting table for the entity.

Type ID: `ope:crafting_table`

### Example
```json
"entity_action": {
    "type": "ope:crafting_table"
}
```
*/
@SuppressWarnings({"unchecked", "UnstableApiUsage", "deprecation"})
public class CraftingTableAction {
    private static final Text TITLE = new TranslatableText("container.crafting");

    public static void action(SerializableData.Instance data, Entity entity) {
        if (!(entity instanceof PlayerEntity)) return;

        PlayerEntity player = (PlayerEntity) entity;

        player.openHandledScreen(
                new SimpleNamedScreenHandlerFactory((i, inventory, _player) ->
                    new CraftingScreenHandler(i, inventory),
                    TITLE
                )
        );

        player.incrementStat(Stats.INTERACT_WITH_CRAFTING_TABLE);
    }

    public static ActionFactory<Entity> getFactory() {
        return new ActionFactory<>(OriginsPowerExpansion.identifier("crafting_table"),
                new SerializableData(),
                CraftingTableAction::action
        );
    }
}
