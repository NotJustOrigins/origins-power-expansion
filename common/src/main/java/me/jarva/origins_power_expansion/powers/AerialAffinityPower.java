package me.jarva.origins_power_expansion.powers;

import io.github.apace100.origins.power.Power;
import io.github.apace100.origins.power.PowerType;
import io.github.apace100.origins.power.factory.PowerFactory;
import io.github.apace100.origins.util.SerializableData;
import me.jarva.origins_power_expansion.OriginsPowerExpansion;
import net.minecraft.entity.player.PlayerEntity;

/** md
---
title: Aerial Affinity (Power Type)
date: 2021-10-08
---
# Aerial Affinity

[Power Type](../)

Applies air affinity which allows you to break blocks when not touching the ground without delay.

Type ID: `ope:aerial_affinity`

### Example
```json
{
    "type": "ope:aerial_affinity",
}
```
 */
public class AerialAffinityPower extends Power {
    public AerialAffinityPower(PowerType<?> type, PlayerEntity player) {
        super(type, player);
    }

    public static PowerFactory<?> getFactory() {
        return new PowerFactory<AerialAffinityPower>(
                OriginsPowerExpansion.identifier("aerial_affinity"),
                new SerializableData(),
                data -> AerialAffinityPower::new
        ).allowCondition();
    }
}
