package me.jarva.origins_power_expansion.actions.entity;

import io.github.apace100.origins.power.factory.action.ActionFactory;
import io.github.apace100.origins.util.SerializableData;
import io.github.apace100.origins.util.SerializableDataType;
import me.jarva.origins_power_expansion.OriginsPowerExpansion;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.Vec3;

@SuppressWarnings({"unchecked", "UnstableApiUsage", "deprecation"})
public class FireProjectileAction {
    public static void action(SerializableData.Instance data, Entity entity) {
        if (data.isPresent("sound")) {
            entity.level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), data.get("sound"), SoundSource.NEUTRAL, 0.5F, 0.4F / (((LivingEntity) entity).getRandom().nextFloat() * 0.4F + 0.8F));
        }
        for (int i = 0; i < data.getInt("count"); ++i) {
            fireProjectile(data, (LivingEntity) entity);
        }
    }
    
    private static void fireProjectile(SerializableData.Instance data, LivingEntity entity) {
        if (!data.isPresent("entity_type")) return;
        Entity firedEntity = data.<EntityType>get("entity_type").create(entity.level);
        if (firedEntity == null) return;

        Vec3 rotationVector = entity.getLookAngle();
        Vec3 spawnPos = entity.position().add(0.0D, entity.getEyeHeight(), 0.0D).add(rotationVector);
        firedEntity.moveTo(spawnPos.x(), spawnPos.y(), spawnPos.z(), entity.xRot, entity.yRot);

        if (firedEntity instanceof Projectile) {
            if (firedEntity instanceof AbstractHurtingProjectile) {
                AbstractHurtingProjectile abstractHurtingProjectile = (AbstractHurtingProjectile) firedEntity;
                abstractHurtingProjectile.xPower = rotationVector.x * data.getDouble("speed");
                abstractHurtingProjectile.yPower = rotationVector.y * data.getDouble("speed");
                abstractHurtingProjectile.zPower = rotationVector.z * data.getDouble("speed");
            }

            Projectile projectile = (Projectile) firedEntity;
            projectile.setOwner(entity);
            projectile.shootFromRotation(entity, entity.xRot, entity.yRot, 0.0F, data.getFloat("speed"), data.getFloat("divergence"));
        } else {
            float f = -Mth.sin(entity.yRot * 0.017453292F) * Mth.cos(entity.xRot * 0.017453292F);
            float g = -Mth.sin(entity.xRot * 0.017453292F);
            float h = Mth.cos(entity.yRot * 0.017453292F) * Mth.cos(entity.xRot * 0.017453292F);

            Vec3 vec3d = new Vec3(f, g, h).normalize()
                    .add(
                            entity.getRandom().nextGaussian() * 0.007499999832361937D * data.getDouble("divergence"),
                            entity.getRandom().nextGaussian() * 0.007499999832361937D * data.getDouble("divergence"),
                            entity.getRandom().nextGaussian() * 0.007499999832361937D * data.getDouble("divergence")
                    ).scale(data.getDouble("speed"));
            firedEntity.setDeltaMovement(vec3d);
            Vec3 entityVelo = entity.getDeltaMovement();
            firedEntity.setDeltaMovement(firedEntity.getDeltaMovement().add(entityVelo.x, entity.isOnGround() ? 0.0D : entityVelo.y, entityVelo.z));
        }

        if (data.isPresent("tag")) {
            CompoundTag mergedTag = firedEntity.saveWithoutId(new CompoundTag());
            mergedTag.merge(data.get("tag"));
            firedEntity.load(mergedTag);
        }

        entity.level.addFreshEntity(firedEntity);
    }

    public static ActionFactory<Entity> getFactory() {
        return new ActionFactory<Entity>(OriginsPowerExpansion.identifier("fire_projectile"),
                new SerializableData()
                    .add("count", SerializableDataType.INT, 1)
                    .add("speed", SerializableDataType.FLOAT, 1.5F)
                    .add("divergence", SerializableDataType.FLOAT, 1F)
                    .add("sound", SerializableDataType.SOUND_EVENT, null)
                    .add("entity_type", SerializableDataType.ENTITY_TYPE)
                    .add("tag", SerializableDataType.NBT, null),
                FireProjectileAction::action
        );
    }
}
