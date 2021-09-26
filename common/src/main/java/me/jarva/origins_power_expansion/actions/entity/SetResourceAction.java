package me.jarva.origins_power_expansion.actions.entity;

import io.github.apace100.origins.component.OriginComponent;
import io.github.apace100.origins.power.CooldownPower;
import io.github.apace100.origins.power.Power;
import io.github.apace100.origins.power.VariableIntPower;
import io.github.apace100.origins.power.factory.action.ActionFactory;
import io.github.apace100.origins.registry.ModComponentsArchitectury;
import io.github.apace100.origins.util.SerializableData;
import io.github.apace100.origins.util.SerializableDataType;
import me.jarva.origins_power_expansion.OriginsPowerExpansion;
import me.jarva.origins_power_expansion.util.ModComponents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;

@SuppressWarnings({"unchecked", "UnstableApiUsage", "deprecation"})
public class SetResourceAction {
    public static void action(SerializableData.Instance data, Entity entity) {
        if (!(entity instanceof PlayerEntity)) return;
        PlayerEntity player = (PlayerEntity) entity;
        OriginComponent component = ModComponents.getOriginComponent(player);
        Power p = component.getPower(data.get("resource"));
        int value = data.get("value");
        if (p instanceof VariableIntPower) {
            VariableIntPower vip = (VariableIntPower) p;
            vip.setValue(value);
            OriginComponent.sync(player);
        } else if (p instanceof CooldownPower) {
            CooldownPower cdp = (CooldownPower) p;
            cdp.setCooldown(value);
            OriginComponent.sync(player);
        }
    }

    public static ActionFactory<Entity> getFactory() {
        return new ActionFactory<Entity>(OriginsPowerExpansion.identifier("set_resource"),
                new SerializableData()
                        .add("resource", SerializableDataType.POWER_TYPE)
                        .add("value", SerializableDataType.INT),
                SetResourceAction::action
        );
    }
}
