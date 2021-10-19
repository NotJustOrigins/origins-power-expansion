package me.jarva.origins_power_expansion.registry;

import dev.architectury.injectables.annotations.ExpectPlatform;
import io.github.apace100.origins.power.factory.PowerFactory;
import me.jarva.origins_power_expansion.powers.*;

public class PowerRegistry {
    public static void register() {
        register(ModifyEnchantmentLevelPower.getFactory());
        register(CustomFootstepPower.getFactory());
        register(MobsIgnorePower.getFactory());
        register(PreventLabelRenderPower.getFactory());
        register(AerialAffinityPower.getFactory());
        register(ModifyStatusEffectDurationPower.getFactory());
        register(ModifyStatusEffectAmplifierPower.getFactory());
        register(ActionOnEquipPower.getFactory());
    }

    @ExpectPlatform
    private static void register(PowerFactory<?> serializer) {
        throw new AssertionError();
    }
}
