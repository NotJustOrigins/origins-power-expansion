package me.jarva.origins_power_expansion.actions.item;

import io.github.apace100.origins.power.factory.action.ActionFactory;
import io.github.apace100.origins.util.SerializableData;
import io.github.apace100.origins.util.SerializableDataType;
import me.jarva.origins_power_expansion.OriginsPowerExpansion;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.enchantment.UnbreakingEnchantment;
import net.minecraft.item.ItemStack;

import java.util.Random;

/** md
---
title: Damage (Item Action)
date: 2021-10-08
---

# Damage

[Item Action](../)

Damages the item stack.

Type ID: `ope:damage`

### Fields

{{build_field_table(
"amount|data_types/integer|1|The amount of damage to apply to the item. (Can be negative)",
"ignore_unbreaking|data_types/boolean|false|Whether positive damage should ignore the unbreaking enchantment.",
)}}

### Example
```json
"entity_action": {
    "type": "ope:damage",
    "amount": 10
}
```
*/
@SuppressWarnings({"unchecked", "UnstableApiUsage", "deprecation"})
public class DamageAction {
    public static void action(SerializableData.Instance data, ItemStack itemStack) {
        int amount = data.getInt("amount");
        int damage = amount;
        if (amount > 0 && !data.getBoolean("ignore_unbreaking")) {
            int levels = EnchantmentHelper.getLevel(Enchantments.UNBREAKING, itemStack);
            Random random = new Random();
            for (int i = 0; i < amount; i++) {
                if (UnbreakingEnchantment.shouldPreventDamage(itemStack, levels, random)) {
                    damage--;
                }
            }
        }
        int newDamage = itemStack.getDamage() + damage;
        itemStack.setDamage(newDamage);
        if (newDamage >= itemStack.getMaxDamage()) {
            itemStack.decrement(1);
            itemStack.setDamage(0);
        }
    }

    public static ActionFactory<ItemStack> getFactory() {
        return new ActionFactory<>(OriginsPowerExpansion.identifier("damage"),
                new SerializableData()
                    .add("amount", SerializableDataType.INT, 1)
                    .add("ignore_unbreaking", SerializableDataType.BOOLEAN, false),
                DamageAction::action
        );
    }
}
