package me.jarva.origins_power_expansion.actions.entity;

import io.github.apace100.origins.power.factory.action.ActionFactory;
import io.github.apace100.origins.util.SerializableData;
import io.github.apace100.origins.util.SerializableDataType;
import me.jarva.origins_power_expansion.OriginsPowerExpansion;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Box;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

/** md
---
title: Fire Projectile (Entity Action)
date: 2021-10-08
---

# Fire Projectile

[Entity Action](../)

Fires a projectile from the entity.

Type ID: `ope:fire_projectile`

### Fields

{{build_field_table(
    "radius|data_types/integer|16|The block radius to apply to the effects in.",
    "action|entity_actions||The action to apply to every entity within the radius.",
    "condition|entity_conditions|_optional_|The condition for whether to include an entity for the action.",
    "include_target|data_types/boolean|false|Whether to include the entity themselves when applying the action."
)}}

### Example
```json
"entity_action": {
    "type": "ope:area_of_effect",
    "radius": 32,
    "action": {
        "type": "origins:set_on_fire",
        "duration": 8
    },
    "include_target": true
}
```
This action will set everyone within 32 blocks of the entity on fire, including the entity themselves.
*/
@SuppressWarnings({"unchecked", "UnstableApiUsage", "deprecation"})
public class AreaOfEffectAction {
    public static void action(SerializableData.Instance data, Entity entity) {
        List<Consumer<Entity>> actions = new ArrayList<>();

        if (data.isPresent("action")) {
            actions.add(data.get("action"));
        }

        if (data.isPresent("actions")) {
            actions.addAll(data.get("actions"));
        }

        Predicate<LivingEntity> predicate = data.isPresent("condition") ? data.get("condition") : x -> true;
        boolean includeTarget = data.get("include_target");
        double radius = data.get("radius");
        double diameter = radius * 2;

        for (LivingEntity check : entity.world.getNonSpectatingEntities(LivingEntity.class, Box.method_30048(diameter, diameter, diameter).offset(entity.method_30950(1F)))) {
            if (check == entity && !includeTarget)
                continue;
            if (predicate.test(check) && check.squaredDistanceTo(entity) < radius * radius)
                actions.forEach(x -> x.accept(check));
        }
    }

    public static ActionFactory<Entity> getFactory() {
        return new ActionFactory<Entity>(OriginsPowerExpansion.identifier("area_of_effect"),
            new SerializableData()
                .add("radius", SerializableDataType.DOUBLE, 16D)
                .add("action", SerializableDataType.ENTITY_ACTION, null)
                .add("actions", SerializableDataType.ENTITY_ACTIONS, null)
                .add("condition", SerializableDataType.ENTITY_CONDITION, null)
                .add("include_target", SerializableDataType.BOOLEAN, false),
                AreaOfEffectAction::action
        );
    }
}
