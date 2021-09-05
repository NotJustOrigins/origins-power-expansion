package me.jarva.origins_power_expansion.actions.entity;

import io.github.apace100.origins.power.factory.action.ActionFactory;
import io.github.apace100.origins.util.SerializableData;
import io.github.apace100.origins.util.SerializableDataType;
import me.jarva.origins_power_expansion.OriginsPowerExpansion;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

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

        for (LivingEntity check : entity.level.getEntitiesOfClass(LivingEntity.class, AABB.ofSize(diameter, diameter, diameter).move(entity.getPosition(1F)))) {
            if (check == entity && !includeTarget)
                continue;
            if (predicate.test(check) && check.distanceToSqr(entity) < radius * radius)
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
