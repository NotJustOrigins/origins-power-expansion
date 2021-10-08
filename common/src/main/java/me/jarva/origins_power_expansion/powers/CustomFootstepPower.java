package me.jarva.origins_power_expansion.powers;

import io.github.apace100.origins.power.Power;
import io.github.apace100.origins.power.PowerType;
import io.github.apace100.origins.power.factory.PowerFactory;
import io.github.apace100.origins.util.SerializableData;
import io.github.apace100.origins.util.SerializableDataType;
import me.jarva.origins_power_expansion.OriginsPowerExpansion;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvent;

/** md
---
title: Custom Footstep (Power Type)
date: 2021-10-08
---
# Custom Footstep

[Power Type](../)

Sets a sound to play on each entity footstep.

Type ID: `ope:custom_footstep`

### Fields

{{build_field_table(
    "muted|data_types/boolean|false|Mutes default footstep sounds",
    "sound|data_types/identifier|_optional_|Sound to play on each footstep",
    "volume|data_types/float|1.0|Volume of the sound",
    "pitch|data_types/float|1.0|Pitch of the sound"
)}}

### Example
```json
{
    "type": "ope:custom_footstep",
    "sound": ""
}
```
This power would play the iron golem footstep sound on every step.
 */
@SuppressWarnings({"UnstableApiUsage", "deprecation"})
public class CustomFootstepPower extends Power {
    private final SoundEvent footstepSound;
    private final Boolean muted;
    private final float pitch;
    private final float volume;

    public static PowerFactory<?> getFactory() {
        return new PowerFactory<CustomFootstepPower>(
            OriginsPowerExpansion.identifier("custom_footstep"),
            new SerializableData()
                .add("muted", SerializableDataType.BOOLEAN, false)
                .add("sound", SerializableDataType.SOUND_EVENT, null)
                .add("volume", SerializableDataType.FLOAT, 1F)
                .add("pitch", SerializableDataType.FLOAT, 1F),
            data -> (type, player) -> {
                return new CustomFootstepPower(type, player, data.get("muted"), data.<SoundEvent>get("sound"), data.getFloat("volume"), data.getFloat("pitch"));
            })
            .allowCondition();
    }

    public CustomFootstepPower(PowerType<?> type, PlayerEntity player, Boolean muted, SoundEvent footstepSound, float volume, float pitch){
        super(type, player);
        this.muted = muted;
        this.footstepSound = footstepSound;
        this.pitch = pitch;
        this.volume = volume;
    }

    public Boolean isMuted() {
        return muted;
    }

    public void playFootstep(Entity entity) {
        if (!this.muted) entity.playSound(this.footstepSound, this.volume, this.pitch);
    }
}
