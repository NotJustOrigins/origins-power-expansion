package me.jarva.origins_power_expansion.registry;

import dev.architectury.injectables.annotations.ExpectPlatform;
import io.github.apace100.origins.power.factory.PowerFactory;
import io.github.apace100.origins.util.SerializableDataType;
import me.jarva.origins_power_expansion.powers.CustomFootstepPower;
import me.jarva.origins_power_expansion.powers.MobsIgnorePower;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EntityType;
import java.util.List;

@SuppressWarnings({"unchecked", "UnstableApiUsage", "deprecation"})
public class PowerRegistry {
    public static final SerializableDataType<List<EntityType<?>>> ENTITY_TYPES;
    public static final SerializableDataType<List<EntityGroup>> ENTITY_GROUPS;

    static {
        ENTITY_TYPES = SerializableDataType.list(SerializableDataType.ENTITY_TYPE);
        ENTITY_GROUPS = SerializableDataType.list(SerializableDataType.ENTITY_GROUP);
    }

    public static void register() {
        register(MobsIgnorePower.getFactory());
        register(CustomFootstepPower.getFactory());
    }

    @ExpectPlatform
    private static void register(PowerFactory<?> serializer) {
        throw new AssertionError();
    }
}
