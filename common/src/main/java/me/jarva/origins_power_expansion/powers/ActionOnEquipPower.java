package me.jarva.origins_power_expansion.powers;

import io.github.apace100.origins.power.Power;
import io.github.apace100.origins.power.PowerType;
import io.github.apace100.origins.power.factory.PowerFactory;
import io.github.apace100.origins.util.SerializableData;
import io.github.apace100.origins.util.SerializableDataType;
import me.jarva.origins_power_expansion.OriginsPowerExpansion;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.function.Consumer;
import java.util.function.Predicate;

/** md
 ---
 title: Action On Equip (Power Type)
 date: 2021-10-08
 ---
 # Action On Equip

 [Power Type](../)

 Fires an action when an item is equipped.

 Type ID: `ope:action_on_equip`

 ### Fields

 {{build_field_table(
 "head|item_conditions||Item condition to match the `head` item.",
 "chest|item_conditions||Item condition to match the `chest` item.",
 "legs|item_conditions||Item condition to match the `legs` item.",
 "feet|item_conditions||Item condition to match the `feet` item.",
 "offhand|item_conditions||Item condition to match the `offhand` item.",
 "action|entity_actions||Action to apply when the matching item is equipped."
 )}}

 ### Example
 ```json
 {
     "type": "ope:action_on_equip",
     "head": {
         "type": "origins:ingredient",
         "ingredient": {
            "item": "minecraft:netherite_helmet"
         }
     },
     "action": {
         "type": "origins:heal",
         "amount": 6
     }
 }
 ```
 This power would heal you for 3 hearts when you equip a netherite helmet.
 */
@SuppressWarnings({"UnstableApiUsage", "deprecation"})
public class ActionOnEquipPower extends Power {
    private final HashMap<EquipmentSlot, Predicate<ItemStack>> armorConditions;
    private final Consumer<Entity> armorAction;

    public ActionOnEquipPower(PowerType<?> type, PlayerEntity player, HashMap<EquipmentSlot, Predicate<ItemStack>> armorConditions, Consumer<Entity> entityAction) {
        super(type, player);
        this.armorConditions = armorConditions;
        this.armorAction = entityAction;
    }

    public void fireAction(EquipmentSlot slot, ItemStack stack) {
        if (armorConditions == null) {
            armorAction.accept(this.player);
            return;
        }
        if (armorConditions.get(slot) == null) return;

        if (armorConditions.get(slot).test(stack)) {
            armorAction.accept(this.player);
        }
    }

    public static PowerFactory<?> getFactory() {
        return new PowerFactory<ActionOnEquipPower>(
                OriginsPowerExpansion.identifier("action_on_equip"),
                new SerializableData()
                        .add("head", SerializableDataType.ITEM_CONDITION)
                        .add("chest", SerializableDataType.ITEM_CONDITION)
                        .add("legs", SerializableDataType.ITEM_CONDITION)
                        .add("feet", SerializableDataType.ITEM_CONDITION)
                        .add("offhand", SerializableDataType.ITEM_CONDITION)
                        .add("action", SerializableDataType.ENTITY_ACTION),
                data -> (type, player) -> {
                    HashMap<EquipmentSlot, Predicate<ItemStack>> conditions = new HashMap<>();

                    if(data.isPresent("head")) {
                        conditions.put(EquipmentSlot.HEAD, data.get("head"));
                    }
                    if(data.isPresent("chest")) {
                        conditions.put(EquipmentSlot.CHEST, data.get("chest"));
                    }
                    if(data.isPresent("legs")) {
                        conditions.put(EquipmentSlot.LEGS, data.get("legs"));
                    }
                    if(data.isPresent("feet")) {
                        conditions.put(EquipmentSlot.FEET, data.get("feet"));
                    }
                    if(data.isPresent("offhand")) {
                        conditions.put(EquipmentSlot.OFFHAND, data.get("offhand"));
                    }

                    return new ActionOnEquipPower(type, player, conditions, data.get("action"));
                }
        ).allowCondition();
    }
}
