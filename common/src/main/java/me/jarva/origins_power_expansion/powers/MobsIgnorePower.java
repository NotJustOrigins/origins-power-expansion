package me.jarva.origins_power_expansion.powers;

import io.github.apace100.origins.power.Power;
import io.github.apace100.origins.power.PowerType;
import io.github.apace100.origins.power.factory.PowerFactory;
import io.github.apace100.origins.util.SerializableData;
import io.github.apace100.origins.util.SerializableDataType;
import me.jarva.origins_power_expansion.OriginsPowerExpansion;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

import java.util.HashSet;
import java.util.function.Predicate;

/** md
---
title: Mob Ignore (Power Type)
date: 2021-10-08
---
# Mob Ignore

[Power Type](../)

Makes mobs ignore the power holder.

Type ID: `ope:mob_ignore`

### Fields

{{build_field_table(
    "mob_condition|entity_conditions||Which entities should ignore the player"
)}}

### Example
```json
{
    "type": "ope:mob_ignore",
    "mob_condition": {
        "type": "origins:entity_type",
        "entity_type": "minecraft:skeleton"
    }
}
```
This power cause skeletons to ignore the player.
 */
@SuppressWarnings({"UnstableApiUsage", "deprecation"})
public class MobsIgnorePower extends Power {
    private final HashSet<EntityType<?>> mobTypes = new HashSet<>();
    private final Predicate<LivingEntity> mobCondition;

    public MobsIgnorePower(PowerType<?> type, PlayerEntity player, Predicate<LivingEntity> mobCondition) {
        super(type, player);
        this.mobCondition = mobCondition;
    }

    public boolean shouldIgnore(LivingEntity mob) {
        return this.mobCondition.test(mob);
    }

    public static PowerFactory<?> getFactory() {
        return new PowerFactory<MobsIgnorePower>(
                OriginsPowerExpansion.identifier("mobs_ignore"),
                new SerializableData()
                        .add("mob_condition", SerializableDataType.ENTITY_CONDITION),
                data -> (type, player) -> new MobsIgnorePower(type, player, data.get("mob_condition")))
                .allowCondition();
    }
}
