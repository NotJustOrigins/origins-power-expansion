package me.jarva.origins_power_expansion.powers;

import io.github.apace100.origins.power.Power;
import io.github.apace100.origins.power.PowerType;
import io.github.apace100.origins.power.factory.PowerFactory;
import io.github.apace100.origins.util.SerializableData;
import io.github.apace100.origins.util.SerializableDataType;
import me.jarva.origins_power_expansion.OriginsPowerExpansion;
import me.jarva.origins_power_expansion.registry.PowerRegistry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@SuppressWarnings({"UnstableApiUsage", "deprecation"})
public class MobsIgnorePower extends Power {
    private final HashSet<EntityType<?>> mobTypes = new HashSet<>();
    private final HashSet<MobType> mobGroups = new HashSet<>();

    public MobsIgnorePower(PowerType<?> type, Player player) {
        super(type, player);
    }

    public HashSet<EntityType<?>> getMobTypes() {
        return mobTypes;
    }

    public HashSet<MobType> getMobGroups() {
        return mobGroups;
    }

    public static PowerFactory<?> getFactory() {
        return new PowerFactory<MobsIgnorePower>(
                OriginsPowerExpansion.identifier("mobs_ignore"),
                new SerializableData()
                        .add("mobs", PowerRegistry.ENTITY_TYPES, null)
                        .add("mob", SerializableDataType.ENTITY_TYPE, null)
                        .add("groups", PowerRegistry.ENTITY_GROUPS, null)
                        .add("group", SerializableDataType.ENTITY_GROUP, null),
                data -> (type, player) -> {
                    MobsIgnorePower power = new MobsIgnorePower(type, player);

                    List<EntityType<?>> mobs = data.isPresent("mobs") ? data.get("mobs") : new ArrayList<EntityType<?>>();
                    if (data.isPresent("mob")) {
                        mobs.add(data.get("mob"));
                    }
                    power.getMobTypes().addAll(mobs);

                    List<MobType> groups = data.isPresent("groups") ? data.get("groups") : new ArrayList<MobType>();
                    if (data.isPresent("group")) {
                        groups.add(data.get("group"));
                    }
                    power.getMobGroups().addAll(groups);

                    return power;
                })
                .allowCondition();
    }
}
