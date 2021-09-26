package me.jarva.origins_power_expansion.actions.entity;


import io.github.apace100.origins.power.factory.action.ActionFactory;
import io.github.apace100.origins.util.SerializableData;
import io.github.apace100.origins.util.SerializableDataType;
import me.jarva.origins_power_expansion.OriginsPowerExpansion;
import net.minecraft.block.Block;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Triple;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

@SuppressWarnings({"unchecked", "UnstableApiUsage", "deprecation"})
public class RaycastAction {
    public static void action(SerializableData.Instance data, Entity entity) {
        double distance = data.isPresent("distance") ? data.get("distance") : 4.5D;
        Vec3d eyePosition = entity.getCameraPosVec(0);
        Vec3d lookVector = entity.getRotationVec(0);
        Vec3d traceEnd = eyePosition.add(lookVector.x * distance, lookVector.y * distance, lookVector.z * distance);

        RaycastContext context = new RaycastContext(eyePosition, traceEnd, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.ANY, entity);
        BlockHitResult blockHitResult = entity.world.raycast(context);
        EntityHitResult entityHitResult = ProjectileUtil.raycast(entity, eyePosition, traceEnd, Box.method_29968(eyePosition), (traceEntity) -> !traceEntity.isSpectator() && traceEntity.collides(), distance);

        HitResult.Type blockHitResultType = blockHitResult.getType();
        HitResult.Type entityHitResultType = Objects.requireNonNull(entityHitResult).getType();
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
