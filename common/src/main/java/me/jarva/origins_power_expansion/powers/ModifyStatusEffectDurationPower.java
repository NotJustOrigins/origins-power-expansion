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
 title: Modify Status Effect Duration (Power Type)
 date: 2021-10-08
 ---
 # Modify Status Effect Duration

 [Power Type](../)

 Modifies a duration level for a status effect.

 Type ID: `ope:modify_status_effect_duration`

 ### Fields

 {{build_field_table(
 "status_effect|data_types/identifier||Status effect to modify",
 "modifier|data_types/modifier_operation||Modifier to apply to the status effect duration"
 )}}

 ### Example
 ```json
 {
     "type": "ope:modify_status_effect_duration",
     "status_effect": "minecraft:strength",
     "modifier": {
         "operation": "multiply_base",
         "value": 1
     }
 }
 ```
 This power would increase the duration of any strength effect you receive by double.
 */
@SuppressWarnings({"UnstableApiUsage", "deprecation"})
public class ModifyStatusEffectDurationPower extends ValueModifyingPower {
    private final StatusEffect statusEffect;

    public ModifyStatusEffectDurationPower(PowerType<?> type, PlayerEntity player, StatusEffect statusEffect) {
        super(type, player);
        this.statusEffect = statusEffect;
    }

    public boolean doesApply(StatusEffect statusEffect) {
        return statusEffect.equals(this.statusEffect);
    }

    public static PowerFactory<?> getFactory() {
        return new PowerFactory<ModifyStatusEffectDurationPower>(
                OriginsPowerExpansion.identifier("modify_status_effect_duration"),
                new SerializableData()
                        .add("status_effect", SerializableDataType.STATUS_EFFECT)
                        .add("modifier", SerializableDataType.ATTRIBUTE_MODIFIER),
                data -> (type, player) -> {
                    ModifyStatusEffectDurationPower modifyStatusEffectAmplifierPower = new ModifyStatusEffectDurationPower(type, player, data.get("status_effect"));
                    modifyStatusEffectAmplifierPower.addModifier(data.get("modifier"));
                    return modifyStatusEffectAmplifierPower;
                })
                .allowCondition();
    }
}
