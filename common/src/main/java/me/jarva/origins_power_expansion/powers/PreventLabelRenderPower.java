package me.jarva.origins_power_expansion.powers;

import io.github.apace100.origins.power.Power;
import io.github.apace100.origins.power.PowerType;
import io.github.apace100.origins.power.factory.PowerFactory;
import io.github.apace100.origins.util.SerializableData;
import io.github.apace100.origins.util.SerializableDataType;
import me.jarva.origins_power_expansion.OriginsPowerExpansion;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

import java.util.function.Predicate;

/** md
 ---
 title: Prevent Label Render (Power Type)
 date: 2021-10-11
 ---
 # Prevent Label Render

 [Power Type](../)

 Prevents the players label from rendering

 Type ID: `ope:prevent_label_render`

 ### Fields

 {{build_field_table(
 "entity_condition|entity_conditions|_optional_|Which entities shouldn't see the label"
 )}}

 ### Example
 ```json
 {
    "type": "ope:prevent_label_render"
 }
 ```
 This power causes a players label to not render for anyone.
 */
@SuppressWarnings({"UnstableApiUsage", "deprecation"})
public class PreventLabelRenderPower extends Power {
    private final Predicate<LivingEntity> entityCondition;

    public PreventLabelRenderPower(PowerType<?> type, PlayerEntity player, Predicate<LivingEntity> entityPredicate) {
        super(type, player);
        this.entityCondition = entityPredicate;
    }

    public boolean shouldHide(LivingEntity entity) {
        return this.entityCondition == null || this.entityCondition.test(entity);
    }

    public static PowerFactory<?> getFactory() {
        return new PowerFactory<PreventLabelRenderPower>(
                OriginsPowerExpansion.identifier("prevent_label_render"),
                new SerializableData()
                        .add("entity_condition", SerializableDataType.ENTITY_CONDITION, null),
                data -> (type, player) -> new PreventLabelRenderPower(type, player, data.get("mob_condition")))
                .allowCondition();
    }
}
