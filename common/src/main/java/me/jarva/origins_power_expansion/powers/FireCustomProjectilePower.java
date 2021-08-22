package me.jarva.origins_power_expansion.powers;

import io.github.apace100.origins.power.FireProjectilePower;
import io.github.apace100.origins.power.PowerType;
import io.github.apace100.origins.util.HudRender;
import me.jarva.origins_power_expansion.entity.CustomProjectile;
import me.jarva.origins_power_expansion.registry.EntitiesRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import org.apache.commons.lang3.tuple.Triple;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class FireCustomProjectilePower extends FireProjectilePower {
    private final Predicate<LivingEntity> hitEntityCondition;
    private Consumer<LivingEntity> hitEntityAction;
    private Consumer<LivingEntity> hitEntitySelfAction;
    private final Predicate<BlockInWorld> hitBlockCondition;
    private Consumer<Triple<Level, BlockPos, Direction>> hitBlockAction;
    private Consumer<LivingEntity> hitBlockSelfAction;

    public FireCustomProjectilePower(
        PowerType<?> type,
        Player player,
        Predicate<LivingEntity> hitEntityCondition,
        Predicate<BlockInWorld> hitBlockCondition,
        int cooldownDuration,
        HudRender hudRender,
        int projectileCount,
        float speed,
        float divergence,
        SoundEvent soundEvent,
        CompoundTag tag
    ) {
        super(type, player, cooldownDuration, hudRender, EntitiesRegistry.CUSTOM_PROJECTILE_ENTITY, projectileCount, speed, divergence, soundEvent, tag);
        this.hitEntityCondition = hitEntityCondition;
        this.hitBlockCondition = hitBlockCondition;
    }

    public void setTargetEntityAction(Consumer<LivingEntity> targetAction) {
        this.hitEntityAction = targetAction;
    }

    public void setTargetBlockAction(Consumer<Triple<Level, BlockPos, Direction>> targetAction) {
        this.hitBlockAction = targetAction;
    }

    public void setSelfBlockAction(Consumer<LivingEntity> targetAction) {
        this.hitBlockSelfAction = targetAction;
    }

    public void setSelfEntityAction(Consumer<LivingEntity> targetAction) {
        this.hitEntitySelfAction = targetAction;
    }

    public void onHitBlock(CustomProjectile customProjectile, BlockHitResult result) {
        BlockInWorld cbp = new BlockInWorld(customProjectile.level, result.getBlockPos(), true);
        if (this.hitEntityCondition == null || this.hitBlockCondition.test(cbp)) {
            if (this.hitBlockAction != null) {
                this.hitBlockAction.accept(Triple.of(
                    customProjectile.level,
                    result.getBlockPos(),
                    result.getDirection()
                ));
            }
            if (this.hitBlockSelfAction != null) {
                if (this.player.isAlive()) {
                    this.hitBlockSelfAction.accept(this.player);
                }
            }
        }
    }

    public void onHitEntity(EntityHitResult hitResult) {
        LivingEntity entity = (LivingEntity) hitResult.getEntity();
        if (this.hitEntityCondition == null || this.hitEntityCondition.test(entity)) {
            if (this.hitEntityAction != null) {
                this.hitEntityAction.accept(entity);
            }
            if (this.hitEntitySelfAction != null) {
                if (this.player.isAlive()) {
                    this.hitEntitySelfAction.accept(this.player);
                }
            }
        }
    }
}
