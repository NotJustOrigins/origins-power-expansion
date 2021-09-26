package me.jarva.origins_power_expansion.powers;

import io.github.apace100.origins.power.Power;
import io.github.apace100.origins.power.PowerType;
import io.github.apace100.origins.power.factory.PowerFactory;
import io.github.apace100.origins.util.SerializableData;
import io.github.apace100.origins.util.SerializableDataType;
import me.jarva.origins_power_expansion.OriginsPowerExpansion;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import java.util.function.Consumer;
import java.util.function.Predicate;

@SuppressWarnings({"UnstableApiUsage", "deprecation"})
public class ActionOnEquip extends Power {

    private final Consumer<LivingEntity> action;
    private final Predicate<ItemStack> item_condition;
    private final EquipmentSlot slot;

    public ActionOnEquip(PowerType<?> type, PlayerEntity player, EquipmentSlot slot, Consumer<LivingEntity> action, Predicate<ItemStack> item_condition) {
        super(type, player);
        this.slot = slot;
        this.action = action;
        this.item_condition = item_condition;
    }

    public void onEquipStack(EquipmentSlot equipmentSlot, ItemStack stack) {
        if (!equipmentSlot.equals(slot)) return;

        boolean shouldExecute = item_condition == null || item_condition.test(stack);
        if (!shouldExecute) return;

        action.accept(player);
    }

    public static PowerFactory<?> getFactory() {
        return new PowerFactory<ActionOnEquip>(
                OriginsPowerExpansion.identifier("action_on_equip"),
                new SerializableData()
                        .add("slot", SerializableDataType.EQUIPMENT_SLOT)
                        .add("action", SerializableDataType.ENTITY_ACTION)
                        .add("item_condition", SerializableDataType.ITEM_CONDITION, null),
                data -> (type, player) -> new ActionOnEquip(type, player, data.get("slot"), data.get("action"), data.get("item_condition")))
                .allowCondition();
    }
}
