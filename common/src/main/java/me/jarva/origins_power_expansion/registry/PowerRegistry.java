package me.jarva.origins_power_expansion.registry;

import dev.architectury.injectables.annotations.ExpectPlatform;
import io.github.apace100.origins.power.factory.PowerFactory;
import me.jarva.origins_power_expansion.powers.CustomFootstepPower;
import me.jarva.origins_power_expansion.powers.MobsIgnorePower;

@SuppressWarnings({"unchecked", "UnstableApiUsage", "deprecation"})
public class PowerRegistry {
    public static void register() {
        register(MobsIgnorePower.getFactory());
        register(CustomFootstepPower.getFactory());
    }

    @ExpectPlatform
    private static void register(PowerFactory<?> serializer) {
        throw new AssertionError();
    }
}
