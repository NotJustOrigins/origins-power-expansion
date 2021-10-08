package me.jarva.origins_power_expansion.actions.entity;

import io.github.apace100.origins.component.OriginComponent;
import io.github.apace100.origins.power.CooldownPower;
import io.github.apace100.origins.power.Power;
import io.github.apace100.origins.power.VariableIntPower;
import io.github.apace100.origins.power.factory.action.ActionFactory;
import io.github.apace100.origins.util.SerializableData;
import io.github.apace100.origins.util.SerializableDataType;
import me.jarva.origins_power_expansion.OriginsPowerExpansion;
import me.jarva.origins_power_expansion.util.ModComponents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;

/** md
---
title: Set Resource (Entity Action)
date: 2021-10-08
---

# Set Resource

[Entity Action](../)

Set a resource for an entity.

Type ID: `ope:set_resource`

### Fields

{{build_field_table(
    "resource|data_types/identifier||ID of the power type that defines the resource. Must be a resource which exists on the player.",
    "value|data_types/integer||The resource will be set to this value, it won't go below `min` or above `max` of the resource.",
)}}

### Example
```json
"entity_action": {
    "type": "ope:set_resource",
    "resource": "namespace:example",
    "value": 5
}
```
*/
@SuppressWarnings({"unchecked", "UnstableApiUsage", "deprecation"})
public class SetResourceAction {
    public static void action(SerializableData.Instance data, Entity entity) {
        if (!(entity instanceof PlayerEntity)) return;
        PlayerEntity player = (PlayerEntity) entity;
        OriginComponent component = ModComponents.getOriginComponent(player);
        Power p = component.getPower(data.get("resource"));
        int value = data.get("value");
        if (p instanceof VariableIntPower) {
            VariableIntPower vip = (VariableIntPower) p;
            vip.setValue(value);
            OriginComponent.sync(player);
        } else if (p instanceof CooldownPower) {
            CooldownPower cdp = (CooldownPower) p;
            cdp.setCooldown(value);
            OriginComponent.sync(player);
        }
    }

    public static ActionFactory<Entity> getFactory() {
        return new ActionFactory<Entity>(OriginsPowerExpansion.identifier("set_resource"),
                new SerializableData()
                        .add("resource", SerializableDataType.POWER_TYPE)
                        .add("value", SerializableDataType.INT),
                SetResourceAction::action
        );
    }
}
