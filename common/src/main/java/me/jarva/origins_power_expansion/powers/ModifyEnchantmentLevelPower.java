package me.jarva.origins_power_expansion.powers;

import io.github.apace100.origins.component.OriginComponent;
import io.github.apace100.origins.power.PowerType;
import io.github.apace100.origins.power.ValueModifyingPower;
import io.github.apace100.origins.power.factory.PowerFactory;
import io.github.apace100.origins.util.SerializableData;
import io.github.apace100.origins.util.SerializableDataType;
import me.jarva.origins_power_expansion.OriginsPowerExpansion;
import me.jarva.origins_power_expansion.access.ItemStackEntity;
import me.jarva.origins_power_expansion.util.ModComponents;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/** md
---
title: Modify Enchantment Level (Power Type)
date: 2021-10-08
---
# Modify Enchantment Level

[Power Type](../)

Modifies the entities enchantment levels.

Type ID: `ope:modify_enchantment_level`

### Fields

{{build_field_table(
    "enchantment|data_types/string||ID of the enchantment to modify",
    "modifier|data_types/attribute_modifier||This modifier will be applied to the enchantment."
)}}

### Example
```json
{
    "type": "ope:modify_enchantment_level",
    "enchantment": "infinity",
    "modifier": {
      "operation": "addition",
      "value": 1
    }
}
```
This would cause the holder to have the infinity enchantment permanently. You can use a condition to ensure the held item has fire aspect on it already to amplify existing effects.
 */
@SuppressWarnings({"UnstableApiUsage", "deprecation"})
public class ModifyEnchantmentLevelPower extends ValueModifyingPower {
    private static final ConcurrentHashMap<String, ConcurrentHashMap<ListTag, ListTag>> entityItemEnchants = new ConcurrentHashMap<>();
    private final Enchantment enchantment;

    public ModifyEnchantmentLevelPower(PowerType<?> type, PlayerEntity player, Enchantment enchantment) {
        super(type, player);
        this.enchantment = enchantment;
    }

    public Enchantment getEnchantment() {
        return this.enchantment;
    }

    public boolean doesApply(Enchantment enchantment) {
        return enchantment.equals(this.enchantment);
    }

    private static Optional<Integer> findEnchantIndex(Identifier id, ListTag enchants) {
        for (int i = 0; i < enchants.size(); ++i) {
            String string = enchants.getCompound(i).getString("id");
            Identifier enchantId = Identifier.tryParse(string);
            if (enchantId != null && enchantId.equals(id)) {
                return Optional.of(i);
            }
        }
        return Optional.empty();
    }

    public static ListTag generateEnchantments(ListTag enchants, ItemStack self) {
        Entity entity = ((ItemStackEntity) (Object) self).getEntity();

        if (!(entity instanceof PlayerEntity)) return enchants;

        OriginComponent oc = ModComponents.getOriginComponent((PlayerEntity) entity);
        if (oc == null) return enchants;

        ListTag newEnchants = enchants.copy();
        List<ModifyEnchantmentLevelPower> powers = oc.getPowers(ModifyEnchantmentLevelPower.class);

        for (ModifyEnchantmentLevelPower power : powers) {
            Identifier id = Registry.ENCHANTMENT.getId(power.getEnchantment());
            Optional<Integer> idx = findEnchantIndex(id, newEnchants);
            if (idx.isPresent()) {
                CompoundTag existingEnchant = newEnchants.getCompound(idx.get());
                int lvl = existingEnchant.getInt("lvl");
                int newLvl = (int) OriginComponent.modify(entity, ModifyEnchantmentLevelPower.class, lvl, powerFilter -> powerFilter.doesApply(power.getEnchantment()));
                existingEnchant.putInt("lvl", newLvl);
                newEnchants.setTag(idx.get(), existingEnchant);
            } else {
                CompoundTag newEnchant = new CompoundTag();
                newEnchant.putString("id", id.toString());
                newEnchant.putInt("lvl", (int) OriginComponent.modify(entity, ModifyEnchantmentLevelPower.class, 0, powerFilter -> powerFilter.doesApply(power.getEnchantment())));
                newEnchants.add(newEnchant);
            }
        };
        return newEnchants;
    }

    public static void updateEnchantments(ItemStack self) {
        Entity entity = ((ItemStackEntity) (Object) self).getEntity();
        if (entity == null) return;
        ConcurrentHashMap<ListTag, ListTag> itemEnchants = entityItemEnchants.computeIfAbsent(entity.getUuidAsString(), (_uuid) -> new ConcurrentHashMap<>());
        ListTag tag = self.getEnchantments();
        ListTag enchantments = ModifyEnchantmentLevelPower.generateEnchantments(tag, self);
        itemEnchants.put(tag, enchantments);
    }

    public static ListTag getEnchantments(ItemStack self) {
        Entity entity = ((ItemStackEntity) (Object) self).getEntity();
        if (entity == null) return self.getEnchantments();
        ConcurrentHashMap<ListTag, ListTag> itemEnchants = entityItemEnchants.computeIfAbsent(entity.getUuidAsString(), (_uuid) -> new ConcurrentHashMap<>());
        ListTag enchants = itemEnchants.computeIfAbsent(self.getEnchantments(), (tag) -> ModifyEnchantmentLevelPower.generateEnchantments(tag, self));
        return enchants;
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
