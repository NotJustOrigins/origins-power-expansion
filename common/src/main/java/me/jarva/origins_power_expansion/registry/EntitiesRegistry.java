package me.jarva.origins_power_expansion.registry;

import dev.architectury.injectables.annotations.ExpectPlatform;
import me.jarva.origins_power_expansion.OriginsPowerExpansion;
import me.jarva.origins_power_expansion.entity.CustomProjectile;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class EntitiesRegistry {
    public static final EntityType<CustomProjectile> CUSTOM_PROJECTILE_ENTITY = EntityType.Builder.<CustomProjectile>of(CustomProjectile::new, MobCategory.MISC)
            .sized(0.25F, 0.25F)
            .clientTrackingRange(64)
            .updateInterval(10)
            .build("custom_projectile");

    public static void register() {
        OriginsPowerExpansion.LOGGER.info("Registering entity!");
        register(CUSTOM_PROJECTILE_ENTITY, OriginsPowerExpansion.identifier("custom_projectile"));
    }

    @ExpectPlatform
    private static void register(EntityType<?> entityType, ResourceLocation resource) {
        throw new AssertionError();
    }
}
