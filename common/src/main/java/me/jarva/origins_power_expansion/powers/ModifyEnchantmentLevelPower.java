package me.jarva.origins_power_expansion.powers;

import io.github.apace100.origins.power.PowerType;
import io.github.apace100.origins.power.ValueModifyingPower;
import io.github.apace100.origins.power.factory.PowerFactory;
import io.github.apace100.origins.util.SerializableData;
import io.github.apace100.origins.util.SerializableDataType;
import me.jarva.origins_power_expansion.OriginsPowerExpansion;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.PlayerEntity;

@SuppressWarnings({"UnstableApiUsage", "deprecation"})
public class ModifyEnchantmentLevelPower extends ValueModifyingPower {
    private final Enchantment enchantment;

    public ModifyEnchantmentLevelPower(PowerType<?> type, PlayerEntity player, Enchantment enchantment) {
        super(type, player);
        this.enchantment = enchantment;
    }

    public boolean doesApply(Enchantment enchantment) {
        return enchantment.equals(this.enchantment);
    }

    public static PowerFactory<?> getFactory() {
        return new PowerFactory<ModifyEnchantmentLevelPower>(
                OriginsPowerExpansion.identifier("modify_enchantment_level"),
                new SerializableData()
                        .add("enchantment", SerializableDataType.ENCHANTMENT)
                        .add("modifier", SerializableDataType.ATTRIBUTE_MODIFIER),
                data -> (type, player) -> {
                    ModifyEnchantmentLevelPower modifyEnchantmentLevelPower = new ModifyEnchantmentLevelPower(type, player, data.get("enchantment"));
                    modifyEnchantmentLevelPower.addModifier(data.get("modifier"));
                    return modifyEnchantmentLevelPower;
                })
                .allowCondition();
    }
}
