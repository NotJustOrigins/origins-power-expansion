package me.jarva.origins_power_expansion.mixin;

import io.github.apace100.origins.component.OriginComponent;
import me.jarva.origins_power_expansion.access.StatusEffectInstanceHiddenEffect;
import me.jarva.origins_power_expansion.powers.ModifyEnchantmentLevelPower;
import me.jarva.origins_power_expansion.powers.ModifyStatusEffectAmplifierPower;
import me.jarva.origins_power_expansion.powers.ModifyStatusEffectDurationPower;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @ModifyVariable(method = "addStatusEffect", at = @At("HEAD"))
    private StatusEffectInstance modifyStatusEffect(StatusEffectInstance effect) {
        StatusEffect effectType = effect.getEffectType();
        int originalAmp = effect.getAmplifier();
        int originalDur = effect.getDuration();

        int amplifier = Math.round(OriginComponent.modify(this, ModifyStatusEffectAmplifierPower.class, originalAmp, power -> power.doesApply(effectType)));
        int duration = Math.round(OriginComponent.modify(this, ModifyStatusEffectDurationPower.class, originalDur, power -> power.doesApply(effectType)));

        if (amplifier != originalAmp || duration != originalDur) {
            return new StatusEffectInstance(
                    effectType,
                    duration,
                    amplifier,
                    effect.isAmbient(),
                    effect.shouldShowParticles(),
                    effect.shouldShowIcon(),
                    ((StatusEffectInstanceHiddenEffect) effect).getHiddenEffect()
            );
        }
        return effect;
    }
}
