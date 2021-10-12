package me.jarva.origins_power_expansion.registry;

import dev.architectury.injectables.annotations.ExpectPlatform;
import io.github.apace100.origins.power.factory.PowerFactory;
import me.jarva.origins_power_expansion.powers.CustomFootstepPower;
import me.jarva.origins_power_expansion.powers.MobsIgnorePower;
import me.jarva.origins_power_expansion.powers.ModifyEnchantmentLevelPower;
import me.jarva.origins_power_expansion.powers.PreventLabelRenderPower;

@SuppressWarnings({"unchecked", "UnstableApiUsage", "deprecation"})
public class PowerRegistry {
    public static void register() {
        register(ModifyEnchantmentLevelPower.getFactory());
        register(CustomFootstepPower.getFactory());
        register(MobsIgnorePower.getFactory());
        register(PreventLabelRenderPower.getFactory());
    }

    @ExpectPlatform
    private static void register(PowerFactory<?> serializer) {
        throw new AssertionError();
    }
}
