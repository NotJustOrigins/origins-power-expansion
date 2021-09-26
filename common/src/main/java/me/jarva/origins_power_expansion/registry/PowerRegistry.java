package me.jarva.origins_power_expansion.registry;

import dev.architectury.injectables.annotations.ExpectPlatform;
import io.github.apace100.origins.power.factory.PowerFactory;
import io.github.apace100.origins.util.SerializableDataType;
import me.jarva.origins_power_expansion.powers.ActionOnEquip;
import me.jarva.origins_power_expansion.powers.CustomFootstepPower;
import me.jarva.origins_power_expansion.powers.MobsIgnorePower;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EntityType;
import java.util.List;

@SuppressWarnings({"unchecked", "UnstableApiUsage", "deprecation"})
public class PowerRegistry {
    public static void register() {
        register(MobsIgnorePower.getFactory());
        register(CustomFootstepPower.getFactory());
        register(ActionOnEquip.getFactory());
    }

    @ExpectPlatform
    private static void register(PowerFactory<?> serializer) {
        throw new AssertionError();
    }
}
