package me.jarva.origins_power_expansion.actions.entity;

import io.github.apace100.origins.power.factory.action.ActionFactory;
import io.github.apace100.origins.util.SerializableData;
import io.github.apace100.origins.util.SerializableDataType;
import me.jarva.origins_power_expansion.OriginsPowerExpansion;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ExplosiveProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

/** md
---
title: Fire Projectile (Entity Action)
date: 2021-10-08
---

# Fire Projectile

[Entity Action](../)

Fires a projectile from the entity.

Type ID: `ope:fire_projectile`

### Fields

{{build_field_table(
    "entity_type|data_types/identifier||The ID of the entity type that will be fired.",
    "cooldown|data_types/integer||The number of ticks the player has to wait between uses of this power.",
    "hud_render|data_types/hud_render||Specifies how and if a cooldown bar is rendered.",
    "count|data_types/integer|1|The amount of projectiles to fire each use.",
    "speed|data_types/float|1.5|The speed applied to the fired projectile.",
    "divergence|data_types/float|1.0|How much each projectile fired is affected by random spread.",
    "sound|data_types/identifier|_optional_|If set, the sound with this ID will be played when the power is used.",
    "tag|data_types/string|_optional_|NBT data of the entity."
)}}

### Example
```json
"entity_action": {
    "type": "ope:fire_projectile",
    "entity_type": "minecraft:arrow",
    "cooldown": 2,
    "hud_render": {
        "should_render": false
    },
    "tag": "{pickup:0b}",
    "key": {
        "key": "key.attack",
        "continuous": true
    }
}
```
*/
@SuppressWarnings({"unchecked", "UnstableApiUsage", "deprecation"})
public class FireProjectileAction {
    public static void action(SerializableData.Instance data, Entity entity) {
        if (data.isPresent("sound")) {
            entity.world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), data.get("sound"), SoundCategory.NEUTRAL, 0.5F, 0.4F / (((LivingEntity) entity).getRandom().nextFloat() * 0.4F + 0.8F));
        }
        for (int i = 0; i < data.getInt("count"); ++i) {
            fireProjectile(data, (LivingEntity) entity);
        }
    }
    
    private static void fireProjectile(SerializableData.Instance data, LivingEntity entity) {
        if (!data.isPresent("entity_type")) return;
        Entity firedEntity = data.<EntityType>get("entity_type").create(entity.world);
        if (firedEntity == null) return;

        Vec3d rotationVector = entity.getRotationVector();
        Vec3d spawnPos = entity.getPos().add(0.0D, entity.getStandingEyeHeight(), 0.0D).add(rotationVector);
        firedEntity.refreshPositionAndAngles(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(), entity.pitch, entity.yaw);

        if (firedEntity instanceof ProjectileEntity) {
            if (firedEntity instanceof ExplosiveProjectileEntity) {
                ExplosiveProjectileEntity abstractHurtingProjectile = (ExplosiveProjectileEntity) firedEntity;
                abstractHurtingProjectile.posX = rotationVector.x * data.getDouble("speed");
                abstractHurtingProjectile.posY = rotationVector.y * data.getDouble("speed");
                abstractHurtingProjectile.posZ = rotationVector.z * data.getDouble("speed");
            }

            ProjectileEntity projectile = (ProjectileEntity) firedEntity;
            projectile.setOwner(entity);
            projectile.setProperties(entity, entity.pitch, entity.yaw, 0.0F, data.getFloat("speed"), data.getFloat("divergence"));
        } else {
            float f = -MathHelper.sin(entity.yaw * 0.017453292F) * MathHelper.cos(entity.pitch * 0.017453292F);
            float g = -MathHelper.sin(entity.pitch * 0.017453292F);
            float h = MathHelper.cos(entity.yaw * 0.017453292F) * MathHelper.cos(entity.pitch * 0.017453292F);

            Vec3d vec3d = new Vec3d(f, g, h).normalize()
                    .add(
                            entity.getRandom().nextGaussian() * 0.007499999832361937D * data.getDouble("divergence"),
                            entity.getRandom().nextGaussian() * 0.007499999832361937D * data.getDouble("divergence"),
                            entity.getRandom().nextGaussian() * 0.007499999832361937D * data.getDouble("divergence")
                    ).multiply(data.getDouble("speed"));
            firedEntity.setVelocity(vec3d);
            Vec3d entityVelo = entity.getVelocity();
            firedEntity.setVelocity(firedEntity.getVelocity().add(entityVelo.x, entity.isOnGround() ? 0.0D : entityVelo.y, entityVelo.z));
        }

        if (data.isPresent("tag")) {
            CompoundTag mergedTag = firedEntity.toTag(new CompoundTag());
            mergedTag.copyFrom(data.get("tag"));
            firedEntity.fromTag(mergedTag);
        }

        entity.world.spawnEntity(firedEntity);
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
