package me.jarva.origins_power_expansion.powers.factory;

import dev.architectury.injectables.annotations.ExpectPlatform;
import io.github.apace100.origins.power.Active;
import io.github.apace100.origins.power.factory.PowerFactory;
import io.github.apace100.origins.util.HudRender;
import io.github.apace100.origins.util.SerializableData;
import io.github.apace100.origins.util.SerializableDataType;
import me.jarva.origins_power_expansion.OriginsPowerExpansion;
import me.jarva.origins_power_expansion.powers.CreativeFlightPower;
import me.jarva.origins_power_expansion.powers.CustomFootstepPower;
import me.jarva.origins_power_expansion.powers.FireCustomProjectilePower;
import me.jarva.origins_power_expansion.powers.MobsIgnorePower;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

@SuppressWarnings({"unchecked", "UnstableApiUsage", "deprecation"})
public class PowerFactories {
    public static final SerializableDataType<List<EntityType<?>>> ENTITY_TYPES;
    public static final SerializableDataType<List<MobType>> ENTITY_GROUPS;

    static {
        ENTITY_TYPES = SerializableDataType.list(SerializableDataType.ENTITY_TYPE);
        ENTITY_GROUPS = SerializableDataType.list(SerializableDataType.ENTITY_GROUP);
    }

    public static void register() {
        register(new PowerFactory<CreativeFlightPower>(OriginsPowerExpansion.identifier("creative_flight"),
                new SerializableData()
                        .add("modifier", SerializableDataType.ATTRIBUTE_MODIFIER, null)
                        .add("modifiers", SerializableDataType.ATTRIBUTE_MODIFIERS, null),
                data -> (type, player) -> {
                        CreativeFlightPower power = new CreativeFlightPower(type, player);
                        if (data.isPresent("modifier")) {
                            power.addModifier(data.get("modifier"));
                        }
                        if (data.isPresent("modifiers")) {
                            data.<List<AttributeModifier>>get("modifiers").forEach(power::addModifier);
                        }
                        return power;
                    })
            .allowCondition());

        register(new PowerFactory<>(OriginsPowerExpansion.identifier("mobs_ignore"),
                new SerializableData()
                    .add("mobs", ENTITY_TYPES, null)
                    .add("mob", SerializableDataType.ENTITY_TYPE, null)
                    .add("groups", ENTITY_GROUPS, null)
                    .add("group", SerializableDataType.ENTITY_GROUP, null),
                data -> (type, player) -> {
                    List<EntityType<?>> mobs = data.isPresent("mobs") ? data.get("mobs") : new ArrayList<EntityType<?>>();
                    if (data.isPresent("mob")) {
                        mobs.add(data.get("mob"));
                    }

                    List<MobType> groups = data.isPresent("groups") ? data.get("groups") : new ArrayList<MobType>();
                    if (data.isPresent("group")) {
                        groups.add(data.get("group"));
                    }

                    MobsIgnorePower power = new MobsIgnorePower(type, player);
                    power.addAllMobTypes(mobs);
                    power.addAllMobGroups(groups);

                    return power;
                }
        )
        .allowCondition());

        register(new PowerFactory<FireCustomProjectilePower>(OriginsPowerExpansion.identifier("fire_custom_projectile"),
                new SerializableData()
                    .add("cooldown", SerializableDataType.INT)
                    .add("hit_entity_condition", SerializableDataType.ENTITY_CONDITION, null)
                    .add("hit_entity_action", SerializableDataType.ENTITY_ACTION, null)
                    .add("hit_entity_self_action", SerializableDataType.ENTITY_ACTION, null)
                    .add("hit_block_condition", SerializableDataType.BLOCK_CONDITION, null)
                    .add("hit_block_action", SerializableDataType.BLOCK_ACTION, null)
                    .add("hit_block_self_action", SerializableDataType.ENTITY_ACTION, null)
                    .add("custom_item", SerializableDataType.STRING, null)
                    .add("count", SerializableDataType.INT, 1)
                    .add("speed", SerializableDataType.FLOAT, 1.5F)
                    .add("divergence", SerializableDataType.FLOAT, 1F)
                    .add("sound", SerializableDataType.SOUND_EVENT, null)
                    .add("hud_render", SerializableDataType.HUD_RENDER)
                    .add("tag", SerializableDataType.NBT, null)
                    .add("key", SerializableDataType.BACKWARDS_COMPATIBLE_KEY, new Active.Key()),
                data ->
                    (type, player) -> {
                        CompoundTag mergedTag = new CompoundTag();
                        CompoundTag tag = data.get("tag");
                        String customItem = data.get("custom_item");
                        if (tag != null) {
                            mergedTag.merge(tag);
                        }
                        if (customItem != null) {
                            CompoundTag item = new CompoundTag();
                            item.putString("id", customItem);
                            item.putByte("Count", (byte) 1);
                            mergedTag.put("Item", item);
                        }
                        FireCustomProjectilePower power = new FireCustomProjectilePower(type, player,
                            data.<Predicate>get("hit_entity_condition"),
                            data.<Predicate>get("hit_block_condition"),
                            data.getInt("cooldown"),
                            data.<HudRender>get("hud_render"),
                            data.getInt("count"),
                            data.getFloat("speed"),
                            data.getFloat("divergence"),
                            data.<SoundEvent>get("sound"),
                            mergedTag);
                        if (data.isPresent("hit_block_action")) {
                            power.setTargetBlockAction(data.<Consumer>get("hit_block_action"));
                        }
                        if (data.isPresent("hit_entity_action")) {
                            power.setTargetEntityAction(data.<Consumer>get("hit_entity_action"));
                        }
                        if (data.isPresent("hit_block_self_action")) {
                            power.setSelfBlockAction(data.<Consumer>get("hit_block_self_action"));
                        }
                        if (data.isPresent("hit_entity_self_action")) {
                            power.setSelfEntityAction(data.<Consumer>get("hit_entity_self_action"));
                        }
                        power.setKey(data.<Active.Key>get("key"));
                        return power;
                    }
                ));

        register(new PowerFactory<CustomFootstepPower>(OriginsPowerExpansion.identifier("custom_footstep"),
                new SerializableData()
                    .add("sound", SerializableDataType.SOUND_EVENT)
                    .add("volume", SerializableDataType.FLOAT, 1F)
                    .add("pitch", SerializableDataType.FLOAT, 1F),
                data -> (type, player) -> {
                    return new CustomFootstepPower(type, player, data.<SoundEvent>get("sound"), data.getFloat("volume"), data.getFloat("pitch"));
                })
                .allowCondition());
    }

    @ExpectPlatform
    private static void register(PowerFactory<?> serializer) {
        throw new AssertionError();
    }
}
