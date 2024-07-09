package me.jarva.origins_power_expansion.actions.entity;


import io.github.apace100.origins.power.factory.action.ActionFactory;
import io.github.apace100.origins.util.SerializableData;
import io.github.apace100.origins.util.SerializableDataType;
import me.jarva.origins_power_expansion.OriginsPowerExpansion;
import me.jarva.origins_power_expansion.util.PlayerReach;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Triple;

import java.util.function.Consumer;
import java.util.function.Predicate;

/** md
---
title: Raycast (Entity Action)
date: 2021-10-08
---

# Raycast

[Entity Action](../)

Fires a projectile from the entity.

Type ID: `ope:raycast`

### Fields

{{build_field_table(
"distance|data_types/integer|Player Reach|The distance for the raycast to travel.",
"block_action|block_actions|_optional_|Action to fire when a valid block is hit.",
"block_condition|block_conditions|_optional_|Condition to compare to the block hit.",
"target_action|entity_actions|_optional_|Action to fire when a valid entity is hit.",
"target_condition|entity_conditions|_optional_|Condition to compare to the entity hit.",
"self_action|entity_actions|_optional_|Action to fire on yourself when the raycast hits anything."
)}}

### Example
```json
"entity_action": {
    "type": "ope:raycast",
    "block_action": {
        "type": "ope:bonemeal"
    },
    "target_action": {
        "type": "origins:set_on_fire",
         "duration": 8
    }
}
```
 */
@SuppressWarnings({"unchecked", "UnstableApiUsage", "deprecation"})
public class RaycastAction {
    public static void action(SerializableData.Instance data, Entity entity) {
        if (!(entity instanceof PlayerEntity)) return;
        double distance = data.isPresent("distance") ? data.get("distance") : PlayerReach.getReach((PlayerEntity) entity);
        Vec3d eyePosition = entity.getCameraPosVec(0);
        Vec3d lookVector = entity.getRotationVec(0);
        Vec3d traceEnd = eyePosition.add(lookVector.x * distance, lookVector.y * distance, lookVector.z * distance);
        Box box = entity.getBoundingBox().stretch(lookVector).expand(distance);

        RaycastContext context = new RaycastContext(eyePosition, traceEnd, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.ANY, entity);
        BlockHitResult blockHitResult = entity.world.raycast(context);

        double reach = blockHitResult != null ? blockHitResult.getBlockPos().getSquaredDistance(eyePosition.x, eyePosition.y, eyePosition.z, true) : distance * distance;
        EntityHitResult entityHitResult = ProjectileUtil.raycast(entity, eyePosition, traceEnd, box, (traceEntity) -> !traceEntity.isSpectator() && traceEntity.collides(), reach);

        HitResult.Type blockHitResultType = blockHitResult.getType();
        HitResult.Type entityHitResultType = (entityHitResult != null) ? entityHitResult.getType() : null;
        if (blockHitResultType == HitResult.Type.MISS && entityHitResultType == HitResult.Type.MISS) return;

        if (blockHitResultType == HitResult.Type.BLOCK) {
            RaycastAction.onHitBlock(data, entity, blockHitResult);
        }

        if (entityHitResultType == HitResult.Type.ENTITY) {
            RaycastAction.onHitEntity(data, entity, entityHitResult);
        }
    }

    private static void fireSelfAction(SerializableData.Instance data, Entity entity) {
        if (!data.isPresent("self_action") || !entity.isAlive()) return;
        Consumer<LivingEntity> selfAction = data.get("self_action");

        selfAction.accept((LivingEntity) entity);
    }

    private static void onHitBlock(SerializableData.Instance data, Entity entity, BlockHitResult result) {
        if (!data.isPresent("block_action")) return;
        CachedBlockPosition blockPosition = new CachedBlockPosition(entity.world, result.getBlockPos(), true);

        boolean blockCondition = !data.isPresent("block_condition") || data.<Predicate<CachedBlockPosition>>get("block_condition").test(blockPosition);
        if (!blockCondition) return;

        Consumer<Triple<World, BlockPos, Direction>> blockAction = data.get("block_action");
        blockAction.accept(Triple.of(entity.world, result.getBlockPos(), result.getSide()));

        fireSelfAction(data, entity);
    }

    private static void onHitEntity(SerializableData.Instance data, Entity entity, EntityHitResult result) {
        if (!data.isPresent("target_action")) return;
        LivingEntity targetEntity = (LivingEntity) result.getEntity();

        boolean targetCondition = !data.isPresent("target_condition") || data.<Predicate<LivingEntity>>get("target_condition").test(targetEntity);
        if(!targetCondition) return;

        Consumer<LivingEntity> targetAction = data.get("target_action");
        targetAction.accept(targetEntity);

        fireSelfAction(data, entity);
    }

    public static ActionFactory<Entity> getFactory() {
        return new ActionFactory<>(OriginsPowerExpansion.identifier("raycast"),
                new SerializableData()
                    .add("distance", SerializableDataType.DOUBLE, null)
                    .add("block_action", SerializableDataType.BLOCK_ACTION, null)
                    .add("block_condition", SerializableDataType.BLOCK_CONDITION, null)
                    .add("target_action", SerializableDataType.ENTITY_ACTION, null)
                    .add("target_condition", SerializableDataType.ENTITY_CONDITION, null)
                    .add("self_action", SerializableDataType.ENTITY_ACTION, null),
                RaycastAction::action
        );
    }
}
