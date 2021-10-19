package me.jarva.origins_power_expansion.powers;

import io.github.apace100.origins.power.PowerType;
import io.github.apace100.origins.power.ValueModifyingPower;
import io.github.apace100.origins.power.factory.PowerFactory;
import io.github.apace100.origins.util.SerializableData;
import io.github.apace100.origins.util.SerializableDataType;
import me.jarva.origins_power_expansion.OriginsPowerExpansion;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.player.PlayerEntity;

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
