package me.jarva.origins_power_expansion.powers;

import io.github.apace100.origins.power.Power;
import io.github.apace100.origins.power.PowerType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class MobsIgnorePower extends Power {

    private final HashSet<EntityType<?>> mobTypes = new HashSet<>();
    private final HashSet<MobType> mobGroups = new HashSet<>();

    public MobsIgnorePower(PowerType<?> type, Player player) {
        super(type, player);
    }

    public void addMobType(EntityType<?> mobType) {
        mobTypes.add(mobType);
    }

    public void addMobGroup(MobType mobGroup) {
        mobGroups.add(mobGroup);
    }

    public void addAllMobGroups(List<MobType> newMobGroups) {
        mobGroups.addAll(newMobGroups);
    }

    public void addAllMobTypes(List<EntityType<?>> newMobTypes) {
        mobTypes.addAll(newMobTypes);
    }

    public boolean containsMobGroup(MobType mobGroup) {
        return mobGroups.contains(mobGroup);
    }

    public boolean containsMobType(EntityType<?> entityType) {
        return mobTypes.contains(entityType);
    }
}
