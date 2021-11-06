package me.jarva.origins_power_expansion.powers;

import io.github.apace100.origins.power.PowerType;
import io.github.apace100.origins.power.ValueModifyingPower;
import io.github.apace100.origins.power.factory.PowerFactory;
import io.github.apace100.origins.util.SerializableData;
import io.github.apace100.origins.util.SerializableDataType;
import me.jarva.origins_power_expansion.OriginsPowerExpansion;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.player.PlayerEntity;

/** md
---
title: Modify Status Effect Amplifier (Power Type)
date: 2021-10-08
---
# Modify Status Effect Amplifier

[Power Type](../)

Modifies an amplifier level for a status effect.

Type ID: `ope:modify_status_effect_amplifier`

### Fields

{{build_field_table(
"status_effect|data_types/identifier||Status effect to modify",
"modifier|data_types/modifier_operation||Modifier to apply to the status effect amplifier"
)}}

### Example
```json
{
    "type": "ope:modify_status_effect_amplifier",
    "status_effect": "minecraft:strength",
    "modifier": {
        "operation": "addition",
        "value": 1
    }
}
```
This power would increase the amplifier of the strength status effect whenever you receive it.
 */
@SuppressWarnings({"UnstableApiUsage", "deprecation"})
public class ModifyStatusEffectAmplifierPower extends ValueModifyingPower {
    private final StatusEffect statusEffect;

    public ModifyStatusEffectAmplifierPower(PowerType<?> type, PlayerEntity player, StatusEffect statusEffect) {
        super(type, player);
        this.statusEffect = statusEffect;
    }

    public boolean doesApply(StatusEffect statusEffect) {
        return statusEffect.equals(this.statusEffect);
    }

    public static PowerFactory<?> getFactory() {
        return new PowerFactory<ModifyStatusEffectAmplifierPower>(
                OriginsPowerExpansion.identifier("modify_status_effect_amplifier"),
                new SerializableData()
                        .add("status_effect", SerializableDataType.STATUS_EFFECT)
                        .add("modifier", SerializableDataType.ATTRIBUTE_MODIFIER),
                data -> (type, player) -> {
                    ModifyStatusEffectAmplifierPower modifyStatusEffectAmplifierPower = new ModifyStatusEffectAmplifierPower(type, player, data.get("status_effect"));
                    modifyStatusEffectAmplifierPower.addModifier(data.get("modifier"));
                    return modifyStatusEffectAmplifierPower;
                })
                .allowCondition();
    }
}
